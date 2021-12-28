package com.example.unitel;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.bson.Document;

import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;

public class RechargeActivity extends AppCompatActivity implements View.OnClickListener {
    String appId = "unitel-app-ofugf";
    EditText rechargeAmountEditText;
    Button bkashButton, nagadButton, rocketButton, continueButton;
    TextView selectedTextView;

    MongoDatabase mongoDatabase;
    MongoClient mongoClient;
    User user;
    MongoCollection<Document> mongoCollection2;

    String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge);

        SharedPreferences sharedPref = this.getSharedPreferences("MyPref", Context.MODE_PRIVATE);

        rechargeAmountEditText = findViewById(R.id.rcamountEditText);
        rechargeAmountEditText.setOnClickListener(this);
        bkashButton = findViewById(R.id.bkashButton);
        bkashButton.setOnClickListener(this);
        nagadButton = findViewById(R.id.nagadButton);
        nagadButton.setOnClickListener(this);
        rocketButton = findViewById(R.id.rocketButton);
        rocketButton.setOnClickListener(this);
        continueButton = findViewById(R.id.rcContinueButton);
        continueButton.setOnClickListener(this);
        selectedTextView = findViewById(R.id.selectedPMTextView);


        phoneNumber = sharedPref.getString("PhoneNumber", "0000");



        //Initializing MongoDB Realm
        Realm.init(this);

        //Creating app/instance on MongoDB
        App app = new App(new AppConfiguration.Builder(appId).build());

        Credentials credentials = Credentials.anonymous();

        app.loginAsync(credentials, result -> {

            if (result.isSuccess()){

                user = app.currentUser();
                mongoClient = user.getMongoClient("mongodb-atlas");
                mongoDatabase = mongoClient.getDatabase("Customer");
                mongoCollection2 = mongoDatabase.getCollection("SIM_Info");
                Log.i(TAG, "onResult: Logged in Successfully in Home");


            } else {
                Log.i(TAG, "onResult: Failed to Log in Home");
            }
        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rcContinueButton:

                String amount = rechargeAmountEditText.getText().toString();
                int inputAmount = Integer.parseInt(amount);
                if (amount != null){

                    Document queryFilter = new Document().append("MobileNumber", phoneNumber );

                    mongoCollection2.findOne(queryFilter).getAsync(result -> {
                        if (result.isSuccess()){
                            Document resultData = result.get();
                            Document simPack = (Document) resultData.get("simPackage");
                            int amountInt = Integer.parseInt(simPack.getString("Balance"));
                            String finalAmount = String.valueOf((amountInt + inputAmount));
                            simPack.append("Balance", finalAmount);
                            
                            mongoCollection2.updateOne(queryFilter, resultData).getAsync(result1 -> {
                                if (result1.isSuccess()){
                                    Log.i(TAG, "onClick: Balance Added Successfully!");
                                    Intent intent = new Intent(this, DashBoardActivity.class);
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
}