package com.example.healthcare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.model.Medecin;

public class DetailMedecin extends AppCompatActivity {

    Medecin medecin;
    private static final String TAG = "DetailMedecin";
    ImageView imageView;
    TextView nom_prenom, specialite_ville, tel, email;
    Button contacter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_medecin);
        Intent intent = getIntent();

        medecin = intent.getParcelableExtra("Medcin");
        Log.d(TAG,medecin.toString());
        imageView = (ImageView) findViewById(R.id.image_med);
        nom_prenom = (TextView) findViewById(R.id.nom_prenom);
        specialite_ville = (TextView) findViewById(R.id.specialite_ville);
        tel = (TextView) findViewById(R.id.tel);
        email = (TextView) findViewById(R.id.email);
        contacter = (Button) findViewById(R.id.contacter);

        nom_prenom.setText("Dr "+ medecin.getNom() + " " + medecin.getPrenom());
        specialite_ville.setText(medecin.getSpecialite() + " à " + medecin.getAdresse());
        tel.setText("Tel: " + medecin.getTel());
        email.setText("Email : " + medecin.getEmail());




    }
}
