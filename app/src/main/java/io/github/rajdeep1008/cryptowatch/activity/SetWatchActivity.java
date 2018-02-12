package io.github.rajdeep1008.cryptowatch.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.rajdeep1008.cryptowatch.R;
import io.github.rajdeep1008.cryptowatch.data.AlertCrypto;
import io.github.rajdeep1008.cryptowatch.data.AlertDao;
import io.github.rajdeep1008.cryptowatch.data.AppDatabase;
import io.github.rajdeep1008.cryptowatch.data.Crypto;
import io.github.rajdeep1008.cryptowatch.extras.Utilities;
import io.github.rajdeep1008.cryptowatch.rest.ServiceGenerator;

public class SetWatchActivity extends AppCompatActivity {

    @BindView(R.id.symbol_iv)
    ImageView symbolIv;

    @BindView(R.id.name_tv)
    TextView nameTv;

    @BindView(R.id.price_tv)
    TextView priceTv;

    @BindView(R.id.above_cb)
    CheckBox aboveCb;

    @BindView(R.id.below_cb)
    CheckBox belowCb;

    @BindView(R.id.above_et)
    EditText aboveEt;

    @BindView(R.id.below_et)
    EditText belowEt;

    @BindView(R.id.save_tv)
    TextView saveTv;

    private Crypto currentCrypto;
    private SharedPreferences prefs;
    private AppDatabase appDatabase;
    private AlertDao alertDao;
    private String price;
    private String rawPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_watch);

        ButterKnife.bind(this);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        appDatabase = AppDatabase.getInstance(this);
        alertDao = appDatabase.alertDao();
        hideKeyboard();

        if (getIntent() != null) {
            if (getIntent().getExtras() != null) {
                if (getIntent().getExtras().getParcelable("crypto") != null) {
                    currentCrypto = getIntent().getExtras().getParcelable("crypto");
                }
            }
        }

        if (currentCrypto == null) {
            Toast.makeText(this, "Error in loading.", Toast.LENGTH_SHORT).show();
            finishActivity();
        }

        price = Utilities.getPrice(currentCrypto, prefs.getString("default_currency", "USD"));
        rawPrice = Utilities.getPriceWithoutCode(currentCrypto, prefs.getString("default_currency", "USD"));

        nameTv.setText(currentCrypto.getName() + " (" + currentCrypto.getSymbol() + ")");
        priceTv.setText(price);
        aboveEt.setText(rawPrice);
        belowEt.setText(rawPrice);
        Glide.with(this).load(String.format(ServiceGenerator.IMAGE_URL, currentCrypto.getId())).into(symbolIv);

        aboveCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                showSave();
                if (b) {
                    aboveEt.setEnabled(b);
                } else {
                    aboveEt.setEnabled(b);
                }
            }
        });

        belowCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                showSave();
                if (b) {
                    belowEt.setEnabled(b);
                } else {
                    belowEt.setEnabled(b);
                }
            }
        });
    }

    @OnClick(R.id.back_btn)
    public void finishActivity() {
        onBackPressed();
    }

    @OnClick(R.id.save_tv)
    public void saveToWatchlist() {
        String lowerPrice = "", upperPrice = "";

        if (alertDao.getById(currentCrypto.getId()) != null) {
            alertDao.removeFromList(currentCrypto.getId());
        }

        AlertCrypto item = new AlertCrypto(currentCrypto.getId(),
                currentCrypto.getSymbol(),
                currentCrypto.getName(),
                rawPrice,
                null,
                null,
                prefs.getString("default_currency", "USD"));

        if (aboveCb.isChecked()) {
            upperPrice = aboveEt.getText().toString().trim();
            item.setUpperPrice(upperPrice);

            if (upperPrice.matches(".*[a-zA-Z].*") || upperPrice.length() == 0) {
                Toast.makeText(this, "Incorrect price", Toast.LENGTH_SHORT).show();
                return;
            }

            if (Double.parseDouble(upperPrice) < Double.parseDouble(rawPrice)) {
                Toast.makeText(this, "Upper price can't be smaller than the current one", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if (belowCb.isChecked()) {
            lowerPrice = belowEt.getText().toString().trim();
            item.setLowerPrice(lowerPrice);

            if (lowerPrice.matches(".*[a-zA-Z].*") || lowerPrice.length() == 0) {
                Toast.makeText(this, "Incorrect price", Toast.LENGTH_SHORT).show();
                return;
            }

            if (Double.parseDouble(lowerPrice) > Double.parseDouble(rawPrice)) {
                Toast.makeText(this, "Lower price can't be greater than the current one", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if (upperPrice.equals(lowerPrice)) {
            Toast.makeText(this, "Upper and lower prices can't be same", Toast.LENGTH_SHORT).show();
            return;
        }

        alertDao.insertSingle(item);
        Utilities.addToWatchlist(this, currentCrypto.getId());
        Toast.makeText(this, "Alert set for " + currentCrypto.getName(), Toast.LENGTH_SHORT).show();
        finish();
    }

    public void showSave() {
        if (aboveCb.isChecked() || belowCb.isChecked()) {
            saveTv.setVisibility(View.VISIBLE);
        } else {
            saveTv.setVisibility(View.GONE);
        }
    }

    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
