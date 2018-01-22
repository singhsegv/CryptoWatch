package io.github.rajdeep1008.cryptofolio.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.rajdeep1008.cryptofolio.R;
import io.github.rajdeep1008.cryptofolio.adapter.MainPagerAdapter;
import io.github.rajdeep1008.cryptofolio.extras.BottomNavigationViewHelper;
import io.github.rajdeep1008.cryptofolio.extras.NonSwipeableViewPager;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.bottom_navigation)
    BottomNavigationView bottomNav;

    @BindView(R.id.main_vp)
    NonSwipeableViewPager mPager;

    private MainPagerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        mAdapter = new MainPagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mAdapter);
        mPager.setOffscreenPageLimit(5);

        BottomNavigationViewHelper.disableShiftMode(bottomNav);
        bottomNav.setSelectedItemId(R.id.action_feed);
        mPager.setCurrentItem(2);

        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_favorites:
                        mPager.setCurrentItem(0);
                        return true;
                    case R.id.action_watchlist:
                        mPager.setCurrentItem(1);
                        return true;
                    case R.id.action_feed:
                        mPager.setCurrentItem(2);
                        return true;
                    case R.id.action_portfolio:
                        mPager.setCurrentItem(3);
                        return true;
                    case R.id.action_settings:
                        mPager.setCurrentItem(4);
                        return true;
                }
                return false;
            }
        });
    }
}
