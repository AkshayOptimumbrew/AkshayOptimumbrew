package askaquestion.com.app.ui.activity;

import static askaquestion.com.app.core.constants.Extras.Category_ID;
import static askaquestion.com.app.core.constants.Extras.Country;
import static askaquestion.com.app.core.constants.Extras.Description;
import static askaquestion.com.app.core.constants.Extras.Gender;
import static askaquestion.com.app.core.constants.Extras.Option1;
import static askaquestion.com.app.core.constants.Extras.Option1Image;
import static askaquestion.com.app.core.constants.Extras.Option2;
import static askaquestion.com.app.core.constants.Extras.Option2Image;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;

import askaquestion.com.app.databinding.ActivityViewQuestionBinding;

public class ViewQuestionActivity extends AppCompatActivity {

    ActivityViewQuestionBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewQuestionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        int option_type = getIntent().getIntExtra("Option_Type", 0);

        if (getIntent().getStringExtra(Gender).equals("1")) {
            binding.textGender.setText("Female");
        } else if (getIntent().getStringExtra(Gender).equals("2")) {
            binding.textGender.setText("Male");
        }
        if (getIntent().getStringExtra(Category_ID).equals("2")) {
            binding.textCategory.setText("Pharmacy");
        } else if (getIntent().getStringExtra(Category_ID).equals("3")) {
            binding.textCategory.setText("Medical");
        } else if (getIntent().getStringExtra(Category_ID).equals("4")) {
            binding.textCategory.setText("Logical");
        } else if (getIntent().getStringExtra(Category_ID).equals("5")) {
            binding.textCategory.setText("Car");
        }

        binding.textCountry.setText(getIntent().getStringExtra(Country));

        if (option_type == 1) {
            if(getIntent().getStringExtra(Option1).endsWith(".png")){
                binding.textLL.setVisibility(View.GONE);
                binding.imageRL.setVisibility(View.VISIBLE);
                binding.textDescription.setText(getIntent().getStringExtra(Description));
                Glide.with(getApplicationContext()).load(getIntent().getStringExtra(Option1)).into(binding.imageOption1ViewQuestion);
                Glide.with(getApplicationContext()).load(getIntent().getStringExtra(Option2)).into(binding.imageOption2ViewQuestion);
            }else{

                binding.textLL.setVisibility(View.VISIBLE);
                binding.imageRL.setVisibility(View.GONE);
                binding.textDescription.setText(getIntent().getStringExtra(Description));
                binding.textOption1.setText(getIntent().getStringExtra(Option1));
                binding.textOption2.setText(getIntent().getStringExtra(Option2));
            }


        }
        if (option_type == 2) {
            binding.textLL.setVisibility(View.GONE);
            binding.imageRL.setVisibility(View.VISIBLE);
            binding.textDescription.setText(getIntent().getStringExtra(Description));
            Glide.with(getApplicationContext()).load(getIntent().getStringExtra(Option1Image)).into(binding.imageOption1ViewQuestion);
            Glide.with(getApplicationContext()).load(getIntent().getStringExtra(Option2Image)).into(binding.imageOption2ViewQuestion);
        }
        binding.buttonCancel.setOnClickListener(v -> finish());

    }
}