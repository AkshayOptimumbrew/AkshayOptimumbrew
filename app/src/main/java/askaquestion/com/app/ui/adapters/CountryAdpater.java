package askaquestion.com.app.ui.adapters;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import askaquestion.com.app.core.interfaces.ForCountry;
import askaquestion.com.app.databinding.AdapterCountryBinding;
import askaquestion.com.app.core.pojo.adapter_model.CountryName;

public class CountryAdpater extends RecyclerView.Adapter<CountryAdpater.MyViewHolder> {
    private static final String TAG = "CountryAdapter";
    String arrayData;
    ArrayList<String> arrayListUser = new ArrayList<>();
    SparseBooleanArray checkBoxStateArray = new SparseBooleanArray();
    Context context;
    List<CountryName> itemList;
    List<CountryName> mFilteredList;
    public ForCountry onclickInterface;
    ArrayList<CountryName> selected = new ArrayList<>();

    public CountryAdpater(Context context, ArrayList<CountryName> itemList, ForCountry onclickInterface) {
        this.context = context;
        this.onclickInterface = onclickInterface;
        this.itemList = itemList;
        this.mFilteredList = itemList;
    }

    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        AdapterCountryBinding binding=AdapterCountryBinding.inflate(LayoutInflater.from(context),parent,false);
        return new MyViewHolder(binding);
    }

    public void selectAll() {
        selected.addAll(itemList);
        notifyDataSetChanged();
    }

    public void clearAll() {
        selected.clear();
        notifyDataSetChanged();
    }

    public void onBindViewHolder(MyViewHolder holder, int position) {
        getData();
        CountryName countryName = itemList.get(position);
        holder.binding.textCountry.setText(countryName.getCountry());
        if (selected.contains(countryName)) {
            holder.binding.checkbox.setChecked(true);
            return;
        }
        holder.binding.checkbox.setChecked(false);

        holder.binding.checkbox.setOnClickListener(view1 -> {
            if (!checkBoxStateArray.get(position, false)) {

                holder.binding.checkbox.setChecked(true);
                checkBoxStateArray.put(position, true);

                for (int i = 0; i < itemList.size(); i++) {
                    if (!arrayListUser.contains(itemList.get(position).getCountry())) {
                        arrayListUser.add(itemList.get(position).getCountry());
                        saveData();
                    }
                    arrayData = arrayListUser.toString().replace("[", "").replace("]", "").trim();
                }
                onclickInterface.setClick(view1, itemList.get(holder.getAbsoluteAdapterPosition()), arrayData);
                return;
            }
            holder.binding.checkbox.setChecked(false);
            checkBoxStateArray.put(position, false);
            for (int i2 = 0; i2 < itemList.size(); i2++) {
                arrayListUser.remove(itemList.get(position).getCountry());
                arrayData = arrayListUser.toString().replace("[", "").replace("]", "");
            }
            onclickInterface.setClick(view1, itemList.get(holder.getAbsoluteAdapterPosition()), arrayData);
        });
    }
    public void saveData() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("Message", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(arrayListUser);

        Log.i(TAG, "saveData:  json" + json);
        editor.putString("List", json);
        editor.apply();
    }


    public ArrayList<String> getData() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("Message", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("List", null);
        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();
        ArrayList<String> arrayList = gson.fromJson(json, type);
        arrayListUser = arrayList;
        if (arrayList == null) {
            arrayListUser = new ArrayList<>();
        }
        return arrayListUser;
    }
    public int getItemCount() {
        return itemList.size();
    }

    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public void setFilter(ArrayList<CountryName> newList) {
        ArrayList arrayList = new ArrayList();
        mFilteredList = arrayList;
        itemList = newList;
        arrayList.addAll(newList);
        notifyDataSetChanged();
    }
    public void filterList(ArrayList<CountryName> filterList){
        itemList=filterList;
        notifyDataSetChanged();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        AdapterCountryBinding binding;
        public MyViewHolder(AdapterCountryBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
            
        }
    }
}
