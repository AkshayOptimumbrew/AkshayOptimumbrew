package askaquestion.com.app.ui.fragments;

import static android.app.Activity.RESULT_OK;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import android.provider.Settings;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import askaquestion.com.app.R;
import askaquestion.com.app.core.interneconnection.DetectConnection;
import askaquestion.com.app.core.sharedprefs.PrefManager;
import askaquestion.com.app.core.sharedprefs.SharedManagerForVolley;
import askaquestion.com.app.databinding.DialogImagePickerBinding;
import askaquestion.com.app.databinding.DialogLogoutBinding;
import askaquestion.com.app.databinding.DialogRatingBinding;
import askaquestion.com.app.databinding.DialogUpdateProfileBinding;
import askaquestion.com.app.ui.activity.SigninActivity;
import askaquestion.com.app.ui.activity.SlideWithWebViewActivity;
import askaquestion.com.app.core.constants.ConstantsAPI;
import askaquestion.com.app.databinding.FragmentSettingBinding;
import askaquestion.com.app.core.pojo.json.Category;
import askaquestion.com.app.core.pojo.resetpasswordforuser.RequestReset;
import askaquestion.com.app.core.pojo.resetpasswordforuser.RequestResponse;
import askaquestion.com.app.core.pojo.updateprofile.RequestProfile;
import askaquestion.com.app.core.pojo.updateprofile.RootUpdateProfile;
import askaquestion.com.app.core.pojo.viewcategories.ResponseOfViewCategories;
import askaquestion.com.app.core.pojo.viewcategories.ResultCategories;


public class SettingFragment extends Fragment {

    FragmentSettingBinding binding;
    int c = 0;
    String TAG = "SettingFragment";
    SharedManagerForVolley managerForVolley;
    ArrayList<Category> categoryList;
    ArrayAdapter<Category> categoryArrayAdapter;
    String category;
    String imagePath, path;
    AlertDialog dialog2;
    PrefManager prefManager;
    CameraImagePicker cameraImagePicker;
    ImagePicker imagePicker;
    Typeface typeface;
    private String request_image_tag = "profile_img";
    private String request_json_tag = "name";
    ProgressDialog progressDialog;
    private RewardedVideoAd AdMobrewardedVideoAd;
    private String AdId = "ca-app-pub-3940256099942544/5224354917";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSettingBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        managerForVolley = new SharedManagerForVolley(getActivity());
        progressDialog = new ProgressDialog(getActivity(), R.style.ProgressDialog);
        prefManager = new PrefManager(getActivity());

        MobileAds.initialize(getActivity());
        typeface = ResourcesCompat.getFont(getActivity(), R.font.sf_atarian_system_extended);

        binding.imageLogout.setOnClickListener(v -> openDialogForLogout());
        binding.imageProfile.setOnClickListener(v -> openDialogForAddImage());

        binding.buttonSave.setOnClickListener(v -> {
            if (binding.editFullName.getText().toString().isEmpty()) {
                showSnackbar("Please Enter Name");
            } else if (imagePath == null) {
                showSnackbar("Please Select Image");
            } else if (new File(imagePath).length() > 2024000) {
                showSnackbar("Images File cannot be larger than 2 MB");
            } else {
                openDialogForAskUserToUpdateProfile();
            }
        });
        binding.buttonSubmit.setOnClickListener(v -> resetPassword());
        binding.buttonRating.setOnClickListener(v -> {
            openDialogForRating();
        });

        binding.buttonViewVideo.setOnClickListener(v -> {
//            showRewardedVideoAd();
            loadRewardedVideoAd();

        });

