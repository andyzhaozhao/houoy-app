package gov.smart.health.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import gov.smart.health.R;
import gov.smart.health.model.MessageBean;

/**
 * Created by laoniu on 2017/07/27.
 */

public class MessageAdapter extends BaseAdapter {
    private LayoutInflater mLayoutInflater;
    private List<MessageBean> mList;

    public MessageAdapter(Context context, List<MessageBean> list) {
        this.mLayoutInflater = LayoutInflater.from(context);
        this.mList = list;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public MessageBean getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        MessageBean bean = mList.get(position);
        if (bean.getType() == MessageBean.Type.INCOMING) {
            return 0;
        } else {
            return 1;
        }


    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        // MsgBean bean = getItem(position);
        if (convertView == null) {
            viewHolder = new ViewHolder();
            if (getItemViewType(position) == 0) {
                convertView = mLayoutInflater.inflate(R.layout.msg_item_incoming_msg, parent, false);
                viewHolder.tv_date = (TextView) convertView.findViewById(R.id.tv_incoming_date);
                viewHolder.tv_content = (TextView) convertView.findViewById(R.id.tv_incoming_content);
            } else {
                convertView = mLayoutInflater.inflate(R.layout.msg_item_outcoming_msg, parent, false);
                viewHolder.tv_date = (TextView) convertView.findViewById(R.id.tv_outcoming_date);
                viewHolder.tv_content = (TextView) convertView.findViewById(R.id.tv_outcoming_content);
            }
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        //设置数据
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        viewHolder.tv_date.setText(format.format(getItem(position).getDate()));
        viewHolder.tv_content.setText(getItem(position).getMsg());
        return convertView;
    }

    private static class ViewHolder {
        private TextView tv_date;
        private TextView tv_content;

    }
}
