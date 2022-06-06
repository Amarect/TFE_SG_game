package com.example.tfe_app;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EmbuscadeActivity extends AppCompatActivity {
    private ProgressBar temps_restant;
    private Button btn_rep1;
    private Button btn_rep2;
    private Button btn_rep3;
    private Button btn_rep4;
    private TextView question;
    private Button btn_quit_embuscade;

    private Embuscade embuscade = new Embuscade.EmbuscadeBuilder().Build();

    private int Pv_left = 2;
    private User user;
    private int time_max = 60;

    private CountDownTimer timer;


    DataBaseInformations databaseInfo = DataBaseInformations.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_embuscade);
        SetDatas();
        SetViews();
        GenerateActivity();
        ActionButtons();
        SetTimer();
    }
    private void SetTimer(){
        timer= new CountDownTimer(time_max * 1000, 1000) {
            public void onTick(long millisUntilFinished) {

                temps_restant.setProgress((int) (millisUntilFinished/1000));
            }
            public void onFinish() {
                DisplayReponse(false);
            }
        }.start();
    }


    private void SetDatas(){
        try {
            embuscade = (Embuscade) getIntent().getSerializableExtra("Embuscade");
            user = databaseInfo.getUser();
        }catch (Exception e){
            Error();
        }
    }

    private void Error(){
        Toast.makeText(getApplicationContext(),"une erreur est survenue",Toast.LENGTH_LONG).show();
        Intent connexion = new Intent(EmbuscadeActivity.this, MainActivity.class);
        startActivity(connexion);
        finish();
    }
    private void SetViews(){
        temps_restant = findViewById(R.id.emb_temps_restant);
        temps_restant.setMax(time_max);
        temps_restant.setProgress(time_max);

        btn_rep1 = findViewById(R.id.btn_rep1);
        btn_rep2 = findViewById(R.id.btn_rep2);
        btn_rep3 = findViewById(R.id.btn_rep3);
        btn_rep4 = findViewById(R.id.btn_rep4);
        btn_quit_embuscade = findViewById(R.id.btn_quit_embuscade);
        question = findViewById(R.id.txt_question);
    }

    private void GenerateActivity(){
        SetRepTxT();
    }

    private void SetRepTxT(){
        System.out.println("EMBUS " + embuscade.question);
        question.setText(embuscade.question);
        if(embuscade.reponses != null){
            btn_rep1.setText(embuscade.GetReponses()[0]);
            btn_rep2.setText(embuscade.GetReponses()[1]);
            btn_rep3.setText(embuscade.GetReponses()[2]);
            btn_rep4.setText(embuscade.GetReponses()[3]);
        }
    }
    private void ActionButtons(){
        btn_rep1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VerifRep(0,btn_rep1);
            }
        });

        btn_rep2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VerifRep(1,btn_rep2);
            }
        });

        btn_rep3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VerifRep(2,btn_rep3);
            }
        });

        btn_rep4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VerifRep(3,btn_rep4);
            }
        });

        btn_quit_embuscade.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                    databaseInfo.SetUser(user);
                    QuitLvL();
            }
        });
    }
    private void VerifRep(int idRep,Button rep_selected){
        if(temps_restant.getProgress() >0){
            if(idRep == embuscade.reponse){ //si bonne reponse
                DisplayReponse(true);
            }
            else{
                Pv_left--;
                rep_selected.setVisibility(View.INVISIBLE);
                if(Pv_left ==0){
                    DisplayReponse(false);
                }
            }

        }
    }
    private void DisplayReponse(boolean reussit){
        timer.cancel();
        temps_restant.setProgress(0);
        if(!reussit){
            user.po = user.po - embuscade.difficulte;
        }
        question.setText(embuscade.correction);
        question.setTextColor(Color.GREEN);
        btn_quit_embuscade.setVisibility(View.VISIBLE);
    }

    private void QuitLvL(){
        databaseInfo.SetUser(user);

        Intent region = new Intent(EmbuscadeActivity.this, RegionActivity.class);

        startActivity(region);
        finish();
    }

}

//temps restant

//apprentissage