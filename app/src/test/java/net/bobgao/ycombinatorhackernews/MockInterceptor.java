package net.bobgao.ycombinatorhackernews;

import android.app.Application;

import net.bobgao.ycombinatorhackernews.util.FileUtil;

import org.robolectric.RuntimeEnvironment;

import java.io.IOException;
import java.net.URISyntaxException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by bobgao on 29/4/16.
 */
public class MockInterceptor implements Interceptor {


    private static final String JSON_ROOT_PATH = "/json/";
    private final String responeJsonPath;

    public MockInterceptor(String responeJsonPath) {
        this.responeJsonPath = responeJsonPath;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        String responseString = createResponseBody(chain);

        Response response = new Response.Builder()
                .code(200)
                .message(responseString)
                .request(chain.request())
                .protocol(Protocol.HTTP_1_0)
                .body(ResponseBody.create(MediaType.parse("application/json"), responseString.getBytes()))
                .addHeader("content-type", "application/json")
                .build();
        return response;
    }

    /**
     * Get json string from file and generate ResponseBody
     *
     * @param chain
     * @return
     */
    private String createResponseBody(Chain chain) {
        String responseString = null;

        HttpUrl url = chain.request().url();
        String path = url.url().getPath();
        if (path.matches("^(/v0/topstories.json)$")) {
            responseString = getResponseString("topstories.json");
        } else if (path.matches("^(/v0/item/11594816.json)$")) {
            responseString = getResponseString("0.json");
        } else if (path.matches("^(/v0/item/11595307.json)$")) {
            responseString = getResponseString("1.json");
        } else if (path.matches("^(/v0/item/11593532.json)$")) {
            responseString = getResponseString("2.json");
        } else if (path.matches("^(/v0/item/11595084.json)$")) {
            responseString = getResponseString("3.json");
        } else if (path.matches("^(/v0/item/11594971.json)$")) {
            responseString = getResponseString("4.json");
        } else if (path.matches("^(/v0/item/11595370.json)$")) {
            responseString = getResponseString("5.json");
        } else if (path.matches("^(/v0/item/11594880.json)$")) {
            responseString = getResponseString("6.json");
        } else if (path.matches("^(/v0/item/11595223.json)$")) {
            responseString = getResponseString("7.json");
        } else if (path.matches("^(/v0/item/11594869.json)$")) {
            responseString = getResponseString("8.json");
        } else if (path.matches("^(/v0/item/11595139.json)$")) {
            responseString = getResponseString("9.json");
        } else if (path.matches("^(/v0/item/11596056.json)$")) {
            responseString = getResponseString("comment1.json");
        }
        return responseString;
    }

    private String getResponseString(String fileName) {
        return FileUtil.readFile(responeJsonPath + fileName, "UTF-8").toString();
    }
}
