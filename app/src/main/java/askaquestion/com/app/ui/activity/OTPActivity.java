package askaquestion.com.app.ui.activity;

import static askaquestion.com.app.core.constants.ConstantsAPI.URL_FORGOT;
import static askaquestion.com.app.core.constants.ConstantsAPI.URL_VERIFY_OTP;
import static askaquestion.com.app.core.constants.Extras.EMAIL;
import static askaquestion.com.app.core.constants.Extras.TOKEN;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.optimumbrew.library.core.volley.GsonRequest;
import com.optimumbrew.library.core.volley.MyVolley;

import askaquestion.com.app.R;
import askaquestion.com.app.core.sharedprefs.PrefManager;
import askaquestion.com.app.core.sharedprefs.SharedManagerForVolley;
import askaquestion.com.app.databinding.ActivityOtpactivityBinding;
import askaquestion.com.app.core.pojo.forgotpasswordapi.RequestForgot;
import askaquestion.com.app.core.pojo.forgotpasswordapi.RootForgot;
import askaquestion.com.app.core.pojo.verifyOtpForUserapi.Rootverifyotp;
import askaquestion.com.app.core.pojo.verifyOtpForUserapi.Rootverifyresponse;
import askaquestion.com.app.databinding.DialogOtpBinding;

public class OTPActivity extends AppCompatActivity {


    ActivityOtpactivityBinding binding;
    String TAG = "OTPActivity";
    ProgressDialog progressDialog;
    PrefManager prefManager;
    SharedManagerForVolley managerForVolley;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOtpactivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        progressDialog = new ProgressDialog(OTPActivity.this, R.style.ProgressDialog);
        prefManager = new PrefManager(getApplicationContext());
        managerForVolley = new SharedManagerForVolley(getApplicationContext());

        binding.buttonSendOTP.setOnClickListener(v -> {
            if (binding.editEmail.getText().toString().isEmpty()) {
                binding.editEmail.setError("Enter Your Email");
            } else {
                showProgress();
                sendOtp();
            }
        });
    }

    public void sendOtp() {

        RequestForgot requestForgot = new RequestForgot();
        requestForgot.setEmail_id(binding.editEmail.getText().toString());
        String body = new Gson().toJson(requestForgot);
        Log.i(TAG, "sendOtp: " + body);
        GsonRequest<RootForgot> gsonRequest = new GsonRequest<RootForgot>(1,
                URL_FORGOT,
                body,
                RootForgot.class,
                null,
                response -> {
                    if (response.getCode() == 200 && response != null) {
                        openDialogForOTP();
                        Log.i(TAG, "onResponse_data: " + response.getMessage());
                    }
                }, error -> {
            progressDialog.cancel();
            if(error instanceof TimeoutError){
                showSnackbar("Server Not Response");
            }
            Log.i(TAG, "sendOtp: " + error);
        });
        MyVolley.getInstance(getApplicationContext()).addToRequestQueue(gsonRequest);

    }

    private void openDialogForOTP() {
        progressDialog.cancel();
        DialogOtpBinding bind = DialogOtpBinding.inflate(getLayoutInflater());
        AlertDialog.Builder builder = new AlertDialog.Builder(OTPActivity.this);
        builder.setView(bind.getRoot());
        AlertDialog dialog = builder.create();
        dialog.show();

        bind.buttonVerify.setOnClickListener(v -> {
            String o1 = bind.editOTP.getText().toString();

            int otp = Integer.parseInt(o1);
            Rootverifyotp rootverifyotp = new Rootverifyotp();
            rootverifyotp.setEmail_id(managerForVolley.getEmail());
            rootverifyotp.setToken(otp);

            String body = new Gson().toJson(rootverifyotp);
            Log.i(TAG, "openDialogForOTP: " + body);//{"email_id":"john2029@mailinator.com","token":8245}
            GsonRequest<Rootverifyresponse> gsonRequest = new GsonRequest<Rootverifyresponse>(1,
                    URL_VERIFY_OTP,
                    body,
                    Rootverifyresponse.class,
                    null,
                    response -> {
                        Log.i(TAG, "onResponse: " + response.getMessage());//
                        if (response.getCode() == 200) {
                            Log.i(TAG, "onResponse: 200" + response.getMessage());
                            Intent intent = new Intent(getApplicationContext(), NewPasswordActivity.class);
                            intent.putExtra(TOKEN, response.getData().getToken());
                            intent.putExtra(EMAIL, binding.editEmail.getText().toString().trim());
                            startActivity(intent);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.i(TAG, "onErrorResponse: " + error);
                }
            });
            MyVolley.getInstance(getApplicationContext()).addToRequestQueue(gsonRequest);
        });

    }

    public void showProgress() {
        progressDialog.setMessage("Loading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        progressDialog.setCancelable(true);
    }

    private void showSnackbar(String str) {
        Snackbar snackbar = Snackbar.make(binding.getRoot(), str, BaseTransientBottomBar.LENGTH_SHORT);
        snackbar.setAction("Dismiss", v -> Toast.makeText(getApplicationContext(), "Dismiss", Toast.LENGTH_SHORT).show()).show();
    }
}