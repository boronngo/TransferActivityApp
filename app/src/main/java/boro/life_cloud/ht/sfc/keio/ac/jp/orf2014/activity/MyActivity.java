package boro.life_cloud.ht.sfc.keio.ac.jp.orf2014.activity;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import boro.life_cloud.ht.sfc.keio.ac.jp.orf2014.R;
import boro.life_cloud.ht.sfc.keio.ac.jp.orf2014.RingerModeView;
import boro.life_cloud.ht.sfc.keio.ac.jp.orf2014.TabListener;
import boro.life_cloud.ht.sfc.keio.ac.jp.orf2014.TransferActivityReceiver;
import boro.life_cloud.ht.sfc.keio.ac.jp.orf2014.fragment.FeatureFragment;
import boro.life_cloud.ht.sfc.keio.ac.jp.orf2014.fragment.TransferActivityFragment;
import boro.life_cloud.ht.sfc.keio.ac.jp.orf2014.service.TransferActivityService;
import boro.life_cloud.ht.sfc.keio.ac.jp.orf2014.service.TransferActivityService.TransferActivityBinder;

public class MyActivity extends Activity {

    private boolean isRunningService = false;

    private RingerModeView ringerModeView;

    private TransferActivityService transferActivityService;
    private TransferActivityReceiver receiver;

    private TransferActivityFragment transferActivityFragment;
    private FeatureFragment featureFragment;

    private List<Double> xList = new ArrayList<Double>();
    private List<Double> yList = new ArrayList<Double>();
    private List<Double> zList = new ArrayList<Double>();

    private String nowTransferActivity = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        setActionBarTab();

        ringerModeView = (RingerModeView) findViewById(R.id.ringer_mode_view1);

        receiver = new TransferActivityReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("ACTION");
        registerReceiver(receiver, intentFilter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        // サービスが動いているかどうかでメニューを切り替える
        if (isRunningService) {
            MenuItem item = menu.findItem(R.id.start_service);
            item.setVisible(false);
            item = menu.findItem(R.id.stop_service);
            item.setVisible(true);
        } else {
            MenuItem item = menu.findItem(R.id.stop_service);
            item.setVisible(false);
            item = menu.findItem(R.id.start_service);
            item.setVisible(true);
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.start_service) {

            Intent intent = new Intent(this, TransferActivityService.class);
            bindService(intent, connection, Context.BIND_AUTO_CREATE);

        } else if (id == R.id.stop_service) {

            unbindService(connection);
            isRunningService = false;

//        } else if (id == R.id.music_service) {
//
//            startService(new Intent(this, MusicService.class));
//
//        } else if (id == R.id.bluetooth_server) {
//
//            BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
//
//            BluetoothServerThread thread = new BluetoothServerThread(this, "aaa", adapter);
//            thread.start();
//
//        } else if (id == R.id.bluetooth_client) {
//
//            transferActivityFragment = (TransferActivityFragment) getFragmentManager().findFragmentByTag("TransferActivityFragment");
//
//            if (transferActivityFragment != null) {
//                transferActivityFragment.addHistory(R.drawable.car);
//            }

        } else if (id == R.id.settings) {

            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);

        }

        return super.onOptionsItemSelected(item);
    }


    public void showRingerModeView(int ringerMode) {

        transferActivityFragment = (TransferActivityFragment) getFragmentManager().findFragmentByTag("TransferActivityFragment");

        if (transferActivityFragment != null) {
            transferActivityFragment.showRingerModeView(ringerMode);
        }

    }

    public void updateTransferActivity(String transferActivity) {

        transferActivityFragment = (TransferActivityFragment) getFragmentManager().findFragmentByTag("TransferActivityFragment");

        // ここどうにかする！
        // なんかnullになってないっぽい

        if (transferActivityFragment != null) {
            try {
                transferActivityFragment.updateTransferActivity(transferActivity);
            } catch (NullPointerException e) {
            }
        }

    }

    public void setNowTransferActivity(String transferActivity) {
        this.nowTransferActivity = transferActivity;
    }

    public void updateAcceleration(double[] acceleration) {

        featureFragment = (FeatureFragment) getFragmentManager().findFragmentByTag("FeatureFragment");

        if (featureFragment != null) {
            featureFragment.updateAcceleration(acceleration);
        }

    }

    public void updateFeatureVector(double[] featureVector) {

        featureFragment = (FeatureFragment) getFragmentManager().findFragmentByTag("FeatureFragment");

        if (featureFragment != null) {
            featureFragment.updateFeatureVector(featureVector);
        }

    }

    private void setActionBarTab() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        Tab tab = actionBar.newTab()
                .setText("ACTIVITY")
                .setTabListener(new TabListener<TransferActivityFragment>(
                        this, "TransferActivityFragment", TransferActivityFragment.class
                ));
        actionBar.addTab(tab);

        tab = actionBar.newTab()
                .setText("FEATURE")
                .setTabListener(new TabListener<FeatureFragment>(
                        this, "FeatureFragment", FeatureFragment.class
                ));
        actionBar.addTab(tab);
    }


    // TransferActivityServiceを繋ぐconnection
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            TransferActivityBinder binder = (TransferActivityBinder) iBinder;
            transferActivityService = binder.getService();
            isRunningService = true;
        }

        // unbindするだけでは呼ばれない。
        // Serviceが強制終了した時などに呼ばれる。
        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            isRunningService = false;
        }
    };
}
