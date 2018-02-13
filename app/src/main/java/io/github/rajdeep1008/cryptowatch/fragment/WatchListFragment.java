package io.github.rajdeep1008.cryptowatch.fragment;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.rajdeep1008.cryptowatch.R;
import io.github.rajdeep1008.cryptowatch.activity.MainActivity;
import io.github.rajdeep1008.cryptowatch.adapter.AlertAdapter;
import io.github.rajdeep1008.cryptowatch.data.AlertCrypto;
import io.github.rajdeep1008.cryptowatch.data.AlertDao;
import io.github.rajdeep1008.cryptowatch.data.AppDatabase;
import io.github.rajdeep1008.cryptowatch.data.Crypto;
import io.github.rajdeep1008.cryptowatch.extras.Utilities;
import io.github.rajdeep1008.cryptowatch.rest.ResponseCallback;
import io.github.rajdeep1008.cryptowatch.rest.ServiceGenerator;
import io.github.rajdeep1008.cryptowatch.service.CryptoUpdateService;

public class WatchListFragment extends Fragment {

    @BindView(R.id.main_rv)
    RecyclerView mainRv;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.empty_tv)
    TextView emptyTv;

    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;

    private AlertAdapter mAdapter;
    private AppDatabase appDatabase;
    private AlertDao alertDao;
    private ServiceGenerator generator;
    private SharedPreferences prefs;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new AlertAdapter(new ArrayList<AlertCrypto>(), getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_watch_list, container, false);

        ButterKnife.bind(this, rootView);
        appDatabase = AppDatabase.getInstance(getActivity());
        alertDao = appDatabase.alertDao();
        generator = new ServiceGenerator();

        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

        mainRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        mainRv.setAdapter(mAdapter);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadWatchList();
                refreshLayout.setRefreshing(false);
            }
        });
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                loadWatchList();
                checkPrices();
                handler.postDelayed(this, 120000);
            }
        });
    }

    public void loadWatchList() {
        progressBar.setVisibility(View.VISIBLE);
        final List<String> favoriteIds = Utilities.getWatchlist(getActivity().getApplicationContext());

        if (favoriteIds != null && favoriteIds.size() != 0) {
            mainRv.setVisibility(View.VISIBLE);
            emptyTv.setVisibility(View.GONE);

            List<AlertCrypto> alertCryptos = alertDao.getAll();
            final List<AlertCrypto> updatedList = new ArrayList<>();

            for (final AlertCrypto item : alertCryptos) {
                generator.getSingleCrypto(new ResponseCallback<Crypto>() {
                    @Override
                    public void success(Crypto crypto) {
                        progressBar.setVisibility(View.GONE);
                        AlertCrypto temp = item;
                        temp.setPrice(Utilities.getPriceWithoutCode(crypto, item.getCurrencySymbol()));
                        updatedList.add(temp);
                        mAdapter.addAll(updatedList);
                    }

                    @Override
                    public void failure(Crypto crypto) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getActivity(), "Error in updating data.", Toast.LENGTH_SHORT).show();
                    }
                }, item.getId(), item.getCurrencySymbol());
            }

        } else {
            progressBar.setVisibility(View.GONE);
            mainRv.setVisibility(View.GONE);
            emptyTv.setVisibility(View.VISIBLE);
        }
    }

    public void checkPrices() {
        final List<AlertCrypto> cryptos = alertDao.getAll();
        if (cryptos.size() == 0) {
            return;
        }

        for (int i = 0; i < cryptos.size(); i++) {
            final int finalI = i;
            generator.getSingleCrypto(new ResponseCallback<Crypto>() {
                @Override
                public void success(Crypto crypto) {
                    AlertCrypto mainCrypto = cryptos.get(finalI);
                    Double price = Double.valueOf(Utilities.getPriceWithoutCode(crypto, mainCrypto.getCurrencySymbol()));

                    if (mainCrypto.getUpperPrice() != null) {
                        if (price > Double.valueOf(mainCrypto.getUpperPrice())) {
                            String heading = "Crypto Watch";
                            String body = mainCrypto.getName() + " (" + mainCrypto.getSymbol() + ") > " + mainCrypto.getUpperPrice() + mainCrypto.getCurrencySymbol();
                            alertDao.removeFromList(mainCrypto.getId());
                            Utilities.removefromWatchlist(getActivity(), mainCrypto.getId());
                            sendNotification(crypto.getRank(), heading, body);
                        }
                    }

                    if (mainCrypto.getLowerPrice() != null) {
                        if (price < Double.valueOf(mainCrypto.getLowerPrice())) {
                            String heading = "Crypto Watch";
                            String body = mainCrypto.getName() + " (" + mainCrypto.getSymbol() + ") < " + mainCrypto.getUpperPrice() + mainCrypto.getCurrencySymbol();
                            alertDao.removeFromList(mainCrypto.getId());
                            Utilities.removefromWatchlist(getActivity(), mainCrypto.getId());
                            sendNotification(crypto.getRank(), heading, body);
                        }
                    }
                }

                @Override
                public void failure(Crypto crypto) {

                }
            }, cryptos.get(i).getId(), cryptos.get(i).getCurrencySymbol());
        }
    }

    private void sendNotification(String rank, String title, String messageBody) {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(getActivity(), 0, intent, PendingIntent.FLAG_ONE_SHOT);
        NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        Bitmap notificationBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getActivity())
                .setContentTitle(title)
                .setSmallIcon(R.drawable.icon)
                .setLargeIcon(notificationBitmap)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        notificationManager.notify(Integer.parseInt(rank), notificationBuilder.build());
    }
}
