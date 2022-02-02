package askaquestion.com.app.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import askaquestion.com.app.databinding.AdapterAnalysisNoneBinding;
import askaquestion.com.app.core.pojo.noneanalysis.ResultNone;

public class AnalysisNoneAdapter extends RecyclerView.Adapter<AnalysisNoneAdapter.myViewHolder>{
    private ArrayList<ResultNone> resultNoneArrayList;
    private Context context;

    public AnalysisNoneAdapter(ArrayList<ResultNone> resultNoneArrayList, Context context) {
        this.resultNoneArrayList = resultNoneArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AdapterAnalysisNoneBinding binding=AdapterAnalysisNoneBinding.inflate(LayoutInflater.from(context),parent,false);
        return new myViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        ResultNone resultNone=resultNoneArrayList.get(position);

        holder.binding.textOption1.setText(String.valueOf(resultNone.getOption1()));
        holder.binding.textOption2.setText(String.valueOf(resultNone.getOption2()));
    }

    @Override
    public int getItemCount() {
        return resultNoneArrayList.size();
    }

    public static class myViewHolder extends RecyclerView.ViewHolder {
        AdapterAnalysisNoneBinding binding;
        public myViewHolder(AdapterAnalysisNoneBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }
    }
}
