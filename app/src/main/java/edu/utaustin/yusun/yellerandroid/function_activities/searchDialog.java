package edu.utaustin.yusun.yellerandroid.function_activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import edu.utaustin.yusun.yellerandroid.R;
import edu.utaustin.yusun.yellerandroid.friends_activities.FriendListActivity;

/**
 * Created by yusun on 12/8/15.
 */
public class SearchDialog extends Dialog {

    private EditText search;

    public SearchDialog(Context context) {
        super(context);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_dialog_layout);
        Window window = getWindow();
        WindowManager.LayoutParams wmlp = window.getAttributes();
        wmlp.gravity = Gravity.TOP;
        wmlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(wmlp);
        initialize();
    }

    private void initialize() {
        search = (EditText) findViewById(R.id.search_text);
        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEARCH && search.getText().length() > 0) {
                    Intent intent = new Intent(getContext(), FriendListActivity.class);
                    intent.putExtra("view", "Search");
                    intent.putExtra("key", search.getText().toString());
                    getContext().startActivity(intent);
                    dismiss();
                }
                return true;
            }
        });

        search.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                }
            }
        });


    }

}
