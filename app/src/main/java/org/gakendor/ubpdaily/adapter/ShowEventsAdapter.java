package org.gakendor.ubpdaily.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.gakendor.ubpdaily.R;
import org.gakendor.ubpdaily.model.Event;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by yaziedda on 3/27/18.
 */

public class ShowEventsAdapter extends RecyclerView.Adapter<ShowEventsAdapter.ViewHolder> {

    List<Event> list;

    public ShowEventsAdapter(List<Event> list) {
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Event model = list.get(position);

        if(model.getId() == 1){
            holder.tvTitle.setTextSize(16);
            holder.tvSubtitle.setTextSize(12);
        }

        holder.tvTitle.setText(model.getJudul());
        holder.tvSubtitle.setText(model.getSubJudul());
        Glide.with(holder.itemView).load("http://ubptechnoday3.com/storage/"+model.getGambarBesar()).into(holder.ivThumb);
        holder.llDate.setVisibility(View.GONE);
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
        @BindView(R.id.ll_date)
        LinearLayout llDate;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
