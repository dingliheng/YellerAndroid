package edu.utaustin.yusun.yellerandroid.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

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

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        return inflater.inflate(R.layout.search_result_layout, parent, false);
    }

}
