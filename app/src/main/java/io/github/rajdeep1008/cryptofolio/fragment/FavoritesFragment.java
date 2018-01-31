package io.github.rajdeep1008.cryptofolio.fragment;

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
import io.github.rajdeep1008.cryptofolio.R;
import io.github.rajdeep1008.cryptofolio.adapter.CryptoAdapter;
import io.github.rajdeep1008.cryptofolio.data.AppDatabase;
import io.github.rajdeep1008.cryptofolio.data.Crypto;
import io.github.rajdeep1008.cryptofolio.data.CryptoDao;
import io.github.rajdeep1008.cryptofolio.extras.Utilities;
import io.github.rajdeep1008.cryptofolio.rest.ResponseCallback;
import io.github.rajdeep1008.cryptofolio.rest.ServiceGenerator;

public class FavoritesFragment extends Fragment {

    @BindView(R.id.main_rv)
    RecyclerView mainRv;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.empty_tv)
    TextView emptyTv;

    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;

    private CryptoAdapter mAdapter;
    private AppDatabase appDatabase;
    private CryptoDao cryptoDao;
    private ServiceGenerator generator;
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
        View rootView = inflater.inflate(R.layout.fragment_favorites, container, false);

        ButterKnife.bind(this, rootView);
        appDatabase = AppDatabase.getInstance(getActivity());
        cryptoDao = appDatabase.cryptoDao();
        generator = new ServiceGenerator();

        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
                if (s.equals("default_currency")) {
                    loadFavorites(sharedPreferences.getString(s, "USD"));
                } else if (s.equals("sort_key")) {

                }
            }
        };

        prefs.registerOnSharedPreferenceChangeListener(listener);

        mainRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        mainRv.setAdapter(mAdapter);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadFavorites(prefs.getString("default_currency", "USD"));
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
                Log.d("test", "running");
                loadFavorites(prefs.getString("default_currency", "USD"));
                handler.postDelayed(this, 180000);
            }
        });
    }

    public void loadFavorites(String currency) {
        final List<String> favoriteIds = Utilities.getFavorites(getActivity().getApplicationContext());

        if (favoriteIds != null && favoriteIds.size() != 0) {
            mainRv.setVisibility(View.VISIBLE);
            emptyTv.setVisibility(View.GONE);

            generator.getFeed(new ResponseCallback<List<Crypto>>() {
                @Override
                public void success(List<Crypto> cryptos) {
                    emptyTv.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);

                    List<Crypto> favoriteList = new ArrayList<>();

                    for (Crypto item : cryptos) {
                        for (String id : favoriteIds) {
                            if (item.getId().equals(id)) {
                                favoriteList.add(item);
                                break;
                            }
                        }
                    }

                    mAdapter.addAll(favoriteList, prefs.getString("default_currency", "USD"));
                }

                @Override
                public void failure(List<Crypto> cryptos) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "Error in loading", Toast.LENGTH_SHORT).show();
                }
            }, currency);
        } else {
            progressBar.setVisibility(View.GONE);
            mainRv.setVisibility(View.GONE);
            emptyTv.setVisibility(View.VISIBLE);
        }
    }
}
