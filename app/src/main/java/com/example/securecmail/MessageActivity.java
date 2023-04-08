package com.example.securecmail;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import jakarta.mail.MessagingException;


public class MessageActivity extends AppCompatActivity {

    EditText input_subject, input_body;
    Button send_button;

    ImageView attachment_button;
    Spinner to_spinner, cc_spinner;
    String[] userInfo = new String[4];

    ArrayList<Contact> contactList = new ArrayList<Contact>();
    ArrayList<String> contactNames = new ArrayList<String>();

    Contact emptyContact = new Contact(null,null,null,null);

    Contact to_contact;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        input_subject = findViewById(R.id.input_subject);
        input_body = findViewById(R.id.input_body);
        send_button = findViewById(R.id.send_button);
        attachment_button = findViewById(R.id.attachment_button);
        to_spinner = findViewById(R.id.contact_spinner);
        cc_spinner = findViewById(R.id.cc_spinner);

        try{
            File filePath = new File(MessageActivity.this.getFilesDir(), "securecmail_data");
            if (!filePath.exists()) {
                filePath.mkdir();
            }//end if

            InputStream inputStream = this.openFileInput("user_email.txt");
            InputStreamReader inputReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputReader);

            String data;

            data = bufferedReader.readLine();
            userInfo = data.split(",");
        }//end try
        catch (IOException e) {
            System.out.println("Error: IOException MessageActivity");
        }//end catch

        contactList.add(emptyContact);
        contactNames.add("None");
        loadContacts();

        ArrayAdapter contactAdapter = new ArrayAdapter(this, R.layout.spinner, contactNames);

        to_spinner.setAdapter(contactAdapter);
        to_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){


            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Object contactUnique = parent.getSelectedItem().toString();

                if (contactUnique == "None") {
                    System.out.println("No contact selected\n");
                }//end if
                else {
                    for (int i = 0; i < contactList.size(); i++) {
                        if (contactList.get(i).getUniqueName() == contactUnique) {
                            to_contact = contactList.get(i);
                        }//end if
                    }//end for

                //test prints
                    System.out.println(to_contact.getUniqueName());
                    System.out.println(to_contact.getFullName());
                    System.out.println(to_contact.getFirstEmailID());
                    System.out.println(to_contact.getSecondEmailID());
                    System.out.println("\n");
                }//end else
        }//end onItemSelected

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }//end onNothingSelected
        });

        cc_spinner.setAdapter(contactAdapter);
        cc_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id){

                //((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);

                Object contactUnique = parent.getSelectedItem().toString();

                if (contactUnique == "None") {
                    System.out.println("No contact selected\n");
                }//end if
                else {
                    for (int i = 0; i < contactList.size(); i++) {
                        if (contactList.get(i).getUniqueName() == contactUnique) {
                            to_contact = contactList.get(i);
                        }//end if
                    }//end for

                    //test prints
                    System.out.println(to_contact.getUniqueName());
                    System.out.println(to_contact.getFullName());
                    System.out.println(to_contact.getFirstEmailID());
                    System.out.println(to_contact.getSecondEmailID());
                    System.out.println("\n");
                }//end else

            }//end onItemSelected

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }//end onNothingSelected
        });


        send_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg_subject = input_subject.getText().toString();
                String msg_body = input_body.getText().toString();
                MailHelper mailHelper = new MailHelper();
                String user = userInfo[0];
                String pass = userInfo[1];
                String host = "smtp.gmail.com";
                if(to_contact != null) {


                    String[] encodedSubject = SecretHelper.encrypt(msg_subject);
                    String[] encodedBody = SecretHelper.encrypt(msg_body);

                    //temp: testing secret sharing methods
                    String decodedSubject = SecretHelper.decode(encodedSubject);
                    String decodedBody = SecretHelper.decode(encodedBody);

                    mailHelper.sendMail(host, user, pass, encodedSubject[0], encodedBody[0], to_contact);
                    mailHelper.sendMail(host, user, pass, encodedSubject[1], encodedBody[1], to_contact);
                    Toast.makeText(getApplicationContext(), "Message Sent!", Toast.LENGTH_LONG).show();
                }//end if
                else {
                    System.out.println("Error, no recipient selected.\n");
                    Toast.makeText(getApplicationContext(), "Error, no recipient selected.", Toast.LENGTH_LONG).show();
                }//end else
            }
        });

        attachment_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MailHelper mailHelper = new MailHelper();
                String user = "";
                String pass = "";
                String host = "imap.gmail.com";
                try {
                    mailHelper.receiveMail(host, user, pass);
                } catch (MessagingException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }//end onCreate

    /**
     * Load contacts from file ot be used in Spinners
     */
    public void loadContacts(){

        File filePath = new File(MessageActivity.this.getFilesDir(), "securecmail_data");
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
                String[] contactInfo = data.split(",");
                Contact contact = new Contact(contactInfo[0], contactInfo[1], contactInfo[2], contactInfo[3]);

                contactList.add(contact);
                contactNames.add(contactInfo[0]);
                //test prints
                System.out.println(contact.getUniqueName());
                System.out.println(contact.getFullName());
                System.out.println(contact.getFirstEmailID());
                System.out.println(contact.getSecondEmailID());
                System.out.println("\n");
            }//end while
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

    }//end loadContacts

    /**
     * Sends the user back to MainActivity
     * @param view The button to go back
     */
    public void Back(View view) {
        Intent intent = new Intent(this,HomeActivity.class);
        startActivity(intent);

    }//end Back

}