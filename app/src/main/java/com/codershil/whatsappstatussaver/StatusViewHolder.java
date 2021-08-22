package com.codershil.whatsappstatussaver;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class StatusViewHolder extends RecyclerView.ViewHolder {

    private ImageView mainStatus, play;

    public StatusViewHolder(@NonNull View itemView) {
        super(itemView);
        mainStatus = itemView.findViewById(R.id.thumbnailOfStatus);
        play = itemView.findViewById(R.id.play);
    }

    public ImageView getMainStatus() {
        return mainStatus;
    }

    public ImageView getPlay() {
        return play;
    }
}
