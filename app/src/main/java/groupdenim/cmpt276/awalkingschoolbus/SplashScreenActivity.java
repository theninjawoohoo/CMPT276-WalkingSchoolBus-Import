package groupdenim.cmpt276.awalkingschoolbus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import static android.system.Os.kill;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        //displayAnimation();
        setAnimationListener();

    }

    private void setAnimationListener() {
        ImageView logo = findViewById(R.id.logo);
        Animation fade = AnimationUtils.loadAnimation(this,R.anim.fade);
        logo.startAnimation(fade);
        fade.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void displayAnimation() {
        ImageView logo = findViewById(R.id.logo);
        Animation fade = AnimationUtils.loadAnimation(this,R.anim.fade);
        logo.startAnimation(fade);
    }
}
