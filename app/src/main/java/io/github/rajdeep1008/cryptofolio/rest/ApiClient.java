package io.github.rajdeep1008.cryptofolio.rest;

import java.util.List;

import io.github.rajdeep1008.cryptofolio.data.Crypto;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by rajdeep1008 on 17/12/17.
 */

public interface ApiClient {

    @GET("ticker")
    Call<List<Crypto>> getCryptoData(@Query("limit") int limit, @Query("convert") String currency);

}
