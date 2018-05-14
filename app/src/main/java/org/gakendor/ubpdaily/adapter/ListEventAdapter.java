package org.gakendor.ubpdaily.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.gakendor.ubpdaily.R;
import org.gakendor.ubpdaily.model.EventCount;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by yaziedda on 3/27/18.
 */

public class ListEventAdapter extends RecyclerView.Adapter<ListEventAdapter.ViewHolder> {

    List<EventCount> list = new ArrayList<>();


    public interface OnItemClickListener {
        void onItemClick(EventCount model);
    }

    private final OnItemClickListener listener;

    public ListEventAdapter(List<EventCount> list, OnItemClickListener listener) {
        this.list = list;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_scan, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final EventCount model = list.get(position);

        holder.tvTitle.setText(model.getJudul());
        holder.tvSubtitle.setVisibility(View.GONE);

        holder.btCount.setText(model.getAbsen()+" / "+model.getTotal());
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

        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_subtitle)
        TextView tvSubtitle;
        @BindView(R.id.textView3)
        TextView textView3;
        @BindView(R.id.bt_count)
        Button btCount;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
