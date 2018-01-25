package io.github.rajdeep1008.cryptofolio.fragment;

import android.os.Bundle;
import android.os.Handler;
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
    private List<Crypto> mainList;

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
        generator = new ServiceGenerator();

        mainRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        mainRv.setAdapter(mAdapter);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
                refreshLayout.setRefreshing(false);
            }
        });

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                Log.d("test", "running networking");
                loadData();
                handler.postDelayed(this, 180000);
            }
        });
    }

    public void loadData() {
        generator.getFeed(new ResponseCallback<List<Crypto>>() {
            @Override
            public void success(List<Crypto> cryptos) {
                progressBar.setVisibility(View.GONE);
                mainList = cryptos;
                mAdapter.addAll(mainList);

                if (cryptoDao.getCryptoCount() == 0) {
                    cryptoDao.insertAll(mainList);
                } else {
                    cryptoDao.updateAll(mainList);
                }
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

    public void showSearchList(String text) {
        List<Crypto> searchList = cryptoDao.searchCryptos("%" + text.toUpperCase() + "%", "%" + text.toLowerCase() + "%");
        mAdapter.addAll(searchList);
    }

    public void showMainList() {
        mAdapter.addAll(mainList);
    }
}
