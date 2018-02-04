package io.github.rajdeep1008.cryptodroid.activity;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.text.DecimalFormat;
import java.util.Currency;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.rajdeep1008.cryptodroid.R;
import io.github.rajdeep1008.cryptodroid.data.Crypto;
import io.github.rajdeep1008.cryptodroid.extras.Utilities;
import io.github.rajdeep1008.cryptodroid.rest.ServiceGenerator;

public class DetailActivity extends AppCompatActivity {

    @BindView(R.id.heading_tv)
    TextView headingView;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.symbol_iv)
    ImageView symbolIv;

    @BindView(R.id.name_tv)
    TextView nameView;

    @BindView(R.id.price_tv)
    TextView priceTv;

    @BindView(R.id.hour_change_tv)
    TextView hourChangeTv;

    @BindView(R.id.day_change_tv)
    TextView dayChangeTv;

    @BindView(R.id.week_change_tv)
    TextView weekChangeTv;

    @BindView(R.id.mc_value_tv)
    TextView marketCapTv;

    @BindView(R.id.vol_value_tv)
    TextView volumeTv;

    @BindView(R.id.as_value_tv)
    TextView availableTv;

    @BindView(R.id.fav_btn)
    Button favBtn;

    Crypto currentCrypto;
    SharedPreferences prefs;
    DecimalFormat df = new DecimalFormat(".####");
    private static final int TYPE_HOUR = 0;
    private static final int TYPE_DAY = 1;
    private static final int TYPE_WEEK = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ButterKnife.bind(this);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);

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

        boolean favorite = Utilities.checkFavorite(getApplicationContext(), currentCrypto);

        if (favorite) {
            favBtn.setText("Remove from favorites");
        } else {
            favBtn.setText("Add to favorites");
        }

        headingView.setText("Rank " + currentCrypto.getRank());
        nameView.setText(currentCrypto.getName() + " (" + currentCrypto.getSymbol() + ")");
        priceTv.setText(getPrice(currentCrypto, prefs.getString("default_currency", "USD")));
        marketCapTv.setText("$" + currentCrypto.getMarketCapUsd());
        volumeTv.setText("$" + currentCrypto.get_24hVolumeUsd());
        availableTv.setText("$" + currentCrypto.getAvailableSupply());
        hourChangeTv.setText(getColoredChanges(currentCrypto, "Change 1h\n" + currentCrypto.getPercentChange1h() + "%", TYPE_HOUR));
        dayChangeTv.setText(getColoredChanges(currentCrypto, "Change 24h\n" + currentCrypto.getPercentChange24h() + "%", TYPE_DAY));
        weekChangeTv.setText(getColoredChanges(currentCrypto, "Change 7d\n" + currentCrypto.getPercentChange7d() + "%", TYPE_WEEK));
        Glide.with(this).load(String.format(ServiceGenerator.IMAGE_URL, currentCrypto.getId())).into(symbolIv);

    }

    @OnClick(R.id.back_btn)
    public void finishActivity() {
        onBackPressed();
    }

    @OnClick(R.id.fav_btn)
    public void addToFavorites() {
        boolean favorite = Utilities.checkFavorite(getApplicationContext(), currentCrypto);
        if (favorite) {
            Utilities.removeFavorites(getApplicationContext(), currentCrypto.getId());
            Toast.makeText(this, "Removed", Toast.LENGTH_SHORT).show();
        } else {
            Utilities.addFavorites(getApplicationContext(), currentCrypto.getId());
            Toast.makeText(this, "Added", Toast.LENGTH_SHORT).show();
        }
    }

    private String getPrice(Crypto item, String currency) {
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

    private String getCurrencyCode(String languageCode, String countryCode) {
        Locale locale = new Locale(languageCode, countryCode);
        Currency currency = Currency.getInstance(locale);
        String symbol = currency.getSymbol();

        return symbol;
    }

    private Spannable getColoredChanges(Crypto item, String value, int type) {
        Spannable temp = new SpannableString(value);

        switch (type) {
            case TYPE_HOUR:
                if (item.getPercentChange1h() != null) {
                    if (Double.parseDouble(item.getPercentChange1h()) < 0) {
                        temp.setSpan(new ForegroundColorSpan(Color.RED), 10, value.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    } else {
                        String tempString = value.substring(0, 10) + "+" + value.substring(10);
                        temp = new SpannableString(tempString);
                        temp.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.positiveGreen)), 10, tempString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                } else {
                    hourChangeTv.setVisibility(View.GONE);
                }
                break;
            case TYPE_DAY:
                if (item.getPercentChange24h() != null) {
                    if (Double.parseDouble(item.getPercentChange24h()) < 0) {
                        temp.setSpan(new ForegroundColorSpan(Color.RED), 10, value.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    } else {
                        String tempString = value.substring(0, 11) + "+" + value.substring(11);
                        temp = new SpannableString(tempString);
                        temp.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.positiveGreen)), 11, tempString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                } else {
                    dayChangeTv.setVisibility(View.GONE);
                }
                break;
            case TYPE_WEEK:
                if (item.getPercentChange7d() != null) {
                    if (Double.parseDouble(item.getPercentChange7d()) < 0) {
                        temp.setSpan(new ForegroundColorSpan(Color.RED), 10, value.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    } else {
                        String tempString = value.substring(0, 10) + "+" + value.substring(10);
                        temp = new SpannableString(tempString);
                        temp.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.positiveGreen)), 10, tempString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                } else {
                    weekChangeTv.setVisibility(View.GONE);
                }
                break;
        }
        return temp;
    }
}
