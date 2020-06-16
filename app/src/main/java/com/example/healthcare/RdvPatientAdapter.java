package com.example.healthcare;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.model.RdvPatient;

import java.util.ArrayList;

public class RdvPatientAdapter  extends RecyclerView.Adapter<RdvPatientAdapter.RdvPatientHolder>{

    private ArrayList<RdvPatient> rdvPatients = new ArrayList<>();
    private static final String TAG = "RdvPatientAdapter";
    private Context mcontext;

    public RdvPatientAdapter(ArrayList<RdvPatient> rdvPatients, Context mcontext) {
        this.rdvPatients = rdvPatients;
        this.mcontext = mcontext;
    }

    @NonNull
    @Override
    public RdvPatientHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rdv_pat_item,parent,false);
        RdvPatientHolder rdv = new RdvPatientHolder(view);
        return rdv;
    }

    @Override
    public void onBindViewHolder(@NonNull RdvPatientHolder holder, int position) {
        final RdvPatient rdv = rdvPatients.get(position);
        holder.nom_medecin.setText("Dr "+rdv.getMedecin().getNom());
        holder.heure_rdv.setText(rdv.getHeure());
        holder.jour_rdv.setText(rdv.getDate());
        holder.annul_rdv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mcontext,"Rende-vous annulé",Toast.LENGTH_LONG).show();
                rdvPatients.remove(rdv);
                notifyDataSetChanged();
            }
        });



    }

    @Override
    public int getItemCount() {
        return rdvPatients.size();
    }

    public  static  class RdvPatientHolder extends RecyclerView.ViewHolder{
        TextView nom_medecin, heure_rdv,jour_rdv;
        Button  annul_rdv;

        public RdvPatientHolder(@NonNull View itemView) {
            super(itemView);
            nom_medecin = itemView.findViewById(R.id.nom_medecin);
            heure_rdv = itemView.findViewById(R.id.heure_rdv);
            annul_rdv = itemView.findViewById(R.id.annul_rdv);
            jour_rdv = itemView.findViewById(R.id.jour_rdv);
        }
    }
}
