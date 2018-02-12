package io.github.rajdeep1008.cryptowatch.rest;

import android.util.Log;

import java.util.List;

import io.github.rajdeep1008.cryptowatch.data.Crypto;
import io.github.rajdeep1008.cryptowatch.data.History;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by rajdeep1008 on 17/12/17.
 */

public class ServiceGenerator {

    public static final String BASE_URL = "https://api.coinmarketcap.com/v1/";
    public static final String IMAGE_URL = "https://files.coinmarketcap.com/static/img/coins/128x128/%s.png";
    public static final String HISTORY_URL = "http://coincap.io/history/";

    private static OkHttpClient httpClient = new OkHttpClient.Builder()
            .addInterceptor(new HTTPLoggingInterceptor())
            .build();

    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient);

    private static Retrofit retrofit = builder.build();

    public static <S> S createService(Class<S> serviceClass) {
        return retrofit.create(serviceClass);
    }

    public void getFeed(final ResponseCallback<List<Crypto>> callback, String currency) {
        builder = changeApiBaseUrl(BASE_URL);
        retrofit = builder.build();
        ApiClient apiClient = createService(ApiClient.class);
        Call<List<Crypto>> call = apiClient.getCryptoData(0, currency);

        call.enqueue(new Callback<List<Crypto>>() {
            @Override
            public void onResponse(Call<List<Crypto>> call, Response<List<Crypto>> response) {
                callback.success(response.body());
            }

            @Override
            public void onFailure(Call<List<Crypto>> call, Throwable t) {
                callback.failure(null);
            }
        });
    }

    public void getSingleCrypto(final ResponseCallback<Crypto> callback, String id, String currency) {
        builder = changeApiBaseUrl(BASE_URL);
        retrofit = builder.build();
        ApiClient apiClient = createService(ApiClient.class);

        Call<List<Crypto>> call = apiClient.getSingleCrypto(id, currency);
        call.enqueue(new Callback<List<Crypto>>() {
            @Override
            public void onResponse(Call<List<Crypto>> call, Response<List<Crypto>> response) {
                callback.success(response.body().get(0));
            }

            @Override
            public void onFailure(Call<List<Crypto>> call, Throwable t) {
                Log.d("test", t.getMessage());
                callback.failure(null);
            }
        });
    }

    public void getHistory(final ResponseCallback<History> callback, String duration, String symbol) {
        builder = changeApiBaseUrl(HISTORY_URL);
        retrofit = builder.build();
        ApiClient apiClient = createService(ApiClient.class);
        Call<History> call = apiClient.getCryptoHistory(duration, symbol);

        call.enqueue(new Callback<History>() {
            @Override
            public void onResponse(Call<History> call, Response<History> response) {
                callback.success(response.body());
            }

            @Override
            public void onFailure(Call<History> call, Throwable t) {
                callback.failure(null);
            }
        });
    }

    public Retrofit.Builder changeApiBaseUrl(String baseUrl) {
        Retrofit.Builder tempBuilder = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient);
        return tempBuilder;
    }
}
