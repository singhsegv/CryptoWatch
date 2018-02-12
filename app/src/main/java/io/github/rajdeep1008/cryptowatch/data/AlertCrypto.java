package io.github.rajdeep1008.cryptowatch.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * Created by rajdeep1008 on 05/02/18.
 */

@Entity(tableName = "alert_crypto")
public class AlertCrypto {

    @PrimaryKey()
    @NonNull
    private String id;

    @ColumnInfo(name = "symbol")
    private String symbol;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "price")
    private String price;

    @ColumnInfo(name = "upper_price")
    private String upperPrice = "";

    @ColumnInfo(name = "lower_price")
    private String lowerPrice = "";

    @ColumnInfo(name = "currency_symbol")
    private String currencySymbol;

    public AlertCrypto(@NonNull String id, String symbol, String name, String price, String upperPrice, String lowerPrice, String currencySymbol) {
        this.id = id;
        this.symbol = symbol;
        this.name = name;
        this.price = price;
        this.upperPrice = upperPrice;
        this.lowerPrice = lowerPrice;
        this.currencySymbol = currencySymbol;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getUpperPrice() {
        return upperPrice;
    }

    public void setUpperPrice(String upperPrice) {
        this.upperPrice = upperPrice;
    }

    public String getLowerPrice() {
        return lowerPrice;
    }

    public void setLowerPrice(String lowerPrice) {
        this.lowerPrice = lowerPrice;
    }

    public String getCurrencySymbol() {
        return currencySymbol;
    }

    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }
}
