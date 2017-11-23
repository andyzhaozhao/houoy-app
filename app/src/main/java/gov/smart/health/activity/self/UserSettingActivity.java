package gov.smart.health.activity.self;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.IntentCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import gov.smart.health.R;
import gov.smart.health.activity.find.DetailActivity;
import gov.smart.health.activity.login.LoginActivity;
import gov.smart.health.activity.self.model.MyPersonInfoListModel;
import gov.smart.health.utils.SHConstants;
import gov.smart.health.utils.SharedPreferencesHelper;
import gov.smart.health.utils.Utils;

public class UserSettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_setting);
        View btnSetting = findViewById(R.id.btn_setting_setting);
        View btnDeviceSetting = findViewById(R.id.btn_setting_device);

        View btnHelp = findViewById(R.id.btn_setting_help);
        View btnUpdate = findViewById(R.id.btn_setting_update);
        View btnLogout = findViewById(R.id.btn_setting_logout);
        TextView version = (TextView)findViewById(R.id.setting_tv_version);
        version.setText("V"+Utils.getVersionName(getApplicationContext()));

        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                if(getIntent() != null) {
                    MyPersonInfoListModel personModel = (MyPersonInfoListModel) getIntent().getSerializableExtra(SHConstants.SettingPersonModelKey);
                    intent.putExtra(SHConstants.SettingPersonModelKey,personModel);
                }
                intent.setClass(getApplication(), UserSettingInfoActivity.class);
                startActivity(intent);
            }
        });
        btnDeviceSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(getApplication(), DeviceSettingActivity.class);
                startActivity(intent);
            }
        });

        btnHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(getApplication(), MyDetailActivity.class);
                startActivity(intent);
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(getApplication(), MyDetailActivity.class);
                startActivity(intent);
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferencesHelper.settingString(SHConstants.LoginUserPkPerson,"");
                Intent intent = new Intent();
                intent.addFlags(IntentCompat.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setClass(getApplication(), LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
