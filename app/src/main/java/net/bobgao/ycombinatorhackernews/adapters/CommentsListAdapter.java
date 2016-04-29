package net.bobgao.ycombinatorhackernews.adapters;

import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.bobgao.ycombinatorhackernews.DatetimeHelper;
import net.bobgao.ycombinatorhackernews.R;
import net.bobgao.ycombinatorhackernews.models.Comment;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by bobgao on 27/4/16.
 */
public class CommentsListAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<Comment> mComments;
    private Context mContext;

    public CommentsListAdapter(Context context, List<Comment> comments) {
        mContext = context;
        mComments = comments;
        mInflater = LayoutInflater.from(context);
    }

    public void setItems(List<Comment> comments) {
        mComments = comments;
    }

    @Override
    public int getCount() {
        return mComments.size();
    }

    @Override
    public Object getItem(int position) {
        return mComments.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mComments.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.view_comment_row, null);
            CommentViewHolder holder = new CommentViewHolder();
            holder.textView = (TextView) convertView.findViewById(R.id.tvTitle);
            holder.authorView = (TextView) convertView.findViewById(R.id.tvAuthor);
            holder.timeView = (TextView) convertView.findViewById(R.id.tvTime);
            holder.llRoot = (LinearLayout) convertView.findViewById(R.id.llRoot);
            convertView.setTag(holder);
        }

        final Comment comment = (Comment) getItem(position);
        CommentViewHolder holder = (CommentViewHolder) convertView.getTag();
        if (comment.getText() != null)
            holder.textView.setText(Html.fromHtml(comment.getText()));
        if (comment.getBy() != null)
            holder.authorView.setText(comment.getBy());
        if (comment.getTime() != 0)
            holder.timeView.setText(DatetimeHelper.showTimeToPresend(comment.getTime()));

        int paddingPixel = 25 * comment.getLevel();
        float density = mContext.getResources().getDisplayMetrics().density;
        int paddingDp = (int) (paddingPixel * density);
        holder.llRoot.setPadding(paddingDp, 0, 0, 0);
        return convertView;
    }

    static class CommentViewHolder {
        LinearLayout llRoot;
        TextView textView;
        TextView authorView;
        TextView timeView;
    }
}
