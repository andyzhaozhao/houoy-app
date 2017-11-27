package gov.smart.health.activity.self.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import gov.smart.health.R;
import gov.smart.health.activity.self.model.LikeRecordHistoryInfoListModel;

/**
 * Created by laoniu on 2017/07/23.
 */

public class SportHistoryRecyclerAdapter extends RecyclerView.Adapter<SportHistoryRecyclerAdapter.ViewHolder>{
    private LayoutInflater mInflater;
    private List<LikeRecordHistoryInfoListModel> mLists;
    private Context mContext;

    public SportHistoryRecyclerAdapter(Context context , List<LikeRecordHistoryInfoListModel> lists){
        mContext = context;
        this.mInflater=LayoutInflater.from(context);
        this.mLists = lists;
    }

    public void addDataLists(List<LikeRecordHistoryInfoListModel> lists) {
        if (this.mLists == null){
            this.mLists = lists;
        } else {
            this.mLists.addAll(lists);
        }
        notifyDataSetChanged();
    }

    public void addNewDataLists(List<LikeRecordHistoryInfoListModel> lists) {
        if (this.mLists == null){
            this.mLists = lists;
        } else {
            this.mLists.addAll(0,lists);
        }
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = mInflater.inflate(R.layout.sport_history_list_item,parent,false);
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
        LikeRecordHistoryInfoListModel model = mLists.get(position);
        holder.sportStartTime.setText(model.time_start);
        holder.sportEndTime.setText(model.time_end);
        holder.sportTime.setText("耗时"+model.time_length+"秒");
        holder.sportPlace.setText(model.place_name);
        holder.sportVideo.setText(model.video_name);
        holder.sportRate.setText(model.heart_rate + "次/秒");
        holder.sportCal.setText(model.indicator_calorie_min == null ? "1cal" : model.indicator_calorie_min+ "cal");
        holder.itemView.setTag(position);
    }
    @Override
    public int getItemCount() {
        return this.mLists == null? 0 : this.mLists.size();
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView sportStartTime;
        public TextView sportEndTime;
        public TextView sportTime;
        public TextView sportPlace;
        public TextView sportVideo;
        public TextView sportRate;
        public TextView sportCal;

        public ViewHolder(View view){
            super(view);
            sportStartTime = (TextView)view.findViewById(R.id.item_history_startdate);
            sportEndTime = (TextView)view.findViewById(R.id.item_history_enddate);
            sportTime = (TextView)view.findViewById(R.id.item_history_time);
            sportPlace = (TextView)view.findViewById(R.id.item_history_place);
            sportVideo = (TextView)view.findViewById(R.id.item_history_video_name);
            sportRate = (TextView)view.findViewById(R.id.item_history_heart_rate);
            sportCal = (TextView)view.findViewById(R.id.item_history_cal);
        }
    }
}