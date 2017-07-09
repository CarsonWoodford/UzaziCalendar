package com.example.carsonwoodford.uzazicalendar;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ToggleButton;

import java.util.Date;

public class CreateEvent extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    Spinner hours;
    Spinner minutes;
    EditText title;
    EditText summary;
    ToggleButton toggleButton;
    Button button;
    Intent intent;
    Date date;
    String hourCount = "0";
    String minuteCount = "0";

    ArrayAdapter<CharSequence> adapter;
    ArrayAdapter<CharSequence> adapter2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        hours = (Spinner) findViewById(R.id.Hours);
        minutes = (Spinner) findViewById(R.id.Minutes);
        title = (EditText) findViewById(R.id.Title);
        summary = (EditText) findViewById(R.id.Summary);
        toggleButton = (ToggleButton) findViewById(R.id.toggleButton);
        button = (Button) findViewById(R.id.FinishButton);
        intent = getIntent();
        date = new Date();
        date.setTime(intent.getLongExtra(MainActivity.PASSED_EVENTS, -1));

        adapter = ArrayAdapter.createFromResource(this,
                R.array.hours_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        hours.setAdapter(adapter);
        adapter2 = ArrayAdapter.createFromResource(this,
                R.array.minutes_array, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        minutes.setAdapter(adapter2);

        hours.setOnItemSelectedListener(this);
        minutes.setOnItemSelectedListener(this);
        //Log.v("ValueCheck", Long.toString(date.getTime()));

        //Date test = new Date(1998, 5, 5, 9, 9, 9);
    }

    public void buttonPressed(View v){
        Intent returnIntent = new Intent();
        returnIntent.putExtra("title", title.getText());
        returnIntent.putExtra("summary", summary.getText());
        //returnIntent.putExtra("hours", adapter.getItem());
        returnIntent.putExtra("date", date.getTime() + (3600000 * Long.parseLong(hourCount, 10) + (60000 * Long.parseLong(minuteCount, 10))));
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
        switch (parent.getId()){
            case R.id.Minutes:

                //////////////
                //change values here to represent new numbers
                //////////////
                if(pos < 10)
                    minuteCount = "0" + String.valueOf(pos);
                else
                    minuteCount = String.valueOf(pos);
                break;
            case R.id.Hours:
                hourCount = String.valueOf(pos+1);
                break;
        }
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }
}
