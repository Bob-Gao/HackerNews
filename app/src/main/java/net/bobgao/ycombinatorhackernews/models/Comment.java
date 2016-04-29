package net.bobgao.ycombinatorhackernews.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bobgao on 23/4/16.
 */
public class Comment extends BaseEntity {

    private int parent;
    private String text;
    private int[] kids;
    private int level;
    private List<Comment> comments = new ArrayList<>();

    public Comment(JSONObject response) throws JSONException {
        if (response.has(BaseEntity.FIELD_TYPE))
            this.setType(response.getString(BaseEntity.FIELD_TYPE));
        if (response.has(BaseEntity.FIELD_BY))
            this.setBy(response.getString(BaseEntity.FIELD_BY));
        if (response.has(BaseEntity.FIELD_ID))
            this.setId(response.getInt(BaseEntity.FIELD_ID));
        if (response.has(BaseEntity.FIELD_PARENT))
            this.setParent(response.getInt(BaseEntity.FIELD_PARENT));
        if (response.has(BaseEntity.FIELD_TIME))
            this.setTime(response.getLong(BaseEntity.FIELD_TIME));
        if (response.has(BaseEntity.FIELD_TEXT))
            this.setText(response.getString(BaseEntity.FIELD_TEXT));
        if (response.has(BaseEntity.FIELD_KIDS)) {
            JSONArray jsonArray = response.getJSONArray(BaseEntity.FIELD_KIDS);
            if (jsonArray.length() > 0) {
                int[] kids = new int[jsonArray.length()];
                for (int i = 0; i < kids.length; i++) {
                    kids[i] = jsonArray.getInt(i);
                }
                this.setKids(kids);
            } else {
                this.setKids(new int[0]);
            }
        } else {
            this.setKids(new int[0]);
        }
    }

    public int getParent() {
        return parent;
    }

    public void setParent(int parent) {
        this.parent = parent;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int[] getKids() {
        return kids;
    }

    public void setKids(int[] kids) {
        this.kids = kids;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
