package com.example.healthcare;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.model.Medecin;
import com.example.model.Patient;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class PatientListAdapter extends  RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "MedecinListAdapter";
    private ArrayList<Patient> patients = new ArrayList<>();
    private Context mcontext;

    public PatientListAdapter(Context context, ArrayList<Patient> patients){
        this.mcontext = context;
        this.patients = patients;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.patient_item_view,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {

        holder = (ViewHolder) holder;
        Log.d(TAG,"OnBindViewHolder called");

        ((ViewHolder) holder).situation_ville.setText(patients.get(position).getSituation_familiale() +"-" + patients.get(position).getAdresse());
        ((ViewHolder) holder).nom_prenom.setText(patients.get(position).getNom() + " " + patients.get(position).getPrenom());
        ((ViewHolder) holder).plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mcontext,DetailPatient.class);
                Log.d(TAG, ""+ patients.get(position).toString());
                intent.putExtra("Patient", patients.get(position));
                mcontext.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return patients.size();
    }

    public class  ViewHolder extends RecyclerView.ViewHolder{

        CircleImageView image_pat;
        TextView nom_prenom,situation_ville,plus;

        public ViewHolder(View itemView){
            super(itemView);

            image_pat = itemView.findViewById(R.id.image_pat);
            nom_prenom = itemView.findViewById(R.id.nom_prenom);
            situation_ville = itemView.findViewById(R.id.situation_ville);
            plus = itemView.findViewById(R.id.plus);
        }
    }
}
