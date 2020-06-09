package com.example.healthcare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.login.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;

public class EspacePatientActivity extends AppCompatActivity {

    ImageView search_med,consult_pat,mes_rdv,nv_rdc;
    String mail_pat;
    TextView deconnect;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_espace_patient);

        final Intent intent = getIntent();
        mail_pat = intent.getStringExtra("mail_pat");
        mes_rdv = findViewById(R.id.mes_rdv);
        nv_rdc = findViewById(R.id.nv_rdc);
        nv_rdc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(EspacePatientActivity.this,ListMedecin.class);
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
                startActivity(new Intent(EspacePatientActivity.this, LoginActivity.class));

            }
        });
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
                Intent i = new Intent(EspacePatientActivity.this, ConsultationsPatientAct.class);
                i.putExtra("mail_pat",mail_pat);
                startActivity(i);
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
