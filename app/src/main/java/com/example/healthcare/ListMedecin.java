package com.example.healthcare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.model.Medecin;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ListMedecin extends AppCompatActivity {

    private ArrayList<Medecin> medecins = new ArrayList<>();
    private static final String TAG = "ListMedecin";
    MedecinListAdapter medecinListAdapter;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_medecin);
        Log.d(TAG,"Oncreate Started");

        iniMedecinList();
    }


    private void iniMedecinList(){
        db.collection("medecins").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                Log.d(TAG,"Souley");
                if(task.isSuccessful()){
                    Toast.makeText(ListMedecin.this,"Success",Toast.LENGTH_SHORT);

                    for (QueryDocumentSnapshot d : task.getResult()) {

                        Medecin med =  d.toObject(Medecin.class);

                        medecins.add(med);


                    }
                    medecinListAdapter.notifyDataSetChanged();

                }
                else {
                    Toast.makeText(ListMedecin.this," not Success",Toast.LENGTH_SHORT);
                    Log.d(TAG,"No items in your collections");
                }

            }
        });

        initMedecinRecyclerView();
    }
    private void initMedecinRecyclerView(){
        Log.d(TAG,"Initiation of RecyclerView started");


        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.medecin_recycler);
        medecinListAdapter = new MedecinListAdapter(this,medecins);
        recyclerView.setAdapter(medecinListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


    }
}
