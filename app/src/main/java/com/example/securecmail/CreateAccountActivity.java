package com.example.securecmail;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class CreateAccountActivity extends AppCompatActivity {

    EditText input_username, input_password, confirm_username, confirm_password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        input_username = findViewById(R.id.input_username_1);
        confirm_username = findViewById(R.id.input_username_2);
        input_password = findViewById(R.id.input_password_1);
        confirm_password = findViewById(R.id.input_password_2);
    }

    public void createAccount(View view) {
        String username = input_username.getText().toString();
        String username_confirm = confirm_username.getText().toString();
        String password = input_password.getText().toString();
        String password_confirm = confirm_password.getText().toString();


        if(!username.equals(username_confirm)){
            //if usernames do not match
        }//end if
        else if(!password.equals(password_confirm)){
            //if passwords do not match
        }//end if
        else if(username.equals("") || username_confirm.equals("") || password.equals("") || password_confirm.equals("")){
            //case if any fields are blank
        }//end if
        else{
            //if everything matches, save info


            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
        }//end else

    }//end createAccount
}