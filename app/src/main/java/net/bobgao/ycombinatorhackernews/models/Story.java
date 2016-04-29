package net.bobgao.ycombinatorhackernews.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by bobgao on 23/4/16.
 */
public class Story extends BaseEntity {

    private String title;
    private String url;
    private int score;
    private int decendants;
    private int[] kids;

    public Story(JSONObject response) throws JSONException {
        this.setType(response.getString(BaseEntity.FIELD_TYPE));
        this.setBy(response.getString(BaseEntity.FIELD_BY));
        this.setScore(response.getInt(BaseEntity.FIELD_SCORE));
        this.setTitle(response.getString(BaseEntity.FIELD_TITLE));
        this.setId(response.getInt(BaseEntity.FIELD_ID));
        this.setTime(response.getLong(BaseEntity.FIELD_TIME));
        if (response.has(BaseEntity.FIELD_URL))
            this.setUrl(response.getString(BaseEntity.FIELD_URL));
        if (this.getType().equals("story"))
            this.setDecendants(response.getInt(BaseEntity.FIELD_DESCENDANTS));
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getDecendants() {
        return decendants;
    }

    public void setDecendants(int decendants) {
        this.decendants = decendants;
    }

    public int[] getKids() {
        return kids;
    }

    public void setKids(int[] kids) {
        this.kids = kids;
    }


}
