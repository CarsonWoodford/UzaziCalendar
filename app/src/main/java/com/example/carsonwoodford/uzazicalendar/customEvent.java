package com.example.carsonwoodford.uzazicalendar;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.api.services.calendar.Calendar;

import java.sql.Time;
import java.util.Date;
/**
 * Represents an event.
 * <p>
 *     This class has variables and methods that are
 *     used for holding information about an event.
 * </p>
*/
public class customEvent implements Parcelable{

    private String name; //event name
    //private Date date;   //event date
    private long time;   //event time
    private String loc;  //event location
    private String desc; //event description
    private String ppl;  //event participants


    /**
     * Default constructor.
     */
    customEvent(){
        name = "";
        time = 0;
        loc = "";
        desc = "";
        ppl = "";
    }

    /**
     * A Non-default constructor
     * @param inputName the name of the event entered by user
     * @param inputDesc the description of the event entered by user
     * @param inputTime the time of the event entered by user
     */
    customEvent(String inputName, String inputDesc, long inputTime){
        name = inputName;
        desc = inputDesc;
        time = inputTime;
        loc = "";
        ppl = "";
    }

    /**
     * getter that gets event name
     * @return returns the name of the event object
     */
    public String getName() {
        return name;
    }

    /**
     * setter that sets the event name
     * @param name the name of the event
     */
    public void setName(String name) {
        this.name = name;
    }

    /*public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }*/

    /**
     * getter that gets the event time
     * @return event time
     */
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

    /**
     * A Non-default constructor
     * @param in an object which is a container for a message
     */
    protected customEvent(Parcel in) {
        name = in.readString();
        time = in.readLong();
        loc = in.readString();
        desc = in.readString();
        ppl = in.readString();
    }

    /**
     * Writes to a parcel, which is a container for a message
     * @param dest Parcel received which function will write to
     * @param flags
     */
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
