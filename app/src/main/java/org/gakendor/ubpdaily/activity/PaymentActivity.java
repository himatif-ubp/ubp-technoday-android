package org.gakendor.ubpdaily.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.snatik.storage.Storage;

import org.gakendor.ubpdaily.R;
import org.gakendor.ubpdaily.clients.ApiUtils;
import org.gakendor.ubpdaily.clients.api.MobileService;
import org.gakendor.ubpdaily.clients.api.model.User;
import org.gakendor.ubpdaily.clients.model.Response;
import org.gakendor.ubpdaily.model.Event;
import org.gakendor.ubpdaily.model.Payment;
import org.gakendor.ubpdaily.util.BmpUtils;
import org.gakendor.ubpdaily.util.DateUtils;
import org.gakendor.ubpdaily.util.IDRUtils;
import org.gakendor.ubpdaily.util.MyPref;
import org.gakendor.ubpdaily.util.PartUtils;
import org.gakendor.ubpdaily.util.Static;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.zelory.compressor.Compressor;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;
import retrofit2.Call;
import retrofit2.Callback;

public class PaymentActivity extends AppCompatActivity {

    Event event;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_date)
    TextView tvDate;
    @BindView(R.id.tv_alamat)
    TextView tvAlamat;
    @BindView(R.id.tv_pemesan)
    TextView tvPemesan;
    @BindView(R.id.tv_instansi)
    TextView tvInstansi;
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
    @BindView(R.id.tv_maps)
    TextView tvMaps;

    private Storage storage;
    private String pathFile;
    Compressor compressor;

    final private int CODE_CAMERA_1 = 3211;
    final private int CODE_GALLERY_1 = 3212;

    String imageKTPPath;
    User user;
    Payment payment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Pembayaran");

        event = (Event) getIntent().getSerializableExtra("event_model");

        materialDialog = new MaterialDialog.Builder(this)
                .content("Loading...")
                .cancelable(false)
                .progress(true, 0)
                .show();

        mobileService = ApiUtils.MobileService(getApplicationContext());
        compressor = new Compressor(this).setQuality(75).setCompressFormat(Bitmap.CompressFormat.WEBP);
        storage = new Storage(getApplicationContext());

        tvTitle.setText(event.getJudul() + "\n" + event.getSubJudul());
        tvDate.setText(DateUtils.getDateUI(event.getTanggal()));
        tvAlamat.setText(event.getAlamat());
        user = MyPref.getLoginData(getApplicationContext());
        tvPemesan.setText(user.getNamaLengkap());
        tvInstansi.setText(user.getInstansi());


        final Map<String, String> map = new HashMap<>();
        map.put("user_id", String.valueOf(user.getId()));
        map.put("product_id", String.valueOf(event.getId()));

        mobileService.payment(map).enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                materialDialog.dismiss();
                if (response.isSuccessful()) {
                    if (response.body() != null && response.body().getData() != null) {
                        String json = new Gson().toJson(response.body().getData());
                        payment = new Gson().fromJson(json, Payment.class);
                        tvPayment.setText(IDRUtils.toRupiah(payment.getBayar()));
                        checkStatus();
                        System.out.println(new Gson().toJson(payment));
                    } else {
                        Toast.makeText(getApplicationContext(), Static.MESSAGE_FAILED, Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), Static.MESSAGE_FAILED, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                materialDialog.dismiss();
                Toast.makeText(getApplicationContext(), Static.MESSAGE_FAILED, Toast.LENGTH_LONG).show();
            }
        });

        TedPermission.with(getApplicationContext())
                .setPermissionListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        String path = storage.getExternalStorageDirectory();
                        pathFile = path + File.separator + Static.DIR_IMAGE;
                        storage.createDirectory(pathFile);
                    }

                    @Override
                    public void onPermissionDenied(ArrayList<String> deniedPermissions) {

                    }
                })
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .check();

        ivUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAndPickPicture();
            }
        });

        btUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (payment.getStatus() == 1) {
                    Intent intent = new Intent(getApplicationContext(), MyTicketActivity.class);
                    intent.putExtra("pay_id", String.valueOf(payment.getId()));
                    startActivity(intent);
                } else {
                    if (imageKTPPath != null) {
                        materialDialog.show();
                        File file = new File(imageKTPPath);
                        MultipartBody.Part body = PartUtils.prepareFilePart("gambar", file);
                        RequestBody ket = PartUtils.createPartFromString(String.valueOf("upload bukti"));
                        RequestBody trId = PartUtils.createPartFromString(String.valueOf(payment.getId()));

                        final Map<String, RequestBody> map = new HashMap<>();
                        map.put("keterangan", ket);
                        map.put("tr_id", trId);

                        mobileService.uploadBuktiTF(body, map)
                                .enqueue(new Callback<Response>() {
                                    @Override
                                    public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                                        materialDialog.dismiss();
                                        if (response.isSuccessful()) {
                                            if (response.body() != null && response.body().getData() != null) {
                                                Payment paymentData = new Gson().fromJson(new Gson().toJson(response.body().getData()), Payment.class);
                                                if (paymentData != null) {
                                                    payment = paymentData;
                                                    checkStatus();
                                                    Glide.with(getApplicationContext()).load("http://api.ubptechnoday3.com/storage/bukti_tf/" + paymentData.getBuktiTransfer()).into(ivUpload);
                                                } else {
                                                    Toast.makeText(getApplicationContext(), Static.MESSAGE_FAILED, Toast.LENGTH_LONG).show();
                                                }
                                            } else {
                                                Toast.makeText(getApplicationContext(), Static.MESSAGE_FAILED, Toast.LENGTH_LONG).show();
                                            }
                                        } else {
                                            Toast.makeText(getApplicationContext(), Static.MESSAGE_FAILED, Toast.LENGTH_LONG).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Response> call, Throwable t) {
                                        materialDialog.dismiss();
                                        Toast.makeText(getApplicationContext(), Static.MESSAGE_FAILED, Toast.LENGTH_LONG).show();
                                    }
                                });
                    } else {
                        Toast.makeText(getApplicationContext(), "Pilih foto terlebih dahulu", Toast.LENGTH_LONG).show();
                    }
                }

            }
        });

        tvMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(event != null){
                    Uri uri = Uri.parse("geo:0,0?q=" + event.getLat() + "," + event.getLng() + "(" + event.getAlamat() + ")");
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
            }
        });


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
                        Glide.with(PaymentActivity.this).load(imageFile).into(ivUpload);
                        payment.setStatus(2);
                        checkStatus();
                        break;
                    case CODE_GALLERY_1:
                        nameNewFile = prepareUpload(imageFile);
                        imageKTPPath = nameNewFile;
                        Glide.with(PaymentActivity.this).load(imageFile).into(ivUpload);
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
                new MaterialDialog.Builder(PaymentActivity.this)
                        .title("Pilih")
                        .items(list)
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                                if (position == 0) {
                                    EasyImage.openCamera(PaymentActivity.this, CODE_CAMERA_1);
                                } else if (position == 1) {
                                    EasyImage.openGallery(PaymentActivity.this, CODE_GALLERY_1);
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

    @OnClick(R.id.textView28)
    public void onViewClicked() {
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
