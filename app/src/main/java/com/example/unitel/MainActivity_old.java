package com.example.unitel;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.unitel.Dashboard.BuyVoicePackFragment;

import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;

public class MainActivity_old extends AppCompatActivity implements View.OnClickListener {

    String appId = "unitel-app-ofugf";

    Button buyNewSim, simReplace, buyNetPack, recharge, buyVoicePacek;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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











        buyNewSim = findViewById(R.id.buy_new_sim);
        buyNewSim.setOnClickListener(this);

        simReplace = findViewById(R.id.sim_replace);
        simReplace.setOnClickListener(this);

        buyNetPack = findViewById(R.id.buy_net_pack);
        buyNetPack.setOnClickListener(this);


        recharge = findViewById(R.id.recharge);
        recharge.setOnClickListener(this);

        buyVoicePacek = findViewById(R.id.buy_voice_pack);
        buyVoicePacek.setOnClickListener(this);



    }

    @Override
    public void onClick(View view) {
        //Log.i("TAG", "onClick: Button Clicked"+ view.getId());
        Intent intent = null;
        switch (view.getId()){
            case R.id.buy_new_sim:
                //Toast.makeText(getApplicationContext(), "BNS clicked", Toast.LENGTH_SHORT).show();
                intent = new Intent(getApplicationContext(),BuyNewSimActivity.class);
                break;
            case R.id.sim_replace:
                intent = new Intent(getApplicationContext(), SimReplaceActivity.class);
                break;
            case R.id.buy_net_pack:
                intent = new Intent(getApplicationContext(), BuyNetPackActivity.class);
                break;
            case R.id.recharge:
                intent = new Intent(getApplicationContext(), RechargeActivity.class);
                break;
            case R.id.buy_voice_pack:
                intent = new Intent(getApplicationContext(), BuyVoicePackFragment.class);
                break;
            default:
                break;
        }

        if (intent != null){
            startActivity(intent);
        }

    }
}