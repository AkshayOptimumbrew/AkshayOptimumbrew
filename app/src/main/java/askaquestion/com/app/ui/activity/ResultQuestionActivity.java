package askaquestion.com.app.ui.activity;

import static askaquestion.com.app.core.constants.ConstantsAPI.URL_ADD_ANSWER;
import static askaquestion.com.app.core.constants.ConstantsAPI.URL_NONE;
import static askaquestion.com.app.core.constants.Extras.EXTRA_OPTION1;
import static askaquestion.com.app.core.constants.Extras.EXTRA_OPTION2;
import static askaquestion.com.app.core.constants.Extras.ID;
import static askaquestion.com.app.core.constants.Extras.QUESTION;
import static askaquestion.com.app.core.constants.Extras.QUESTION_ID;
import static askaquestion.com.app.core.constants.Extras.SELECTED_ANSWER;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.optimumbrew.library.core.volley.GsonRequest;
import com.optimumbrew.library.core.volley.MyVolley;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import askaquestion.com.app.R;
import askaquestion.com.app.core.sharedprefs.SharedManagerForVolley;
import askaquestion.com.app.databinding.ActivityResultQuestionBinding;
import askaquestion.com.app.core.pojo.addanswer.RequestAnswer;
import askaquestion.com.app.core.pojo.addanswer.ResponseAnswer;
import askaquestion.com.app.core.pojo.analysiscountry.RequestAnalysis;
import askaquestion.com.app.core.pojo.noneanalysis.ResponseNone;
import askaquestion.com.app.core.pojo.noneanalysis.ResultNone;

public class ResultQuestionActivity extends AppCompatActivity {

    ActivityResultQuestionBinding binding;
    SharedManagerForVolley managerForVolley;
    String TAG = "ResultQuestionActivity";
    private ArrayList<ResultNone> resultNoneArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityResultQuestionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        managerForVolley = new SharedManagerForVolley(getApplicationContext());

        int questionId = getIntent().getIntExtra(QUESTION_ID, 30);
        ApiForNone(questionId);
        String o1 = getIntent().getStringExtra(EXTRA_OPTION1);
        String o2 = getIntent().getStringExtra(EXTRA_OPTION2);
        String description = getIntent().getStringExtra(QUESTION);

        binding.textOption1.setText(String.valueOf(o1));
        binding.textOption2.setText(String.valueOf(o2));
        binding.textDescription.setText(description);



        binding.imageNext.setOnClickListener(v -> {
            finish();
        });
    }

    private void ApiForNone(int questionId) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + managerForVolley.getToken());

        RequestAnalysis requestAnalysis = new RequestAnalysis();
        requestAnalysis.setQuestion_id(questionId);
        String request = new Gson().toJson(requestAnalysis);

        Log.i(TAG, "ApiForNone: "+request);

        GsonRequest<ResponseNone> gsonRequest = new GsonRequest<ResponseNone>(1,
                URL_NONE,
                request,
                ResponseNone.class,
                headers,
                new Response.Listener<ResponseNone>() {
                    @Override
                    public void onResponse(ResponseNone response) {
                        resultNoneArrayList=response.getData().getResult();

                        for(ResultNone resultNone:resultNoneArrayList){
                            String num1=resultNone.getOption1();
                            String num2=resultNone.getOption2();
                            num1=removeLastChar(num1);
                            num2=removeLastChar(num2);

                            binding.piechart.addPieSlice(new PieModel("Option1", Float.valueOf(num1), Color.parseColor("#FE6DA8")));
                            binding.piechart.addPieSlice(new PieModel("Option2", Float.valueOf(num2), Color.parseColor("#56B7F1")));
                            binding.piechart.startAnimation();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "onErrorResponse: " + error);
            }
        });
        MyVolley.getInstance(getApplicationContext()).addToRequestQueue(gsonRequest);
    }
    private String removeLastChar(String s)
    {
        return s.substring(0, s.length() - 1);
    }
}