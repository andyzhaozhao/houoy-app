package gov.smart.health.adapter;

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
import gov.smart.health.activity.vr.VTOVRPlayerActivity;
import gov.smart.health.activity.vr.model.SportVideoListModel;
import gov.smart.health.model.SportModel;

/**
 * Created by laoniu on 2017/07/23.
 */

public class SportRefreshRecyclerAdapter extends RecyclerView.Adapter<SportRefreshRecyclerAdapter.ViewHolder>{
    private LayoutInflater mInflater;
    private List<SportVideoListModel> mLists;
    private Context mContext;

    public SportRefreshRecyclerAdapter(Context context , List<SportVideoListModel> lists){
        mContext = context;
        this.mInflater=LayoutInflater.from(context);
        this.mLists = lists;
    }

    public void addDataLists(List<SportVideoListModel> lists) {
        if (this.mLists == null){
            this.mLists = lists;
        } else {
            this.mLists.addAll(lists);
        }
        notifyDataSetChanged();
    }

    public void addNewDataLists(List<SportVideoListModel> lists) {
        if (this.mLists == null){
            this.mLists = lists;
        } else {
            this.mLists.addAll(0,lists);
        }
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = mInflater.inflate(R.layout.sport_list_item,parent,false);
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
        holder.title.setText(mLists.get(position).video_name);
        holder.time.setText(mLists.get(position).video_desc);
        //holder.download.setText(mLists.get(position).getDownload() ? "已下载":"未下载");
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
        public TextView time;
        public TextView download;

        public ViewHolder(View view){
            super(view);
            image = (ImageView)view.findViewById(R.id.sport_item_img);
            title = (TextView)view.findViewById(R.id.sport_item_title);
            time = (TextView)view.findViewById(R.id.sport_item_time);
            download = (TextView)view.findViewById(R.id.sport_item_download);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(mContext, VTOVRPlayerActivity.class);
                    mContext.startActivity(intent);
                }
            });
        }
    }
}