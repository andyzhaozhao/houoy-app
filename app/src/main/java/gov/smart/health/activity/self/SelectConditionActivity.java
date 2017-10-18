package gov.smart.health.activity.self;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import gov.smart.health.R;
import gov.smart.health.activity.vr.SportAreaActivity;
import gov.smart.health.utils.DateTimePickDialogUtil;

public class SelectConditionActivity extends AppCompatActivity {

    private TextView mETDataFirst, mETDataSecond;
    private String initStartDateTime = "2017年9月3日"; // 初始化开始时间
    private String initEndDateTime = "2017年10月23日"; // 初始化结束时间
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_condition);

        findViewById(R.id.tv_area).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(SelectConditionActivity.this, SportAreaActivity.class);
                startActivityForResult(intent,0);
            }
        });
        mETDataFirst = (TextView)findViewById(R.id.et_data_first);
        mETDataFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DateTimePickDialogUtil dateTimePicKDialog = new DateTimePickDialogUtil(
                        SelectConditionActivity.this, initStartDateTime);
                dateTimePicKDialog.dateTimePicKDialog(mETDataFirst);
            }
        });
        mETDataSecond = (TextView)findViewById(R.id.et_data_second);
        mETDataSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DateTimePickDialogUtil dateTimePicKDialog = new DateTimePickDialogUtil(
                        SelectConditionActivity.this, initEndDateTime);
                dateTimePicKDialog.dateTimePicKDialog(mETDataSecond);
            }
        });
        findViewById(R.id.btn_commit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
