package com.example.enlight;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.paolorotolo.appintro.AppIntro;

public class IntroActivity extends AppIntro {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        addSlide(IntroSlide.newInstance(R.layout.activity_walkthrough1));
        addSlide(IntroSlide.newInstance(R.layout.activity_walkthrough2));
        addSlide(IntroSlide.newInstance(R.layout.activity_walkthrough3));

        showSkipButton(true);
        setProgressButtonEnabled(true);

        showSeparator(false);

        setImageNextButton(getDrawable(R.drawable.ic_navigate_next_black_24dp));
        setSkipText("Skip");
        setColorSkipButton(Color.BLACK);
        setSkipTextTypeface(R.font.productb);
        setDoneText("Get Started");
        setColorDoneText(Color.BLACK);
        setDoneTextTypeface(R.font.productb);

    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        Intent intent = new Intent(IntroActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        Intent intent = new Intent(IntroActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
        // Do something when the slide changes.
    }
}
