package com.example.healthcare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.model.Consultation;
import com.example.model.Medecin;
import com.example.model.Patient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class PatientListActi extends AppCompatActivity {
    RecyclerView list_patients;
    PatientAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<Patient> patients = new ArrayList<>();
    String mail_med;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = "PatientListActi";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_list);

        Intent i = getIntent();
        mail_med = i.getStringExtra("mail_med");
        list_patients = findViewById(R.id.list_patients);
        list_patients.setHasFixedSize(true);
        adapter = new PatientAdapter(patients,this);
        layoutManager = new LinearLayoutManager(this);
        list_patients.setAdapter(adapter);
        list_patients.setLayoutManager(layoutManager);
        initListPatients();

    }

    private void recupPatienst(String path){
        Log.d(TAG,"RECUPPAT called");
       final ArrayList<Consultation> cons = new ArrayList<>();
        db.document(path).collection("consultationsMed").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                Log.d(TAG,"SUCC_CONS");
                Log.d(TAG,"CONSEMPTY " + queryDocumentSnapshots.isEmpty());

                for (QueryDocumentSnapshot d:queryDocumentSnapshots){
                    cons.add(d.toObject(Consultation.class));
                }
                Log.d(TAG,"consEMpty :" + cons.isEmpty());
                for (final Consultation c: cons){
                    Log.d(TAG,"MAILPAT :" + c.getNomPatient());
                    db.collection("patients").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                            if(task.isSuccessful()){
                                for (QueryDocumentSnapshot d: task.getResult()){
                                    Patient pat =  d.toObject(Patient.class);

                                    if(pat.getEmail().equals(c.getNomPatient())){

                                        patients.add(pat);
                                        adapter.notifyDataSetChanged();
                                    }
                                }
                            }
                        }
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG,"FAILED_CONS");
            }
        });



    }

    private void initListPatients(){

        db.collection("medecins").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()){
                    Log.d(TAG,"MAIL_MED : " + mail_med);
                    Log.d(TAG,"PAS_PB_LIST_MED");
                    Log.d(TAG,"ISEMPTY_: " + task.getResult().isEmpty());

                    for (QueryDocumentSnapshot d: task.getResult()){
                        Medecin med = d.toObject(Medecin.class);

                        if(med.getEmail().equals(mail_med)){
                            Log.d(TAG,"IF_STAT");
                            recupPatienst(d.getReference().getPath());
                            adapter.notifyDataSetChanged();
                        }
                    }
                }else {
                    Log.d(TAG,"PB_LIST_MED");
                }

            }
        });

    }
}
