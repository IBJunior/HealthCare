package com.example.healthcare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.model.Medecin;
import com.example.model.Patient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ListPatient extends AppCompatActivity {

    private ArrayList<Patient> patients = new ArrayList<>();
    private static final String TAG = "ListPatient";
    PatientListAdapter patientListAdapter;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_patient);
        Log.d(TAG,"Oncreate Started");

        iniMedecinList();
    }


    private void iniMedecinList(){
        db.collection("patients").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                Log.d(TAG,"Souley");
                if(task.isSuccessful()){
                    Toast.makeText(ListPatient.this,"Success",Toast.LENGTH_SHORT);

                    for (QueryDocumentSnapshot d : task.getResult()) {

                        Patient pat =  d.toObject(Patient.class);

                        patients.add(pat);


                    }
                    patientListAdapter.notifyDataSetChanged();

                }
                else {
                    Toast.makeText(ListPatient.this," not Success",Toast.LENGTH_SHORT);
                    Log.d(TAG,"No items in your collections");
                }

            }
        });

        initMedecinRecyclerView();
    }
    private void initMedecinRecyclerView(){
        Log.d(TAG,"Initiation of RecyclerView started");


        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.patient_recycler);
        patientListAdapter = new PatientListAdapter(this,patients);
        recyclerView.setAdapter(patientListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


    }
}
