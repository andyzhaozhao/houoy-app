package gov.smart.health.activity.vr.adapter;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import gov.smart.health.R;
import gov.smart.health.activity.vr.model.VideoFolderListModel;
import gov.smart.health.utils.SHConstants;

/**
 * Created by laoniu on 2017/07/23.
 */

public class SportAreaRefreshRecyclerAdapter extends RecyclerView.Adapter<SportAreaRefreshRecyclerAdapter.ViewHolder>{
    private LayoutInflater mInflater;
    private List<VideoFolderListModel> mLists;
    private Activity mActivity;

    public SportAreaRefreshRecyclerAdapter(Activity activity , List<VideoFolderListModel> lists){
        mActivity = activity;
        this.mInflater=LayoutInflater.from(activity);
        this.mLists = lists;
    }

    public void addDataLists(List<VideoFolderListModel> lists) {
        if (this.mLists == null){
            this.mLists = lists;
        } else {
            this.mLists.addAll(lists);
        }
        notifyDataSetChanged();
    }

    public void addNewDataLists(List<VideoFolderListModel> lists) {
        if (this.mLists == null){
            this.mLists = lists;
        } else {
            this.mLists.addAll(0,lists);
        }
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = mInflater.inflate(R.layout.sport_area_list_item,parent,false);
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
        final VideoFolderListModel model = mLists.get(position);
        holder.content.setText(model.folder_name);
        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mActivity);
                alertDialogBuilder.setMessage("确认选择[" +model.folder_name +"]这个地点？");
                alertDialogBuilder.setPositiveButton("取消",null);
                alertDialogBuilder.setNeutralButton("好的",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent();
                                intent.putExtra(SHConstants.Video_Floder_Key,model);
                                mActivity.setResult(0,intent);
                                mActivity.finish();
                            }
                        });
                alertDialogBuilder.setCancelable(true);
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

            }
        });
    }
    @Override
    public int getItemCount() {
        return this.mLists == null? 0 : this.mLists.size();
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView content;

        public ViewHolder(View view){
            super(view);
            content = (TextView)view.findViewById(R.id.sport_area_item_content);
        }
    }
}