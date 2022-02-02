package askaquestion.com.app.ui.fragments;

import static android.content.Context.MODE_PRIVATE;

import static askaquestion.com.app.core.constants.ConstantsAPI.URL_ADD_QUESTION;
import static askaquestion.com.app.core.constants.Extras.EXTRA_OPTION1;
import static askaquestion.com.app.core.constants.Extras.EXTRA_OPTION1_IMAGE;
import static askaquestion.com.app.core.constants.Extras.EXTRA_OPTION2;
import static askaquestion.com.app.core.constants.Extras.EXTRA_OPTION2_IMAGE;
import static askaquestion.com.app.core.constants.Extras.EXTRA_OPTION_TYPE;
import static askaquestion.com.app.core.constants.Extras.EXTRA_QUESTION;
import static askaquestion.com.app.core.constants.Extras.EXTRA_QUESTION_FOR_IMAGE;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import askaquestion.com.app.core.interfaces.ForCountry;
import askaquestion.com.app.core.pojo.imagethroughaddquestion.RequestImageDescription;
import askaquestion.com.app.core.pojo.imagethroughaddquestion.RequestImageOption;
import askaquestion.com.app.databinding.DialogCountryBinding;
import askaquestion.com.app.databinding.DialogImagePickerBinding;
import askaquestion.com.app.ui.activity.PreviewActivity;
import askaquestion.com.app.R;
import askaquestion.com.app.core.sharedprefs.SharedManagerForVolley;
import askaquestion.com.app.ui.adapters.CountryAdpater;
import askaquestion.com.app.core.constants.ConstantsAPI;
import askaquestion.com.app.databinding.FragmentAddQuestionBinding;
import askaquestion.com.app.core.pojo.adapter_model.CountryList;
import askaquestion.com.app.core.pojo.adapter_model.CountryName;
import askaquestion.com.app.core.pojo.json.Gender;
import askaquestion.com.app.core.pojo.addquestionimage.ResponseTextImage;
import askaquestion.com.app.core.pojo.addquestiontext.RequestDataMain;
import askaquestion.com.app.core.pojo.addquestiontext.RequestDataSub;
import askaquestion.com.app.core.pojo.uploadimage.ResponseUpload;
import askaquestion.com.app.core.pojo.viewcategories.ResponseOfViewCategories;
import askaquestion.com.app.core.pojo.viewcategories.ResultCategories;

public class AddQuestionFragment extends Fragment {

    FragmentAddQuestionBinding binding;
    String TAG = "AddQuestionFragment";
    Activity activity;
    String category;
    ArrayList<Gender> genderList;
    ArrayAdapter<Gender> genderArrayAdapter;
    String gender;
    String strName;
    public ForCountry onClickInterface;
    int option_type = 1;
    String imagePath, questionImage, imageOne, imageTwo, imagePath1, imagePath2;
    CameraImagePicker cameraImagePicker;
    ImagePicker imagePicker;
    long imageSize, questionImageSize;
    int count = 0;
    CountryAdpater countryAdpater;
    ArrayList<CountryName> countryNameList;
    ArrayList<CountryName> filterList;
    private static final String request_image_tag = "file";
    private static final String request_json_tag = "request_data";
    DialogCountryBinding bindi;
    ArrayList<CountryList> countryArrayList;
    ProgressDialog progressDialog;
    SharedManagerForVolley managerForVolley;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddQuestionBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Message", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        Log.i(TAG, "onViewCreated: ");
        managerForVolley = new SharedManagerForVolley(getActivity());
        progressDialog = new ProgressDialog(activity,R.style.ProgressDialog);

        onClickInterface = (v, countryName, string) -> {
            bindi.textMarquee.setVisibility(View.VISIBLE);
            bindi.textMarquee.setText(string);
            bindi.textMarquee.setSelected(true);
            if (string.isEmpty()) {
                bindi.textMarquee.setVisibility(View.GONE);
            }
            strName = string;
            char[] ch = new char[string.length()];
            count = 1;
            for (int i = 0; i < string.length() - 1; i++) {
                ch[i] = string.charAt(i);
                if (ch[i] == ',') {
                    count++;
                }
            }
        };
        loadCategoriesFromAPI();

        initSpinnerGender();
        createList();

