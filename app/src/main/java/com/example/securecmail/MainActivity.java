package com.example.securecmail;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import jakarta.mail.MessagingException;


public class MainActivity extends AppCompatActivity {

    //Button createAccountButton;
    TextView createAccountText;
    EditText input_username, input_password;
    String[] userInfo = new String[2];
    boolean first = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createAccountText = findViewById(R.id.to_create_account_button);
        input_username = findViewById(R.id.input_username);
        input_password = findViewById(R.id.input_password);

        File filePath = new File(MainActivity.this.getFilesDir(), "securecmail_data");
        if(!filePath.exists()){
            filePath.mkdir();
        }//end if

        try {
            InputStream inputStream = this.openFileInput("user_login.txt");
            InputStreamReader inputReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputReader);

            String data;
            while(bufferedReader.ready()){
                data = bufferedReader.readLine();
                userInfo = data.split(",");
            }//end while

            if(userInfo[0] != null){
                first = true;
                createAccountText.setVisibility(View.INVISIBLE);
            }//end if

            inputStream.close();
        }//end try
        catch(FileNotFoundException e){
            System.out.printf("Error: File Not Found");
        }//end catch
        catch (IOException e) {
            Toast.makeText(this, "Error: IOException Thrown",Toast.LENGTH_LONG).show();
            System.out.printf("Error: IOException Thrown");
        }//end catch
    }

    /**
     * Sends the user to the Home Activity
     * @param view The button to go to Home
     */
    public void toHome(View view) {
        String username = input_username.getText().toString();
        String password = input_password.getText().toString();
        //only send user to HomeActivity if they have an account
        if(username.equals(userInfo[0]) && password.equals(userInfo[1])){
            Intent intent = new Intent(this,HomeActivity.class);
            startActivity(intent);
        }//end if
        else{
            Toast.makeText(this, "Username or password do not match!",Toast.LENGTH_LONG).show();
        }//end else

    }//end toHome

    /**
     * Sends the user to CreateAccount Activity
     * @param view The button to go to CreateAccount
     */
    public void toCreateAccount(View view) {
        if(first == false) {
            Intent intent = new Intent(this, CreateAccountActivity.class);
            startActivity(intent);
        }//end if
    }//end toCreateAccount


}