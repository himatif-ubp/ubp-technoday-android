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
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.gakendor.ubpdaily.R;
import org.gakendor.ubpdaily.activity.DetailMyEvent;
import org.gakendor.ubpdaily.adapter.MyEventsAdapter;
import org.gakendor.ubpdaily.clients.ApiUtils;
import org.gakendor.ubpdaily.clients.api.MobileService;
import org.gakendor.ubpdaily.clients.model.Response;
import org.gakendor.ubpdaily.model.MyEvent;
import org.gakendor.ubpdaily.util.MyPref;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyEventsFragment extends Fragment {


    @BindView(R.id.recycleView)
    RecyclerView recycleView;
    Unbinder unbinder;
    MobileService mobileService;
    @BindView(R.id.swipe_container)
    SwipeRefreshLayout swipeContainer;
    @BindView(R.id.ll_error)
    LinearLayout llError;

    public MyEventsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_events, container, false);
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
        recycleView.setVisibility(View.VISIBLE);
        llError.setVisibility(View.GONE);
        Map<String, String> map = new HashMap<>();
        map.put("id", String.valueOf(MyPref.getLoginData(getContext()).getId()));

        mobileService.myEvents(map).enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                swipeContainer.setRefreshing(false);
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        String json = new Gson().toJson(response.body().getData());
                        List<MyEvent> list = new Gson().fromJson(json, new TypeToken<List<MyEvent>>() {
                        }.getType());
                        setToAdapter(list);
                    }else{
                        recycleView.setVisibility(View.GONE);
                        llError.setVisibility(View.VISIBLE);
                    }
                }else{
                    recycleView.setVisibility(View.GONE);
                    llError.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                swipeContainer.setRefreshing(false);
                recycleView.setVisibility(View.GONE);
                llError.setVisibility(View.VISIBLE);
            }
        });
    }

    private void setToAdapter(List<MyEvent> list) {
        if(list.size() > 0) {
            MyEventsAdapter eventsAdapter = new MyEventsAdapter(getContext(), list, new MyEventsAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(MyEvent model) {
                    Intent intent = new Intent(getContext(), DetailMyEvent.class);
                    intent.putExtra("event_model", model);
                    startActivity(intent);
                }
            });
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
            recycleView.setLayoutManager(layoutManager);
            recycleView.setAdapter(eventsAdapter);
            recycleView.setAdapter(eventsAdapter);
        }else{
            recycleView.setVisibility(View.GONE);
            llError.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }
}
