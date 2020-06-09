package com.example.healthcare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.login.LoginActivity;
import com.example.model.Medecin;
import com.google.firebase.auth.FirebaseAuth;

public class DetailMedecin extends AppCompatActivity {

    Medecin medecin;
    private static final String TAG = "DetailPatient";
    ImageView imageView,home;
    TextView nom_prenom, specialite, adresse, tel, email, deconnect;
    Button rdv;
    String mail_pat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_medecin);
        Intent intent = getIntent();

        mail_pat = intent.getStringExtra("mail_pat");
        home = findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(DetailMedecin.this,EspacePatientActivity.class);
                intent1.putExtra("mail_pat",mail_pat);
                startActivity(intent1);
            }
        });
        deconnect = findViewById(R.id.deconnexion);
        deconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(DetailMedecin.this, LoginActivity.class));

            }
        });
        medecin = intent.getParcelableExtra("medecin");


        imageView = (ImageView) findViewById(R.id.image_med);
        nom_prenom = (TextView) findViewById(R.id.nom_prenom);
        tel = (TextView) findViewById(R.id.tel);
        email = (TextView) findViewById(R.id.email);
        adresse = (TextView) findViewById(R.id.adresse);
        specialite = findViewById(R.id.specialite);

        rdv = findViewById(R.id.rdv);

        rdv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DetailMedecin.this,PriseRdv.class);
                i.putExtra("medecin",medecin);
                i.putExtra("mail_pat",mail_pat);
                startActivity(i);
            }
        });
        
        nom_prenom.setText("Dr "+medecin.getNom() + " " + medecin.getPrenom());
        adresse.setText(medecin.getAdresse());
        specialite.setText(medecin.getSpecialite());
        tel.setText(medecin.getTel());
        email.setText(medecin.getEmail());

    }
}
