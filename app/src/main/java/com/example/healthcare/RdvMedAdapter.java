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

import com.example.model.RdvMedecin;

import java.util.ArrayList;

public class RdvMedAdapter extends  RecyclerView.Adapter<RdvMedAdapter.RdvMedHolder> {

    private ArrayList<RdvMedecin> rdvMedecins = new ArrayList<>();
    private static final String TAG = "RdvMedAdapter";
    private Context mcontext;

    public RdvMedAdapter(ArrayList<RdvMedecin> rdvMedecins, Context mcontext) {
        this.rdvMedecins = rdvMedecins;
        this.mcontext = mcontext;
    }

    @NonNull
    @Override
    public RdvMedHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rdv_med_item,parent,false);
        RdvMedHolder  rdvMedHolder = new  RdvMedHolder(view);
        return  rdvMedHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull RdvMedHolder holder, int position) {
        Log.d(TAG,"OnBind_Consult_Clicked");

        RdvMedecin rdvMedecin = rdvMedecins.get(position);


        holder.nom_patient.setText(rdvMedecin.getPatient().getNom() + "Email : " + rdvMedecin.getPatient().getEmail());
        holder.heure_rdv.setText(rdvMedecin.getHeure());
        String str = holder.jour_rdv.getText().toString();
        holder.jour_rdv.setText(str + " " + rdvMedecin.getDate());
        holder.consulter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Consulter_Clicked");
                Toast.makeText(mcontext,"Consulter_Clicked",Toast.LENGTH_LONG).show();
            }
        });
        holder.annul_rdv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "annuler_Clicked");
                Toast.makeText(mcontext,"annuler_Clicked",Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return rdvMedecins.size();
    }

    public static class  RdvMedHolder extends RecyclerView.ViewHolder{

        TextView nom_patient, heure_rdv,jour_rdv;
        Button consulter, annul_rdv;

        public RdvMedHolder(@NonNull View itemView) {
            super(itemView);

            nom_patient = itemView.findViewById(R.id.nom_patient);
            heure_rdv = itemView.findViewById(R.id.heure_rdv);
            consulter = itemView.findViewById(R.id.consulter);
            annul_rdv = itemView.findViewById(R.id.annul_rdv);
            jour_rdv = itemView.findViewById(R.id.jour_rdv);
        }
    }
}
