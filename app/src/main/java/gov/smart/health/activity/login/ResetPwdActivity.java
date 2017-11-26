package gov.smart.health.activity.login;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

import java.util.HashMap;

import gov.smart.health.R;
import gov.smart.health.activity.login.model.RegisterUserModel;
import gov.smart.health.activity.login.model.ResetPasswordModel;
import gov.smart.health.utils.SHConstants;
import gov.smart.health.utils.SharedPreferencesHelper;

public class ResetPwdActivity extends AppCompatActivity {

    private TextView userMail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pwd);

        View btnReset = findViewById(R.id.btn_reset_user);
        userMail = (TextView)findViewById(R.id.reset_mail);

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPwd();
            }
        });
    }

    private void resetPwd(){

        String userMailTxt = userMail.getText().toString();
        if(userMailTxt.isEmpty()){
            Toast.makeText(getApplication(),"请输入完整信息",Toast.LENGTH_LONG).show();
            return;
        }
        String pk = SharedPreferencesHelper.gettingString(SHConstants.LoginUserPkPerson,"");

        HashMap<String,Object> map = new HashMap<>();
        map.put(SHConstants.Password_Email, userMailTxt);
        map.put(SHConstants.Password_User_PK, pk);

        AndroidNetworking.get(SHConstants.ForgetPassword)
                .addQueryParameter(map)
                .addHeaders(SHConstants.HeaderContentType, SHConstants.HeaderContentTypeValue)
                .addHeaders(SHConstants.HeaderAccept, SHConstants.HeaderContentTypeValue)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        ResetPasswordModel model = gson.fromJson(response,ResetPasswordModel.class);
                        if (model.success){
                            Toast.makeText(getApplication(),"密码找回成功",Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getApplication(),"密码找回失败",Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d("","response error"+anError.getErrorDetail());
                        Toast.makeText(getApplication(),"密码找回失败",Toast.LENGTH_LONG).show();
                    }
                });
    }
}
