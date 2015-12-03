package edu.utaustin.yusun.yellerandroid.friends_activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import edu.utaustin.yusun.yellerandroid.R;

public class FriendListActivity extends Activity {
    String[] stringArray;
    ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);

        stringArray = new String[10];
        for(int i=0; i < stringArray.length; i++){
            stringArray[i] = "String " + i;
        }
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, stringArray);
        ListView stringListView = (ListView) findViewById(R.id.listViewId);
        stringListView.setAdapter(arrayAdapter);
    }
}
