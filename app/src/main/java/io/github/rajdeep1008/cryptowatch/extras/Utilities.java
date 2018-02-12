package io.github.rajdeep1008.cryptowatch.extras;

import android.content.Context;
import android.content.SharedPreferences;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import io.github.rajdeep1008.cryptowatch.data.AlertCrypto;
import io.github.rajdeep1008.cryptowatch.data.Crypto;

/**
 * Created by rajdeep1008 on 24/01/18.
 */

public class Utilities {

    public static final String PREFS_NAME = "CRYPTO_SETTINGS";
    public static final String FAVORITES_KEY = "CRYPTO_FAVORITES";
    public static final String WATCHLIST_KEY = "CRYPTO_WATCHLIST";
    public static DecimalFormat df = new DecimalFormat(".####");

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

    public static boolean checkWatchlist(Context context, AlertCrypto item) {
        SharedPreferences preferences = context.getSharedPreferences(PREFS_NAME, context.MODE_PRIVATE);
        Set<String> watchlistSet = preferences.getStringSet(WATCHLIST_KEY, new HashSet<String>());
        if (watchlistSet.contains(item.getId())) {
            return true;
        } else {
            return false;
        }
    }

    public static void addToWatchlist(Context context, String id) {
        SharedPreferences preferences = context.getSharedPreferences(PREFS_NAME, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Set<String> watchSet = preferences.getStringSet(WATCHLIST_KEY, new HashSet<String>());
        Set<String> tempSet = new HashSet<>(watchSet);
        tempSet.add(id);
        editor.putStringSet(WATCHLIST_KEY, tempSet);
        editor.commit();
    }

    public static void removefromWatchlist(Context context, String id) {
        SharedPreferences preferences = context.getSharedPreferences(PREFS_NAME, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Set<String> watchSet = preferences.getStringSet(WATCHLIST_KEY, new HashSet<String>());
        Set<String> tempSet = new HashSet<>(watchSet);
        tempSet.remove(id);
        editor.putStringSet(WATCHLIST_KEY, tempSet);
        editor.commit();
    }

    public static ArrayList<String> getWatchlist(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFS_NAME, context.MODE_PRIVATE);
        List<String> watchList;
        Set<String> watchSet = preferences.getStringSet(WATCHLIST_KEY, null);

        if (watchSet != null) {
            watchList = new ArrayList<>(watchSet);
        } else {
            return null;
        }
        return (ArrayList<String>) watchList;
    }

    public static String getPrice(Crypto item, String currency) {
        String price = "";
        switch (currency) {
            case "USD":
                price += getCurrencyCode("en", "US") + df.format(Double.valueOf(item.getPriceUsd()));
                break;
            case "AUD":
                price += getCurrencyCode("en", "AU") + df.format(Double.valueOf(item.getPriceAud()));
                break;
            case "CAD":
                price += getCurrencyCode("en", "CA") + df.format(Double.valueOf(item.getPriceCad()));
                break;
            case "EUR":
                price += getCurrencyCode("en", "EU") + df.format(Double.valueOf(item.getPriceEur()));
                break;
            case "HKD":
                price += getCurrencyCode("en", "HK") + df.format(Double.valueOf(item.getPriceHkd()));
                break;
            case "GBP":
                price += getCurrencyCode("en", "GB") + df.format(Double.valueOf(item.getPriceGbp()));
                break;
            case "JPY":
                price += getCurrencyCode("en", "JP") + df.format(Double.valueOf(item.getPriceJpy()));
                break;
            case "INR":
                price += getCurrencyCode("en", "IN") + df.format(Double.valueOf(item.getPriceInr()));
                break;
        }
        return price;
    }

    public static String getPriceWithoutCode(Crypto item, String currency) {
        String price = "";
        switch (currency) {
            case "USD":
                price += df.format(Double.valueOf(item.getPriceUsd()));
                break;
            case "AUD":
                price += df.format(Double.valueOf(item.getPriceAud()));
                break;
            case "CAD":
                price += df.format(Double.valueOf(item.getPriceCad()));
                break;
            case "EUR":
                price += df.format(Double.valueOf(item.getPriceEur()));
                break;
            case "HKD":
                price += df.format(Double.valueOf(item.getPriceHkd()));
                break;
            case "GBP":
                price += df.format(Double.valueOf(item.getPriceGbp()));
                break;
            case "JPY":
                price += df.format(Double.valueOf(item.getPriceJpy()));
                break;
            case "INR":
                price += df.format(Double.valueOf(item.getPriceInr()));
                break;
        }
        return price;
    }

    public static String getCurrencyCode(String languageCode, String countryCode) {
        Locale locale = new Locale(languageCode, countryCode);
        Currency currency = Currency.getInstance(locale);
        String symbol = currency.getSymbol();

        return symbol;
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
