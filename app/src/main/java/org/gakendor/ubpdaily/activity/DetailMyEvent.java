package org.gakendor.ubpdaily.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.gakendor.ubpdaily.R;
import org.gakendor.ubpdaily.model.MyEvent;
import org.gakendor.ubpdaily.util.DateUtils;
import org.gakendor.ubpdaily.util.IDRUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DetailMyEvent extends AppCompatActivity implements OnMapReadyCallback {

    GoogleMap mMap;
    @BindView(R.id.bt_register)
    TextView btRegister;
    MyEvent event;
    @BindView(R.id.iv_thumb)
    ImageView ivThumb;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_kuota)
    TextView tvKuota;
    @BindView(R.id.tv_harga)
    TextView tvHarga;
    @BindView(R.id.tv_date)
    TextView tvDate;
    @BindView(R.id.tv_alamat)
    TextView tvAlamat;
    @BindView(R.id.tv_nav)
    TextView tvNav;
    @BindView(R.id.tv_deskripsi)
    TextView tvDeskripsi;
    @BindView(R.id.iv_finish)
    ImageView ivFinish;
    @BindView(R.id.iv_image_show)
    ImageView ivImageShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_my_event);
        ButterKnife.bind(this);

        event = (MyEvent) getIntent().getSerializableExtra("event_model");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        tvTitle.setText(event.getJudul() + "\n" + event.getSubJudul());
        tvKuota.setText(event.getStok() + " seat");
        tvHarga.setText(IDRUtils.toRupiah(event.getHarga()));
        tvDate.setText(DateUtils.getDateUI(event.getTanggal()));
        tvDeskripsi.setText(Html.fromHtml(event.getDeskripsi()));

        tvAlamat.setText(event.getAlamat());

        Glide.with(getApplicationContext()).load("http://ubptechnoday3.com/storage/" + event.getGambarBesar()).into(ivThumb);
        Glide.with(getApplicationContext()).load("http://ubptechnoday3.com/storage/" + event.getGambarBesar()).into(ivImageShow);
        ivImageShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ShowImage.class);
                intent.putExtra("link", "http://ubptechnoday3.com/storage/" + event.getGambarBesar());
                startActivity(intent);
            }
        });

        tvNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Uri gmmIntentUri = Uri.parse("geo:"+product.getLat()+","+product.getLng());
//                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
//                mapIntent.setPackage("com.google.android.apps.maps");
//                startActivity(mapIntent);

//                Uri gmmIntentUri = Uri.parse("google.streetview:cbll="+product.getLat()+","+product.getLng());

// Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
//                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
// Make the Intent explicit by setting the Google Maps package
//                mapIntent.setPackage("com.google.android.apps.maps");

// Attempt to start an activity that can handle the Intent
//                startActivity(mapIntent);

//                Uri gmmIntentUri = Uri.parse("geo:"+product.getLat()+","+product.getLng());
//                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
//                mapIntent.setPackage("com.google.android.apps.maps");
//                startActivity(mapIntent);

                Uri uri = Uri.parse("geo:0,0?q=" + event.getLat() + "," + event.getLng() + "(" + event.getAlamat() + ")");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        switch (event.getStatus()) {
            case -1:
                btRegister.setText("PEMBAYARAN DITOLAK !");
                btRegister.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.red));
                break;
            case 0:
                btRegister.setText("MENUNGGU VERIFIKASI PEMBAYARAN");
                btRegister.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
                break;
            case 1:
                btRegister.setText("PEMBAYARAN BERHASIL, LIHAT TIKET");
                btRegister.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.green));
                break;
            case 2:
                btRegister.setText("UPLOAD BUKTI TRANSFER");
                btRegister.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.red));
                break;
            default:
                break;
        }

        ivFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng latLng = new LatLng(event.getLat(), event.getLng());
        float zoomLevel = 16.0f; //This goes up to 21
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel));
        mMap.addMarker(new MarkerOptions().position(latLng).title("Kamu disini"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

    }

    @OnClick(R.id.bt_register)
    public void onViewClicked() {
        if (event.getStatus() == 1) {
            Intent intent = new Intent(getApplicationContext(), MyTicketActivity.class);
            intent.putExtra("pay_id", String.valueOf(event.getId()));
            startActivity(intent);
        } else {
            Intent intent = new Intent(getApplicationContext(), MyPaymentActivity.class);
            intent.putExtra("event_model", event);
            startActivity(intent);
        }
    }
}

