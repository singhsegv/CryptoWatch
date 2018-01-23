package io.github.rajdeep1008.cryptofolio.rest;

import java.util.List;

import io.github.rajdeep1008.cryptofolio.model.Crypto;
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

    private static String BASE_URL = "https://api.coinmarketcap.com/v1/";
    public static final String IMAGE_URL = "https://files.coinmarketcap.com/static/img/coins/32x32/%s.png";

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

    public void getFeed(final ResponseCallback<List<Crypto>> callback) {
        ApiClient apiClient = createService(ApiClient.class);
        Call<List<Crypto>> call = apiClient.getCryptoData(0);

        call.enqueue(new Callback<List<Crypto>>() {
            @Override
            public void onResponse(Call<List<Crypto>> call, Response<List<Crypto>> response) {
                callback.success(response.body());
            }

            @Override
            public void onFailure(Call<List<Crypto>> call, Throwable t) {
            }
        });
    }
}
