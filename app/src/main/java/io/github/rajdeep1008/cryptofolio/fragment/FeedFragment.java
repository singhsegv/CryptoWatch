package io.github.rajdeep1008.cryptofolio.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.rajdeep1008.cryptofolio.R;
import io.github.rajdeep1008.cryptofolio.adapter.CryptoAdapter;
import io.github.rajdeep1008.cryptofolio.data.Crypto;
import io.github.rajdeep1008.cryptofolio.data.CryptoDao;
import io.github.rajdeep1008.cryptofolio.extras.AppDatabase;
import io.github.rajdeep1008.cryptofolio.extras.Utilities;
import io.github.rajdeep1008.cryptofolio.rest.ResponseCallback;
import io.github.rajdeep1008.cryptofolio.rest.ServiceGenerator;

public class FeedFragment extends Fragment {

    @BindView(R.id.main_rv)
    RecyclerView mainRv;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;

    private ServiceGenerator generator;
    private CryptoAdapter mAdapter;
    private AppDatabase appDatabase;
    private CryptoDao cryptoDao;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new CryptoAdapter(new ArrayList<Crypto>(), getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_feed, container, false);
        ButterKnife.bind(this, rootView);
        appDatabase = AppDatabase.getInstance(getActivity());
        cryptoDao = appDatabase.cryptoDao();

        mainRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        mainRv.setAdapter(mAdapter);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

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
    }

    public void loadData() {
        generator.getFeed(new ResponseCallback<List<Crypto>>() {
            @Override
            public void success(List<Crypto> cryptos) {
                progressBar.setVisibility(View.GONE);
                List<Crypto> list = cryptos;
                mAdapter.addAll(list);
                cryptoDao.clearTable();
                cryptoDao.insertAll(list);
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
