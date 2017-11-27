package gov.smart.health.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import gov.smart.health.activity.login.model.LoginModel;
import gov.smart.health.utils.SHConstants;
import gov.smart.health.utils.SharedPreferencesHelper;
import gov.smart.health.utils.Utils;

public class LoginActivity extends AppCompatActivity {

    private TextView mUserName,mUserPwd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mUserName = (TextView)findViewById(R.id.user_name);
        mUserPwd = (TextView)findViewById(R.id.user_pwd);
        Button login = (Button)findViewById(R.id.btn_login);

        TextView userResetPwd = (TextView)findViewById(R.id.btn_reset_pwd);
        TextView register = (TextView)findViewById(R.id.btn_no_register);

        if(SHConstants.isDebug){
            mUserName.setText("181212121");
            mUserPwd.setText("test1");
        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = mUserName.getText().toString();
                String pwd = mUserPwd.getText().toString();
                if (Utils.isEmpty(name) || Utils.isEmpty(pwd)) {
                    Toast.makeText(getApplicationContext(), "请输入用户名或密码!", Toast.LENGTH_LONG).show();
                    return;
                }

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put(SHConstants.LoginMobile, name);
                    jsonObject.put(SHConstants.LoginPassword, pwd);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                AndroidNetworking.post(SHConstants.SigninSystemMobile)
                        .addJSONObjectBody(jsonObject) // posting json
                        .addHeaders(SHConstants.HeaderContentType, SHConstants.HeaderContentTypeValue)
                        .addHeaders(SHConstants.HeaderAccept, SHConstants.HeaderContentTypeValue)
                        .setPriority(Priority.MEDIUM)
                        .build()
                        .getAsString(new StringRequestListener() {
                            @Override
                            public void onResponse(String response) {
                                Gson gson = new Gson();
                                LoginModel model = gson.fromJson(response,LoginModel.class);
                                if (model.success){
                                    SharedPreferencesHelper.settingString(SHConstants.LoginUserPkPerson,model.resultData.pk_user);
                                    SharedPreferencesHelper.settingString(SHConstants.LoginUserPersonName,model.resultData.user_name);
                                    Intent intent = new Intent();
                                    intent.setClass(getApplicationContext(),HomeActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(getApplication(),"登录失败",Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onError(ANError anError) {
                                Log.d("","response error"+anError.getErrorDetail());
                                Toast.makeText(getApplication(),"登录失败",Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(),RegisterActivity.class);
                startActivity(intent);
            }
        });

        userResetPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(),ResetPwdActivity.class);
                startActivity(intent);
            }
        });

    }
}
