package com.example.securecmail;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;



public class MainActivity extends AppCompatActivity {

    Button createAccountButton;
    EditText input_username, input_password;
    String[] userInfo = new String[2];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createAccountButton = findViewById(R.id.to_create_account_button);
        input_username = findViewById(R.id.input_username);
        input_password = findViewById(R.id.input_password);

        //delete user login file for testing
        //app has to run once with this uncommented, then again uncommented so that
        //the file does not delete after creating an account for testing
        //deleting file manually in Studio works as well

        /*
        File filePathDelete = new File(MainActivity.this.getFilesDir(), "securecmail_data");
        if(filePathDelete.exists()){
            deleteFile("user_login.txt");
        }//end if
        */

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

                System.out.println(userInfo[0]);
                System.out.println(userInfo[1]);
            }//end while

            if(userInfo[0] != null){
                createAccountButton.setVisibility(View.INVISIBLE);
            }//end if

            inputStream.close();
        }//end try
        catch(FileNotFoundException e){
            Toast.makeText(this, "Error: File Not Found",Toast.LENGTH_LONG).show();
            System.out.printf("Error: File Not Found");
        }//end catch
        catch (IOException e) {
            Toast.makeText(this, "Error: IOException Thrown",Toast.LENGTH_LONG).show();
            System.out.printf("Error: IOException Thrown");
        }//end catch
    }

    public void toHome(View view) {
        String username = input_username.getText().toString();
        String password = input_password.getText().toString();

        if(username.equals(userInfo[0]) && password.equals(userInfo[1])){
            Intent intent = new Intent(this,HomeActivity.class);
            startActivity(intent);
        }//end if
        else{
            Toast.makeText(this, "Username or password do not match!",Toast.LENGTH_LONG).show();
        }//end else

    }//end toHome

    public void toCreateAccount(View view) {
        Intent intent = new Intent(this,CreateAccountActivity.class);
        startActivity(intent);
    }//end toCreateAccount
}