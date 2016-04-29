package net.bobgao.ycombinatorhackernews.models;

/**
 * Created by bobgao on 23/4/16.
 */
public class Part extends BaseEntity {

    private int parent;
    private int score;
    private String text;

    public int getParent() {
        return parent;
    }

    public void setParent(int parent) {
        this.parent = parent;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
