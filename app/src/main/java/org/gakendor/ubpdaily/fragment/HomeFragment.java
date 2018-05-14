package org.gakendor.ubpdaily.fragment;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.gakendor.ubpdaily.R;
import org.gakendor.ubpdaily.adapter.ShowEventsAdapter;
import org.gakendor.ubpdaily.clients.ApiUtils;
import org.gakendor.ubpdaily.clients.api.MobileService;
import org.gakendor.ubpdaily.clients.api.model.User;
import org.gakendor.ubpdaily.clients.model.Response;
import org.gakendor.ubpdaily.model.Event;
import org.gakendor.ubpdaily.model.Iklan;
import org.gakendor.ubpdaily.util.MyPref;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {


    Unbinder unbinder;
    @BindView(R.id.slider)
    SliderLayout slider;
    @BindView(R.id.recycleView)
    RecyclerView recycleView;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_instansi)
    TextView tvInstansi;
    @BindView(R.id.tvMore)
    TextView tvMore;

    MobileService mobileService;
    @BindView(R.id.pb_loading)
    ProgressBar pbLoading;
    @BindView(R.id.tv_link)
    TextView tvLink;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        mobileService = ApiUtils.MobileService(getActivity());

        final HashMap<String, String> url_maps = new HashMap<>();
//        url_maps.put("Pengumuman Hari ini", "https://marketplace.canva.com/MABy2PdFF7w/1/0/thumbnail_large/canva-student-council-event-poster-MABy2PdFF7w.jpg");
//        url_maps.put("Jangan lewatkan !", "https://i.pinimg.com/736x/5d/32/0d/5d320daf07e10f03cb94c3bf2f274f13--flyer-design-event-poster-design.jpg");
//        url_maps.put("Ingat !!!", "https://i.pinimg.com/originals/d6/1c/1d/d61c1d8bb37f7225c5fa56c28ba99276.jpg");

        mobileService.iklan().enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                if (response.isSuccessful()) {
                    String json = new Gson().toJson(response.body().getData());
                    List<Iklan> list = new Gson().fromJson(json, new TypeToken<List<Iklan>>() {
                    }.getType());
                    if (list != null && list.size() > 0) {
                        for (Iklan iklan : list) {
                            url_maps.put(iklan.getJudul(), "http://ubptechnoday3.com/storage/iklan/" + iklan.getGambar());
                        }
                    }
                    loadSlider(url_maps);
                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {

            }
        });


        User user = MyPref.getLoginData(getActivity());

        tvName.setText(user.getNamaLengkap());
        tvInstansi.setText(user.getInstansi());

        mobileService.eventsAll().enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                pbLoading.setVisibility(View.GONE);
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
                pbLoading.setVisibility(View.GONE);
            }
        });

        tvLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://www.ubptechnoday3.com";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

    }

    private void setToAdapter(List<Event> list) {
        ShowEventsAdapter eventsAdapter = new ShowEventsAdapter(list);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recycleView.setLayoutManager(layoutManager);
        recycleView.setAdapter(eventsAdapter);
    }

    private void loadSlider(HashMap<String, String> url_maps) {
        for (String name : url_maps.keySet()) {
            TextSliderView textSliderView = new TextSliderView(getContext());

            textSliderView
                    .description(name)
                    .image(url_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.CenterCrop);

            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra", name);

            slider.addSlider(textSliderView);
        }
        slider.setPresetTransformer(SliderLayout.Transformer.Default);
        slider.setPresetIndicator(SliderLayout.PresetIndicators.Right_Top);
        slider.setCustomAnimation(new DescriptionAnimation());
        slider.setDuration(4000);
        slider.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
