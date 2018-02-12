package io.github.rajdeep1008.cryptowatch.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.DecimalFormat;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnLongClick;
import io.github.rajdeep1008.cryptowatch.R;
import io.github.rajdeep1008.cryptowatch.activity.MainActivity;
import io.github.rajdeep1008.cryptowatch.data.AlertCrypto;
import io.github.rajdeep1008.cryptowatch.data.AlertDao;
import io.github.rajdeep1008.cryptowatch.data.AppDatabase;
import io.github.rajdeep1008.cryptowatch.extras.Utilities;
import io.github.rajdeep1008.cryptowatch.rest.ServiceGenerator;

/**
 * Created by rajdeep1008 on 06/02/18.
 */

public class AlertAdapter extends RecyclerView.Adapter<AlertAdapter.ViewHolder> {

    private List<AlertCrypto> itemList;
    private Context mContext;

    public AlertAdapter(List<AlertCrypto> list, Context context) {
        this.itemList = list;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.alert_item_layout, parent, false);

        return new ViewHolder(view, mContext);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        AlertCrypto item = itemList.get(position);
        holder.init(item);
    }

    public void addAll(List<AlertCrypto> list) {
        itemList = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (itemList != null)
            return itemList.size();

        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.price_tv)
        TextView priceTv;

        @BindView(R.id.upper_price_tv)
        TextView upperPriceTv;

        @BindView(R.id.below_price_tv)
        TextView belowPriceTv;

        @BindView(R.id.symbol_tv)
        TextView symbolTv;

        @BindView(R.id.name_tv)
        TextView nameTv;

        @BindView(R.id.symbol_iv)
        ImageView symbolIv;

        @BindView(R.id.upper_price_rl)
        RelativeLayout upperPriceView;

        @BindView(R.id.below_price_rl)
        RelativeLayout belowPriceView;

        Context mContext;
        AlertCrypto currentItem;
        DecimalFormat df = new DecimalFormat(".####");


        public ViewHolder(View itemView, Context context) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mContext = context;
        }

        public void init(AlertCrypto item) {
            currentItem = item;
            symbolTv.setText(item.getSymbol() + " | ");
            nameTv.setText(item.getName());
            priceTv.setText(getSymbol(item, item.getCurrencySymbol()) + item.getPrice());
            if (item.getUpperPrice() != null) {
                upperPriceView.setVisibility(View.VISIBLE);
                upperPriceTv.setText(getSymbol(item, item.getCurrencySymbol()) + item.getUpperPrice());
            } else {
                upperPriceView.setVisibility(View.GONE);
            }
            if (item.getLowerPrice() != null) {
                belowPriceView.setVisibility(View.VISIBLE);
                belowPriceTv.setText(getSymbol(item, item.getCurrencySymbol()) + item.getLowerPrice());
            } else {
                belowPriceView.setVisibility(View.GONE);
            }
            Glide.with(mContext).load(String.format(ServiceGenerator.IMAGE_URL, item.getId())).into(symbolIv);
        }

        @OnLongClick(R.id.item_holder)
        public boolean itemLongClicked() {
            String[] menuItems = null;
            final boolean exist = Utilities.checkWatchlist(mContext.getApplicationContext(), currentItem);

            if (exist) {
                menuItems = new String[]{"Remove from watchlist"};

                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setItems(menuItems, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == 0) {
                            Utilities.removefromWatchlist(mContext, currentItem.getId());
                            AppDatabase appDatabase;
                            AlertDao alertDao;
                            appDatabase = AppDatabase.getInstance(mContext);
                            alertDao = appDatabase.alertDao();
                            alertDao.removeFromList(currentItem.getId());

                            ((MainActivity) mContext).updateWatchlist();
                        }
                    }
                });

                builder.create().show();
            }
            return true;
        }

        private String getSymbol(AlertCrypto item, String currency) {
            String price = "";
            switch (currency) {
                case "USD":
                    price += getCurrencyCode("en", "US");
                    break;
                case "AUD":
                    price += getCurrencyCode("en", "AU");
                    break;
                case "CAD":
                    price += getCurrencyCode("en", "CA");
                    break;
                case "EUR":
                    price += getCurrencyCode("en", "EU");
                    break;
                case "HKD":
                    price += getCurrencyCode("en", "HK");
                    break;
                case "GBP":
                    price += getCurrencyCode("en", "GB");
                    break;
                case "JPY":
                    price += getCurrencyCode("en", "JP");
                    break;
                case "INR":
                    price += getCurrencyCode("en", "IN");
                    break;
            }

            return price;
        }

        private String getCurrencyCode(String languageCode, String countryCode) {
            Locale locale = new Locale(languageCode, countryCode);
            Currency currency = Currency.getInstance(locale);
            String symbol = currency.getSymbol();

            return symbol;
        }
    }
}
