package org.gakendor.ubpdaily.activity;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
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

public class UpdateProfileActivity extends AppCompatActivity {

    @BindView(R.id.et_msisdn)
    TextInputEditText etMsisdn;
    @BindView(R.id.et_email)
    TextInputEditText etEmail;
    @BindView(R.id.et_nama)
    TextInputEditText etNama;
    @BindView(R.id.et_instansi)
    TextInputEditText etInstansi;
    @BindView(R.id.textView8)
    TextView textView8;
    @BindView(R.id.bt_update)
    Button btUpdate;
    MobileService mobileService;
    MaterialDialog materialDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Update Profile");

        mobileService = ApiUtils.MobileService(getApplicationContext());
        materialDialog = new MaterialDialog.Builder(UpdateProfileActivity.this)
                .content("Loading...")
                .cancelable(false)
                .progress(true, 0)
                .build();

        loadLoginData();


    }

    private void loadLoginData() {
        User user = MyPref.getLoginData(getApplicationContext());
        etMsisdn.setText(user.getMsisdn());
        etNama.setText(user.getNamaLengkap());
        etEmail.setText(user.getEmail());
        etInstansi.setText(user.getInstansi());

        etMsisdn.setEnabled(false);
        etEmail.setEnabled(false);
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
        }else{
            materialDialog.show();
            String nama = etNama.getText().toString();
            String instansi = etInstansi.getText().toString();

            final Map<String, String> map = new HashMap<>();
            map.put("id", String.valueOf(MyPref.getLoginData(getApplicationContext()).getId()));
            map.put("nama", nama);
            map.put("instansi", instansi);

            mobileService.updateProfile(map).enqueue(new Callback<Response>() {
                @Override
                public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                    materialDialog.dismiss();
                    if(response.isSuccessful()){
                        if(response.body() != null && response.body().getData() != null){
                            User user = new Gson().fromJson(new Gson().toJson(response.body().getData()), User.class);
                            MyPref.setLoginData(getApplicationContext(), user);
                            loadLoginData();
                            Toast.makeText(getApplicationContext(), "Sukses Update Profile", Toast.LENGTH_LONG).show();
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
