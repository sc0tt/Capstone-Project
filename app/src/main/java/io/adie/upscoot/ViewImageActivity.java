package io.adie.upscoot;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.koushikdutta.ion.Ion;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class ViewImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().hide();

        setContentView(R.layout.activity_view_image);

        FloatingActionButton mControlsView = (FloatingActionButton)findViewById(R.id.copyFab);
        TouchImageView mContentView = (TouchImageView) findViewById(R.id.fullscreen_content);

        // Use the URL instead of the cached image. GIFs will not animated when loaded from the cache.
        final String url = getIntent().getStringExtra("url");

        Ion.with(mContentView)
                .placeholder(R.drawable.ic_cached_24dp)
                .error(R.drawable.ic_error_24dp)
                .load(url);

        // Set up the user interaction to manually show or hide the system UI.
        mControlsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("io.adie.upscoot", url);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getApplicationContext(), "URL copied to clipboard!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
