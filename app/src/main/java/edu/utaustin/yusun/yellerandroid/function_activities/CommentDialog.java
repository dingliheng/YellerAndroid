package edu.utaustin.yusun.yellerandroid.function_activities;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.nio.channels.AsynchronousCloseException;

import cz.msebera.android.httpclient.Header;
import edu.utaustin.yusun.yellerandroid.R;
import edu.utaustin.yusun.yellerandroid.adapter.PullToRefreshListViewAdapter;
import edu.utaustin.yusun.yellerandroid.main_fragments.MainActivity;

/**
 * Created by yusun on 12/11/15.
 */
public class CommentDialog extends Dialog {
    private BootstrapEditText commment;
    private PullToRefreshListViewAdapter adapter;
    private String feed_id;

    public CommentDialog(Context context, String feed_id, PullToRefreshListViewAdapter adapter) {
        super(context);
        this.adapter = adapter;
        this.feed_id = feed_id;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment_dialog_layout);
        Window window = getWindow();
        WindowManager.LayoutParams wmlp = window.getAttributes();
        wmlp.gravity = Gravity.BOTTOM;
        wmlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(wmlp);
        initialize();
    }

    private void initialize() {
        commment = (BootstrapEditText) findViewById(R.id.comment_sent);
        commment.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEND && commment.getText().length() > 0) {

                    RequestParams params = new RequestParams();
                    String comment_url = "http://socialyeller.appspot.com/android_comment";
                    params.put("yeller_id", feed_id);
                    System.out.println("comment id: " + feed_id);
                    params.put("User_email", MainActivity.user_email);
                    params.put("comment",commment.getText());
                    AsyncHttpClient httpClient = new AsyncHttpClient();
                    httpClient.post(comment_url, params, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                        }
                    });

                    adapter.notifyDataSetChanged();
                    dismiss();
                }
                return true;
            }
        });

        commment.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                }
            }
        });
    }
}
