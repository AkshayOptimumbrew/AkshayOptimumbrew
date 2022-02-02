package askaquestion.com.app.ui.fragments;

import static askaquestion.com.app.core.constants.ConstantsAPI.URL_ADD_ANSWER;
import static askaquestion.com.app.core.constants.Extras.EXTRA_OPTION1;
import static askaquestion.com.app.core.constants.Extras.EXTRA_OPTION2;
import static askaquestion.com.app.core.constants.Extras.EXTRA_QUESTION;
import static askaquestion.com.app.core.constants.Extras.QUESTION_ID;
import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.ParseError;
import com.android.volley.Response;
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
import com.optimumbrew.library.core.volley.GsonRequest;
import com.optimumbrew.library.core.volley.MyVolley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import askaquestion.com.app.R;
import askaquestion.com.app.core.interneconnection.DetectConnection;
import askaquestion.com.app.core.pojo.addanswer.RequestAnswer;
import askaquestion.com.app.core.pojo.addanswer.ResponseAnswer;
import askaquestion.com.app.core.pojo.getallquestionbyuser.ResponseQuestion;
import askaquestion.com.app.core.pojo.getallquestionbyuser.ResultWithQuestionId;
import askaquestion.com.app.databinding.DialogAddAnswerBinding;
import askaquestion.com.app.ui.activity.ResultQuestionActivity;
import askaquestion.com.app.core.sharedprefs.PrefManager;
import askaquestion.com.app.core.sharedprefs.SharedManagerForVolley;
import askaquestion.com.app.ui.activity.SigninActivity;
import askaquestion.com.app.core.constants.ConstantsAPI;
import askaquestion.com.app.databinding.DialogTokenExpiredBinding;
import askaquestion.com.app.databinding.FragmentViewQuestionBinding;
import askaquestion.com.app.core.pojo.reportquestion.RequestReport;
import askaquestion.com.app.core.pojo.reportquestion.ResponseReport;

public class ViewQuestionFragment extends Fragment {

    FragmentViewQuestionBinding binding;
    SharedManagerForVolley managerForVolley;
    String TAG = "ViewQuestionFragment";
    ArrayList<ResultWithQuestionId> resultArrayList;
    ArrayList<ResultWithQuestionId> myResultArrayList;
    int index = 0;
    PrefManager prefManager;
    ProgressDialog progressDialog;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentViewQuestionBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(!DetectConnection.checkInternetConnection(getActivity())){
            Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }

        myResultArrayList = new ArrayList<>();
        managerForVolley = new SharedManagerForVolley(getActivity());
        prefManager = new PrefManager(getActivity());
        progressDialog = new ProgressDialog(getActivity(),R.style.ProgressDialog);

        LocationPermission();
        binding.imageRefresh.setOnClickListener(v -> {
            Log.i(TAG, "onViewCreated: ");
            LocationPermission();

        });
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + managerForVolley.getToken());
        /*if (resultArrayList == null) {
            binding.rlimg.setVisibility(View.GONE);
            binding.rlWithImageAndText.setVisibility(View.GONE);
            binding.textDescription.setVisibility(View.GONE);
            binding.imageRefresh.setVisibility(View.GONE);
            binding.textNoQuestionFound.setVisibility(View.VISIBLE);
        }*/
