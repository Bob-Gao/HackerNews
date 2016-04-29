package net.bobgao.ycombinatorhackernews.activities;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import net.bobgao.ycombinatorhackernews.Constants;
import net.bobgao.ycombinatorhackernews.HNExecutorService;
import net.bobgao.ycombinatorhackernews.NetworkHelper;
import net.bobgao.ycombinatorhackernews.R;
import net.bobgao.ycombinatorhackernews.adapters.StoryListAdapter;
import net.bobgao.ycombinatorhackernews.models.Story;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {


    private final String TAG = "YCHN";
    private static AsyncHttpClient mClient = new AsyncHttpClient();
    private static ReadWriteLock mLock = new ReentrantReadWriteLock();
    // Table items
    private static int loadedSize = 0;
    // Top story ids
    private static List<Integer> mTopStories = new ArrayList<>();
    private static boolean mLoading = false;
    // Data of list view
    protected List<Story> mStories = new ArrayList<>();
    private List<Integer> mProcessingRequest = new ArrayList<>();
    protected StoryListAdapter mAdapter;
    protected SwipeRefreshLayout swipeContainer;
    protected ListView mListView;
    protected View mFooterView;
    protected TextView mFooterTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(getString(R.string.popular));
        // Lookup the swipe container view
        mFooterView = ((LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.view_footer, null, false);
        mFooterTextView = (TextView) mFooterView.findViewById(R.id.footer_1);
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        mListView = (ListView) findViewById(R.id.lvItems);
        mAdapter = new StoryListAdapter(this, mStories);
        mListView.setAdapter(mAdapter);
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        // Setup refresh listener which triggers new data loading
        mClient.setThreadPool(HNExecutorService.THREAD_POOL_EXECUTOR);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchStoriesAsync();
            }
        });
        fetchStoriesAsync();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * Get top story ids
     */
    public void fetchStoriesAsync() {
        if (!NetworkHelper.isNetworkAvailable(this)) {
            Toast.makeText(this, "Network is unreachable.", Toast.LENGTH_LONG).show();
            return;
        }
        mLoading = true;
        mClient.get(Constants.getTopStoriesUrl(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.d(TAG, response.toString());
                try {
                    mProcessingRequest.clear();
                    mTopStories.clear();
                    mStories.clear();
                    loadedSize = 0;
                    for (int i = 0; i < response.length(); i++) {
                        int id = response.getInt(i);
                        mTopStories.add(id);
                        if (i < 10) {
                            mLock.writeLock().lock();
                            mProcessingRequest.add(id);
                            mLock.writeLock().unlock();
                        }
                    }
                    for (Integer id : mProcessingRequest) {
                        fetchStoryDetailAsync(id);
                    }
                } catch (JSONException e) {
                    mStories.clear();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(MainActivity.this, "Unable to retrieve data, please try again later.", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFinish() {
                swipeContainer.setRefreshing(false);
                mListView.addFooterView(mFooterView);
                // Implementing scroll refresh
                mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(AbsListView absListView, int i) {
                    }

                    @Override
                    public void onScroll(AbsListView absListView, int firstItem, int visibleItemCount, final int totalItems) {
                        int total = firstItem + visibleItemCount;
                        if (total == totalItems && !mLoading) {
                            mFooterTextView.setText(getText(R.string.loading));
                            fetchMoreStoriesAsync();
                        }
                    }
                });
            }
        });

    }


    /**
     * Load more stories
     */
    private void fetchMoreStoriesAsync() {
        if (!NetworkHelper.isNetworkAvailable(this)) {
            Toast.makeText(this, "Network is unreachable.", Toast.LENGTH_LONG).show();
            return;
        }
        mProcessingRequest.clear();
        for (int i = loadedSize; i < mTopStories.size(); i++) {
            int id = mTopStories.get(i);
            if (i < loadedSize + 10) {
                mLock.writeLock().lock();
                mProcessingRequest.add(id);
                mLock.writeLock().unlock();
            }
        }
        if (mProcessingRequest.isEmpty()) {
            mListView.removeFooterView(mFooterView);
        } else {
            mLoading = true;
            for (Integer id : mProcessingRequest) {
                fetchStoryDetailAsync(id);
            }
        }

    }


    /**
     * Get story details
     *
     * @param id
     */
    public void fetchStoryDetailAsync(final int id) {
        if (!NetworkHelper.isNetworkAvailable(this)) {
            Toast.makeText(this, "Network is unreachable.", Toast.LENGTH_LONG).show();
            return;
        }
        loadedSize++;
        mClient.get(Constants.getStoryUrl(id), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d(TAG, response.toString());
                try {
                    Story story = new Story(response);
                    mStories.add(story);
                    if (mProcessingRequest.isEmpty()) {
                        refreshTableList();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                this.sendCancelMessage();
            }

            @Override
            public void onFinish() {
                mLock.writeLock().lock();
                mProcessingRequest.remove(Integer.valueOf(id));
                mLock.writeLock().unlock();
                if (mProcessingRequest.isEmpty()) {
                    mFooterTextView.setText(getText(R.string.more_news));
                    swipeContainer.setRefreshing(false);
                    mLoading = false;
                }
            }
        });
    }

    protected void refreshTableList() {
        mAdapter.notifyDataSetChanged();
    }


}
