package io.github.rajdeep1008.cryptowatch.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.rajdeep1008.cryptowatch.R;
import io.github.rajdeep1008.cryptowatch.adapter.CryptoAdapter;
import io.github.rajdeep1008.cryptowatch.data.AppDatabase;
import io.github.rajdeep1008.cryptowatch.data.Crypto;
import io.github.rajdeep1008.cryptowatch.data.CryptoDao;
import io.github.rajdeep1008.cryptowatch.extras.CryptoComparator;
import io.github.rajdeep1008.cryptowatch.rest.ResponseCallback;
import io.github.rajdeep1008.cryptowatch.rest.ServiceGenerator;

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
    private SharedPreferences prefs;
    private SharedPreferences.OnSharedPreferenceChangeListener listener;

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

        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
                if (s.equals("default_currency")) {
                    loadData(sharedPreferences.getString(s, "USD"), prefs.getString("sort_key", "RANK"));
                } else if (s.equals("sort_key")) {
                    CryptoComparator comparatorName = getComparatorString(sharedPreferences.getString(s, "RANK"));
                    Collections.sort(mainList, CryptoComparator.getComparator(comparatorName));
                    mAdapter.addAll(mainList, prefs.getString("default_currency", "USD"));
                }
            }
        };

        prefs.registerOnSharedPreferenceChangeListener(listener);

        mainRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        mainRv.setAdapter(mAdapter);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData(prefs.getString("default_currency", "USD"), prefs.getString("sort_key", "RANK"));
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
                loadData(prefs.getString("default_currency", "USD"), prefs.getString("sort_key", "RANK"));
                handler.postDelayed(this, 180000);
            }
        });
    }

    public void loadData(final String currency, final String comparatorName) {
        generator.getFeed(new ResponseCallback<List<Crypto>>() {
            @Override
            public void success(List<Crypto> cryptos) {
                progressBar.setVisibility(View.GONE);
                mainList = cryptos;
                CryptoComparator comparator = getComparatorString(comparatorName);
                Collections.sort(mainList, CryptoComparator.getComparator(comparator));
                mAdapter.addAll(mainList, currency);

                if (cryptoDao.getCryptoCount() == 0) {
                    cryptoDao.insertAll(mainList);
                } else {
                    cryptoDao.clearTable();
                    cryptoDao.insertAll(mainList);
                }
            }

            @Override
            public void failure(List<Crypto> cryptos) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "Error in loading", Toast.LENGTH_SHORT).show();
            }
        }, currency);
    }

    public void scrollToTop() {
        mainRv.smoothScrollToPosition(0);
    }

    public void showSearchList(String text) {
        List<Crypto> searchList = cryptoDao.searchCryptos("%" + text.toUpperCase() + "%", "%" + text.toLowerCase() + "%");
        mAdapter.addAll(searchList, prefs.getString("default_currency", "USD"));
    }

    public void showMainList() {
        mAdapter.addAll(mainList, prefs.getString("default_currency", "USD"));
    }

    private CryptoComparator getComparatorString(String name) {
        CryptoComparator comparator = CryptoComparator.RANK;
        switch (name) {
            case "RANK":
                comparator = CryptoComparator.RANK;
                break;
            case "PHTL":
                comparator = CryptoComparator.PHTL;
                break;
            case "PLTH":
                comparator = CryptoComparator.PLTH;
                break;
            case "CHTL24":
                comparator = CryptoComparator.CHTL24;
                break;
            case "CLTH24":
                comparator = CryptoComparator.CLTH24;
                break;
            case "CHTL1":
                comparator = CryptoComparator.CHTL1;
                break;
            case "CLTH1":
                comparator = CryptoComparator.CLTH1;
                break;
        }
        return comparator;
    }
}
