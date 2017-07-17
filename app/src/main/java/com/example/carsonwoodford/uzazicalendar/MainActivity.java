package com.example.carsonwoodford.uzazicalendar;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.*;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;

import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.ExponentialBackOff;

import com.google.api.services.calendar.*;
import com.google.api.client.util.DateTime;

import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.*;
import com.google.api.services.calendar.model.Event;

import android.Manifest;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
//import android.text.method.ScrollingMovementMethod;
//import android.view.View;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
//import android.widget.LinearLayout;
//import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;


import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

//import com.google.gson.Gson;

import static java.security.AccessController.getContext;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * Main_Activity
 * MainActivity is the basis for the app. It contains the calendar, which
 * is the main part of the app, and the code to supply it with information.
 * It also contains the neccessary code and buttons to navigate to the other
 * activities in the app as well as pass information along to them.
 * @author Carson Woodford, Heather Brune, Marcus Hedgecock
 */

public class MainActivity extends Activity
        implements EasyPermissions.PermissionCallbacks {
    GoogleAccountCredential mCredential;
    private TextView mOutputText;
    //private Button mCallApiButton;
    ProgressDialog mProgress;

    static final int REQUEST_ACCOUNT_PICKER = 1000;
    static final int REQUEST_AUTHORIZATION = 1001;
    static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    static final int REQUEST_PERMISSION_GET_ACCOUNTS = 1003;
    static final String STATE_NOTIFICATIONS = "WantsNotifications";

    static final int ADD_EVENT_RETURN = 5000;

    private static final String BUTTON_TEXT = "Call Google Calendar API";
    public static final String PREF_ACCOUNT_NAME = "accountName";
    public static final String[] SCOPES = { CalendarScopes.CALENDAR };
    public static final String PASSED_EVENTS = "Passed Events";
    public static final String PASSED_CALENDAR = "Passed Calendar";
    public static final String PREFS_NAME = "MyPrefsFile";

    private com.google.api.services.calendar.Calendar mService = null;

    CompactCalendarView compactCalendarView;
    TextView monthDisplay;

    List<customEvent> customEvents = new ArrayList<customEvent>();

    private boolean wantsNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        compactCalendarView = (CompactCalendarView) findViewById(R.id.compactcalendar_view);

        /*if (savedInstanceState != null) {
            // Restore value of members from saved state
            wantsNotes = savedInstanceState.getBoolean(STATE_NOTIFICATIONS);
        } else {
            int requestCode = 1; // Or some number you choose
            startActivityForResult(new Intent(MainActivity.this, RequestNotifications.class), requestCode);
        }*/

        mOutputText = new TextView(this);
        //assertEquals(items, events.getItems());
        //assertTrue(JSON_FACTORY == JacksonFactory.getDefaultInstance());
        //assertEquals(myCalendar.calendar().toString, "Uzazi Village");

        ViewGroup.LayoutParams tlp = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        mOutputText.setText("");
        mOutputText.setText(
                "Click the \'" + BUTTON_TEXT +"\' button to test the API.");

        mProgress = new ProgressDialog(this);
        mProgress.setMessage("Calling Google Calendar API ...");

        // Initialize credentials and service object.
        mCredential = GoogleAccountCredential.usingOAuth2(
                getApplicationContext(), Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff());
        getResultsFromApi();

        final Intent intent = new Intent(this, View_Event.class);

        if (compactCalendarView == null){
            Log.d("testing","it is null");
        }
        else
            Log.d("testing","it is not null");


        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            /**
             * Gets the selected date and opens a new activity with it.
             * @param dateClicked indicates the selected date
             */
            @Override
            public void onDayClick(Date dateClicked) {
                boolean areEvents = false;
                List<com.github.sundeepk.compactcalendarview.domain.Event> events = compactCalendarView.getEvents(dateClicked);
                for (com.github.sundeepk.compactcalendarview.domain.Event element : events){
                    for (customEvent elementOther : customEvents) {
                        if (element.getTimeInMillis() == elementOther.getTime()) {
                            intent.putExtra(PASSED_EVENTS, elementOther);
                            areEvents = true;
                        }
                    }
                }
                if (areEvents)
                    startActivity(intent);
                else {
                    Intent intentOther = new Intent(MainActivity.this, CreateEvent.class);
                    intentOther.putExtra(PASSED_EVENTS, dateClicked.getTime());
                    startActivityForResult(intentOther, ADD_EVENT_RETURN);
                }
            }

            /**
             * Unused function that is called when the month is changed.
             * @param firstDayOfNewMonth sets the first day of the month.
             */
            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
            }
        });

        monthDisplay = (TextView) findViewById(R.id.monthDisplayed);

        Date temp = compactCalendarView.getFirstDayOfCurrentMonth();
        monthDisplay.setText(new SimpleDateFormat("MMMM").format(temp));


    }

    /**
     * Attempt to call the API, after verifying that all the preconditions are
     * satisfied. The preconditions are: Google Play Services installed, an
     * account was selected and the device currently has online access. If any
     * of the preconditions are not satisfied, the app will prompt the user as
     * appropriate.
     */
    private void getResultsFromApi() {
        if (! isGooglePlayServicesAvailable()) {
            acquireGooglePlayServices();
        }  else if (! isDeviceOnline()) {
            Toast.makeText(MainActivity.this, "No network connection availible",
                    Toast.LENGTH_LONG).show();
            Log.e("Debugging", "No network connection available");
        } else if (mCredential.getSelectedAccountName() == null) {
            chooseAccount();
        }else {
            new MakeRequestTask(mCredential).execute();
        }
    }

    /**
     * Attempts to set the account used with the API credentials. If an account
     * name was previously saved it will use that one; otherwise an account
     * picker dialog will be shown to the user. Note that the setting the
     * account to use with the credentials object requires the app to have the
     * GET_ACCOUNTS permission, which is requested here if it is not already
     * present. The AfterPermissionGranted annotation indicates that this
     * function will be rerun automatically whenever the GET_ACCOUNTS permission
     * is granted.
     */
    @AfterPermissionGranted(REQUEST_PERMISSION_GET_ACCOUNTS)
    private void chooseAccount() {
        if (EasyPermissions.hasPermissions(
                this, Manifest.permission.GET_ACCOUNTS)) {
            String accountName = getPreferences(Context.MODE_PRIVATE)
                    .getString(PREF_ACCOUNT_NAME, null);
            if (accountName != null) {
                mCredential.setSelectedAccountName(accountName);
                getResultsFromApi();
            } else {
                // Start a dialog from which the user can choose an account
                startActivityForResult(
                        mCredential.newChooseAccountIntent(),
                        REQUEST_ACCOUNT_PICKER);
            }
        } else {
            // Request the GET_ACCOUNTS permission via a user dialog
            EasyPermissions.requestPermissions(
                    this,
                    "This app needs to access your Google account (via Contacts).",
                    REQUEST_PERMISSION_GET_ACCOUNTS,
                    Manifest.permission.GET_ACCOUNTS);
        }
    }

    /**
     * Called when an activity launched here (specifically, AccountPicker
     * and authorization) exits, giving you the requestCode you started it with,
     * the resultCode it returned, and any additional data from it.
     * @param requestCode code indicating which activity result is incoming.
     * @param resultCode code indicating the result of the incoming
     *     activity result.
     * @param data Intent (containing result data) returned by incoming
     *     activity result.
     */
    @Override
    protected void onActivityResult(
            int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case 1:
                wantsNotes = data.getExtras().getBoolean("canNotify");
                SharedPreferences settings2 = getSharedPreferences(PREFS_NAME, 0);
                SharedPreferences.Editor editor2 = settings2.edit();
                editor2.putBoolean("silentMode", wantsNotes);
                editor2.apply();
                break;
            case ADD_EVENT_RETURN:
                if (data != null)
                    if (data.hasExtra("title") && data.hasExtra("summary") && data.hasExtra("date"))
                        new InsertTask(data.getStringExtra("title"), data.getStringExtra("summary"), data.getLongExtra("date", -1)).execute();
                break;
            case REQUEST_GOOGLE_PLAY_SERVICES:
                if (resultCode != RESULT_OK) {
                    Toast.makeText(MainActivity.this, "This app requires Google Play Services. Please install Google Play Services on your device and relaunch this app.",
                            Toast.LENGTH_LONG).show();
                    Log.e("Debugging", "This app requires Google Play Services. Please install Google Play Services on your device and relaunch this app.");
                } else {
                    getResultsFromApi();
                }
                break;
            case REQUEST_ACCOUNT_PICKER:
                if (resultCode == RESULT_OK && data != null &&
                        data.getExtras() != null) {
                    String accountName =
                            data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                    if (accountName != null) {
                        SharedPreferences settings =
                                getPreferences(Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString(PREF_ACCOUNT_NAME, accountName);
                        editor.apply();
                        mCredential.setSelectedAccountName(accountName);
                        getResultsFromApi();
                    }
                }
                break;
            case REQUEST_AUTHORIZATION:
                if (resultCode == RESULT_OK) {
                    getResultsFromApi();
                }
                break;
        }
    }

    /**
     * Respond to requests for permissions at runtime for API 23 and above.
     * @param requestCode The request code passed in
     *     requestPermissions(android.app.Activity, String, int, String[])
     * @param permissions The requested permissions. Never null.
     * @param grantResults The grant results for the corresponding permissions
     *     which is either PERMISSION_GRANTED or PERMISSION_DENIED. Never null.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(
                requestCode, permissions, grantResults, this);
    }

    /**
     * Callback for when a permission is granted using the EasyPermissions
     * library.
     * @param requestCode The request code associated with the requested
     *         permission
     * @param list The requested permission list. Never null.
     */
    @Override
    public void onPermissionsGranted(int requestCode, List<String> list) {
        // Do nothing.
    }

    /**
     * Callback for when a permission is denied using the EasyPermissions
     * library.
     * @param requestCode The request code associated with the requested
     *         permission
     * @param list The requested permission list. Never null.
     */
    @Override
    public void onPermissionsDenied(int requestCode, List<String> list) {
        // Do nothing.
    }

    /**
     * Checks whether the device currently has a network connection.
     * @return true if the device has a network connection, false otherwise.
     */
    private boolean isDeviceOnline() {
        ConnectivityManager connMgr =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    /**
     * Check that Google Play services APK is installed and up to date.
     * @return true if Google Play Services is available and up to
     *     date on this device; false otherwise.
     */
    private boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability apiAvailability =
                GoogleApiAvailability.getInstance();
        final int connectionStatusCode =
                apiAvailability.isGooglePlayServicesAvailable(this);
        return connectionStatusCode == ConnectionResult.SUCCESS;
    }

    /**
     * Attempt to resolve a missing, out-of-date, invalid or disabled Google
     * Play Services installation via a user dialog, if possible.
     */
    private void acquireGooglePlayServices() {
        GoogleApiAvailability apiAvailability =
                GoogleApiAvailability.getInstance();
        final int connectionStatusCode =
                apiAvailability.isGooglePlayServicesAvailable(this);
        if (apiAvailability.isUserResolvableError(connectionStatusCode)) {
            showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode);
        }
    }


    /**
     * Display an error dialog showing that Google Play Services is missing
     * or out of date.
     * @param connectionStatusCode code describing the presence (or lack of)
     *     Google Play Services on this device.
     */
    void showGooglePlayServicesAvailabilityErrorDialog(
            final int connectionStatusCode) {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        Dialog dialog = apiAvailability.getErrorDialog(
                MainActivity.this,
                connectionStatusCode,
                REQUEST_GOOGLE_PLAY_SERVICES);
        dialog.show();
    }
















    /**
     * An asynchronous task that handles the Google Calendar API call.
     * Placing the API calls in their own task ensures the UI stays responsive.
     */
    private class MakeRequestTask extends AsyncTask<Void, Void, List<String>> {
        //private com.google.api.services.calendar.Calendar mService = null;
        private Exception mLastError = null;

        MakeRequestTask(GoogleAccountCredential credential) {
            HttpTransport transport = AndroidHttp.newCompatibleTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            mService = new com.google.api.services.calendar.Calendar.Builder(
                    transport, jsonFactory, credential)
                    .setApplicationName("Uzazi Village")
                    .build();
        }

        /**
         * Background task to call Google Calendar API.
         * @param params no parameters needed for this task.
         */
        @Override
        protected List<String> doInBackground(Void... params) {
            try {
                return getDataFromApi();
            } catch (Exception e) {
                mLastError = e;
                cancel(true);
                return null;
            }
        }

        /**
         * Fetch a list of the next 10 events from the primary calendar.
         * //@return List of Strings describing returned events.
         * //@throws //IOException
         */
        private List<String> getDataFromApi() throws IOException {
            // List the next 10 events from the primary calendar.
            DateTime now = new DateTime(System.currentTimeMillis());
            List<String> eventStrings = new ArrayList<String>();
            CalendarListEntry publicCalendar = new CalendarListEntry();
            //publicCalendar.setId("i3jbqatm5hjsmf101mfd0isadk@group.calendar.google.com");
            publicCalendar.setId("info@uzazivillage.com");
            mService.calendarList().insert(publicCalendar);
            Events events = mService.events().list(publicCalendar.getId())
                    .setMaxResults(40)
                    .setTimeMin(now)
                    .setOrderBy("startTime")
                    .setSingleEvents(true)
                    .execute();
            List<Event> items = events.getItems();

            for (Event event : items) {
                DateTime start = event.getStart().getDateTime();
                if (start == null) {
                    // All-day events don't have start times, so just use
                    // the start date.
                    start = event.getStart().getDate();
                }
                com.github.sundeepk.compactcalendarview.domain.Event temp = new com.github.sundeepk.compactcalendarview.domain.Event(Color.MAGENTA, start.getValue(), event.getSummary());
                compactCalendarView.addEvent(temp, false);
                customEvent tempEvent = new customEvent(event.getSummary(), event.getDescription(), start.getValue());
                customEvents.add(tempEvent);
                eventStrings.add(
                        String.format("%s (%s)", event.getSummary(), start));
            }
            Log.i("Connection","Successfully connected to API.");
            return eventStrings;
        }


        @Override
        protected void onPreExecute() {
            mOutputText.setText("");
            mProgress.show();
        }

        @Override
        protected void onPostExecute(List<String> output) {
            mProgress.hide();
            if (output == null || output.size() == 0) {
                Toast.makeText(MainActivity.this, "No results returned",
                        Toast.LENGTH_LONG).show();
                Log.e("Debugging", "No results returned");
            } else {
                output.add(0, "Data retrieved using the Google Calendar API:");
                mOutputText.setText(TextUtils.join("\n", output));
            }
        }

        @Override
        protected void onCancelled() {
            mProgress.hide();
            if (mLastError != null) {
                if (mLastError instanceof GooglePlayServicesAvailabilityIOException) {
                    showGooglePlayServicesAvailabilityErrorDialog(
                            ((GooglePlayServicesAvailabilityIOException) mLastError)
                                    .getConnectionStatusCode());
                } else if (mLastError instanceof UserRecoverableAuthIOException) {
                    startActivityForResult(
                            ((UserRecoverableAuthIOException) mLastError).getIntent(),
                            MainActivity.REQUEST_AUTHORIZATION);
                } else {
                    Toast.makeText(MainActivity.this, "The following error occured:\n" + mLastError.getMessage(),
                            Toast.LENGTH_LONG).show();
                    Log.e("Debugging", "The following error occurred: " + mLastError.getMessage());
                }
            } else {
                Toast.makeText(MainActivity.this, "Request cancelled",
                        Toast.LENGTH_LONG).show();
                Log.e("Debugging", "Request cancelled.");
            }
        }
    }










    /**
     * An asynchronous task that handles the Google Calendar API call.
     * Placing the API calls in their own task ensures the UI stays responsive.
     */
    private class InsertTask extends AsyncTask<Void, Void, List<String>> {
        Event event;
        DateTime startDateTime;
        EventDateTime start;
        DateTime endDateTime;
        EventDateTime end;

        InsertTask(String title, String description, Long startingDate) {
            event = new Event()
                    .setSummary(title)
                    .setLocation(" ")
                    .setDescription(description);

            //startDateTime = new DateTime("2017-07-10T09:00:00-07:00");
            startDateTime = new DateTime(startingDate);
            start = new EventDateTime()
                    .setDateTime(startDateTime)
                    .setTimeZone("America/Los_Angeles");
            event.setStart(start);

            endDateTime = new DateTime(startingDate+30000);
            end = new EventDateTime()
                    .setDateTime(endDateTime)
                    .setTimeZone("America/Los_Angeles");
            event.setEnd(end);
        }

        /**
         * Background task to call Google Calendar API.
         * @param params no parameters needed for this task.
         */
        @Override
        protected List<String> doInBackground(Void... params) {
            //String calendarId = "i3jbqatm5hjsmf101mfd0isadk@group.calendar.google.com";
            String calendarId = "info@uzazivillage.com";
            try{
                event = mService.events().insert(calendarId, event).execute();
                return null;
            } catch (Exception e){
                Log.e("Insert","Inserting didn't work: " + e.getMessage());
                return null;
            }
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(List<String> output) {
        }

        @Override
        protected void onCancelled() {
        }
    }

    //function for the Events/Calendar button goes here

    //Function for the Donation button goes here.
    // these functions are not quite finished. We need to find out the correct URL
    // to send them to in order to make a donation.

    /**
     * Sends user to correct site to make a donation
     */
    public void goToPayPal(View view) {
        goToUrl("https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=WQCMX4SDALH3E");
    }

    /**
     * Sends user to contact info page on website
     */
    public void goToUzaziVillage(View view) {
        //goToUrl("http://www.uzazivillage.org/about-us/contact-us/");
        Intent myIntent = new Intent(this, Contact.class);
        startActivity(myIntent);
    }

    /**
     * Assists in sending user to a webpage outside the app
     */
    public void goToUrl(String url) {
        Uri theUrl = Uri.parse(url);
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, theUrl);
        startActivity(launchBrowser);
    }


    public void goOneRight(View view){
        compactCalendarView.showNextMonth();
        Date temp = compactCalendarView.getFirstDayOfCurrentMonth();
        monthDisplay.setText(new SimpleDateFormat("MMMM").format(temp));
    }

    public void goOneLeft(View view){
        compactCalendarView.showPreviousMonth();
        Date temp = compactCalendarView.getFirstDayOfCurrentMonth();
        monthDisplay.setText(new SimpleDateFormat("MMMM").format(temp));
    }
}
