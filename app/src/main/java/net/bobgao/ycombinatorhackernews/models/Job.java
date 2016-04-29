package net.bobgao.ycombinatorhackernews.models;

/**
 * Created by bobgao on 23/4/16.
 */
public class Job extends BaseEntity {

    private String title;
    private String text;
    private int score;
    private String url;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
