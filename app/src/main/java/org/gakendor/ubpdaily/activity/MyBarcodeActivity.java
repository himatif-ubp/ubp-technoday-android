package org.gakendor.ubpdaily.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.snatik.storage.Storage;

import net.glxn.qrgen.android.QRCode;
import net.glxn.qrgen.core.image.ImageType;

import org.gakendor.ubpdaily.R;
import org.gakendor.ubpdaily.clients.ApiUtils;
import org.gakendor.ubpdaily.clients.api.MobileService;
import org.gakendor.ubpdaily.clients.api.model.User;
import org.gakendor.ubpdaily.clients.model.Response;
import org.gakendor.ubpdaily.model.MyTicket;
import org.gakendor.ubpdaily.model.Payment;
import org.gakendor.ubpdaily.model.Product;
import org.gakendor.ubpdaily.util.BmpUtils;
import org.gakendor.ubpdaily.util.DateUtils;
import org.gakendor.ubpdaily.util.MyPref;
import org.gakendor.ubpdaily.util.Static;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.zelory.compressor.Compressor;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;
import retrofit2.Call;
import retrofit2.Callback;

public class MyBarcodeActivity extends AppCompatActivity implements OnMapReadyCallback {

    Product product;
    @BindView(R.id.tv_alamat)
    TextView tvAlamat;
    @BindView(R.id.tv_tf)
    TextView tvTf;
    @BindView(R.id.tv_payment)
    TextView tvPayment;
    @BindView(R.id.iv_upload)
    ImageView ivUpload;
    MaterialDialog materialDialog;
    MobileService mobileService;
    @BindView(R.id.bt_upload)
    Button btUpload;
    @BindView(R.id.tv_pemesan)
    TextView tvPemesan;
    @BindView(R.id.tv_instansi)
    TextView tvInstansi;

