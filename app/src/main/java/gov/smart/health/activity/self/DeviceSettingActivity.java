package gov.smart.health.activity.self;

import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.Toast;

import com.fitpolo.support.bluetooth.BluetoothModule;
import com.fitpolo.support.callback.ConnStateCallback;
import com.fitpolo.support.callback.ScanDeviceCallback;
import com.fitpolo.support.entity.BleDevice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gov.smart.health.R;
import gov.smart.health.utils.SharedPreferencesHelper;

public class DeviceSettingActivity extends AppCompatActivity {

    private Map<String,BleDevice> bleDeviceMap = new HashMap<>();

    private static String AddressKey = "addressKey";
    private static int REQUEST_ENABLE_BLUETOOTH = 123;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_setting);
        findViewById(R.id.btn_setting_device).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BluetoothAdapter Bt = BluetoothAdapter.getDefaultAdapter();
                if(Bt == null){
                    Toast.makeText(getApplication(), "您的手机不支持蓝牙功能！", Toast.LENGTH_LONG).show();
                    return;
                }
                boolean btEnable = Bt.isEnabled();
                if(btEnable){
                    doConnectionWatch();
                }else{
                    Intent btOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(btOn, REQUEST_ENABLE_BLUETOOTH);
                }

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int ResultCode, Intent date){
        if(requestCode == REQUEST_ENABLE_BLUETOOTH){
            BluetoothAdapter Bt = BluetoothAdapter.getDefaultAdapter();
            if(Bt.isEnabled()){
                doConnectionWatch();
            }
        }
    }

    private void doConnectionWatch() {
        BluetoothModule bluetoothModule = BluetoothModule.getInstance();
        if(bluetoothModule.isBluetoothOpen()) {
            String deviceAddress = SharedPreferencesHelper.gettingString(AddressKey,null);
            if(deviceAddress != null && bluetoothModule.isConnDevice(getApplicationContext(),deviceAddress)){
                SharedPreferencesHelper.settingString(AddressKey,null);
                bluetoothModule.disConnectBle();
                Toast.makeText(getApplication(), "解除绑定", Toast.LENGTH_LONG).show();
            } else {
                bluetoothModule.startScanDevice(new ScanDeviceCallback() {
                    @Override
                    public void onStartScan() {
                        Toast.makeText(getApplication(), "开始搜索设备", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onScanDevice(BleDevice device) {
                        bleDeviceMap.put(device.name, device);
                        Toast.makeText(getApplication(), "搜索设备" + device.name, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onStopScan() {
                        Toast.makeText(getApplication(), "搜索设备结束", Toast.LENGTH_LONG).show();
                        showDialog();
                    }
                });
            }
        } else {
            Toast.makeText(getApplication(), "请确认蓝牙是否正常开启！", Toast.LENGTH_LONG).show();
        }
    }

    private void showDialog(){
        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(getApplication());
        builder.setTitle("选择设备");

        final ArrayList<String> devicesList = new ArrayList<String>();
        String[] devices = {};
        // add a radio button list
        for (int i = 0 ;i <bleDeviceMap.size() ; i++) {
            devicesList.add(bleDeviceMap.get(i).name);
        }
        int checkedItem = 1; // cow
        builder.setSingleChoiceItems(devicesList.toArray(devices), checkedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = devicesList.get(which);
                final BleDevice device = bleDeviceMap.get(name);
                BluetoothModule bluetoothModule = BluetoothModule.getInstance();
                bluetoothModule.createBluetoothGatt(getApplicationContext(), device.address, new ConnStateCallback() {
                    @Override
                    public void onConnSuccess() {
                        SharedPreferencesHelper.settingString(AddressKey,device.address);
                        Toast.makeText(getApplication(), "设备结束成功", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onConnFailure(int errorCode) {
                        Toast.makeText(getApplication(), "设备连接失败", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onDisconnect() {
                        Toast.makeText(getApplication(), "断开连接", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        // add OK and Cancel buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // user clicked OK
            }
        });
        builder.setNegativeButton("Cancel", null);

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
