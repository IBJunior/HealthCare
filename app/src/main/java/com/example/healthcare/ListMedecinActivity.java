package com.example.healthcare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.LinearLayout;

public class ListMedecinActivity extends AppCompatActivity {
    private RecyclerView list_med;
    private  RecyclerView.Adapter list_med_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_medecin);


        list_med = findViewById(R.id.list_med);
        //list_med_adapter =  new MedecinsAdapter(medecins);
        RecyclerView.LayoutManager  layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL,false);
        list_med.setAdapter(list_med_adapter);
        list_med.setLayoutManager(layoutManager);


    }

}
