package io.github.rajdeep1008.cryptofolio.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.format.DateUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.text.DecimalFormat;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnLongClick;
import io.github.rajdeep1008.cryptofolio.R;
import io.github.rajdeep1008.cryptofolio.activity.MainActivity;
import io.github.rajdeep1008.cryptofolio.data.Crypto;
import io.github.rajdeep1008.cryptofolio.extras.Utilities;
import io.github.rajdeep1008.cryptofolio.rest.ServiceGenerator;

/**
 * Created by rajdeep1008 on 09/01/18.
 */

public class CryptoAdapter extends RecyclerView.Adapter<CryptoAdapter.ViewHolder> {

    private List<Crypto> itemList;
    private Context mContext;
    public static String currency;

    public CryptoAdapter(List<Crypto> list, Context context) {
        itemList = list;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.feed_item_layout, parent, false);

        return new ViewHolder(view, mContext);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Crypto cryptoItem = itemList.get(position);
        holder.init(cryptoItem);
    }

    public void addAll(List<Crypto> list, String curr) {
        itemList = list;
        currency = curr;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.symbol_iv)
        ImageView symbolIv;

        @BindView(R.id.symbol_tv)
        TextView symbolTv;

        @BindView(R.id.name_tv)
        TextView nameTv;

        @BindView(R.id.price_tv)
        TextView priceTv;

        @BindView(R.id.day_change_tv)
        TextView dayChangeTv;

        @BindView(R.id.hour_change_tv)
        TextView hourChangeTv;

        @BindView(R.id.week_change_tv)
        TextView weekChangeTv;

        @BindView(R.id.last_updated_tv)
        TextView lastUpdatedTv;

        private Context mContext;
        private Crypto currentItem;
        private static final int TYPE_HOUR = 0;
        private static final int TYPE_DAY = 1;
        private static final int TYPE_WEEK = 2;
        DecimalFormat df = new DecimalFormat(".####");

        public ViewHolder(View itemView, Context context) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mContext = context;
        }

        private void init(Crypto item) {
            currentItem = item;
            symbolTv.setText(item.getSymbol() + " | ");
            nameTv.setText(item.getName());
            priceTv.setText(getPrice(item, currency));
            hourChangeTv.setText(getColoredChanges(item, "1h: " + item.getPercentChange1h() + "%", TYPE_HOUR));
            dayChangeTv.setText(getColoredChanges(item, "24h: " + item.getPercentChange24h() + "%", TYPE_DAY));
            weekChangeTv.setText(getColoredChanges(item, "7d: " + item.getPercentChange7d() + "%", TYPE_WEEK));

            lastUpdatedTv.setText("(Updated: " + DateUtils.getRelativeTimeSpanString(Long.parseLong(item.getLastUpdated()) * 1000, System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS) + ")");

            Glide.with(mContext).load(String.format(ServiceGenerator.IMAGE_URL, item.getId())).into(symbolIv);
        }

        private Spannable getColoredChanges(Crypto item, String value, int type) {
            Spannable temp = new SpannableString(value);

            switch (type) {
                case TYPE_HOUR:
                    if (item.getPercentChange1h() != null) {
                        if (Double.parseDouble(item.getPercentChange1h()) < 0) {
                            temp.setSpan(new ForegroundColorSpan(Color.RED), 4, value.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        } else {
                            String tempString = value.substring(0, 4) + "+" + value.substring(4);
                            temp = new SpannableString(tempString);
                            temp.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.positiveGreen)), 4, tempString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        }
                    } else {
                        hourChangeTv.setVisibility(View.GONE);
                    }
                    break;
                case TYPE_DAY:
                    if (item.getPercentChange24h() != null) {
                        if (Double.parseDouble(item.getPercentChange24h()) < 0) {
                            temp.setSpan(new ForegroundColorSpan(Color.RED), 4, value.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        } else {
                            String tempString = value.substring(0, 5) + "+" + value.substring(5);
                            temp = new SpannableString(tempString);
                            temp.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.positiveGreen)), 4, tempString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        }
                    } else {
                        dayChangeTv.setVisibility(View.GONE);
                    }
                    break;
                case TYPE_WEEK:
                    if (item.getPercentChange7d() != null) {
                        if (Double.parseDouble(item.getPercentChange7d()) < 0) {
                            temp.setSpan(new ForegroundColorSpan(Color.RED), 4, value.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        } else {
                            String tempString = value.substring(0, 4) + "+" + value.substring(4);
                            temp = new SpannableString(tempString);
                            temp.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.positiveGreen)), 4, tempString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        }
                    } else {
                        weekChangeTv.setVisibility(View.GONE);
                    }
                    break;
            }
            return temp;
        }

        @OnLongClick(R.id.item_holder)
        public boolean itemLongClicked() {
            String[] menuItems;
            final boolean favorite = Utilities.checkFavorite(mContext.getApplicationContext(), currentItem);

            if (favorite) {
                menuItems = new String[]{"Remove from favorites"};
            } else {
                menuItems = new String[]{"Add to favorites"};
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setItems(menuItems, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (i == 0) {
                        if (favorite) {
                            Utilities.removeFavorites(mContext.getApplicationContext(), currentItem.getId());
                            Toast.makeText(mContext, "Removed", Toast.LENGTH_SHORT).show();
                            ((MainActivity) mContext).updateFavorites();
                        } else {
                            Utilities.addFavorites(mContext.getApplicationContext(), currentItem.getId());
                            Toast.makeText(mContext, "Added", Toast.LENGTH_SHORT).show();
                            ((MainActivity) mContext).updateFavorites();
                        }
                    }
                }
            });

            builder.create().show();
            return true;
        }

        private String getPrice(Crypto item, String currency) {
            String price = "";
            switch (currency) {
                case "USD":
                    price += getCurrencyCode("en", "US") + df.format(Double.valueOf(item.getPriceUsd()));
                    break;
                case "AUD":
                    price += getCurrencyCode("en", "AU") + df.format(Double.valueOf(item.getPriceAud()));
                    break;
                case "CAD":
                    price += getCurrencyCode("en", "CA") + df.format(Double.valueOf(item.getPriceCad()));
                    break;
                case "EUR":
                    price += getCurrencyCode("en", "EU") + df.format(Double.valueOf(item.getPriceEur()));
                    break;
                case "HKD":
                    price += getCurrencyCode("en", "HK") + df.format(Double.valueOf(item.getPriceHkd()));
                    break;
                case "GBP":
                    price += getCurrencyCode("en", "GB") + df.format(Double.valueOf(item.getPriceGbp()));
                    break;
                case "JPY":
                    price += getCurrencyCode("en", "JP") + df.format(Double.valueOf(item.getPriceJpy()));
                    break;
                case "INR":
                    price += getCurrencyCode("en", "IN") + df.format(Double.valueOf(item.getPriceInr()));
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
