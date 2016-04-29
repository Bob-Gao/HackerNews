package net.bobgao.ycombinatorhackernews.models;

/**
 * Created by bobgao on 23/4/16.
 */
public class Ask extends BaseEntity {

    private int descendants;
    private String title;
    private String text;
    private String url;
    private int score;
    private int[] kits;

    public int getDescendants() {
        return descendants;
    }

    public void setDescendants(int descendants) {
        this.descendants = descendants;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int[] getKits() {
        return kits;
    }

    public void setKits(int[] kits) {
        this.kits = kits;
    }
}
