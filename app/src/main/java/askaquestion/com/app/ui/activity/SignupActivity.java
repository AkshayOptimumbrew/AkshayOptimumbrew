package askaquestion.com.app.ui.activity;

import static askaquestion.com.app.core.constants.ConstantsAPI.URL_OTP;
import static askaquestion.com.app.core.constants.ConstantsAPI.URL_SIGNUP;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.kbeanie.multipicker.api.CameraImagePicker;
import com.kbeanie.multipicker.api.ImagePicker;
import com.kbeanie.multipicker.api.Picker;
import com.kbeanie.multipicker.api.callbacks.ImagePickerCallback;
import com.kbeanie.multipicker.api.entity.ChosenImage;
import com.optimumbrew.library.core.volley.GsonRequest;
import com.optimumbrew.library.core.volley.MyVolley;
import com.optimumbrew.library.core.volley.PhotoMultipartRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import askaquestion.com.app.R;
import askaquestion.com.app.core.interneconnection.DetectConnection;
import askaquestion.com.app.core.sharedprefs.PrefManager;
import askaquestion.com.app.core.sharedprefs.SharedManagerForVolley;
import askaquestion.com.app.databinding.ActivitySignupBinding;
import askaquestion.com.app.core.pojo.json.Country;
import askaquestion.com.app.core.pojo.registerapi.RegistrationResponseData;
import askaquestion.com.app.core.pojo.registerapi.RequestData;
import askaquestion.com.app.core.pojo.registerapi.RootSignup;
import askaquestion.com.app.core.pojo.verifyuser.DeviceInfosignup;
import askaquestion.com.app.core.pojo.verifyuser.RootVerify;
import askaquestion.com.app.core.pojo.verifyuser.Rootuser;
import askaquestion.com.app.databinding.DialogImagePickerBinding;
import askaquestion.com.app.databinding.DialogOtpBinding;

public class SignupActivity extends AppCompatActivity {

