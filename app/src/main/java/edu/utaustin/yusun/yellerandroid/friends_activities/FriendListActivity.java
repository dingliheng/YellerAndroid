package edu.utaustin.yusun.yellerandroid.friends_activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

import edu.utaustin.yusun.yellerandroid.R;
import edu.utaustin.yusun.yellerandroid.adapter.FriendInfoAdapter;
import edu.utaustin.yusun.yellerandroid.data.FriendInfoItem;

public class FriendListActivity extends Activity {
    ArrayList <FriendInfoItem> friendItems;
    FriendInfoAdapter friendListArrayAdapter;
    String activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);

        Intent intent = getIntent();
        activity = intent.getStringExtra("activity");
        initializeItems();

        friendListArrayAdapter = new FriendInfoAdapter(this, friendItems);
        ListView stringListView = (ListView) findViewById(R.id.listViewId);
        stringListView.setAdapter(friendListArrayAdapter);
    }

    private void initializeItems() {
        friendItems = new ArrayList<>();
        if (activity.equals("Search")) {
            String searchKey = getIntent().getStringExtra("key");
            //According to the key, extract information from the backend to the friendItems.

        } else if (activity.equals("Connections")) { //Show all the friends

        }

    }
}
