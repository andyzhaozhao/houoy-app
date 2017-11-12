package gov.smart.health.activity.find;

import android.content.Intent;
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
import gov.smart.health.activity.HomeActivity;
import gov.smart.health.activity.find.model.FindAttentionListDataModel;
import gov.smart.health.activity.find.model.FindEssayListDataModel;
import gov.smart.health.activity.find.model.FindPersonFlowModel;
import gov.smart.health.activity.login.model.LoginModel;
import gov.smart.health.utils.SHConstants;
import gov.smart.health.utils.SharedPreferencesHelper;

public class DetailActivity extends AppCompatActivity {

    private FindEssayListDataModel model;
    private FindAttentionListDataModel attentionModel ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        model = (FindEssayListDataModel)getIntent().getSerializableExtra( SHConstants.PersonFlowModelKey);
        attentionModel = (FindAttentionListDataModel)getIntent().getSerializableExtra( SHConstants.PersonAttentionModelKey);

        TextView name = (TextView)findViewById(R.id.detail_name);
        TextView subname = (TextView)findViewById(R.id.detail_subname);
        TextView text = (TextView)findViewById(R.id.detail_text);
        View button = findViewById(R.id.btn_attention);
        if(model !=null) {
            name.setText(model.essay_name);
            subname.setText(model.essay_subname);
            text.setText(Html.fromHtml(model.essay_content));
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sendAttention();
                }
            });
        }else if(attentionModel !=null) {
            name.setText(attentionModel.record_share_code);
            subname.setText(attentionModel.record_share_name);
            text.setText(Html.fromHtml(attentionModel.record_share_desc));
            button.setVisibility(View.INVISIBLE);
        }
    }

    private void sendAttention() {
        String name = SharedPreferencesHelper.gettingString(SHConstants.LoginUserPersonName,"");
        String pk = SharedPreferencesHelper.gettingString(SHConstants.LoginUserPkPerson,"");

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(SHConstants.LoginUserPersonName, name);
            jsonObject.put(SHConstants.LoginUserPkPerson, pk);
            jsonObject.put(SHConstants.PersonFlowPersonName, model.person_name);
            jsonObject.put(SHConstants.PersonFlowPkPerson, model.pk_person);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking.post(SHConstants.PersonFollowSave)
                .addJSONObjectBody(jsonObject) // posting json
                .addHeaders(SHConstants.HeaderContentType, SHConstants.HeaderContentTypeValue)
                .addHeaders(SHConstants.HeaderAccept, SHConstants.HeaderContentTypeValue)
                .setPriority(Priority.LOW)
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
