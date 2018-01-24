package io.github.rajdeep1008.cryptofolio.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

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

public class FavoritesFragment extends Fragment {

    @BindView(R.id.main_rv)
    RecyclerView mainRv;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.empty_tv)
    TextView emptyTv;

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
        View rootView = inflater.inflate(R.layout.fragment_favorites, container, false);
        ButterKnife.bind(this, rootView);

        appDatabase = AppDatabase.getInstance(getActivity());
        cryptoDao = appDatabase.cryptoDao();

        mainRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        mainRv.setAdapter(mAdapter);

        List<String> favoriteList = Utilities.getFavorites(getActivity().getApplicationContext());
        if (favoriteList != null) {
            emptyTv.setVisibility(View.GONE);
            List<Crypto> list = cryptoDao.loadFavorites(favoriteList);
            mAdapter.addAll(list);
        } else {
            emptyTv.setVisibility(View.VISIBLE);
        }

        return rootView;
    }
}
