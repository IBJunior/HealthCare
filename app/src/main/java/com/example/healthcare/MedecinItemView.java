package com.example.healthcare;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MedecinItemView extends RecyclerView.ViewHolder {
    private TextView textView2,textView;
    public MedecinItemView(@NonNull View itemView) {
        super(itemView);
       textView = itemView.findViewById(R.id.textView);
       textView2 = itemView.findViewById(R.id.textView2);
    }
}
