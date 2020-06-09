package com.example.healthcare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.login.LoginActivity;
import com.example.model.Patient;
import com.google.firebase.auth.FirebaseAuth;

import de.hdodenhof.circleimageview.CircleImageView;

public class DetailPatient extends AppCompatActivity {

    Patient patient;
    CircleImageView image_pat;
    ImageView home;
    TextView nom_prenom,ville_pat,email,tel,situation,deconnect;
    String mail_med;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_patient);
         Intent i = getIntent();

        patient = i.getParcelableExtra("patient");
        mail_med = i.getStringExtra("mail_med");



        nom_prenom = findViewById(R.id.nom_prenom);
        ville_pat = findViewById(R.id.ville_pat);
        email = findViewById(R.id.email);
        tel = findViewById(R.id.tel);
        situation = findViewById(R.id.situation);
        image_pat = findViewById(R.id.image_pat);


        home = findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailPatient.this,EspaceMedecinActivity.class);
                intent.putExtra("mail_med",mail_med);
                startActivity(intent);
            }
        });
        deconnect = findViewById(R.id.deconnexion);
        deconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(DetailPatient.this, LoginActivity.class));

            }
        });


        nom_prenom.setText(patient.getNom() + " " + patient.getPrenom());
        ville_pat.setText(patient.getAdresse());
        email.setText(patient.getEmail());
        tel.setText(patient.getTel());
        situation.setText(patient.getSituation_familiale());
    }
}
