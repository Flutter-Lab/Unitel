package com.example.unitel.Dashboard;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.unitel.DashBoardActivity;
import com.example.unitel.R;

import org.bson.Document;

import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;


public class BuyVoicePackFragment extends Fragment implements View.OnClickListener {
    Button vpButton1, vpButton2, vpButton3, vpButton4, vpButton5, vpButton6, vpButton7, vpButton8, vpButton9, vpButton10;

    String appId = "unitel-app-ofugf";
    MongoDatabase mongoDatabase;
    MongoClient mongoClient;
    User user;
    MongoCollection<Document> collection;
    String phoneNumber;





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_buy_voice_pack, container, false);

        SharedPreferences sharedPref = getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        phoneNumber = sharedPref.getString("PhoneNumber", "0");



        vpButton1 = view.findViewById(R.id.vp_button1);
        vpButton2 = view.findViewById(R.id.vp_button2);
        vpButton3 = view.findViewById(R.id.vp_button3);
        vpButton4 = view.findViewById(R.id.vp_button4);
        vpButton5 = view.findViewById(R.id.vp_button5);
        vpButton6 = view.findViewById(R.id.vp_button6);
        vpButton7 = view.findViewById(R.id.vp_button7);
        vpButton8 = view.findViewById(R.id.vp_button8);
        vpButton9 = view.findViewById(R.id.vp_button9);
        vpButton10 = view.findViewById(R.id.vp_button10);

        vpButton1.setOnClickListener(this);
        vpButton2.setOnClickListener(this);
        vpButton3.setOnClickListener(this);
        vpButton4.setOnClickListener(this);
        vpButton5.setOnClickListener(this);
        vpButton6.setOnClickListener(this);
        vpButton7.setOnClickListener(this);
        vpButton8.setOnClickListener(this);
        vpButton9.setOnClickListener(this);
        vpButton10.setOnClickListener(this);

        initMongoDB();














        return view;
    }

    @Override
    public void onClick(View view) {
        int voiceAmount;
        int validityTime = 30;
        int price;

        switch (view.getId()) {
            case R.id.vp_button1:
                voiceAmount = 25;
                price = 20;
                break;

            //Log.i(TAG, "onClick: Button 1 Clicked"+dataAmount+price+validityTime);


            case R.id.vp_button2:
                voiceAmount = 40;
                price = 35;
                break;

            case R.id.vp_button3:
                voiceAmount = 60;
                price = 50;
                break;

            case R.id.vp_button4:
                voiceAmount = 90;
                price = 75;
                break;

            case R.id.vp_button5:
                voiceAmount = 120;
                price = 100;
                break;

            case R.id.vp_button6:
                voiceAmount = 150;
                price = 120;
                break;

            case R.id.vp_button7:
                voiceAmount = 200;
                price = 160;
                validityTime = 10;
                break;

            case R.id.vp_button8:
                voiceAmount = 300;
                price = 200;
                break;

            case R.id.vp_button9:
                voiceAmount = 400;
                price = 270;
                break;

            case R.id.vp_button10:
                voiceAmount = 500;
                price = 250;
                break;

            default:
                voiceAmount = 0;
                price = 0;
                validityTime = 0;
        }

        Log.i(TAG, "onClick:  Data:  "+voiceAmount + ", Price: "+price + ", Validity: "+validityTime);


        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setTitle("Confirm Purchase")
                .setMessage("Voice Pack: "+voiceAmount +" Minutes"+"\n"+ "Price: "+price +" tk" +"\n"+ "Validity: "+validityTime+" Days" )
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Log.i(TAG, "onClick: Data Pack Purchased");
                        buyVoicePack(voiceAmount, price);
                        Toast.makeText(getActivity(), "Data Pack Purchased", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();


    }



    private void initMongoDB(){
        //Initializing MongoDB Realm
        Realm.init(getActivity());
        //Creating app/instance on MongoDB
        App app = new App(new AppConfiguration.Builder(appId).build());

        Credentials credentials = Credentials.anonymous();

        app.loginAsync(credentials, result -> {

            if (result.isSuccess()){

                user = app.currentUser();
                mongoClient = user.getMongoClient("mongodb-atlas");
                mongoDatabase = mongoClient.getDatabase("Customer");
                collection = mongoDatabase.getCollection("SIM_Info");
                Log.i(TAG, "onResult: Logged in Successfully in Home");


            } else {
                Log.i(TAG, "onResult: Failed to Log in Home");
            }
        });
    }


    private void buyVoicePack(int voiceAmount, int packPrice){
        {

            Document queryFilter = new Document().append("MobileNumber", phoneNumber );

            collection.findOne(queryFilter).getAsync(result -> {
                if (result.isSuccess()){
                    Document resultData = result.get();
                    Document simPack = (Document) resultData.get("simPackage");
                    int amountInt = (int) Float.parseFloat(simPack.getString("Balance"));
                    int voiceBalance = Integer.parseInt(simPack.getString("Talk_Time"));
                    if (packPrice > amountInt){
                        Toast.makeText(getActivity(), "Insufficient balance. Please recharge", Toast.LENGTH_SHORT).show();
                    } else {
                        String finalAmount = String.valueOf((amountInt - packPrice));
                        String voiceBalanceFinal = String.valueOf(voiceBalance + voiceAmount);
                        simPack.append("Balance", finalAmount);
                        simPack.append("Talk_Time", voiceBalanceFinal);
                    }


                    collection.updateOne(queryFilter, resultData).getAsync(result1 -> {
                        if (result1.isSuccess()){
                            Log.i(TAG, "onClick: Balance Added Successfully!");
                            Intent intent = new Intent(getActivity(), DashBoardActivity.class);
                            startActivity(intent);
                        } else {
                            Log.i(TAG, "onClick: Something wrong in update");
                        }
                    });

                }
            });


        }
    }
}