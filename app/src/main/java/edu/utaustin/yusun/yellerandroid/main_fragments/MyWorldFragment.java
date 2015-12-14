package edu.utaustin.yusun.yellerandroid.main_fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;

import com.beardedhen.androidbootstrap.BootstrapCircleThumbnail;
import com.beardedhen.androidbootstrap.TypefaceProvider;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import cz.msebera.android.httpclient.Header;
import edu.utaustin.yusun.yellerandroid.R;
import edu.utaustin.yusun.yellerandroid.adapter.PullToRefreshListViewAdapter;
import edu.utaustin.yusun.yellerandroid.data.ListItem;
import edu.utaustin.yusun.yellerandroid.friends_activities.FriendListActivity;
import edu.utaustin.yusun.yellerandroid.function_activities.PullToRefreshListView;
import edu.utaustin.yusun.yellerandroid.function_activities.SearchDialog;
import edu.utaustin.yusun.yellerandroid.function_activities.UploadImageActivity;
import edu.utaustin.yusun.yellerandroid.login_register.LoginActivity;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * Use the {@link MyWorldFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyWorldFragment extends Fragment implements
        View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    public static final String BITMAPIMAGE = "BitmapImage";
    private PullToRefreshListView listView;
    private PullToRefreshListViewAdapter adapter;

    private String user_email = MainActivity.user_email;
    private String avatar_url;
    BootstrapCircleThumbnail avatar_view;
    //Data to show
    ArrayList<ListItem> items;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyWorldFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyWorldFragment newInstance(String param1, String param2) {
        MyWorldFragment fragment = new MyWorldFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TypefaceProvider.registerDefaultIconSets();
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_my_world, container, false);

        //Add listeners to buttons
        rootView.findViewById(R.id.connections_btn).setOnClickListener(this);
        rootView.findViewById(R.id.add_friend_btn).setOnClickListener(this);
        rootView.findViewById(R.id.change_avatar_btn).setOnClickListener(this);
        rootView.findViewById(R.id.logoff_btn).setOnClickListener(this);
        avatar_view = (BootstrapCircleThumbnail) rootView.findViewById(R.id.avatar);
        listView = (PullToRefreshListView) rootView.findViewById(R.id.pull_to_refresh_listview);

        //Initialize the items
        if (items == null)
            initializeItems();

        if (avatar_url != null)
            Picasso.with(getContext()).load(avatar_url).into(avatar_view);

        adapter = new PullToRefreshListViewAdapter(getActivity(), items) {};
        listView.setAdapter(adapter);

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

        // click listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @SuppressLint("NewApi")
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {

                PullToRefreshListViewAdapter.ViewHolder viewHolder = (PullToRefreshListViewAdapter.ViewHolder) arg1.getTag();
                if (viewHolder.name != null) {

                }
            }
        });

        // Register the context menu for actions
        registerForContextMenu(listView);
        return rootView;
    }

    private void initializeItems() {
        items = new ArrayList<>();
        String feed_url = "http://socialyeller.appspot.com/android_myworld";
        RequestParams params = new RequestParams();
        final ArrayList<String> yellers_key_ids = new ArrayList<String>();
        params.put("User_email", user_email);
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
                    for (int i = 0; i <  yellers_key_ids.size(); i++) {
                        final ListItem item = new ListItem();
                        newparams.put("yeller_id", yellers_key_ids.get(i));
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
                                    ArrayList<String> picture_urls = new ArrayList<>();
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
                                        Picasso.with(getContext()).load(avatar_url).into(avatar_view);
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
            case R.id.connections_btn:
                Intent intent = new Intent(getContext(), FriendListActivity.class);
                intent.putExtra("activity", "Connections");
                startActivity(intent);
                break;
            case R.id.add_friend_btn:
                searchNewFriend(v);
                break;
            case R.id.change_avatar_btn:
                selectImage();
                break;
            case R.id.logoff_btn:
                Intent intent2 = new Intent(getContext(), LoginActivity.class);
                startActivity(intent2);
                break;

        }
    }

    //Search for new friend function
    public void searchNewFriend(View v) {
        SearchDialog searchDialog = new SearchDialog(this.getContext());
        searchDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        searchDialog.show();


    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);
                } else if (items[item].equals("Choose from Library")) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(
                            Intent.createChooser(intent, "Select File"),
                            SELECT_FILE);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });


        builder.show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Intent intent = new Intent(getContext(), UploadImageActivity.class);
        intent.putExtra("activity", "MyWorld");
        intent.putExtra(BITMAPIMAGE, thumbnail);
        startActivity(intent);
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        Uri selectedImageUri = data.getData();
        String[] projection = {MediaStore.MediaColumns.DATA};
        Cursor cursor = getContext().getContentResolver().query(selectedImageUri, projection, null, null,
                null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();

        String selectedImagePath = cursor.getString(column_index);

        Bitmap bm;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(selectedImagePath, options);
        final int REQUIRED_SIZE = 200;
        int scale = 1;
        while (options.outWidth / scale / 2 >= REQUIRED_SIZE
                && options.outHeight / scale / 2 >= REQUIRED_SIZE)
            scale *= 2;
        options.inSampleSize = scale;
        options.inJustDecodeBounds = false;
        bm = BitmapFactory.decodeFile(selectedImagePath, options);

        Intent intent = new Intent(getContext(), UploadImageActivity.class);
        intent.putExtra("activity", "MyWorld");
        intent.putExtra(BITMAPIMAGE, bm);
        startActivity(intent);

    }
}
