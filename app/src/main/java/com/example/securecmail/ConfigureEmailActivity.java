package com.example.securecmail;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class ConfigureEmailActivity extends AppCompatActivity {

    EditText input_email1, input_email2, input_password1, input_password2;
    ImageView backButton;
    boolean first = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configure_email);

        input_email1 = findViewById(R.id.input_your_email1);
        input_email2 = findViewById(R.id.input_your_email2);
        input_password1 = findViewById(R.id.input_your_password1);
        input_password2 = findViewById(R.id.input_your_password2);
        backButton = findViewById(R.id.back_button_test);

        try {
            File filePath = new File(ConfigureEmailActivity.this.getFilesDir(), "securecmail_data");
            if (!filePath.exists()) {
                filePath.mkdir();
            }//end if

            InputStream inputStream = this.openFileInput("user_email.txt");
            InputStreamReader inputReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputReader);
        }//end try
        catch (FileNotFoundException e) {
            //if this exception is thrown, assume the user just created an account
            backButton.setVisibility(View.INVISIBLE);
            first = true;
        }//end catch
    }


    public void saveUserInfo(View view) {
        String email1 = input_email1.getText().toString();
        String email2 = input_email2.getText().toString();
        String password1 = input_password1.getText().toString();
        String password2 = input_password2.getText().toString();

        try {
            File filePath = new File(ConfigureEmailActivity.this.getFilesDir(), "securecmail_data");
            if (!filePath.exists()) {
                filePath.mkdir();
            }//end if

            File file = new File(this.getFilesDir(), "user_email.txt");
            FileOutputStream stream = new FileOutputStream(file, false);
            OutputStreamWriter outputStream = new OutputStreamWriter(stream);

            outputStream.write(email1 + "," + password1 + "," + email2 + "," + password2);
            outputStream.close();
            readUserEmailTest();

            //if the user has just made an account, send them back to login activity
            if(first == true){
                first = false;
                Intent intent = new Intent(this,MainActivity.class);
                startActivity(intent);
            }//end if

        }//end try
        catch (IOException e) {
            System.out.println("Error: IOException");
        }//end catch

    }//end saveUserInfo

    public void readUserEmailTest(){

        String contactString = "";
        File filePath = new File(ConfigureEmailActivity.this.getFilesDir(), "securecmail_data");
        if(!filePath.exists()){
            filePath.mkdir();
        }//end if

        try {
            InputStream inputStream = this.openFileInput("user_email.txt");
            InputStreamReader inputReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputReader);

            String data;
            while(bufferedReader.ready()){
                data = bufferedReader.readLine();
                System.out.println(data);
            }//end while
            inputStream.close();
        }//end try
        catch(FileNotFoundException e){
            //add toast later
            System.out.printf("Error: File Not Found");
        }//end catch
        catch (IOException e) {
            //add toast later
            System.out.printf("Error: IOException Thrown");
        }//end catch
    }//end readContactTest



        /**
     * Sends the user back to MainActivity
     * @param view The button to go back
     */
    public void Back(View view) {
        if(first == false){
            Intent intent = new Intent(this,HomeActivity.class);
            startActivity(intent);
        }//end if


    }//end Back


}