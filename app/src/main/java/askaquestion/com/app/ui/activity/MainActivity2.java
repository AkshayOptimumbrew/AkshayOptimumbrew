package askaquestion.com.app.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

import askaquestion.com.app.R;
import askaquestion.com.app.databinding.ActivityMain2Binding;

public class MainActivity2 extends AppCompatActivity {

    ActivityMain2Binding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMain2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String e1 = binding.et1.getText().toString();
        String e2 = binding.et2.getText().toString();
        String e3 = binding.et3.getText().toString();
        String e4 = binding.et4.getText().toString();

        binding.et1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                binding.et2.requestFocus();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.et2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.et3.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.et3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.et4.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
       /* if(e1.length()==1){

        }else if(e2.length()==1){
            binding.et3.requestFocus();
        }else if(e3.length()==1){
            binding.et4.requestFocus();
        }else{
            Log.i("TAG", "onCreate: ");
        }*/
    }
}