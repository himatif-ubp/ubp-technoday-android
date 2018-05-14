package org.gakendor.ubpdaily.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.gakendor.ubpdaily.R;
import org.gakendor.ubpdaily.clients.ApiUtils;
import org.gakendor.ubpdaily.clients.api.MobileService;
import org.gakendor.ubpdaily.clients.api.model.User;
import org.gakendor.ubpdaily.clients.model.Response;
import org.gakendor.ubpdaily.util.MyPref;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;

import com.afollestad.materialdialogs.MaterialDialog;
import com.facebook.accountkit.AccessToken;
import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.PhoneNumber;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.ll_ui_action)
    LinearLayout llUiAction;
    @BindView(R.id.bt_masuk)
    Button btMasuk;

    MaterialDialog materialDialog;
    MobileService mobileService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        materialDialog = new MaterialDialog.Builder(this)
                .content("Loading...")
                .cancelable(false)
                .progress(true, 0)
                .build();

        mobileService = ApiUtils.MobileService(getApplicationContext());

        initAnimation();
    }

    private void initAnimation() {
        llUiAction.setVisibility(View.GONE);
        final Animation slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    if (MyPref.getLoginData(getApplicationContext()) != null) {
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                llUiAction.setVisibility(View.VISIBLE);
                                llUiAction.setAnimation(slideUp);
                            }
                        });
                    }
                }
            }
        }).start();
    }

    @Override
    protected void onActivityResult(
            final int requestCode,
            final int resultCode,
            final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == APP_REQUEST_CODE) { // confirm that this response matches your request
            AccountKitLoginResult loginResult = data.getParcelableExtra(AccountKitLoginResult.RESULT_KEY);
            String toastMessage;
            if (loginResult.getError() != null) {
                toastMessage = loginResult.getError().getErrorType().getMessage();
                Toast.makeText(getApplicationContext(), loginResult.getError().getUserFacingMessage(), Toast.LENGTH_LONG).show();
            } else if (loginResult.wasCancelled()) {
                toastMessage = "Login Cancelled";
            } else {
                if (loginResult.getAccessToken() != null) {
                    toastMessage = "Success:" + loginResult.getAccessToken().getAccountId();
                } else {
                    toastMessage = String.format(
                            "Success:%s...",
                            loginResult.getAuthorizationCode().substring(0,10));
                }

                // If you have an authorization code, retrieve it from
                // loginResult.getAuthorizationCode()
                // and pass it to your server and exchange it for an access token.

                // Success! Start your next activity...

//                final AccessToken accessToken = loginResult.getAccessToken();
//                if (accessToken != null) {
//
//                }
                materialDialog.show();
                AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
                    @Override
                    public void onSuccess(final Account account) {
                        final PhoneNumber number = account.getPhoneNumber();
                        if (number != null) {
                            final String phoneNumber = number.toString().replaceFirst("\\+62", "0");
                            Map<String, String> params  = new HashMap<String, String>();
                            params.put("msisdn", phoneNumber);
                            mobileService.checkNumber(params)
                                    .enqueue(new Callback<Response>() {
                                        @Override
                                        public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                                            materialDialog.dismiss();
                                            if(response.isSuccessful()){
                                                if(response.body() != null){
                                                    Response body = response.body();
                                                    if(body.getData() == null){
                                                        // registrasi
                                                        Intent intent = new Intent(getApplicationContext(), RegistrasiActivity.class);
                                                        intent.putExtra("msisdn", phoneNumber);
                                                        startActivity(intent);
                                                        finish();
                                                    }else{
                                                        // login
                                                        String json = new Gson().toJson(body.getData());
                                                        User user = new Gson().fromJson(json, User.class);
                                                        MyPref.setLoginData(getApplicationContext(), user);
                                                        Toast.makeText(getApplicationContext(), "Welcome back "+user.getNamaLengkap()+" !", Toast.LENGTH_LONG).show();
                                                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                                        finish();
                                                    }
                                                }else{
                                                    Toast.makeText(getApplicationContext(), "Gagal login, silahkan coba lagi", Toast.LENGTH_LONG).show();
                                                }
                                            }else{
                                                Toast.makeText(getApplicationContext(), "Gagal login, silahkan coba lagi", Toast.LENGTH_LONG).show();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<Response> call, Throwable t) {
                                            materialDialog.dismiss();
                                        }
                                    });
                        }else{
                            Log.e("error", "Number null=");
                        }
                    }

                    @Override
                    public void onError(final AccountKitError error) {
                        Log.e("error", "AccountKitError=" + error);
                    }
                });

//                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }

            // Surface the result to your user in an appropriate way.
//            Toast.makeText(
//                    this,
//                    toastMessage,
//                    Toast.LENGTH_LONG)
//                    .show();
        }
    }

    @OnClick(R.id.bt_masuk)
    public void onViewClicked() {
        phoneLogin();
    }



    public static int APP_REQUEST_CODE = 99;

    public void phoneLogin() {
        final Intent intent = new Intent(LoginActivity.this, AccountKitActivity.class);
        AccountKitConfiguration.AccountKitConfigurationBuilder configurationBuilder =
                new AccountKitConfiguration.AccountKitConfigurationBuilder(
                        LoginType.PHONE,
                        AccountKitActivity.ResponseType.TOKEN);

        intent.putExtra(
                AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION,
                configurationBuilder.build());
        startActivityForResult(intent, APP_REQUEST_CODE);
    }
}
