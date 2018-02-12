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
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.rajdeep1008.cryptowatch.R;
import io.github.rajdeep1008.cryptowatch.adapter.AlertAdapter;
import io.github.rajdeep1008.cryptowatch.data.AlertCrypto;
import io.github.rajdeep1008.cryptowatch.data.AlertDao;
import io.github.rajdeep1008.cryptowatch.data.AppDatabase;
import io.github.rajdeep1008.cryptowatch.data.Crypto;
import io.github.rajdeep1008.cryptowatch.extras.Utilities;
import io.github.rajdeep1008.cryptowatch.rest.ResponseCallback;
import io.github.rajdeep1008.cryptowatch.rest.ServiceGenerator;

public class WatchListFragment extends Fragment {

    @BindView(R.id.main_rv)
    RecyclerView mainRv;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.empty_tv)
    TextView emptyTv;

    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;

    private AlertAdapter mAdapter;
    private AppDatabase appDatabase;
    private AlertDao alertDao;
    private ServiceGenerator generator;
    private SharedPreferences prefs;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new AlertAdapter(new ArrayList<AlertCrypto>(), getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_watch_list, container, false);

        ButterKnife.bind(this, rootView);
        appDatabase = AppDatabase.getInstance(getActivity());
        alertDao = appDatabase.alertDao();
        generator = new ServiceGenerator();

        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

        mainRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        mainRv.setAdapter(mAdapter);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadWatchList();
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
                Log.d("test", "running the watchlist");
                loadWatchList();
                handler.postDelayed(this, 120000);
            }
        });
    }

    public void loadWatchList() {
        progressBar.setVisibility(View.VISIBLE);
        final List<String> favoriteIds = Utilities.getWatchlist(getActivity().getApplicationContext());

        if (favoriteIds != null && favoriteIds.size() != 0) {
            mainRv.setVisibility(View.VISIBLE);
            emptyTv.setVisibility(View.GONE);

            List<AlertCrypto> alertCryptos = alertDao.getAll();
            final List<AlertCrypto> updatedList = new ArrayList<>();

            for (final AlertCrypto item : alertCryptos) {
                generator.getSingleCrypto(new ResponseCallback<Crypto>() {
                    @Override
                    public void success(Crypto crypto) {
                        progressBar.setVisibility(View.GONE);
                        AlertCrypto temp = item;
                        temp.setPrice(Utilities.getPriceWithoutCode(crypto, item.getCurrencySymbol()));
                        updatedList.add(temp);
                        mAdapter.addAll(updatedList);
                    }

                    @Override
                    public void failure(Crypto crypto) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getActivity(), "Error in updating data.", Toast.LENGTH_SHORT).show();
                    }
                }, item.getId(), item.getCurrencySymbol());
            }

        } else {
            progressBar.setVisibility(View.GONE);
            mainRv.setVisibility(View.GONE);
            emptyTv.setVisibility(View.VISIBLE);
        }
    }
}
