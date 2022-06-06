package com.example.tfe_app;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.text.Layout;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;

public class NiveauActivity extends AppCompatActivity {
    private Niveau niveau;
    private User user;

    private RelativeLayout layout_erreurs;
    private RelativeLayout Layout_end_niveau;
    private RelativeLayout Layout_infos;
    private RelativeLayout Layout_explication;
    private LinearLayout code_layout;

    private TextView txt_explication;
    private Button btn_close_explication;

    private ProgressBar time_left;
    private CountDownTimer timer;
    private TextView txt_progressbar;
    private Button btn_popo_view;
    private Button btn_popo_time;
    private ArrayList<Button> liste_btn_erreurs = new ArrayList<>();
    private ArrayList<Boolean> liste_btn_erreurs_checked = new ArrayList<>();

    private TextView txt_score1;
    private TextView txt_score2;
    private TextView txt_score3;
    private ImageView view_po1;
    private ImageView view_po2;
    private ImageView view_po3;
    private ImageView view_po4;
    private TextView txt_end_niveau;
    private Button btn_end_niveau;

    //effects potions visibles
    private ImageView effect_speed;

    //variable necessaire au programme
    private int total_errors;
    private int finded_errors;
    private int pv=3;
    private int score=0;
    private int tempsPotionTemps = 5; // en secondes
    private int nbr_erreurs_affichée = 3; // en nombre d'erreurs
    private String[] liste_corrections;

    private boolean lvl_reussit = true;

    //gestion des coordonnées des erreurs
    private int size_erreur = 60;

    private int hauteur_maps = 732; // en dpx (taille en px 1459)
    private int largeur_maps = 370; // en dpx (taille en px 819)

    private double case_hauteur = hauteur_maps / 16; //grilles sont hautes de 19 lignes de 10 cases
    private double case_large = largeur_maps / 9;


