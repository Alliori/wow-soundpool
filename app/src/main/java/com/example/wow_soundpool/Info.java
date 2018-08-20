package com.example.wow_soundpool;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

public class Info extends AppCompatActivity {

    private static int TIMER = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(Info.this, MainActivity.class);
                startActivity(intent);
                Runtime.getRuntime().gc();
                finish();
            }
        }, TIMER);
    }
}