        binding.editDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.textCount.setText(new StringBuilder().append(s.length()).append(" / 200"));
                if (s.length() > 150) {
                    binding.editDescription.setTextColor(getResources().getColor(R.color.red));
                } else {
                    binding.editDescription.setTextColor(getResources().getColor(R.color.white));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.radiobuttonImage.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                option_type = 2;
                binding.textLL.setVisibility(View.GONE);
                binding.imageRL.setVisibility(View.VISIBLE);
            } else {
                option_type = 1;
                binding.imageRL.setVisibility(View.GONE);
                binding.textLL.setVisibility(View.VISIBLE);
            }
            Log.i(TAG, "onViewCreated: " + option_type);
        });

        binding.rl8.setOnClickListener(v -> {
            showCountryListDialog(binding);
        });

        binding.imageCamera.setOnClickListener(v -> openDialogForAddImage());
        binding.imageviewOption1ViewQuestion.setOnClickListener(v1 -> openDialogForQuestionOneImage());
        binding.imageviewOption2ViewQuestion.setOnClickListener(v1 -> openDialogForQuestionTwoImage());
        binding.buttonSubmit.setOnClickListener(v -> {
            if (option_type == 1) {
                if (questionImage == null) {
                    Toast.makeText(getActivity(), "Select Image", Toast.LENGTH_SHORT).show();
                } else if (binding.editDescription.getText().toString().isEmpty()) {
                    binding.editDescription.setError("Please Enter Description");
                } else if (binding.textOption.getText().toString().isEmpty()) {
                    binding.editOption1.setError("Please Enter Option1");
                } else if (binding.editOption2.getText().toString().isEmpty()) {
                    binding.editOption2.setError("Please Enter Option2");
                } else if (genderType() == -1) {
                    Toast.makeText(getActivity(), "Please Select Gender", Toast.LENGTH_SHORT).show();
                } else if (categoryType() == -1) {
                    Toast.makeText(getActivity(), "Please Select Category", Toast.LENGTH_SHORT).show();
                } else {

                    RequestDataSub requestDataSub = new RequestDataSub();
                    requestDataSub.setCategoryId(categoryType());
                    requestDataSub.setDescription(binding.editDescription.getText().toString());
                    requestDataSub.setOption1(binding.editOption1.getText().toString());
                    requestDataSub.setOption2(binding.editOption2.getText().toString());
                    requestDataSub.setGender(genderType());
                    requestDataSub.setCountry(strName);
                    requestDataSub.setOptionType(1);

                    RequestDataMain requestDataMain = new RequestDataMain();
                    requestDataMain.setRequestData(requestDataSub);


                    File file = new File(questionImage);
                    Log.i(TAG, "onViewCreated: new File(questionImage)::" + file.exists());

                    Log.i(TAG, "onViewCreated: Image::::" + questionImage);
                    Log.i(TAG, "onViewCreated: option_type:::::" + option_type);

                    Map<String, String> headers = new HashMap<>();
                    headers.put("Authorization", "Bearer " + managerForVolley.getToken());
                    Log.i(TAG, "onViewCreated: token:::" + headers.toString());

                    Gson gson = new Gson();
                    String request = gson.toJson(requestDataMain.getRequestData());
                    Log.i(TAG, "onViewCreated: request::" + request);

                    PhotoMultipartRequest<ResponseTextImage> photoMultipartRequest = new PhotoMultipartRequest<ResponseTextImage>(
                            URL_ADD_QUESTION,
                            "question_image",
                            file,
                            "request_data",
                            request,
                            ResponseTextImage.class,
                            headers,
                            response -> {
                                if (response.getCode() == 200 && response != null) {
                                    Log.i(TAG, "onResponse: " + response.getMessage());
                                    showSnackbar(response.getMessage());
                                }

                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.i(TAG, "onViewCreated: " + error.toString());
                            AddQuestionFragment.this.showSnackbar(error.toString());
                            progressDialog.dismiss();
                        }
                    });
                    photoMultipartRequest.setShouldCache(true);
                    MyVolley.getInstance(getActivity()).addToRequestQueue(photoMultipartRequest);
                }
            }
            /*---------------------------------------------------------------------------------------------------------------*/
            if (option_type == 2) {
                if (imageOne == null) {
                    Toast.makeText(getActivity(), "Select Image", Toast.LENGTH_SHORT).show();
                } else if (binding.editDescription.getText().toString().isEmpty()) {
                    binding.editDescription.setError("Please Enter Description");
                } else if (imageOne == null) {
                    Toast.makeText(getActivity(), "Please Select Option One Image", Toast.LENGTH_SHORT).show();
                } else if (imageTwo == null) {
                    Toast.makeText(getActivity(), "Please Select Option Two Image", Toast.LENGTH_SHORT).show();
                } else if (genderType() == -1) {
                    Toast.makeText(getActivity(), "Please Select Gender", Toast.LENGTH_SHORT).show();
                } else if (categoryType() == -1) {
                    Toast.makeText(getActivity(), "Please Select Category", Toast.LENGTH_SHORT).show();
                } else {

                    RequestImageDescription requestImageDescription = new RequestImageDescription();
                    requestImageDescription.setDescription(binding.editDescription.getText().toString());
                    requestImageDescription.setCountry(strName);
                    requestImageDescription.setCategory_id(categoryType());
                    requestImageDescription.setOption_type(2);
                    requestImageDescription.setGender(genderType());


                    Log.i(TAG, "onViewCreated: 1::" + imageOne);
                    Log.i(TAG, "onViewCreated: 2::" + imageTwo);

                    File file1 = new File(imageOne);
                    File file2 = new File(imageTwo);

                    RequestImageOption requestImageOption = new RequestImageOption();
                    requestImageOption.setOption1(imageOne);
                    requestImageOption.setOption2(imageTwo);
                    requestImageOption.setRequest_data(requestImageDescription);
                    requestImageOption.setQuestion_image(questionImage);

                    String request = new Gson().toJson(requestImageOption.getRequest_data());
                    Log.i(TAG, "onViewCreated request: " + request);


                    Map<String, String> headers = new HashMap<>();
                    headers.put("Authorization", "Bearer " + managerForVolley.getToken());

                    JSONObject jsonObject;

                    jsonObject = new JSONObject();
                    try {
                        jsonObject.put("request_data", request);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(1, URL_ADD_QUESTION, jsonObject, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.i(TAG, "onResponse: " + response);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.i(TAG, "onErrorResponse: " + error);
                        }
                    }) {
                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            Map<String, String> headers = new HashMap<>();
                            headers.put("Authorization", "Bearer " + managerForVolley.getToken());
                            return headers;
                        }

                        @Nullable
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            params.put("request_data", request);
                            params.put("option1", imageOne);
                            params.put("option2", imageTwo);
                            return params;
                        }
                    };
                    Volley.newRequestQueue(getActivity()).add(jsonObjectRequest);

                }
            }
        });
        binding.buttonPreview.setOnClickListener(v -> {
            if (questionImage == null) {
                Toast.makeText(getActivity(), "Select Image", Toast.LENGTH_SHORT).show();
            } else if (binding.editDescription.getText().toString().isEmpty()) {
                binding.editDescription.setError("Please Enter Description");
            } else if (binding.textOption.getText().toString().isEmpty()) {
                binding.editOption1.setError("Please Enter Option1");
            } else if (binding.editOption2.getText().toString().isEmpty()) {
                binding.editOption2.setError("Please Enter Option2");
            } else if (genderType() == -1) {
                Toast.makeText(getActivity(), "Please Select Gender", Toast.LENGTH_SHORT).show();
            } else if (categoryType() == -1) {
                Toast.makeText(getActivity(), "Please Select Category", Toast.LENGTH_SHORT).show();
            } else {
                if (option_type == 1) {
                    Intent intent = new Intent(getActivity(), PreviewActivity.class);
                    intent.putExtra(EXTRA_OPTION_TYPE, option_type);
                    intent.putExtra(EXTRA_QUESTION, binding.editDescription.getText().toString());
                    intent.putExtra(EXTRA_OPTION1, binding.editOption1.getText().toString());
                    intent.putExtra(EXTRA_OPTION2, binding.editOption2.getText().toString());
                    Log.i(TAG, "onViewCreated:ddddddd " + binding.editDescription.getText().toString());
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getActivity(), PreviewActivity.class);
                    intent.putExtra(EXTRA_OPTION_TYPE, option_type);
                    intent.putExtra(EXTRA_QUESTION_FOR_IMAGE, binding.editDescription.getText().toString());
                    intent.putExtra(EXTRA_OPTION1_IMAGE, imageOne);
                    intent.putExtra(EXTRA_OPTION2_IMAGE, imageTwo);
                    startActivity(intent);
                }
            }

        });
    }

    /*---------------------------------------------------------------------------------------------------------------*/

    private void uploadImageApi(String questionImage) {
       /* File newFile = new Compressor.Builder(getActivity())
                .setMaxWidth(200)
                .setMaxHeight(200)
                .setQuality(50)
                .setCompressFormat(Bitmap.CompressFormat.JPEG)
                .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES).getAbsolutePath())
                .build()
                .compressToFile(file);
        Log.i(TAG, "uploadImageApi: file : " + file.exists());*/

        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + managerForVolley.getToken());
        PhotoMultipartRequest<ResponseUpload> photoMultipartRequest = new PhotoMultipartRequest<>(
                ConstantsAPI.URL_UPLOAD_IMAGE,
                request_image_tag,
                new File(questionImage),
                request_json_tag,
                questionImage,
                ResponseUpload.class,
                headers,
                response -> {
                    Log.i(TAG, "onResponse: " + response.getMessage());
                    Glide.with(getActivity()).load(questionImage).addListener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            binding.imageCamera.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            binding.imageCamera.setVisibility(View.GONE);
                            return false;
                        }
                    }).into(binding.imageBackground);

                    progressDialog.cancel();
                    showSnackbar(response.getMessage());
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i(TAG, "uploadImageApi: Error:::" + error);
                        progressDialog.cancel();
                    }
                });
        MyVolley.getInstance(getActivity()).addToRequestQueue(photoMultipartRequest);

    }
    /*---------------------------------------------------------------------------------------------------------------*/

    public int genderType() {
        if (gender.equals("Female")) {
            return 1;
        } else if (gender.equals("Male")) {
            return 2;
        } else {
            return 3;
        }
    }
    /*---------------------------------------------------------------------------------------------------------------*/

    public int categoryType() {
        if (category.equals("pharmacy")) {
            return 2;
        } else if (category.equals("medical")) {
            return 3;
        } else if (category.equals("logical")) {
            return 4;
        } else if (category.equals("car")) {
            return 5;
        } else if (category.equals("car")) {
            return 6;
        } else {
            return 0;
        }
    }
    /*---------------------------------------------------------------------------------------------------------------*/

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
            Log.i(TAG, "loadCategoriesFromAPI: " + error);
        });
        MyVolley.getInstance(getActivity()).addToRequestQueue(gsonRequest);
    }
    /*---------------------------------------------------------------------------------------------------------------*/

    private void spinnerList(ArrayList<ResultCategories> resultCategoriesArrayList) {
        ArrayAdapter<ResultCategories> resultCategoriesArrayAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_category_list, resultCategoriesArrayList);
        resultCategoriesArrayAdapter.setDropDownViewResource(R.layout.spinner_category_list);
        binding.spinnerCategory.setAdapter(resultCategoriesArrayAdapter);

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
    /*---------------------------------------------------------------------------------------------------------------*/

    @SuppressLint("SetTextI18n")
    private void showCountryListDialog(FragmentAddQuestionBinding binding) {

        Dialog dialog = new Dialog(getActivity());
        DialogCountryBinding bind = DialogCountryBinding.inflate(getLayoutInflater());
        bindi = bind;
        dialog.setContentView(bind.getRoot());
        dialog.getWindow().setLayout(-1, -2);
        dialog.show();
        bind.rcViewAsaDialog.setLayoutManager(new LinearLayoutManager(getActivity()));
        bind.textSelect.setOnClickListener(v -> {
            binding.textSetCountry.setText(count + "  Selected");
            count = 0;
            dialog.dismiss();
            dialog.setCanceledOnTouchOutside(false);
//            countryAdpater.clearAll();
           /* SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Message", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();*/
        });
        bind.textCancel.setOnClickListener(v -> {
            countryAdpater.clearAll();
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Message", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();
            dialog.dismiss();
        });
        try {
            countryNameList = new ArrayList<>();
            JSONArray jsonArray = new JSONArray(loadJSONFromAsset());
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                CountryName data = new CountryName();
                data.country = jsonObject.getString("country");
                countryNameList.add(data);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        countryAdpater = new CountryAdpater(getActivity(), countryNameList, onClickInterface);
        bind.rcViewAsaDialog.setAdapter(countryAdpater);
        /*bind.search.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String s1 = s.toString().toLowerCase().trim();
                filterList = new ArrayList<>();
                Iterator<CountryName> it = countryNameList.iterator();
                while (it.hasNext()) {
                    CountryName sqliteData = it.next();
                    if (sqliteData.getCountry().toLowerCase().startsWith(s1)) {
                        filterList.add(sqliteData);
                    }
                }
                Iterator<CountryName> it2 = filterList.iterator();
                while (it2.hasNext()) {
                }
                countryAdpater.setFilter(filterList);
                countryAdpater.notifyDataSetChanged();
            }

            public void afterTextChanged(Editable s) {
            }
        });*/

        bind.search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String str = newText.toLowerCase().trim();
                filterList = new ArrayList<>();
                Iterator<CountryName> it = countryNameList.iterator();
                while (it.hasNext()) {
                    CountryName sqliteData = it.next();
                    if (sqliteData.getCountry().toLowerCase().startsWith(str)) {
                        filterList.add(sqliteData);
                    }
                }
                countryAdpater.setFilter(filterList);
                genderArrayAdapter.notifyDataSetChanged();
                if (filterList.isEmpty()) {
                    Toast.makeText(getActivity(), "No Data Found..", Toast.LENGTH_SHORT).show();
                } else {
                    countryAdpater.filterList(filterList);
                }
                filter(str);
                return false;
            }
        });

        bind.checkboxAll.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                countryAdpater.selectAll();
                ArrayList<String> arrayListUser = new ArrayList<>();
                Iterator<CountryName> it = countryNameList.iterator();
                while (it.hasNext()) {
                    arrayListUser.add(it.next().getCountry());
                    bind.textMarquee.setVisibility(View.VISIBLE);
                    bind.textMarquee.setText(String.valueOf(arrayListUser));
                    bind.textMarquee.setSelected(true);
                }
                bind.textSelect.setOnClickListener(v -> {
                    binding.textSetCountry.setText(countryNameList.size() + " Selected");
                    dialog.dismiss();
                });
                return;
            }
            countryAdpater.clearAll();
            ArrayList<String> arrayListUser2 = new ArrayList<>();
            Iterator<CountryName> it2 = countryNameList.iterator();
            while (it2.hasNext()) {
                arrayListUser2.remove(it2.next().getCountry());
                bind.textMarquee.setText(" ");
                bind.textMarquee.setVisibility(View.GONE);
            }
            bind.textSelect.setOnClickListener(v -> {
                binding.textSetCountry.setText("SET COUNTRY");
                dialog.dismiss();
            });
        });
        countryAdpater.notifyDataSetChanged();
    }
    /*---------------------------------------------------------------------------------------------------------------*/

    private void filter(String text) {
        ArrayList<CountryName> filteredlist = new ArrayList<>();

        for (CountryName user : countryNameList) {
            if (user.getCountry().toLowerCase().contains(text.toLowerCase())) {
                filteredlist.add(user);
                countryAdpater.saveData();
            }
        }
        if (filteredlist.isEmpty()) {
            Toast.makeText(getActivity(), "No Data Found...", Toast.LENGTH_SHORT).show();
        } else {
            countryAdpater.filterList(filteredlist);
        }
    }
    /*---------------------------------------------------------------------------------------------------------------*/

    public String loadJSONFromAsset() {
        String json;
        try {
            InputStream is = getActivity().getAssets().open("country.json");
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

    private void createList() {
        countryArrayList = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(loadJSONFromAsset());
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                countryArrayList.add(new CountryList(jsonObject.getString("country")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    /*---------------------------------------------------------------------------------------------------------------*/

    private void initSpinnerGender() {
        createListGender();
        genderArrayAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_country_gender, genderList);
        genderArrayAdapter.setDropDownViewResource(R.layout.spinner_country_gender);
        binding.spinnerGender.setAdapter(genderArrayAdapter);

        binding.spinnerGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String str = parent.getItemAtPosition(position).toString();
                gender = str;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    /*---------------------------------------------------------------------------------------------------------------*/

    private void createListGender() {
        genderList = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(loadJSONFromAssetGender());
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                genderList.add(new Gender(jsonObject.getString("gender")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    /*---------------------------------------------------------------------------------------------------------------*/

    public String loadJSONFromAssetGender() {
        String json;
        try {
            InputStream is = getActivity().getAssets().open("gender.json");
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

    private void openDialogForAddImage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        DialogImagePickerBinding bind = DialogImagePickerBinding.inflate(getLayoutInflater());
        builder.setView(bind.getRoot());

        AlertDialog dialog = builder.create();
        dialog.show();

        bind.textCamera.setOnClickListener(v -> {
            openCameraPermission();
            dialog.dismiss();
        });
        bind.textGallery.setOnClickListener(v -> {
            openGalleryPermission();
            dialog.dismiss();
        });
    }
    /*---------------------------------------------------------------------------------------------------------------*/

    private void openDialogForQuestionOneImage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        DialogImagePickerBinding bind = DialogImagePickerBinding.inflate(getLayoutInflater());
        builder.setView(bind.getRoot());

        AlertDialog dialog = builder.create();
        dialog.show();

        bind.textCamera.setOnClickListener(v -> {
            openCameraPermissionImageOne();
            dialog.dismiss();
        });
        bind.textGallery.setOnClickListener(v -> {
            openGalleryPermissionImageOne();
            dialog.dismiss();
        });
    }
    /*---------------------------------------------------------------------------------------------------------------*/

    private void openGalleryPermissionImageOne() {
        Dexter.withActivity(getActivity())
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        openGalleryOne();
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

    private void openGalleryOne() {
        imagePicker = new ImagePicker(this);
        imagePicker.setImagePickerCallback(new ImagePickerCallback() {
            @Override
            public void onImagesChosen(List<ChosenImage> list) {
                for (ChosenImage chosenImage : list) {
                    imagePath1 = chosenImage.getOriginalPath();
                    System.out.println("Gall:" + chosenImage.getOriginalPath());
                    System.out.println("Gall:" + chosenImage.getThumbnailPath());
                    imageOne = imagePath1;
                    if (imageOne != null) {
                        Glide.with(getActivity()).load(imageOne).listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                return false;
                            }
                        }).into(binding.imageviewOption1ViewQuestion);
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

    private void openCameraPermissionImageOne() {
        Dexter.withActivity(getActivity())
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        openCameraOne();
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

    private void openCameraOne() {
        cameraImagePicker = new CameraImagePicker(this);
        cameraImagePicker.setImagePickerCallback(new ImagePickerCallback() {
            @Override
            public void onImagesChosen(List<ChosenImage> list) {
                Log.i(TAG, "onImagesChosen: " + list.get(0).getHeight());
                for (ChosenImage image : list) {
                    imageOne = image.getThumbnailPath();
                    System.out.println("ssss:" + imageOne);
                    if (imageOne != null) {
                        uploadImageApi(imageOne);
                        Glide.with(getActivity()).load(imageOne).listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                return false;
                            }
                        }).into(binding.imageviewOption1ViewQuestion);
                    }
                }
            }

            @Override
            public void onError(String s) {
            }
        });
        imageOne = cameraImagePicker.pickImage();
    }

    /*---------------------------------------------------------------------------------------------------------------*/


    private void openDialogForQuestionTwoImage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        DialogImagePickerBinding bind = DialogImagePickerBinding.inflate(getLayoutInflater());
        builder.setView(bind.getRoot());

        AlertDialog dialog = builder.create();
        dialog.show();

        bind.textCamera.setOnClickListener(v -> {
            openCameraPermissionImageTwo();
            dialog.dismiss();
        });
        bind.textGallery.setOnClickListener(v -> {
            openGalleryPermissionImageTwo();
            dialog.dismiss();
        });
    }
    /*---------------------------------------------------------------------------------------------------------------*/

    private void openGalleryPermissionImageTwo() {
        Dexter.withActivity(getActivity())
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        openGalleryTwo();
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

    private void openGalleryTwo() {
        imagePicker = new ImagePicker(this);
        imagePicker.setImagePickerCallback(new ImagePickerCallback() {
            @Override
            public void onImagesChosen(List<ChosenImage> list) {
                for (ChosenImage chosenImage : list) {
                    imagePath2 = chosenImage.getOriginalPath();
                    imageSize = chosenImage.getSize();
                    imageTwo = imagePath2;
                    Log.i(TAG, "onImagesChosen: size:::" + imageSize);
                    if (imageSize > 10240000) {
                        showSnackbar("Images File cannot be larger than 10 MB");
                        binding.imageCamera.setVisibility(View.GONE);
                    } else {
                        if (imageTwo != null) {
                            Glide.with(getActivity()).load(imageTwo).listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                                binding.imgCamera.setVisibility(View.GONE);
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                                binding.imgCamera.setVisibility(View.GONE);
                                    return false;
                                }
                            }).into(binding.imageviewOption2ViewQuestion);
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

    private void openCameraPermissionImageTwo() {
        Dexter.withActivity(getActivity())
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        openCameraTwo();
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

    private void openCameraTwo() {
        cameraImagePicker = new CameraImagePicker(this);
        cameraImagePicker.setImagePickerCallback(new ImagePickerCallback() {
            @Override
            public void onImagesChosen(List<ChosenImage> list) {
                for (ChosenImage image : list) {
                    imageTwo = image.getThumbnailPath();
                    System.out.println("sssstwo:" + imageTwo);
                    if (imageTwo != null) {
                        uploadImageApi(imageTwo);
                        Glide.with(getActivity()).load(imageTwo).listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                return false;
                            }
                        }).into(binding.imageviewOption2ViewQuestion);
                    }
                }
            }

            @Override
            public void onError(String s) {
            }
        });
        imageTwo = cameraImagePicker.pickImage();
    }
    /*---------------------------------------------------------------------------------------------------------------*/

    private void openGallery() {
        showProgress();
        imagePicker = new ImagePicker(this);
        imagePicker.setImagePickerCallback(new ImagePickerCallback() {
            @Override
            public void onImagesChosen(List<ChosenImage> list) {
                for (ChosenImage chosenImage : list) {
                    imagePath = chosenImage.getOriginalPath();

                    questionImage = imagePath;

                    questionImageSize = chosenImage.getSize();
                    if (questionImageSize > 200000) {
                        progressDialog.cancel();
                        showSnackbar("Images File cannot be larger than 200KB");
                    } else {
                        if (questionImage != null) {
                            uploadImageApi(questionImage);
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
        cameraImagePicker = new CameraImagePicker(this);
        cameraImagePicker.setImagePickerCallback(new ImagePickerCallback() {
            @Override
            public void onImagesChosen(List<ChosenImage> list) {
                for (ChosenImage image : list) {
                    questionImage = image.getThumbnailPath();
                    questionImageSize = image.getSize();
                    System.out.println("ssss:" + questionImage);
                    if (questionImageSize > 200000) {
                        progressDialog.cancel();
                        showSnackbar("Images File cannot be larger than 200KB");
                    } else {
                        if (questionImage != null) {
                            uploadImageApi(questionImage);
                           /* Glide.with(getActivity()).load(questionImage).listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    binding.imageCamera.setVisibility(View.GONE);
                                    return false;
                                }
                            }).into(binding.imageBackground);*/
                        }
                    }

                }
            }

            @Override
            public void onError(String s) {
            }
        });
        questionImage = cameraImagePicker.pickImage();
    }
    /*---------------------------------------------------------------------------------------------------------------*/

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
    /*---------------------------------------------------------------------------------------------------------------*/

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
    /*---------------------------------------------------------------------------------------------------------------*/

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
    /*---------------------------------------------------------------------------------------------------------------*/

    private void openPermissionSetting() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_SETTINGS);
        startActivity(intent);
    }
    /*---------------------------------------------------------------------------------------------------------------*/

    public void showProgress() {
        progressDialog.setMessage("Please Wait...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        progressDialog.setCancelable(true);
    }
    /*---------------------------------------------------------------------------------------------------------------*/

    private void showSnackbar(String str) {
        Snackbar snackbar = Snackbar.make(binding.getRoot(), str, BaseTransientBottomBar.LENGTH_SHORT);
        snackbar.show();
    }
    /*---------------------------------------------------------------------------------------------------------------*/

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Picker.PICK_IMAGE_CAMERA) {
                cameraImagePicker.submit(data);
                Log.i(TAG, "onActivityResult: " + data);
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
    /*---------------------------------------------------------------------------------------------------------------*/

    @Override
    public void onAttach(@NonNull Activity context) {
        super.onAttach(context);
        activity = context;
    }
    /*---------------------------------------------------------------------------------------------------------------*/

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i(TAG, "onDestroyView: ");
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Message", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}