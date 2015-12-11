package edu.utaustin.yusun.yellerandroid.function_activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

import cz.msebera.android.httpclient.Header;
import edu.utaustin.yusun.yellerandroid.R;
import edu.utaustin.yusun.yellerandroid.main_fragments.MainActivity;

public class PublishMoodActivity extends Activity {
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final String user_email = MainActivity.user_email;
        System.out.println("PUBLISH_EMAIL: "+user_email);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_mood);

        // Set up the action bar.
        final ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        final BootstrapButtonGroup targetGroup = (BootstrapButtonGroup) findViewById(R.id.options);
        targetGroup.getChildAt(0).setSelected(true);
        //publish button action
        final BootstrapButton mood_button = (BootstrapButton) findViewById(R.id.publish_btn);
        mood_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String to_whom;
                boolean anonymous = findViewById(R.id.anonymous).isSelected();
                if (targetGroup.getChildAt(0).isSelected()) {           //To public is selected
                    publish(0, anonymous);
                    to_whom = "public";
                } else if ((targetGroup.getChildAt(1).isSelected())) {  //To friends is selected
                    publish(1, anonymous);
                    to_whom = "friend";
                } else {                                                //To self is selected
                    publish(2, anonymous);
                    to_whom = "self";
                }
                EditText yellerText = (EditText) findViewById(R.id.text_content);
                String yeller_content = yellerText.getText().toString();

                RequestParams params = new RequestParams();
                params.put("User_email", user_email);
                params.put("to:", to_whom);
                params.put("content", yeller_content);
                params.put("is_Anonynmous", anonymous);

                String pulibshMood_url = "http://socialyeller.appspot.com/android_publishmood";
                AsyncHttpClient httpClient = new AsyncHttpClient();
                httpClient.get(pulibshMood_url, params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        Toast.makeText(context, "Upload Successfully", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Toast.makeText(context, "Upload Unsuccessfully", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    public void publish (int i, boolean anonymous) {
        switch (i) {
            case 0:
                break;
            case 1:
                break;
            case 2:
                break;
        }

        //To transfer to backend..

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
                            Intent intent = new Intent(PublishMoodActivity.this, MainActivity.class);
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
}
