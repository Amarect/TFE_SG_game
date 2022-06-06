package com.example.tfe_app;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Layout;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import java.lang.reflect.Type;
import java.util.ArrayList;

@RequiresApi(api = Build.VERSION_CODES.O)
public class HubActivity extends AppCompatActivity {
    private LinearLayout layoutRegion;
    private ArrayList<Region> regions = new ArrayList();
    HorizontalScrollView scrollMaps;

    private Button btn_alerte_retour;
    private Button btn_alerte_continue;
    private RelativeLayout alerte_fond;
    private TextView txt_alerte_message;
    private String alerte_message = "Attention ! \n Si vous commencez une nouvelle aventure, vous perdrez votre progression actuelle!";

    private int actu_region=0;
    private User user_actu;
    private Region region_selected;
    private int max_region=0;
    private ArrayList<Niveau> liste_niveaux;
    private ArrayList<Embuscade> liste_embuscades;

    private int sizeX_view = 400;

    //texte region
    private int size_title = 60;
    private int size_accroche = 26;
    private int size_txt_button = 25;
    private int size_difficulte = size_accroche +5;


    DataBaseInformations databaseInfo = DataBaseInformations.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hub);
        GetDatas();
        SetViews();
        GenerateRegionsView();
        PositionPlayer();
    }
    private void PositionPlayer(){
        scrollMaps = findViewById(R.id.scrollview_Maps);
        int actu_pos_player = user_actu.actu_LvL * RegionActivity.dpToPx(sizeX_view,this);
        scrollMaps.post(new Runnable() {
            public void run() {
                scrollMaps.smoothScrollTo(actu_pos_player, 0);
            }
        });
    }

    private void SetViews(){
        layoutRegion = findViewById(R.id.layoutRegions);
        txt_alerte_message =findViewById(R.id.txt_alerte_message);
        txt_alerte_message.setText(alerte_message);
        btn_alerte_continue = findViewById(R.id.btn_alerte_continue);
        btn_alerte_retour = findViewById(R.id.btn_alerte_retour);
        btn_alerte_retour.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CloseAlerte();
            }
        });
        alerte_fond = findViewById(R.id.alerte_fond);
    }
    private void GetDatas(){
        try{
            user_actu = databaseInfo.getUser();
            actu_region = user_actu.actu_LvL;
            max_region = user_actu.max_LvL;
            regions = databaseInfo.getRegions_list();
            System.out.println("ALL GET " + regions);
        }
        catch (Exception e){
            GoBack();
        }

    }
    private void GoBack(){
        Toast.makeText(getApplicationContext(),"une erreur est survenue",Toast.LENGTH_SHORT).show();
        Intent connexion = new Intent(HubActivity.this, MainActivity.class);
        startActivity(connexion);
        finish();
    }

    private void GenerateRegionsView(){
        System.out.println("Region " + regions);
        for (int iCompteur =0; iCompteur< regions.size(); iCompteur++){
            //set cadre
            RelativeLayout regionView = new RelativeLayout(getApplicationContext());
            regionView.setBackgroundResource(R.drawable.parchemin_image);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RegionActivity.dpToPx(sizeX_view,this), ViewGroup.LayoutParams.MATCH_PARENT);
            regionView.setLayoutParams(params);

            //set title
            TextView nomRegion = new TextView(getApplicationContext());
            nomRegion.setText(regions.get(iCompteur).nom);
            nomRegion.setTextSize(size_title);
            nomRegion.setTextColor(Color.BLACK);
            nomRegion.setTypeface(getResources().getFont(R.font.offenbacher));
            RelativeLayout.LayoutParams params_txt = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, RegionActivity.dpToPx(200,this));
            params_txt.topMargin = RegionActivity.dpToPx(70,this);
            nomRegion.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            nomRegion.setLayoutParams(params_txt);

            regionView.addView(nomRegion);

            //set accroche
            TextView accrocheRegion = new TextView(getApplicationContext());
            accrocheRegion.setText(regions.get(iCompteur).accroche);
            accrocheRegion.setTextSize(size_accroche);
            accrocheRegion.setTextColor(Color.BLACK);
            accrocheRegion.setTypeface(getResources().getFont(R.font.offenbacher));

            RelativeLayout.LayoutParams params_accroche = new RelativeLayout.LayoutParams(RegionActivity.dpToPx(320,this), RegionActivity.dpToPx(300,this));
            params_accroche.topMargin = RegionActivity.dpToPx(210,this);
            params_accroche.leftMargin = RegionActivity.dpToPx(35,this);
            accrocheRegion.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            accrocheRegion.setLayoutParams(params_accroche);

            regionView.addView(accrocheRegion);

            //set Difficulte

            TextView txt_difficulte = new TextView(getApplicationContext());
            txt_difficulte.setText("Difficulte : " + regions.get(iCompteur).difficulte);
            txt_difficulte.setTextSize(size_difficulte);
            txt_difficulte.setTextColor(Color.BLACK);
            txt_difficulte.setTypeface(getResources().getFont(R.font.offenbacher));

            RelativeLayout.LayoutParams params_difficulte = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, RegionActivity.dpToPx(200,this));
            params_difficulte.topMargin = RegionActivity.dpToPx(490,this);
            txt_difficulte.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            txt_difficulte.setLayoutParams(params_difficulte);

            regionView.addView(txt_difficulte);

            //set button
            Button region_btn = new Button(getApplicationContext());

            RelativeLayout.LayoutParams params_btn = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, RegionActivity.dpToPx(100,this));
            params_btn.topMargin = RegionActivity.dpToPx(520,this);
            region_btn.setLayoutParams(params_btn);
            region_btn.setBackgroundColor(Color.TRANSPARENT);

            region_btn.setTextSize(size_txt_button);
            region_btn.setTextColor(Color.BLACK);
            region_btn.setTypeface(getResources().getFont(R.font.offenbacher));

            String txt_button ="";
            if(iCompteur<=user_actu.max_LvL){
                int finalICompteur = iCompteur;
                if(user_actu.actu_LvL == iCompteur){
                    txt_button ="Reprendre aventure";
                }else{
                    txt_button = "Commencez aventure";
                }
                region_btn.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Verif_alerte(finalICompteur);
                    }
                });
            }
            else{
                txt_button = "Zone non découverte";
            }
            SpannableString content = new SpannableString(txt_button);
            content.setSpan(new UnderlineSpan(), 0, content.length(), 0);

            region_btn.setText(content);
            regionView.addView(region_btn);

            layoutRegion.addView(regionView);
        }
    }

    public void OpenAlerte(int idRegion){
        alerte_fond.setVisibility(View.VISIBLE);
        btn_alerte_continue.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ContinueAlerte(idRegion);
            }
        });
    }
    public void ContinueAlerte(int idRegion){
        user_actu.actu_path_position=0;
        PrepareRegion(idRegion);
    }
    public void CloseAlerte(){
        alerte_fond.setVisibility(View.INVISIBLE);
    }
    public void Verif_alerte(int idRegion){
        if(user_actu.actu_LvL != idRegion){
            OpenAlerte(idRegion);
        }
        else{
            PrepareRegion(idRegion);
        }
    }
    public void PrepareRegion(int idRegion){
        region_selected = regions.get(idRegion);
        user_actu.actu_LvL = idRegion;
        databaseInfo.SetUser(user_actu);
        databaseInfo.setActu_region(region_selected);
        try {
            GetLvL();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void StartRegion(){
        Region region = region_selected;
        databaseInfo.setActu_region(region);
        databaseInfo.setListe_niveaux(liste_niveaux);
        databaseInfo.setListe_embuscades(liste_embuscades);

        if(user_actu.actu_path_position ==0){
            Intent boutique_avant_region = new Intent(HubActivity.this, Boutique_activity.class);
            startActivity(boutique_avant_region);
        }else{
            Intent voyage_region = new Intent(HubActivity.this, RegionActivity.class);
            startActivity(voyage_region);
        }
        finish();

    }


    private void GetLvL() throws InterruptedException {
        String difficulte;
        difficulte = "" + region_selected.difficulte;
        String urlConnection = "http://192.168.1.229/TFE/GetLvLs.php";

        //Start ProgressBar first (Set visibility VISIBLE)
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                //Starting Write and Read data with URL
                //Creating array for parameters
                String[] field = new String[1];
                field[0] = "difficulte";
                //Creating array for data
                String[] data = new String[1];
                data[0] = difficulte;
                PutData putData = new PutData(urlConnection, "POST", field, data);
                if (putData.startPut()) {
                    if (putData.onComplete()) {
                        String result = putData.getResult();
                        //End ProgressBar (Set visibility to GONE)
                        Log.i("PutData", result);
                        if(result.equals("Error: Database connection") ){
                            GoBack();
                        }
                        else{
                            String getJson = result;
                            Type regionListType = new TypeToken<ArrayList<Niveau>>(){}.getType();
                            liste_niveaux = new Gson().fromJson(getJson, regionListType);

                            GetEmbuscades();
                        }
                    }
                }
                //End Write and Read data with URL
            }
        });

    }

    private void GetEmbuscades(){
        String difficulte;
        difficulte = "" + region_selected.difficulte;
        String urlConnection = "http://192.168.1.229/TFE/GetEmbuscades.php";

        //Start ProgressBar first (Set visibility VISIBLE)
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                //Starting Write and Read data with URL
                //Creating array for parameters
                String[] field = new String[1];
                field[0] = "difficulte";
                //Creating array for data
                String[] data = new String[1];
                data[0] = difficulte;
                PutData putData = new PutData(urlConnection, "POST", field, data);
                if (putData.startPut()) {
                    if (putData.onComplete()) {
                        String result = putData.getResult();
                        //End ProgressBar (Set visibility to GONE)
                        Log.i("PutData", result);
                        if(result.equals("Error: Database connection") ){
                            GoBack();
                        }
                        else{
                            String getJson = result;
                            System.out.println("GET EMBUSCADES " + getJson);
                            Type regionListType = new TypeToken<ArrayList<Embuscade>>(){}.getType();
                            liste_embuscades = new Gson().fromJson(getJson, regionListType);
                            StartRegion();
                        }
                    }
                }
                //End Write and Read data with URL
            }
        });
    }


}
