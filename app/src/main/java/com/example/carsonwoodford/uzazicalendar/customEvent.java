package com.example.carsonwoodford.uzazicalendar;


import android.os.Parcel;
import android.os.Parcelable;

import java.sql.Time;
import java.util.Date;

public class customEvent implements Parcelable{

    private String name; //event name
    //private Date date;   //event date
    private long time;   //event time
    private String loc;  //event location
    private String desc; //event description
    private String ppl;  //event participants

    customEvent(){
        name = "";
        time = 0;
        loc = "";
        desc = "";
        ppl = "";
    }

    customEvent(String inputName, String inputDesc, long inputTime){
        name = inputName;
        desc = inputDesc;
        time = inputTime;
        loc = "";
        ppl = "";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /*public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }*/

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getLoc() {
        return loc;
    }

    public void setLoc(String loc) {
        this.loc = loc;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPpl() {
        return ppl;
    }

    public void setPpl(String ppl) {
        this.ppl = ppl;
    }

    @Override
    public int describeContents() {
        return 0;
    }


    protected customEvent(Parcel in) {
        name = in.readString();
        time = in.readLong();
        loc = in.readString();
        desc = in.readString();
        ppl = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeLong(time);
        dest.writeString(loc);
        dest.writeString(desc);
        dest.writeString(ppl);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<customEvent> CREATOR = new Parcelable.Creator<customEvent>() {
        @Override
        public customEvent createFromParcel(Parcel in) {
            return new customEvent(in);
        }

        @Override
        public customEvent[] newArray(int size) {
            return new customEvent[size];
        }
    };
}
