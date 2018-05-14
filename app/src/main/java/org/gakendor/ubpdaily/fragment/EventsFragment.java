package org.gakendor.ubpdaily.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.gakendor.ubpdaily.R;
import org.gakendor.ubpdaily.activity.DetailEvent;
import org.gakendor.ubpdaily.adapter.EventsAdapter;
import org.gakendor.ubpdaily.clients.ApiUtils;
import org.gakendor.ubpdaily.clients.api.MobileService;
import org.gakendor.ubpdaily.clients.model.Response;
import org.gakendor.ubpdaily.model.Event;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventsFragment extends Fragment {


    @BindView(R.id.recycleView)
    RecyclerView recycleView;
    Unbinder unbinder;
    MobileService mobileService;
    @BindView(R.id.swipe_container)
    SwipeRefreshLayout swipeContainer;

    public EventsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_blank, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mobileService = ApiUtils.MobileService(getContext());
        loadData();

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        });

    }

    private void loadData() {
        swipeContainer.setRefreshing(true);
        mobileService.events().enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                swipeContainer.setRefreshing(false);
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        String json = new Gson().toJson(response.body().getData());
                        List<Event> list = new Gson().fromJson(json, new TypeToken<List<Event>>() {
                        }.getType());
                        setToAdapter(list);
                    }
                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                swipeContainer.setRefreshing(false);
            }
        });
    }

    private void setToAdapter(List<Event> list) {
        EventsAdapter eventsAdapter = new EventsAdapter(list, new EventsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Event model) {
                Intent intent = new Intent(getContext(), DetailEvent.class);
                intent.putExtra("event_model", model);
                startActivity(intent);
            }
        });
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recycleView.setLayoutManager(layoutManager);
        recycleView.setAdapter(eventsAdapter);
    }

}

