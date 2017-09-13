package gov.smart.health.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import gov.smart.health.R;
import gov.smart.health.model.MessageModel;

/**
 * Created by laoniu on 2017/07/23.
 */

public class MessageRefreshRecyclerAdapter extends RecyclerView.Adapter<MessageRefreshRecyclerAdapter.ViewHolder>{
    private LayoutInflater mInflater;
    private List<MessageModel> mLists;
    private Context mContext;

    public MessageRefreshRecyclerAdapter(Context context , List<MessageModel> lists){
        mContext = context;
        this.mInflater=LayoutInflater.from(context);
        this.mLists = lists;
    }

    public void addDataLists(List<MessageModel> lists) {
        if (this.mLists == null){
            this.mLists = lists;
        } else {
            this.mLists.addAll(lists);
        }
        notifyDataSetChanged();
    }

    public void addNewDataLists(List<MessageModel> lists) {
        if (this.mLists == null){
            this.mLists = lists;
        } else {
            this.mLists.addAll(0,lists);
        }
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = mInflater.inflate(R.layout.message_list_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    /**
     * 数据的绑定显示
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.image.setImageResource(mLists.get(position).getImageid());
        holder.title.setText(mLists.get(position).getTitle());
        holder.content.setText(mLists.get(position).getContent());
        holder.itemView.setTag(position);
    }
    @Override
    public int getItemCount() {
        return this.mLists == null? 0 : this.mLists.size();
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView title;
        public TextView content;

        public ViewHolder(View view){
            super(view);
            image = (ImageView)view.findViewById(R.id.message_item_img);
            title = (TextView)view.findViewById(R.id.message_item_title);
            content = (TextView)view.findViewById(R.id.message_item_content);
//            view.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent();
//                    intent.setClass(mContext, MessageActivity.class);
//                    mContext.startActivity(intent);
//                }
//            });
        }
    }
}