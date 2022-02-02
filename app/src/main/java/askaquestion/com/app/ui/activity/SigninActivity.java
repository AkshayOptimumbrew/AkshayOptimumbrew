package askaquestion.com.app.ui.activity;

import static askaquestion.com.app.core.constants.ConstantsAPI.URL_LOGIN;
import static askaquestion.com.app.core.constants.ConstantsAPI.URL_LOGIN2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.Settings;
import android.text.InputType;
import android.text.format.Time;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.optimumbrew.library.core.volley.GsonRequest;
import com.optimumbrew.library.core.volley.MyVolley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import askaquestion.com.app.R;
import askaquestion.com.app.core.interneconnection.DetectConnection;
import askaquestion.com.app.core.pojo.login.RequestDataLogin;
import askaquestion.com.app.core.sharedprefs.PrefManager;
import askaquestion.com.app.core.sharedprefs.SharedManagerForVolley;
import askaquestion.com.app.databinding.ActivitySigninBinding;
import askaquestion.com.app.core.pojo.signinapi.DeviceInfoLogin;
import askaquestion.com.app.core.pojo.signinapi.LoginData;
import askaquestion.com.app.core.pojo.signinapi.Userlogin;

public class SigninActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 1;
    ActivitySigninBinding binding;
    PrefManager prefManager;
    String udid;
    SharedManagerForVolley volleyManager;
    String TAG = "SigninActivity";
    ProgressDialog progressDialog;
    GoogleSignInClient mGoogleSignInClient;
    CallbackManager callbackManager;
    Typeface typeface;
    private static final String EMAIL = "email";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySigninBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (!DetectConnection.checkInternetConnection(getApplicationContext())) {
            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }

        prefManager = new PrefManager(getApplicationContext());
        volleyManager = new SharedManagerForVolley(getApplicationContext());

        Log.i(TAG, "onCreate: " + volleyManager.getImagePath());
        progressDialog = new ProgressDialog(SigninActivity.this, R.style.ProgressDialog);

        typeface = ResourcesCompat.getFont(getApplicationContext(), R.font.sf_atarian_system_extended);

        if (volleyManager.getLoginStatus()) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }
        udid = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);


        binding.buttonFacebook.setOnClickListener(view -> {
            LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList(EMAIL));
        });

        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                prefManager.setLoginStatus(true);
                Log.i(TAG, "onSuccess: ");
                AccessToken token = loginResult.getAccessToken();

                GraphRequest request = GraphRequest.newMeRequest(
                        token,
                        (object, response) -> {
                            // Insert your code here
                            Log.i(TAG, "onCompleted: " + response.toString());
                        });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                Log.d("MainActivity", "onCancel: ");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("MainActivity", "onCancel: ");
            }
        });
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.server_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        updateUI(account);


        binding.buttonGoogle.setOnClickListener(v -> {
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        });
        binding.imageShowPassBtn.setOnTouchListener((v, event) -> {

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    binding.editPassword.setInputType(InputType.TYPE_CLASS_TEXT);
                    binding.editPassword.setTypeface(typeface);
                    break;
                case MotionEvent.ACTION_UP:
                    binding.editPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    binding.editPassword.setTypeface(typeface);
                    break;
            }
            return true;
        });
        binding.textSignUp.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), SignupActivity.class)));
        binding.textForgotPassword.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), OTPActivity.class)));
        binding.buttonLogin.setOnClickListener(v -> {

            Pattern p_Email = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
            Matcher m_Email = p_Email.matcher(binding.editEmail.getText().toString());

            String pass = "^(?=.*[0-9])"
                    + "(?=.*[a-z])(?=.*[A-Z])"
                    + "(?=.*[@#$%^&+=])"
                    + "(?=\\S+$).{8,20}$";
            Pattern p = Pattern.compile(pass);
            Matcher m = p.matcher(binding.editPassword.getText().toString());

            if (binding.editEmail.getText().toString().isEmpty()) {
                binding.editEmail.setError("Please Enter Email");
                binding.editEmail.requestFocus();
            } else if (!m_Email.matches()) {
                binding.editEmail.setError("Please Enter Valid Email");
                binding.editEmail.requestFocus();
            } else if (binding.editPassword.getText().toString().isEmpty()) {
                binding.editPassword.setError("Please Enter Password");
                binding.editPassword.requestFocus();
            } else if (!m.matches()) {
                showSnackbar("Password Must be At Least 8 Character");
                binding.editPassword.requestFocus();
            } else if (!DetectConnection.checkInternetConnection(getApplicationContext())) {
                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
            } else {
                DeviceInfoLogin deviceInfo = new DeviceInfoLogin();
                deviceInfo.setDevice_udid(udid);

                LoginData loginData = new LoginData();
                loginData.setEmail_id(binding.editEmail.getText().toString());
                prefManager.setEmail(binding.editEmail.getText().toString());
                loginData.setPassword(binding.editPassword.getText().toString());
                loginData.setDevice_info(deviceInfo);

                String request = new Gson().toJson(loginData);
                Log.i(TAG, "onCreate: " + request);
                GsonRequest<Userlogin> gsonRequest = new GsonRequest<Userlogin>(Request.Method.POST, URL_LOGIN, request,
                        Userlogin.class, null, response -> {
                    if (response.getCode() == 200 && response != null) {
                        Log.i(TAG, "onResponse:_message" + response.getMessage());
                        Log.i(TAG, "onResponse:_code" + response.getCode());
                        volleyManager.setToken(response.getData().getToken());
                        Log.i(TAG, "onResponse: Token" + volleyManager.getToken());
                        volleyManager.setLoginStatus(true);
                        showProgress();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    } else if (response.getCode() == 201) {
                        showSnackbar("Email And Password Are Not Match");
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        if (error instanceof TimeoutError) {
                            showSnackbar("Server Not Response");
                        } else if (error instanceof ParseError) {
                            showSnackbar("Check Your Email And Password");
                        }
                        Log.i(TAG, "onErrorResponse: " + error);
                    }
                });
                gsonRequest.setShouldCache(true);
                MyVolley.getInstance(getApplicationContext()).addToRequestQueue(gsonRequest);
            }

        });
    }


    private void updateUI(GoogleSignInAccount account) {

    }

    private void showSnackbar(String error) {
        Snackbar snackbar = Snackbar.make(binding.getRoot(), error.toString(), BaseTransientBottomBar.LENGTH_SHORT);
        snackbar.setAction("Dismiss", v -> Toast.makeText(getApplicationContext(), "Dismiss", Toast.LENGTH_SHORT).show()).show();
    }

    public void showProgress() {
        progressDialog.setMessage("Loading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        progressDialog.setCancelable(true);
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            updateUI(account);
        } catch (ApiException e) {
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
            if (task.isSuccessful()) {
                Log.i(TAG, "onActivityResult: Token:::" + task.getResult().getIdToken());
                volleyManager.setToken(task.getResult().getIdToken());
                volleyManager.setLoginStatus(true);
                showProgress();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                /*Log.i(TAG, "onActivityResult: ");
                DeviceInfoLogin deviceInfo = new DeviceInfoLogin();
                deviceInfo.setDevice_udid(udid);

                LoginData loginData = new LoginData();
                loginData.setEmail_id(task.getResult().getEmail());
                prefManager.setEmail(task.getResult().getEmail());
                loginData.setPassword(task.getResult().getId());
                loginData.setDevice_info(deviceInfo);

                String request = new Gson().toJson(loginData);
                Log.i(TAG, "onCreate: " + request);
                GsonRequest<Userlogin> gsonRequest = new GsonRequest<Userlogin>(Request.Method.POST, URL_LOGIN, request,
                        Userlogin.class, null, response -> {
                    Log.i(TAG, "onResponse:_message" + response.getMessage());
                    Log.i(TAG, "onResponse:_code" + response.getCode());
                    volleyManager.setToken(task.getResult().getIdToken());
                    Log.i(TAG, "onResponse: Token" + volleyManager.getToken());
                    volleyManager.setLoginStatus(true);
                    showProgress();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        showSnackbar(error.toString());

                    }
                });
                gsonRequest.setShouldCache(true);
                MyVolley.getInstance(getApplicationContext()).addToRequestQueue(gsonRequest);*/
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                prefManager.setLoginStatus(true);
            }
        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        moveTaskToBack(true);
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}