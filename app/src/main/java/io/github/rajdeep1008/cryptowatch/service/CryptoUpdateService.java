package io.github.rajdeep1008.cryptowatch.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

import java.util.List;

import io.github.rajdeep1008.cryptowatch.R;
import io.github.rajdeep1008.cryptowatch.activity.MainActivity;
import io.github.rajdeep1008.cryptowatch.data.AlertCrypto;
import io.github.rajdeep1008.cryptowatch.data.AlertDao;
import io.github.rajdeep1008.cryptowatch.data.AppDatabase;
import io.github.rajdeep1008.cryptowatch.data.Crypto;
import io.github.rajdeep1008.cryptowatch.extras.Utilities;
import io.github.rajdeep1008.cryptowatch.rest.ResponseCallback;
import io.github.rajdeep1008.cryptowatch.rest.ServiceGenerator;

/**
 * Created by rajdeep1008 on 24/01/18.
 */

public class CryptoUpdateService extends JobService {

    private static final String TAG = "CryptoUpdateJob";
    private ServiceGenerator serviceGenerator;
    private AppDatabase appDatabase;
    private AlertDao alertDao;

    @Override
    public boolean onStartJob(final JobParameters job) {
        appDatabase = AppDatabase.getInstance(this);
        alertDao = appDatabase.alertDao();
        serviceGenerator = new ServiceGenerator();

        final List<AlertCrypto> cryptos = alertDao.getAll();
        if (cryptos.size() == 0) {
            jobFinished(job, false);
        }

        for (int i = 0; i < cryptos.size(); i++) {
            final int finalI = i;
            serviceGenerator.getSingleCrypto(new ResponseCallback<Crypto>() {
                @Override
                public void success(Crypto crypto) {
                    AlertCrypto mainCrypto = cryptos.get(finalI);
                    Double price = Double.valueOf(Utilities.getPriceWithoutCode(crypto, mainCrypto.getCurrencySymbol()));

                    if (mainCrypto.getUpperPrice() != null) {
                        if (price > Double.valueOf(mainCrypto.getUpperPrice())) {
                            String heading = "Crypto Watch";
                            String body = mainCrypto.getName() + " (" + mainCrypto.getSymbol() + ") > " + mainCrypto.getUpperPrice() + mainCrypto.getCurrencySymbol();
                            alertDao.removeFromList(mainCrypto.getId());
                            Utilities.removefromWatchlist(CryptoUpdateService.this, mainCrypto.getId());
                            sendNotification(crypto.getRank(), heading, body);
                        }
                    }

                    if (mainCrypto.getLowerPrice() != null) {
                        if (price < Double.valueOf(mainCrypto.getLowerPrice())) {
                            String heading = "Crypto Watch";
                            String body = mainCrypto.getName() + " (" + mainCrypto.getSymbol() + ") < " + mainCrypto.getUpperPrice() + mainCrypto.getCurrencySymbol();
                            alertDao.removeFromList(mainCrypto.getId());
                            Utilities.removefromWatchlist(CryptoUpdateService.this, mainCrypto.getId());
                            sendNotification(crypto.getRank(), heading, body);
                        }
                    }

                    if (finalI == cryptos.size() - 1) {
                        jobFinished(job, false);
                    }
                }

                @Override
                public void failure(Crypto crypto) {
                    jobFinished(job, true);
                }
            }, cryptos.get(i).getId(), cryptos.get(i).getCurrencySymbol());
        }
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        return true;
    }

    private void sendNotification(String rank, String title, String messageBody) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Bitmap notificationBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
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
