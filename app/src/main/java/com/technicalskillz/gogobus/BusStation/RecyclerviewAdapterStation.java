package com.technicalskillz.gogobus.BusStation;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.technicalskillz.gogobus.Bus.Service;
import com.technicalskillz.gogobus.BusActivity;
import com.technicalskillz.gogobus.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RecyclerviewAdapterStation extends RecyclerView.Adapter<RecyclerviewAdapterStation.MyViewHolder> implements Filterable {

    List<Station> list;
    List<Station> listFull;
    Context ctx;

    public RecyclerviewAdapterStation(List<Station> list, Context ctx) {
        this.list = list;
        this.ctx = ctx;
        listFull = new ArrayList<>(list);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_view_stations, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.BusStopCode.setText("(" + list.get(position).getBusStopCode() + ")");
        holder.Description.setText(list.get(position).getDescription());
        holder.RoadName.setText(list.get(position).getRoadName() + "");



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ctx, BusActivity.class);
                intent.putExtra("BusStopCode",list.get(position).getBusStopCode());
                intent.putExtra("Latitude",list.get(position).getLatitude()+"");
                intent.putExtra("Longitude",list.get(position).getLongitude()+"");
                ctx.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public Filter getFilter() {
        return FilterData;
    }

    private Filter FilterData = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<Station> temList = new ArrayList<>();
            if (charSequence == null || charSequence.length() == 0) {
                temList.addAll(listFull);
            } else {
                String filterPatter = charSequence.toString().toLowerCase().toString();
                for (Station items:listFull)
                {
                    if (items.getRoadName().toLowerCase().contains(filterPatter) || items.getBusStopCode().toLowerCase().contains(filterPatter))
                    {
                        temList.add(items);
                    }
                }
            }
            FilterResults filterResults=new FilterResults();
            filterResults.values=temList;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                list.clear();;
                list.addAll((Collection<? extends Station>) filterResults.values);
                notifyDataSetChanged();
        }
    };

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView BusStopCode, RoadName, Description, Latitude, Longitude;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            BusStopCode = itemView.findViewById(R.id.busStopCode);
            RoadName = itemView.findViewById(R.id.roadName);
            Description = itemView.findViewById(R.id.Description);
//            Latitude=itemView.findViewById(R.id.Latitude);
//            Longitude=itemView.findViewById(R.id.Longitude);
        }
    }
}
