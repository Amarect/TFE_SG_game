package com.example.tfe_app;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcelable;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vishnusivadas.advanced_httpurlconnection.FetchData;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;

public class RegionActivity extends AppCompatActivity {
    private ScrollView region_scroll;
    private RelativeLayout layout_region;
    private LinearLayout maps_container;
    private RelativeLayout layout_niveaux;
    private ImageView player_token;
    private Button btn_back_hub;
    private TextView txt_region_name;
    private TextView txt_region_diff;
    private TextView txt_po_region;

    private Region actu_region;
    private User user;
    private ArrayList<Niveau> liste_niveaux;
    private ArrayList<Embuscade> liste_embuscades;
    private ArrayList<TextView> liste_buttons_niveau = new ArrayList<TextView>();

    private RelativeLayout layout_popu_embuscade;
    private Button btn_accept_embuscade;
    private Button btn_fuite_embuscade;
    private Boolean embuscade_en_cours = false; //empeche l'utilisateur de revenir au menu en cas d'embuscade

    private int actu_niveau_position;
    private int actu_path_position;

    DataBaseInformations databaseInfo = DataBaseInformations.getInstance();

    private int niveau_en_cours = 0;

    //valeurs necessaires au programme
    private int size_LvL = 50;
    private int size_player = 120; // marge d'erreur pour centrer token sur route

    private int hauteur_maps = 732; // en dpx (taille en px 1459)
    private int largeur_maps = 370; // en dpx (taille en px 819)

