package com.example.tfe_app;

import com.google.gson.Gson;

import java.io.Serializable;

public class Embuscade implements Serializable {
    public String question;
    public String reponses;
    public String correction;
    public int reponse;
    public int difficulte;

    public String[] GetReponses(){
        return new Gson().fromJson(reponses, String[].class);
    }

    public static class EmbuscadeBuilder{
        private String question = "no_question";
        private String reponses = null;
        private String correction = "pas de correction";
        private int reponse = 0;
        private int difficulte = 0;

        public EmbuscadeBuilder withQuestion(String question){
            this.question = question;
            return this;
        }
        public EmbuscadeBuilder withReponses(String[] reponses){
            this.reponses = new Gson().toJson(reponses);
            return this;
        }
        public EmbuscadeBuilder withReponse(int reponse){
            this.reponse = reponse;
            return this;
        }
        public EmbuscadeBuilder withDifficulte(int difficulte){
            this.difficulte = difficulte;
            return this;
        }
        public EmbuscadeBuilder withCorrection(String correction){
            this.correction = correction;
            return this;
        }

        public Embuscade Build(){
            Embuscade embuscade = new Embuscade();
            embuscade.question = question;
            embuscade.reponses = reponses;
            embuscade.reponse = reponse;
            embuscade.difficulte = difficulte;
            embuscade.correction = correction;
            return embuscade;
        }

    }
}
