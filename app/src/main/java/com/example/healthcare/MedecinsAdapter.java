package com.example.healthcare;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.model.Medecin;

import java.util.ArrayList;

public class MedecinsAdapter extends RecyclerView.Adapter<MedecinItemView> {
    private ArrayList<Medecin> medecins;
    @NonNull
    @Override
    public MedecinItemView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull MedecinItemView holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}

