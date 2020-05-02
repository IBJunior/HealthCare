package com.example.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.opengl.ETC1;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.healthcare.MainActivity;
import com.example.healthcare.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    ImageView logo;
    private FirebaseAuth mAuth;
    EditText mail,passwd;
    FirebaseAuth.AuthStateListener mAuthStateListener;
    Button signIn;
    TextView noAccountText,signuptext;
    LinearLayout noAccountlayout ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        logo = (ImageView) findViewById(R.id.logo);
        signIn = (Button) findViewById(R.id.signIn);
        noAccountlayout = (LinearLayout) findViewById(R.id.noAccountlayout);
        mail = (EditText) findViewById(R.id.mail);
        passwd = (EditText) findViewById(R.id.passwd);
        noAccountText = (TextView) findViewById(R.id.noAccountText);
        signuptext = (TextView) findViewById(R.id.signuptext);
        mAuth = FirebaseAuth.getInstance();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mFirebaseUser = mAuth.getCurrentUser();

                if(mFirebaseUser !=null){
                    Toast.makeText(LoginActivity.this,"You are logged",Toast.LENGTH_SHORT);
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                }
                else {
                    Toast.makeText(LoginActivity.this,"You're not log",Toast.LENGTH_SHORT);
                }
            }
        };

    }

    public void signIn(View v){

        String email = mail.getText().toString();
        String pass = passwd.getText().toString();
        if(email.isEmpty())
        {
            mail.setError("Provide an email please");
            mail.requestFocus();
        }
        else if(pass.isEmpty()){
            passwd.setError("Provide a password please");
            passwd.requestFocus();
        }
        else  if(email.isEmpty() && pass.isEmpty())
        {
            Toast.makeText(LoginActivity.this,"Fields are empty",Toast.LENGTH_SHORT);
        }
        else if(!(email.isEmpty() && pass.isEmpty())){

            mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(LoginActivity.this,new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(!task.isSuccessful()){
                        Toast.makeText(LoginActivity.this,"SingUp Failed",Toast.LENGTH_SHORT);

                    }
                    else {
                        startActivity(new Intent(LoginActivity.this,MainActivity.class));
                    }
                }
            });
        }

    }
    public void signupAct(View v){
       startActivity(new Intent(LoginActivity.this,CreerUnComptActivity.class));

    }
    /*public void signUp(View v){
        startActivity(new Intent(LoginActivity.this,CreerUnComptActivity.class));
        String email = mail.getText().toString();
        String pass = passwd.getText().toString();
        if(email.isEmpty())
        {
            mail.setError("Provide an email please");
            mail.requestFocus();
        }
        else if(pass.isEmpty()){
            passwd.setError("Provide a password please");
            passwd.requestFocus();
        }
        else  if(email.isEmpty() && pass.isEmpty())
        {
            Toast.makeText(LoginActivity.this,"Fields are empty",Toast.LENGTH_SHORT);
        }
        else if(!(email.isEmpty() && pass.isEmpty())){
            mAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(LoginActivity.this,
                    new OnCompleteListener<com.google.firebase.auth.AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<com.google.firebase.auth.AuthResult> task) {

                            if(!task.isSuccessful()){
                                Toast.makeText(LoginActivity.this,"SingUp Failed",Toast.LENGTH_SHORT);

                            }
                            else
                            {
                                startActivity(new Intent(LoginActivity.this,MainActivity.class));
                            }
                        }
                    });
        }

    }*/

}
