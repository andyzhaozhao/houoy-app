package gov.smart.health.app;

import android.app.Application;

import com.androidnetworking.AndroidNetworking;
import com.crashlytics.android.Crashlytics;

import gov.smart.health.utils.SharedPreferencesHelper;
import io.fabric.sdk.android.Fabric;

/**
 * Created by laoniu on 2017/07/30.
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AndroidNetworking.initialize(getApplicationContext());
        Fabric.with(this, new Crashlytics());
        new SharedPreferencesHelper(getApplicationContext());
    }
}
