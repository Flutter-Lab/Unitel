package com.example.unitel;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;

public class MainActivity extends AppCompatActivity {


    String appId = "unitel-app-ofugf";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPref = this.getSharedPreferences("MyPref", Context.MODE_PRIVATE);

        String phoneNumber = sharedPref.getString("PhoneNumber", "0");

        int phoneInt = Integer.parseInt(phoneNumber);

        if (phoneInt<10000){
            getSupportFragmentManager().beginTransaction().replace(R.id.linearLayout_container,
                    new WelcomeFragment()).commit();
        } else {
            Intent intent = new Intent(this, DashBoardActivity.class);
            startActivity(intent);
        }


        //Initializing MongoDB Realm
        Realm.init(this);

        //Creating app/instance on MongoDB
        App app = new App(new AppConfiguration.Builder(appId).build());

        Credentials credentials = Credentials.emailPassword("rokeya@gmail.com", "Roking123");

        app.loginAsync(credentials, result -> {

            if (result.isSuccess()){

                Log.i(TAG, "onResult: Logged in with Email");

            } else {
                Log.i(TAG, "onResult: Failed to Log in");
            }

        });

//        app.getEmailPassword().registerUserAsync("rokeya@gmail.com", "Roking123", it->{
//
//            if (it.isSuccess()){
//                Log.i(TAG, "onCreate: User Registered with Email successfully");
//
//            } else{
//
//                Log.i(TAG, "onCreate: User Registration failed");
//
//            }
//        });
    }




}