package com.example.healthcare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.model.Medecin;
import com.example.model.RdvMedecin;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class RdvMedList extends AppCompatActivity {

    ArrayList<RdvMedecin> rdvMedecins = new ArrayList<>();
    String mail_med;
    RecyclerView listRdvsMed;
    RdvMedAdapter rdvMedAdapter;
    RecyclerView.LayoutManager layoutManager;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = "RdvMedList";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rdv_med_list);

        initListRdv();
        Intent i = getIntent();
        mail_med = i.getStringExtra("mail_med");
        Log.d(TAG,"MAIL_MED_ :" + mail_med);
        listRdvsMed = findViewById(R.id.list_rdv_med);
        listRdvsMed.setHasFixedSize(true);
        rdvMedAdapter = new RdvMedAdapter(rdvMedecins,this);
        layoutManager =  new LinearLayoutManager(this);
        listRdvsMed.setLayoutManager(layoutManager);
        listRdvsMed.setAdapter(rdvMedAdapter);
        rdvMedAdapter.setMail_med(mail_med);


    }

    private  void  initListRdv(){

        db.collection("medecins").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                Log.d(TAG,"SingleMedFound" );
                Log.d(TAG,"ISEMPTY + " + queryDocumentSnapshots.isEmpty());

                for (QueryDocumentSnapshot document : queryDocumentSnapshots){
                    Log.d(TAG,"IDD : " +document.getId());
                    Medecin med = document.toObject(Medecin.class);

                    if(med.getEmail().equals(mail_med)){
                        db.collection("medecins/"+document.getId() + "/rdvsMed").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                if (task.isSuccessful()){
                                    Log.d(TAG,"ListRdvGet");

                                    for (QueryDocumentSnapshot d: task.getResult()){
                                        RdvMedecin rdv = d.toObject(RdvMedecin.class);
                                        rdvMedecins.add(rdv);
                                        rdvMedAdapter.notifyDataSetChanged();
                                    }
                                }
                                else {
                                    Log.d(TAG,"ListRdvNotGet");
                                }

                            }
                        });
                    }

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG,"SingleMedNotFound");
            }
        });
    }
}
