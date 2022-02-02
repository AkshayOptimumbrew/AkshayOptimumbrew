package askaquestion.com.app.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import askaquestion.com.app.databinding.AdapterAnalysisGenderyBinding;
import askaquestion.com.app.core.pojo.analysisgender.ResultGender;

public class AnalysisGenderAdapter extends RecyclerView.Adapter<AnalysisGenderAdapter.myViewHolder> {

    private Context context;
    private ArrayList<ResultGender> resultGenderArrayList;

    public AnalysisGenderAdapter(Context context, ArrayList<ResultGender> resultGenderArrayList) {
        this.context = context;
        this.resultGenderArrayList = resultGenderArrayList;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AdapterAnalysisGenderyBinding binding = AdapterAnalysisGenderyBinding.inflate(LayoutInflater.from(context), parent, false);
        return new myViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {

       ResultGender resultGender=resultGenderArrayList.get(position);
       holder.binding.textOption1.setText(String.valueOf(resultGender.getOption1()));
       holder.binding.textOption2.setText(String.valueOf(resultGender.getOption2()));
    }

    @Override
    public int getItemCount() {
        return resultGenderArrayList.size();
    }

    public static class myViewHolder extends RecyclerView.ViewHolder {
        AdapterAnalysisGenderyBinding binding;

        public myViewHolder(@NonNull AdapterAnalysisGenderyBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }

}
