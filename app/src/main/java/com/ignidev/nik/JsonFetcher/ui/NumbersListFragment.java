package com.ignidev.nik.JsonFetcher.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.ignidev.nik.JsonFetcher.utility.NetworkUtils;
import com.ignidev.nik.JsonFetcher.R;
import com.ignidev.nik.JsonFetcher.core.Number;
import com.ignidev.nik.JsonFetcher.core.NumbersAdapter;

import java.util.List;

public class NumbersListFragment extends Fragment {

    private ProgressBar progressView;
    private View emptyView;
    private RecyclerView listView;

    private final NetworkUtils.OnDownloadFinishedCallback downloadCallback = new NetworkUtils.OnDownloadFinishedCallback() {
        @Override
        public void onDownloadStarted() {
            changeProgressVisibility(true);
        }

        @Override
        public void onDownloadFinished(String rawData) {
            Log.i(getClass().getSimpleName(), "onDownloadFinished: ");
            changeProgressVisibility(false);
            List<Number> data = NetworkUtils.parseJsonToNumbers(rawData);
            if (data.isEmpty()) {
                emptyView.setVisibility(View.VISIBLE);
                listView.setVisibility(View.GONE);
            } else {
                listView.setAdapter(new NumbersAdapter(getContext(), data));
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.numbers_fragment, container, false);
        progressView = view.findViewById(R.id.progress);
        emptyView = view.findViewById(R.id.empty_view);
        listView = view.findViewById(R.id.list_view);
        listView.setLayoutManager(new LinearLayoutManager(getContext()));
        listView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        createAndFillAdapter(getString(R.string.json_test_url));//todo: update to R.string.json_url when it's ready
        return view;
    }

    private void createAndFillAdapter(String urlString) {
        NetworkUtils.fetchJsonData(urlString, downloadCallback);
    }

    private void changeProgressVisibility(boolean visible) {
        progressView.setVisibility(visible ? View.VISIBLE : View.GONE);
    }
}
