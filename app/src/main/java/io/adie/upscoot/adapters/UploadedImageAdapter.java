package io.adie.upscoot.adapters;


import android.app.Activity;
import android.app.Application;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.bitmap.BitmapInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.adie.upscoot.R;
import io.adie.upscoot.tables.UploadedImage;

public class UploadedImageAdapter extends RecyclerView.Adapter<UploadedImageAdapter.ViewHolder> {
    private static final String TAG = GalleryAdapter.class.getSimpleName();

    private List<String> mDataset;
    private Map<String, String> cache = new HashMap<>();
    private ViewImageListener mListener;
    private Application ctx;

    public UploadedImageAdapter(Activity act) {
        mListener = (ViewImageListener) act;
        this.ctx = act.getApplication();
        updateDataSet();
    }

    @Override
    public UploadedImageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.gallery_image_item, parent, false);
        ViewHolder vh = new ViewHolder(v);

        vh.mImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ImageButton ib = (ImageButton) v;
                BitmapInfo bi = Ion.with(ib).getBitmapInfo();
                if (bi.key != null)
                    mListener.onImageClicked(bi.key);
            }
        });

        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // Set by position here.
        if (!cache.containsKey(mDataset.get(position))) {
            Ion.with(holder.mImageButton)
                    //.centerCrop()
                    //.crossfade(true)
                    .placeholder(R.drawable.ic_cached_24dp)
                    .error(R.drawable.ic_error_24dp)
                    .load(mDataset.get(position))
                    .setCallback(new FutureCallback<ImageView>() {
                        @Override
                        public void onCompleted(Exception e, ImageView result) {
                            if (result != null) {
                                BitmapInfo bi = Ion.with(result).getBitmapInfo();
                                cache.put(mDataset.get(position), bi.key);
                            }
                        }
                    });
        } else {
            String key = cache.get(mDataset.get(position));
            BitmapInfo bi = Ion.getDefault(ctx).getBitmapCache().get(key);
            if (bi != null) {
                holder.mImageButton.setImageBitmap(bi.bitmap);
            } else {
                holder.mImageButton.setImageResource(R.drawable.ic_error_24dp);
            }
        }

    }

    @Override
    public int getItemCount() {
        return mDataset == null ? 0 : mDataset.size();
    }


    public void updateDataSet() {
        mDataset = new ArrayList<>();
        List<UploadedImage> images = UploadedImage.listAll(UploadedImage.class);

        Log.d(TAG, String.format("Images found: %d", images.size()));

        if (images.size() > 0) {
            for (UploadedImage img : images) {
                mDataset.add(img.getURL());
            }
            Collections.reverse(mDataset);
            notifyDataSetChanged();
        }

    }


    public interface ViewImageListener {
        void onImageClicked(String key);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageButton mImageButton;

        public ViewHolder(View v) {
            super(v);
            mImageButton = (ImageButton) itemView.findViewById(R.id.img_container);
            mImageButton.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
    }
}
