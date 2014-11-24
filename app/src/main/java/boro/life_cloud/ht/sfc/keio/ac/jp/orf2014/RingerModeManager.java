package boro.life_cloud.ht.sfc.keio.ac.jp.orf2014;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;

/**
 * Created by yuuki on 14/11/11.
 */
public class RingerModeManager {

    private static RingerModeManager ringerModeManager = new RingerModeManager();

    private Context context;
    private AudioManager audioManager;
    private NotificationManager notificationManager;
    private Notification.Builder builder;

    private int nowRingerMode;
    public final static int RINGER_MODE_NORMAL = AudioManager.RINGER_MODE_NORMAL;
    public final static int RINGER_MODE_VIBRATE = AudioManager.RINGER_MODE_VIBRATE;
    public final static int RINGER_MODE_SILENT = AudioManager.RINGER_MODE_SILENT;


    private RingerModeManager() {
        context = App.getInstance();

        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        nowRingerMode = audioManager.getRingerMode();

        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        builder = new Notification.Builder(context)
                .setSmallIcon(android.R.drawable.ic_notification_clear_all);

    }

    public static RingerModeManager getInstance() {
        return ringerModeManager;
    }

    public void setRingerMode(int ringerMode) {

        Intent intent = new Intent("ACTION");
        intent.putExtra("RingerMode", ringerMode);

        switch (ringerMode) {

            case RINGER_MODE_NORMAL:

                if(nowRingerMode != RINGER_MODE_NORMAL) {
                    nowRingerMode = RINGER_MODE_NORMAL;
                    audioManager.setRingerMode(RINGER_MODE_NORMAL);
                    builder.setTicker("マナーモードを解除しました");
                    notificationManager.notify(1, builder.build());
                    context.sendBroadcast(intent);
                }

                break;

            case RINGER_MODE_VIBRATE:

                if(nowRingerMode != RINGER_MODE_VIBRATE) {
                    nowRingerMode = RINGER_MODE_VIBRATE;
                    audioManager.setRingerMode(RINGER_MODE_VIBRATE);
                    builder.setTicker("マナーモードに設定しました");
                    notificationManager.notify(1, builder.build());
                    context.sendBroadcast(intent);
                }

                break;

            case RINGER_MODE_SILENT:

                if(nowRingerMode != RINGER_MODE_SILENT) {
                    nowRingerMode = RINGER_MODE_SILENT;
                    audioManager.setRingerMode(RINGER_MODE_SILENT);
                    builder.setTicker("サイレントマナーに設定しました");
                    notificationManager.notify(1, builder.build());
                    context.sendBroadcast(intent);
                }

                break;

        }

    }

}
