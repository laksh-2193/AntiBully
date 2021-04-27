package com.example.antibully;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.WindowManager;


import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;

import java.util.ArrayList;

public class OnBoarding extends AppIntro {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//                getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        addSlide(AppIntroFragment.newInstance("Don't be a victim","You can report incidents live to anyone you wish to",R.drawable.onboard_bully_1,Color.parseColor("#3498db")));
        addSlide(AppIntroFragment.newInstance("Live GPS","Your GPS location will be shared with your trusted ones",R.drawable.onboard_bully_2,Color.parseColor("#130f40")));
        addSlide(AppIntroFragment.newInstance("Record voice","Voice recording will automatically be enabled to record the voice as a solid proof",R.drawable.onboard_bully_3,Color.parseColor("#be2edd")));
        showSkipButton(true);
        setProgressButtonEnabled(true);




    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        startActivity(new Intent(OnBoarding.this,PhoneNumberInput.class));
        overridePendingTransition(0,0);
        finish();

    }

    @Override
    public void onDonePressed() {
        super.onDonePressed();
        startActivity(new Intent(OnBoarding.this,PhoneNumberInput.class));
        overridePendingTransition(0,0);
        finish();
    }
}
