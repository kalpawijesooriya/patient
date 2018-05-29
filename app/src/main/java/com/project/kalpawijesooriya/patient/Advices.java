package com.project.kalpawijesooriya.patient;

/**
 * Created by Kalpa Wijesooriya on 5/6/2018.
 */

public class Advices {



        private String title;
        private String discription;
        private String image;


        public  Advices() {
        }
        public  Advices(String title, String discription, String image ) {
            this.title = title;
            this.discription = discription;
            this.image = image;
        }

        public String gettitle() {
            return title;
        }
        public void settitle(String title) {
            this.title =title;
        }
        public String getdiscription() {
            return discription;
        }
        public void setdiscription(String discription) {
            this.discription = discription;
        }
        public String getImage() {
            return image;
        }
        public void setImage(String image) {
            this.image = image;
        }





    }

