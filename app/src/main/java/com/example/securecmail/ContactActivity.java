package com.example.securecmail;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class ContactActivity extends AppCompatActivity {

    EditText contactUniqueName;
    EditText contactFullName;
    EditText contactEmail1;
    EditText contactEmail2;

    String uniqueName;
    String fullName;
    String emailId1;
    String emailId2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        contactUniqueName = findViewById(R.id.input_contact_name);
        contactFullName = findViewById(R.id.input_full_name);
        contactEmail1 = findViewById(R.id.input_email1);
        contactEmail2 = findViewById(R.id.input_email2);

        //temporarily deleting contact_info.txt every run for testing
        File filePath = new File(ContactActivity.this.getFilesDir(), "securecmail_data");
        if(filePath.exists()){
            deleteFile("contact_info.txt");
        }//end if

    }

    /**
     * Save a contact's info
     * NOT IMPLEMENTED YET
     * @param view The button to save a contact
     */
    public void saveContact(View view) {

        if(checkContact()){
            Contact newContact = new Contact(uniqueName,fullName,emailId1,emailId2);
            try{
                File filePath = new File(ContactActivity.this.getFilesDir(), "securecmail_data");
                if(!filePath.exists()){
                    filePath.mkdir();
                }//end if

                File file = new File(this.getFilesDir(), "contact_info.txt");
                FileOutputStream stream = new FileOutputStream(file, true);
                OutputStreamWriter outputStream = new OutputStreamWriter(stream);
                outputStream.write(newContact.getUniqueName()+ ',');
                outputStream.write(newContact.getFullName() + ',');
                outputStream.write(newContact.getFirstEmailID()+ ',');
                outputStream.write(newContact.getSecondEmailID() + "\n");
                outputStream.close();

                System.out.println("New Contact was saved successfully!");
                readContactTest();
            }
            catch(FileNotFoundException e){
                //add toast later
                e.printStackTrace();
                System.out.printf("Error: File Not Found");
            }//end catch
            catch (IOException e) {
                //add toast later
                e.printStackTrace();
                System.out.printf("Error: IOException Thrown");
            }
        }//end if
        else{
            Toast.makeText(this, "Error: Contact information was missing.",Toast.LENGTH_LONG).show();
        }//end else
    }//end saveContact


    /**
     * Checks to see if all fields for a contact were filled
     * @return False if any field is blank; true is not are blank
     */
    public boolean checkContact(){

        uniqueName = contactUniqueName.getText().toString();
        fullName = contactFullName.getText().toString();
        emailId1 = contactEmail1.getText().toString();
        emailId2 = contactEmail2.getText().toString();

        //check to see if all contact fields were entered
        if(uniqueName.equals("") || fullName.equals("") || emailId1.equals("") || emailId2.equals("")){
            return false;
        }//end if

        return true;
    }//end checkContact

    public void readContactTest(){

        String contactString = "";
        File filePath = new File(ContactActivity.this.getFilesDir(), "securecmail_data");
        if(!filePath.exists()){
            filePath.mkdir();
        }//end if

        try {
            InputStream inputStream = this.openFileInput("contact_info.txt");
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
     * @param view The back button
     */
    public void Back(View view) {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }//end Back
}