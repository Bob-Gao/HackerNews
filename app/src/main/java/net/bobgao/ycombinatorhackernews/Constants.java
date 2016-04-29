package net.bobgao.ycombinatorhackernews;

/**
 * Created by bobgao on 25/4/16.
 */
public class Constants {

    private static final String BASE_URL = "https://hacker-news.firebaseio.com/v0/";

    public static String getTopStoriesUrl() {
        return BASE_URL + "topstories.json?print=pretty";
    }

    public static String getStoryUrl(int id) {
        return BASE_URL + "item/" + id + ".json?print=pretty";
    }

    public static String getCommentUrl(int id) {
        return BASE_URL + "item/" + id + ".json?print=pretty";
    }

    public static String getAskUrl(int id) {
        return BASE_URL + "item/" + id + ".json?print=pretty";
    }

    public static String getJobUrl(int id) {
        return BASE_URL + "item/" + id + ".json?print=pretty";
    }

    public static String getPollUrl(int id) {
        return BASE_URL + "item/" + id + ".json?print=pretty";
    }

    public static String getPartUrl(int id) {
        return BASE_URL + "item/" + id + ".json?print=pretty";
    }
}
