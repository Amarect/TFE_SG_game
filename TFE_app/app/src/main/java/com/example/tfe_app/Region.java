package com.example.tfe_app;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;

public class Region implements Serializable {
    public String nom;
    public String liste_niveaux ;
    public String pathpoints; //niveau save ou ils sont sur le pathway
    public int difficulte;
    public String image;
    public int chance_embuscade;
    public int tailleMaps;
    public String accroche;

    public int[] getPosition_LvL(){
        int [] list_pos_lvl = new Gson().fromJson(liste_niveaux, int[].class);
        return list_pos_lvl;
    }
    public int[][] getpathpoints(){
        int [][] list_path_points = new Gson().fromJson(pathpoints, int[][].class);
        return list_path_points;
    }

    public static class RegionBuilder {
        private String nom = "no_name";
        private String liste_niveaux  = null;
        private String pathpoints= null;
        private int difficulte = 0;
        private String image = "no_image";
        private int chance_embuscade=0;
        private int tailleMaps = 1;
        private String accroche="no_accroche";

        public RegionBuilder withNom (String nom){
            this.nom = nom;
            return this;
        }
        public RegionBuilder withposition_LvL (int[] position_LvL){
            this.liste_niveaux = new Gson().toJson(position_LvL);   ;
            return this;
        }
        public RegionBuilder withPathpoints (int[][] pathpoints){
            this.pathpoints = new Gson().toJson(pathpoints);
            return this;
        }
        public RegionBuilder withDifficulte (int difficulte){
            this.difficulte = difficulte;
            return this;
        }
        public RegionBuilder withImage(String image){
            this.image = image;
            return this;
        }
        public RegionBuilder withChance_embuscade(int chance_embuscade){
            this.chance_embuscade = chance_embuscade;
            return this;
        }
        public RegionBuilder withTailleMaps (int tailleMaps){
            this.tailleMaps = tailleMaps;
            return this;
        }
        public RegionBuilder withAccroche(String accroche){
            this.accroche = accroche;
            return this;
        }

        public Region build(){
            Region region = new Region();
            region.nom = nom;
            region.liste_niveaux = liste_niveaux;
            region.pathpoints = pathpoints;
            region.difficulte = difficulte;
            region.image = image;
            region.chance_embuscade = chance_embuscade;
            region.tailleMaps = tailleMaps;
            region.accroche = accroche;
            return region;
        }
    }

}
