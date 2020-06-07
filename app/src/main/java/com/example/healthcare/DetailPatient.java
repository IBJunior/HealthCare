package com.example.healthcare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.model.Patient;

import de.hdodenhof.circleimageview.CircleImageView;

public class DetailPatient extends AppCompatActivity {

    Patient patient;
    CircleImageView image_pat;
    TextView nom_prenom,ville_pat,email,tel,situation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_patient);
        Intent i = getIntent();

        patient = i.getParcelableExtra("patient");

        nom_prenom = findViewById(R.id.nom_prenom);
        ville_pat = findViewById(R.id.ville_pat);
        email = findViewById(R.id.email);
        tel = findViewById(R.id.tel);
        situation = findViewById(R.id.situation);
        image_pat = findViewById(R.id.image_pat);


        nom_prenom.setText(patient.getNom() + " " + patient.getPrenom());
        ville_pat.setText(patient.getAdresse());
        email.setText(patient.getEmail());
        tel.setText(patient.getTel());
        situation.setText(patient.getSituation_familiale());
    }
}
