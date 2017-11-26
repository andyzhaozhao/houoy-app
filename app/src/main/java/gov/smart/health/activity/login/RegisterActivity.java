package gov.smart.health.activity.login;

import android.content.Intent;
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

import gov.smart.health.R;
import gov.smart.health.activity.HomeActivity;
import gov.smart.health.activity.login.model.LoginModel;
import gov.smart.health.activity.login.model.RegisterUserModel;
import gov.smart.health.utils.SHConstants;
import gov.smart.health.utils.SharedPreferencesHelper;

public class RegisterActivity extends AppCompatActivity {

    private TextView userName,userPhone,userMail,userFirstPwd ,userSecondPwd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        View btnRegister = findViewById(R.id.btn_register_user);
        userName = (TextView)findViewById(R.id.register_user_name);
        userPhone = (TextView)findViewById(R.id.register_user_phone);
        userMail = (TextView)findViewById(R.id.register_mail);
        userFirstPwd = (TextView)findViewById(R.id.register_first_pwd);
        userSecondPwd = (TextView)findViewById(R.id.register_second_pwd);
        
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void registerUser(){

        String userNameTxt = userName.getText().toString();
        String userPhoneTxt = userPhone.getText().toString();
        String userMailTxt = userMail.getText().toString();
        String userFirstPwdTxt = userFirstPwd.getText().toString();
        String userSecondPwdTxt =  userSecondPwd.getText().toString();
        if(userNameTxt.isEmpty() || userPhoneTxt.isEmpty() ||userMailTxt.isEmpty() ||userFirstPwdTxt.isEmpty() ||userSecondPwdTxt.isEmpty()){
            Toast.makeText(getApplication(),"请输入完整信息",Toast.LENGTH_LONG).show();
            return;
        }
        if(!userFirstPwdTxt.equals(userSecondPwdTxt)){
            Toast.makeText(getApplication(),"两次密码输入不正确",Toast.LENGTH_LONG).show();
            return;
        }

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(SHConstants.Register_Person_Code, userNameTxt);
            jsonObject.put(SHConstants.Register_Person_name, userNameTxt);
            jsonObject.put(SHConstants.Register_Mobile, userPhoneTxt);
            jsonObject.put(SHConstants.Register_Mobile, userMailTxt);
            jsonObject.put(SHConstants.Register_Password, userFirstPwdTxt);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking.post(SHConstants.PersonSave)
                .addJSONObjectBody(jsonObject) // posting json
                .addHeaders(SHConstants.HeaderContentType, SHConstants.HeaderContentTypeValue)
                .addHeaders(SHConstants.HeaderAccept, SHConstants.HeaderContentTypeValue)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        RegisterUserModel model = gson.fromJson(response,RegisterUserModel.class);
                        if (model.success){
                            Toast.makeText(getApplication(),"注册成功",Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getApplication(),"注册失败",Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d("","response error"+anError.getErrorDetail());
                        Toast.makeText(getApplication(),"注册失败",Toast.LENGTH_LONG).show();
                    }
                });
    }
}
