package edu.utaustin.yusun.yellerandroid.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import edu.utaustin.yusun.yellerandroid.R;

/**
 * Created by mackbook on 12/4/15.
 */
public class FriendInfoAdapter extends ArrayAdapter {
    private LayoutInflater inflater;

    public FriendInfoAdapter(Activity activity, String[] items){
        super(activity, R.layout.search_result_layout, items);
        inflater = activity.getWindow().getLayoutInflater();
    }

    public class ViewHolder {

        public TextView name;
        public ImageView avatar;
        public TextView timestamp;
        public TextView latest_tweet;
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

        holder.name.setText("Yu Sun");
        holder.avatar.setImageResource(R.mipmap.avatar);
        holder.latest_tweet.setText("It is so nice!");
        holder.timestamp.setText("15min");

        return convertView;
    }

}
