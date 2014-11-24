package boro.life_cloud.ht.sfc.keio.ac.jp.orf2014.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import boro.life_cloud.ht.sfc.keio.ac.jp.orf2014.R;


/**
 * Created by boro on 14/10/30.
 */
public class MusicService extends Service {

    private MediaPlayer mediaPlayer;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println("onCreate");

        mediaPlayer = MediaPlayer.create(this, R.raw.bgm0);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        System.out.println("onStartCommand");



        if(mediaPlayer!=null){
            mediaPlayer.start();
        }

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("onDestroy");

        mediaPlayer.release();

    }
}
