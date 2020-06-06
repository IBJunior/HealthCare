package com.example.healthcare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class EspaceMedecinActivity extends AppCompatActivity {

    String mail_med;
    ImageView dispo,consults,mes_rdv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_espace_medecin);
        consults = findViewById(R.id.consults);

        final Intent i = getIntent();

        mail_med = i.getStringExtra("mail_med");
        dispo = findViewById(R.id.dispo);

        mes_rdv = findViewById(R.id.mes_rdv);
        mes_rdv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EspaceMedecinActivity.this,RdvMedList.class);
                intent.putExtra("mail_med",mail_med);
                startActivity(intent);
            }
        });
        dispo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EspaceMedecinActivity.this,GestionDispoActivity.class);
                intent.putExtra("mail_med",mail_med);
                startActivity(intent);
            }
        });
        consults.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EspaceMedecinActivity.this,GestionConsultations.class);
                intent.putExtra("mail_med", mail_med);
                startActivity(intent);
            }
        });
    }
}
