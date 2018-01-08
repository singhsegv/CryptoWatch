package io.github.rajdeep1008.cryptofolio.rest;

import java.util.List;

import io.github.rajdeep1008.cryptofolio.model.Crypto;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by rajdeep1008 on 17/12/17.
 */

public interface ApiClient {

    @GET("ticker")
    Call<List<Crypto>> getCryptoData();
}
