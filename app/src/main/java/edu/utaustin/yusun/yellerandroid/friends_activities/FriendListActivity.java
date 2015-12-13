package edu.utaustin.yusun.yellerandroid.friends_activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import edu.utaustin.yusun.yellerandroid.R;
import edu.utaustin.yusun.yellerandroid.adapter.FriendInfoAdapter;
import edu.utaustin.yusun.yellerandroid.data.FriendInfoItem;
import edu.utaustin.yusun.yellerandroid.data.ListItem;
import edu.utaustin.yusun.yellerandroid.main_fragments.MainActivity;

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
            String searchfriend_url = "http://socialyeller.appspot.com/android_searchfriend";
            final FriendInfoItem item = new FriendInfoItem();
            RequestParams params = new RequestParams();
            params.put("search_name", searchKey);
            AsyncHttpClient httpClient = new AsyncHttpClient();
            httpClient.get(searchfriend_url, params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    try {
                        //An item of data
                        String name, status, profilePic, timeStamp;
                        JSONObject jObject = new JSONObject(new String(responseBody));
                        Boolean isVaild = jObject.getBoolean("isVaild");

                        if (isVaild) {
                            name = jObject.getString("fullname");
                            System.out.println(name);
                            item.setName(name);
                            status = jObject.getString("status");
                            System.out.println(status);
                            item.setStatus(status);
                            profilePic = jObject.getString("portrait_url");
                            System.out.println(profilePic);
                            item.setProfilePic(profilePic);
                            timeStamp = jObject.getString("timestamp");
                            System.out.println(timeStamp);
                            item.setTimeStamp(timeStamp);
                            friendItems.add(item);
                        } else {
                            System.out.println("This friend is not existent");
                        }
                    } catch (JSONException j) {
                        System.out.println("JSON Error");
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                }
            });
                    //According to the key, extract information from the backend to the friendItems.

        } else if (activity.equals("Connections")) { //Show all the friends
            String myfriend_url = "http://socialyeller.appspot.com/android_myfriend";
            RequestParams params = new RequestParams();
            final ArrayList<String> friendlist = new ArrayList<String>();
            params.put("User_email", MainActivity.user_email);
            AsyncHttpClient httpClient = new AsyncHttpClient();
            httpClient.get(myfriend_url, params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    try {
                        JSONObject jObject = new JSONObject(new String(responseBody));
                        JSONArray friendlist_json = jObject.getJSONArray("friendlist");
                        for (int i = 0; i < friendlist_json.length(); i++) {
                            friendlist.add(friendlist_json.getString(i));
                            System.out.println("friend" + i + ": " + friendlist.get(i));
                        }
                    } catch (JSONException j) {
                        System.out.println("JSON Error");
                    }
                    String searchfriend_url = "http://socialyeller.appspot.com/android_searchfriend";
                    for (int i = 0; i < friendlist.size(); i++) {
                        RequestParams newparams = new RequestParams();
                        newparams.put("friend_name", friendlist.get(i));
                        AsyncHttpClient newhttpClient = new AsyncHttpClient();
                        newhttpClient.get(searchfriend_url, newparams, new AsyncHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                try {
                                    //An item of data
                                    String name, status, profilePic, timeStamp;
                                    JSONObject jObject = new JSONObject(new String(responseBody));
                                    Boolean isVaild = jObject.getBoolean("isVaild");

                                    if (isVaild) {
                                        name = jObject.getString("fullname");
                                        System.out.println(name);
                                        status = jObject.getString("status");
                                        System.out.println(status);
                                        profilePic = jObject.getString("portrait_url");
                                        System.out.println(profilePic);
                                        timeStamp = jObject.getString("timestamp");
                                        System.out.println(timeStamp);
                                        final FriendInfoItem item = new FriendInfoItem(name, status, profilePic, timeStamp);
                                        friendItems.add(item);
                                        System.out.println("balabala: " + friendItems.get(0).getName());
                                    } else {
                                        System.out.println("This friend is not existent");
                                    }
                                } catch (JSONException j) {
                                    System.out.println("JSON Error");
                                }
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            }
                        });
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                }
            });
        }
    }
}
