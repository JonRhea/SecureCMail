package com.example.securecmail;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import jakarta.mail.MessagingException;


public class MessageActivity extends AppCompatActivity {

    EditText input_subject, input_body;
    Button send_button, attachment_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        input_subject = findViewById(R.id.input_subject);
        input_body = findViewById(R.id.input_body);
        send_button = findViewById(R.id.send_button);
        attachment_button = findViewById(R.id.attachment_button);
        send_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg_subject = input_subject.toString();
                String msg_body = input_body.toString();
                MailHelper mailHelper = new MailHelper();
                String user = "";
                String pass = "";
                String host = "smtp.gmail.com";
                mailHelper.sendMail(host, user, pass);
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