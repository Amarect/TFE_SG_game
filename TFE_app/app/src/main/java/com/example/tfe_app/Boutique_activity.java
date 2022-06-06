package com.example.tfe_app;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.tfe_app.databinding.ActivityBoutiqueBinding;

public class Boutique_activity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityBoutiqueBinding binding;

    private Button btn_bonus_embuscade;
    private Button btn_bonus_vue;
    private Button btn_bonus_temps;
    private Button btn_back;
    private Button btn_start_aventure;

    private TextView nbr_bonus_temps;
    private TextView nbr_bonus_vue;
    private TextView nbr_bonus_embuscade;
    private TextView txt_po_restant;

    private User user;

    private int price_time = 3;
    private int price_vue = 2;
    private int price_embuscade = 3;

    DataBaseInformations databaseInfo = DataBaseInformations.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boutique);
        user = databaseInfo.getUser();
        SetViews();
    }

    private void SetViews(){
        btn_bonus_embuscade = findViewById(R.id.btn_bonus_embuscade);
        btn_bonus_vue = findViewById(R.id.btn_bonus_vue);
        btn_bonus_temps = findViewById(R.id.btn_bonus_temps);
        btn_back = findViewById(R.id.btn_retour);
        btn_start_aventure = findViewById(R.id.btn_start_aventure);

        nbr_bonus_temps = findViewById(R.id.nbr_bonus_temps);
        nbr_bonus_vue = findViewById(R.id.nbr_bonus_vue);
        nbr_bonus_embuscade = findViewById(R.id.nbr_bonus_embuscade);
        txt_po_restant = findViewById(R.id.txt_po_restant);
        RefreshCharges();

        SetButtonEffects();

    }
    private void RefreshCharges(){
        nbr_bonus_temps.setText(""+ user.nbr_bonus_time + " charge(s)");
        nbr_bonus_vue.setText(""+ user.nbr_bonus_vue + " charge(s)");
        nbr_bonus_embuscade.setText(""+ user.nbr_bonus_embuscade + " charge(s)");
        txt_po_restant.setText( "" +user.po + " Po restants");
    }
    private void SetButtonEffects(){

        btn_bonus_embuscade.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(price_time <= user.po){
                    user.po = user.po - price_time;
                    Achat_bonus(3);
                }
            }
        });
        btn_bonus_vue.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(price_vue <= user.po){
                    user.po = user.po - price_vue;
                    Achat_bonus(2);
                }
            }
        });
        btn_bonus_temps.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(price_embuscade <= user.po){
                    user.po = user.po - price_embuscade;
                    Achat_bonus(1);
                }
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                GoBack();
            }
        });
        btn_start_aventure.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                StartAventure();
            }
        });

    }
    private void Achat_bonus(int type_bonus){
        if(type_bonus == 1){ //bonus time
            user.nbr_bonus_time++;
        }
        else if(type_bonus == 2){ //bonus vue
            user.nbr_bonus_vue++;
        }
        else if(type_bonus == 3){ //bonus embuscade
            user.nbr_bonus_embuscade++;
        }
        RefreshCharges();
    }
    private void GoBack(){
        databaseInfo.SetUser(user);
        Intent region = new Intent(Boutique_activity.this, HubActivity.class);

        startActivity(region);
        finish();
    }
    private void StartAventure(){
        databaseInfo.SetUser(user);
        Intent region = new Intent(Boutique_activity.this, RegionActivity.class);

        startActivity(region);
        finish();

    }
}