//        showProgress();
        binding.textNoQuestionFound.setVisibility(View.GONE);
        GsonRequest<ResponseQuestion> gsonRequest = new GsonRequest<ResponseQuestion>(1,
                ConstantsAPI.URL_GET_QUESTION,
                "{}",
                ResponseQuestion.class,
                headers,
                response -> {
                    progressDialog.cancel();
                    Log.i(TAG, "onViewCreated: code::" + response.getCode());
                    Log.i(TAG, "onResponse: Result::" + response.getData().getResult());
                    resultArrayList = response.getData().getResult();
                    myResultArrayList = resultArrayList;
                    if (response.getCode() == 400) {
                        progressDialog.cancel();
                        openDialogForLogout();
                    }
                    if (resultArrayList != null) {

                        binding.rlimg.setVisibility(View.VISIBLE);
                        binding.rlWithImageAndText.setVisibility(View.VISIBLE);
                        binding.textDescription.setVisibility(View.VISIBLE);
                        binding.textLL.setVisibility(View.VISIBLE);
//                        binding.editOption2.setVisibility(View.VISIBLE);
                        binding.imageRefresh.setVisibility(View.GONE);
                        binding.textNoQuestionFound.setVisibility(View.GONE);
                        ResultWithQuestionId result = resultArrayList.get(index);
                        if (result.getOption_type() == 1) {
                            binding.textDescription.setText(result.getDescription());

                            Glide.with(getActivity()).load(result.getQuestion_original())
                                    .listener(new RequestListener<Drawable>() {
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
                                    }).into(binding.imageQuestion);
                            binding.editOption1.setText(result.getOption1());
                            binding.editOption2.setText(result.getOption2());
                        }
                        if (result.getOption_type() == 2) {
                            binding.textDescription.setText(result.getDescription());
                            Glide.with(getActivity()).load(result.getOption1_original_image()).listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    binding.progressbarOption1.setVisibility(View.GONE);
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    binding.progressbarOption1.setVisibility(View.GONE);
                                    return false;
                                }
                            }).into(binding.imageOption1ViewQuestion);
                            Glide.with(getActivity()).load(result.getOption2_original_image()).listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    binding.progressbarOption2.setVisibility(View.GONE);
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    binding.progressbarOption2.setVisibility(View.GONE);
                                    return false;
                                }
                            }).into(binding.imageOption2ViewQuestion);
                        }

                    }
                    binding.editOption1.setOnClickListener(v -> {
                        openDialogForAskUserToAnswer1(resultArrayList.get(index));
                    });
                    binding.editOption2.setOnClickListener(v -> {
                        openDialogForAskUserToAnswer2(resultArrayList.get(index));
                    });
                    if(myResultArrayList.size()==1){
                        binding.textSkip.setVisibility(View.GONE);
                    }else{
                        binding.textSkip.setVisibility(View.VISIBLE);
                    }
                    binding.textSkip.setOnClickListener(v -> {
                        if (resultArrayList != null) {
                            index++;
                            Log.i(TAG, "onViewCreated: size:::"+resultArrayList.size());//194
                            ResultWithQuestionId result = resultArrayList.get(index);
                            if (result.getOption_type() == 1) {
                                binding.textLL.setVisibility(View.GONE);
                                binding.imageRL.setVisibility(View.VISIBLE);
                                if(result.getOption1().endsWith(".png")&&result.getOption2().endsWith(".png")){
                                    binding.textDescription.setText(result.getDescription());
                                    binding.textDescription.setText(result.getDescription());
                                    Glide.with(getActivity()).load(result.getQuestion_original()).into(binding.imageQuestion);
                                    Glide.with(getActivity()).load(result.getOption1()).listener(new RequestListener<Drawable>() {
                                        @Override
                                         public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                            binding.imageOption1ViewQuestion.setImageResource(R.drawable.enable_to_load_image);
                                            binding.progressbarOption1.setVisibility(View.GONE);
                                            binding.progressbarOption2.setVisibility(View.GONE);
                                            return false;
                                        }

                                        @Override
                                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                            binding.progressbarOption1.setVisibility(View.GONE);
                                            binding.progressbarOption2.setVisibility(View.GONE);
                                            return false;
                                        }
                                    }).into(binding.imageOption1ViewQuestion);
                                    Glide.with(getActivity()).load(result.getOption2()).into(binding.imageOption2ViewQuestion);
                                }else{
                                    binding.textLL.setVisibility(View.VISIBLE);
                                    binding.imageRL.setVisibility(View.GONE);
                                    binding.textDescription.setText(result.getDescription());
                                    Glide.with(getActivity()).load(result.getQuestion_original())
                                            .into(binding.imageQuestion);
                                    binding.editOption1.setText(result.getOption1());
                                    binding.editOption2.setText(result.getOption2());
                                }

                            }
                            if (result.getOption_type() == 2) {
                                binding.textDescription.setText(result.getDescription());
                                Glide.with(getActivity()).load(result.getOption1_original_image()).into(binding.imageOption1ViewQuestion);
                                Glide.with(getActivity()).load(result.getOption2_original_image()).into(binding.imageOption2ViewQuestion);
                            }
                            if (index == resultArrayList.size() - 1) {
                                showSnackbar("This is Last Question");
                                binding.textSkip.setVisibility(View.GONE);
                                binding.textSkip.setEnabled(false);
                            }

                        }
                    });
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "onErrorResponse::::::::" + error);
                if (error.toString().equals("com.optimumbrew.library.core.volley.CustomError: The token has been blacklisted")) {
                    progressDialog.cancel();
                    openDialogForLogout();
                }else if(error instanceof ParseError){
                    showSnackbar("Server Not Respond");
                }
            }
        });
        MyVolley.getInstance(getContext()).addToRequestQueue(gsonRequest);


        binding.imageReportIcon.setOnClickListener(v -> {
            RequestReport requestReport = new RequestReport();
            requestReport.setQuestion_id(7);

            String body = new Gson().toJson(requestReport);

            Log.i(TAG, "onViewCreated: " + body);

            GsonRequest<ResponseReport> gsonRequest1 = new GsonRequest<>(1,
                    ConstantsAPI.URL_REPORT_QUESTION,
                    body,
                    ResponseReport.class,
                    headers,
                    new Response.Listener<ResponseReport>() {
                        @Override
                        public void onResponse(ResponseReport response) {
                            showSnackbar(response.getMessage());
                            Log.i(TAG, "onResponse: " + response.getMessage());
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.i(TAG, "onErrorResponse: " + error);
                }
            });
            MyVolley.getInstance(getActivity()).addToRequestQueue(gsonRequest1);
        });
    }

    private void openDialogForLogout() {
        final Dialog dialog = new Dialog(getActivity());
        DialogTokenExpiredBinding binding = DialogTokenExpiredBinding.inflate(getLayoutInflater());
        dialog.setContentView(binding.getRoot());
        dialog.getWindow().setLayout(-1, -2);
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);

        binding.buttonClick.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), SigninActivity.class));
            prefManager.setLoginStatus(false);
            managerForVolley.setLoginStatus(false);
        });
    }

    public void showProgress() {
        progressDialog.setMessage("Please Wait While Load Data..");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        progressDialog.setCancelable(true);
    }



    private void openDialogForAskUserToAnswer1(ResultWithQuestionId result) {
        final Dialog dialog = new Dialog(getActivity());
        DialogAddAnswerBinding binding = DialogAddAnswerBinding.inflate(getLayoutInflater());
        dialog.setContentView(binding.getRoot());
        dialog.getWindow().setLayout(-1, -2);
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);

        binding.textYes.setOnClickListener(v ->{
            addAnswerForOption1(result);
            Intent intent = new Intent(getActivity(), ResultQuestionActivity.class);
            Log.i(TAG, "openDialogForAskUserToAnswer1: QuestionID:::" + result.getQuestion_id());
            Log.i(TAG, "openDialogForAskUserToAnswer1: option1:::" + result.getOption1());
            intent.putExtra("SELECTED_ANSWER", 1);
            intent.putExtra(EXTRA_OPTION1, result.getOption1());
            intent.putExtra(EXTRA_OPTION2, result.getOption2());
            intent.putExtra(EXTRA_QUESTION, result.getDescription());
            intent.putExtra(QUESTION_ID, result.getQuestion_id());
            Log.i(TAG, "openDialogForAskUserToAnswer1: QuestionDESC:::" + result.getDescription());
            startActivity(intent);
            dialog.dismiss();
        });
        binding.textNo.setOnClickListener(v ->dialog.dismiss());
    }

    private void addAnswerForOption1(ResultWithQuestionId result) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + managerForVolley.getToken());

        RequestAnswer requestAnswer = new RequestAnswer();
        requestAnswer.setQuestion_id(result.getQuestion_id());
        requestAnswer.setSelected_answer(1);

        String body = new Gson().toJson(requestAnswer);
        Log.i(TAG, "onCreate: body::::" + body);

        GsonRequest<ResponseAnswer> gsonRequest = new GsonRequest<ResponseAnswer>(1,
                URL_ADD_ANSWER,
                body,
                ResponseAnswer.class,
                headers,
                new Response.Listener<ResponseAnswer>() {
                    @Override
                    public void onResponse(ResponseAnswer response) {
                        Log.i(TAG, "onResponse URL_ADD_ANSWER: " + response.getMessage());
                        Toast.makeText(getActivity(), "Answer Added Successfully", Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "onErrorResponse URL_ADD_ANSWER: "+error);
            }
        });
        MyVolley.getInstance(getActivity()).addToRequestQueue(gsonRequest);
    }
    private void openDialogForAskUserToAnswer2(ResultWithQuestionId result) {
        final Dialog dialog = new Dialog(getActivity());
        DialogAddAnswerBinding binding = DialogAddAnswerBinding.inflate(getLayoutInflater());
        dialog.setContentView(binding.getRoot());
        dialog.getWindow().setLayout(-1, -2);
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);

        binding.textYes.setOnClickListener(v ->{
            addAnswerForOption2(result);

            Intent intent = new Intent(getActivity(), ResultQuestionActivity.class);
            Log.i(TAG, "openDialogForAskUserToAnswer1: QuestionID:::" + result.getQuestion_id());
            Log.i(TAG, "openDialogForAskUserToAnswer1: option2:::" + result.getOption2());
            intent.putExtra("SELECTED_ANSWER", 2);
            intent.putExtra(EXTRA_OPTION2, result.getOption2());
            intent.putExtra(EXTRA_OPTION1, result.getOption1());
            intent.putExtra(EXTRA_QUESTION, result.getDescription());
            intent.putExtra(QUESTION_ID, result.getQuestion_id());
            startActivity(intent);
            dialog.dismiss();
        });
        binding.textNo.setOnClickListener(v ->dialog.dismiss());
    }

    private void addAnswerForOption2(ResultWithQuestionId result) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + managerForVolley.getToken());

        RequestAnswer requestAnswer = new RequestAnswer();
        requestAnswer.setQuestion_id(result.getQuestion_id());
        requestAnswer.setSelected_answer(2);

        String body = new Gson().toJson(requestAnswer);
        Log.i(TAG, "onCreate: body::::" + body);

        GsonRequest<ResponseAnswer> gsonRequest = new GsonRequest<ResponseAnswer>(1,
                URL_ADD_ANSWER,
                body,
                ResponseAnswer.class,
                headers,
                new Response.Listener<ResponseAnswer>() {
                    @Override
                    public void onResponse(ResponseAnswer response) {
                        Log.i(TAG, "onResponse URL_ADD_ANSWER: " + response.getMessage());
                        Toast.makeText(getActivity(), "Answer Added Successfully", Toast.LENGTH_SHORT).show();
                        showSnackbar(response.getMessage());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "onErrorResponse URL_ADD_ANSWER: "+error);
            }
        });
        MyVolley.getInstance(getActivity()).addToRequestQueue(gsonRequest);
    }
    private void LocationPermission() {
        Dexter.withActivity(getActivity())
                .withPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        Log.i(TAG, "onPermissionGranted: ");
                        binding.imageRefresh.setVisibility(View.GONE);
                        if (myResultArrayList.isEmpty()) {
                            binding.rlimg.setVisibility(View.GONE);
                            binding.rlWithImageAndText.setVisibility(View.GONE);
                            binding.textDescription.setVisibility(View.GONE);
                            binding.textNoQuestionFound.setVisibility(View.VISIBLE);
                        }else{
                            binding.imageRefresh.setVisibility(View.GONE);
                            binding.textLL.setVisibility(View.VISIBLE);
                            binding.rlWithImageAndText.setVisibility(View.VISIBLE);
                            binding.imageQuestion.setVisibility(View.VISIBLE);
                            binding.textDescription.setVisibility(View.VISIBLE);
                        }
                    }


                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        binding.imageRefresh.setVisibility(View.VISIBLE);
                        binding.textLL.setVisibility(View.GONE);
                        binding.rlWithImageAndText.setVisibility(View.GONE);
                        binding.imageQuestion.setVisibility(View.GONE);
                        binding.textDescription.setVisibility(View.GONE);
                        if (response.isPermanentlyDenied()) {
                            binding.textLL.setVisibility(View.GONE);
                            binding.imageRefresh.setVisibility(View.VISIBLE);
                            binding.imageRefresh.setOnClickListener(v ->{
                                openDialog();
                            });
                        }

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    private void showSnackbar(String str) {
        Snackbar snackbar = Snackbar.make(binding.getRoot(), str, BaseTransientBottomBar.LENGTH_SHORT);
        snackbar.show();
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
        Intent intent = new Intent(Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS);
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume: ");
        int permission=getActivity().checkCallingOrSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION);
        if(permission== PackageManager.PERMISSION_GRANTED){
            Log.i(TAG, "onResume: Granted..");
            binding.imageRefresh.setVisibility(View.GONE);
        }else{
            binding.imageRefresh.setVisibility(View.VISIBLE);
        }
    }
}