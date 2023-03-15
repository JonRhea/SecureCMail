package com.example.securecmail;

import android.util.Log;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Properties;

import jakarta.mail.Authenticator;
import jakarta.mail.Folder;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Store;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

public class MailHelper {

    public void receiveMail(String hostInput, String user, String pass) throws MessagingException {
        new Thread(){
            public void run() {

                String host = resolveHostToIPV4(hostInput);

                Properties props = System.getProperties();
                props.put("mail.debug", "true");
                props.put("mail.imap.host", host);
                props.put("mail.imap.ssl.enable", "true");
                props.put("mail.imap.port", "993");
                props.put("mail.imap.auth", "true");
                props.put("mail.imap.ssl.checkserveridentity", "false");
                props.put("mail.imap.ssl.trust", "*");

                Authenticator auth = new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(user, pass);
                    }
                };

                try {
                    Session imapSession = Session.getInstance(props, auth);
                    imapSession.setDebug(true);
                    Store store = imapSession.getStore("imap");
                    store.connect(host, user, pass);
                    Folder inbox = store.getFolder("Inbox");
                    inbox.open(Folder.READ_WRITE);
                    Message[] messages = inbox.getMessages();
                    for (int i = 1; i < 11; i++) {
                        Log.d("Email Number " + i, messages[messages.length-i].getSubject());//outputs "i" most recent emails from inbox
                        //get all messages with SecureCMail Header -- SecureCMail and maybe a timestamp in subject, some confirmation in body before share 1?
                        //keep user info stored persistently, keep user email accounts info in encrypted shared prefs?
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
            }//thread bracket
        }.start();
    }

    public void sendMail(String hostInput, String user, String pass, String msg_subject, String msg_body){
        new Thread(){
            public void run() {

                String host = resolveHostToIPV4(hostInput);

                boolean sessionDebug = true;
                String to = "";
                String from = "";
                String subject = msg_subject;
                String messageText = msg_body;

                Properties props = System.getProperties();
                //props.put("mail.smtp.ehlo", "false");
                props.put("mail.debug", "true");
                props.put("mail.smtp.host", host);
                props.put("mail.smtp.ssl.enable", "true");
                props.put("mail.smtp.port", "465");
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.ssl.checkserveridentity", "false"); //maybe figure out how to fix this
                props.put("mail.smtp.ssl.trust", "*");

                Authenticator auth = new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(user, pass);
                    }
                };

                try {
                    Session mailSession = Session.getDefaultInstance(props, auth);
                    mailSession.setDebug(sessionDebug);
                    Message msg = new MimeMessage(mailSession);
                    msg.setFrom(new InternetAddress(from));
                    InternetAddress[] address = {new InternetAddress(to)};
                    msg.setRecipients(Message.RecipientType.TO, address);
                    msg.setSubject(subject);
                    msg.setSentDate(new Date());
                    msg.setText(messageText);
                    Transport transport = mailSession.getTransport("smtp");
                    transport.connect(host, user, pass);
                    transport.sendMessage(msg, msg.getAllRecipients());
                    transport.close();
                    System.out.println("Message sent successfully!");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }.start();
    }

    public String resolveHostToIPV4(String host){
        InetAddress hostINets[] = null;
        String hostIPV4 = null;
        try {
            hostINets = InetAddress.getAllByName("smtp.gmail.com");
            System.out.println("Host inet is: "+hostINets);
            for(int i =0;i<hostINets.length;i++){
                if(hostINets[i] instanceof Inet4Address){
                    hostIPV4 = hostINets[i].getHostAddress();
                }
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return hostIPV4;
    }


}
