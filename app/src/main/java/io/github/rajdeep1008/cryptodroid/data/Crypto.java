package io.github.rajdeep1008.cryptodroid.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by rajdeep1008 on 17/12/17.
 */

@Entity(tableName = "crypto")
public class Crypto implements Parcelable {

    @PrimaryKey()
    @SerializedName("id")
    @Expose
    @NonNull
    private String id;

    @ColumnInfo(name = "name")
    @SerializedName("name")
    @Expose
    private String name;

    @ColumnInfo(name = "symbol")
    @SerializedName("symbol")
    @Expose
    private String symbol;

    @ColumnInfo(name = "rank")
    @SerializedName("rank")
    @Expose
    private String rank;

    @ColumnInfo(name = "price_usd")
    @SerializedName("price_usd")
    @Expose
    private String priceUsd;

    @ColumnInfo(name = "price_aud")
    @SerializedName("price_aud")
    @Expose
    private String priceAud;

    @ColumnInfo(name = "price_cad")
    @SerializedName("price_cad")
    @Expose
    private String priceCad;

    @ColumnInfo(name = "price_eur")
    @SerializedName("price_eur")
    @Expose
    private String priceEur;

    @ColumnInfo(name = "price_hkd")
    @SerializedName("price_hkd")
    @Expose
    private String priceHkd;

    @ColumnInfo(name = "price_gbp")
    @SerializedName("price_gbp")
    @Expose
    private String priceGbp;

    @ColumnInfo(name = "price_jpy")
    @SerializedName("price_jpy")
    @Expose
    private String priceJpy;

    @ColumnInfo(name = "price_inr")
    @SerializedName("price_inr")
    @Expose
    private String priceInr;

    @ColumnInfo(name = "price_btc")
    @SerializedName("price_btc")
    @Expose
    private String priceBtc;

    @ColumnInfo(name = "24h_volume_usd")
    @SerializedName("24h_volume_usd")
    @Expose
    private String _24hVolumeUsd;

    @ColumnInfo(name = "market_cap_usd")
    @SerializedName("market_cap_usd")
    @Expose
    private String marketCapUsd;

    @ColumnInfo(name = "available_supply")
    @SerializedName("available_supply")
    @Expose
    private String availableSupply;

    @ColumnInfo(name = "total_supply")
    @SerializedName("total_supply")
    @Expose
    private String totalSupply;

    @ColumnInfo(name = "max_supply")
    @SerializedName("max_supply")
    @Expose
    private String maxSupply;

    @ColumnInfo(name = "percent_change_1h")
    @SerializedName("percent_change_1h")
    @Expose
    private String percentChange1h;

    @ColumnInfo(name = "percent_change_24h")
    @SerializedName("percent_change_24h")
    @Expose
    private String percentChange24h;

    @ColumnInfo(name = "percent_change_7d")
    @SerializedName("percent_change_7d")
    @Expose
    private String percentChange7d;

    @ColumnInfo(name = "last_updated")
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

    public Crypto() {
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

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public void setPriceUsd(String priceUsd) {
        this.priceUsd = priceUsd;
    }

    public void setPriceBtc(String priceBtc) {
        this.priceBtc = priceBtc;
    }

    public void set_24hVolumeUsd(String _24hVolumeUsd) {
        this._24hVolumeUsd = _24hVolumeUsd;
    }

    public void setMarketCapUsd(String marketCapUsd) {
        this.marketCapUsd = marketCapUsd;
    }

    public void setAvailableSupply(String availableSupply) {
        this.availableSupply = availableSupply;
    }

    public void setTotalSupply(String totalSupply) {
        this.totalSupply = totalSupply;
    }

    public void setMaxSupply(String maxSupply) {
        this.maxSupply = maxSupply;
    }

    public void setPercentChange1h(String percentChange1h) {
        this.percentChange1h = percentChange1h;
    }

    public void setPercentChange24h(String percentChange24h) {
        this.percentChange24h = percentChange24h;
    }

    public void setPercentChange7d(String percentChange7d) {
        this.percentChange7d = percentChange7d;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getPriceAud() {
        return priceAud;
    }

    public void setPriceAud(String priceAud) {
        this.priceAud = priceAud;
    }

    public String getPriceCad() {
        return priceCad;
    }

    public void setPriceCad(String priceCad) {
        this.priceCad = priceCad;
    }

    public String getPriceEur() {
        return priceEur;
    }

    public void setPriceEur(String priceEur) {
        this.priceEur = priceEur;
    }

    public String getPriceHkd() {
        return priceHkd;
    }

    public void setPriceHkd(String priceHkd) {
        this.priceHkd = priceHkd;
    }

    public String getPriceGbp() {
        return priceGbp;
    }

    public void setPriceGbp(String priceGbp) {
        this.priceGbp = priceGbp;
    }

    public String getPriceJpy() {
        return priceJpy;
    }

    public void setPriceJpy(String priceJpy) {
        this.priceJpy = priceJpy;
    }

    public String getPriceInr() {
        return priceInr;
    }

    public void setPriceInr(String priceInr) {
        this.priceInr = priceInr;
    }
}
