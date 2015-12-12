package edu.utaustin.yusun.yellerandroid.login_register;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import edu.utaustin.yusun.yellerandroid.R;
import edu.utaustin.yusun.yellerandroid.main_fragments.MainActivity;


public class RegisterActivity extends Activity {
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Register Button
        final BootstrapButton register_button = (BootstrapButton) findViewById(R.id.btnRegister);
        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText idText = (EditText) findViewById(R.id.reg_fullname);
                final String newUser_id = idText.getText().toString();
                EditText mailText = (EditText) findViewById(R.id.reg_email);
                final String newUser_email = mailText.getText().toString();
                EditText passwordText = (EditText) findViewById(R.id.reg_password);
                final String newUser_password = passwordText.getText().toString();

                //Check Validity
                boolean cancel = false;
                View focusView = null;

                // Check for a valid password, if the user entered one.
                if (TextUtils.isEmpty(newUser_password)) {
                    passwordText.setError(getString(R.string.error_field_required));
                    focusView = passwordText;
                    cancel = true;
                } else if (!isPasswordValid(newUser_password)) {
                    passwordText.setError(getString(R.string.error_invalid_password));
                    focusView = passwordText;
                    cancel = true;
                }

                // Check for a valid email address.
                if (TextUtils.isEmpty(newUser_email)) {
                    mailText.setError(getString(R.string.error_field_required));
                    focusView = mailText;
                    cancel = true;
                } else if (!isEmailValid(newUser_email)) {
                    mailText.setError(getString(R.string.error_invalid_email));
                    focusView = mailText;
                    cancel = true;
                }

                // Check for a valid user name, if the user entered one.
                if (TextUtils.isEmpty(newUser_id)) {
                    idText.setError(getString(R.string.error_field_required));
                    focusView = idText;
                    cancel = true;
                }

                if (cancel) {
                    // There was an error; don't attempt register and focus the first
                    // form field with an error.
                    focusView.requestFocus();
                } else {
                    RequestParams params = new RequestParams();
                    params.put("newUser_id", newUser_id);
                    params.put("newUser_email", newUser_email);
                    params.put("newUser_password", newUser_password);

                    String register_url = "http://socialyeller.appspot.com/android_register";
                    AsyncHttpClient httpClient = new AsyncHttpClient();
                    httpClient.get(register_url, params, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                        Log.w("async", "success!!!!");
//                        Toast.makeText(context, "Connect Successfully", Toast.LENGTH_SHORT).show();
                            try {
                                JSONObject jObject = new JSONObject(new String(responseBody));
                                String if_newUser = jObject.getString("newUser");
                                if (if_newUser.equals("1")) {
                                    Intent mainactivity = new Intent(RegisterActivity.this, MainActivity.class);
                                    mainactivity.putExtra("user_email", newUser_email);
                                    startActivity(mainactivity);
                                } else {
                                    Toast.makeText(context, "The e-mail has been registered, please sign in", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException j) {
                                System.out.println("JSON Error");
                            }
                        }


                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable e) {
                            Log.e("Posting_to_blob", "There was a problem in retrieving the url : " + e.toString());
                        }
                    });
                }
            }
        });

        TextView loginScreen = (TextView) findViewById(R.id.link_to_login);

        // Listening to Login Screen link
        loginScreen.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                finish();
            }
        });


    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

}
