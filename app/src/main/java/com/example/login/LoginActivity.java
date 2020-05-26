package com.example.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.healthcare.EspaceMedecinActivity;
import com.example.healthcare.EspacePatientActivity;
import com.example.healthcare.MainActivity;
import com.example.healthcare.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;


public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private FirebaseAuth mAuth;
    EditText mail,passwd;
    TextView welcme;
    Button connect;
    FirebaseAuth.AuthStateListener mAuthStateListener;
    // Access a Cloud Firestore instance from your Activity
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        mail = (EditText) findViewById(R.id.email);
        passwd = (EditText) findViewById(R.id.passwd);
        connect = (Button) findViewById(R.id.seConnecter);
        String welcome="";


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

    public void seConnecter(View v){

        final String mail_str = mail.getText().toString();
        String passwd_str = passwd.getText().toString();


        if( mail_str.isEmpty() ){
            mail.setError("Veuillez renseigner un email");
            mail.requestFocus();

        }
        else  if(!isEmailValid(mail_str)){
            mail.setError("Veuillez renseigner un email valide");
            mail.requestFocus();
        }
        else if(passwd_str.isEmpty()){
            passwd.setError("Entrez un mot de passe");
            passwd.requestFocus();
        }
        else if (!passwd_str.isEmpty() && passwd_str.length() <8){
            passwd.setError("Entrez un mot de passe d'aumoins 8 caractères");
            passwd.requestFocus();

        }
        else if(!(mail_str.isEmpty() && passwd_str.isEmpty()) && (passwd_str.length()>=8 && isEmailValid(mail_str) )){
            mAuth.signInWithEmailAndPassword(mail_str,passwd_str).addOnCompleteListener(LoginActivity.this,new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        lancerEspaceUser(mail_str);
                        Log.d(TAG, "Login Succeed");



                    }
                    else {

                        Toast.makeText(LoginActivity.this,"SingUp Failed",Toast.LENGTH_SHORT);
                        Log.d(TAG,"Login Failed");

                    }
                }
            });
        }



    }

    private void lancerEspaceUser(final String ad_mail)
    {

        db.collection("medecins")
                .whereEqualTo("email", ad_mail ).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()){
                    Intent intent = new Intent(LoginActivity.this, EspaceMedecinActivity.class);
                    intent.putExtra("mail_med",ad_mail);
                    startActivity(intent);
                }
                else {
                    Intent intent = new Intent(LoginActivity.this,EspacePatientActivity.class);
                    intent.putExtra("mail_pat",ad_mail);
                    startActivity(intent);

                }

            }
        });
    }


    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public void creerCompte(View view) {
        final String mail_str = mail.getText().toString();
        String passwd_str = passwd.getText().toString();
        if( mail_str.isEmpty() ){
            mail.setError("Veuillez renseigner un email");
            mail.requestFocus();

        }
        else  if(!isEmailValid(mail_str)){
            mail.setError("Veuillez renseigner un email valide");
            mail.requestFocus();
        }
        else if(passwd_str.isEmpty()){
            passwd.setError("Entrez un mot de passe");
            passwd.requestFocus();
        }
        else if (!passwd_str.isEmpty() && passwd_str.length() <8){
            passwd.setError("Entrez un mot de passe d'aumoins 8 caractères");
            passwd.requestFocus();

        }
        else if(!(mail_str.isEmpty() && passwd_str.isEmpty()) && (passwd_str.length()>=8 && isEmailValid(mail_str) )){
            mAuth.createUserWithEmailAndPassword(mail_str,passwd_str).addOnCompleteListener(LoginActivity.this,
                    new OnCompleteListener<com.google.firebase.auth.AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<com.google.firebase.auth.AuthResult> task) {

                            if(task.isSuccessful()){
                                Intent intent = new Intent(LoginActivity.this,CreerUnComptActivity.class);
                                intent.putExtra("mail",mail_str);
                                Log.d(TAG,"Sign  up successfully !");

                                startActivity(intent);


                            }
                            else
                            {
                                Toast.makeText(LoginActivity.this,"Cette adresse possède déjà un compte connectez-vous",Toast.LENGTH_LONG).show();
                                Log.d(TAG,"Sign up failed epicly");

                            }
                        }
                    });
        }


    }

}
