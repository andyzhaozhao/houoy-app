package gov.smart.health.activity.self;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import gov.smart.health.R;
import gov.smart.health.utils.SHConstants;

public class MyDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_detail);
        TextView title = (TextView)findViewById(R.id.tv_title);
        title.setText("详细信息");
        //TODO detail view.
    }
}
