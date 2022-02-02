package askaquestion.com.app.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import askaquestion.com.app.databinding.AdapterAnalysisCountryBinding;
import askaquestion.com.app.core.pojo.analysiscountry.ResultAnalysisCountry;

public class AnalysisCountryAdapter extends RecyclerView.Adapter<AnalysisCountryAdapter.myViewHolder>{
    private ArrayList<ResultAnalysisCountry> resultAnalysisCountryArrayList;
    private Context context;

    public AnalysisCountryAdapter(ArrayList<ResultAnalysisCountry> resultAnalysisCountryArrayList, Context context) {
        this.resultAnalysisCountryArrayList = resultAnalysisCountryArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AdapterAnalysisCountryBinding binding=AdapterAnalysisCountryBinding.inflate(LayoutInflater.from(context),parent,false);
        return new myViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        ResultAnalysisCountry result=resultAnalysisCountryArrayList.get(position);
        holder.binding.textCountry.setText(result.getCountry());
        holder.binding.textOption1.setText(result.getOption1());
        holder.binding.textOption2.setText(result.getOption2());
    }

    @Override
    public int getItemCount() {
        return resultAnalysisCountryArrayList.size();
    }

    public static class myViewHolder extends RecyclerView.ViewHolder {
        AdapterAnalysisCountryBinding binding;
        public myViewHolder(@NonNull AdapterAnalysisCountryBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }
    }
}
