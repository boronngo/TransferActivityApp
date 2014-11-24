package boro.life_cloud.ht.sfc.keio.ac.jp.orf2014.activity;

import android.app.Activity;
import android.os.Bundle;

import boro.life_cloud.ht.sfc.keio.ac.jp.orf2014.R;

public class SettingsActivity extends Activity {

    private final String KEY_PREF_AUTO_RINGER_MODE = "is_auto_ringer_mode";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
    }

}