    ActivitySignupBinding binding;
    String TAG = "SignupActivity";
    String country;
    PrefManager prefManager;
    String imagePath, path;
    long imageSize;
    CameraImagePicker cameraImagePicker;
    ImagePicker imagePicker;
    String udid;
    Typeface typeface;
    ProgressDialog progressDialog;
    SharedManagerForVolley managerForVolley;
    ArrayList<Country> countryList;
    ArrayAdapter<Country> countryArrayAdapter;
    private final static String request_image_tag = "profile_img";
    private final static String request_json_tag = "request_data";

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint({"WrongConstant", "HardwareIds"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (!DetectConnection.checkInternetConnection(getApplicationContext())) {
            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
        typeface = ResourcesCompat.getFont(getApplicationContext(), R.font.sf_atarian_system_extended);


        udid = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        progressDialog = new ProgressDialog(SignupActivity.this,R.style.ProgressDialog);
        prefManager = new PrefManager(getApplicationContext());
        managerForVolley = new SharedManagerForVolley(getApplicationContext());
        binding.imageBack.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), SigninActivity.class)));
        binding.imageProfile.setOnClickListener(v -> {
            openDialogForAddImage();
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
        binding.imageShowPassBtn2.setOnTouchListener((v, event) -> {

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    binding.editConfirmPassword.setInputType(InputType.TYPE_CLASS_TEXT);
                    binding.editConfirmPassword.setTypeface(typeface);
                    break;
                case MotionEvent.ACTION_UP:
                    binding.editConfirmPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    binding.editConfirmPassword.setTypeface(typeface);
                    break;
            }
            return true;
        });
        binding.buttonSignup.setOnClickListener(v -> {
            String name = binding.editName.getText().toString().trim();
            String email = binding.editEmail.getText().toString().trim();
            String password = binding.editPassword.getText().toString().trim();
            String confirmPassword = binding.editConfirmPassword.getText().toString().trim();

            Pattern p_Email = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
            String pass = "^(?=.*[0-9])"
                    + "(?=.*[a-z])(?=.*[A-Z])"
                    + "(?=.*[@#$%^&+=])"
                    + "(?=\\S+$).{8,20}$";
            Pattern p = Pattern.compile(pass);
            Matcher m = p.matcher(password);
            Matcher m_Email = p_Email.matcher(email);

            if (name.isEmpty()) {
                binding.editName.setError("Enter Your Name");
                binding.editName.requestFocus();
            } else if (email.isEmpty()) {
                binding.editEmail.setError("Enter Your Email");
                binding.editEmail.requestFocus();
            } else if (!m_Email.matches()) {
                binding.editEmail.setError("Please Enter Valid Email");
                binding.editEmail.requestFocus();
            } else if (country == null) {
                Toast.makeText(getApplicationContext(), "Please Select Country", Toast.LENGTH_SHORT).show();
            } else if (password.isEmpty()) {
                binding.editPassword.setError("Please Enter Password");
                binding.editPassword.requestFocus();
            } else if (!m.matches()) {
                showSnackbar("Password Must be At Least 8 Character");
                binding.editPassword.requestFocus();
            } else if (confirmPassword.isEmpty()) {
                showSnackbar("Please Enter Password");
            } else if (!password.equals(confirmPassword)) {
                showSnackbar("Password Not Match");
                binding.editConfirmPassword.requestFocus();
            } else if (country == null) {
                showSnackbar("Select Country");
            } else if (gender() == 0) {
                showSnackbar("Select Gender");
            } else if (path == null) {
                showSnackbar("Please Select Image");
            } else if (!DetectConnection.checkInternetConnection(getApplicationContext())) {
                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
            } else {
//                prefManager.setEmail(email);
                prefManager.setPassword(password);
                prefManager.setLoginStatus(false);
                showProgress();
                managerForVolley.setImagePath(path);
                managerForVolley.setPassword(password);
                registrationApi(name, email, password, country, gender());
            }
        });
        initSpinner();
    }

    private void registrationApi(String name, String email, String password, String cntry, int gender) {

        MyVolley.getInstance(getApplicationContext());
        RequestData request_data = new RequestData();
        request_data.setCountry(cntry);
        request_data.setEmail_id(email);
        request_data.setGender(gender);
        request_data.setPassword(password);
        request_data.setFirst_name(name);
        request_data.setLast_name("");

        RegistrationResponseData responseData = new RegistrationResponseData();
        responseData.setRequest_data(request_data);
        String request = new Gson().toJson(responseData.getRequest_data());
        Log.i(TAG, "onResponse:_gson" + request);

        PhotoMultipartRequest<RootSignup> photoMultipartRequest = new PhotoMultipartRequest<RootSignup>
                (URL_SIGNUP,
                        request_image_tag,
                        new File(path),
                        request_json_tag,
                        request,
                        RootSignup.class,
                        (Map<String, String>) null,
                        response -> {
                            if (response != null && response.getCode() == 200) {
                                Log.i(TAG, "onResponse_id:" + response.getData().getUser_reg_temp_id());
                                prefManager.setTempid(response.getData().getUser_reg_temp_id());
                                managerForVolley.setEmail(request_data.getEmail_id());
                                openDialogForOTP();
                                Log.i(TAG, "onResponse_data: " + response.getMessage());
                            }

                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        SignupActivity.this.showSnackbar(error.toString());
                        progressDialog.dismiss();
                        if(error instanceof TimeoutError){
                            showSnackbar("Server Not Response");
                        }
                /*if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Log.i(TAG, "onErrorResponse: " + "TimeoutError || NoConnectionError");
                    SignupActivity.this.showSnackbar("NoConnection");
                    progressDialog.dismiss();
                } else if (error instanceof AuthFailureError) {
                    Log.i(TAG, "onErrorResponse: " + "AuthFailureError");
                    SignupActivity.this.showSnackbar("AuthFailureError");
                    progressDialog.dismiss();
                } else if (error instanceof ServerError) {
                    Log.i(TAG, "onErrorResponse: " + "ServerError");
                    SignupActivity.this.showSnackbar("ServerError");
                    progressDialog.dismiss();
                } else if (error instanceof NetworkError) {
                    Log.i(TAG, "onErrorResponse: " + "NetworkError");
                    SignupActivity.this.showSnackbar("NetworkError");
                    progressDialog.dismiss();
                } else if (error instanceof ParseError) {
                    Log.i(TAG, "onErrorResponse: " + "ParseError");
                    SignupActivity.this.showSnackbar("ParseError");
                    progressDialog.dismiss();
                } else if (error instanceof CustomError) {
                    SignupActivity.this.showSnackbar("Email Already Exist");
                    progressDialog.dismiss();
                }*/
                    }
                });
        photoMultipartRequest.setShouldCache(true);
        MyVolley.getInstance(getApplicationContext()).addToRequestQueue(photoMultipartRequest);
    }

    private void openDialogForOTP() {
        progressDialog.cancel();
        AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
        DialogOtpBinding bind = DialogOtpBinding.inflate(getLayoutInflater());
        builder.setView(bind.getRoot());

        AlertDialog dialog = builder.create();
        dialog.show();

        bind.buttonVerify.setOnClickListener(v -> {
            String o1 = bind.editOTP.getText().toString();

            int otp = Integer.parseInt(o1);

            Rootuser rootuser = new Rootuser();

            DeviceInfosignup deviceInfo = new DeviceInfosignup();
            deviceInfo.setDevice_udid(udid);

            rootuser.setDevice_info(deviceInfo);
            rootuser.setUser_reg_temp_id(prefManager.getTempid());
            rootuser.setToken(otp);

            String body = new Gson().toJson(rootuser);
            Log.i(TAG, "onCreate:_body " + body);
            GsonRequest<RootVerify> gsonRequest = new GsonRequest<RootVerify>(1,
                    URL_OTP,
                    body,
                    RootVerify.class,
                    null,
                    response -> {
                        Log.i(TAG, "onResponse: " + response.getMessage());
                        if (response.getMessage().equals("Login successfully.")) {
                            managerForVolley.setLoginStatus(false);
                            progressDialog.cancel();
                            startActivity(new Intent(getApplicationContext(), SigninActivity.class));
                            dialog.dismiss();
                            finish();
                        }

                        Log.i(TAG, "onResponse: " + response.getCode());
                    }, error -> {
                Log.i(TAG, "onErrorResponse: " + error);
                Toast.makeText(SignupActivity.this, "Invalid OTP", Toast.LENGTH_SHORT).show();
            });
            MyVolley.getInstance(getApplicationContext()).addToRequestQueue(gsonRequest);
        });

    }
    /*---------------------------------------------------------------------------------------------------------------*/

    public int gender() {
        if (binding.radiobuttonMale.isChecked()) {
            return 1;
        } else if (binding.radiobuttonFemale.isChecked()) {
            return 2;
        } else {
            return 0;
        }
    }
    /*---------------------------------------------------------------------------------------------------------------*/

    public void showProgress() {

        progressDialog.setMessage("Loading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        progressDialog.setCancelable(true);
    }
    /*---------------------------------------------------------------------------------------------------------------*/

    private void initSpinner() {
        createList();
        countryArrayAdapter = new ArrayAdapter<>(SignupActivity.this, R.layout.spinner_country_list, countryList);
        countryArrayAdapter.setDropDownViewResource(R.layout.spinner_country_list);
        binding.spinner.setAdapter(countryArrayAdapter);

        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String str = parent.getItemAtPosition(position).toString();
                country = str;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
    /*---------------------------------------------------------------------------------------------------------------*/

    private void createList() {
        countryList = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(loadJSONFromAsset());
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                countryList.add(new Country(jsonObject.getString("country")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    /*---------------------------------------------------------------------------------------------------------------*/

    public String loadJSONFromAsset() {
        String json;
        try {
            InputStream is = getAssets().open("country.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
    /*---------------------------------------------------------------------------------------------------------------*/

    private void showSnackbar(String str) {
        Snackbar snackbar = Snackbar.make(binding.getRoot(), str, BaseTransientBottomBar.LENGTH_SHORT);
        snackbar.setAction("Dismiss", v -> Toast.makeText(getApplicationContext(), "Dismiss", Toast.LENGTH_SHORT).show()).show();
    }
    /*---------------------------------------------------------------------------------------------------------------*/

    private void openDialogForAddImage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
        DialogImagePickerBinding bind = DialogImagePickerBinding.inflate(getLayoutInflater());
        builder.setView(bind.getRoot());

        AlertDialog dialog = builder.create();
        dialog.show();

        bind.textCamera.setOnClickListener(v -> {
            openCameraPermission();
            binding.progress.setVisibility(View.VISIBLE);
            dialog.dismiss();
        });
        bind.textGallery.setOnClickListener(v -> {
            openGalleryPermission();
            binding.progress.setVisibility(View.VISIBLE);
            dialog.dismiss();
        });
    }

    /*---------------------------------------------------------------------------------------------------------------*/

    private void openGallery() {

        imagePicker = new ImagePicker(SignupActivity.this);
        imagePicker.setImagePickerCallback(new ImagePickerCallback() {
            @Override
            public void onImagesChosen(List<ChosenImage> list) {
                for (ChosenImage chosenImage : list) {
                    chosenImage.getSize();
                    Log.i(TAG, "onImagesChosen: Size::" + chosenImage.getSize());
                    imagePath = chosenImage.getOriginalPath();
                    imageSize = chosenImage.getSize();
                    System.out.println("Gall:" + chosenImage.getOriginalPath());
                    path = imagePath;
                    if (imageSize > 10240000) {
                        showSnackbar("Images File cannot be larger than 10 MB");
                        binding.progress.setVisibility(View.GONE);
                    } else {
                        if (path != null) {
                            Glide.with(getApplicationContext()).load(imagePath).listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    binding.progress.setVisibility(View.GONE);
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    binding.progress.setVisibility(View.GONE);
                                    return false;
                                }
                            }).into(binding.imageProfile);
                        }
                    }


                }

            }

            @Override
            public void onError(String s) {

            }
        });
        imagePicker.pickImage();
    }
    /*---------------------------------------------------------------------------------------------------------------*/

    private void openCamera() {
        cameraImagePicker = new CameraImagePicker(SignupActivity.this);
        cameraImagePicker.setImagePickerCallback(new ImagePickerCallback() {
            @Override
            public void onImagesChosen(List<ChosenImage> list) {
                for (ChosenImage image : list) {

                    path = image.getThumbnailPath();
                    imageSize = image.getSize();


                    System.out.println("ssss:" + path);
                    if (imageSize > 10240000) {
                        showSnackbar("Images File cannot be larger than 10 MB");
                        binding.progress.setVisibility(View.GONE);
                    } else {
                        if (path != null) {
                            Glide.with(getApplicationContext()).load(path).listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    binding.progress.setVisibility(View.GONE);
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    binding.progress.setVisibility(View.GONE);
                                    return false;
                                }
                            }).into(binding.imageProfile);
                        }
                    }
                }
            }

            @Override
            public void onError(String s) {

            }
        });
        path = cameraImagePicker.pickImage();
    }
    /*---------------------------------------------------------------------------------------------------------------*/

    private void openCameraPermission() {
        Dexter.withActivity(SignupActivity.this)
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        openCamera();
                    }


                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        if (response.isPermanentlyDenied()) {
                            openDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }
    /*---------------------------------------------------------------------------------------------------------------*/

    private void openGalleryPermission() {
        Dexter.withActivity(SignupActivity.this)
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        openGallery();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        if (response.isPermanentlyDenied()) {
                            openDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();

    }
    /*---------------------------------------------------------------------------------------------------------------*/

    private void openDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this)
                .setTitle("Need Permission")
                .setMessage("This App Needs Permission To Use This Features.You Can Grant Them in App Setting.")
                .setPositiveButton("GOTO SETTINGS", (dialog, which) -> {
                    dialog.cancel();
                    openPermissionSetting();
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }
    /*---------------------------------------------------------------------------------------------------------------*/

    private void openPermissionSetting() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_SETTINGS);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.i(TAG, "onActivityResult: ");
        if (resultCode == RESULT_OK) {
            if (requestCode == Picker.PICK_IMAGE_CAMERA) {
                cameraImagePicker.submit(data);
            }
            if (requestCode == Picker.PICK_IMAGE_DEVICE) {
                imagePicker.submit(data);
            }
        } else {
            if (data == null) {
                progressDialog.cancel();
                Log.i(TAG, "onActivityResult: null");
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        progressDialog.cancel();
    }
}