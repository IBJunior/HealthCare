package com.example.healthcare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class ConsultationsPatientAct extends AppCompatActivity {

    ImageView home;
    String mail_pat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultations_patient);
        home = findViewById(R.id.home);
        Intent i = getIntent();
        mail_pat = i.getStringExtra("mail_pat");

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ConsultationsPatientAct.this,EspacePatientActivity.class);
                intent.putExtra("mail_pat",mail_pat);
                startActivity(intent);
            }
        });

    }
}
