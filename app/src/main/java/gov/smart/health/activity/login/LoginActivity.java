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
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.LoginEvent;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import gov.smart.health.R;
import gov.smart.health.activity.HomeActivity;
import gov.smart.health.model.LoginModel;
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
        Button register = (Button)findViewById(R.id.btn_register);

        TextView userResetPwd = (TextView)findViewById(R.id.btn_reset_pwd);
        TextView userNoLogin = (TextView)findViewById(R.id.btn_no_login);

        //if(SHConstants.isDebug){
            mUserName.setText("admin");
            mUserPwd.setText("1");
        //}
        userResetPwd.setMovementMethod(LinkMovementMethod.getInstance());
        userResetPwd.setText(Html.fromHtml("<u>忘记密码</u>"));

        userNoLogin.setMovementMethod(LinkMovementMethod.getInstance());
        userNoLogin.setText(Html.fromHtml("<u>游客登录</u>"));

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
                    jsonObject.put("user_code", name);
                    jsonObject.put("user_password", pwd);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                AndroidNetworking.post("http://182.92.128.240:8889/api/login/signinMobile")
                        .addJSONObjectBody(jsonObject) // posting json
                        .addHeaders("Content-Type", "application/json")
                        .addHeaders("Accept", "application/json")
                        .setPriority(Priority.LOW)
                        .build()
                        .getAsString(new StringRequestListener() {
                            @Override
                            public void onResponse(String response) {
                                Gson gson = new Gson();
                                LoginModel model = gson.fromJson(response,LoginModel.class);
                                Log.d("","response"+response + " "+model.toString());
                                model
                            }

                            @Override
                            public void onError(ANError anError) {
                                Log.d("","response error"+anError.getErrorDetail());
                            }
                        });




//                Intent intent = new Intent();
//                intent.setClass(getApplicationContext(),HomeActivity.class);
//                startActivity(intent);
//                finish();
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

        userNoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginEvent loginEvent = new LoginEvent();
                loginEvent.putSuccess(false);
                loginEvent.putMethod("Android");
                Answers.getInstance().logLogin(loginEvent);

                Intent intent = new Intent();
                intent.setClass(getApplicationContext(),HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
