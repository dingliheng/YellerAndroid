package edu.utaustin.yusun.yellerandroid.friends_activities;

import android.app.Activity;
import android.os.Bundle;

import edu.utaustin.yusun.yellerandroid.R;

public class SearchFriendActivity extends Activity {

    String[] mobileArray = {"Android","IPhone","WindowsMobile","Blackberry","WebOS","Ubuntu","Windows7","Max OS X"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_friend);
    }
}