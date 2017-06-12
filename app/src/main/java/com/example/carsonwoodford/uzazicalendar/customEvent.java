package com.example.carsonwoodford.uzazicalendar;


import java.sql.Time;
import java.util.Date;

public class customEvent {

    private String name; //event name
    private Date date;   //event date
    private Time time;   //event time
    private String loc;  //event location
    private String desc; //event description
    private String ppl;  //event participants

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
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
}
