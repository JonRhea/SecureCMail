package com.example.securecmail;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder>{



    private Message[] messages;
    private Context c;
    private OnRowListener onRowListener;
    MailHelper mailHelper = new MailHelper();

    Message prevMessage;
    MimeMessage prevMime;
    InternetAddress prevSender;

    public RecyclerAdapter(Context c, String[] userInfo,OnRowListener onRowListener) {
        this.c = c;

        try {
            MailHelper.receiveMessages receive = mailHelper.new receiveMessages("imap.gmail.com", userInfo[0], userInfo[1]);
            messages = receive.execute().get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        this.onRowListener = onRowListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(c);
        View v = inflater.inflate(R.layout.inbox_row, parent, false);
        return new MyViewHolder(v, onRowListener);
    }

    /**
     * This method binds data to viewholder programmatically
     * @param holder The ViewHolder which should be updated to represent the contents of the
     *        item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final String[] holderSender = new String[1];
        final String[] holderSubject = new String[1];
        final String[] holderBody = new String[1];
        if(messages.length-holder.getLayoutPosition()-1<messages.length) {
            new Thread() {
                public void run() {
                    try {
                        Message message = messages[messages.length-holder.getLayoutPosition()-1];
                        MimeMessage mimeMessage = new MimeMessage((MimeMessage) message);
                        InternetAddress senderAddress = (InternetAddress) message.getFrom()[0];
                        holderSender[0] = senderAddress.getPersonal();
                        holderSubject[0] = message.getSubject();
                        holderBody[0] = mimeMessage.getContent().toString();

                        String subject = holderSubject[0];
                        String[] subjectSplit = subject.split(" ");
                        if(subjectSplit[0].equals("SecureCMail:")){
                            if(prevMessage == null) {
                                prevSender = senderAddress;
                                prevMessage = message;
                                prevMime = mimeMessage;
                            }//end if
                            else{
                                String[] subjects = new String[2];
                                String[] bodies = new String[2];
                                if(subjectSplit[1].equals("1")){
                                    subjects[0] = message.getSubject();
                                    subjects[1] = prevMessage.getSubject();

                                    bodies[0] = mimeMessage.getContent().toString().substring(0, mimeMessage.getContent().toString().length() - 2);
                                    bodies[1] = prevMime.getContent().toString().substring(0, prevMime.getContent().toString().length() - 2);


                                }//end if
                                else{
                                    subjects[1] = message.getSubject();
                                    subjects[0] = prevMessage.getSubject();

                                    bodies[1] = mimeMessage.getContent().toString().substring(0, mimeMessage.getContent().toString().length() - 2);
                                    bodies[0] = prevMime.getContent().toString().substring(0, prevMime.getContent().toString().length() - 2);
                                }//end else
                                String decodeSubject = SecretHelper.decode(subjects);
                                String decodeBody = SecretHelper.decode(bodies);
                                holderSubject[0] = decodeSubject;
                                holderBody[0] = decodeBody;
                            }//end else
                        }//end if

                    } catch (MessagingException | IOException e) {
                        throw new RuntimeException(e);
                    }
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            holder.sender.setText(holderSender[0]);
                            holder.subject.setText(holderSubject[0]);
                            Log.d("Bind view holder", "Output for subject: " + holderSubject[0]);
                            holder.body.setText(holderBody[0]);
                        }
                    });
                }
            }.start();
        }
    }

    /**
     * This method creates a viewholder to be populated, using the inbox_row.xml layout file
     */
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView subject, sender, body;
        OnRowListener onRowListener;
        public MyViewHolder(@NonNull View itemView, OnRowListener onRowListener) {
            super(itemView);
            subject = itemView.findViewById(R.id.subject);
            sender = itemView.findViewById(R.id.sender);
            body = itemView.findViewById(R.id.body);
            this.onRowListener = onRowListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onRowListener.onRowClick(getAdapterPosition());
        }
    }

    public interface OnRowListener{
        void onRowClick(int position);
    }

    @Override
    public int getItemCount() {
        return messages.length;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
