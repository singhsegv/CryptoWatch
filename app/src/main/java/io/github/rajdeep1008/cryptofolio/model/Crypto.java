package io.github.rajdeep1008.cryptofolio.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by rajdeep1008 on 17/12/17.
 */

public class Crypto implements Parcelable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("symbol")
    @Expose
    private String symbol;
    @SerializedName("rank")
    @Expose
    private String rank;
    @SerializedName("price_usd")
    @Expose
    private String priceUsd;
    @SerializedName("price_btc")
    @Expose
    private String priceBtc;
    @SerializedName("24h_volume_usd")
    @Expose
    private String _24hVolumeUsd;
    @SerializedName("market_cap_usd")
    @Expose
    private String marketCapUsd;
    @SerializedName("available_supply")
    @Expose
    private String availableSupply;
    @SerializedName("total_supply")
    @Expose
    private String totalSupply;
    @SerializedName("max_supply")
    @Expose
    private String maxSupply;
    @SerializedName("percent_change_1h")
    @Expose
    private String percentChange1h;
    @SerializedName("percent_change_24h")
    @Expose
    private String percentChange24h;
    @SerializedName("percent_change_7d")
    @Expose
    private String percentChange7d;
    @SerializedName("last_updated")
    @Expose
    private String lastUpdated;

    protected Crypto(Parcel in) {
        id = in.readString();
        name = in.readString();
        symbol = in.readString();
        rank = in.readString();
        priceUsd = in.readString();
        priceBtc = in.readString();
        _24hVolumeUsd = in.readString();
        marketCapUsd = in.readString();
        availableSupply = in.readString();
        totalSupply = in.readString();
        maxSupply = in.readString();
        percentChange1h = in.readString();
        percentChange24h = in.readString();
        percentChange7d = in.readString();
        lastUpdated = in.readString();
    }

    public static final Creator<Crypto> CREATOR = new Creator<Crypto>() {
        @Override
        public Crypto createFromParcel(Parcel in) {
            return new Crypto(in);
        }

        @Override
        public Crypto[] newArray(int size) {
            return new Crypto[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(symbol);
        dest.writeString(rank);
        dest.writeString(priceUsd);
        dest.writeString(priceBtc);
        dest.writeString(_24hVolumeUsd);
        dest.writeString(marketCapUsd);
        dest.writeString(availableSupply);
        dest.writeString(totalSupply);
        dest.writeString(maxSupply);
        dest.writeString(percentChange1h);
        dest.writeString(percentChange24h);
        dest.writeString(percentChange7d);
        dest.writeString(lastUpdated);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getRank() {
        return rank;
    }

    public String getPriceUsd() {
        return priceUsd;
    }

    public String getPriceBtc() {
        return priceBtc;
    }

    public String get_24hVolumeUsd() {
        return _24hVolumeUsd;
    }

    public String getMarketCapUsd() {
        return marketCapUsd;
    }

    public String getAvailableSupply() {
        return availableSupply;
    }

    public String getTotalSupply() {
        return totalSupply;
    }

    public String getMaxSupply() {
        return maxSupply;
    }

    public String getPercentChange1h() {
        return percentChange1h;
    }

    public String getPercentChange24h() {
        return percentChange24h;
    }

    public String getPercentChange7d() {
        return percentChange7d;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

}
