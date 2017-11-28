package gov.smart.health.activity.self;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.fitpolo.support.OrderEnum;
import com.fitpolo.support.bluetooth.BluetoothModule;
import com.fitpolo.support.callback.ConnStateCallback;
import com.fitpolo.support.callback.OrderCallback;
import com.fitpolo.support.callback.ScanDeviceCallback;
import com.fitpolo.support.entity.BaseResponse;
import com.fitpolo.support.entity.BleDevice;
import com.fitpolo.support.task.InnerVersionTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gov.smart.health.R;
import gov.smart.health.utils.SharedPreferencesHelper;
import pub.devrel.easypermissions.EasyPermissions;

public class DeviceSettingActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks  {

    private Map<String,BleDevice> bleDeviceMap = new HashMap<>();

    public static String AddressKey = "addressKey";
    public static String DeviceNameKey = "deviceNameKey";
    public static String VRDeviceNameKey = "VRDeviceNameKey";
    private static int REQUEST_ENABLE_BLUETOOTH = 123;
    private String[] blueToothPermissions = { Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private TextView deviceName;
    private TextView VrDeviceName;
    private RadioButton r1,r2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_setting);
        deviceName = (TextView)findViewById(R.id.tv_setting_fitpolo_device);
        deviceName.setVisibility(View.INVISIBLE);
        VrDeviceName = (TextView)findViewById(R.id.btn_setting_device_text);
        r1 = (RadioButton)findViewById(R.id.device_r1);
        r2 = (RadioButton)findViewById(R.id.device_r2);
        // default radio.
        if(SharedPreferencesHelper.gettingLong(VRDeviceNameKey, 1) == 1) {
            r1.setChecked(true);
            r2.setChecked(false);
            VrDeviceName.setText("暴风小D");
        } else {
            r2.setChecked(true);
            r1.setChecked(false);
            VrDeviceName.setText("暴风魔镜2代");
        }
        findViewById(R.id.btn_setting_device1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferencesHelper.settingLong(VRDeviceNameKey,1);
                r1.setChecked(true);
                r2.setChecked(false);
                VrDeviceName.setText("暴风小D");
            }
        });
        findViewById(R.id.btn_setting_device2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferencesHelper.settingLong(VRDeviceNameKey,2);
                r2.setChecked(true);
                r1.setChecked(false);
                VrDeviceName.setText("暴风魔镜2代");
            }
        });

        findViewById(R.id.btn_setting_device).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BluetoothAdapter Bt = BluetoothAdapter.getDefaultAdapter();
                if (Bt == null) {
                    Toast.makeText(getApplication(), "您的手机不支持蓝牙功能！", Toast.LENGTH_LONG).show();
                    return;
                }
                boolean btEnable = Bt.isEnabled();
                if (btEnable) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (EasyPermissions.hasPermissions(getApplicationContext(), blueToothPermissions)) {
                            doConnectionWatch();
                        } else {
                            EasyPermissions.requestPermissions(DeviceSettingActivity.this, "请求授权",
                                    REQUEST_ENABLE_BLUETOOTH, blueToothPermissions);
                        }
                    } else {
                        doConnectionWatch();
                    }
                } else {
                    Toast.makeText(getApplication(), "请确认蓝牙是否正常开启！", Toast.LENGTH_LONG).show();
                }
            }
        });
        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        BluetoothModule bluetoothModule = BluetoothModule.getInstance();
        if (bluetoothModule.isBluetoothOpen()) {
            String deviceAddress = SharedPreferencesHelper.gettingString(AddressKey, null);
            if (deviceAddress != null && bluetoothModule.isConnDevice(getApplicationContext(), deviceAddress)) {
                String name = SharedPreferencesHelper.gettingString(DeviceNameKey, null);
                deviceName.setText("已绑定："+name);
                deviceName.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> list) {
        // Some permissions have been granted
        if(requestCode == REQUEST_ENABLE_BLUETOOTH) {
            doConnectionWatch();
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> list) {
        // Some permissions have been denied
        // ...
        Toast.makeText(getApplication(), "需要您的权限授权！", Toast.LENGTH_LONG).show();
    }
    private void doConnectionWatch() {
        BluetoothModule bluetoothModule = BluetoothModule.getInstance();
        if(bluetoothModule.isBluetoothOpen()) {
            String deviceAddress = SharedPreferencesHelper.gettingString(AddressKey,null);
            if(deviceAddress != null && bluetoothModule.isConnDevice(getApplicationContext(),deviceAddress)){
                SharedPreferencesHelper.settingString(AddressKey,null);
                bluetoothModule.disConnectBle();
                Toast.makeText(getApplication(), "解除绑定", Toast.LENGTH_LONG).show();
                deviceName.setVisibility(View.INVISIBLE);
            } else {
                bluetoothModule.startScanDevice(new ScanDeviceCallback() {
                    @Override
                    public void onStartScan() {
                        Toast.makeText(getApplication(), "开始搜索设备", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onScanDevice(BleDevice device) {
                        bleDeviceMap.put(device.name, device);
                        //Toast.makeText(getApplication(), "搜索到设备：" + device.name, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onStopScan() {
                        //BluetoothModule.getInstance().
                        if(bleDeviceMap.size() >0){
                            showDialog();
                        } else {
                            Toast.makeText(getApplication(), "没有找到可用设备", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        } else {
            Toast.makeText(getApplication(), "请确认蓝牙是否正常开启！", Toast.LENGTH_LONG).show();
        }
    }

    private void showDialog(){
        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(DeviceSettingActivity.this);
        builder.setTitle("选择设备");

        final ArrayList<String> devicesList = new ArrayList<String>();
        String[] devices = {};
        // add a radio button list
        for (String key : bleDeviceMap.keySet()) {
            if(bleDeviceMap.get(key) != null  && bleDeviceMap.get(key).name != null) {
                devicesList.add(bleDeviceMap.get(key).name);
            }
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
                        SharedPreferencesHelper.settingString(DeviceNameKey,device.name);
                        deviceName.setText("已绑定："+device.name);
                        deviceName.setVisibility(View.VISIBLE);
                        Toast.makeText(getApplication(), "设备绑定成功", Toast.LENGTH_LONG).show();
                        InnerVersionTask task = new InnerVersionTask(new OrderCallback() {
                            @Override
                            public void onOrderResult(OrderEnum order, BaseResponse response) {

                            }

                            @Override
                            public void onOrderTimeout(OrderEnum order) {

                            }

                            @Override
                            public void onOrderFinish() {

                            }
                        });
                        BluetoothModule.getInstance().sendOrder(task);
                    }

                    @Override
                    public void onConnFailure(int errorCode) {
                        Toast.makeText(getApplication(), "设备连接失败", Toast.LENGTH_LONG).show();
                        deviceName.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onDisconnect() {
                        Toast.makeText(getApplication(), "断开连接", Toast.LENGTH_LONG).show();
                        deviceName.setVisibility(View.INVISIBLE);
                    }
                });
                bluetoothModule.setOpenReConnect(true);
                dialog.dismiss();
            }
        });

        // add OK and Cancel buttons
        builder.setNegativeButton("取消连接", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // user clicked OK
                dialog.dismiss();
            }
        });
        
        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