    @BindView(R.id.tv_nav)
    TextView tvNav;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_date)
    TextView tvDate;
    @BindView(R.id.iv_deskripsi)
    TextView tvDeskripsi;
    @BindView(R.id.iv_barcode)
    ImageView ivBarcode;
    @BindView(R.id.iv_image_show)
    ImageView ivImageShow;

    private Storage storage;
    private String pathFile;
    Compressor compressor;

    final private int CODE_CAMERA_1 = 3211;
    final private int CODE_GALLERY_1 = 3212;

    String imageKTPPath;
    User user;
    Payment payment;

    GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_barcode);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Tiket");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        materialDialog = new MaterialDialog.Builder(this)
                .content("Loading...")
                .cancelable(false)
                .progress(true, 0)
                .show();

        mobileService = ApiUtils.MobileService(getApplicationContext());
        compressor = new Compressor(this).setQuality(75).setCompressFormat(Bitmap.CompressFormat.WEBP);
        storage = new Storage(getApplicationContext());

        final Map<String, String> map = new HashMap<>();
        map.put("tr_id", String.valueOf(getIntent().getStringExtra("tr_id")));

        mobileService.getBarcode(map).enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                materialDialog.dismiss();
                if (response.isSuccessful()) {
                    if (response.body() != null && response.body().getData() != null) {
                        MyTicket myTicket = new Gson().fromJson(new Gson().toJson(response.body().getData()), MyTicket.class);
                        System.out.println(new Gson().toJson(myTicket));
                        product = myTicket.getProduct();
                        user = myTicket.getUser();
//                        user.setId(myTicket.getUser().getId());
//                        user.setNamaLengkap(myTicket.getUser().getNamaLengkap());
//                        user.setInstansi(myTicket.getUser().getInstansi());
                        Bitmap myBitmap = QRCode.from(myTicket.getPayment().getTicketId()).to(ImageType.PNG).withSize(250, 250).bitmap();
                        ivBarcode.setImageBitmap(myBitmap);
                        initInformation();
                        setMap();
                    } else {
                        materialDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Barcode tidak tersedia", Toast.LENGTH_LONG).show();
                        finish();
                    }
                } else {
                    materialDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Barcode tidak tersedia", Toast.LENGTH_LONG).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                materialDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Barcode tidak tersedia", Toast.LENGTH_LONG).show();
                finish();
            }
        });

        btUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialDialog.Builder(MyBarcodeActivity.this)
                        .title("Pemberitahuan")
                        .content("Apakah anda yakin akan mengkonfirmasi?")
                        .positiveText("Ya")
                        .negativeText("Tidak")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                materialDialog.show();
                                mobileService.absen(map).enqueue(new Callback<Response>() {
                                    @Override
                                    public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                                        materialDialog.dismiss();
                                        if (response.isSuccessful()) {
                                            if (response.body() != null && response.body().getData() != null) {
                                                finish();
                                                Toast.makeText(getApplicationContext(), "Berhasil absen", Toast.LENGTH_LONG).show();
                                            } else {
                                                materialDialog.dismiss();
                                                Toast.makeText(getApplicationContext(), "Gagal absen", Toast.LENGTH_LONG).show();
                                                finish();
                                            }
                                        } else {
                                            materialDialog.dismiss();
                                            Toast.makeText(getApplicationContext(), "Gagal absen", Toast.LENGTH_LONG).show();
                                            finish();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Response> call, Throwable t) {
                                        materialDialog.dismiss();
                                        Toast.makeText(getApplicationContext(), "Gagal absen", Toast.LENGTH_LONG).show();
                                        finish();
                                    }
                                });
                            }
                        })
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        });




    }

    private void initInformation() {
        if (product != null) {
            tvTitle.setText(product.getJudul() + "\n" + product.getSubJudul());
            tvDate.setText(DateUtils.getDateUI(product.getTanggal()));
            tvAlamat.setText(product.getTempat());
            tvPemesan.setText(user.getNamaLengkap());
            tvInstansi.setText(user.getInstansi());
            tvDeskripsi.setText(Html.fromHtml(product.getDeskripsi()));
            Glide.with(getApplicationContext()).load("http://ubptechnoday3.com/storage/" + product.getGambarBesar()).into(ivImageShow);
            ivImageShow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), ShowImage.class);
                    intent.putExtra("link", "http://ubptechnoday3.com/storage/" + product.getGambarBesar());
                    startActivity(intent);
                }
            });
        }
    }

    private void checkStatus() {
        if (payment.getBuktiTransfer() != null) {
            Glide.with(getApplicationContext()).load("http://api.ubptechnoday3.com/storage/bukti_tf/" + payment.getBuktiTransfer()).into(ivUpload);
        }
        switch (payment.getStatus()) {
            case -1:
                btUpload.setText("PEMBAYARAN DITOLAK !");
                btUpload.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.red));
                break;
            case 0:
                btUpload.setText("MENUNGGU VERIFIKASI PEMBAYARAN");
                btUpload.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
                break;
            case 1:
                btUpload.setText("PEMBAYARAN BERHASIL, LIHAT TIKET");
                btUpload.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.green));
                break;
            case 2:
                btUpload.setText("UPLOAD BUKTI TRANSFER");
                btUpload.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.red));
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        EasyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onImagePicked(File imageFile, EasyImage.ImageSource source, int type) {
                switch (type) {
                    case CODE_CAMERA_1:
                        String nameNewFile = prepareUpload(imageFile);
                        imageKTPPath = nameNewFile;
                        Glide.with(MyBarcodeActivity.this).load(imageFile).into(ivUpload);
                        payment.setStatus(2);
                        checkStatus();
                        break;
                    case CODE_GALLERY_1:
                        nameNewFile = prepareUpload(imageFile);
                        imageKTPPath = nameNewFile;
                        Glide.with(MyBarcodeActivity.this).load(imageFile).into(ivUpload);
                        payment.setStatus(2);
                        checkStatus();
                        break;
                }
            }

        });
    }

    PermissionListener permissionListener() {
        return new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                String[] list = new String[]{"Kamera", "Galeri"};
                new MaterialDialog.Builder(MyBarcodeActivity.this)
                        .title("Pilih")
                        .items(list)
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                                if (position == 0) {
                                    EasyImage.openCamera(MyBarcodeActivity.this, CODE_CAMERA_1);
                                } else if (position == 1) {
                                    EasyImage.openGallery(MyBarcodeActivity.this, CODE_GALLERY_1);
                                }
                            }
                        }).show();
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                finish();
            }
        };
    }

    private void checkAndPickPicture() {
        TedPermission.with(this)
                .setPermissionListener(permissionListener())
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                .check();
    }

    private String prepareUpload(File imageFile) {
        byte[] bytes = storage.readFile(imageFile.getPath());
        String nameNewImage = user.getId() + "_PAYMENT_" + System.currentTimeMillis() + ".jpg";
        String nameNewFile = pathFile + "/" + nameNewImage;
        storage.createFile(nameNewFile, bytes);

        Bitmap original = BmpUtils.decodeFile(new File(nameNewFile), 800, 800);
        BmpUtils.compress(original, nameNewImage);
        return nameNewFile;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        setMap();
    }

    private void setMap() {
        if (product != null) {
            LatLng latLng = new LatLng(product.getLat(), product.getLng());
            float zoomLevel = 16.0f; //This goes up to 21
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel));
            mMap.addMarker(new MarkerOptions().position(latLng).title("Kamu disini"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
