package edu.utaustin.yusun.yellerandroid.main_fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.TypefaceProvider;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

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
import im.delight.android.location.SimpleLocation;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link NearbyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NearbyFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private PullToRefreshListView listView;
    private PullToRefreshListViewAdapter adapter;

    private SimpleLocation location;

    //Data to show
    ArrayList<ListItem> items;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NearbyFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NearbyFragment newInstance(String param1, String param2) {
        NearbyFragment fragment = new NearbyFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public NearbyFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TypefaceProvider.registerDefaultIconSets();
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        // construct a new instance of SimpleLocation
        location = new SimpleLocation(getContext());

        // if we can't access the location yet
        if (!location.hasLocationEnabled()) {
            // ask the user to enable location access
            SimpleLocation.openSettings(getContext());
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_nearby, container, false);

        if (items == null)
            initializeItems();
        listView = (PullToRefreshListView) rootView.findViewById(R.id.pull_to_refresh_listview);

        listView.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {

            @Override
            public void onRefresh() {

                initializeItems();
                Collections.sort(items);
                listView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        adapter = new PullToRefreshListViewAdapter(getActivity(), items){};
                        listView.setAdapter(adapter);
                        listView.onRefreshComplete();
                    }
                }, 2000);
            }
        });

        adapter = new PullToRefreshListViewAdapter(getActivity(), items) {};
        listView.setAdapter(adapter);


        // click listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @SuppressLint("NewApi")
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {

                PullToRefreshListViewAdapter.ViewHolder viewHolder = (PullToRefreshListViewAdapter.ViewHolder) arg1.getTag();
                if (viewHolder.name != null) {
                    Toast.makeText(getContext(), viewHolder.name.getText(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        TextView textView = (TextView) rootView.findViewById(R.id.location);
        String locationTxt = "latitude: " + location.getLatitude() + "longitude: " + location.getLongitude();
        textView.setText(locationTxt);

        // Register the context menu for actions
        registerForContextMenu(listView);
        return rootView;
    }

    private void initializeItems() {
        items = new ArrayList<>();
        String feed_url = "http://socialyeller.appspot.com/android_nearby";
        RequestParams params = new RequestParams();
        final ArrayList<String> yellers_key_ids = new ArrayList<String>();
        params.put("latitude", location.getLatitude());
        params.put("longitude", location.getLongitude());
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
                    System.out.println("length " + yellers_key_ids.size());
                    for (int i = 0; i < yellers_key_ids.size(); i++) {
                        final ListItem item = new ListItem();
                        newparams.put("yeller_id", yellers_key_ids.get(i));
                        item.setYeller_id(yellers_key_ids.get(i));
                        final int j = i;
                        AsyncHttpClient newhttpClient = new AsyncHttpClient();
                        newhttpClient.get(findyeller_url, newparams, new AsyncHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                try {
                                    //An item of data

                                    JSONObject jObject = new JSONObject(new String(responseBody));
                                    String anonymity = jObject.getString("anonymity");

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



}
