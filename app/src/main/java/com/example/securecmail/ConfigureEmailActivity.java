package com.example.securecmail;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ConfigureEmailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configure_email);
    }

    /**
     * Sends the user back to MainActivity
     * @param view The button to go back
     */
    public void Back(View view) {
        Intent intent = new Intent(this,HomeActivity.class);
        startActivity(intent);

    }//end Back
}