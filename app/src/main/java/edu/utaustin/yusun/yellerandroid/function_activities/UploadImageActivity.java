package edu.utaustin.yusun.yellerandroid.function_activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapThumbnail;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import cz.msebera.android.httpclient.Header;
import edu.utaustin.yusun.yellerandroid.R;
import edu.utaustin.yusun.yellerandroid.main_fragments.FeedPadFragment;
import edu.utaustin.yusun.yellerandroid.main_fragments.MainActivity;
import im.delight.android.location.SimpleLocation;

public class UploadImageActivity extends Activity {
    private Bitmap bitmap;
    private String upload_url;
    private String activity;
    Context context = this;
    private SimpleLocation location;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_image);

        // construct a new instance of SimpleLocation
        location = new SimpleLocation(this);
        // if we can't access the location yet
        if (!location.hasLocationEnabled()) {
            // ask the user to enable location access
            SimpleLocation.openSettings(this);
        }

        // Set up the action bar.
        final ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        bitmap = (Bitmap) intent.getParcelableExtra(FeedPadFragment.BITMAPIMAGE);
        activity = intent.getStringExtra("activity");
        BootstrapThumbnail imageView = (BootstrapThumbnail) findViewById(R.id.imageView);
        imageView.setImageBitmap(bitmap);
        upload_url = getUploadURL();
    }

    //upload image button action
    public void upload_image(View v) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] b = baos.toByteArray();
        byte[] encodedImage = Base64.encode(b, Base64.DEFAULT);
        RequestParams params = new RequestParams();
        params.put("file", new ByteArrayInputStream(b));
        final String user_email = MainActivity.user_email;
        params.put("User_email", user_email);
        params.put("activity", activity);
        params.put("latitude", location.getLatitude());
        params.put("longitude", location.getLongitude());


        if (activity.equals("FeedPad")) {
            EditText pictureText = (EditText) findViewById(R.id.pictureTags);
            String picture_content = pictureText.getText().toString();
            params.put("content", picture_content);
        }
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(upload_url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Toast.makeText(context, "Upload Successful", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(context, "Upload Unsuccessful", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                final CharSequence[] items = {"Yes!", "Opps..Stay here"};
                AlertDialog.Builder builder = new AlertDialog.Builder(UploadImageActivity.this);
                builder.setTitle("Are you sure to give up the upload?");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (items[item].equals("Yes!")) {
                            Intent intent = new Intent(UploadImageActivity.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        } else if (items[item].equals("Opps..Stay here")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public String getUploadURL(){
        AsyncHttpClient httpClient = new AsyncHttpClient();
        String request_url="http://socialyeller.appspot.com/android_getUploadUrl";
        System.out.println(request_url);
        httpClient.get(request_url, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                Toast.makeText(context, "Connect Successfully", Toast.LENGTH_SHORT).show();
                try {
                    JSONObject jObject = new JSONObject(new String(response));
                    upload_url = jObject.getString("upload_url");
//                    postToServer(encodedImage, photoCaption, upload_url);

                } catch (JSONException j) {
                    System.out.println("JSON Error");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                Toast.makeText(context, "Connect Unsuccessfully", Toast.LENGTH_SHORT).show();
            }
        });
        return upload_url;
    }
}
