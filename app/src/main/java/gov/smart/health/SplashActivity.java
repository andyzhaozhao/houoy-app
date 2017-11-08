package gov.smart.health;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import gov.smart.health.activity.HomeActivity;
import gov.smart.health.activity.login.LoginActivity;
import gov.smart.health.utils.SHConstants;
import gov.smart.health.utils.SharedPreferencesHelper;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Intent intent = new Intent();
        String pk = SharedPreferencesHelper.gettingString(SHConstants.LoginUserPkPerson,"");
        if(pk.isEmpty()) {
            intent.setClass(this, LoginActivity.class);
        } else {
            intent.setClass(this, HomeActivity.class);
        }
        startActivity(intent);
        finish();
    }
}
