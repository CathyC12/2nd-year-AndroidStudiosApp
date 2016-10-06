package ayry.com.ary_app;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kearn on 18/04/2016.
 */
public class EventsSearchAdapter extends RecyclerView.Adapter<EventsSearchAdapter.ViewHolder> {

    Context context;
    List<EventsModel> eventsModelList;
    OnItemClickListener clickListener;

    public  EventsSearchAdapter(Context context){
        this.context = context;
        eventsModelList = new ArrayList<>();
    }

    public void clear(){
        if(eventsModelList != null && !eventsModelList.isEmpty()){
            eventsModelList.clear();
            notifyDataSetChanged();
        }
    }
    public void addAll(List<EventsModel> eventsModelList){
        this.eventsModelList.addAll(eventsModelList);
        notifyDataSetChanged();
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(context).inflate(R.layout.events_result_row_item, parent, false);
        return new ViewHolder(view);
    }




    @Override
    public void onBindViewHolder(ViewHolder holder, int position, List<Object> payloads) {
        final  EventsModel events = eventsModelList.get(position);
        holder.itemHolder.title.setText(events.getTitle());
        holder.itemHolder.catagory.setText(events.getCatagory());
        holder.itemHolder.date.setText(events.getDate());
        holder.itemHolder.address.setText(events.getLocatiion());
        holder.itemHolder.time.setText(events.getTime());


    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final  EventsModel events = eventsModelList.get(position);
        holder.itemHolder.title.setText(events.getTitle());
        holder.itemHolder.catagory.setText(events.getCatagory());
        holder.itemHolder.date.setText(events.getDate());
        holder.itemHolder.address.setText(events.getLocatiion());
        holder.itemHolder.time.setText(events.getTime());

        //holder.itemHolder.image.setImageDrawable(context.getResources().getDrawable(R.drawable.monument));
    }

    @Override
    public int getItemCount() {
        return eventsModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ItemHolder itemHolder;
        public ViewHolder(View itemView) {
            super(itemView);

            itemHolder = new ItemHolder();
            itemHolder.title = (TextView) itemView.findViewById(R.id.titleTv);
            itemHolder.catagory = (TextView) itemView.findViewById(R.id.catagoryTv);
            itemHolder.address = (TextView) itemView.findViewById(R.id.addressTv);
            itemHolder.date = (TextView) itemView.findViewById(R.id.event_dateTv);
            itemHolder.time = (TextView) itemView.findViewById(R.id.event_timeTv);
            itemHolder.image = (ImageButton) itemView.findViewById(R.id.imageButton);
            itemHolder.holder = (LinearLayout) itemView.findViewById(R.id.mainHolder);

            itemHolder.holder.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(clickListener != null){
                clickListener.onItemClick(v, getPosition());
            }
        }
    }

    public interface OnItemClickListener{
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener clickListener){
        this.clickListener = clickListener;
    }

    public static class ItemHolder{
        TextView title, catagory, address, date, time;
        ImageButton image;
        LinearLayout holder;
    }


}

