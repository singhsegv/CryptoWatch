package io.github.rajdeep1008.cryptowatch.activity;

import android.content.Intent;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.rajdeep1008.cryptowatch.R;
import io.github.rajdeep1008.cryptowatch.data.Crypto;
import io.github.rajdeep1008.cryptowatch.data.History;
import io.github.rajdeep1008.cryptowatch.extras.Utilities;
import io.github.rajdeep1008.cryptowatch.rest.ResponseCallback;
import io.github.rajdeep1008.cryptowatch.rest.ServiceGenerator;

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

    @BindView(R.id.chart)
    LineChart chart;

    @BindView(R.id.day_tv)
    TextView dayTv;

    @BindView(R.id.week_tv)
    TextView weekTv;

    @BindView(R.id.month_tv)
    TextView monthTv;

    @BindView(R.id.half_year_tv)
    TextView halfYearTv;

    @BindView(R.id.year_tv)
    TextView yearTv;

    @BindView(R.id.chart_progress)
    ProgressBar chartProgress;

    @BindView(R.id.no_chart_tv)
    TextView noChartTv;

    private Crypto currentCrypto;
    private SharedPreferences prefs;
    private ServiceGenerator generator;
    private History historyData;

    private static final int TYPE_HOUR = 0;
    private static final int TYPE_DAY = 1;
    private static final int TYPE_WEEK = 2;

    public static final String DURATION_TYPE_DAY = "1day";
    public static final String DURATION_TYPE_WEEK = "7day";
    public static final String DURATION_TYPE_MONTH = "30day";
    public static final String DURATION_TYPE_HALF_YEAR = "180day";
    public static final String DURATION_TYPE_YEAR = "365day";

    List<Entry> entries = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ButterKnife.bind(this);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        generator = new ServiceGenerator();
        chart.setNoDataText("");

        if (getIntent() != null) {
            if (getIntent().getExtras() != null) {
                if (getIntent().getExtras().getParcelable("crypto") != null) {
                    chartProgress.setVisibility(View.VISIBLE);
                    currentCrypto = getIntent().getExtras().getParcelable("crypto");
                    loadChart(DURATION_TYPE_DAY);
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
        priceTv.setText(Utilities.getPrice(currentCrypto, prefs.getString("default_currency", "USD")));
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

        boolean favoriteNew = Utilities.checkFavorite(getApplicationContext(), currentCrypto);

        if (favoriteNew) {
            favBtn.setText("Remove from favorites");
        } else {
            favBtn.setText("Add to favorites");
        }
    }

    @OnClick(R.id.watch_btn)
    public void addToWatchList() {
        Intent intent = new Intent(this, SetWatchActivity.class);
        intent.putExtra("crypto", currentCrypto);
        startActivityForResult(intent, 1008);
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

    @OnClick({R.id.day_tv, R.id.week_tv, R.id.month_tv, R.id.half_year_tv, R.id.year_tv})
    public void handleChart(View view) {
        switch (view.getId()) {
            case R.id.day_tv:
                chart.clear();
                chartProgress.setVisibility(View.VISIBLE);
                loadChart(DURATION_TYPE_DAY);
                dayTv.setBackground(getResources().getDrawable(R.drawable.chart_tv_bg));
                weekTv.setBackgroundColor(getResources().getColor(R.color.white));
                monthTv.setBackgroundColor(getResources().getColor(R.color.white));
                halfYearTv.setBackgroundColor(getResources().getColor(R.color.white));
                yearTv.setBackgroundColor(getResources().getColor(R.color.white));
                break;
            case R.id.week_tv:
                chart.clear();
                chartProgress.setVisibility(View.VISIBLE);
                loadChart(DURATION_TYPE_WEEK);
                weekTv.setBackground(getResources().getDrawable(R.drawable.chart_tv_bg));
                dayTv.setBackgroundColor(getResources().getColor(R.color.white));
                monthTv.setBackgroundColor(getResources().getColor(R.color.white));
                halfYearTv.setBackgroundColor(getResources().getColor(R.color.white));
                yearTv.setBackgroundColor(getResources().getColor(R.color.white));
                break;
            case R.id.month_tv:
                chart.clear();
                chartProgress.setVisibility(View.VISIBLE);
                loadChart(DURATION_TYPE_MONTH);
                monthTv.setBackground(getResources().getDrawable(R.drawable.chart_tv_bg));
                weekTv.setBackgroundColor(getResources().getColor(R.color.white));
                dayTv.setBackgroundColor(getResources().getColor(R.color.white));
                halfYearTv.setBackgroundColor(getResources().getColor(R.color.white));
                yearTv.setBackgroundColor(getResources().getColor(R.color.white));
                break;
            case R.id.half_year_tv:
                chart.clear();
                chartProgress.setVisibility(View.VISIBLE);
                loadChart(DURATION_TYPE_HALF_YEAR);
                halfYearTv.setBackground(getResources().getDrawable(R.drawable.chart_tv_bg));
                weekTv.setBackgroundColor(getResources().getColor(R.color.white));
                monthTv.setBackgroundColor(getResources().getColor(R.color.white));
                dayTv.setBackgroundColor(getResources().getColor(R.color.white));
                yearTv.setBackgroundColor(getResources().getColor(R.color.white));
                break;
            case R.id.year_tv:
                chart.clear();
                chartProgress.setVisibility(View.VISIBLE);
                loadChart(DURATION_TYPE_YEAR);
                yearTv.setBackground(getResources().getDrawable(R.drawable.chart_tv_bg));
                weekTv.setBackgroundColor(getResources().getColor(R.color.white));
                monthTv.setBackgroundColor(getResources().getColor(R.color.white));
                halfYearTv.setBackgroundColor(getResources().getColor(R.color.white));
                dayTv.setBackgroundColor(getResources().getColor(R.color.white));
                break;
        }
    }

    public void loadChart(final String duration) {

        final IAxisValueFormatter formatter = new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                Date date = new Date((long) value);
                SimpleDateFormat dateFormat = null;
                switch (duration) {
                    case DURATION_TYPE_DAY:
                        dateFormat = new SimpleDateFormat("hh:mm");
                        break;
                    case DURATION_TYPE_WEEK:
                        dateFormat = new SimpleDateFormat("dd/MM");
                        break;
                    case DURATION_TYPE_MONTH:
                        dateFormat = new SimpleDateFormat("dd/MM");
                        break;
                    case DURATION_TYPE_HALF_YEAR:
                        dateFormat = new SimpleDateFormat("MM/yyyy");
                        break;
                    case DURATION_TYPE_YEAR:
                        dateFormat = new SimpleDateFormat("yyyy");
                        break;
                }
                String time = dateFormat.format(date);
                return time;
            }
        };

        generator.getHistory(new ResponseCallback<History>() {
            @Override
            public void success(History history) {
                if (history != null) {
                    noChartTv.setVisibility(View.GONE);
                    chartProgress.setVisibility(View.GONE);
                    chart.invalidate();
                    historyData = history;
                    for (List<Float> temp : historyData.getPriceList()) {
                        entries.add(new Entry(temp.get(0), temp.get(1)));
                    }
                    LineDataSet dataSet = new LineDataSet(entries, "");
                    LineData lineData = new LineData(dataSet);
                    XAxis xAxis = chart.getXAxis();
                    xAxis.setValueFormatter(formatter);
                    chart.getDescription().setText("Price in USD");
                    chart.getLegend().setEnabled(false);
                    chart.setData(lineData);
                } else {
                    noChartTv.setVisibility(View.VISIBLE);
                    chartProgress.setVisibility(View.GONE);
                }
            }

            @Override
            public void failure(History history) {
                chartProgress.setVisibility(View.GONE);
                Toast.makeText(DetailActivity.this, "Error in fetching data", Toast.LENGTH_SHORT).show();
            }
        }, duration, currentCrypto.getSymbol());
    }
}
