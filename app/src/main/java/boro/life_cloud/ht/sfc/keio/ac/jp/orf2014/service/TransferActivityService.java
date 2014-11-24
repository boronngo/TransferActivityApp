package boro.life_cloud.ht.sfc.keio.ac.jp.orf2014.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.preference.PreferenceManager;

import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;

import boro.life_cloud.ht.sfc.keio.ac.jp.orf2014.R;
import boro.life_cloud.ht.sfc.keio.ac.jp.orf2014.RingerModeManager;
import boro.life_cloud.ht.sfc.keio.ac.jp.orf2014.transfer_activity.FeatureExtractor;
import boro.life_cloud.ht.sfc.keio.ac.jp.orf2014.transfer_activity.TransferActivityClassifier;

public class TransferActivityService extends Service implements SensorEventListener {

    private final IBinder binder = new TransferActivityBinder();

    private SensorManager sensorManager;
    private Sensor accelerometer;

    private ArrayList<Double> xList = new ArrayList<Double>();
    private ArrayList<Double> yList = new ArrayList<Double>();
    private ArrayList<Double> zList = new ArrayList<Double>();

    private TransferActivityClassifier classifier = new TransferActivityClassifier();
    private RingerModeManager ringerModeManager;
    private SharedPreferences preferences;

    private MediaPlayer mp;

    public class TransferActivityBinder extends Binder {

        public TransferActivityService getService() {
            return TransferActivityService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();

        ringerModeManager = RingerModeManager.getInstance();

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

    }

    @Override
    public IBinder onBind(Intent intent) {
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_FASTEST);
        return binder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        sensorManager.unregisterListener(this);
        return super.onUnbind(intent);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        xList.add((double) event.values[0]);
        yList.add((double) event.values[1]);
        zList.add((double) event.values[2]);


        final double[] acceleration = {
                event.values[0], event.values[1], event.values[2]
        };


        Intent intent = new Intent("ACTION");
        intent.putExtra("Acceleration", acceleration);

        if (xList.size() >= 128) {
            // 特徴量の抽出
            ArrayList<Double> featureVector = FeatureExtractor.getfeaturevector(xList, yList, zList, "TD");

            intent.putExtra("FeatureVector", ArrayUtils.toPrimitive(featureVector.toArray(new Double[featureVector.size()])));


            // 移動手段を判定する
            String transferActivity = classifier.getTransferActivity(featureVector);


            // 移動手段によってどうするかを記述
            if (transferActivity.equals("Bicycle")) {

                ringerModeManager.setRingerMode(Integer.parseInt(preferences.getString("ringer_mode_bicycle", "")));
                releaseMediaPlayer();

            } else if (transferActivity.equals("Bus")) {

                ringerModeManager.setRingerMode(Integer.parseInt(preferences.getString("ringer_mode_bus", "")));
                releaseMediaPlayer();

            } else if (transferActivity.equals("Car")) {

                ringerModeManager.setRingerMode(Integer.parseInt(preferences.getString("ringer_mode_car", "")));
                releaseMediaPlayer();

            } else if (transferActivity.equals("Other")) {

                ringerModeManager.setRingerMode(Integer.parseInt(preferences.getString("ringer_mode_other", "")));
                releaseMediaPlayer();

            } else if (transferActivity.equals("Run")) {

                ringerModeManager.setRingerMode(Integer.parseInt(preferences.getString("ringer_mode_run", "")));
                mp = MediaPlayer.create(this, R.raw.bgm0);

            } else if (transferActivity.equals("Train")) {

                ringerModeManager.setRingerMode(Integer.parseInt(preferences.getString("ringer_mode_train", "")));
                releaseMediaPlayer();

            } else if (transferActivity.equals("Walk")) {

                ringerModeManager.setRingerMode(Integer.parseInt(preferences.getString("ringer_mode_walk", "")));
                releaseMediaPlayer();

            }


            // ArrayListを初期化
            xList = new ArrayList<Double>();
            yList = new ArrayList<Double>();
            zList = new ArrayList<Double>();

            intent.putExtra("TransferActivity", transferActivity);

        }

        sendBroadcast(intent);

    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    private void releaseMediaPlayer() {
        if (mp != null) {
            mp.release();
        }
    }

}
