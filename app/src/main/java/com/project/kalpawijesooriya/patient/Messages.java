package com.project.kalpawijesooriya.patient;

/**
 * Created by Kalpa Wijesooriya on 3/26/2018.
 */

public class Messages {
    private String message;
    private boolean seen;
    private long time;
    private String from;
    private String type;
    private String image;






    public Messages(String message,  boolean seen, long time, String type, String image) {
        this.message = message;
        this.seen = seen;
        this.time = time;
        this.image = image;
        this.type = type;
        this.message = message;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


    public void setFrom(String from) {
        this.from = from;
    }

    public String getFrom() {
        return from;
    }


    public void setMessage(String message) {
        this.message = message;
    }

    public void setSeen( boolean seen) {
        this.seen = seen;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void setType(String type) {
        this.type = type;
    }



    public String getMessage() {
        return message;
    }

    public boolean getSeen() {
        return seen;
    }

    public long getTime() {
        return time;
    }

    public String getType() {
        return type;
    }





    public Messages()
    {}


}
