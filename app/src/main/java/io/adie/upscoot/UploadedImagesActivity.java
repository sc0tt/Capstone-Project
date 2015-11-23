package io.adie.upscoot;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import io.adie.upscoot.adapters.UploadedImageAdapter;

public class UploadedImagesActivity extends AppCompatActivity implements UploadedImageAdapter.ViewImageListener {
    private static final String TAG = UploadedImagesActivity.class.getSimpleName();
    private Tracker mTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploaded_images);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        UpscootApplication application = (UpscootApplication) getApplication();
        mTracker = application.getDefaultTracker();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "Viewing screen: " + TAG);
        mTracker.setScreenName("Screen~" + TAG);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    public void onImageClicked(String key) {
        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("Uploads")
                .setAction("View")
                .setLabel(key)
                .build());
        Log.i(TAG, "Viewing image: " + key);
        mTracker.setScreenName("Image~" + key);

        Bundle b = new Bundle();
        b.putString("url", key);
        if (GalleryActivity.isMasterDetails) {
            ViewImageFragment f = new ViewImageFragment();

            f.setArguments(b);

            FragmentManager manager = getFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.view_image_container, f).addToBackStack(TAG).commit();
        } else {
            Intent intent = new Intent(this, ViewImageActivity.class);
            intent.putExtra("url", b);
            startActivity(intent);
        }
    }


}
