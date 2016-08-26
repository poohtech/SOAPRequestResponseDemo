package com.example.soaprequestresponsedemo;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.MyViewHolder> {

    private ArrayList<ListBean> moviesList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtLat, txtLon;

        public MyViewHolder(View view) {
            super(view);
            txtLat = (TextView) view.findViewById(R.id.txtLat);
            txtLon = (TextView) view.findViewById(R.id.txtLon);
        }
    }


    public ListAdapter(ArrayList<ListBean> moviesList) {
        this.moviesList = moviesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.txtLat.setText(String.valueOf(moviesList.get(position).lat));
        holder.txtLon.setText(String.valueOf(moviesList.get(position).log));
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }
}