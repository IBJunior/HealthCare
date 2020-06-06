package com.example.healthcare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class GestionConsultations extends AppCompatActivity {

    TextView lis_consult,def_consult;
    String mail_med;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion_consultations);
        lis_consult = findViewById(R.id.lis_consult);
        def_consult = findViewById(R.id.def_consult);
        Intent i = getIntent();
        mail_med = i.getStringExtra("mail_med");


        lis_consult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(GestionConsultations.this,ConsultationsMedActi.class);
                intent.putExtra("mail_med",mail_med);
                startActivity(intent);

            }
        });
        def_consult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GestionConsultations.this,RdvMedList.class);
                intent.putExtra("mail_med",mail_med);
                startActivity(intent);
            }
        });



    }
}