    private double case_hauteur = hauteur_maps / 16; //grilles sont hautes de 19 lignes de 10 cases
    private double case_large = largeur_maps / 9;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_region);

        GetDatas();
        SaveDBUser();
        Generation_region();
    }
    private void Generation_region(){
        SetViews();
        AddmapsToRegion();
        GetPositionPlayer();
        AddNiveauxToRegion(maps_container);
    }
    private void SetViews(){
        region_scroll = findViewById(R.id.scrollview_region);
        layout_region = findViewById(R.id.layout_region);
        btn_back_hub = findViewById(R.id.btn_back_hub);
        txt_po_region = findViewById(R.id.txt_po_region);
        txt_po_region.setText(""+ user.po);
        txt_region_diff = findViewById(R.id.txt_difficulte);
        txt_region_diff.setText("Difficulte : " + actu_region.difficulte);
        txt_region_name = findViewById(R.id.txt_region_name);

        SpannableString content = new SpannableString(actu_region.nom);
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        txt_region_name.setText(content);



        layout_popu_embuscade = findViewById(R.id.layout_popu_embuscade);
        btn_accept_embuscade = findViewById(R.id.btn_accept_embuscade);
        btn_fuite_embuscade = findViewById(R.id.btn_fuite_embuscade);
        if(user.nbr_bonus_embuscade==0){
            btn_fuite_embuscade.setText("plus de potion d'invisiblité");
        }
        else{
            btn_fuite_embuscade.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    ClosePopUpEmbuscade();
                }
            });
        }
        btn_accept_embuscade.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                StartEmbuscade();
            }
        });

        btn_back_hub.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ReturnHub();
            }
        });

    }
    private void GetDatas(){

        liste_niveaux = databaseInfo.getListe_niveaux();
        liste_embuscades = databaseInfo.getListe_embuscades();
        actu_region = databaseInfo.getActu_region();
        user = databaseInfo.getUser();
    }

    private void Error(){
        Toast.makeText(getApplicationContext(),"une erreur est survenue",Toast.LENGTH_LONG).show();
        Intent connexion = new Intent(RegionActivity.this, MainActivity.class);
        startActivity(connexion);
        finish();
    }

    private void AddmapsToRegion(){
        maps_container = findViewById(R.id.maps_container);
        ImageView imageView = new ImageView(getApplicationContext());
        try {
            imageView.setImageResource(databaseInfo.getGeneratedRegionsMaps().get(actu_region.image) );
            int pos_x = dpToPx((actu_region.tailleMaps * hauteur_maps),this)-size_LvL;
            imageView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,pos_x));
            maps_container.addView(imageView);
        }catch (Exception e){
            Error();
        }
     }
    private void GetPositionPlayer(){
        actu_path_position = user.actu_path_position;
        if(databaseInfo.getActuNiveau() ==-1){
            actu_niveau_position = getNiveauPosition();
            databaseInfo.setActuNiveau(actu_niveau_position);
        }
        if(databaseInfo.getActuNiveau() > databaseInfo.getListe_niveaux().size()){
            Finregion();
        }
        else{
            actu_niveau_position = databaseInfo.getActuNiveau();
        }

        player_token = findViewById(R.id.player_token);
        int pos_x = dpToPx(actu_region.getpathpoints()[0][actu_path_position]*case_large,this)-size_player;
        player_token.setTranslationX(pos_x);
        int playerY = dpToPx(actu_region.getpathpoints()[1][actu_path_position]*case_hauteur,this)-size_player;
        player_token.setTranslationY(playerY);
        region_scroll.post(new Runnable() {
            public void run() {
                region_scroll.scrollTo(0, playerY);
            }
        });
     }
     private int getNiveauPosition(){

        int niveau_pos = 0;
        int [] liste_lvls = actu_region.getPosition_LvL();
        for (int icompteur=0; icompteur < liste_lvls.length;icompteur++){
            if(actu_path_position >= liste_lvls[icompteur]){
                niveau_pos++;
            }
        }
        return niveau_pos;
     }

    private void AddNiveauxToRegion(LinearLayout maps_container){
        layout_niveaux = findViewById(R.id.layout_niveaux);
        layout_niveaux.setLayoutParams(maps_container.getLayoutParams());
        for(int niveau=0 ; niveau < actu_region.getPosition_LvL().length; niveau++){

            TextView niveau_btn = new TextView(getApplicationContext());
            if(niveau >= actu_niveau_position){
                niveau_btn.setBackgroundResource(R.drawable.lvl_incomplet);
            }
            else{
                niveau_btn.setBackgroundResource(R.drawable.lvl_complet);
            }

            RelativeLayout.LayoutParams params = new LayoutParams(dpToPx(size_LvL,this),dpToPx(size_LvL,this));
            int pos_x = dpToPx(actu_region.getpathpoints()[0][actu_region.getPosition_LvL()[niveau]]*case_large,this)-size_LvL;
            params.leftMargin = pos_x;
            int pos_y = dpToPx(actu_region.getpathpoints()[1][actu_region.getPosition_LvL()[niveau]]*case_hauteur,this)-size_LvL;
            params.topMargin = pos_y;
            niveau_btn.setLayoutParams(params);

            niveau_btn.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            final int niveauPos = actu_region.getPosition_LvL()[niveau];
            final int niveauSelect = niveau;

            niveau_btn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    MovementPlayer(niveauPos,niveauSelect);
                }
            });
            liste_buttons_niveau.add(niveau_btn);
            layout_niveaux.addView(niveau_btn);
        }
    }

    protected static int dpToPx(int dp, Context context) {
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }
    protected static int dpToPx(double dp, Context context) {
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }

    private void StartNiveau(int niveauSelect){
        niveau_en_cours = niveauSelect;
        Intent niveau = new Intent(RegionActivity.this, NiveauActivity.class);
        Niveau niveauselect= liste_niveaux.get(niveauSelect);
        databaseInfo.SetUser(user);
        niveau.putExtra("niveau",niveauselect);
        actu_niveau_position++;
        databaseInfo.setActuNiveau(actu_niveau_position);
        startActivity(niveau);
    }
    private void StartEmbuscade(){

        Intent embuscade = new Intent(RegionActivity.this, EmbuscadeActivity.class);
        Embuscade embuscadeSelected = SelectEmbuscade();
        databaseInfo.SetUser(user);
        databaseInfo.setActuNiveau(actu_niveau_position);
        embuscade.putExtra("Embuscade",embuscadeSelected);
        startActivity(embuscade);
        finish();

    }
    private Embuscade SelectEmbuscade(){
        Embuscade embuscade = new Embuscade();
        int random = new Random().nextInt(liste_embuscades.size());
        embuscade = liste_embuscades.get(random);
        return embuscade;
    }


    private void MovementPlayer(int niveauPos, int niveauSelect){
        if ((niveauSelect)== actu_niveau_position){
            MoveToken(niveauPos,niveauSelect);
        }
    }
    private boolean TestEmbuscade(int chanceEmbuscade){
        int max_chance = 100;
        int min_chance = 1 ;
        int range = max_chance - min_chance;
        int rand = (int)(Math.random() * range) + min_chance;
        if(rand< chanceEmbuscade){
            return true;
        }
        return false;
    }
    private void ShowEmbuscadePopUp(){
        embuscade_en_cours=true;
        layout_popu_embuscade.setVisibility(View.VISIBLE);
    }

    private void ClosePopUpEmbuscade(){
        embuscade_en_cours=false;
        layout_popu_embuscade.setVisibility(View.INVISIBLE);
    }

    private void MoveToken(int niveauPos,int niveauSelect){
        Path path = new Path();
        path.moveTo(dpToPx(actu_region.getpathpoints()[0][actu_path_position]*case_large,this)-size_player, dpToPx(actu_region.getpathpoints()[1][actu_path_position]*case_hauteur,this)-size_player);
        int taille_path =0;

        int posY=0;
        boolean embuscade = false;
        for(int icompteur = actu_path_position+1; icompteur<= niveauPos;icompteur++){

            if(taille_path>0){
                embuscade = TestEmbuscade(actu_region.chance_embuscade);
                if(embuscade){
                    break;
                }
            }
            posY = dpToPx(actu_region.getpathpoints()[1][icompteur]*case_hauteur,this)-size_player;
            path.lineTo(dpToPx(actu_region.getpathpoints()[0][icompteur]*case_large,this)-size_player, dpToPx(actu_region.getpathpoints()[1][icompteur]*case_hauteur,this)-size_player);
            actu_path_position ++;
            user.actu_path_position = actu_path_position;
            taille_path++;
        }

        ObjectAnimator objectAnimator= ObjectAnimator.ofFloat(player_token, "x", "y", path);
        objectAnimator.setDuration(3000);
        objectAnimator.start();

        int finalPosY = posY-650;
        region_scroll.post(new Runnable() {
            public void run() {
                region_scroll.smoothScrollTo(0, finalPosY);
            }
        });


        boolean finalEmbuscade = embuscade;
        objectAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if(finalEmbuscade){
                    ShowEmbuscadePopUp();
                }
                else{
                    StartNiveau(niveauSelect);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    public void Finregion(){
        if(user.actu_LvL == user.max_LvL){
            user.max_LvL++;
            user.actu_LvL++;
        }
        ReturnHub();
    }
    public void ReturnHub(){
        if(!embuscade_en_cours){
            SaveDBUser();
            Intent connexion = new Intent(RegionActivity.this, HubActivity.class);
            startActivity(connexion);
            finish();
        }
    }


    ///// sent datas

    public void SaveDBUser(){
        String urlConnection = "http://192.168.1.229/TFE/SaveUser.php";
        String nom,password,max_LvL,actu_LvL,mail,po,actu_path_position,nbr_bonus_time,nbr_bonus_embuscade,nbr_bonus_vue;
        nom = user.nom;
        password = user.password;
        max_LvL = ""+user.max_LvL;
        actu_LvL = ""+user.actu_LvL;
        mail = user.mail;
        po = ""+user.po;
        actu_path_position = ""+user.actu_path_position;

        nbr_bonus_time = ""+user.nbr_bonus_time;
        nbr_bonus_embuscade = ""+user.nbr_bonus_embuscade;
        nbr_bonus_vue = ""+user.nbr_bonus_vue;

        if(!nom.equals("") && !password.equals("")){

            //Start ProgressBar first (Set visibility VISIBLE)
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    //Starting Write and Read data with URL
                    //Creating array for parameters
                    String[] field = new String[10];
                    field[0] = "nom";
                    field[1] = "password";
                    field[2] = "max_LvL";
                    field[3] = "actu_LvL";
                    field[4] = "mail";
                    field[5] = "po";
                    field[6] = "actu_path_position";
                    field[7] = "nbr_bonus_time";
                    field[8] = "nbr_bonus_embuscade";
                    field[9] = "nbr_bonus_vue";
                    //Creating array for data
                    String[] data = new String[10];
                    data[0] = nom;
                    data[1] = password;
                    data[2] = max_LvL;
                    data[3] = actu_LvL;
                    data[4] = mail;
                    data[5] = po;
                    data[6] = actu_path_position;
                    data[7] = nbr_bonus_time;
                    data[8] = nbr_bonus_embuscade;
                    data[9] = nbr_bonus_vue;
                    PutData putData = new PutData(urlConnection, "POST", field, data);
                    if (putData.startPut()) {
                        if (putData.onComplete()) {
                            String result = putData.getResult();
                            //End ProgressBar (Set visibility to GONE)
                            Log.i("PutData", result);
                            if (!result.equals("Operation reussite")){
                                Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
                                Error();
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

}
