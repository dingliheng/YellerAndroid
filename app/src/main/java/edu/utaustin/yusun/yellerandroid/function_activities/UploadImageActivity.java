package edu.utaustin.yusun.yellerandroid.function_activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.beardedhen.androidbootstrap.BootstrapThumbnail;

import edu.utaustin.yusun.yellerandroid.R;
import edu.utaustin.yusun.yellerandroid.main_fragments.FeedPadFragment;
import edu.utaustin.yusun.yellerandroid.main_fragments.MainActivity;

public class UploadImageActivity extends Activity {
    private Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_image);
        // Set up the action bar.
        final ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        bitmap = (Bitmap) intent.getParcelableExtra(FeedPadFragment.BITMAPIMAGE);
        BootstrapThumbnail imageView = (BootstrapThumbnail) findViewById(R.id.imageView);
        imageView.setImageBitmap(bitmap);
    }

    //upload image button action
    public void upload_image(View v) {

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
}
