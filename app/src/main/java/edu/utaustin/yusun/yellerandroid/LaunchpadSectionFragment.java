package edu.utaustin.yusun.yellerandroid;

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
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * A fragment that launches other parts of the demo application.
 */
public class LaunchpadSectionFragment extends Fragment {
    int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    Button btnSelect;
    ImageView ivImage;
    public static final String BITMAPIMAGE = "BitmapImage";

    private PullToRefreshListView listView;
    private PullToRefreshListViewSampleAdapter adapter;

    // IDs for the context menu actions
    private final int idEdit = 1;
    private final int idDelete = 2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_section_launchpad, container, false);

        // Demonstration of a collection-browsing activity.
        rootView.findViewById(R.id.demo_collection_button)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), CollectionDemoActivity.class);
                        startActivity(intent);
                    }
                });

        // Demonstration of navigating to external activities.
        rootView.findViewById(R.id.demo_external_activity)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Create an intent that asks the user to pick a photo, but using
                        // FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET, ensures that relaunching
                        // the application from the device home screen does not return
                        // to the external activity.
                        Intent externalActivityIntent = new Intent(Intent.ACTION_PICK);
                        externalActivityIntent.setType("image/*");
                        externalActivityIntent.addFlags(
                                Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                        startActivity(externalActivityIntent);
                    }
                });

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
                // Your code to refresh the list contents goes here

                // for example:
                // If this is a webservice call, it might be asynchronous so
                // you would have to call listView.onRefreshComplete(); when
                // the webservice returns the data
                adapter.loadData();

                // Make sure you call listView.onRefreshComplete()
                // when the loading is done. This can be done from here or any
                // other place, like on a broadcast receive from your loading
                // service or the onPostExecute of your AsyncTask.

                // For the sake of this sample, the code will pause here to
                // force a delay when invoking the refresh
                listView.postDelayed(new Runnable() {


                    @Override
                    public void run() {
                        listView.onRefreshComplete();
                    }
                }, 2000);
            }
        });

        adapter = new PullToRefreshListViewSampleAdapter() {};
        listView.setAdapter(adapter);

        // Request the adapter to load the data
        adapter.loadData();

        // click listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {

                PullToRefreshListViewSampleAdapter.ViewHolder viewHolder = (PullToRefreshListViewSampleAdapter.ViewHolder) arg1.getTag();
                if (viewHolder.name != null){
                    Toast.makeText(getContext(), viewHolder.name.getText(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Register the context menu for actions
        registerForContextMenu(listView);
        return rootView;
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
        startActivity(intent);

    }

    /**
     * The adapter used to display the results in the list
     *
     */
    public abstract class PullToRefreshListViewSampleAdapter extends android.widget.BaseAdapter {

        private ArrayList<String> items = new ArrayList<String>();;

        public class ViewHolder {
            public String id;
            public TextView name;
        }

        /**
         * Loads the data.
         */
        public void loadData() {

            // Here add your code to load the data for example from a webservice or DB

            items = new ArrayList<String>();

            items.add("Ajax Amsterdam");
            items.add("Barcelona");
            items.add("Manchester United");
            items.add("Chelsea");
            items.add("Real Madrid");
            items.add("Bayern Munchen");
            items.add("Internazionale");
            items.add("Valencia");
            items.add("Arsenal");
            items.add("AS Roma");
            items.add("Tottenham Hotspur");
            items.add("PSV");
            items.add("Olympique Lyon");
            items.add("AC Milan");
            items.add("Dortmund");
            items.add("Schalke 04");
            items.add("Twente");
            items.add("Porto");
            items.add("Juventus");


            // MANDATORY: Notify that the data has changed
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View rowView = convertView;

            String record = (String) getItem(position);

            LayoutInflater inflater = getActivity().getLayoutInflater();

            ViewHolder viewHolder = new ViewHolder();

            if (convertView == null){
                rowView = inflater.inflate(R.layout.list_item,null);

                viewHolder.name = (TextView) rowView.findViewById(R.id.textView1);

                rowView.setTag(viewHolder);
            }

            final ViewHolder holder = (ViewHolder) rowView.getTag();

            holder.name.setText(record);

            return rowView;
        }
    }
}


