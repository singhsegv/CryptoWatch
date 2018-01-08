package io.github.rajdeep1008.cryptofolio.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.rajdeep1008.cryptofolio.R;
import io.github.rajdeep1008.cryptofolio.model.Crypto;
import io.github.rajdeep1008.cryptofolio.rest.ServiceGenerator;

/**
 * Created by rajdeep1008 on 09/01/18.
 */

public class CryptoAdapter extends RecyclerView.Adapter<CryptoAdapter.ViewHolder> {

    List<Crypto> itemList;
    Context mContext;

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

        Context mContext;

        public ViewHolder(View itemView, Context context) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mContext = context;
        }

        public void init(Crypto item) {
            symbolTv.setText(item.getSymbol() + " | ");
            nameTv.setText(item.getName());
            priceTv.setText(item.getPriceUsd());
            dayChangeTv.setText("1h: " + item.getPercentChange24h());
            hourChangeTv.setText("24h: " + item.getPercentChange24h());

            Glide.with(mContext).load(String.format(ServiceGenerator.IMAGE_URL, item.getId())).into(symbolIv);
        }
    }
}
