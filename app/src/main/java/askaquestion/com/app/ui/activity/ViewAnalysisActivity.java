package askaquestion.com.app.ui.activity;

import static askaquestion.com.app.core.constants.ConstantsAPI.URL_COUNTRY;
import static askaquestion.com.app.core.constants.ConstantsAPI.URL_GENDER;
import static askaquestion.com.app.core.constants.ConstantsAPI.URL_NONE;
import static askaquestion.com.app.core.constants.Extras.EXTRA_DESCRIPTION;
import static askaquestion.com.app.core.constants.Extras.QUESTION_ID;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.optimumbrew.library.core.volley.GsonRequest;
import com.optimumbrew.library.core.volley.MyVolley;

import java.util.HashMap;
import java.util.Map;

import askaquestion.com.app.core.sharedprefs.SharedManagerForVolley;
import askaquestion.com.app.ui.adapters.AnalysisCountryAdapter;
import askaquestion.com.app.ui.adapters.AnalysisGenderAdapter;
import askaquestion.com.app.ui.adapters.AnalysisNoneAdapter;
import askaquestion.com.app.databinding.ActivityViewAnalysisBinding;
import askaquestion.com.app.core.pojo.analysiscountry.RequestAnalysis;
import askaquestion.com.app.core.pojo.analysiscountry.ResponseAnalysisCountry;
import askaquestion.com.app.core.pojo.analysisgender.ResponseGender;
import askaquestion.com.app.core.pojo.noneanalysis.ResponseNone;

public class ViewAnalysisActivity extends AppCompatActivity {

    ActivityViewAnalysisBinding binding;
    String TAG = "ViewAnalysisActivity";
    SharedManagerForVolley managerForVolley;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewAnalysisBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.textDescription.setText(getIntent().getStringExtra(EXTRA_DESCRIPTION));
        managerForVolley = new SharedManagerForVolley(getApplicationContext());

        ApiForCountry();
        binding.radiobuttonNone.setOnClickListener(v ->{
            ApiForNone();
        });
        binding.radiobuttonCountry.setOnClickListener(v ->{
            ApiForCountry();
        });
        binding.radiobuttonGender.setOnClickListener(v ->{
            ApiForGender();
        });
    }

    private void ApiForNone() {
        binding.radiobuttonNone.setChecked(true);
        binding.radiobuttonCountry.setChecked(false);
        binding.radiobuttonGender.setChecked(false);

        binding.recyclerViewNone.setVisibility(View.VISIBLE);
        binding.recyclerViewGender.setVisibility(View.GONE);
        binding.recyclerViewCountry.setVisibility(View.GONE);
        binding.llGender.setVisibility(View.VISIBLE);
        binding.llCountry.setVisibility(View.GONE);
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + managerForVolley.getToken());

        RequestAnalysis requestAnalysis = new RequestAnalysis();
        requestAnalysis.setQuestion_id(/*getIntent().getIntExtra(QUESTION_ID,30)*/30);
        String request = new Gson().toJson(requestAnalysis);

        Log.i(TAG, "ApiForNone: "+request);

        GsonRequest<ResponseNone> gsonRequest = new GsonRequest<ResponseNone>(1,
                URL_NONE,
                request,
                ResponseNone.class,
                headers,
                response -> {
                    Log.i(TAG, "onResponse: None" + response.getData().getResult().size());
                    binding.recyclerViewNone.setAdapter(new AnalysisNoneAdapter(response.getData().getResult(), getApplicationContext()));
                    binding.recyclerViewNone.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                }, error -> Log.i(TAG, "onErrorResponse: " + error));
        MyVolley.getInstance(getApplicationContext()).addToRequestQueue(gsonRequest);
    }

    private void ApiForGender() {
        binding.radiobuttonNone.setChecked(false);
        binding.radiobuttonGender.setChecked(true);
        binding.radiobuttonCountry.setChecked(false);

        binding.recyclerViewNone.setVisibility(View.GONE);
        binding.recyclerViewCountry.setVisibility(View.GONE);
        binding.recyclerViewGender.setVisibility(View.VISIBLE);
        binding.llGender.setVisibility(View.VISIBLE);
        binding.llCountry.setVisibility(View.GONE);

        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + managerForVolley.getToken());

        RequestAnalysis requestAnalysis = new RequestAnalysis();

        Log.i(TAG, "ApiForGender: getIntent().getIntExtra(QUESTION_ID,30)"+getIntent().getIntExtra(QUESTION_ID,30));
        requestAnalysis.setQuestion_id(/*getIntent().getIntExtra(QUESTION_ID,30)*/30);
        String request = new Gson().toJson(requestAnalysis);

        Log.i(TAG, "ApiForGender: "+request);
        GsonRequest<ResponseGender> gsonRequest = new GsonRequest<ResponseGender>(1,
                URL_GENDER,
                request,
                ResponseGender.class,
                headers,
                response -> {
                    Log.i(TAG, "onResponse: Country" + response.getData().getResult().size());
                    binding.recyclerViewGender.setAdapter(new AnalysisGenderAdapter(getApplicationContext(), response.getData().getResult()));
                    binding.recyclerViewGender.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                }, error -> Log.i(TAG, "onErrorResponse: " + error));
        MyVolley.getInstance(getApplicationContext()).addToRequestQueue(gsonRequest);
    }

    private void ApiForCountry() {
        binding.radiobuttonNone.setChecked(false);
        binding.radiobuttonGender.setChecked(false);
        binding.radiobuttonCountry.setChecked(true);

        binding.recyclerViewCountry.setVisibility(View.VISIBLE);
        binding.recyclerViewGender.setVisibility(View.GONE);
        binding.recyclerViewNone.setVisibility(View.GONE);
        binding.llGender.setVisibility(View.GONE);
        binding.llCountry.setVisibility(View.VISIBLE);

        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + managerForVolley.getToken());

        RequestAnalysis requestAnalysis = new RequestAnalysis();
        requestAnalysis.setQuestion_id(/*getIntent().getIntExtra(QUESTION_ID,30)*/30);
        String request = new Gson().toJson(requestAnalysis);

        Log.i(TAG, "ApiForCountry: "+request);

        GsonRequest<ResponseAnalysisCountry> gsonRequest = new GsonRequest<ResponseAnalysisCountry>(1,
                URL_COUNTRY,
                request,
                ResponseAnalysisCountry.class,
                headers,
                response -> {
                    Log.i(TAG, "onResponse: Country" + response.getData().getResult().size());
                    binding.recyclerViewCountry.setAdapter(new AnalysisCountryAdapter(response.getData().getResult(), getApplicationContext()));
                    binding.recyclerViewCountry.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                }, error -> Log.i(TAG, "onErrorResponse: " + error));
        MyVolley.getInstance(getApplicationContext()).addToRequestQueue(gsonRequest);
    }


}