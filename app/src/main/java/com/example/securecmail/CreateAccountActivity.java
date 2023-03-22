package com.example.securecmail;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;


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

    /**
     * Saves the user's username and password for login
     * @param view The create account button to save the info
     */
    public void createAccount(View view) {
        String username = input_username.getText().toString();
        String username_confirm = confirm_username.getText().toString();
        String password = input_password.getText().toString();
        String password_confirm = confirm_password.getText().toString();




        if(!username.equals(username_confirm)){
            Toast.makeText(this, "Error: Usernames do not match.",Toast.LENGTH_LONG).show();
        }//end if
        else if(!password.equals(password_confirm)){
            Toast.makeText(this, "Error: Passwords do not match.",Toast.LENGTH_LONG).show();
        }//end if
        else if(username.equals("") || username_confirm.equals("") || password.equals("") || password_confirm.equals("")){
            Toast.makeText(this, "Error: One of the fields is blank.",Toast.LENGTH_LONG).show();
        }//end if
        else{
            //if everything matches, save info
            try{
                File filePath = new File(CreateAccountActivity.this.getFilesDir(), "securecmail_data");
                if(!filePath.exists()){
                    filePath.mkdir();
                }//end if


                File file = new File(this.getFilesDir(), "user_login.txt");
                FileOutputStream stream = new FileOutputStream(file, false);
                OutputStreamWriter outputStream = new OutputStreamWriter(stream);
                outputStream.write(username + ',' + password);

                outputStream.close();

                System.out.println("Username and password saved successfully.");
            }//end try
            catch(FileNotFoundException e){
                Toast.makeText(this, "Error: File Not Found",Toast.LENGTH_LONG).show();
                e.printStackTrace();
                System.out.printf("Error: File Not Found");
            }//end catch
            catch (IOException e) {
                Toast.makeText(this, "Error: IOException Thrown",Toast.LENGTH_LONG).show();
                e.printStackTrace();
                System.out.printf("Error: IOException Thrown");
            }//end catch

            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
        }//end else

    }//end createAccount
}