package gov.smart.health.activity.vr;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import gov.smart.health.R;
import gov.smart.health.fragment.VRFragment;

public class SelectParkActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_park);
        Button scanButton = (Button) findViewById(R.id.btn_near);
        scanButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent mIntent = new Intent();
                mIntent.putExtra("link", "link");
                // 设置结果，并进行传送
                setResult(VRFragment.SELECT_PARK_REQUEST_CODE, mIntent);
                finish();
            }
        });
    }

}
