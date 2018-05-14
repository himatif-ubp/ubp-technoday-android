package org.gakendor.ubpdaily.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.gakendor.ubpdaily.R;
import org.gakendor.ubpdaily.model.Event;
import org.gakendor.ubpdaily.model.MyEvent;
import org.gakendor.ubpdaily.util.DateUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by yaziedda on 3/27/18.
 */

public class MyEventsAdapter extends RecyclerView.Adapter<MyEventsAdapter.ViewHolder> {

    List<MyEvent> list = new ArrayList<>();
    public interface OnItemClickListener {
        void onItemClick(MyEvent model);
    }

    private final OnItemClickListener listener;
    Context context;

    public MyEventsAdapter(Context context, List<MyEvent> list, OnItemClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_events, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final MyEvent model = list.get(position);

        holder.tvTitle.setText(model.getJudul());
        holder.tvSubtitle.setText(model.getSubJudul());
        Glide.with(holder.itemView).load("http://ubptechnoday3.com/storage/"+model.getGambarBesar()).into(holder.ivThumb);

        switch (model.getStatus()){
            case -1:
                holder.tvStatus.setText("DITOLAK");
                holder.tvStatus.setBackgroundColor(ContextCompat.getColor(context, R.color.red));
                break;
            case 0:
                holder.tvStatus.setText("VERIFIKASI");
                holder.tvStatus.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));
                break;
            case 1:
                holder.tvStatus.setText("LIHAT TIKET");
                holder.tvStatus.setBackgroundColor(ContextCompat.getColor(context, R.color.green));
                break;
            case 2:
                holder.tvStatus.setText("BAYAR");
                holder.tvStatus.setBackgroundColor(ContextCompat.getColor(context, R.color.red));;
                break;
            default:
                break;
        }

        holder.tvDate.setText(DateUtils.getDateOnly(model.getTanggal()));
        holder.tvMonth.setText(DateUtils.getMonth(model.getTanggal()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(model);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_thumb)
        ImageView ivThumb;
        @BindView(R.id.tv_month)
        TextView tvMonth;
        @BindView(R.id.tv_date)
        TextView tvDate;
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_subtitle)
        TextView tvSubtitle;
        @BindView(R.id.tv_status)
        TextView tvStatus;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
