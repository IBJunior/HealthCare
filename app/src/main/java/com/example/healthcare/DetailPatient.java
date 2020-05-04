package com.example.healthcare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.model.Medecin;
import com.example.model.Patient;

import java.text.SimpleDateFormat;

public class DetailPatient extends AppCompatActivity {

    Patient patient;
    private static final String TAG = "DetailPatient";
    ImageView imageView;
    TextView nom_prenom, situation_ville, tel, email;
    Button contacter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_patient);
        Intent intent = getIntent();

        patient = intent.getParcelableExtra("Patient");

        imageView = (ImageView) findViewById(R.id.image_pat);
        nom_prenom = (TextView) findViewById(R.id.nom_prenom);
        situation_ville = (TextView) findViewById(R.id.situation_ville);
        tel = (TextView) findViewById(R.id.tel);
        email = (TextView) findViewById(R.id.email);

        contacter = (Button) findViewById(R.id.contacter);

        nom_prenom.setText(patient.getNom() + " " + patient.getPrenom());
        situation_ville.setText(patient.getSituation_familiale() + " à " + patient.getAdresse());
        tel.setText("Tel: " + patient.getTel());
        email.setText("Email : " + patient.getEmail());





    }
}
