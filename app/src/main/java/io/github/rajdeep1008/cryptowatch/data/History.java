package io.github.rajdeep1008.cryptowatch.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by rajdeep1008 on 07/02/18.
 */

public class History {
    @SerializedName("price")
    List<List<Float>> priceList;

    public List<List<Float>> getPriceList() {
        return priceList;
    }

    public void setPriceList(List<List<Float>> priceList) {
        this.priceList = priceList;
    }
}
