package net.bobgao.ycombinatorhackernews.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import net.bobgao.ycombinatorhackernews.DatetimeHelper;
import net.bobgao.ycombinatorhackernews.R;
import net.bobgao.ycombinatorhackernews.activities.CommentsActivity;
import net.bobgao.ycombinatorhackernews.models.BaseEntity;
import net.bobgao.ycombinatorhackernews.models.Story;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by bobgao on 25/4/16.
 */
public class StoryListAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<Story> mStories = new ArrayList<>();
    private Context mContext;

    public StoryListAdapter(Context context, List<Story> stories) {
        mContext = context;
        mStories = stories;
        mInflater = LayoutInflater.from(context);
    }

    public List<Story> getStories() {
        return mStories;
    }

    public void setStories(List<Story> stories) {
        this.mStories = stories;
    }

    @Override
    public int getCount() {
        return mStories.size();
    }

    @Override
    public Object getItem(int position) {
        return mStories.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mStories.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.view_row, null);
            StoryViewHolder holder = new StoryViewHolder();
            holder.titleView = (TextView) convertView.findViewById(R.id.tvTitle);
            holder.pointsView = (TextView) convertView.findViewById(R.id.tvPoints);
            holder.authorView = (TextView) convertView.findViewById(R.id.tvAuthor);
            holder.timeView = (TextView) convertView.findViewById(R.id.tvTime);
            holder.commentsButton = (ImageButton) convertView.findViewById(R.id.btnComments);
            holder.urlButton = (ImageButton) convertView.findViewById(R.id.btnUrl);
            convertView.setTag(holder);
        }

        final Story story = (Story) getItem(position);
        StoryViewHolder holder = (StoryViewHolder) convertView.getTag();
        holder.titleView.setText(story.getTitle());
        holder.pointsView.setText(String.valueOf(story.getScore()));
        holder.authorView.setText(story.getBy());
        holder.timeView.setText(DatetimeHelper.showTimeToPresend(story.getTime()));

        if (story.getUrl() == null) {
            holder.urlButton.setVisibility(View.INVISIBLE);
        } else {
            holder.urlButton.setVisibility(View.VISIBLE);
            holder.urlButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(story.getUrl()));
                    mContext.startActivity(i);
                }
            });
        }

        if (story.getKids().length > 0) {
            holder.commentsButton.setVisibility(View.VISIBLE);
            holder.commentsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(mContext, CommentsActivity.class);
                    i.putExtra(BaseEntity.FIELD_KIDS, story.getKids());
                    mContext.startActivity(i);
                }
            });
        } else {
            holder.commentsButton.setVisibility(View.INVISIBLE);
        }
        return convertView;
    }

    static class StoryViewHolder {
        TextView titleView;
        TextView pointsView;
        TextView authorView;
        TextView timeView;
        ImageButton commentsButton;
        ImageButton urlButton;
    }
}
