package io.github.rajdeep1008.cryptowatch.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.rajdeep1008.cryptowatch.R;
import io.github.rajdeep1008.cryptowatch.adapter.MainPagerAdapter;
import io.github.rajdeep1008.cryptowatch.extras.BottomNavigationViewHelper;
import io.github.rajdeep1008.cryptowatch.extras.NonSwipeableViewPager;
import io.github.rajdeep1008.cryptowatch.fragment.FavoritesFragment;
import io.github.rajdeep1008.cryptowatch.fragment.FeedFragment;
import io.github.rajdeep1008.cryptowatch.fragment.WatchListFragment;
import io.github.rajdeep1008.cryptowatch.service.CryptoUpdateService;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.bottom_navigation)
    BottomNavigationView bottomNav;

    @BindView(R.id.main_vp)
    NonSwipeableViewPager mPager;

    @BindView(R.id.search_btn)
    ImageButton searchBtn;

    @BindView(R.id.search_et)
    EditText searchEt;

    @BindView(R.id.back_btn)
    ImageButton backBtn;

    @BindView(R.id.heading_tv)
    TextView headingTv;

    private MainPagerAdapter mAdapter;
    private int lastSelected;
    private SharedPreferences prefs;
    private FirebaseJobDispatcher dispatcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        mAdapter = new MainPagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mAdapter);
        //TODO Change this to 5
        mPager.setOffscreenPageLimit(4);

        BottomNavigationViewHelper.disableShiftMode(bottomNav);
        bottomNav.setSelectedItemId(R.id.action_feed);
        lastSelected = R.id.action_feed;
        mPager.setCurrentItem(2);

        final FeedFragment feedFragment = (FeedFragment) mPager.getAdapter().instantiateItem(mPager, 2);

        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_favorites:
                        onBackClicked();
                        searchBtn.setVisibility(View.GONE);
                        mPager.setCurrentItem(0);
                        lastSelected = R.id.action_favorites;
                        return true;
                    case R.id.action_watchlist:
                        onBackClicked();
                        searchBtn.setVisibility(View.GONE);
                        mPager.setCurrentItem(1);
                        lastSelected = R.id.action_watchlist;
                        return true;
                    case R.id.action_feed:
                        if (lastSelected == R.id.action_feed) {
                            feedFragment.scrollToTop();
                        } else {
                            mPager.setCurrentItem(2);
                        }
                        searchBtn.setVisibility(View.VISIBLE);
                        lastSelected = R.id.action_feed;
                        return true;
//                    case R.id.action_portfolio:
//                        onBackClicked();
//                        searchBtn.setVisibility(View.GONE);
//                        mPager.setCurrentItem(3);
//                        lastSelected = R.id.action_portfolio;
//                        return true;
                    case R.id.action_settings:
                        onBackClicked();
                        searchBtn.setVisibility(View.GONE);
                        //TODO change this to 4
                        mPager.setCurrentItem(3);
                        lastSelected = R.id.action_settings;
                        return true;
                }
                return false;
            }
        });

        searchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().length() == 0) {
                    feedFragment.showMainList();
                } else {
                    feedFragment.showSearchList(charSequence.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        searchEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                searchEt.post(new Runnable() {
                    @Override
                    public void run() {
                        InputMethodManager imm = (InputMethodManager) MainActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(searchEt, InputMethodManager.SHOW_IMPLICIT);
                    }
                });
            }
        });

        dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(this));
        Job job = createJob(dispatcher);
        dispatcher.schedule(job);
    }

    @Override
    public void onBackPressed() {
        if (searchEt.getVisibility() == View.VISIBLE) {
            onBackClicked();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        updateFavorites();
        updateWatchlist();
    }

    public void updateFavorites() {
        FavoritesFragment fragment = (FavoritesFragment) mPager.getAdapter().instantiateItem(mPager, 0);
        fragment.loadFavorites(prefs.getString("default_currency", "USD"));
    }

    public void updateWatchlist() {
        WatchListFragment fragment = (WatchListFragment) mPager.getAdapter().instantiateItem(mPager, 1);
        fragment.loadWatchList();
    }

    @OnClick(R.id.search_btn)
    public void showSearchBar() {
        searchEt.setVisibility(View.VISIBLE);
        searchEt.requestFocus();
        backBtn.setVisibility(View.VISIBLE);
        searchBtn.setVisibility(View.GONE);
        headingTv.setVisibility(View.GONE);
    }

    @OnClick(R.id.back_btn)
    public void onBackClicked() {
        searchEt.setText("");
        searchEt.setVisibility(View.GONE);
        searchEt.clearFocus();
        hideKeyboard();
        backBtn.setVisibility(View.GONE);
        searchBtn.setVisibility(View.VISIBLE);
        headingTv.setVisibility(View.VISIBLE);
    }

    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public Job createJob(FirebaseJobDispatcher dispatcher) {
        int periodicity = (int) TimeUnit.MINUTES.toSeconds(2);
        int toleranceInterval = (int) TimeUnit.MINUTES.toSeconds(1);

        Job job = dispatcher.newJobBuilder()
                .setService(CryptoUpdateService.class)
                .setTag("CryptoUpdateJob")
                .setLifetime(Lifetime.FOREVER)
                .setReplaceCurrent(true)
                .setRecurring(true)
                .setTrigger(Trigger.executionWindow(periodicity - toleranceInterval, periodicity))
                .setConstraints(Constraint.ON_ANY_NETWORK)
                .build();

        return job;
    }
}
