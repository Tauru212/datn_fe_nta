package edu.re.estate.presenters;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import edu.re.estate.R;
import edu.re.estate.presenters.tutorial.TutorialActivity;
import edu.re.estate.utils.SessionManager;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        SessionManager.setCities(this);
        SessionManager.refreshUser(this);

        getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.bg_main));
    }

    @Override
    protected void onResume() {
        super.onResume();
        new Thread(() -> {
            try {
                Thread.sleep(5000);
                Intent intent = new Intent(this, TutorialActivity.class);
                startActivity(intent);
                finish();
            } catch (InterruptedException e) {
                Log.e("ERROR", "InterruptedException " + e.getMessage());
            }
        }).start();
    }
}
