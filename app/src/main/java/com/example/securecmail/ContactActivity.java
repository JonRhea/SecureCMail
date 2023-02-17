package com.example.securecmail;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class ContactActivity extends AppCompatActivity {

    EditText contactUniqueName;
    EditText contactFullName;
    EditText contactEmail1;
    EditText contactEmail2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        contactUniqueName = findViewById(R.id.input_contact_name);
        contactFullName = findViewById(R.id.input_full_name);
        contactEmail1 = findViewById(R.id.input_email1);
        contactEmail2 = findViewById(R.id.input_email2);

    }

    /**
     * Save a contact's info
     * NOT IMPLEMENTED YET
     * @param view The button to save a contact
     */
    public void saveContact(View view) {

    }//end saveContact

    /**
     * Sends the user back to MainActivity
     * @param view The back button
     */
    public void Back(View view) {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }//end Back
}