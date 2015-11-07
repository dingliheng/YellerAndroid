package edu.utaustin.yusun.yellerandroid;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;

public class UploadImageActivity extends Activity {
    private Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_image);
        Intent intent = getIntent();
        bitmap = (Bitmap) intent.getParcelableExtra(LaunchpadSectionFragment.BITMAPIMAGE);
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setImageBitmap(bitmap);
    }
}
