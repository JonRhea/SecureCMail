package com.example.securecmail;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder>{



    private Message[] messages;
    private Context c;
    private OnRowListener onRowListener;
    private onItemClick onItemListener;
    MailHelper mailHelper = new MailHelper();
    boolean edited = false;

    ArrayList<Message> unpairedMessage = new ArrayList<>();

    public RecyclerAdapter(Context c, String[] userInfo, OnRowListener onRowListener, onItemClick onItemClick) {
        this.c = c;

        try {
            MailHelper.receiveMessages receive = mailHelper.new receiveMessages("imap.gmail.com", userInfo[0], userInfo[1], "outlook.office365.com", userInfo[2], userInfo[3] );
            messages = receive.execute().get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        this.onRowListener = onRowListener;
        this.onItemListener = onItemClick;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(c);
        View v = inflater.inflate(R.layout.inbox_row, parent, false);
        return new MyViewHolder(v, onRowListener, onItemListener);
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
                        boolean found = false;
                        Message message = messages[messages.length-holder.getLayoutPosition()-1];
                        MimeMessage mimeMessage = new MimeMessage((MimeMessage) message);
                        InternetAddress senderAddress = (InternetAddress) message.getFrom()[0];
                        holderSender[0] = senderAddress.getPersonal();
                        holderSubject[0] = message.getSubject();
                        holderBody[0] = mimeMessage.getContent().toString();
                        String subject = holderSubject[0];
                        String[] subjectSplit = subject.split(" ");
                        if(subjectSplit[0].equals("SecureCMail:")){
                            //if split email was found, try to find its other part
                            for(int i = 0; i < unpairedMessage.toArray().length; i++){
                                Message singleMessage = unpairedMessage.get(i);
                                String singleSubject = singleMessage.getSubject();
                                String[] singleSplit = singleSubject.split(" ");
                                if(singleSplit[2].equals(subjectSplit[2])){
                                    found = true;

                                    if(singleSplit[1].equals(subjectSplit[1])){
                                        //this is to prevent adding duplicate pairs to the unpaired list
                                        break;
                                    }//end if

                                    unpairedMessage.remove(i);
                                    String[] subjects = new String[2];
                                    String[] bodies = new String[2];

                                    //need to check to see which set of shares the second part found is
                                    if(subjectSplit[1].equals("1")){
                                        subjects[0] = message.getSubject();
                                        subjects[1] = singleMessage.getSubject();

                                        bodies[0] = mimeMessage.getContent().toString().substring(0, mimeMessage.getContent().toString().length() - 2);
                                        bodies[1] = singleMessage.getContent().toString().substring(0, singleMessage.getContent().toString().length() - 2);
                                    }//end if
                                    else{
                                        subjects[1] = message.getSubject();
                                        subjects[0] = singleMessage.getSubject();

                                        bodies[1] = mimeMessage.getContent().toString().substring(0, mimeMessage.getContent().toString().length() - 2);
                                        bodies[0] = singleMessage.getContent().toString().substring(0, singleMessage.getContent().toString().length() - 2);
                                    }//end else
                                    String decodeSubject = SecretHelper.decode(subjects);
                                    String decodeBody = SecretHelper.decode(bodies);
                                    holderSubject[0] = decodeSubject;
                                    holderBody[0] = decodeBody;
                                    edited = true;
                                    break;//break out of for loop, we found a pair
                                }//end if

                            }//end for
                            //if no match was found, add to list of unpaired shares
                            if(found == false){
                                unpairedMessage.add(message);
                            }//end if
                        }//end if

                    } catch (MessagingException | IOException e) {
                        throw new RuntimeException(e);
                    }
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            if(edited){
                                edited = false;
                                holder.sender.setText(holderSender[0]);
                                //add "SecureCMail-Decoded" to identify which emails were reconstructed
                                holder.subject.setText("SecureCMail-Decoded "+ holderSubject[0]);
                                holder.body.setText(holderBody[0]);
                            }
                            else {
                                holder.sender.setText(holderSender[0]);
                                holder.subject.setText(holderSubject[0]);
                                holder.body.setText(holderBody[0]);
                            }
                            Log.d("Bind view holder", "Output for subject: " + holderSubject[0]);
                            //holder.body.setText(holderBody[0]);
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
        onItemClick onItemListener;
        public MyViewHolder(@NonNull View itemView, OnRowListener onRowListener, onItemClick onItemClick) {
            super(itemView);
            subject = itemView.findViewById(R.id.subject);
            sender = itemView.findViewById(R.id.sender);
            //body is used to store reconstructed body, since IMAP prevents modifying emails
            body = itemView.findViewById(R.id.body);
            body.setVisibility(View.INVISIBLE);//hide it from users
            this.onRowListener = onRowListener;
            this.onItemListener = onItemClick;
            itemView.setOnClickListener(this);
        }

        /**
         * Gets the strings needed to pass into the alert listener to view emails
         * @param view The email clicked in RecycleViewer
         */
        @Override
        public void onClick(View view) {
            onRowListener.onRowClick(getAdapterPosition());
            getMessage message = new getMessage(getAdapterPosition());
            String text = subject.getText().toString();
            String split[] = text.split(" ");

            //if a reconstructed method was found, pass the decoded subject and body to alert onClick
            if(split[0].equals("SecureCMail-Decoded")){
                String[] strings = new String[2];
                strings[1] = text.substring(20, text.length());
                strings[0] = body.getText().toString();
                //now call listener to construct alert
                onItemListener.onClick(strings);
            }//end if
            else {
                try {
                    //now call listener to construct alert
                    onItemListener.onClick(message.execute().get());
                } catch (ExecutionException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }//end else
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

    class getMessage extends AsyncTask<Integer, Void, String[]> {
        Integer position;
        String subject, body;
        getMessage(Integer position){
            this.position = position;
        }
        @Override
        protected String[] doInBackground(Integer... integers) {
            Message message = messages[messages.length - position - 1];
            try {
                //body = message.getContent().toString();
                body = mailHelper.getText(message);
                Log.d("getMessage Task", "Body of message is: "+body);
                subject = message.getSubject();
            } catch (IOException | MessagingException e) {
                throw new RuntimeException(e);
            }
            return new String[]{body,subject};
        }
    }
}
