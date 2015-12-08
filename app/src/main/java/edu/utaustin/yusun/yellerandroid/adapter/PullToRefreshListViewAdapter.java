package edu.utaustin.yusun.yellerandroid.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapThumbnail;
import com.beardedhen.androidbootstrap.TypefaceProvider;

import java.util.ArrayList;

import edu.utaustin.yusun.yellerandroid.R;

/**
 * Created by mackbook on 11/25/15.
 */
public abstract class PullToRefreshListViewAdapter extends android.widget.BaseAdapter {
    private ArrayList<String> items = new ArrayList<String>();;
    private Context mContext;
    private View rowView;
    public PullToRefreshListViewAdapter(Context context, ArrayList<String> items) {
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

        items = new ArrayList<String>();

        items.add("item 1");
        items.add("item 2");
        items.add("item 3");



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

        String record = (String) getItem(position);

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

        holder.name.setText(record);

        holder.name.setText("Yu Sun");

        holder.timestamp.setText("15min");

        holder.statusMsg.setText("I love Christmas!!!!");

        holder.profilePic.setImageResource(R.mipmap.avatar);

        holder.feedImageView.setImageResource(R.mipmap.christmas);

        if (convertView == null) {
            LinearLayout reply = (LinearLayout) rowView.findViewById(R.id.reply);

            for (int i = 0; i < 3; i++) {
                inflater.inflate(R.layout.comment, reply, true);
                TextView comment = (TextView) reply.findViewById(R.id.comment_content);
                comment.setText("Love u");
            }
        }
        return rowView;
    }
}
