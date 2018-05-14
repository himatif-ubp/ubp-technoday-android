package org.gakendor.ubpdaily.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import org.gakendor.ubpdaily.R;
import org.gakendor.ubpdaily.activity.DetailEvent;
import org.gakendor.ubpdaily.activity.LoginActivity;
import org.gakendor.ubpdaily.activity.PaymentActivity;
import org.gakendor.ubpdaily.activity.UpdatePasswordActivity;
import org.gakendor.ubpdaily.activity.UpdateProfileActivity;
import org.gakendor.ubpdaily.adapter.EventsAdapter;
import org.gakendor.ubpdaily.model.Event;
import org.gakendor.ubpdaily.util.MyPref;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingFragment extends Fragment {


    Unbinder unbinder;
    @BindView(R.id.tv_profile)
    TextView tvProfile;
    @BindView(R.id.tv_pwd)
    TextView tvPwd;

    public SettingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @OnClick({R.id.tv_profile, R.id.tv_pwd, R.id.tv_logout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_profile:
                startActivity(new Intent(getContext(), UpdateProfileActivity.class));
                break;
            case R.id.tv_pwd:
                startActivity(new Intent(getContext(), UpdatePasswordActivity.class));
                break;
            case R.id.tv_logout:
                new MaterialDialog.Builder(getActivity())
                        .title("Pemberitahuan")
                        .content("Apakah anda yakin akan logout?")
                        .positiveText("Ya")
                        .negativeText("Tidak")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                MyPref.logout(getContext());
                                startActivity(new Intent(getContext(), LoginActivity.class));
                                getActivity().finish();
                            }
                        })
                        .show();

                break;
        }
    }
}
