package com.example.tfe_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vishnusivadas.advanced_httpurlconnection.FetchData;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import java.lang.reflect.Type;
import java.sql.Connection;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    TextInputEditText txt_nom, txtinput_password;
    Button btn_connexion;
    String urlConnection = "http://192.168.1.229/TFE/login.php";

    ArrayList<Region> region_list = new ArrayList<Region>();

    DataBaseInformations databaseInfo = DataBaseInformations.getInstance();
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setViews();


    }
    private void setViews(){
        txt_nom = findViewById(R.id.txt_nom);
        txtinput_password = findViewById(R.id.txtinput_password);


        btn_connexion= (Button)findViewById(R.id.btnConnexion);
        btn_connexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TryConnexion();
            }
        });

        Button btn_signup = findViewById(R.id.btn_signup);
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent signUp = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(signUp);
                finish();
            }
        });
    }


    private void TryConnexion(){
        String nom,password;
        nom = txt_nom.getText().toString();
        password = txtinput_password.getText().toString();

        if(!nom.equals("") && !password.equals("")){

            //Start ProgressBar first (Set visibility VISIBLE)
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    //Starting Write and Read data with URL
                    //Creating array for parameters
                    String[] field = new String[2];
                    field[0] = "nom";
                    field[1] = "password";
                    //Creating array for data
                    String[] data = new String[2];
                    data[0] = nom;
                    data[1] = password;
                    PutData putData = new PutData(urlConnection, "POST", field, data);
                    if (putData.startPut()) {
                        if (putData.onComplete()) {
                            String result = putData.getResult();
                            //End ProgressBar (Set visibility to GONE)
                            Log.i("PutData", result);
                            if(result.equals("Nom or Password wrong")){
                                Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
                            }
                            else if(result.equals("Error: Database connection") ){
                                Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Gson gson = new Gson();
                                String getJson = result;
                                user = new Gson().fromJson(result, User.class);
                                //etape 2 connexion
                                GetRegions();
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
    public void GetRegions(){

        System.out.println("test json regions = " );

        String urlConnection = "http://192.168.1.229/TFE/Getregions.php";

        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                FetchData fetchdata = new FetchData (urlConnection);
                if (fetchdata.startFetch()) {
                    if (fetchdata.onComplete()) {
                        String result = fetchdata.getResult();
                        Log.i("PutData", result);

                        if(result.equals("Error: Database connection") ){
                            Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
                        }
                        else{
                            String getJson = result;
                            Type regionListType = new TypeToken<ArrayList<Region>>(){}.getType();
                            region_list = new Gson().fromJson(result, regionListType);
                            System.out.println("JSON GET " +region_list);
                            System.out.println("Data test2 " + region_list.get(0).nom);
                            System.out.println("Data test " + region_list.get(0).pathpoints);

                            //start a tout
                            startApp();
                        }
                    }
                }
                //End Write and Read data with URL
            }
        });
        System.out.println("escape" );

    }

    public void startApp(){
        databaseInfo.SetUser(user);
        databaseInfo.setRegions_list(region_list);
        Intent hub = new Intent(MainActivity.this, HubActivity.class);
        hub.putExtra("User", user);
        System.out.println("JSON GETTED " + region_list);
        hub.putExtra("Liste_regions",region_list);
        hub.putExtra("ActuRegion",0);
        hub.putExtra("Max_region",1);
        startActivity(hub);
        finish();
    }


}