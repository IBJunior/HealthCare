package com.example.healthcare;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.model.Patient;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class PatientAdapter  extends  RecyclerView.Adapter<PatientAdapter.PatientHolder>{
    public PatientAdapter(ArrayList<Patient> patients, Context mcontext) {
        this.patients = patients;
        this.mcontext = mcontext;
    }

    private ArrayList<Patient> patients = new ArrayList<>();
    private Context mcontext;
    private String mail_med;

    public String getMail_med() {
        return mail_med;
    }

    public void setMail_med(String mail_med) {
        this.mail_med = mail_med;
    }

    @NonNull
    @Override
    public PatientHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.patient_item_view,parent,false);
        PatientHolder holder = new PatientHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull PatientHolder holder, int position) {
        final Patient pat = patients.get(position);
        holder.ville_patient.setText(pat.getAdresse());
        holder.nom_prenom.setText(pat.getNom() + " " + pat.getPrenom());
        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mcontext,DetailPatient.class);
                intent.putExtra("patient",pat);
                intent.putExtra("mail_med",mail_med);
                mcontext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return patients.size();
    }

    public static  class  PatientHolder extends RecyclerView.ViewHolder{
        CircleImageView image_pat;
        TextView nom_prenom,ville_patient;
        LinearLayout plus;

        public PatientHolder(@NonNull View itemView) {
            super(itemView);
            image_pat = itemView.findViewById(R.id.image_pat);
            nom_prenom = itemView.findViewById(R.id.nom_prenom);
            ville_patient = itemView.findViewById(R.id.ville_pat);
            plus = itemView.findViewById(R.id.plus);
        }
    }
}
