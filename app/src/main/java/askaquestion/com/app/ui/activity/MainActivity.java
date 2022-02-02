package askaquestion.com.app.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;

import askaquestion.com.app.R;
import askaquestion.com.app.core.sharedprefs.SharedManagerForVolley;
import askaquestion.com.app.databinding.ActivityMainBinding;
import askaquestion.com.app.ui.fragments.AccountFragment;
import askaquestion.com.app.ui.fragments.SettingFragment;
import askaquestion.com.app.ui.fragments.AddQuestionFragment;
import askaquestion.com.app.ui.fragments.ViewQuestionFragment;

public class MainActivity extends AppCompatActivity  {

    ActivityMainBinding binding;
    SharedManagerForVolley managerForVolley;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        managerForVolley=new SharedManagerForVolley(getApplicationContext());

        openFragment(new ViewQuestionFragment());

        binding.bottomNavigation.setOnItemSelectedListener(item -> {

            switch (item.getItemId()){
                case R.id.account:
                    openFragment(new AccountFragment());
                    break;
                case R.id.setting:
                    openFragment(new SettingFragment());
                    break;
                case R.id.addQuestion:
                    openFragment(new AddQuestionFragment());
                    break;
                case R.id.viewQuestion:
                    openFragment(new ViewQuestionFragment());
                    break;
            }
            return true;
        });
    }
    public void openFragment(Fragment fragment){
        FragmentManager manager=getSupportFragmentManager();
        FragmentTransaction transaction=manager.beginTransaction();
        transaction.replace(R.id.fragment_container,fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        moveTaskToBack(true);
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}