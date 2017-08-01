package gov.smart.health.activity.self;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import gov.smart.health.R;

public class UserSettingInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_setting_info);


        TextView userName = (TextView) findViewById(R.id.et_update_user_name);
        TextView userAge = (TextView) findViewById(R.id.et_update_user_age);
        TextView userContent = (TextView) findViewById(R.id.et_update_user_content);

        View btnIcon = findViewById(R.id.et_update_user_icon);
        ImageView icon = (ImageView) findViewById(R.id.img_update_user_icon);

        View btnUpdate = findViewById(R.id.btn_update_user);

        View btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
