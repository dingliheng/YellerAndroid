package edu.utaustin.yusun.yellerandroid.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import edu.utaustin.yusun.yellerandroid.R;
import edu.utaustin.yusun.yellerandroid.data.FriendInfoItem;

/**
 * Created by yusun on 12/4/15.
 */
public class FriendInfoAdapter extends ArrayAdapter {
    private LayoutInflater inflater;
    private ArrayList<FriendInfoItem> items = new ArrayList<>();

    public FriendInfoAdapter(Activity activity,  ArrayList<FriendInfoItem> items){
        super(activity, R.layout.search_result_layout, items);
        inflater = activity.getWindow().getLayoutInflater();
        this.items = items;
    }

    public class ViewHolder {

        public TextView name;
        public ImageView avatar;
        public TextView timestamp;
        public TextView latest_tweet;
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
    public View getView(int position, View convertView, ViewGroup parent){
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.search_result_layout, parent, false);
            holder = new ViewHolder();
            holder.name = (TextView)convertView.findViewById(R.id.name);
            holder.avatar = (ImageView) convertView.findViewById(R.id.avatar);
            holder.timestamp = (TextView) convertView.findViewById(R.id.timestamp);
            holder.latest_tweet = (TextView) convertView.findViewById(R.id.lastest_tweet);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.name.setText(items.get(position).getName());
        holder.avatar.setImageResource(R.mipmap.avatar);
        holder.latest_tweet.setText(items.get(position).getStatus());
        holder.timestamp.setText(items.get(position).getTimeStamp());

        return convertView;
    }

}
