package edu.utaustin.yusun.yellerandroid.friends_activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapCircleThumbnail;
import com.beardedhen.androidbootstrap.TypefaceProvider;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

import cz.msebera.android.httpclient.Header;
import edu.utaustin.yusun.yellerandroid.R;
import edu.utaustin.yusun.yellerandroid.adapter.PullToRefreshListViewAdapter;
import edu.utaustin.yusun.yellerandroid.data.ListItem;
import edu.utaustin.yusun.yellerandroid.function_activities.PullToRefreshListView;
import edu.utaustin.yusun.yellerandroid.main_fragments.MainActivity;

public class FriendPageActivity extends Activity implements
        View.OnClickListener {

    private PullToRefreshListView listView;
    private PullToRefreshListViewAdapter adapter;
    private String friendName;
    private String avatar_url;
    BootstrapCircleThumbnail avatar_view;
    //Data to show
    ArrayList<ListItem> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_page);
        TypefaceProvider.registerDefaultIconSets();

        //Add listeners to buttons
        findViewById(R.id.back_btn).setOnClickListener(this);
        findViewById(R.id.follow_btn).setOnClickListener(this);
        avatar_view = (BootstrapCircleThumbnail) findViewById(R.id.friend_avatar);
        friendName = getIntent().getStringExtra("userName");
        System.err.println("FriendPage: " + friendName);
        System.out.println("friend name: " + friendName);
        initializeItems(friendName);

        if (avatar_url != null)
            Picasso.with(this).load(avatar_url).into(avatar_view);

        listView = (PullToRefreshListView) findViewById(R.id.pull_to_refresh_listview);

        listView.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {

            @Override
            public void onRefresh() {

                initializeItems(friendName);
                Collections.sort(items);
                listView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        adapter = new PullToRefreshListViewAdapter(FriendPageActivity.this, items){};
                        listView.setAdapter(adapter);
                        listView.onRefreshComplete();
                    }
                }, 2000);
            }
        });

        adapter = new PullToRefreshListViewAdapter(this, items) {};
        listView.setAdapter(adapter);

        // click listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @SuppressLint("NewApi")
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {

                PullToRefreshListViewAdapter.ViewHolder viewHolder = (PullToRefreshListViewAdapter.ViewHolder) arg1.getTag();
                if (viewHolder.name != null) {
                    Toast.makeText(FriendPageActivity.this, viewHolder.name.getText(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Register the context menu for actions
        registerForContextMenu(listView);
    }

    //Initialize the items data to show.
    private void initializeItems(String friendName) {
//        System.out.println("friendName: "+friendName);
        items = new ArrayList<>();
        String feed_url = "http://socialyeller.appspot.com/android_friendpage";
        RequestParams params = new RequestParams();
        final ArrayList<String> yellers_key_ids = new ArrayList<String>();
        params.put("User_name", friendName);
        AsyncHttpClient httpClient = new AsyncHttpClient();
        httpClient.get(feed_url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    JSONObject jObject = new JSONObject(new String(responseBody));
                    JSONArray key_ids_json = jObject.getJSONArray("yellers_key_ids");
                    for (int i = 0; i < key_ids_json.length(); i++) {
                        yellers_key_ids.add(key_ids_json.getString(i));
                        System.out.println(key_ids_json.getString(i));
                    }
                    //initialize Items
                    String findyeller_url = "http://socialyeller.appspot.com/android_findyeller";
                    RequestParams newparams = new RequestParams();
                    System.out.println("length "+yellers_key_ids.size());
                    for (int i = 0; i <  yellers_key_ids.size(); i++){
                        final ListItem item = new ListItem();
                        newparams.put("yeller_id", yellers_key_ids.get(i));
                        item.setYeller_id(yellers_key_ids.get(i));
                        AsyncHttpClient newhttpClient = new AsyncHttpClient();
                        newhttpClient.get(findyeller_url, newparams, new AsyncHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                try {
                                    //An item of data

                                    JSONObject jObject = new JSONObject(new String(responseBody));
                                    String name = jObject.getString("fullname");
                                    item.setName(name);

                                    String timestamp = jObject.getString("date");
                                    item.setTimeStamp(timestamp);

                                    String statusMsg = jObject.getString("content");
                                    item.setStatus(statusMsg);

                                    JSONArray picture_urls_json = jObject.getJSONArray("picture_urls");
                                    ArrayList<String> picture_urls = new ArrayList<String>();
                                    for (int i = 0; i < picture_urls_json.length(); i++) {
                                        picture_urls.add(picture_urls_json.getString(i));
                                    }

                                    if (picture_urls.size() > 0) {
                                        String feedImageView_url = picture_urls.get(0);
                                        item.setImage(feedImageView_url);
                                    }

                                    String profilePic_url = jObject.getString("portrait_url");
                                    item.setProfilePic(profilePic_url);

                                    if (avatar_url == null) {
                                        avatar_url = profilePic_url;
                                        Picasso.with(FriendPageActivity.this).load(avatar_url).into(avatar_view);
                                    }

                                    items.add(item);
                                    Collections.sort(items);
                                    if (adapter != null)
                                        adapter.notifyDataSetChanged();


                                } catch (JSONException j) {
                                    System.out.println("JSON Error");
                                }
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            }
                        });

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
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_btn:
                finish();
                break;
            case R.id.follow_btn:
                String findyeller_url = "http://socialyeller.appspot.com/android_follow";
                RequestParams params = new RequestParams();
                params.put("User_email", MainActivity.user_email);
                params.put("friendname", friendName);
                System.out.println("friend name: "+ friendName);
                AsyncHttpClient newhttpClient = new AsyncHttpClient();
                newhttpClient.get(findyeller_url, params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        Toast.makeText(FriendPageActivity.this, "Follow Successfully", Toast.LENGTH_SHORT).show();
                        findViewById(R.id.follow_btn).setEnabled(false);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                    }
                });
                break;
        }
    }
}