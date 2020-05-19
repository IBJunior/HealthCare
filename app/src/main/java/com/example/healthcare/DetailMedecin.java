package com.example.healthcare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.model.Medecin;

public class DetailMedecin extends AppCompatActivity {

    Medecin medecin;
    private static final String TAG = "DetailPatient";
    ImageView imageView;
    TextView nom_prenom, specialite, adresse, tel, email;
    Button rdv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_medecin);
        Intent intent = getIntent();

        medecin = intent.getParcelableExtra("Patient");

        imageView = (ImageView) findViewById(R.id.image_med);
        nom_prenom = (TextView) findViewById(R.id.nom_prenom);
        specialite = (TextView) findViewById(R.id.specialite);
        tel = (TextView) findViewById(R.id.tel);
        email = (TextView) findViewById(R.id.email);
        adresse = (TextView) findViewById(R.id.adresse);

        rdv = (Button) findViewById(R.id.rdv);

        nom_prenom.setText("Dr "+medecin.getNom() + " " + medecin.getPrenom());
        specialite.setText(medecin.getSpecialite());
        adresse.setText(medecin.getAdresse());
        tel.setText("Tel: " + medecin.getTel());
        email.setText("Email : " + medecin.getEmail());





    }
}
