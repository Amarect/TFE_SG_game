package com.example.tfe_app;

import java.io.Serializable;

public class User implements Serializable {
    public int id;
    public String nom;
    public int max_LvL;
    public int actu_LvL;
    public String password;
    public String mail;
    public int po;
    public int actu_path_position;

    public int nbr_bonus_time;
    public int nbr_bonus_embuscade;
    public int nbr_bonus_vue;

    public class Usersbuilder{
        private int id;
        private String nom;
        private int max_LvL;
        private int actu_LvL;
        private String password;
        private String mail;
        private int po;
        private int actu_path_position;

        private int nbr_bonus_time;
        private int nbr_bonus_embuscade;
        private int nbr_bonus_vue;

        public Usersbuilder withId(int id){
            this.id = id;
            return this;
        }
        public Usersbuilder withNom(String nom){
            this.nom = nom;
            return this;
        }
        public Usersbuilder withMax_LvL(int max_LvL){
            this.max_LvL = max_LvL;
            return this;
        }
        public Usersbuilder withActu_LvL(int actu_LvL){
            this.actu_LvL = actu_LvL;
            return this;
        }
        public Usersbuilder withPassword(String password){
            this.password = password;
            return this;
        }
        public Usersbuilder withMail(String mail){
            this.mail = mail;
            return this;
        }
        public Usersbuilder withPo(int po){
            this.po = po;
            return this;
        }
        public Usersbuilder withActu_path_position(int actu_path_position){
            this.actu_path_position = actu_path_position;
            return this;
        }
        public Usersbuilder withNbr_bonus_time(int nbr_bonus_time){
            this.nbr_bonus_time = nbr_bonus_time;
            return this;
        }
        public Usersbuilder withNbr_bonus_embuscade(int nbr_bonus_embuscade){
            this.nbr_bonus_embuscade = nbr_bonus_embuscade;
            return this;
        }
        public Usersbuilder withNbr_bonus_vue(int nbr_bonus_vue){
            this.nbr_bonus_vue = nbr_bonus_vue;
            return this;
        }

        public User Build(){
            User user = new User();
            user.nom = nom;
            user.id = id;
            user.max_LvL = max_LvL;
            user.actu_LvL = actu_LvL;
            user.password = password;
            user.mail = mail;
            user.po = po;
            user.actu_path_position = actu_path_position;

            user.nbr_bonus_time = nbr_bonus_time;
            user.nbr_bonus_embuscade = nbr_bonus_embuscade;
            user.nbr_bonus_vue = nbr_bonus_vue;

            return user;
        }
    }
}
