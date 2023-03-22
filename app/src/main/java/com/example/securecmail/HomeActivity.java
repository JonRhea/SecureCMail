package com.example.securecmail;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    /**
     * Sends the user to the ConfigureEmail Activity
     * @param view The button to go to Configure
     */
    public void toConfigure(View view) {
        Intent intent = new Intent(this,ConfigureEmailActivity.class);
        startActivity(intent);

    }//end toConfigure

    /**
     * Sends the user to the ContactActivity
     * @param view The button to go to Contacts
     */
    public void toContacts(View view) {
        Intent intent = new Intent(this,ContactActivity.class);
        startActivity(intent);
    }//end toContacts

    /**
     * Sends the user to the ContactActivity
     * @param view The button to go to Message
     */
    public void toMessage(View view) {
        Intent intent = new Intent(this,MessageActivity.class);
        startActivity(intent);
    }//end toMessage
}
