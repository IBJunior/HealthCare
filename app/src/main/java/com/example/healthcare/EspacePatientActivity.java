package com.example.healthcare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

public class EspacePatientActivity extends AppCompatActivity {

    ImageView search_med;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_espace_patient);

        search_med = (ImageView) findViewById(R.id.search_med);
        search_med.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EspacePatientActivity.this,ListMedecin.class));
            }
        });


    }


}
