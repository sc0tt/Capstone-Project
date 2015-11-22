package io.adie.upscoot;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import io.adie.upscoot.adapters.UploadedImageAdapter;

public class UploadedImagesActivity extends AppCompatActivity implements UploadedImageAdapter.ViewImageListener {
    private static final String TAG = UploadedImagesActivity.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private SwipeRefreshLayout mRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploaded_images);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRecyclerView = (RecyclerView) findViewById(R.id.uploadedContainer);
        mRefresh = (SwipeRefreshLayout) findViewById(R.id.refreshUploaded);
        mLayoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new UploadedImageAdapter(UploadedImagesActivity.this);
        mRecyclerView.setAdapter(mAdapter);

        mRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        ((UploadedImageAdapter)mAdapter).updateDataSet();
                        mRefresh.setRefreshing(false);
                    }
                });
            }
        });
    }

    public void onImageClicked(String key) {
        Intent intent = new Intent(this, ViewImageActivity.class);
        intent.putExtra("url", key);
        startActivity(intent);
    }

}
