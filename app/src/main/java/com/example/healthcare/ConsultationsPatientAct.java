package com.example.healthcare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.model.Consultation;
import com.example.model.Patient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ConsultationsPatientAct extends AppCompatActivity {

    ImageView home;
    String mail_pat;
    ConsultPatAdapter adapter;
    RecyclerView list_pat;
    ArrayList<Consultation> cons = new ArrayList<>();
    RecyclerView.LayoutManager layoutManager;
    private static final String TAG = "ConsultationsPatientAct";
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultations_patient);
        home = findViewById(R.id.home);
        Intent i = getIntent();
        mail_pat = i.getStringExtra("mail_pat");

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ConsultationsPatientAct.this,EspacePatientActivity.class);
                intent.putExtra("mail_pat",mail_pat);
                startActivity(intent);
            }
        });
        initListConsultations();

        list_pat = findViewById(R.id.pat_consultations);
        list_pat.setHasFixedSize(true);
        adapter = new ConsultPatAdapter(cons,this);
        layoutManager  = new LinearLayoutManager(this);
        list_pat.setAdapter(adapter);
        list_pat.setLayoutManager(layoutManager);

    }


    private  void initListConsultations(){

        db.collection("patients").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                Log.d(TAG,"SUCSS_List_pat");

                for (QueryDocumentSnapshot d: queryDocumentSnapshots){
                    Patient pat = d.toObject(Patient.class);
                    if(pat.getEmail().equals(mail_pat)){
                        db.document(d.getReference().getPath()).collection("consultationsPat").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                if(task.isSuccessful()){
                                    for (QueryDocumentSnapshot d: task.getResult()){
                                        Consultation c = d.toObject(Consultation.class);
                                        cons.add(c);
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
                Log.d(TAG,"Failed_List_pat");
            }
        });

    }
}
