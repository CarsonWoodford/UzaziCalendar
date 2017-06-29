package com.example.carsonwoodford.uzazicalendar;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

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
        title.setText(event.getName());

        TextView date = (TextView) findViewById(R.id.textView2);
        TextView time = (TextView) findViewById(R.id.eventTime);
        Date d = new Date(event.getTime());
        date.setText(java.text.DateFormat.getDateInstance().format(d));
        time.setText(new SimpleDateFormat("hh:mm a").format(d));

        TextView summary = (TextView) findViewById(R.id.eventDescription);
        if(event.getDesc() != "" && event.getDesc() != null && event.getDesc() != " ")
            summary.setText(event.getDesc());
        else
            summary.setText("No description availible.");
    }


}
