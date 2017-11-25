package gov.smart.health.activity.self;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.androidnetworking.widget.ANImageView;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.List;

import gov.smart.health.R;
import gov.smart.health.activity.HomeActivity;
import gov.smart.health.activity.login.model.LoginModel;
import gov.smart.health.activity.self.model.MyPersonInfoListModel;
import gov.smart.health.activity.self.model.UploadPersonImageInfoModel;
import gov.smart.health.utils.SHConstants;
import gov.smart.health.utils.SharedPreferencesHelper;
import pub.devrel.easypermissions.EasyPermissions;

public class UserSettingInfoActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    private static final int IMAGE = 1;
    private String[] galleryPermissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private static int REQUEST_ENABLE_LIBRARY = 123;
    private MyPersonInfoListModel personModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_setting_info);

        //TODO save userInfo.
        TextView userName = (TextView) findViewById(R.id.et_update_user_name);
        TextView userAge = (TextView) findViewById(R.id.et_update_user_age);
        TextView userContent = (TextView) findViewById(R.id.et_update_user_content);

        View btnIcon = findViewById(R.id.et_update_user_icon);
        if(getIntent() != null) {
            personModel = (MyPersonInfoListModel) getIntent().getSerializableExtra(SHConstants.SettingPersonModelKey);
            loadIcon();
        }

        View btnUpdate = findViewById(R.id.btn_update_user);

        btnIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (EasyPermissions.hasPermissions(getApplicationContext(), galleryPermissions)) {
                        pickImageFromGallery();
                    } else {
                        EasyPermissions.requestPermissions(UserSettingInfoActivity.this, "Access for storage",
                                REQUEST_ENABLE_LIBRARY, galleryPermissions);
                    }
                } else {
                    pickImageFromGallery();
                }
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void loadIcon(){
        if(personModel.portraitPath != null && !personModel.portraitPath.isEmpty()) {
            ANImageView icon = (ANImageView) findViewById(R.id.img_update_user_icon);
            icon.setDefaultImageResId(R.mipmap.person_default_icon);
            icon.setErrorImageResId(R.mipmap.person_default_icon);
            String path =  SHConstants.BaseUrlPhoto + personModel.portraitPath +"?time="+ System.currentTimeMillis();
            icon.setImageUrl(path);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> list) {
        // Some permissions have been granted
        if(requestCode == REQUEST_ENABLE_LIBRARY) {
            pickImageFromGallery();
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> list) {
        // Some permissions have been denied
        // ...
    }

    private void pickImageFromGallery(){
        try {
            //调用相册
            Intent intent = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, IMAGE);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void saveImageToServer(String imageBase64Str){
        String pk = SharedPreferencesHelper.gettingString(SHConstants.LoginUserPkPerson,"");

        AndroidNetworking.post(SHConstants.PersonUploadMobile+"?"+SHConstants.CommonUser_PK+"="+pk)
                .addStringBody(imageBase64Str)
                .addHeaders(SHConstants.HeaderContentType, SHConstants.HeaderContentTypeValue)
                .addHeaders(SHConstants.HeaderAccept, SHConstants.HeaderContentTypeValue)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        UploadPersonImageInfoModel model = gson.fromJson(response,UploadPersonImageInfoModel.class);
                        if (model.success){
                            Toast.makeText(getApplication(),"保存成功",Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getApplication(),"保存失败",Toast.LENGTH_LONG).show();
                            ANImageView icon = (ANImageView) findViewById(R.id.img_update_user_icon);
                            icon.setDefaultImageResId(R.mipmap.person_default_icon);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d("","response error"+anError.getErrorDetail());
                        Toast.makeText(getApplication(),"保存失败",Toast.LENGTH_LONG).show();
                        ANImageView icon = (ANImageView) findViewById(R.id.img_update_user_icon);
                        icon.setDefaultImageResId(R.mipmap.person_default_icon);
                    }
                });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //获取图片路径
        if (requestCode == IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            try{
                Uri selectedImage = data.getData();
                String[] filePathColumns = {MediaStore.Images.Media.DATA};
                Cursor c = getContentResolver().query(selectedImage, filePathColumns, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePathColumns[0]);
                String imagePath = c.getString(columnIndex);
                c.close();
                Bitmap bm = BitmapFactory.decodeFile(imagePath);
                ((ImageView)findViewById(R.id.img_update_user_icon)).setImageBitmap(bm);
                saveImageToServer(bitmapToString(imagePath));
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public byte[] bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 30, baos);
        return baos.toByteArray();
    }




    //把bitmap转换成String
    public static String bitmapToString(String filePath) {

        Bitmap bm = getSmallBitmap(filePath);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 30, baos);
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    // 根据路径获得图片并压缩，返回bitmap用于显示
    public static Bitmap getSmallBitmap(String filePath) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, 80, 80);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(filePath, options);
    }

    //计算图片的缩放值
    public static int calculateInSampleSize(BitmapFactory.Options options,int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height/ (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }


}
