package io.github.rajdeep1008.cryptofolio.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.rajdeep1008.cryptofolio.R;
import io.github.rajdeep1008.cryptofolio.adapter.CryptoAdapter;
import io.github.rajdeep1008.cryptofolio.model.Crypto;
import io.github.rajdeep1008.cryptofolio.rest.ResponseCallback;
import io.github.rajdeep1008.cryptofolio.rest.ServiceGenerator;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.main_rv)
    RecyclerView mainRv;

    ServiceGenerator generator;
    CryptoAdapter mAdapter;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mContext = MainActivity.this;

        generator = new ServiceGenerator();
        generator.getFeed(new ResponseCallback<List<Crypto>>() {
            @Override
            public void success(List<Crypto> cryptos) {
                List<Crypto> list = cryptos;
                mAdapter = new CryptoAdapter(list, mContext);
                mainRv.setLayoutManager(new LinearLayoutManager(mContext));
                mainRv.setAdapter(mAdapter);
            }

            @Override
            public void failure(List<Crypto> cryptos) {
                Toast.makeText(mContext, "Error in loading", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
