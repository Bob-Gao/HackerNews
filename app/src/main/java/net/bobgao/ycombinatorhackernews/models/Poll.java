package net.bobgao.ycombinatorhackernews.models;

/**
 * Created by bobgao on 23/4/16.
 */
public class Poll extends BaseEntity {

    private int descendants;
    private String title;
    private String text;
    private int score;
    private int[] kids;
    private int[] parts;


    public int getDescendants() {
        return descendants;
    }

    public void setDescendants(int descendants) {
        this.descendants = descendants;
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

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int[] getKids() {
        return kids;
    }

    public void setKids(int[] kids) {
        this.kids = kids;
    }

    public int[] getParts() {
        return parts;
    }

    public void setParts(int[] parts) {
        this.parts = parts;
    }
}
