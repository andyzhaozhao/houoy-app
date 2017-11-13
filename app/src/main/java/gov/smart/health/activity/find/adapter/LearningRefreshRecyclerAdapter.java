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

import com.androidnetworking.widget.ANImageView;

import java.util.List;

import gov.smart.health.R;
import gov.smart.health.activity.find.DetailActivity;
import gov.smart.health.activity.find.model.FindEssayListDataModel;
import gov.smart.health.utils.SHConstants;

/**
 * Created by laoniu on 2017/07/23.
 */

public class LearningRefreshRecyclerAdapter extends RecyclerView.Adapter<LearningRefreshRecyclerAdapter.ViewHolder>{
    private LayoutInflater mInflater;
    private List<FindEssayListDataModel> mLists;
    private Context mContext;

    public LearningRefreshRecyclerAdapter(Context context ,List<FindEssayListDataModel> lists){
        mContext = context;
        this.mInflater=LayoutInflater.from(context);
        this.mLists = lists;
    }

    public void addDataLists(List<FindEssayListDataModel> lists) {
        if (this.mLists == null){
            this.mLists = lists;
        } else {
            this.mLists.addAll(lists);
        }
        notifyDataSetChanged();
    }

    public void addNewDataLists(List<FindEssayListDataModel> lists) {
        if (this.mLists == null){
            this.mLists = lists;
        } else {
            this.mLists.addAll(0,lists);
        }
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = mInflater.inflate(R.layout.learning_list_item,parent,false);
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
        final FindEssayListDataModel model = mLists.get(position);
        holder.image.setDefaultImageResId(R.mipmap.healthicon);
        holder.image.setErrorImageResId(R.mipmap.healthicon);
        holder.image.setImageUrl(model.path_thumbnail);

        holder.title.setText(model.essay_name);
        holder.content.setText(Html.fromHtml(model.essay_content));
        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra(SHConstants.PersonFlowModelKey,model);
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
        public ANImageView image;
        public TextView title;
        public TextView content;

        public ViewHolder(View view){
            super(view);
            image = (ANImageView)view.findViewById(R.id.learning_item_img);
            title = (TextView)view.findViewById(R.id.learning_item_title);
            content = (TextView)view.findViewById(R.id.learning_item_content);
        }
    }
}