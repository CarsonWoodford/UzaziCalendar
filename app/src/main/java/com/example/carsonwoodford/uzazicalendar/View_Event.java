package com.example.carsonwoodford.uzazicalendar;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

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
        String passedEvents = intent.getStringExtra(MainActivity.PASSED_EVENTS);
        TextView title = (TextView) findViewById(R.id.eventName);
        title.setText(passedEvents);
    }


}
