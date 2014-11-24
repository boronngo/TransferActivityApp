package boro.life_cloud.ht.sfc.keio.ac.jp.orf2014;

import android.app.Application;

/**
 * Created by boro on 14/10/30.
 */
public class App extends Application {

    private static App instance;

    public App() {
        instance = this;
    }

    public static App getInstance() {
        return  instance;
    }
}
