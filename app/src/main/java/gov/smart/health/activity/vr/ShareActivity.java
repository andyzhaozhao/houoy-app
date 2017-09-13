package gov.smart.health.activity.vr;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import gov.smart.health.R;

public class ShareActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        View btnShare = findViewById(R.id.btn_share);
        View btnShareCancel = findViewById(R.id.btn_share_cancel);
        btnShare.setOnClickListener(this);
        btnShareCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_share:
                Intent share = new Intent(Intent.ACTION_SEND);
                share.putExtra(Intent.EXTRA_TEXT,"Hello");
                share.setType("text/plain");
                startActivity(share);
                break;
            case R.id.btn_share_cancel:
                finish();
                break;
        }
    }
}
