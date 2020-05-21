package com.example.healthcare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.model.Medecin;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ListMedecin extends AppCompatActivity {

    private ArrayList<Medecin> medecins = new ArrayList<>();
    private static final String TAG = "ListPatient";
    MedecinListAdapter medecinListAdapter;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    EditText search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG,"On Create called");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_medecin);
        Log.d(TAG,"Oncreate Started");
        search = (EditText) findViewById(R.id.search);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });
        iniMedecinList();

    }

    private void filter(String s){
        ArrayList<Medecin> medecins_filter = new ArrayList<>();

        for (Medecin med : medecins){
            if (med.getSpecialite().toLowerCase().trim().contains(s) || med.getAdresse().toLowerCase().trim().contains(s)){
                medecins_filter.add(med);
            }
        }
        medecinListAdapter.filterList(medecins_filter);
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
                        Log.d(TAG," "+ med.getNom());


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
