package com.example.healthcare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.model.Patient;
import com.example.model.RdvPatient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class RdvPatientList extends AppCompatActivity {
    RecyclerView list_pat_rdv;
    RdvPatientAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
    String mail_pat;
    private ArrayList<RdvPatient> rdvPatients = new ArrayList<>();
    private static final String TAG = "RdvPatientList";
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rdv_patient_list);

        Intent i = getIntent();

        mail_pat = i.getStringExtra("mail_pat");
        initRdvsPatient();

        list_pat_rdv = findViewById(R.id.list_rdv_pat);
        list_pat_rdv.setHasFixedSize(true);
        adapter = new RdvPatientAdapter(rdvPatients,this);
       layoutManager = new LinearLayoutManager(this);
       list_pat_rdv.setLayoutManager(layoutManager);
       list_pat_rdv.setAdapter(adapter);
    }

    private  void  initRdvsPatient(){

        db.collection("patients").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                for (QueryDocumentSnapshot d: queryDocumentSnapshots){
                    Patient pat = d.toObject(Patient.class);

                    if(pat.getEmail().equals(mail_pat)){
                        db.collection("patients/"+d.getId() + "/rdvsPat").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                if (task.isSuccessful()){
                                    for (QueryDocumentSnapshot d: task.getResult()){
                                        RdvPatient rdv = d.toObject(RdvPatient.class);
                                        rdvPatients.add(rdv);
                                        adapter.notifyDataSetChanged();
                                    }
                                }

                            }
                        });
                    }
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }
}
