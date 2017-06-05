package com.example.carsonwoodford.uzazicalendar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.apache.ApacheHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.Throwables;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.client.util.DateTime;

import com.google.api.services.calendar.*;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

import static java.security.AccessController.getContext;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

public class MainActivity extends AppCompatActivity {

    private static HttpTransport HTTP_TRANSPORT;
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static FileDataStoreFactory DATA_STORE_FACTORY;
    //private static final java.io.File DATA_STORE_DIR = new java.io.File(System.getProperty("user.home"), "uzazi-village");
    private static java.io.File DATA_STORE_DIR;
    private static final List<String> SCOPES =
            Arrays.asList(CalendarScopes.CALENDAR_READONLY);
    private static final String APPLICATION_NAME =
            "UzaziVillage";
    com.google.api.services.calendar.Calendar myCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        try {
            //HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            HTTP_TRANSPORT = new ApacheHttpTransport();
            DATA_STORE_DIR = new java.io.File(getFilesDir(), "uzazi-village");
            DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
            Credential credential = authorize();
            myCalendar = getCalendarService();
            CalendarListEntry calendarListEntry = new CalendarListEntry();
            calendarListEntry.setId("i3jbqatm5hjsmf101mfd0isadk@group.calendar.google.com");
            myCalendar.calendarList().insert(calendarListEntry);
            Events events = myCalendar.events().list(calendarListEntry.getId())
                    .setMaxResults(10)
                    .setTimeMin(new DateTime(System.currentTimeMillis()))
                    .setOrderBy("startTime")
                    .setSingleEvents(true)
                    .execute();
            List<Event> items = events.getItems();
            assertEquals(items, events.getItems());
            assertTrue(JSON_FACTORY == JacksonFactory.getDefaultInstance());
            assertEquals(myCalendar.calendars().toString(), "Uzazi-Village");
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(1);
        }


    }

    public static Credential authorize() throws IOException {
        // Load client secrets.
        InputStream in =
                MainActivity.class.getResourceAsStream("/client_secret.json");
        GoogleClientSecrets clientSecrets =
                GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow =
                new GoogleAuthorizationCodeFlow.Builder(
                        HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                        .setDataStoreFactory(DATA_STORE_FACTORY)
                        .setAccessType("offline")
                        .build();
        Credential credential = new AuthorizationCodeInstalledApp(
                flow, new LocalServerReceiver()).authorize("user");
        System.out.println(
                "Credentials saved to " + DATA_STORE_DIR.getAbsolutePath());
        return credential;
    }

    public static com.google.api.services.calendar.Calendar getCalendarService() throws IOException {
        Credential credential = authorize();
        return new com.google.api.services.calendar.Calendar.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    //function for the Events/Calendar button goes here

    //Function for the Donation button goes here.
    //The quick brown fox jumped over the lazy dog
}
