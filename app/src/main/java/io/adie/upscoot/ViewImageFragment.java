package io.adie.upscoot;


import android.app.Fragment;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.koushikdutta.ion.Ion;


/**
 * A simple {@link Fragment} subclass.
 */
public class ViewImageFragment extends Fragment {
    private static final String TAG = ViewImageFragment.class.getSimpleName();

    public ViewImageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_view_image, container, false);

        Bundle b = getArguments();

        final String url = b.getString("url");

        Log.d(TAG, String.format("Viewing %s in full.", url));

        FloatingActionButton mControlsView = (FloatingActionButton) v.findViewById(R.id.copyFab);
        TouchImageView mContentView = (TouchImageView) v.findViewById(R.id.fullscreen_content);

        // Use the URL instead of the cached image. GIFs will not animated when loaded from the cache.
        Ion.with(mContentView)
                .placeholder(R.drawable.ic_cached_24dp)
                .error(R.drawable.ic_error_24dp)
                .load(url);

        // Set up the user interaction to manually show or hide the system UI.
        mControlsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText(getResources().getString(R.string.package_copy), url);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getActivity(), getResources().getString(R.string.copied_toast_text), Toast.LENGTH_SHORT).show();
            }
        });

        return v;
    }

}
