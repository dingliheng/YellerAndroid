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

import com.beardedhen.androidbootstrap.BootstrapEditText;

import edu.utaustin.yusun.yellerandroid.R;

/**
 * Created by yusun on 12/11/15.
 */
public class CommentDialog extends Dialog {
    private BootstrapEditText commment;

    public CommentDialog(Context context) {
        super(context);

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
