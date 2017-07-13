package com.example.carsonwoodford.uzazicalendar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * View Event is called to display the google calendar information
 * for the date pressed using the activity_events.xml
 */
public class View_Event extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        Intent intent = getIntent();
        customEvent event = intent.getParcelableExtra(MainActivity.PASSED_EVENTS);


        TextView title = (TextView) findViewById(R.id.eventName);
        TextView date = (TextView) findViewById(R.id.eventDate);
        TextView time = (TextView) findViewById(R.id.eventTime);
        TextView summary = (TextView) findViewById(R.id.eventDescription);

        if (event != null) {
            title.setText(event.getName());

            Date d = new Date(event.getTime());
            date.setText(java.text.DateFormat.getDateInstance().format(d));
            //date.setText("Hello");
            time.setText(new SimpleDateFormat("hh:mm a").format(d));

            if (event.getDesc() != null) {
                if (!event.getDesc().equals("") && !event.getDesc().equals(" "))
                    summary.setText(event.getDesc());
            } else
                summary.setText("No description availible.");
        }
        else {
            title.setText("No events on this date");
            date.setText("No set date");
            time.setText("No set time");
            summary.setText("No description availible.");
        }

    }

}
