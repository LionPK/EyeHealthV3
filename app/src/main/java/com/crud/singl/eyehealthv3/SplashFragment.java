package com.crud.singl.eyehealthv3;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

/**
 * @Copyright by Mr.Praneed Klanboon
 * Email: Praneed.kla@northbkk.ac.th
 * */
public class SplashFragment extends Activity implements Animation.AnimationListener{
    Animation animatorFadeIn;
    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle saveInstanceState) {

        super.onCreate(saveInstanceState);
        setContentView(R.layout.eye_splash_fragment);

        if(Build.VERSION.SDK_INT < 22){
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }else {
            View decorView = getWindow().getDecorView();

            // Hind status bar
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
            // Please remember, you couldn't show status bar if you want to show logo flash screen

        }
        // Load animation picture
        animatorFadeIn = AnimationUtils.loadAnimation(getApplicationContext(),
                R.animator.eye_animation_fade_in);

        // Config how to load animator picture
        animatorFadeIn.setAnimationListener(this);
        // Animation for pictures
        linearLayout = (LinearLayout) findViewById(R.id.layout_linear);
        // Start to display animator pictures
        linearLayout.setVisibility(View.VISIBLE);
        linearLayout.startAnimation(animatorFadeIn);
    }

    @Override
    public  void onBackPressed(){
        this.finish();
        super.onBackPressed();
    }

    @Override
    public void onAnimationStart(Animation animation) {
        // Add code under activities used
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        // When program doing this method. System will call to WelcomeActivity
        Intent i = new Intent(SplashFragment.this, SignInActivity.class);
        startActivity(i);
        this.finish();
    }

    @Override
    public void onAnimationRepeat(Animation animation) {
        // Add code under activities used
    }
}
