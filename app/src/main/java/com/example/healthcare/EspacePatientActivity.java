package com.example.healthcare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class EspacePatientActivity extends AppCompatActivity {

    ImageView search_med,consult_pat,mes_rdv;
    String mail_pat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_espace_patient);

        final Intent intent = getIntent();
        mail_pat = intent.getStringExtra("mail_pat");
        mes_rdv = findViewById(R.id.mes_rdv);
        mes_rdv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EspacePatientActivity.this,RdvPatientList.class);
                i.putExtra("mail_pat",mail_pat);
                startActivity(i);
            }
        });
        consult_pat = findViewById(R.id.consult_patient);
        consult_pat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EspacePatientActivity.this, ConsultationsMedActi.class);
                i.putExtra("mail_pat",mail_pat);
            }
        });
        search_med = (ImageView) findViewById(R.id.search_med);
        search_med.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(EspacePatientActivity.this,ListMedecin.class);
                intent1.putExtra("mail_pat",mail_pat);
                startActivity(intent1);
            }
        });


    }


}
