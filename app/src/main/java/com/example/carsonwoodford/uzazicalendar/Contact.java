package com.example.carsonwoodford.uzazicalendar;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Contact extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact);
    }

    public void goToFacebook(View view) {
        goToUrl("https://www.facebook.com/Uzazi-Village-239146869511450/");
    }

    public void goToCom(View view) {
        goToUrl("http://www.uzazivillage.com/");
    }

    public void goToOrg(View view) {
        goToUrl("http://www.uzazivillage.org/");
    }

    public void goToUrl(String url) {
        Uri theUrl = Uri.parse(url);
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, theUrl);
        startActivity(launchBrowser);
    }
}
