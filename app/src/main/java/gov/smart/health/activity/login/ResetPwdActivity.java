package gov.smart.health.activity.login;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import gov.smart.health.R;

public class ResetPwdActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pwd);

        View btnBack = findViewById(R.id.btn_back);
        View btnGetVerify = findViewById(R.id.btn_verify_get);
        View btnReset = findViewById(R.id.btn_reset_user);
        TextView userPhone = (TextView)findViewById(R.id.reset_user_phone);
        TextView userVerify = (TextView)findViewById(R.id.reset_verify);
        TextView userFirstPwd = (TextView)findViewById(R.id.reset_first_pwd);
        TextView userSecondPwd = (TextView)findViewById(R.id.reset_second_pwd);


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnGetVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
