package io.adie.upscoot;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class ViewImageActivity extends AppCompatActivity {
    static final String TAG = ViewImageActivity.class.getSimpleName();
    private Tracker mTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().hide();

        setContentView(R.layout.activity_view_image);

        ViewImageFragment f = new ViewImageFragment();

        Bundle b = getIntent().getBundleExtra("url");
        f.setArguments(b);

        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.view_image_container, f).addToBackStack(TAG).commit();

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
}
