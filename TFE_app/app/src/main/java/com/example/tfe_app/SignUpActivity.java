package com.example.tfe_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.vishnusivadas.advanced_httpurlconnection.FetchData;
import com.vishnusivadas.advanced_httpurlconnection.PutData;


public class SignUpActivity extends AppCompatActivity {

    TextInputEditText  txt_nom, txt_password, txt_mail;
    Button btn_signUp;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        setViews();


    }

    private void setViews(){
        txt_mail = findViewById(R.id.text_mail);
        txt_password = findViewById(R.id.txt_password);
        txt_nom = findViewById(R.id.text_nom);
        btn_signUp = findViewById(R.id.signin);
        progressBar = findViewById(R.id.attente_signup);

        btn_signUp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                String nom,mail,password;
                nom = txt_nom.getText().toString();
                mail = txt_mail.getText().toString();
                password = txt_password.getText().toString();

                if(!nom.equals("") && !mail.equals("") && !password.equals("")){

                    //Start ProgressBar first (Set visibility VISIBLE)
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setVisibility(View.VISIBLE);
                            //Starting Write and Read data with URL
                            //Creating array for parameters
                            String[] field = new String[3];
                            field[0] = "nom";
                            field[1] = "mail";
                            field[2] = "password";
                            //Creating array for data
                            String[] data = new String[3];
                            data[0] = nom;
                            data[1] = mail;
                            data[2] = password;
                            PutData putData = new PutData("http://192.168.1.229/TFE/signup.php", "POST", field, data);
                            if (putData.startPut()) {
                                if (putData.onComplete()) {
                                    progressBar.setVisibility(View.INVISIBLE);
                                    String result = putData.getResult();
                                    //End ProgressBar (Set visibility to GONE)
                                    Log.i("PutData", result);
                                    if(result.equals("Sign Up Success")){
                                        Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
                                        Intent connexion = new Intent(SignUpActivity.this, MainActivity.class);
                                        startActivity(connexion);
                                        finish();
                                    }
                                    else{
                                        Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                            //End Write and Read data with URL
                        }
                    });
                }

                else{
                    Toast.makeText(getApplicationContext(),"Remplissez tous les champs",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}