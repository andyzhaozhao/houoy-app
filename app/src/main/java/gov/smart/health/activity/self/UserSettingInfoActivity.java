package gov.smart.health.activity.self;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import gov.smart.health.R;

public class UserSettingInfoActivity extends AppCompatActivity {

    private static final int IMAGE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_setting_info);


        TextView userName = (TextView) findViewById(R.id.et_update_user_name);
        TextView userAge = (TextView) findViewById(R.id.et_update_user_age);
        TextView userContent = (TextView) findViewById(R.id.et_update_user_content);

        View btnIcon = findViewById(R.id.et_update_user_icon);
        //ImageView icon = (ImageView) findViewById(R.id.img_update_user_icon);

        View btnUpdate = findViewById(R.id.btn_update_user);

        View btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //调用相册
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, IMAGE);

            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //获取图片路径
        if (requestCode == IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            String[] filePathColumns = {MediaStore.Images.Media.DATA};
            Cursor c = getContentResolver().query(selectedImage, filePathColumns, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePathColumns[0]);
            String imagePath = c.getString(columnIndex);
            showImage(imagePath);
            c.close();
        }
    }

    //加载图片
    private void showImage(String imaePath){
        Bitmap bm = BitmapFactory.decodeFile(imaePath);
        ((ImageView)findViewById(R.id.img_update_user_icon)).setImageBitmap(bm);
    }
}
