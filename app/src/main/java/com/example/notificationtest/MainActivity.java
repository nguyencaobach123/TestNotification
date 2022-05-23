package com.example.notificationtest;

import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.notificationtest.services.OnClearFromRecentService;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity{
    ArrayList<TrackFiles> trackFiles = new ArrayList<>();
    ImageView next, prev, play;
    TextView title;
    Intent openNoti;
    private int position = 0;
    private int playButton;
    private boolean isPlaying = false;
    NotificationManager notificationManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        generateFiles();
        createNotificationChannel();
        registerReceiver(broadcastReceiver, new IntentFilter("TRACKS_TRACKS"));
        openNoti = new Intent(getBaseContext(), OnClearFromRecentService.class);
        startService(openNoti);
        next = findViewById(R.id.next);
        prev = findViewById(R.id.previous);
        play = findViewById(R.id.play);
        title = findViewById(R.id.titleSong);
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prevClicked();
            }
        });
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playClicked();
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextClicked();
            }
        });
    }

    private void generateFiles() {
        TrackFiles trackFile = new TrackFiles("Song 1", "Artist 1", R.drawable.hongnhung);
        trackFiles.add(trackFile);
        TrackFiles trackFile1 = new TrackFiles("Song 2", "Artist 2", R.drawable.pentatonix);
        trackFiles.add(trackFile1);
        TrackFiles trackFile2 = new TrackFiles("Song 3", "Artist 3", R.drawable.thebeatles);
        trackFiles.add(trackFile2);
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Show Notification";
            String description = "This allows you to show notification";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CreateNotification.CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getExtras().getString("actionname");
            switch (action) {
                case CreateNotification.ACTION_PREV:
                    prevClicked();
                    break;
                case CreateNotification.ACTION_PLAY:
                    playClicked();
                    break;
                case CreateNotification.ACTION_NEXT:
                    nextClicked();
                    break;
            }
        }
    };
    public void nextClicked() {
        if (position == trackFiles.size() - 1) {
            position = 0;
        }
        else {
            position++;
        }
        title.setText(trackFiles.get(position).getTitle());
        CreateNotification.createNotification(this, trackFiles.get(position), playButton);
    }

    public void prevClicked() {
        if (position == 0) {
            position = trackFiles.size() - 1;
        }
        else {
            position--;
        }
        title.setText(trackFiles.get(position).getTitle());
        CreateNotification.createNotification(this, trackFiles.get(position), playButton);
    }

    public void playClicked() {
        isPlaying = !isPlaying;
        if (isPlaying) {
            playButton = R.drawable.ic_baseline_pause_24;
        }
        else {
            playButton = R.drawable.ic_baseline_play_arrow_24;
        }
        play.setImageResource(playButton);
        title.setText(trackFiles.get(position).getTitle());
        CreateNotification.createNotification(this, trackFiles.get(position), playButton);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//
//        }
        stopService(openNoti);
        notificationManager.cancel(1);
        unregisterReceiver(broadcastReceiver);
    }
}