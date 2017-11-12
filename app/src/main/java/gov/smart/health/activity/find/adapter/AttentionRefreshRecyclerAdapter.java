package gov.smart.health.activity.find.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import gov.smart.health.R;
import gov.smart.health.activity.find.DetailActivity;
import gov.smart.health.activity.find.model.FindAttentionListDataModel;
import gov.smart.health.activity.find.model.FindEssayListDataModel;
import gov.smart.health.utils.SHConstants;

/**
 * Created by laoniu on 2017/07/23.
 */

public class AttentionRefreshRecyclerAdapter extends RecyclerView.Adapter<AttentionRefreshRecyclerAdapter.ViewHolder>{
    private LayoutInflater mInflater;
    private List<FindAttentionListDataModel> mLists;
    private Context mContext;

    public AttentionRefreshRecyclerAdapter(Context context , List<FindAttentionListDataModel> lists){
        mContext = context;
        this.mInflater=LayoutInflater.from(context);
        this.mLists = lists;
    }

    public void addDataLists(List<FindAttentionListDataModel> lists) {
        if (this.mLists == null){
            this.mLists = lists;
        } else {
            this.mLists.addAll(lists);
        }
        notifyDataSetChanged();
    }

    public void addNewDataLists(List<FindAttentionListDataModel> lists) {
        if (this.mLists == null){
            this.mLists = lists;
        } else {
            this.mLists.addAll(0,lists);
        }
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = mInflater.inflate(R.layout.list_attention_item,parent,false);
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
        final FindAttentionListDataModel model = mLists.get(position);

        holder.image.setImageResource(R.mipmap.healthicon);
        holder.title.setText(model.record_share_name);
        holder.content.setText(Html.fromHtml(model.record_share_desc));
        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra(SHConstants.PersonAttentionModelKey,model);
                intent.setClass(mContext, DetailActivity.class);
                mContext.startActivity(intent);
            }
        });
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
            image = (ImageView)view.findViewById(R.id.attention_item_img);
            title = (TextView)view.findViewById(R.id.attention_item_title);
            content = (TextView)view.findViewById(R.id.attention_item_content);
        }
    }
}