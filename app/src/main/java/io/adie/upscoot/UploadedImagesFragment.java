package io.adie.upscoot;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.adie.upscoot.adapters.UploadedImageAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class UploadedImagesFragment extends Fragment {
    private static final String TAG = UploadedImagesFragment.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private SwipeRefreshLayout mRefresh;

    public UploadedImagesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_uploaded_images, container, false);

        mRecyclerView = (RecyclerView) v.findViewById(R.id.uploadedContainer);
        mRefresh = (SwipeRefreshLayout) v.findViewById(R.id.refreshUploaded);
        mLayoutManager = new GridLayoutManager(getContext(), 2);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new UploadedImageAdapter(getActivity());
        mRecyclerView.setAdapter(mAdapter);

        mRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        ((UploadedImageAdapter) mAdapter).updateDataSet();
                        mRefresh.setRefreshing(false);
                    }
                });
            }
        });
        return v;
    }

}
