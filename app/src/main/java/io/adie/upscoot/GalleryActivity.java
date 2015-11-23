package io.adie.upscoot;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.koushikdutta.ion.Ion;

import io.adie.upscoot.adapters.GalleryAdapter;

public class GalleryActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, GalleryFragment.OnFragmentInteractionListener, GalleryAdapter.ViewImageListener  {

    private static final String TAG = GalleryActivity.class.getSimpleName();
    public static final int SELECT_PICTURE = 1;
    static boolean isMasterDetails = false;
    private Tracker mTracker;
    AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Ion.getDefault(getApplicationContext()).getConscryptMiddleware().enable(false);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if(findViewById(R.id.view_image_container) != null) {
            GalleryActivity.isMasterDetails = true;
        }

        UpscootApplication application = (UpscootApplication) getApplication();
        mTracker = application.getDefaultTracker();

        try {
            mAdView = (AdView) findViewById(R.id.adView);
            AdRequest adRequest = new AdRequest.Builder()
                    .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                    .addTestDevice("BBC597A1A5D8D97DADB65A62DD6C30E0")
                    .build();
            mAdView.loadAd(adRequest);
        } catch (NullPointerException err) {
            Log.e(TAG, "Exception while displaying ad", err);
        }
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
                .setCategory("Gallery")
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

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /*public void onImageClicked(String key) {
        Intent intent = new Intent(this, ViewImageActivity.class);
        intent.putExtra("url", key);
        startActivity(intent);
    }*/

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_gallery) {

        } else if (id == R.id.uploads) {
            Intent uploadedImagesIntent = new Intent(GalleryActivity.this, UploadedImagesActivity.class);
            startActivity(uploadedImagesIntent);
        } else if (id == R.id.settings) {
            Intent settingsIntent = new Intent(GalleryActivity.this, SettingsActivity.class);
            startActivity(settingsIntent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void onFragmentInteraction(Uri uri) {
        // Do nothing.
    }
}
