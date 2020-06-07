package com.example.healthcare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.model.Consultation;
import com.example.model.Medecin;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ConsultationsMedActi extends AppCompatActivity {

    String  mail_med;
    ArrayList<Consultation> cons = new ArrayList<>();
    ConsultMedAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView list_cons;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = "ConsultationsMedActi";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultations);
        Intent intent = getIntent();
        mail_med = intent.getStringExtra("mail_med");
        list_cons = findViewById(R.id.med_consultations);

        initConsList();
        list_cons.setHasFixedSize(true);
        adapter = new ConsultMedAdapter(cons,this);
        layoutManager = new LinearLayoutManager(this);
        list_cons.setAdapter(adapter);
        list_cons.setLayoutManager(layoutManager);
    }

    private  void initConsList(){
        db.collection("medecins").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    Log.d(TAG,"LisT_Med");
                    for (QueryDocumentSnapshot d: task.getResult()){
                        Medecin med = d.toObject(Medecin.class);

                        if(med.getEmail().equals(mail_med)){
                            Log.d(TAG,"LisT_Med : " + mail_med);
                            db.collection("medecins/"+d.getId() + "/consultationsMed").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    Log.d(TAG,"CAN_LIST_CONS");

                                    for (QueryDocumentSnapshot d: queryDocumentSnapshots){
                                        Consultation c = d.toObject(Consultation.class);
                                        cons.add(c);
                                        adapter.notifyDataSetChanged();
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG,"CANT_LIST_CONS : " + e.getMessage());
                                }
                            });
                        }
                    }
                }
                else {
                    Log.d(TAG,"PB_LIST_MED");
                }
            }
        });
    }
}
