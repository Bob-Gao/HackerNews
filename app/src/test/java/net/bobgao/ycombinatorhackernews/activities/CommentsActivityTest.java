package net.bobgao.ycombinatorhackernews.activities;

import android.content.Intent;
import android.test.InstrumentationTestCase;
import android.view.View;
import android.widget.ImageButton;

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
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowApplication;
import org.robolectric.shadows.ShadowLog;
import org.robolectric.util.ActivityController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by bobgao on 30/4/16.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class CommentsActivityTest extends InstrumentationTestCase {
    private static final String JSON_ROOT_PATH = "/json/";
    private String jsonFullPath;
    private OkHttpClient mOkHttpClient;
    private ActivityController<CommentsActivity> controller;

    @Before
    public void setUp() throws Exception {
        // Print logs
        ShadowLog.stream = System.out;
        // Get path of json files
        jsonFullPath = getClass().getResource(JSON_ROOT_PATH).toURI().getPath();

        // Create Http Client and setup interceptor
        mOkHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new MockInterceptor(jsonFullPath))
                .build();
        controller = Robolectric.buildActivity(CommentsActivity.class);
    }


    @Test
    public void testActivity() {
        int[] kids = new int[]{11596056, 11596190, 11596271, 11596287, 11596257};
        Intent intent = new Intent(RuntimeEnvironment.application, CommentsActivity.class);
        intent.putExtra(BaseEntity.FIELD_KIDS, kids);
        CommentsActivity commentsActivity = controller
                .withIntent(intent)
                .create()
                .start()
                .resume()
                .visible()
                .get();
        assertNotNull(commentsActivity);
        assertEquals(commentsActivity.getTitle(), "Comments");
        assertNotNull(commentsActivity.mListView);
        assertNotNull(commentsActivity.swipeContainer);
        assertNotNull(commentsActivity.mAdapter);
    }

    @Test
    public void mockComment() throws IOException {
        Request request = new Request.Builder()
                .url(Constants.getCommentUrl(11596056))
                .build();
        Response response = mOkHttpClient.newCall(request).execute();
        assertEquals(FileUtil.readFile(jsonFullPath + "comment1.json", "UTF-8").toString(), response.body().string());
    }

    @Test
    public void testListView() throws Exception {
        int[] kids = new int[]{11596056, 11596190, 11596271, 11596287, 11596257};
        Intent intent = new Intent(RuntimeEnvironment.application, CommentsActivity.class);
        intent.putExtra(BaseEntity.FIELD_KIDS, kids);
        CommentsActivity commentsActivity = controller
                .withIntent(intent)
                .create()
                .start()
                .resume()
                .get();
        List<Comment> comments = new ArrayList<>();
        for (int i = 1; i <= kids.length; i++) {
            String json = FileUtil.readFile(jsonFullPath + "comment" + i + ".json", "UTF-8").toString();
            JSONObject jsonObject = new JSONObject(json);
            comments.add(new Comment(jsonObject));
        }
        commentsActivity.mComments= comments;
        commentsActivity.arrangeData();
        assertEquals(5, commentsActivity.mAdapter.getCount());
    }


}