package com.codershil.whatsappstatussaver;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class StatusAdapter extends RecyclerView.Adapter<StatusViewHolder> {

    // adapter class for showing statuses in recyclerview

    Context context;
    ArrayList<StatusModel> filesList;
    StatusClickListener statusClickListener;


    public StatusAdapter(Context context, ArrayList<StatusModel> filesList, StatusClickListener statusClickListener) {
        this.context = context;
        this.filesList = filesList;
        this.statusClickListener = statusClickListener;
    }

    @NonNull
    @Override
    public StatusViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_status, null, false);
        return new StatusViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StatusViewHolder holder, int position) {
        StatusModel statusModel = filesList.get(position);
        if (statusModel.getUri().toString().endsWith(".mp4")) {
            holder.getPlay().setVisibility(View.VISIBLE);
        } else {
            holder.getPlay().setVisibility(View.INVISIBLE);
        }
        Glide.with(context).load(statusModel.getUri()).into(holder.getMainStatus());

        holder.getMainStatus().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statusClickListener.onStatusClicked(holder);
            }
        });
    }

    @Override
    public int getItemCount() {
        return filesList.size();
    }

    public void updateStatusData(ArrayList<StatusModel> filesList) {
        this.filesList = filesList;
        notifyDataSetChanged();
    }

}
