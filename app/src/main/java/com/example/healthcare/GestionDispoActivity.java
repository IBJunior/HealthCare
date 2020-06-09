package com.example.healthcare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.login.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;

public class GestionDispoActivity extends AppCompatActivity {

    TextView creerDispo,mesDispo,deconnect;
    String mail_med;
    ImageView home;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion_dispo);
        mesDispo = findViewById(R.id.mesDispo);
        creerDispo = findViewById(R.id.creerDispo);
        Intent i = getIntent();
        mail_med = i.getStringExtra("mail_med");

        mesDispo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GestionDispoActivity.this,ListDispoActi.class);
                intent.putExtra("mail_med",mail_med);
                startActivity(intent);
            }
        });

        creerDispo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GestionDispoActivity.this,CreerDispoAct.class);
                intent.putExtra("mail_med",mail_med);
                startActivity(intent);
            }
        });
        home = findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GestionDispoActivity.this,EspaceMedecinActivity.class);
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
                startActivity(new Intent(GestionDispoActivity.this, LoginActivity.class));

            }
        });

    }
}