    DataBaseInformations databaseInfo = DataBaseInformations.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_niveau);
        SetDatas();
        GetViews();
        GenerateLvL();
        TempsEcoule(niveau.temps_limite);
    }

    public void onBackPressed () {

    }
    private void SetDatas(){
        niveau = (Niveau) getIntent().getSerializableExtra("niveau");
        liste_corrections = niveau.GetCorrection_erreurs();
        user = databaseInfo.getUser();
        total_errors = niveau.nbr_erreurs;
    }


    private void GetViews(){
        layout_erreurs = findViewById(R.id.layout_erreurs);
        Layout_end_niveau = findViewById(R.id.Layout_end_niveau);
        Layout_infos = findViewById(R.id.Layout_infos);
        Layout_explication = findViewById(R.id.Layout_explication);

        txt_explication = findViewById(R.id.txt_explication);
        btn_close_explication = findViewById(R.id.btn_close_explication);

        time_left = findViewById(R.id.progress_time_left);
        txt_progressbar = findViewById(R.id.txt_progressbar);
        btn_popo_view = findViewById(R.id.btn_popo_view);
        btn_popo_view.setText(""+user.nbr_bonus_vue);
        btn_popo_time = findViewById(R.id.btn_popo_time);
        btn_popo_time.setText(""+user.nbr_bonus_time);

        txt_score1 = findViewById(R.id.txt_score1);
        txt_score2 = findViewById(R.id.txt_score2);
        txt_score3 = findViewById(R.id.txt_score3);

        view_po1 = findViewById(R.id.view_po1);
        view_po2 = findViewById(R.id.view_po2);
        view_po3 = findViewById(R.id.view_po3);
        view_po4 = findViewById(R.id.view_po4);
        txt_end_niveau = findViewById(R.id.txt_end_niveau);
        btn_end_niveau = findViewById(R.id.btn_end_niveau);

        effect_speed = findViewById(R.id.effect_speed);

        btn_popo_view.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(user.nbr_bonus_vue>0){
                    user.nbr_bonus_vue--;
                    btn_popo_view.setText(""+user.nbr_bonus_vue);
                    PotionVue();
                }
            }
        });
        btn_popo_time.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(user.nbr_bonus_time>0){
                    user.nbr_bonus_time--;
                    btn_popo_time.setText(""+user.nbr_bonus_time);
                    PotionTemps();
                }
            }
        });

        layout_erreurs.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ErrorsMissed();
            }
        });

        btn_end_niveau.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ExitLvL();
            }
        });

        btn_close_explication.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CloseExplications();
            }
        });
    }

    private void GenerateLvL(){
        TextView consigne = findViewById(R.id.txt_consigne);
        consigne.setText(""+niveau.enonce);

        code_layout = findViewById(R.id.code_layout);
        ImageView imageView = new ImageView(getApplicationContext());
        try {
            imageView.setImageResource(databaseInfo.getGeneratedNiveauxMaps().get(niveau.code) );
            imageView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RegionActivity.dpToPx(729,this)));
            code_layout.addView(imageView);
        }catch (Exception e){
            ErrorSystem();
        }
        GenerateErrors();
    }
    private void GenerateErrors(){
        for (int icompteur=0; icompteur<niveau.nbr_erreurs;icompteur++){

            Button niveau_btn = new Button(getApplicationContext());
            niveau_btn.setBackgroundColor(Color.TRANSPARENT);
            niveau_btn.setTag(false);
            int idErreur=0;
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RegionActivity.dpToPx(60,this),RegionActivity.dpToPx(60,this));
            params.leftMargin = RegionActivity.dpToPx(niveau.GetEmplacement_erreurs()[0][icompteur]*case_hauteur,this)-size_erreur;
            params.topMargin = RegionActivity.dpToPx(niveau.GetEmplacement_erreurs()[1][icompteur]*case_large,this)-size_erreur;
            niveau_btn.setLayoutParams(params);

            int finalIcompteur = icompteur;
            niveau_btn.setOnClickListener(new View.OnClickListener() {
                private boolean clicked=false;
                private int id_button = finalIcompteur;
                public void onClick(View v) {
                    if(!clicked){
                        niveau_btn.setBackgroundResource(R.drawable.bugz);
                        niveau_btn.setTag(true);
                        liste_btn_erreurs_checked.set(id_button,true) ;
                        ErrorFinded();
                    }
                    clicked=true;
                }
            });
            liste_btn_erreurs.add(niveau_btn);
            liste_btn_erreurs_checked.add(false);

            layout_erreurs.addView(niveau_btn);
        }
    }
    public boolean ErrorFinded(){
        finded_errors++;
        score = score + 100;

        if(finded_errors == total_errors){
            EndLvL();
        }

        return false;
    }

    public void ErrorsMissed(){
        PVLoose();
    }

    public void PVLoose(){

        int progress_actu = time_left.getProgress() - 5;
        if (progress_actu <=0){
            progress_actu =0;
        }
        txt_progressbar.setText(""+ progress_actu);
        time_left.setProgress(progress_actu);
    }

    public void TempsEcoule(int actu_temps){
        time_left.setMax(actu_temps);
        time_left.setProgress(actu_temps);
        System.out.println("progress = " + time_left.getProgress());
        System.out.println("set timer = " + actu_temps);
        actu_temps=actu_temps*1000;
        startCountdown(actu_temps);
    }

    private void PotionVue(){
        Random rand = new Random();
        int retry = 0;
        Drawable myDrawable = getResources().getDrawable(R.drawable.bugz );
        for(int icompteur=0; icompteur<nbr_erreurs_affichée; icompteur++){
            int id_erreur = rand.nextInt(liste_btn_erreurs.size());
            if(liste_btn_erreurs_checked.get(id_erreur) == true){
                retry++;
                icompteur--;
            }
            else{
                liste_btn_erreurs.get(id_erreur).setBackgroundResource(R.drawable.oeil);
            }

            if(retry>3){
                break;
            }
        }
    }
    private void PotionTemps(){
        timer.cancel();
        effect_speed.setVisibility(View.VISIBLE);
        new CountDownTimer(tempsPotionTemps*1000, 1000) {
            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                effect_speed.setVisibility(View.INVISIBLE);
                startCountdown(time_left.getProgress()*1000);
            }
        }.start();

    }
    private void startCountdown(int temps){
        timer= new CountDownTimer(temps, 1000) {
            public void onTick(long millisUntilFinished) {
                txt_progressbar.setText("" + (time_left.getProgress() - 1));
                time_left.setProgress(time_left.getProgress() - 1 );

                if(time_left.getProgress()<=0){
                    EndLvL();
                }
            }

            public void onFinish() {
                EndLvL();
            }
        }.start();
    }


    //action d'affichage de correction
    private void PrepareCorrection(){
        int icompteur=0;
        for (Button btn_error: liste_btn_erreurs) {
            if(!(boolean) btn_error.getTag()){
                btn_error.setBackgroundResource(R.drawable.info);
                int finalIcompteur = icompteur;
                btn_error.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Display_correction(finalIcompteur);
                    }
                });
            }
            icompteur++;
        }
    }

    private void Display_correction(int id_correction){
        if(liste_corrections!= null && id_correction< liste_corrections.length){
            txt_explication.setText(liste_corrections[id_correction]);
        }
        else {
            txt_explication.setText("pas de correction disponible");
        }
        Layout_explication.setVisibility(View.VISIBLE);

    }
    private void CloseExplications(){
        Layout_explication.setVisibility(View.INVISIBLE);
    }




    //action de fin de niveau

    private void EndLvL(){
        //PoPup victoire ou defate + affichage score total
        timer.cancel();
        PrepareCorrection();
        btn_popo_view.setVisibility(View.INVISIBLE);
        btn_popo_time.setVisibility(View.INVISIBLE);

        int time_left = Integer.parseInt((String) txt_progressbar.getText());
        time_left = time_left ;
        score = score + (time_left*10) ;
        txt_end_niveau.setText("Score : " + score);
        System.out.println("Score : " + score);

        int score1 = niveau.GetTranche_score()[0];
        int score2 = niveau.GetTranche_score()[1];
        int score3 = niveau.GetTranche_score()[2];

        txt_score1.setText(""+score1);
        txt_score2.setText(""+score2);
        txt_score3.setText(""+score3);

        if(score >= score3){
            user.po = user.po + 4;
            ShowPo(4);
        }else if(score >= score2){
            user.po = user.po + 3;
            ShowPo(3);
        }
        else if(score >= score1){
            user.po = user.po + 2;
            ShowPo(2);
        }else{
            txt_end_niveau.setText("niveau echoué");
            user.actu_path_position--;
            databaseInfo.setActuNiveau(databaseInfo.getActuNiveau()-1);
            lvl_reussit = false;
        }


        Layout_infos.setVisibility(View.INVISIBLE);
        Layout_end_niveau.setVisibility(View.VISIBLE);


        databaseInfo.SetUser(user);

    }
    private void ShowPo(int nbr_po){
        txt_score1.setVisibility(View.VISIBLE);
        txt_score2.setVisibility(View.VISIBLE);
        txt_score3.setVisibility(View.VISIBLE);

        view_po1.setVisibility(View.VISIBLE);

        if(nbr_po>=2){
            view_po2.setVisibility(View.VISIBLE);
        }
        if(nbr_po>=3){
            view_po3.setVisibility(View.VISIBLE);
        }
        if(nbr_po>=4){
            view_po4.setVisibility(View.VISIBLE);

        }
    }

    private void ExitLvL(){
        Intent region = new Intent(NiveauActivity.this, RegionActivity.class);
        region.putExtra("niveau_restult",lvl_reussit);
        startActivity(region);
        finish();
    }
    private void ErrorSystem(){
        Toast.makeText(getApplicationContext(),"une erreur est survenue",Toast.LENGTH_LONG).show();
        Intent main = new Intent(NiveauActivity.this, MainActivity.class);
        startActivity(main);
        finish();
    }


}
