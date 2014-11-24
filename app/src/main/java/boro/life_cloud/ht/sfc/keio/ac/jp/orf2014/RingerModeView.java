package boro.life_cloud.ht.sfc.keio.ac.jp.orf2014;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.media.AudioManager;
import android.util.AttributeSet;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;


/**
 * Created by yuuki on 14/11/02.
 */
public class RingerModeView extends ImageView {

    private Bitmap silentOffImage;
    private Bitmap silentImage;
    private Bitmap silentOffBitmap;
    private Bitmap silentBitmap;

    public RingerModeView(Context context, AttributeSet attrs) {
        super(context, attrs);

        setAlpha(0.8f);

        silentOffImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.silent_mode_off);
        silentImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.silent_mode);

        silentOffBitmap = Bitmap.createBitmap(silentOffImage.getWidth() + 20, silentOffImage.getHeight() + 20, Bitmap.Config.ARGB_8888);
        silentBitmap = Bitmap.createBitmap(silentImage.getWidth() + 20, silentImage.getHeight() + 20, Bitmap.Config.ARGB_8888);

        Canvas canvas1 = new Canvas(silentOffBitmap);
        Paint paint1 = new Paint();
        paint1.setColor(Color.parseColor("#E0E0E0"));
        canvas1.drawRoundRect(new RectF(0, 0, canvas1.getWidth(), canvas1.getHeight()), 10, 10, paint1);
        canvas1.drawBitmap(silentOffImage, 10, 10, paint1);

        Canvas canvas2 = new Canvas(silentBitmap);
        Paint paint2 = new Paint();
        paint2.setColor(Color.parseColor("#E0E0E0"));
        canvas2.drawRoundRect(new RectF(0, 0, canvas2.getWidth(), canvas2.getHeight()), 10, 10, paint2);
        canvas2.drawBitmap(silentImage, 10, 10, paint2);

    }

    public void show(int RingerMode) {
        if (RingerMode == AudioManager.RINGER_MODE_NORMAL) {
            this.setImageBitmap(silentOffBitmap);
        } else if (RingerMode == AudioManager.RINGER_MODE_VIBRATE) {
            this.setImageBitmap(silentBitmap);
        }
        this.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.ringer_mode));
    }
}