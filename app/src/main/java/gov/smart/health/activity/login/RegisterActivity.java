package gov.smart.health.activity.login;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import gov.smart.health.R;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        View btnBack = findViewById(R.id.btn_back);
        View btnGetVerify = findViewById(R.id.btn_verify_get);
        View btnRegister = findViewById(R.id.btn_register_user);
        TextView userName = (TextView)findViewById(R.id.register_user_name);
        TextView userPhone = (TextView)findViewById(R.id.register_user_phone);
        TextView userVerify = (TextView)findViewById(R.id.register_verify);
        TextView userFirstPwd = (TextView)findViewById(R.id.register_first_pwd);
        TextView userSecondPwd = (TextView)findViewById(R.id.register_second_pwd);


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
        
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
}
