package edu.utaustin.yusun.yellerandroid.main_fragments;

/**
 * Created by yusun on 11/7/15.
 */

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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import edu.utaustin.yusun.yellerandroid.R;
import edu.utaustin.yusun.yellerandroid.adapter.PullToRefreshListViewAdapter;
import edu.utaustin.yusun.yellerandroid.data.ListItem;
import edu.utaustin.yusun.yellerandroid.friends_activities.FriendPageActivity;
import edu.utaustin.yusun.yellerandroid.function_activities.PublishMoodActivity;
import edu.utaustin.yusun.yellerandroid.function_activities.PullToRefreshListView;
import edu.utaustin.yusun.yellerandroid.function_activities.UploadImageActivity;

/**
 * A fragment that launches other parts of the demo application.
 */
public class FeedPadFragment extends Fragment {
    int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    Button btnSelect;
    ImageView ivImage;
    public static final String BITMAPIMAGE = "BitmapImage";

    private PullToRefreshListView listView;
    private PullToRefreshListViewAdapter adapter;

    // IDs for the context menu actions
    private final int idEdit = 1;
    private final int idDelete = 2;
    private String user_email = MainActivity.user_email;

    //Data to show
    ArrayList<ListItem> items;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_section_launchpad, container, false);


        final BootstrapButton mood_button = (BootstrapButton) rootView.findViewById(R.id.mood_button);
        mood_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), PublishMoodActivity.class);
                startActivity(intent);

            }
        });

        final BootstrapButton photo_button = (BootstrapButton) rootView.findViewById(R.id.photo_button);
        photo_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        if (items == null)
            initializeItems();
        listView = (PullToRefreshListView) rootView.findViewById(R.id.pull_to_refresh_listview);

        // OPTIONAL: Disable scrolling when list is refreshing
        // listView.setLockScrollWhileRefreshing(false);

        // OPTIONAL: Uncomment this if you want the Pull to Refresh header to show the 'last updated' time
        // listView.setShowLastUpdatedText(true);

        // OPTIONAL: Uncomment this if you want to override the date/time format of the 'last updated' field
        // listView.setLastUpdatedDateFormat(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"));

        // OPTIONAL: Uncomment this if you want to override the default strings
        // listView.setTextPullToRefresh("Pull to Refresh");
        // listView.setTextReleaseToRefresh("Release to Refresh");
        // listView.setTextRefreshing("Refreshing");

        // MANDATORY: Set the onRefreshListener on the list. You could also use
        // listView.setOnRefreshListener(this); and let this Activity
        // implement OnRefreshListener.
        listView.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {

            @Override
            public void onRefresh() {

                initializeItems();
                adapter = new PullToRefreshListViewAdapter(getActivity(), items) {};
                listView.setAdapter(adapter);

                listView.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        listView.onRefreshComplete();
                    }
                }, 2000);
            }
        });

        adapter = new PullToRefreshListViewAdapter(getActivity(), items) {};
        listView.setAdapter(adapter);

        // click listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {

                PullToRefreshListViewAdapter.ViewHolder viewHolder = (PullToRefreshListViewAdapter.ViewHolder) arg1.getTag();
                if (viewHolder.name != null) {
                    Intent intent = new Intent(getContext(), FriendPageActivity.class);
                    intent.putExtra("userName", (String) viewHolder.name.getText());
                    System.out.println("userName: "+ viewHolder.name.getText());
                    startActivity(intent);
                }
            }
        });

        // Register the context menu for actions
        registerForContextMenu(listView);
        return rootView;
    }

    private void initializeItems() {
        items = new ArrayList<>();
        String feed_url = "http://socialyeller.appspot.com/android_feed";
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
                    System.out.println("length "+yellers_key_ids.size());
                    for (int i = 0; i <  yellers_key_ids.size(); i++){
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


//                                    try{
//                                        Thread.sleep(1000);
//                                    } catch (InterruptedException e) {
//                                        e.printStackTrace();
//                                    }

                                } catch (JSONException j) {
                                    System.out.println("JSON Error");
                                }
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            }
                        });
                        items.add(item);
                    }
                    ;
                } catch (JSONException j) {
                    System.out.println("JSON Error");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
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
        intent.putExtra(BITMAPIMAGE, thumbnail);
        intent.putExtra("activity", "FeedPad");
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
        intent.putExtra(BITMAPIMAGE, bm);
        intent.putExtra("activity", "FeedPad");
        startActivity(intent);

    }

}

