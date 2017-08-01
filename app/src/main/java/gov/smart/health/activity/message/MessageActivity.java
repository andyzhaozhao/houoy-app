package gov.smart.health.activity.message;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import gov.smart.health.R;
import gov.smart.health.adapter.MessageAdapter;
import gov.smart.health.model.MessageBean;
import gov.smart.health.utils.UtilsGetMessage;

public class MessageActivity extends AppCompatActivity implements View.OnClickListener {

    private ListView lv_list;
    private MessageAdapter mMessageAdapter;
    private List<MessageBean> mList;
    private EditText et_input;
    private Button btn_input;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            MessageBean bean = (MessageBean) msg.obj;

            mList.add(bean);
            mMessageAdapter.notifyDataSetChanged();
            lv_list.setSelection(mList.size() - 1);

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

    lv_list = (ListView) findViewById(R.id.lv_list);
    et_input = (EditText) findViewById(R.id.et_input);
    btn_input = (Button) findViewById(R.id.btn_input);

    initDatas();
    setEvent();
}

    private void setEvent() {
        btn_input.setOnClickListener(this);
    }

    private void initDatas() {
        mList = new ArrayList<>();
        mMessageAdapter = new MessageAdapter(getApplicationContext(), mList);
        lv_list.setAdapter(mMessageAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_input:
                final String send = et_input.getText().toString();
                if (TextUtils.isEmpty(send)) {
                    Toast.makeText(getApplicationContext(), "can't be null", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    MessageBean bean = new MessageBean();
                    bean.setMsg(send);
                    bean.setType(MessageBean.Type.OUTCOMING);
                    bean.setDate(new Date());
                    mList.add(bean);
                    mMessageAdapter.notifyDataSetChanged();
                    lv_list.setSelection(mList.size() - 1);
                    et_input.setText("");

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            MessageBean bean = UtilsGetMessage.getMessage(send);
                            Message message = Message.obtain();
                            message.obj = bean;
                            mHandler.sendMessage(message);


                        }
                    }).start();


                }
                break;
        }

    }
}
