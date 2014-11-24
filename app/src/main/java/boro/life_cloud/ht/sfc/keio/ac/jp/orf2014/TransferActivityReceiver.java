package boro.life_cloud.ht.sfc.keio.ac.jp.orf2014;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Arrays;

import boro.life_cloud.ht.sfc.keio.ac.jp.orf2014.activity.MyActivity;
import boro.life_cloud.ht.sfc.keio.ac.jp.orf2014.fragment.TransferActivityFragment;

/**
 * Created by yuuki on 14/11/02.
 */
public class TransferActivityReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        MyActivity myActivity = (MyActivity) context;

        if (intent.hasExtra("TransferActivity")) {

            String transferActivity = intent.getStringExtra("TransferActivity");
            myActivity.updateTransferActivity(transferActivity);

        }

        if (intent.hasExtra("RingerMode")) {

            int ringerMode = intent.getIntExtra("RingerMode", 0);
            myActivity.showRingerModeView(ringerMode);

        }

        if (intent.hasExtra("Acceleration")) {

            double[] acceleration = intent.getDoubleArrayExtra("Acceleration");
            myActivity.updateAcceleration(acceleration);

        }

        if (intent.hasExtra("FeatureVector")) {
            double[] featureVector = intent.getDoubleArrayExtra("FeatureVector");
            myActivity.updateFeatureVector(featureVector);

        }


    }
}
