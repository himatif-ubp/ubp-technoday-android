package org.gakendor.ubpdaily.activity;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
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

public class UpdatePasswordActivity extends AppCompatActivity {

    @BindView(R.id.et_pwd_old)
    TextInputEditText etPwdOld;
    @BindView(R.id.et_pwd_new_1)
    TextInputEditText etPwdNew1;
    @BindView(R.id.et_pwd_new_2)
    TextInputEditText etPwdNew2;
    MobileService mobileService;
    MaterialDialog materialDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);
        ButterKnife.bind(this);

        mobileService = ApiUtils.MobileService(getApplicationContext());
        materialDialog = new MaterialDialog.Builder(UpdatePasswordActivity.this)
                .content("Loading...")
                .cancelable(false)
                .progress(true, 0)
                .build();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Update Profile");


    }

    @OnClick(R.id.bt_update)
    public void onViewClicked() {
        if(etPwdOld.getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(), "Tidak boleh kosong !", Toast.LENGTH_LONG).show();
            return;
        }else if(etPwdNew1.getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(), "Tidak boleh kosong !", Toast.LENGTH_LONG).show();
            return;
        }else if(etPwdNew2.getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(), "Tidak boleh kosong !", Toast.LENGTH_LONG).show();
            return;
        }else if(etPwdNew1.getText().toString().length() <= 5 || etPwdNew2.getText().toString().length() <= 5){
            Toast.makeText(getApplicationContext(), "Password harus lebih dari 6 karakter!", Toast.LENGTH_LONG).show();
            return;
        } else if(!etPwdNew1.getText().toString().equals(etPwdNew2.getText().toString())){
            Toast.makeText(getApplicationContext(), "Password Baru tidak sama !", Toast.LENGTH_LONG).show();
            return;
        }else{
            materialDialog.show();
            String pwdOld = etPwdOld.getText().toString();
            String pwdNew = etPwdNew1.getText().toString();

            Map<String, String> map = new HashMap<>();
            map.put("id", String.valueOf(MyPref.getLoginData(getApplicationContext()).getId()));
            map.put("pwd_old", pwdOld);
            map.put("pwd_new_1", pwdNew);

            mobileService.updatePassword(map).enqueue(new Callback<Response>() {
                @Override
                public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                    materialDialog.dismiss();
                    if(response.isSuccessful()){
                        if(response.body() != null && response.body().getData() != null){
                            User user = new Gson().fromJson(new Gson().toJson(response.body().getData()), User.class);
                            MyPref.setLoginData(getApplicationContext(), user);
                            Toast.makeText(getApplicationContext(), "Sukses Update Password", Toast.LENGTH_LONG).show();
                            finish();
                        }else{
                            if(response.body().getMessage() != null) {
                                Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                            }else{
                                Toast.makeText(getApplicationContext(), Static.MESSAGE_FAILED, Toast.LENGTH_LONG).show();
                            }
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
