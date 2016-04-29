package net.bobgao.ycombinatorhackernews.models;

/**
 * Created by bobgao on 23/4/16.
 */
public class BaseEntity {

    public static final String FIELD_ID = "id";
    public static final String FIELD_BY = "by";
    public static final String FIELD_TYPE = "type";
    public static final String FIELD_TIME = "time";
    public static final String FIELD_DELETED = "deleted";
    public static final String FIELD_TEXT = "text";
    public static final String FIELD_DEAD = "dead";
    public static final String FIELD_PARENT = "parent";
    public static final String FIELD_KIDS = "kids";
    public static final String FIELD_URL = "url";
    public static final String FIELD_TITLE = "title";
    public static final String FIELD_SCORE = "score";
    public static final String FIELD_PARTS = "parts";
    public static final String FIELD_DESCENDANTS = "descendants";

    private int id;
    private String by;
    private String type;
    private long time;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBy() {
        return by;
    }

    public void setBy(String by) {
        this.by = by;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
