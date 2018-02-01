package io.github.rajdeep1008.cryptodroid.extras;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.github.rajdeep1008.cryptodroid.data.Crypto;

/**
 * Created by rajdeep1008 on 24/01/18.
 */

public class Utilities {

    public static final String PREFS_NAME = "CRYPTO_SETTINGS";
    public static final String FAVORITES_KEY = "CRYPTO_FAVORITES";

    public static boolean checkFavorite(Context context, Crypto item) {
        SharedPreferences preferences = context.getSharedPreferences(PREFS_NAME, context.MODE_PRIVATE);
        Set<String> favoriteSet = preferences.getStringSet(FAVORITES_KEY, new HashSet<String>());
        if (favoriteSet.contains(item.getId())) {
            return true;
        } else {
            return false;
        }
    }

    public static void addFavorites(Context context, String id) {
        SharedPreferences preferences = context.getSharedPreferences(PREFS_NAME, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Set<String> favoriteSet = preferences.getStringSet(FAVORITES_KEY, new HashSet<String>());
        Set<String> tempSet = new HashSet<>(favoriteSet);
        tempSet.add(id);
        editor.putStringSet(FAVORITES_KEY, tempSet);
        editor.commit();
    }

    public static void removeFavorites(Context context, String id) {
        SharedPreferences preferences = context.getSharedPreferences(PREFS_NAME, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Set<String> favoriteSet = preferences.getStringSet(FAVORITES_KEY, new HashSet<String>());
        Set<String> tempSet = new HashSet<>(favoriteSet);
        tempSet.remove(id);
        editor.putStringSet(FAVORITES_KEY, tempSet);
        editor.commit();
    }

    public static ArrayList<String> getFavorites(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFS_NAME, context.MODE_PRIVATE);
        List<String> favoriteList;
        Set<String> favoriteSet = preferences.getStringSet(FAVORITES_KEY, null);

        if (favoriteSet != null) {
            favoriteList = new ArrayList<>(favoriteSet);
        } else {
            return null;
        }
        return (ArrayList<String>) favoriteList;
    }

    //    public Job createJob(FirebaseJobDispatcher dispatcher) {
//        int periodicity = (int) TimeUnit.MINUTES.toSeconds(6);
//        int toleranceInterval = (int) TimeUnit.MINUTES.toSeconds(1);
//
//        Job job = dispatcher.newJobBuilder()
//                .setService(CryptoUpdateService.class)
//                .setTag("CryptoUpdateJob")
//                .setLifetime(Lifetime.FOREVER)
//                .setReplaceCurrent(true)
//                .setRecurring(true)
//                .setTrigger(Trigger.executionWindow(periodicity - toleranceInterval, periodicity))
//                .setConstraints(Constraint.ON_ANY_NETWORK)
//                .build();
//
//        return job;
//    }

//        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(getActivity()));
//        Job job = createJob(dispatcher);
//        dispatcher.schedule(job);
}
