package com.project.kalpawijesooriya.patient;

/**
 * Created by Kalpa Wijesooriya on 8/31/2018.
 */

public class Bookings {
    private String BillImage;
    private String ConsultationID;
    private String Date;
    private  String Number;
    private  String Patient_ID;
    private String Time;
public  Bookings()
{}
    public Bookings(String billImage, String consultationID, String date, String number, String patient_ID, String time) {
        BillImage = billImage;
        ConsultationID = consultationID;
        Date = date;
        Number = number;
        Patient_ID = patient_ID;
        Time = time;
    }

    public String getBillImage() {
        return BillImage;
    }

    public void setBillImage(String billImage) {
        BillImage = billImage;
    }

    public String getConsultationID() {
        return ConsultationID;
    }

    public void setConsultationID(String consultationID) {
        ConsultationID = consultationID;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getNumber() {
        return Number;
    }

    public void setNumber(String number) {
        Number = number;
    }

    public String getPatient_ID() {
        return Patient_ID;
    }

    public void setPatient_ID(String patient_ID) {
        Patient_ID = patient_ID;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }


}
