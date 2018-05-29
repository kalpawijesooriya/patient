package com.project.kalpawijesooriya.patient;

/**
 * Created by Kalpa Wijesooriya on 4/29/2018.
 */

public class Consultation {
    private String Date;
    private String DoctorID;
    private String Room;
    private String ScheduleID;

    private String LastAppoimentNo;



    public Consultation(String date, String doctorID, String room, String scheduleID) {
        Date = date;
        DoctorID = doctorID;
        Room = room;
        ScheduleID = scheduleID;
    }


    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getDoctorID() {
        return DoctorID;
    }

    public void setDoctorID(String doctorID) {
        DoctorID = doctorID;
    }

    public String getRoom() {
        return Room;
    }

    public void setRoom(String room) {
        Room = room;
    }

    public String getScheduleID() {
        return ScheduleID;
    }

    public void setScheduleID(String scheduleID) {
        ScheduleID = scheduleID;
    }

    public void setLastAppoimentNo(String lastAppoimentNo) {LastAppoimentNo = lastAppoimentNo;}

    public String getLastAppoimentNo() {return LastAppoimentNo;}



    public Consultation() {}



}
