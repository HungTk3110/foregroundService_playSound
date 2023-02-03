package com.example.service_sample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.service_sample.model.Song;

public class MainActivity extends AppCompatActivity {
    Button btnStartService, btnStopService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnStartService = findViewById(R.id.buttonStartService);
        btnStopService = findViewById(R.id.buttonStopService);
        btnStartService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startService();
            }
        });
        btnStopService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopService();
            }
        });
    }
    public void startService() {
        Song song = new Song("Đau Để Trưởng Thành","OnlyC",R.drawable.img , R.raw.mp3);

        Intent serviceIntent = new Intent(this, MyService.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("object_song",song);

        serviceIntent.putExtras(bundle);
        ContextCompat.startForegroundService(this, serviceIntent);
    }
    public void stopService() {
        Intent serviceIntent = new Intent(this, MyService.class);
        stopService(serviceIntent);
    }
}