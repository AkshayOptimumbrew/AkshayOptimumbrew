package askaquestion.com.app.ui.fragments;

import static askaquestion.com.app.core.constants.ConstantsAPI.URL_VIEW_QUESTION;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.optimumbrew.library.core.volley.GsonRequest;
import com.optimumbrew.library.core.volley.MyVolley;

import java.util.HashMap;
import java.util.Map;

import askaquestion.com.app.core.sharedprefs.SharedManagerForVolley;
import askaquestion.com.app.ui.adapters.ViewQuestionAdapter;
import askaquestion.com.app.databinding.FragmentAccountBinding;
import askaquestion.com.app.core.pojo.getquestion.ResponseForGetQuestion;


public class AccountFragment extends Fragment {

    FragmentAccountBinding binding;
    SharedManagerForVolley managerForVolley;
    String TAG = "AccountFragment";
    ViewQuestionAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAccountBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        managerForVolley = new SharedManagerForVolley(getActivity());

        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + managerForVolley.getToken());


        GsonRequest<ResponseForGetQuestion> gsonRequest = new GsonRequest<ResponseForGetQuestion>(
                1,
                URL_VIEW_QUESTION,
                "{}",
                ResponseForGetQuestion.class,
                headers,
                response -> {
                    if (response.getCode() == 200 && response != null) {
                        Log.i(TAG, "onResponse: " + response.getData().getResult());
                        binding.textNoQuestionFound.setVisibility(View.GONE);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                        linearLayoutManager.scrollToPosition(response.getData().getResult().size() - 1);
                        adapter = new ViewQuestionAdapter(response.getData().getResult(), getActivity());
                        adapter.notifyDataSetChanged();
                        binding.rvViewQuestion.setLayoutManager(linearLayoutManager);
                        binding.rvViewQuestion.setAdapter(adapter);
                    }

                    if (response.getData().getResult().isEmpty()) {
                        binding.textNoQuestionFound.setVisibility(View.VISIBLE);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "onErrorResponse: " + error);
            }
        });
        MyVolley.getInstance(getActivity()).addToRequestQueue(gsonRequest);

    }
}