        binding.buttonPrivacy.setOnClickListener(v -> {
            if (!DetectConnection.checkInternetConnection(getActivity())) {
                Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
            } else {
                showProgress();
                startActivity(new Intent(getActivity(), SlideWithWebViewActivity.class));
                progressDialog.cancel();
            }


        });
        binding.buttonTerms.setOnClickListener(v -> {
            if (!DetectConnection.checkInternetConnection(getActivity())) {
                Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
            } else {
                showProgress();
                startActivity(new Intent(getActivity(), SlideWithWebViewActivity.class));
                progressDialog.cancel();
            }
        });
        binding.imageShowPassBtn.setOnTouchListener((v, event) -> {

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    binding.editOldPassword.setInputType(InputType.TYPE_CLASS_TEXT);
                    binding.editOldPassword.setTypeface(typeface);
                    break;
                case MotionEvent.ACTION_UP:
                    binding.editOldPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    binding.editOldPassword.setTypeface(typeface);
                    break;
            }
            return true;
        });
        binding.imageShowPassBtn1.setOnTouchListener((v, event) -> {

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    binding.editNewPassword.setInputType(InputType.TYPE_CLASS_TEXT);
                    binding.editNewPassword.setTypeface(typeface);
                    break;
                case MotionEvent.ACTION_UP:
                    binding.editNewPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    binding.editNewPassword.setTypeface(typeface);
                    break;
            }
            return true;
        });
        binding.imageShowPassBtn2.setOnTouchListener((v, event) -> {

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    binding.editConfirmNewPassword.setInputType(InputType.TYPE_CLASS_TEXT);
                    binding.editConfirmNewPassword.setTypeface(typeface);
                    break;
                case MotionEvent.ACTION_UP:
                    binding.editConfirmNewPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    binding.editConfirmNewPassword.setTypeface(typeface);
                    break;
            }
            return true;
        });
        loadCategoriesFromAPI();
        initSpinner();
    }

    private void loadRewardedVideoAd() {
        if (!DetectConnection.checkInternetConnection(getActivity())) {
            Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        } else {
            showProgress();
            AdMobrewardedVideoAd = MobileAds.getRewardedVideoAdInstance(getActivity());

            AdMobrewardedVideoAd.setRewardedVideoAdListener(
                    new RewardedVideoAdListener() {
                        @Override
                        public void onRewardedVideoAdLoaded() {
                            AdMobrewardedVideoAd.show();
                            Log.i(TAG, "onRewardedVideoAdLoaded: ");
                        }

                        @Override
                        public void onRewardedVideoAdOpened() {
                            Log.i(TAG, "onRewardedVideoAdOpened: ");
                        }

                        @Override
                        public void onRewardedVideoStarted() {
                            Log.i(TAG, "onRewardedVideoStarted: ");
                        }

                        @Override
                        public void onRewardedVideoAdClosed() {
                            progressDialog.cancel();
                            Log.i(TAG, "onRewardedVideoAdClosed: ");
                        }

                        @Override
                        public void onRewarded(RewardItem rewardItem) {
                            progressDialog.cancel();
                            Toast.makeText(getActivity(), "You Got 10 Coins", Toast.LENGTH_SHORT).show();
                            c = c + 10;
                            binding.textVideoAdsCoin.setText(new StringBuilder().append(c).append(" COINS"));
                            Log.i(TAG, "onRewarded: ");
                        }

                        @Override
                        public void
                        onRewardedVideoAdLeftApplication() {
                            Log.i(TAG, "onRewardedVideoAdLeftApplication: ");
                        }

                        @Override
                        public void onRewardedVideoAdFailedToLoad(int i) {
                            Log.i(TAG, "onRewardedVideoAdFailedToLoad: ");
                        }

                        @Override
                        public void onRewardedVideoCompleted() {
                            Log.i(TAG, "onRewardedVideoCompleted: ");
                        }
                    });

            AdMobrewardedVideoAd.loadAd(AdId, new AdRequest.Builder().build());
        }

    }

    private void showRewardedVideoAd() {
        if (AdMobrewardedVideoAd.isLoaded()) {
            AdMobrewardedVideoAd.show();
        } else {
            AdMobrewardedVideoAd.loadAd(AdId, new AdRequest.Builder().build());
        }
    }

    private void openDialogForLogout() {
        final Dialog dialog = new Dialog(getActivity());
        DialogLogoutBinding binding = DialogLogoutBinding.inflate(getLayoutInflater());
        dialog.setContentView(binding.getRoot());
        dialog.getWindow().setLayout(-1, -2);
        dialog.show();

        binding.textNo.setOnClickListener(v -> dialog.dismiss());
        binding.textYes.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), SigninActivity.class));
            prefManager.setLoginStatus(false);
            managerForVolley.setLoginStatus(false);
        });
    }

    private void openDialogForRating() {
        DialogRatingBinding ratingBinding = DialogRatingBinding.inflate(getLayoutInflater());
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(ratingBinding.getRoot());
        AlertDialog dialog = builder.create();
        dialog2 = dialog;
        dialog.show();

        ratingBinding.ratingBar.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> {
            Toast.makeText(getActivity(), "Rating : " + rating, Toast.LENGTH_SHORT).show();
            Intent launchIntent = getActivity().getPackageManager().getLaunchIntentForPackage("com.android.vending");
            startActivity(launchIntent);
            dialog.dismiss();
        });
        ratingBinding.textNotnow.setOnClickListener(v -> dialog.dismiss());
    }

    private void resetPassword() {
        String oldPassword = binding.editOldPassword.getText().toString().trim();
        String newPassword = binding.editNewPassword.getText().toString().trim();
        String confirmNewPassword = binding.editConfirmNewPassword.getText().toString().trim();

        String pass = "^(?=.*[0-9])"
                + "(?=.*[a-z])(?=.*[A-Z])"
                + "(?=.*[@#$%^&+=])"
                + "(?=\\S+$).{8,20}$";
        Pattern p = Pattern.compile(pass);
        Matcher m = p.matcher(newPassword);

        if (oldPassword.isEmpty()) {
            binding.editOldPassword.setError("Please Enter Password");
        } else if (newPassword.isEmpty()) {
            binding.editNewPassword.setError("Please Enter Password");
        } else if (oldPassword.equals(newPassword)) {
            binding.editOldPassword.setError("Please Choose Different Password");
        } else if (!m.matches()) {
            binding.editNewPassword.setError("Password Must be At Least 8 Character");
        } else if (confirmNewPassword.isEmpty()) {
            binding.editConfirmNewPassword.setError("Please Enter Password");
        } else if (!newPassword.equals(confirmNewPassword)) {
            Toast.makeText(getActivity(), "Password Not Match", Toast.LENGTH_SHORT).show();
        } else {

            RequestReset requestReset = new RequestReset();
            requestReset.setEmail_id(prefManager.getEmail());
            requestReset.setOld_password(oldPassword);
            requestReset.setNew_password(newPassword);

            String body = new Gson().toJson(requestReset);
            Log.i(TAG, "resetPassword: " + body);

            Map<String, String> headers = new HashMap<>();
            headers.put("Authorization", "Bearer " + managerForVolley.getToken());

            GsonRequest<RequestResponse> gsonRequest = new GsonRequest<RequestResponse>(1,
                    ConstantsAPI.URL_RESET_PASSWORD,
                    body,
                    RequestResponse.class,
                    headers,
                    response -> {
                        showSnackbar("Reset Successfully");
                        Log.i(TAG, "onResponse: " + response.getMessage());
                    },
                    error -> {
                        showSnackbar(error.toString());
                    });
            MyVolley.getInstance(getActivity()).addToRequestQueue(gsonRequest);
            binding.editOldPassword.setText("");
            binding.editConfirmNewPassword.setText("");
            binding.editNewPassword.setText("");
        }
    }

    private void openDialogForAskUserToUpdateProfile() {
        DialogUpdateProfileBinding bind = DialogUpdateProfileBinding.inflate(getLayoutInflater());
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(bind.getRoot());
        AlertDialog dialog = builder.create();
        dialog.show();

        bind.textYes.setOnClickListener(v -> {
            editProfileForUser();
            dialog.dismiss();
        });
        bind.textNo.setOnClickListener(v -> dialog.dismiss());
    }

    private void editProfileForUser() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + managerForVolley.getToken());

        RequestProfile requestProfile = new RequestProfile();
        requestProfile.setName(binding.editFullName.getText().toString().trim());
        if (path != null) {
            Log.i(TAG, "editProfileForUser: " + path);
            requestProfile.setProfile_img(path);
        }
        String body = new Gson().toJson(requestProfile);
        Log.i(TAG, "editProfileForUser:body " + body);
        PhotoMultipartRequest<RootUpdateProfile> photoMultipartRequest = new PhotoMultipartRequest<>(
                ConstantsAPI.URL_EDIT_PROFILE,
                request_image_tag,
                new File(path),
                request_json_tag,
                body,
                RootUpdateProfile.class,
                headers,
                new Response.Listener<RootUpdateProfile>() {
                    @Override
                    public void onResponse(RootUpdateProfile response) {
                        if (response.getCode() == 200 && response != null) {
                            SettingFragment.this.showSnackbar("Profile Updated Successfully");
                            Log.i(TAG, "onResponse: " + response.getMessage());
                            binding.editFullName.setText("");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.cancel();
                SettingFragment.this.showSnackbar(error.toString());
            }
        });

        MyVolley.getInstance(getActivity()).addToRequestQueue(photoMultipartRequest);
    }

    private void showSnackbar(String str) {
        Snackbar snackbar = Snackbar.make(binding.getRoot(), str, BaseTransientBottomBar.LENGTH_SHORT);
        snackbar.show();
    }

    private void loadCategoriesFromAPI() {
        GsonRequest<ResponseOfViewCategories> gsonRequest = new GsonRequest<>(1,
                ConstantsAPI.URL_CATEGORIES,
                "{}",
                ResponseOfViewCategories.class,
                null,
                response -> {
                    ArrayList<ResultCategories> resultCategoriesArrayList = response.getData().getResult();
                    spinnerList(resultCategoriesArrayList);
                }, error -> {

        });
        MyVolley.getInstance(getActivity()).addToRequestQueue(gsonRequest);
    }

    public void showProgress() {
        progressDialog.setMessage("Please Wait...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        progressDialog.setCancelable(true);
    }

    private void spinnerList(ArrayList<ResultCategories> resultCategoriesArrayList) {
        ArrayAdapter<ResultCategories> resultCategoriesArrayAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_category_list, resultCategoriesArrayList);
        resultCategoriesArrayAdapter.setDropDownViewResource(R.layout.spinner_category_list);
        binding.spinnerCategory.setAdapter(resultCategoriesArrayAdapter);

        binding.spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String str = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initSpinner() {
        createList();
        categoryArrayAdapter = new ArrayAdapter<Category>(getActivity(), R.layout.spinner_category_list, categoryList);
        categoryArrayAdapter.setDropDownViewResource(R.layout.spinner_category_list);
        binding.spinnerCategory.setAdapter(categoryArrayAdapter);

        binding.spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String str = parent.getItemAtPosition(position).toString();
                category = str;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void openDialogForAddImage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        DialogImagePickerBinding bind = DialogImagePickerBinding.inflate(getLayoutInflater());
        builder.setView(bind.getRoot());

        AlertDialog dialog = builder.create();
        dialog.show();

        bind.textCamera.setOnClickListener(v -> {
            openCameraPermission();
//            binding.progress.setVisibility(View.VISIBLE);
            dialog.dismiss();
        });
        bind.textGallery.setOnClickListener(v -> {
            openGalleryPermission();
//            binding.progress.setVisibility(View.VISIBLE);
            dialog.dismiss();
        });
    }

    private void createList() {
        categoryList = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(loadJSONFromAsset());
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                categoryList.add(new Category(jsonObject.getString("category")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String loadJSONFromAsset() {
        String json;
        try {
            InputStream is = getActivity().getAssets().open("category.json");
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

    private void openGallery() {
        showProgress();
        imagePicker = new ImagePicker(this);
        imagePicker.setImagePickerCallback(new ImagePickerCallback() {
            @Override
            public void onImagesChosen(List<ChosenImage> list) {
                for (ChosenImage chosenImage : list) {
                    imagePath = chosenImage.getOriginalPath();
                    System.out.println("Gall:" + chosenImage.getHeight());

                    path = imagePath;

                    if (path != null) {
                        Glide.with(getActivity()).load(imagePath).listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                binding.progress.setVisibility(View.GONE);
                                progressDialog.cancel();
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                binding.progress.setVisibility(View.GONE);
                                progressDialog.cancel();
                                return false;
                            }
                        }).into(binding.imageProfile);
                    } else {
                        Glide.with(getActivity()).load(managerForVolley.getImagePath()).listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                binding.progress.setVisibility(View.GONE);
                                progressDialog.cancel();
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                binding.progress.setVisibility(View.GONE);
                                progressDialog.cancel();
                                return false;
                            }
                        }).into(binding.imageProfile);
                    }
                }
            }

            @Override
            public void onError(String s) {

            }
        });
        imagePicker.pickImage();
    }

    private void openCamera() {
        cameraImagePicker = new CameraImagePicker(this);
        cameraImagePicker.setImagePickerCallback(new ImagePickerCallback() {
            @Override
            public void onImagesChosen(List<ChosenImage> list) {
                for (ChosenImage image : list) {
                    imagePath = image.getThumbnailPath();
                    System.out.println("ssss:" + imagePath);
                    if (imagePath != null) {
                        Glide.with(getActivity()).load(imagePath)/*.listener(new RequestListener<Drawable>() {
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
                        })*/.into(binding.imageProfile);
                    } else {
                        Glide.with(getActivity()).load(managerForVolley.getImagePath())/*.listener(new RequestListener<Drawable>() {
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
                        })*/.into(binding.imageProfile);
                    }
                }
            }

            @Override
            public void onError(String s) {

            }
        });
        path = cameraImagePicker.pickImage();
    }

    private void openCameraPermission() {
        Dexter.withActivity(getActivity())
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

    private void openGalleryPermission() {
        Dexter.withActivity(getActivity())
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

    private void openDialog() {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getActivity())
                .setTitle("Need Permission")
                .setMessage("This App Needs Permission To Use This Features.You Can Grant Them in App Setting.")
                .setPositiveButton("GOTO SETTINGS", (dialog, which) -> {
                    dialog.cancel();
                    openPermissionSetting();
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void openPermissionSetting() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_SETTINGS);
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
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
                binding.progress.setVisibility(View.GONE);
            }
        }
    }
}