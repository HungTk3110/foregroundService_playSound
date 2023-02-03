package com.example.service_sample;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.media.session.MediaSessionCompat;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import com.example.service_sample.model.Song;


public class MyService extends Service {

    private static final int ACTION_PAUSE = 1;
    private static final int ACTION_RESUME = 2;
    private boolean isPlaying;
    private MediaPlayer mediaPlayer;
    private Song mSong;
    public static final String CHANNEL_ID = "ForegroundServiceChannel";

    @Override
    public void onCreate() {
        super.onCreate();
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Bundle bundle = intent.getExtras();
        Song song = (Song) bundle.get("object_song");
        if(song != null){
            mSong = song ;
            startMusic(song);
            sendNotification(song);
        }
        createNotificationChannel();

        int actionMusic = intent.getIntExtra("action_music_service" , 0);
        handleACtionMusic(actionMusic);

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mediaPlayer != null){
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            serviceChannel.setSound(null,null);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }

    private void sendNotification(Song song) {

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), song.getImg());

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_small_music)
                .setSubText("TKH_MP3_Player")
                .setContentTitle(song.getTitle())
                .setContentText(song.getSingle())
                .setLargeIcon(bitmap)
                // Apply the media style template
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                        .setShowActionsInCompactView( 0 , 1 , 2 /* #1: pause button */)
                        .setMediaSession(new MediaSessionCompat(this , "tag").getSessionToken()));
        if(isPlaying){
            notificationBuilder.addAction(R.drawable.ic_baseline_skip_previous_24, "Previous", null) // #0
                    .addAction(R.drawable.ic_baseline_pause_24, "Pause", getPendingIntent(this , ACTION_PAUSE))  // #1
                    .addAction(R.drawable.ic_baseline_skip_next_24, "Next", null) ;    // #2
        }
        else {
            notificationBuilder.addAction(R.drawable.ic_baseline_skip_previous_24, "Previous", null) // #0
                    .addAction(R.drawable.ic_baseline_play_arrow_24, "Pause", getPendingIntent(this , ACTION_RESUME))  // #1
                    .addAction(R.drawable.ic_baseline_skip_next_24, "Next", null)   ;  // #2
        }
        Notification notification = notificationBuilder.build();
        startForeground(1, notification);
    }

    private PendingIntent getPendingIntent(Context context , int action){
        Intent intent = new Intent(this  , MyReceiver.class);
        intent.putExtra("action_music",action);

        return PendingIntent.getBroadcast(context.getApplicationContext() , action , intent , PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private void startMusic(Song song) {
        mediaPlayer = MediaPlayer.create(getApplicationContext() ,song.getResource());
        mediaPlayer.start();
        isPlaying = true;
    }

    private void handleACtionMusic(int action){
        switch (action){
            case ACTION_PAUSE:
                pauseMusic();
                break;
            case ACTION_RESUME:
                resumeMusic();
                break;
        }
    }

    private void pauseMusic(){
        if(mediaPlayer != null && isPlaying){
            mediaPlayer.pause();
            isPlaying = false;
            sendNotification(mSong);
        }
    }

    private void resumeMusic(){
        if(mediaPlayer != null &&!isPlaying){
            mediaPlayer.start();
            isPlaying = true;
            sendNotification(mSong);
        }
    }
}
