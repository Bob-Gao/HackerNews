package net.bobgao.ycombinatorhackernews.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import net.bobgao.ycombinatorhackernews.Constants;
import net.bobgao.ycombinatorhackernews.HNExecutorService;
import net.bobgao.ycombinatorhackernews.NetworkHelper;
import net.bobgao.ycombinatorhackernews.R;
import net.bobgao.ycombinatorhackernews.adapters.CommentsListAdapter;
import net.bobgao.ycombinatorhackernews.models.BaseEntity;
import net.bobgao.ycombinatorhackernews.models.Comment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import cz.msebera.android.httpclient.Header;

/**
 * Created by bobgao on 27/4/16.
 */
public class CommentsActivity extends AppCompatActivity {

    private final String TAG = "YCHN";
    private static ReadWriteLock mLock = new ReentrantReadWriteLock();
    private static AsyncHttpClient mClient = new AsyncHttpClient();
    private static List<Integer> mProcessingRequest = new ArrayList<>();
    private static List<Integer> mProcessingReplyRequest = new ArrayList<>();
    protected static List<Comment> mComments = new ArrayList<>();
    protected ListView mListView;
    protected SwipeRefreshLayout swipeContainer;
    protected CommentsListAdapter mAdapter;
    protected int[] mKids;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.comments));
        setContentView(R.layout.activity_comments);
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        mKids = getIntent().getIntArrayExtra(BaseEntity.FIELD_KIDS);
        mListView = (ListView) findViewById(R.id.lvItems);
        mAdapter = new CommentsListAdapter(this, mComments);
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
                sendRequest();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        sendRequest();
    }

    private void sendRequest() {
        if (!NetworkHelper.isNetworkAvailable(this)) {
            Toast.makeText(this, "Network is unreachable.", Toast.LENGTH_LONG).show();
        } else {
            for (int id : mKids) {
                mLock.writeLock().lock();
                mProcessingRequest.add(id);
                mLock.writeLock().unlock();
            }
            for (int id : mProcessingRequest) {
                fetchCommentsAsync(id);
            }
        }
    }

    /**
     * Get comment details
     *
     * @param id
     */
    private void fetchCommentsAsync(final int id) {
        mClient.get(Constants.getCommentUrl(id), new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d(TAG, response.toString());
                try {
                    if (response.has(BaseEntity.FIELD_DELETED) && response.getBoolean(BaseEntity.FIELD_DELETED)) {
                        Log.d(TAG, "DELETED COMMENT");
                    } else {
                        Comment comment = new Comment(response);
                        if (comment.getKids().length > 0) {
                            List list = Arrays.asList(comment.getKids());
                            mLock.writeLock().lock();
                            mProcessingReplyRequest.addAll(list);
                            mLock.writeLock().unlock();
                        }
                        mComments.add(comment);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mLock.writeLock().lock();
                mProcessingRequest.remove(Integer.valueOf(id));
                mLock.writeLock().unlock();
                if (mProcessingRequest.isEmpty()) {
                    if (mProcessingReplyRequest.isEmpty()) {
                        arrangeData();
                    } else {
                        fetchReplyAsync();
                    }
                }
            }

        });
    }


    public void fetchReplyAsync() {
        for (final Comment comment : mComments) {
            for (int i = 0; i < comment.getKids().length; i++) {
                final int id = comment.getKids()[i];
                mClient.get(Constants.getCommentUrl(id), new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        Log.d(TAG, response.toString());
                        try {
                            if (response.has(BaseEntity.FIELD_DELETED) && response.getBoolean(BaseEntity.FIELD_DELETED)) {
                                Log.d(TAG, "DELETED COMMENT");
                            } else {
                                Comment reply = new Comment(response);
                                comment.getComments().add(reply);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFinish() {
                        mLock.writeLock().lock();
                        mProcessingReplyRequest.remove(Integer.valueOf(id));
                        if (mProcessingReplyRequest.isEmpty()) {
                            arrangeData();
                        }
                        mLock.writeLock().unlock();
                    }
                });
            }
        }
    }


    /**
     * Put comments and its replies together
     */
    public void arrangeData() {
        swipeContainer.setRefreshing(false);
        List<Comment> comments = new ArrayList();
        for (Comment comment : mComments) {
            comment.setLevel(0);
            comments.add(comment);
            for (Comment reply : comment.getComments()) {
                reply.setLevel(1);
                comments.add(reply);
            }
        }
        mAdapter.setItems(comments);
        mAdapter.notifyDataSetChanged();
    }


}
