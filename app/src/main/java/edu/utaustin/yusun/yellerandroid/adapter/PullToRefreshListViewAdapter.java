package edu.utaustin.yusun.yellerandroid.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapThumbnail;
import com.beardedhen.androidbootstrap.TypefaceProvider;

import java.util.ArrayList;

import edu.utaustin.yusun.yellerandroid.R;
import edu.utaustin.yusun.yellerandroid.data.ListItem;
import edu.utaustin.yusun.yellerandroid.function_activities.CommentDialog;

/**
 * Created by yusun on 11/25/15.
 */
public abstract class PullToRefreshListViewAdapter extends android.widget.BaseAdapter {
    private ArrayList<ListItem> items = new ArrayList<ListItem>();;
    private Context mContext;
    private View rowView;
    public PullToRefreshListViewAdapter(Context context, ArrayList<ListItem> items) {
        mContext = context;
        this.items = items;
        TypefaceProvider.registerDefaultIconSets();
    }
    public class ViewHolder {
        public String id;
        public TextView name;
        public ImageView profilePic;
        public BootstrapThumbnail feedImageView;
        public TextView timestamp;
        public TextView statusMsg;
    }


    /**
     * Loads the data.
     */
    public void loadData() {

        // Here add your code to load the data for example from a webservice or DB

        // MANDATORY: Notify that the data has changed
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    //Show one list item
    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        rowView = convertView;

        ListItem record = (ListItem) getItem(position);

        LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
        ViewHolder viewHolder = new ViewHolder();

        if (convertView == null){
            rowView = inflater.inflate(R.layout.list_item, parent, false);

            viewHolder.name = (TextView) rowView.findViewById(R.id.name);
            viewHolder.timestamp = (TextView) rowView.findViewById(R.id.timestamp);
            viewHolder.statusMsg = (TextView) rowView.findViewById(R.id.txtStatusMsg);
            viewHolder.profilePic = (ImageView) rowView.findViewById(R.id.profilePic);
            viewHolder.feedImageView = (BootstrapThumbnail) rowView.findViewById(R.id.feedImage1);
            rowView.setTag(viewHolder);
        }

        final ViewHolder holder = (ViewHolder) rowView.getTag();

        holder.name.setText(record.getName());
        holder.timestamp.setText(record.getTimeStamp()); // need to modify the time to the elapse time
        holder.statusMsg.setText(record.getStatus());

        holder.profilePic.setImageResource(R.mipmap.avatar); //Pisacco

        holder.feedImageView.setImageResource(R.mipmap.christmas);

        if (convertView == null) {
            LinearLayout reply = (LinearLayout) rowView.findViewById(R.id.reply);

            for (int i = 0; i < 3; i++) {
                inflater.inflate(R.layout.comment, reply, true);
                TextView comment = (TextView) reply.findViewById(R.id.comment_content);
                comment.setText("Love u");
            }
        }

        rowView.findViewById(R.id.comment_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommentDialog searchDialog = new CommentDialog(v.getContext());
                searchDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                searchDialog.show();
            }
        });
        return rowView;
    }
}
