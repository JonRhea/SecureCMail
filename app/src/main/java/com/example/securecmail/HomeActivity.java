package com.example.securecmail;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class HomeActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        recyclerView = findViewById(R.id.recyclerView);
        RecyclerAdapter adapter = new RecyclerAdapter(this, new RecyclerAdapter.OnRowListener() {
            @Override
            public void onRowClick(int position) {
                Log.d("Inbox Activity", "Row "+position+" clicked!");
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
