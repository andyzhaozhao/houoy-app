package gov.smart.health.activity.self.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import gov.smart.health.R;
import gov.smart.health.activity.message.FriendInfoActivity;
import gov.smart.health.activity.self.MyAttentionDetailActivity;
import gov.smart.health.activity.self.model.LikeAttentionInfoListModel;
import gov.smart.health.model.FriendModel;

/**
 * Created by laoniu on 2017/07/23.
 */

public class MyAttentionRefreshRecyclerAdapter extends RecyclerView.Adapter<MyAttentionRefreshRecyclerAdapter.ViewHolder>{
    private LayoutInflater mInflater;
    private List<LikeAttentionInfoListModel> mLists;
    private Context mContext;

    public MyAttentionRefreshRecyclerAdapter(Context context , List<LikeAttentionInfoListModel> lists){
        mContext = context;
        this.mInflater=LayoutInflater.from(context);
        this.mLists = lists;
    }

    public void addDataLists(List<LikeAttentionInfoListModel> lists) {
        if (this.mLists == null){
            this.mLists = lists;
        } else {
            this.mLists.addAll(lists);
        }
        notifyDataSetChanged();
    }

    public void addNewDataLists(List<LikeAttentionInfoListModel> lists) {
        if (this.mLists == null){
            this.mLists = lists;
        } else {
            this.mLists.addAll(0,lists);
        }
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = mInflater.inflate(R.layout.list_myattention_item,parent,false);
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
        holder.image.setImageResource(R.mipmap.healthicon);
        holder.title.setText(mLists.get(position).follow_pk_person);
        holder.content.setText(mLists.get(position).follow_person_name);
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
            image = (ImageView)view.findViewById(R.id.myattention_item_img);
            title = (TextView)view.findViewById(R.id.myattention_item_title);
            content = (TextView)view.findViewById(R.id.myattention_item_content);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(mContext, MyAttentionDetailActivity.class);
                    mContext.startActivity(intent);
                }
            });
        }
    }
}