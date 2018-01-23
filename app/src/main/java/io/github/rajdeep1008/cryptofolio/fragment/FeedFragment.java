package io.github.rajdeep1008.cryptofolio.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.rajdeep1008.cryptofolio.R;
import io.github.rajdeep1008.cryptofolio.adapter.CryptoAdapter;
import io.github.rajdeep1008.cryptofolio.model.Crypto;
import io.github.rajdeep1008.cryptofolio.rest.ResponseCallback;
import io.github.rajdeep1008.cryptofolio.rest.ServiceGenerator;

public class FeedFragment extends Fragment {

    @BindView(R.id.main_rv)
    RecyclerView mainRv;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;

    ServiceGenerator generator;
    CryptoAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_feed, container, false);
        ButterKnife.bind(this, rootView);

        progressBar.setVisibility(View.VISIBLE);
        generator = new ServiceGenerator();
        loadData();

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
                refreshLayout.setRefreshing(false);
            }
        });

        return rootView;
    }

    public void loadData() {
        generator.getFeed(new ResponseCallback<List<Crypto>>() {
            @Override
            public void success(List<Crypto> cryptos) {
                progressBar.setVisibility(View.GONE);
                List<Crypto> list = cryptos;
                mAdapter = new CryptoAdapter(list, getActivity());
                mainRv.setLayoutManager(new LinearLayoutManager(getActivity()));
                mainRv.setAdapter(mAdapter);
            }

            @Override
            public void failure(List<Crypto> cryptos) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "Error in loading", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void scrollToTop() {
        mainRv.smoothScrollToPosition(0);
    }
}
