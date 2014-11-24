package boro.life_cloud.ht.sfc.keio.ac.jp.orf2014.fragment;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import boro.life_cloud.ht.sfc.keio.ac.jp.orf2014.R;

/**
 * Created by boro on 14/11/10.
 */
public class SettingsFragment extends PreferenceFragment implements OnSharedPreferenceChangeListener {

    private SharedPreferences preferences;

    private ListPreference bicyclePreference;
    private ListPreference busPreference;
    private ListPreference carPreference;
    private ListPreference otherPreference;
    private ListPreference runPreference;
    private ListPreference trainPreference;
    private ListPreference walkPreference;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference);

        preferences = getPreferenceScreen().getSharedPreferences();
        bicyclePreference = (ListPreference) getPreferenceScreen().findPreference("ringer_mode_bicycle");
        busPreference = (ListPreference) getPreferenceScreen().findPreference("ringer_mode_bus");
        carPreference = (ListPreference) getPreferenceScreen().findPreference("ringer_mode_car");
        otherPreference = (ListPreference) getPreferenceScreen().findPreference("ringer_mode_other");
        runPreference = (ListPreference) getPreferenceScreen().findPreference("ringer_mode_run");
        trainPreference = (ListPreference) getPreferenceScreen().findPreference("ringer_mode_train");
        walkPreference = (ListPreference) getPreferenceScreen().findPreference("ringer_mode_walk");

    }

    @Override
    public void onResume() {
        super.onResume();
        preferences.registerOnSharedPreferenceChangeListener(this);
        updatePreferenceSummary();
    }

    @Override
    public void onPause() {
        super.onPause();
        preferences.unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        updatePreferenceSummary();
    }

    private void updatePreferenceSummary() {
        bicyclePreference.setSummary(bicyclePreference.getEntry());
        busPreference.setSummary(busPreference.getEntry());
        carPreference.setSummary(carPreference.getEntry());
        otherPreference.setSummary(otherPreference.getEntry());
        runPreference.setSummary(runPreference.getEntry());
        trainPreference.setSummary(trainPreference.getEntry());
        walkPreference.setSummary(walkPreference.getEntry());
    }

}
