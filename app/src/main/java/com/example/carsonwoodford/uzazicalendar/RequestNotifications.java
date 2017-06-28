package com.example.carsonwoodford.uzazicalendar;

import android.app.Activity;
import android.app.usage.UsageEvents;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

/**
 * RequestNotifications presents the user with the request_notifications.xml
 */

public class RequestNotifications extends Activity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.request_notifications);
    }

    /**
     * If user selects no notifications this function is called to set
     * to can notify.
     * @param view scope of the project when the button was pressed
     */
    public void canNotifyPressed(View view){
        Intent intent = new Intent();
        intent.putExtra("canNotify", true);
        setResult(RESULT_OK, intent);
        finish();

    }

    /**
     * If user selects no notifications this function is called to set
     * to cant notify.
     * @param view scope of the project when the button was pressed
     */
    public void cantNotifyPressed(View view){
        Intent intent = new Intent();
        intent.putExtra("canNotify", false);
        setResult(RESULT_OK, intent);
        finish();
    }
}
