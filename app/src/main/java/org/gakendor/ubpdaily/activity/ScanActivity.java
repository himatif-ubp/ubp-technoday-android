package org.gakendor.ubpdaily.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import org.gakendor.ubpdaily.R;
import org.gakendor.ubpdaily.adapter.ListEventAdapter;
import org.gakendor.ubpdaily.clients.ApiUtils;
import org.gakendor.ubpdaily.clients.api.MobileService;
import org.gakendor.ubpdaily.clients.model.Response;
import org.gakendor.ubpdaily.model.EventCount;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;

public class ScanActivity extends AppCompatActivity {

    @BindView(R.id.recycleView)
    RecyclerView recycleView;
    @BindView(R.id.swipe_container)
    SwipeRefreshLayout swipeContainer;
    MobileService mobileService;
    @BindView(R.id.bt_scan)
    Button btScan;
    MaterialDialog materialDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        ButterKnife.bind(this);

        materialDialog = new MaterialDialog.Builder(this)
                .content("Loading...")
                .cancelable(false)
                .progress(true, 0)
                .build();

        mobileService = ApiUtils.MobileService(getApplicationContext());

        loadData();

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        });

        btScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TedPermission.with(ScanActivity.this)
                        .setPermissionListener(permissionlistener)
                        .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                        .setPermissions(Manifest.permission.CAMERA)
                        .check();
            }
        });
    }

    PermissionListener permissionlistener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            Intent i = new Intent(ScanActivity.this, ScannerActivity.class);
            startActivityForResult(i, 1);
        }

        @Override
        public void onPermissionDenied(ArrayList<String> deniedPermissions) {
            Toast.makeText(ScanActivity.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
        }


    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                String result=data.getStringExtra("result");
                Intent intent = new Intent(getApplicationContext(), MyBarcodeActivity.class);
                intent.putExtra("tr_id", result);
                startActivity(intent);
//                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }//onActivityResult

    private void loadData() {
        swipeContainer.setRefreshing(true);
        mobileService.eventsCount().enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                swipeContainer.setRefreshing(false);
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        String json = new Gson().toJson(response.body().getData());
                        System.out.println(json);
                        List<EventCount> list = new Gson().fromJson(json, new TypeToken<List<EventCount>>() {
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

    private void setToAdapter(List<EventCount> list) {
        ListEventAdapter eventsAdapter = new ListEventAdapter(list, new ListEventAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(EventCount model) {
//                Intent intent = new Intent(getApplicationContext(), DetailEvent.class);
//                intent.putExtra("event_model", model);
//                startActivity(intent);
            }
        });
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recycleView.setLayoutManager(layoutManager);
        recycleView.setAdapter(eventsAdapter);
    }
}
