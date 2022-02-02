package askaquestion.com.app.ui.adapters;

import static askaquestion.com.app.core.constants.Extras.Category_ID;
import static askaquestion.com.app.core.constants.Extras.Country;
import static askaquestion.com.app.core.constants.Extras.Description;
import static askaquestion.com.app.core.constants.Extras.Gender;
import static askaquestion.com.app.core.constants.Extras.Option1;
import static askaquestion.com.app.core.constants.Extras.Option1Image;
import static askaquestion.com.app.core.constants.Extras.Option2;
import static askaquestion.com.app.core.constants.Extras.Option2Image;
import static askaquestion.com.app.core.constants.Extras.Option_Type;
import static askaquestion.com.app.core.constants.Extras.QUESTION_ID;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.optimumbrew.library.core.volley.GsonRequest;
import com.optimumbrew.library.core.volley.MyVolley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import askaquestion.com.app.core.constants.ConstantsAPI;
import askaquestion.com.app.core.pojo.getallquestionbyuser.ResponseQuestion;
import askaquestion.com.app.core.pojo.getallquestionbyuser.ResultWithQuestionId;
import askaquestion.com.app.core.sharedprefs.SharedManagerForVolley;
import askaquestion.com.app.ui.activity.ViewAnalysisActivity;
import askaquestion.com.app.ui.activity.ViewQuestionActivity;
import askaquestion.com.app.databinding.AdapterViewQuestionBinding;
import askaquestion.com.app.core.pojo.getquestion.Result;

public class ViewQuestionAdapter extends RecyclerView.Adapter<ViewQuestionAdapter.myViewHolder> {
    ArrayList<Result> arrayList;
    Context context;
    String TAG="ViewQuestionAdapter";
    SharedManagerForVolley managerForVolley;
    int Question_id;

    public ViewQuestionAdapter(ArrayList<Result> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AdapterViewQuestionBinding binding=AdapterViewQuestionBinding.inflate(LayoutInflater.from(context),parent,false);
        return new myViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        managerForVolley = new SharedManagerForVolley(context);
        Result result=arrayList.get(position);
        holder.binding.textDescription.setText(result.getDescription());
        holder.binding.buttonViewQuestion.setOnClickListener(v ->{
            Log.i(TAG, "onBindViewHolder: "+position);
            Intent intent=new Intent(context, ViewQuestionActivity.class);
            intent.putExtra(Description,result.getDescription());
            intent.putExtra(Option1,result.getOption1());
            intent.putExtra(Option2,result.getOption2());
            intent.putExtra(Country,result.getCountry());
            Log.i(TAG, "onBindViewHolder: Country::::::"+result.getCountry());
            intent.putExtra(Gender,result.getGender());
            intent.putExtra(Category_ID,result.getCategory_id());
            Log.i(TAG, "onBindViewHolder: "+result.getCategory_id());

            intent.putExtra(Option1Image,result.getOption1_compress_image());
            intent.putExtra(Option2Image,result.getOption2_compress_image());
            intent.putExtra(Option_Type,result.getOption_type());
            context.startActivity(new Intent(intent));
        });
        holder.binding.buttonViewAnalysis.setOnClickListener(v ->{
            Map<String, String> headers = new HashMap<>();
            headers.put("Authorization", "Bearer " + managerForVolley.getToken());
            GsonRequest<ResponseQuestion> gsonRequest=new GsonRequest<ResponseQuestion>(1, ConstantsAPI.URL_GET_QUESTION, "{}",
                    ResponseQuestion.class, headers, new Response.Listener<ResponseQuestion>() {
                @Override
                public void onResponse(ResponseQuestion response) {
                    Log.i(TAG, "onResponse: "+response.getMessage());
                    ArrayList<ResultWithQuestionId> resultWithQuestionIdArrayList=response.getData().getResult();
                    for(ResultWithQuestionId id:resultWithQuestionIdArrayList){
                        Question_id=id.getQuestion_id();
                    }
                    Intent intent=new Intent(context, ViewAnalysisActivity.class);
                    intent.putExtra(Description,result.getDescription());
                    intent.putExtra(QUESTION_ID,Question_id);
                    Log.i(TAG, "onResponse: id.getQuestion_id()::"+Question_id);
                    context.startActivity(new Intent(intent));
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.i(TAG, "onErrorResponse: "+error);
                }
            });
            MyVolley.getInstance(context).addToRequestQueue(gsonRequest);

        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class myViewHolder extends RecyclerView.ViewHolder {
        AdapterViewQuestionBinding binding;
        public myViewHolder(@NonNull AdapterViewQuestionBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }
    }
}
