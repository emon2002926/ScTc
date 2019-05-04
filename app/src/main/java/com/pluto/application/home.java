package com.pluto.application;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

public class home extends AppCompatActivity {
    @Override

    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_activity_menu, menu);

        return super.onCreateOptionsMenu(menu);

    }


    public void Maps(MenuItem item) {
        Intent intent = new Intent(home.this,Maps.class);
        startActivity(intent);
    }

    public void fire(View view) {
        Intent intent = new Intent(home.this,Firebase1.class);
        startActivity(intent);
    }



    Firebase firebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P){
            NotificationChannel channel = new
                    NotificationChannel("MyNotifications", "MyNotifications",
                    NotificationManager.IMPORTANCE_DEFAULT);


            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);

        }
        FirebaseMessaging.getInstance().subscribeToTopic("general")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "succes";
                        if (!task.isSuccessful()) {
                            msg ="faild";
                        }

                        Toast.makeText(home.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });


    }


    public void fj(View view) {
        Intent intent = new Intent(home.this,ImageI.class);
        startActivity(intent);
    }
}


