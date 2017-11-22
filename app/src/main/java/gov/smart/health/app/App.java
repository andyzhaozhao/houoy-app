package gov.smart.health.app;

import android.app.Application;
import android.bluetooth.BluetoothAdapter;

import com.androidnetworking.AndroidNetworking;
import com.crashlytics.android.Crashlytics;
import com.fitpolo.support.Fitpolo;

import gov.smart.health.activity.vr.downloadfile.DownloadManager;
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
        DownloadManager.shareDownloadManager();
        BluetoothAdapter Bt = BluetoothAdapter.getDefaultAdapter();
        if(Bt != null) {
            Fitpolo.init(this);
        }
    }
}
