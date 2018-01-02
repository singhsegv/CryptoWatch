package io.github.rajdeep1008.cryptofolio;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

import io.github.rajdeep1008.cryptofolio.rest.ResponseCallback;

public class MainActivity extends AppCompatActivity {

    ServiceGenerator generator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        generator = new ServiceGenerator();
        generator.getFeed(new ResponseCallback<List<Crypto>>() {
            @Override
            public void success(List<Crypto> cryptos) {
                Log.d("test", "success");
            }

            @Override
            public void failure(List<Crypto> cryptos) {

            }
        });

    }
}
