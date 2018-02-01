package io.github.rajdeep1008.cryptodroid.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import io.github.rajdeep1008.cryptodroid.fragment.FavoritesFragment;
import io.github.rajdeep1008.cryptodroid.fragment.FeedFragment;
import io.github.rajdeep1008.cryptodroid.fragment.SettingsFragment;
import io.github.rajdeep1008.cryptodroid.fragment.WatchListFragment;

/**
 * Created by rajdeep1008 on 21/01/18.
 */

public class MainPagerAdapter extends FragmentStatePagerAdapter {

    public MainPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new FavoritesFragment();
            case 1:
                return new WatchListFragment();
            case 2:
                return new FeedFragment();
            //TODO return portfolio fragment
            case 3:
                return new SettingsFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        //TODO change this to 5
        return 4;
    }
}
