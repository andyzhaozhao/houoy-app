package gov.smart.health.activity.find;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import gov.smart.health.R;
import gov.smart.health.activity.find.model.FindAttentionListDataModel;
import gov.smart.health.activity.find.model.FindEssayListDataModel;
import gov.smart.health.activity.find.model.FindPersonFlowModel;
import gov.smart.health.utils.SHConstants;
import gov.smart.health.utils.SharedPreferencesHelper;

public class AttentionDetailActivity extends AppCompatActivity {

    private FindAttentionListDataModel attentionModel ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attention_detail);
        attentionModel = (FindAttentionListDataModel)getIntent().getSerializableExtra( SHConstants.PersonAttentionModelKey);

        TextView title = (TextView)findViewById(R.id.title);
        TextView subname = (TextView)findViewById(R.id.detail_subname);
        TextView text = (TextView)findViewById(R.id.detail_text);
        TextView time = (TextView)findViewById(R.id.detail_time);
        View button = findViewById(R.id.btn_attention);
        boolean isShow = (boolean) getIntent().getSerializableExtra( SHConstants.ShowAttentionModelKey);
        if(isShow) {
            button.setVisibility(View.VISIBLE);
        } else {
            button.setVisibility(View.INVISIBLE);
        }
        if(attentionModel !=null) {
            title.setText(attentionModel.record_share_name+"的状态");
            subname.setText(attentionModel.record_share_name);
            time.setText(attentionModel.ts);
            text.setText(Html.fromHtml(attentionModel.record_share_desc));
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sendAttention();
                }
            });
        }

        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void sendAttention() {
        String name = SharedPreferencesHelper.gettingString(SHConstants.LoginUserPersonName,"");
        String pk = SharedPreferencesHelper.gettingString(SHConstants.LoginUserPkPerson,"");

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(SHConstants.LoginUserPersonName, name);
            jsonObject.put(SHConstants.LoginUserPkPerson, pk);
            jsonObject.put(SHConstants.PersonFlowPersonName, attentionModel.person_name);
            jsonObject.put(SHConstants.PersonFlowPkPerson, attentionModel.pk_person);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking.post(SHConstants.PersonFollowSave)
                .addJSONObjectBody(jsonObject) // posting json
                .addHeaders(SHConstants.HeaderContentType, SHConstants.HeaderContentTypeValue)
                .addHeaders(SHConstants.HeaderAccept, SHConstants.HeaderContentTypeValue)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        FindPersonFlowModel model = gson.fromJson(response,FindPersonFlowModel.class);
                        if (model.success){
                            Toast.makeText(getApplication(),"关注成功",Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getApplication(),"关注失败",Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d("","response error"+anError.getErrorDetail());
                        Toast.makeText(getApplication(),"关注失败",Toast.LENGTH_LONG).show();
                    }
                });
    }
}
