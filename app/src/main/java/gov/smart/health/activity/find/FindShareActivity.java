package gov.smart.health.activity.find;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.Date;

import gov.smart.health.R;
import gov.smart.health.activity.HomeActivity;
import gov.smart.health.activity.find.model.FindShareModel;
import gov.smart.health.activity.login.model.LoginModel;
import gov.smart.health.utils.SHConstants;
import gov.smart.health.utils.SharedPreferencesHelper;

public class FindShareActivity extends AppCompatActivity implements View.OnClickListener{

    private  View btnShare;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_share);
        btnShare = findViewById(R.id.btn_share);
        btnShare.setOnClickListener(this);

        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_share:
                btnShare.setClickable(false);
                this.sendData();
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                break;
        }
    }

    public void sendData(){

        TextView tvTitle = (TextView) findViewById(R.id.tv_title);
        TextView tvContext = (TextView) findViewById(R.id.tv_context);
        if(tvTitle.getText().toString().isEmpty()){
            Toast.makeText(getApplication(),"请输入分享信息",Toast.LENGTH_LONG).show();
            btnShare.setClickable(true);
            return;
        }
        String name = SharedPreferencesHelper.gettingString(SHConstants.LoginUserPersonName,"");
        String pk = SharedPreferencesHelper.gettingString(SHConstants.LoginUserPkPerson,"");

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(SHConstants.FindSharePersonName, name);
            jsonObject.put(SHConstants.FindSharePkPerson, pk);
            jsonObject.put(SHConstants.FindShareRecordShareCode , System.currentTimeMillis());
            jsonObject.put(SHConstants.FindShareRecordShareDesc, tvContext.getText());
            jsonObject.put(SHConstants.FindShareRecordShareName, tvTitle.getText());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AndroidNetworking.post(SHConstants.RecordShareSave)
                .addJSONObjectBody(jsonObject) // posting json
                .addHeaders(SHConstants.HeaderContentType, SHConstants.HeaderContentTypeValue)
                .addHeaders(SHConstants.HeaderAccept, SHConstants.HeaderContentTypeValue)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        FindShareModel model = gson.fromJson(response,FindShareModel.class);
                        if (model.success){
                            Toast.makeText(getApplication(),"保存成功",Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getApplication(),"保存失败",Toast.LENGTH_LONG).show();
                        }
                        btnShare.setClickable(true);
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d("","response error"+anError.getErrorDetail());
                        Toast.makeText(getApplication(),"保存失败",Toast.LENGTH_LONG).show();
                        btnShare.setClickable(true);
                    }
                });
    }
}
