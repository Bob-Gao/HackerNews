package net.bobgao.ycombinatorhackernews.activities;

import android.content.Intent;
import android.test.InstrumentationTestCase;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import net.bobgao.ycombinatorhackernews.BuildConfig;
import net.bobgao.ycombinatorhackernews.Constants;
import net.bobgao.ycombinatorhackernews.MockInterceptor;
import net.bobgao.ycombinatorhackernews.R;
import net.bobgao.ycombinatorhackernews.models.BaseEntity;
import net.bobgao.ycombinatorhackernews.models.Comment;
import net.bobgao.ycombinatorhackernews.models.Story;
import net.bobgao.ycombinatorhackernews.util.FileUtil;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowApplication;
import org.robolectric.shadows.ShadowLog;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by bobgao on 29/4/16.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class MainActivityTest extends InstrumentationTestCase {

    private static final String JSON_ROOT_PATH = "/json/";
    private String jsonFullPath;
    protected OkHttpClient mOkHttpClient;

    @Before
    public void setUp() throws URISyntaxException {
        // Print logs
        ShadowLog.stream = System.out;
        // Get path of json files
        jsonFullPath = getClass().getResource(JSON_ROOT_PATH).toURI().getPath();

        // Create Http Client and setup interceptor
        mOkHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new MockInterceptor(jsonFullPath))
                .build();
    }

    @Test
    public void testActivity() {
        MainActivity mainActivity = Robolectric.setupActivity(MainActivity.class);
        assertNotNull(mainActivity);
        assertEquals(mainActivity.getTitle(), "Popular News");
        assertNotNull(mainActivity.mFooterView);
        assertNotNull(mainActivity.mListView);
        assertNotNull(mainActivity.swipeContainer);
        assertNotNull(mainActivity.mAdapter);
        assertNotNull(mainActivity.mFooterTextView);
    }

    @Test
    public void mockTopStories() throws IOException {
        Request request = new Request.Builder()
                .url(Constants.getTopStoriesUrl())
                .build();
        Response response = mOkHttpClient.newCall(request).execute();
        assertEquals(FileUtil.readFile(jsonFullPath + "topstories.json", "UTF-8").toString(), response.body().string());
    }

    @Test
    public void mockStory() throws IOException {
        Type listType = new TypeToken<List<String>>() {
        }.getType();
        List<String> ids = new Gson().fromJson(FileUtil.readFile(jsonFullPath + "topstories.json", "UTF-8").toString(), listType);
        assertEquals(ids.size(), 500);
        Request request = new Request.Builder()
                .url(Constants.getStoryUrl(Integer.valueOf(ids.get(0))))
                .build();
        Response response = mOkHttpClient.newCall(request).execute();
        assertEquals(FileUtil.readFile(jsonFullPath + "0.json", "UTF-8").toString(), response.body().string());
    }

    @Test
    public void testListView() throws Exception {
        MainActivity mainActivity = Robolectric.setupActivity(MainActivity.class);
        List<Story> stories = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            String json = FileUtil.readFile(jsonFullPath + i + ".json", "UTF-8").toString();
            JSONObject jsonObject = new JSONObject(json);
            stories.add(new Story(jsonObject));
        }
        mainActivity.mAdapter.setStories(stories);
        mainActivity.mAdapter.notifyDataSetChanged();
        assertEquals(10, mainActivity.mAdapter.getCount());
        View view = mainActivity.mAdapter.getView(0, null, null);
        ImageButton btnUrl = (ImageButton) view.findViewById(R.id.btnUrl);

        ImageButton btnComments = (ImageButton) view.findViewById(R.id.btnComments);
        btnComments.performClick();
        Intent actualIntent = ShadowApplication.getInstance().getNextStartedActivity();
        assertNotNull(actualIntent.getIntArrayExtra(BaseEntity.FIELD_KIDS));
    }
}