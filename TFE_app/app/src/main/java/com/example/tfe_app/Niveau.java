package com.example.tfe_app;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class Niveau implements Serializable {
    public String code;
    public int difficulte;
    public int emplacement = 0; //position sur les pathways
    public int temps_limite; // en secondes
    public int nbr_erreurs;
    public String emplacement_erreurs; //emplacement des erreurs dans le code.
    public String tranche_score;
    public String enonce;
    public String correction_erreurs;

    public int[][] GetEmplacement_erreurs(){
        return new Gson().fromJson(emplacement_erreurs, int[][].class);
    }
    public int[] GetTranche_score(){
        return new Gson().fromJson(tranche_score, int[].class);
    }
    public String[]GetCorrection_erreurs(){
        return new Gson().fromJson(correction_erreurs, String[].class);
    }

    public static class NiveauBuilder {
        private String code = "no_code";
        private int difficulte = -1;
        private int emplacement = 0;
        private int temps_limite = 60;
        private int nbr_erreurs = -1;
        private String emplacement_erreurs = null;
        private String tranche_score = null;
        private String enonce = "no_enonce";
        private String correction_erreurs ="null";

        public NiveauBuilder withCode(String code){
            this.code = code;
            return this;
        }
        public NiveauBuilder withDifficulte(int difficulte){
            this.difficulte = difficulte;
            return this;
        }
        public NiveauBuilder withemplacement(int emplacement){
            this.emplacement = emplacement;
            return this;
        }
        public NiveauBuilder withTemps_limite(int temps_limite){
            this.temps_limite = temps_limite;
            return this;
        }
        public NiveauBuilder withNbr_erreurs(int nbr_erreurs){
            this.nbr_erreurs = nbr_erreurs;
            return this;
        }
        public NiveauBuilder withEmplacement_erreurs(int[][] emplacement_erreurs){
            this.emplacement_erreurs = new Gson().toJson(emplacement_erreurs);
            return this;
        }
        public NiveauBuilder withTranche_score(int[] tranche_score){
            this.tranche_score = new Gson().toJson(tranche_score);
            return this;
        }
        public NiveauBuilder withEnonce(String enonce){
            this.enonce = enonce;
            return this;
        }
        public NiveauBuilder withCorrection_erreurs(String[] correction_erreurs){
            this.correction_erreurs = new Gson().toJson(correction_erreurs);
            return this;
        }
        public Niveau Build(){
            Niveau niveau = new Niveau();
            niveau.code = code;
            niveau.difficulte = difficulte;
            niveau.emplacement = emplacement;
            niveau.temps_limite = temps_limite;
            niveau.nbr_erreurs = nbr_erreurs;
            niveau.emplacement_erreurs = emplacement_erreurs;
            niveau.tranche_score = tranche_score;
            niveau.enonce = enonce;
            niveau.correction_erreurs = correction_erreurs;
            return niveau;
        }
    }
}
