package edu.utaustin.yusun.yellerandroid.function_activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapButtonGroup;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.seismic.ShakeDetector;

import cz.msebera.android.httpclient.Header;
import edu.utaustin.yusun.yellerandroid.R;
import edu.utaustin.yusun.yellerandroid.main_fragments.MainActivity;
import im.delight.android.location.SimpleLocation;

public class PublishMoodActivity extends Activity implements ShakeDetector.Listener {
    Context context = this;
    BootstrapButton mood_button;

    private SimpleLocation location;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final String user_email = MainActivity.user_email;
        System.out.println("PUBLISH_EMAIL: "+user_email);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_mood);


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

        final BootstrapButtonGroup targetGroup = (BootstrapButtonGroup) findViewById(R.id.options);
        targetGroup.getChildAt(0).setSelected(true);
        //publish button action
        mood_button = (BootstrapButton) findViewById(R.id.publish_btn);
        mood_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String to_whom;
                boolean anonymous = findViewById(R.id.anonymous).isSelected();
                if (targetGroup.getChildAt(0).isSelected()) {           //To public is selected
                    to_whom = "public";
                } else if ((targetGroup.getChildAt(1).isSelected())) {  //To friends is selected
                    to_whom = "friend";
                } else {                                                //To self is selected
                    to_whom = "self";
                }
                EditText yellerText = (EditText) findViewById(R.id.text_content);
                String yeller_content = yellerText.getText().toString();

                RequestParams params = new RequestParams();
                params.put("User_email", user_email);
                params.put("to:", to_whom);
                params.put("content", yeller_content);
                params.put("is_Anonynmous", anonymous);
                params.put("latitude", location.getLatitude());
                params.put("longitude", location.getLongitude());

                String pulibshMood_url = "http://socialyeller.appspot.com/android_publishmood";
                AsyncHttpClient httpClient = new AsyncHttpClient();
                httpClient.get(pulibshMood_url, params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        Toast.makeText(context, "Upload Successful", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Toast.makeText(context, "Upload Unsuccessful", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        ShakeDetector sd = new ShakeDetector(this);
        sd.start(sensorManager);
    }
    @Override
    public void hearShake() {
        Toast.makeText(this, "Shake to send!", Toast.LENGTH_SHORT).show();
        mood_button.performClick();
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                final CharSequence[] items = {"Yes!", "Opps..Stay here"};
                AlertDialog.Builder builder = new AlertDialog.Builder(PublishMoodActivity.this);
                builder.setTitle("Are you sure to give up the upload?");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (items[item].equals("Yes!")) {
//                            Intent intent = new Intent(PublishMoodActivity.this, MainActivity.class);
//                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                            startActivity(intent);
                            finish();
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


}
