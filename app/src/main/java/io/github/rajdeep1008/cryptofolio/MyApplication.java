package io.github.rajdeep1008.cryptofolio;

import android.app.Application;

import com.facebook.stetho.Stetho;

/**
 * Created by rajdeep1008 on 24/01/18.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
    }
}
