package com.example.securecmail;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MessageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
    }

    /**
     * Sends the user back to MainActivity
     * @param view The button to go back
     */
    public void Back(View view) {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);

    }//end Back
}