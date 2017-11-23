package gov.smart.health.activity.self;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import gov.smart.health.R;
import gov.smart.health.activity.self.model.LikeAttentionInfoListModel;
import gov.smart.health.activity.vr.model.SportVideoListModel;
import gov.smart.health.utils.SHConstants;

public class MyAttentionDetailActivity extends AppCompatActivity {

    private LikeAttentionInfoListModel model = new LikeAttentionInfoListModel();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_attention_detail);
        if(getIntent() != null && getIntent().getSerializableExtra(SHConstants.PersonAttentionModelKey)!= null){
            model = (LikeAttentionInfoListModel) getIntent().getSerializableExtra(SHConstants.PersonAttentionModelKey);
        }
        TextView title = (TextView)findViewById(R.id.tv_title);
        title.setText(model.person_name == null?"详细信息":model.person_name);

        //TODO detail view.
    }
}
