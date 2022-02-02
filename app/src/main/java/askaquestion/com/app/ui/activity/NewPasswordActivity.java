package askaquestion.com.app.ui.activity;

import static askaquestion.com.app.core.constants.ConstantsAPI.URL_NEW_PASSWORD;
import static askaquestion.com.app.core.constants.Extras.EMAIL;
import static askaquestion.com.app.core.constants.Extras.TOKEN;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.optimumbrew.library.core.volley.GsonRequest;
import com.optimumbrew.library.core.volley.MyVolley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import askaquestion.com.app.core.sharedprefs.SharedManagerForVolley;
import askaquestion.com.app.databinding.ActivityNewPasswordBinding;
import askaquestion.com.app.core.pojo.forgotpasswordapi.RequestForgot;
import askaquestion.com.app.core.pojo.forgotpasswordapi.RootForgot;

public class NewPasswordActivity extends AppCompatActivity {

    ActivityNewPasswordBinding binding;
    SharedManagerForVolley managerForVolley;
    String TAG="NewPasswordActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityNewPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        managerForVolley=new SharedManagerForVolley(getApplicationContext());

        binding.buttonSave.setOnClickListener(v ->{
            String newPassword = binding.editNewPassword.getText().toString().trim();
            String confirmNewPassword = binding.editConfirmNewPassword.getText().toString().trim();

            String pass = "^(?=.*[0-9])"
                    + "(?=.*[a-z])(?=.*[A-Z])"
                    + "(?=.*[@#$%^&+=])"
                    + "(?=\\S+$).{8,20}$";
            Pattern p = Pattern.compile(pass);
            Matcher m = p.matcher(newPassword);

            if (newPassword.isEmpty()) {
                binding.editNewPassword.setError("Please Enter Password");
            } else if (!m.matches()) {
                binding.editNewPassword.setError("Password Must be At Least 8 Character");
            } else if (confirmNewPassword.isEmpty()) {
                binding.editConfirmNewPassword.setError("Please Enter Password");
            } else if (!newPassword.equals(confirmNewPassword)) {
                Toast.makeText(getApplicationContext(), "Password Not Match", Toast.LENGTH_SHORT).show();
            }else {
                RequestForgot requestForgot = new RequestForgot();
                requestForgot.setEmail_id(getIntent().getStringExtra(EMAIL));
                requestForgot.setPassword(binding.editNewPassword.getText().toString());
                requestForgot.setToken(getIntent().getStringExtra(TOKEN));

                String request = new Gson().toJson(requestForgot);//{"email_id":"john2029@mailinator.com","password":"John@2029","token":"9Dnpuzw9H5WtpGcRStpM0P0jnf0CxW"}
                Log.i(TAG, "onCreate: " + request);

                try {
                    JSONObject obj = new JSONObject(request);
                    Log.i(TAG, "onCreate: "+obj.toString());
                    GsonRequest<RootForgot> gsonRequest = new GsonRequest<RootForgot>(1,
                            URL_NEW_PASSWORD,
                            obj.toString(),
                            RootForgot.class,
                            null,
                            new Response.Listener<RootForgot>() {
                                @Override
                                public void onResponse(RootForgot response) {
                                    Log.i(TAG, "onResponse: " + response.getMessage());
                                    if (response.getCode() == 200) {
                                        startActivity(new Intent(getApplicationContext(), SigninActivity.class));
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.i(TAG, "onErrorResponse: " + error);
                            showSnackbar(error.toString());
                        }
                    });
                    MyVolley.getInstance(getApplicationContext()).addToRequestQueue(gsonRequest);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        binding.imageButtonEye1.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        binding.editNewPassword.setInputType(InputType.TYPE_CLASS_TEXT);
                        break;
                    case MotionEvent.ACTION_UP:
                        binding.editNewPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        break;
                }
                return true;
            }
        });
        binding.imageButtonEye2.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        binding.editConfirmNewPassword.setInputType(InputType.TYPE_CLASS_TEXT);
                        break;
                    case MotionEvent.ACTION_UP:
                        binding.editConfirmNewPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        break;
                }
                return true;
            }
        });
    }
    private void showSnackbar(String str) {
        Snackbar snackbar = Snackbar.make(binding.getRoot(), str, BaseTransientBottomBar.LENGTH_SHORT);
        snackbar.setAction("Dismiss", v -> Toast.makeText(getApplicationContext(), "Dismiss", Toast.LENGTH_SHORT).show()).show();
    }
}