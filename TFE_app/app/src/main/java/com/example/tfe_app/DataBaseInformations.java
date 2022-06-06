package com.example.tfe_app;

import java.util.ArrayList;
import java.util.HashMap;

public class DataBaseInformations {
    private User user;
    private ArrayList<Region> regions_list;
    private Region actu_region;
    private ArrayList<Niveau> liste_niveaux;
    private ArrayList<Embuscade> liste_embuscades;
    private int actuNiveau = -1;
    private HashMap<String, Integer> mapsRegions = GenerateRegionsMaps();
    private HashMap<String, Integer> mapsLvLs = GenerateNiveauxsMaps();

    private static final DataBaseInformations databaseInfos = new DataBaseInformations();


    /////////////Fonctions//////////////////////


    public static DataBaseInformations getInstance(){
        return databaseInfos;
    }

    public User getUser(){
        return user;
    }
    public void SetUser(User Userset){
        this.user = Userset;
    }

    public ArrayList<Region> getRegions_list(){
        return regions_list;
    }
    public void setRegions_list(ArrayList<Region> regionsListSet){
        this.regions_list = regionsListSet;
    }

    public Region getActu_region(){
        return actu_region;
    }
    public void setActu_region(Region actu_regionSet){
        this.actu_region = actu_regionSet;
    }

    public ArrayList<Niveau> getListe_niveaux(){
        return liste_niveaux;
    }
    public void setListe_niveaux(ArrayList<Niveau> liste_niveauxSet){
        this.liste_niveaux = liste_niveauxSet;
    }

    public ArrayList<Embuscade> getListe_embuscades(){
        return liste_embuscades;
    }
    public void setListe_embuscades(ArrayList<Embuscade> liste_embuscadesSet){
        this.liste_embuscades = liste_embuscadesSet;
    }

    public int getActuNiveau(){
        return actuNiveau;
    }
    public void setActuNiveau(int actuNiveauSet){
        this.actuNiveau = actuNiveauSet;
    }

    public HashMap<String, Integer> GenerateRegionsMaps(){
        HashMap<String, Integer> mapsRegions = new HashMap<String, Integer>();
        mapsRegions.put("R1", R.drawable.region1);
        mapsRegions.put("R2", R.drawable.final_region1);
        return mapsRegions;
    }
    public HashMap<String, Integer> getGeneratedRegionsMaps(){
        return this.mapsRegions;
    }

    public HashMap<String, Integer> GenerateNiveauxsMaps(){
        HashMap<String, Integer> mapsLvLs = new HashMap<String, Integer>();
        mapsLvLs.put("LVL1", R.drawable.lvl1);
        mapsLvLs.put("LVL2", R.drawable.lvl2);
        mapsLvLs.put("LVL3", R.drawable.lvl3);
        mapsLvLs.put("LVL4", R.drawable.lvl4);
        mapsLvLs.put("LVL5", R.drawable.lvl5);
        mapsLvLs.put("LVL6", R.drawable.lvl6);
        mapsLvLs.put("LVL7", R.drawable.lvl7);
        mapsLvLs.put("LVL8", R.drawable.lvl8);
        return mapsLvLs;
    }
    public HashMap<String, Integer> getGeneratedNiveauxMaps(){
        return this.mapsLvLs;
    }

}
