package boro.life_cloud.ht.sfc.keio.ac.jp.orf2014;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;

/**
 * Created by yuuki on 14/11/16.
 */
public class TabListener<T extends Fragment> implements ActionBar.TabListener{

    private Fragment fragment;
    private final Activity activity;
    private final String tag;
    private final Class<T> clz;

    public TabListener(Activity activity, String tag, Class<T> clz) {
        this.activity = activity;
        this.tag = tag;
        this.clz = clz;
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        if (fragment == null) {
            fragment = Fragment.instantiate(activity, clz.getName());
            fragmentTransaction.add(android.R.id.content, fragment, tag);
        } else {
            fragmentTransaction.attach(fragment);
        }
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        if (fragment != null) {
            fragmentTransaction.detach(fragment);
        }
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }
}
