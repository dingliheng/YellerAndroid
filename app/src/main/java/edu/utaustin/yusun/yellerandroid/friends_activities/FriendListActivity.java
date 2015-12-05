package edu.utaustin.yusun.yellerandroid.friends_activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import edu.utaustin.yusun.yellerandroid.R;
import edu.utaustin.yusun.yellerandroid.adapter.FriendInfoAdapter;

public class FriendListActivity extends Activity {
    String[] stringArray;
    FriendInfoAdapter friendListArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);

        stringArray = new String[10];
        for(int i=0; i < stringArray.length; i++){
            stringArray[i] = "String " + i;
        }

        friendListArrayAdapter = new FriendInfoAdapter(this, new String[10]);
        ListView stringListView = (ListView) findViewById(R.id.listViewId);
        stringListView.setAdapter(friendListArrayAdapter);
    }
}
