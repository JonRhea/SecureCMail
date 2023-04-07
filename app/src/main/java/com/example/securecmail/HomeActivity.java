package com.example.securecmail;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class HomeActivity extends AppCompatActivity {

    String[] userInfo = new String[4];

    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        try {
            File filePath = new File(HomeActivity.this.getFilesDir(), "securecmail_data");
            if (!filePath.exists()) {
                filePath.mkdir();
            }//end if

            InputStream inputStream = this.openFileInput("user_email.txt");
            InputStreamReader inputReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputReader);

            String data;

            data = bufferedReader.readLine();
            System.out.println(data);
            if(data == null){
                Intent intent = new Intent(this,ConfigureEmailActivity.class);
                startActivity(intent);
            }
            userInfo = data.split(",");

        }//end try
        catch (IOException e) {
            System.out.println("Error: IOException");
        }//end catch

        recyclerView = findViewById(R.id.recyclerView);
        RecyclerAdapter adapter = new RecyclerAdapter(this, userInfo,new RecyclerAdapter.OnRowListener() {
            @Override
            public void onRowClick(int position) {
                Log.d("Inbox Activity", "Row "+position+" clicked!");
            }
        }, new onItemClick() {
            @Override
            public void onClick(String[] strings) {

                AlertDialog.Builder messageViewAlert = new AlertDialog.Builder(HomeActivity.this);
                View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.alert_display_message, null);
                WebView webview = view.findViewById(R.id.webview);
                webview.setWebViewClient(new WebViewClient());
                webview.getSettings().setMediaPlaybackRequiresUserGesture(false);
                webview.setWebChromeClient(new WebChromeClient());
                webview.loadDataWithBaseURL(null, strings[0], "text/html", "UTF-8", null);
                messageViewAlert.setTitle(strings[1]);
                messageViewAlert.setView(view);
                messageViewAlert.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                messageViewAlert.show();
            }
        });
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
    }

    /**
     * Sends the user to the ConfigureEmail Activity
     * @param view The button to go to Configure
     */
    public void toConfigure(View view) {
        Intent intent = new Intent(this,ConfigureEmailActivity.class);
        startActivity(intent);
    }//end toConfigure

    /**
     * Sends the user to the ContactActivity
     * @param view The button to go to Contacts
     */
    public void toContacts(View view) {
        Intent intent = new Intent(this,ContactActivity.class);
        startActivity(intent);
    }//end toContacts

    /**
     * Sends the user to the ContactActivity
     * @param view The button to go to Message
     */
    public void toMessage(View view) {
        Intent intent = new Intent(this,MessageActivity.class);
        startActivity(intent);
    }//end toMessage
}
