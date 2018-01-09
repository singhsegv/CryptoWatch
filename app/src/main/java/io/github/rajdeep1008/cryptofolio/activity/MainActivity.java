package io.github.rajdeep1008.cryptofolio.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.rajdeep1008.cryptofolio.R;
import io.github.rajdeep1008.cryptofolio.adapter.CryptoAdapter;
import io.github.rajdeep1008.cryptofolio.extras.BottomNavigationViewHelper;
import io.github.rajdeep1008.cryptofolio.model.Crypto;
import io.github.rajdeep1008.cryptofolio.rest.ResponseCallback;
import io.github.rajdeep1008.cryptofolio.rest.ServiceGenerator;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.main_rv)
    RecyclerView mainRv;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.prograss_bar)
    ProgressBar progressBar;

    @BindView(R.id.bottom_navigation)
    BottomNavigationView bottomNav;

    ServiceGenerator generator;
    CryptoAdapter mAdapter;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        mContext = MainActivity.this;

        BottomNavigationViewHelper.disableShiftMode(bottomNav);
        bottomNav.setSelectedItemId(R.id.action_feed);
        progressBar.setVisibility(View.VISIBLE);

        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_favorites:
                        return true;
                    case R.id.action_watchlist:
                        return true;
                    case R.id.action_feed:
                        return true;
                    case R.id.action_portfolio:
                        return true;
                    case R.id.action_settings:
                        return true;
                }
                return false;
            }
        });

        generator = new ServiceGenerator();
        generator.getFeed(new ResponseCallback<List<Crypto>>() {
            @Override
            public void success(List<Crypto> cryptos) {
                progressBar.setVisibility(View.GONE);
                List<Crypto> list = cryptos;
                mAdapter = new CryptoAdapter(list, mContext);
                mainRv.setLayoutManager(new LinearLayoutManager(mContext));
                mainRv.setAdapter(mAdapter);
            }

            @Override
            public void failure(List<Crypto> cryptos) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(mContext, "Error in loading", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
