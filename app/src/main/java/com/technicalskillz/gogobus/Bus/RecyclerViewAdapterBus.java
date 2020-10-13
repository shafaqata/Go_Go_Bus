package com.technicalskillz.gogobus.Bus;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.technicalskillz.gogobus.BusActivity;
import com.technicalskillz.gogobus.R;

import java.util.List;

public class RecyclerViewAdapterBus extends RecyclerView.Adapter<RecyclerViewAdapterBus.MyViewHolder> {

    Context ctx;
    public static List<Service> serviceNextBus;

    public RecyclerViewAdapterBus(List<Service> bus, Context context) {
        serviceNextBus = bus;
        ctx = context;
    }


    @NonNull
    @Override
    public RecyclerViewAdapterBus.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_bus, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapterBus.MyViewHolder holder, int position) {



        String str1 = serviceNextBus.get(position).getNextBus().getEstimatedArrival();
        String str2 = serviceNextBus.get(position).getNextBus2().getEstimatedArrival();
        String str3 = serviceNextBus.get(position).getNextBus3().getEstimatedArrival();

        char[] ch1 = str1.toCharArray();
        char[] ch2 = str2.toCharArray();
        char[] ch3 = str3.toCharArray();

        char[] time1 = new char[20];
        char[] time2 = new char[20];
        char[] time3 = new char[20];

        for (int i = 11; i <= 15; i++) {
            if (str1.length() > 16) {
                 time1[i] = ch1[i];
            }
            if (str2.length() > 16) {
                time2[i] = ch2[i];
            }
            if (str3.length() > 16) {
                time3[i] = ch3[i];
            }
        }


        holder.ServiceID.setText(serviceNextBus.get(position).getServiceNo());
        if (new String(time1).trim().length()>0)
        {

            holder.EstimatedArrival1.setText(new String(time1).toString().trim()+"");
        }
        if (new String(time1).trim().length()>0)
        {
            holder.EstimatedArrival2.setText(new String(time2).toString().trim()+"");
        }
        if (new String(time1).trim().length()>0)
        {
           holder.EstimatedArrival3.setText(new String(time3).toString().trim()+"");
        }


    }

    @Override
    public int getItemCount() {
        return serviceNextBus.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

//        TextView DestinationCode1, EstimatedArrival1, Latitude1, Longitude1;
//        TextView DestinationCode2, EstimatedArrival2, Latitude2, Longitude2;
//        TextView DestinationCode3, EstimatedArrival3, Latitude3, Longitude3;
//        CardView cardView1, cardView2, cardView3;

        TextView EstimatedArrival1, EstimatedArrival2, EstimatedArrival3, ServiceID;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            EstimatedArrival1 = itemView.findViewById(R.id.EstimatedArrival1);
            EstimatedArrival2 = itemView.findViewById(R.id.EstimatedArrival2);
            EstimatedArrival3 = itemView.findViewById(R.id.EstimatedArrival3);
            ServiceID = itemView.findViewById(R.id.ServiceID);

//            DestinationCode1 = itemView.findViewById(R.id.DestinationCode1);
//            EstimatedArrival1 = itemView.findViewById(R.id.EstimatedArrival1);
//            Latitude1 = itemView.findViewById(R.id.Latitude1);
//            Longitude1 = itemView.findViewById(R.id.Longitude1);
//
//            DestinationCode2 = itemView.findViewById(R.id.DestinationCode2);
//            EstimatedArrival2 = itemView.findViewById(R.id.EstimatedArrival2);
//            Latitude2 = itemView.findViewById(R.id.Latitude2);
//            Longitude2 = itemView.findViewById(R.id.Longitude2);
//
//            DestinationCode3 = itemView.findViewById(R.id.DestinationCode3);
//            EstimatedArrival3 = itemView.findViewById(R.id.EstimatedArrival3);
//            Latitude3 = itemView.findViewById(R.id.Latitude3);
//            Longitude3 = itemView.findViewById(R.id.Longitude3);
//
//            cardView1 = itemView.findViewById(R.id.card1);
//            cardView2 = itemView.findViewById(R.id.card2);
//            cardView3 = itemView.findViewById(R.id.card3);


        }
    }
}
