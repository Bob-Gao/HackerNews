package net.bobgao.ycombinatorhackernews;

import java.util.concurrent.Callable;

/**
 * Created by bobgao on 30/4/16.
 */
public class Task implements Callable {

    private String name;

    public Task(String name) {
        this.name = name;
    }

    @Override
    public Object call() throws Exception {
        return null;
    }
}
