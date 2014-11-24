package boro.life_cloud.ht.sfc.keio.ac.jp.orf2014;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;

/**
 * Created by yuuki on 14/11/09.
 */
public class OldRingerModeManager {

    private Context context;
    private AudioManager audioManager;

    private NotificationManager notificationManager;
    private Notification.Builder builder;

    private int ringerMode;
    private boolean isEnable;



    public OldRingerModeManager(Context context) {
        this.context = context;

        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        ringerMode = audioManager.getRingerMode();


        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        builder = new Notification.Builder(context)
                .setSmallIcon(android.R.drawable.ic_notification_clear_all);

    }

    public void setNormal() {

        if (ringerMode != AudioManager.RINGER_MODE_NORMAL) {
            ringerMode = AudioManager.RINGER_MODE_NORMAL;
            audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);

            // Activityに画像を表示するように通知
            Intent intent = new Intent("ACTION");
            intent.putExtra("RingerMode", ringerMode);
            context.sendBroadcast(intent);

            builder.setTicker("マナーモードを解除しました");
            notificationManager.notify(1, builder.build());
        }
    }

    public void setSilent() {

        if (ringerMode != AudioManager.RINGER_MODE_VIBRATE) {
            ringerMode = AudioManager.RINGER_MODE_VIBRATE;
            audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);

            // Activityに画像を表示するように通知
            Intent intent = new Intent("ACTION");
            intent.putExtra("RingerMode", ringerMode);
            context.sendBroadcast(intent);

            builder.setTicker("マナーモードに設定しました");
            notificationManager.notify(1, builder.build());
        }
    }

}
