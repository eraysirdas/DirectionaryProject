package com.eraysirdas.dictionaryproject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {
    private static final int SPLASH_DURATION = 2000; // 2 saniye


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);
        // Handler ile Splash ekranında bekleme süresi ekle
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Firebase ile giriş yapma kontrolü
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    // Eğer kullanıcı zaten giriş yaptıysa, doğrudan MainActivity'ye geçiş yap
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    // Eğer kullanıcı giriş yapmamışsa, giriş yapma ekranını göster
                    Intent intent = new Intent(SplashActivity.this, MainSignActivity.class);
                    startActivity(intent);
                }
                finish(); // Geçişten sonra mevcut aktiviteyi kapat
            }
        }, SPLASH_DURATION); // 2000 milisaniye sonra geçiş yap

    }
}