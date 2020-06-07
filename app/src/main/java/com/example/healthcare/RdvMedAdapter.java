package com.example.healthcare;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.model.Medecin;
import com.example.model.RdvMedecin;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class RdvMedAdapter extends  RecyclerView.Adapter<RdvMedAdapter.RdvMedHolder> {

    private ArrayList<RdvMedecin> rdvMedecins = new ArrayList<>();
    private static final String TAG = "RdvMedAdapter";
    private Context mcontext;
    String mail_med,ref_del,ref_med;
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    public RdvMedAdapter(ArrayList<RdvMedecin> rdvMedecins, Context mcontext) {
        this.rdvMedecins = rdvMedecins;
        this.mcontext = mcontext;
    }

    public String getMail_med() {
        return mail_med;
    }

    public void setMail_med(String mail_med) {
        this.mail_med = mail_med;
    }


    @NonNull
    @Override
    public RdvMedHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rdv_med_item,parent,false);
        RdvMedHolder  rdvMedHolder = new  RdvMedHolder(view);
        return  rdvMedHolder;

    }
    private  void annulerRdvMed(final RdvMedecin rdv){

        db.collection("medecins").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if(task.isSuccessful()){



                    for (QueryDocumentSnapshot d: task.getResult()){

                        Medecin med = d.toObject(Medecin.class);
                        if(med.getEmail().equals(mail_med)){
                            ref_med =d.getId();
                            db.collection("medecins/"+d.getId() + "/rdvsMed").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    Log.d(TAG,"EMPTY_RDVMED : " + task.getResult().isEmpty());
                                    if(task.isSuccessful()){
                                        for (QueryDocumentSnapshot d: task.getResult()){
                                            RdvMedecin rdvMed = d.toObject(RdvMedecin.class);


                                            if(rdvMed.getDate().equals(rdv.getDate()) && rdvMed.getHeure().equals(rdv.getHeure())){
                                                ref_del = d.getId();
                                                Log.d(TAG,"PATH_DEL: " + "medecins/"+d.getId()+ "/rdvsMed"+ref_del);
                                                db.document("medecins/"+d.getId()+ "/rdvsMed/" + ref_del).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if(task.isSuccessful()){
                                                            Log.d(TAG,"RDV_DELETED");
                                                        }
                                                        else {
                                                            Log.d(TAG,"RDV_NOT_DELETED");
                                                        }
                                                    }
                                                });

                                            }

                                        }
                                    }
                                    else {
                                        Log.d(TAG, "TASK_FAILED");
                                    }
                                }
                            });

                        }
                    }


                }
            }
        });
    }

    @Override
    public void onBindViewHolder(@NonNull RdvMedHolder holder, int position) {
        Log.d(TAG,"OnBind_Consult_Clicked");

        final RdvMedecin rdvMedecin = rdvMedecins.get(position);


        holder.nom_patient.setText(rdvMedecin.getPatient().getNom() + "Email : " + rdvMedecin.getPatient().getEmail());
        holder.heure_rdv.setText(rdvMedecin.getHeure());
        String str = holder.jour_rdv.getText().toString();
        holder.jour_rdv.setText(str + " " + rdvMedecin.getDate());
        holder.consulter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Consulter_Clicked");
                Toast.makeText(mcontext,"Consulter_Clicked",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(mcontext,CreerConsultationAct.class);
                intent.putExtra("rdv",rdvMedecin);
                intent.putExtra("mail_med",mail_med);
                mcontext.startActivity(intent);

            }
        });
        holder.annul_rdv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "annuler_Clicked");
                annulerRdvMed(rdvMedecin);
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
