package askaquestion.com.app.ui.activity;

import static askaquestion.com.app.core.constants.Extras.EXTRA_OPTION1;
import static askaquestion.com.app.core.constants.Extras.EXTRA_OPTION1_IMAGE;
import static askaquestion.com.app.core.constants.Extras.EXTRA_OPTION2;
import static askaquestion.com.app.core.constants.Extras.EXTRA_OPTION2_IMAGE;
import static askaquestion.com.app.core.constants.Extras.EXTRA_OPTION_TYPE;
import static askaquestion.com.app.core.constants.Extras.EXTRA_QUESTION;
import static askaquestion.com.app.core.constants.Extras.EXTRA_QUESTION_FOR_IMAGE;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;

import askaquestion.com.app.databinding.ActivityPreviewBinding;

public class PreviewActivity extends AppCompatActivity {

    ActivityPreviewBinding binding;

    String TAG="PreviewActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityPreviewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        int option_type=getIntent().getIntExtra(EXTRA_OPTION_TYPE,0);
        Log.i(TAG, "onCreate: ot::"+option_type);
        if(option_type==1){
            binding.imageRL.setVisibility(View.GONE);
            binding.textLL.setVisibility(View.VISIBLE);
            String question=getIntent().getStringExtra(EXTRA_QUESTION);
            String option1=getIntent().getStringExtra(EXTRA_OPTION1);
            String option2=getIntent().getStringExtra(EXTRA_OPTION2);

            binding.textDescription.setText(question);
            binding.editOption1.setText(option1);
            binding.editOption2.setText(option2);
        }
        if(option_type==2){
            binding.textLL.setVisibility(View.GONE);
            binding.imageRL.setVisibility(View.VISIBLE);
            String question_Image=getIntent().getStringExtra(EXTRA_QUESTION_FOR_IMAGE);
            String option1_Image=getIntent().getStringExtra(EXTRA_OPTION1_IMAGE);
            String option2_Image=getIntent().getStringExtra(EXTRA_OPTION2_IMAGE);

            Log.i(TAG, "onCreate: "+option1_Image+"--"+option2_Image);
            binding.textDescription.setText(question_Image);
            Glide.with(getApplicationContext()).load(option1_Image).into(binding.imageOption1ViewQuestion);
            Glide.with(getApplicationContext()).load(option2_Image).into(binding.imageOption2ViewQuestion);
        }


        binding.buttonCancel.setOnClickListener(v ->{
            finish();
        });
    }
}