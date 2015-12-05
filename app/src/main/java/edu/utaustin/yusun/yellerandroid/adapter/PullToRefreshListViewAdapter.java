package edu.utaustin.yusun.yellerandroid.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
    }
    public class ViewHolder {
        public String id;
        public TextView name;
    }


    /**
     * Loads the data.
     */
    public void loadData() {

        // Here add your code to load the data for example from a webservice or DB

        items = new ArrayList<String>();

        items.add("Ajax Amsterdam");
        items.add("Barcelona");
        items.add("Manchester United");



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

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        rowView = convertView;

        String record = (String) getItem(position);

        LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();

        ViewHolder viewHolder = new ViewHolder();

        if (convertView == null){
            rowView = inflater.inflate(R.layout.list_item, parent, false);

            viewHolder.name = (TextView) rowView.findViewById(R.id.name);

            rowView.setTag(viewHolder);
        }

        final ViewHolder holder = (ViewHolder) rowView.getTag();

        holder.name.setText(record);

        TextView name = (TextView) rowView.findViewById(R.id.name);
        name.setText("Yu Sun");
        TextView timestamp = (TextView) rowView
                .findViewById(R.id.timestamp);
        timestamp.setText("15min");

        TextView statusMsg = (TextView) rowView
                .findViewById(R.id.txtStatusMsg);
        statusMsg.setText("I love Christmas!!!!");

        ImageView profilePic = (ImageView) rowView
                .findViewById(R.id.profilePic);
        profilePic.setImageResource(R.mipmap.avatar);

        ImageView feedImageView = (ImageView) rowView
                .findViewById(R.id.feedImage1);
        feedImageView.setImageResource(R.mipmap.christmas);

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
