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
import android.widget.TextView;
import android.widget.Toast;

import com.example.login.LoginActivity;
import com.example.model.Consultation;
import com.example.model.Medecin;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ConsultationsMedActi extends AppCompatActivity {

    String  mail_med;
    ImageView home;
    ArrayList<Consultation> cons = new ArrayList<>();
    ConsultMedAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
    TextView deconnect;
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
        home = findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(ConsultationsMedActi.this,EspaceMedecinActivity.class);
                intent1.putExtra("mail_med",mail_med);
                startActivity(intent1);
            }
        });
        deconnect = findViewById(R.id.deconnexion);
        deconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(ConsultationsMedActi.this, LoginActivity.class));

            }
        });

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

        if (cons.isEmpty()){
            Toast.makeText(ConsultationsMedActi.this, "Vous n'avez aucune consultation ",Toast.LENGTH_LONG).show();
        }
    }
}
