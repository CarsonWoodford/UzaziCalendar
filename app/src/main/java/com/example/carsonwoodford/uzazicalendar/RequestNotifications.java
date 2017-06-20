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
 * Created by CarsonWoodford on 6/12/17.
 */

public class RequestNotifications extends Activity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.request_notifications);
    }

    public void canNotifyPressed(View view){
        Intent intent = new Intent();
        intent.putExtra("canNotify", true);
        setResult(RESULT_OK, intent);
        finish();

    }
    public void cantNotifyPressed(View view){
        Intent intent = new Intent();
        intent.putExtra("canNotify", false);
        setResult(RESULT_OK, intent);
        finish();
    }
}
