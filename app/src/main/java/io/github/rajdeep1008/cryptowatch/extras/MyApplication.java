package io.github.rajdeep1008.cryptowatch.extras;

import android.app.Application;

import com.facebook.stetho.Stetho;

/**
 * Created by rajdeep1008 on 06/02/18.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
    }
}
