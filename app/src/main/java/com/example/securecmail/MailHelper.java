package com.example.securecmail;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import jakarta.mail.Authenticator;
import jakarta.mail.Folder;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.Part;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Store;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

public class MailHelper {
    public void receiveMail(String host1, String user1, String pass1, String host2, String user2, String pass2) throws MessagingException {

        new Thread(){
            public void run() {

                String host1resolved = resolveHostToIPV4(host1);
                String host2resolved = resolveHostToIPV4(host2);

                Properties props1 = System.getProperties();
                //props1.put("mail.debug", "true");
                props1.put("mail.imap.host", host1resolved);
                props1.put("mail.imap.ssl.enable", "true");
                props1.put("mail.imap.port", "993");
                props1.put("mail.imap.auth", "true");
                props1.put("mail.imap.ssl.checkserveridentity", "false");
                props1.put("mail.imap.ssl.trust", "*");

                Properties props2 = System.getProperties();
                //props1.put("mail.debug", "true");
                props2.put("mail.imap.host", host2resolved);
                props2.put("mail.imap.ssl.enable", "true");
                props2.put("mail.imap.port", "993");
                props2.put("mail.imap.auth", "true");
                props2.put("mail.imap.ssl.checkserveridentity", "false");
                props2.put("mail.imap.ssl.trust", "*");

                Authenticator auth1 = new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(user1, pass1);
                    }
                };

                Authenticator auth2 = new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(user2, pass2);
                    }
                };

                try {
                    Session imapSession1 = Session.getInstance(props1, auth1);
                    Session imapSession2 = Session.getInstance(props2, auth2);
                    Store store1 = imapSession1.getStore("imap");
                    Store store2 = imapSession2.getStore("imap");
                    store1.connect(host1resolved, user1, pass1);
                    store2.connect(host2resolved, user2, pass2);
                    Folder inbox1 = store1.getFolder("Inbox");
                    Folder inbox2 = store2.getFolder("Inbox");
                    inbox1.open(Folder.READ_WRITE);
                    inbox2.open(Folder.READ_WRITE);
                    Log.d("MailHelper Receive", "Inside receive method...");
                    Message[] messages1 = inbox1.getMessages();
                    Message[] messages2 = inbox2.getMessages();
                    for (int i = 1; i < 11; i++) {
                        Log.d("Email Number " + i, "Inbox 1: " + messages1[messages1.length-i].getSubject() + "Inbox 2: " +messages2[messages2.length-i].getSubject());//outputs "i" most recent emails from inboxes
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
            }//thread bracket
        }.start();
    }

    class receiveMessages extends AsyncTask<String,Void,Message[]>{
        String host1, user1, pass1, host2, user2, pass2;

        receiveMessages(String host1, String user1, String pass1, String host2, String user2, String pass2){
            this.host1 = host1;
            this.user1 = user1;
            this.pass1 = pass1;
            this.host2 = host2;
            this.user2 = user2;
            this.pass2 = pass2;
        }

        @Override
        protected Message[] doInBackground(String... strings) {
            String host1resolved = resolveHostToIPV4(host1);
            String host2resolved = resolveHostToIPV4(host2);

            Properties props1 = System.getProperties();
            //props1.put("mail.debug", "true");
            props1.put("mail.imap.host", host1resolved);
            props1.put("mail.imap.ssl.enable", "true");
            props1.put("mail.imap.port", "993");
            props1.put("mail.imap.auth", "true");
            props1.put("mail.imap.ssl.checkserveridentity", "false");
            props1.put("mail.imap.ssl.trust", "*");

            Properties props2 = System.getProperties();
            //props1.put("mail.debug", "true");
            props2.put("mail.imap.host", host2resolved);
            props2.put("mail.imap.ssl.enable", "true");
            props2.put("mail.imap.port", "993");
            props2.put("mail.imap.auth", "true");
            props2.put("mail.imap.ssl.checkserveridentity", "false");
            props2.put("mail.imap.ssl.trust", "*");

            Authenticator auth1 = new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(user1, pass1);
                }
            };

            Authenticator auth2 = new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(user2, pass2);
                }
            };

            try {
                Session imapSession1 = Session.getInstance(props1, auth1);
                Session imapSession2 = Session.getInstance(props2, auth2);
                Store store1 = imapSession1.getStore("imap");
                Store store2 = imapSession2.getStore("imap");
                store1.connect(host1resolved, user1, pass1);
                store2.connect(host2resolved, user2, pass2);
                Folder inbox1 = store1.getFolder("Inbox");
                Folder inbox2 = store2.getFolder("Inbox");
                inbox1.open(Folder.READ_WRITE);
                inbox2.open(Folder.READ_WRITE);
                Log.d("MailHelper Receive", "Inside receive method...");
                Message[] messages1 = inbox1.getMessages();
                Message[] messages2 = inbox2.getMessages();

                ArrayList<Message> unpairedMessage = new ArrayList<>();
                List<Message> allMessages = new ArrayList<>();
                /*
                Runnable runnable1 = new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < messages1.length; i++) {//Find messages from inbox 1 and add to unpaired messages list
                            String subject = null;
                            try {
                                subject = messages1[i].getSubject();
                            } catch (MessagingException e) {
                                throw new RuntimeException(e);
                            }
                            String[] subjectSplit = subject.split(" ");
                            if (subjectSplit[0].equals("SecureCMail:")) {
                                unpairedMessage.add(messages1[i]);
                            }
                        }
                    }
                };
                Thread thread1 = new Thread(runnable1);
                thread1.start();

                Runnable runnable2 = new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < messages2.length; i++) {//Find messages from inbox 1 and add to unpaired messages list
                            String subject = null;
                            try {
                                subject = messages2[i].getSubject();
                            } catch (MessagingException e) {
                                throw new RuntimeException(e);
                            }
                            if(subject==null){

                            }else {
                                String[] subjectSplit = subject.split(" ");
                                if (subjectSplit[0].equals("SecureCMail:")) {
                                    unpairedMessage.add(messages2[i]);
                                }
                            }
                        }
                    }
                };

                Thread thread2 = new Thread(runnable2);
                thread2.start();
                //Iterate through each unpaired message,
                for(int j=0; j<unpairedMessage.toArray().length;j++) {
                    Message message = unpairedMessage.get(j);
                    MimeMessage mimeMessage = new MimeMessage((MimeMessage) message);
                    String subject = message.getSubject();
                    String[] subjectSplit = subject.split(" ");
                    Iterator<Message> iter = unpairedMessage.iterator();
                    int index = 0;
                    while(iter.hasNext()) {
                        Message singleMessage = unpairedMessage.get(index);
                        String singleSubject = singleMessage.getSubject();
                        String[] singleSplit = singleSubject.split(" ");
                        if (singleSplit[2].equals(subjectSplit[2])) {
                            unpairedMessage.remove(index);
                            //found = true;
                            String[] subjects = new String[2];
                            String[] bodies = new String[2];

                            if (subjectSplit[1].equals("1")) {
                                subjects[0] = message.getSubject();
                                subjects[1] = singleMessage.getSubject();

                                bodies[0] = mimeMessage.getContent().toString().substring(0, mimeMessage.getContent().toString().length() - 2);
                                bodies[1] = singleMessage.getContent().toString().substring(0, singleMessage.getContent().toString().length() - 2);
                            }//end if
                            else {
                                subjects[1] = message.getSubject();
                                subjects[0] = singleMessage.getSubject();

                                bodies[1] = mimeMessage.getContent().toString().substring(0, mimeMessage.getContent().toString().length() - 2);
                                bodies[0] = singleMessage.getContent().toString().substring(0, singleMessage.getContent().toString().length() - 2);
                            }//end else
                            String decodeSubject = SecretHelper.decode(subjects);
                            String decodeBody = SecretHelper.decode(bodies);

                            message.addFrom(singleMessage.getFrom());
                            message.setSubject(decodeSubject);
                            ((MimeMessage) message).setText(decodeBody);
                            allMessages.add(message);
                        }//end if
                        index++;
                    }
                }*/
                Collections.addAll(allMessages, messages1);
                Collections.addAll(allMessages, messages2);
                return allMessages.toArray(new Message[0]);
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
        }

    }

    public static void sendMail(String hostInput, String user, String pass, String msg_subject, String msg_body, String to){
        new Thread(){
            public void run() {

                String host = resolveHostToIPV4(hostInput);

                boolean sessionDebug = true;
                //System.out.println(user);
                String from = user; //user's email ids will go here
                String subject = msg_subject;
                String messageText = msg_body;

                Properties props = new Properties();
                //props.put("mail.smtp.ehlo", "false");
                props.put("mail.debug", "true");
                props.put("mail.smtp.host", host);
                props.put("mail.smtp.auth", "true");
                if(hostInput.equals("smtp.gmail.com")){
                    props.put("mail.smtp.port", "465");
                    props.put("mail.smtp.ssl.enable", "true");
                    props.put("mail.smtp.ssl.trust", "*");
                    props.put("mail.smtp.ssl.checkserveridentity", "false");
                }else{
                    props.put("mail.smtp.port", "587");
                    props.put("mail.smtp.starttls.enable", "true");
                    props.put("mail.smtp.ssl.trust", "*");
                    props.put("mail.smtp.ssl.checkserveridentity", "false");
                }
                //maybe figure out how to fix this

                Authenticator auth = new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(user, pass);
                    }
                };

                try {
                    //System.out.println("Host is: "+host+", "+"User is: "+user);
                    Session mailSession = Session.getInstance(props, auth);
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

                    //System.out.println("Message sent successfully!");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }.start();
    }

    public static String resolveHostToIPV4(String host){
        InetAddress hostINets[] = null;
        String hostIPV4 = null;
        try {
            hostINets = InetAddress.getAllByName(host);
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

    private boolean textIsHtml = false;

    /**
     * Return the primary text content of the message.
     * Obtained from official javamail documentation
     */
    public String getText(Part p) throws
            MessagingException, IOException {
        if (p.isMimeType("text/*")) {
            String s = (String)p.getContent();
            textIsHtml = p.isMimeType("text/html");
            return s;
        }

        if (p.isMimeType("multipart/alternative")) {
            // prefer html text over plain text
            Multipart mp = (Multipart)p.getContent();
            String text = null;
            for (int i = 0; i < mp.getCount(); i++) {
                Part bp = mp.getBodyPart(i);
                if (bp.isMimeType("text/plain")) {
                    if (text == null)
                        text = getText(bp);
                    continue;
                } else if (bp.isMimeType("text/html")) {
                    String s = getText(bp);
                    if (s != null)
                        return s;
                } else {
                    return getText(bp);
                }
            }
            return text;
        } else if (p.isMimeType("multipart/*")) {
            Multipart mp = (Multipart)p.getContent();
            for (int i = 0; i < mp.getCount(); i++) {
                String s = getText(mp.getBodyPart(i));
                if (s != null)
                    return s;
            }
        }

        return null;
    }

}
