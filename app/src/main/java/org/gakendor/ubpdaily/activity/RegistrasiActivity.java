package org.gakendor.ubpdaily.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;

import org.gakendor.ubpdaily.R;
import org.gakendor.ubpdaily.clients.ApiUtils;
import org.gakendor.ubpdaily.clients.api.MobileService;
import org.gakendor.ubpdaily.clients.api.model.User;
import org.gakendor.ubpdaily.clients.model.Response;
import org.gakendor.ubpdaily.util.MyPref;
import org.gakendor.ubpdaily.util.Static;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;

public class RegistrasiActivity extends AppCompatActivity {

    @BindView(R.id.et_msisdn)
    TextInputEditText etMsisdn;
    @BindView(R.id.et_email)
    TextInputEditText etEmail;
    @BindView(R.id.et_nama)
    TextInputEditText etNama;
    @BindView(R.id.et_instansi)
    TextInputEditText etInstansi;
    @BindView(R.id.et_pwd_1)
    TextInputEditText etPwd1;
    @BindView(R.id.et_pwd_2)
    TextInputEditText etPwd2;
    @BindView(R.id.bt_update)
    Button btUpdate;
    MobileService mobileService;
    MaterialDialog materialDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrasi);
        ButterKnife.bind(this);
        setTitle("Registrasi");

        mobileService = ApiUtils.MobileService(getApplicationContext());
        materialDialog = new MaterialDialog.Builder(RegistrasiActivity.this)
                .content("Loading...")
                .cancelable(false)
                .progress(true, 0)
                .build();

        etMsisdn.setText(getIntent().getStringExtra("msisdn"));
        etMsisdn.setEnabled(false);
    }

    @OnClick(R.id.bt_update)
    public void onViewClicked() {
        if(etMsisdn.getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(), "Tidak boleh kosong !", Toast.LENGTH_LONG).show();
            return;
        }else if(etNama.getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(), "Tidak boleh kosong !", Toast.LENGTH_LONG).show();
            return;
        }else if(etEmail.getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(), "Tidak boleh kosong !", Toast.LENGTH_LONG).show();
            return;
        }else if(etInstansi.getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(), "Tidak boleh kosong !", Toast.LENGTH_LONG).show();
            return;
        }else if(etPwd1.getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(), "Tidak boleh kosong !", Toast.LENGTH_LONG).show();
            return;
        }else if(etPwd2.getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(), "Tidak boleh kosong !", Toast.LENGTH_LONG).show();
            return;
        }else if(etPwd1.getText().toString().length() <= 5){
            Toast.makeText(getApplicationContext(), "Password sedikitnya harus 6 karakter !", Toast.LENGTH_LONG).show();
            return;
        }else if(!etPwd1.getText().toString().equals(etPwd2.getText().toString())){
            Toast.makeText(getApplicationContext(), "Password tidak sama !", Toast.LENGTH_LONG).show();
            return;
        }else{
            materialDialog.show();
            String nama = etNama.getText().toString();
            String email = etEmail.getText().toString();
            String msisdn = etMsisdn.getText().toString();
            String instansi = etInstansi.getText().toString();

            final Map<String, String> map = new HashMap<>();
            map.put("email", email);
            map.put("msisdn", msisdn);
            map.put("nama", nama);
            map.put("instansi", instansi);
            map.put("pwd", etPwd1.getText().toString());

            mobileService.register(map).enqueue(new Callback<Response>() {
                @Override
                public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                    materialDialog.dismiss();
                    if(response.isSuccessful()){
                        if(response.body() != null && response.body().getData() != null){
                            User user = new Gson().fromJson(new Gson().toJson(response.body().getData()), User.class);
                            MyPref.setLoginData(getApplicationContext(), user);
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            Toast.makeText(getApplicationContext(), "Sukses Registrasi", Toast.LENGTH_LONG).show();
                            finish();
                        }else{
                            Toast.makeText(getApplicationContext(), Static.MESSAGE_FAILED, Toast.LENGTH_LONG).show();
                        }
                    }else {
                        Toast.makeText(getApplicationContext(), Static.MESSAGE_FAILED, Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<Response> call, Throwable t) {
                    materialDialog.dismiss();
                    Toast.makeText(getApplicationContext(), Static.MESSAGE_FAILED, Toast.LENGTH_LONG).show();
                }
            });

        }
    }

    @Override
    public void onBackPressed() {

    }
}
