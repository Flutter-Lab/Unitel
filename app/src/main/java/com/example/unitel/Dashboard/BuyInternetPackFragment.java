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


public class BuyInternetPackFragment extends Fragment implements View.OnClickListener {
    Button ipButton1, ipButton2, ipButton3, ipButton4, ipButton5, ipButton6, ipButton7, ipButton8, ipButton9, ipButton10;

    String appId = "unitel-app-ofugf";
    MongoDatabase mongoDatabase;
    MongoClient mongoClient;
    User user;
    MongoCollection<Document> collection;
    String phoneNumber;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_buy_internet_pack, container, false);

        SharedPreferences sharedPref = getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        phoneNumber = sharedPref.getString("PhoneNumber", "0");



        ipButton1 = view.findViewById(R.id.ip_button1);
        ipButton2 = view.findViewById(R.id.ip_button2);
        ipButton3 = view.findViewById(R.id.ip_button3);
        ipButton4 = view.findViewById(R.id.ip_button4);
        ipButton5 = view.findViewById(R.id.ip_button5);
        ipButton6 = view.findViewById(R.id.ip_button6);
        ipButton7 = view.findViewById(R.id.ip_button7);
        ipButton8 = view.findViewById(R.id.ip_button8);
        ipButton9 = view.findViewById(R.id.ip_button9);
        ipButton10 = view.findViewById(R.id.ip_button10);

        ipButton1.setOnClickListener(this);
        ipButton2.setOnClickListener(this);
        ipButton3.setOnClickListener(this);
        ipButton4.setOnClickListener(this);
        ipButton5.setOnClickListener(this);
        ipButton6.setOnClickListener(this);
        ipButton7.setOnClickListener(this);
        ipButton8.setOnClickListener(this);
        ipButton9.setOnClickListener(this);
        ipButton10.setOnClickListener(this);

        initMongoDB();



        return view;
    }



    @Override
    public void onClick(View view) {
        int dataAmount;
        int validityTime = 30;
        int price;

        switch (view.getId()) {
            case R.id.ip_button1:
                dataAmount = 1024;
                price = 199;
                break;

                //Log.i(TAG, "onClick: Button 1 Clicked"+dataAmount+price+validityTime);


            case R.id.ip_button2:
                dataAmount = 2048;
                price = 249;
                break;

            case R.id.ip_button3:
                dataAmount = 4096;
                price = 299;
                break;

            case R.id.ip_button4:
                dataAmount = 8192;
                price = 349;
                break;

            case R.id.ip_button5:
                dataAmount = 16384;
                price = 399;
                break;

            case R.id.ip_button6:
                dataAmount = 32768;
                price = 449;
                break;

            case R.id.ip_button7:
                dataAmount = 500;
                price = 50;
                validityTime = 10;
                break;

            case R.id.ip_button8:
                dataAmount = 200;
                price = 40;
                validityTime = 7;
                break;

            case R.id.ip_button9:
                dataAmount = 100;
                price = 30;
                validityTime = 2;
                break;

            case R.id.ip_button10:
                dataAmount = 50;
                price = 10;
                validityTime = 1;
                break;

            default:
                dataAmount =0;
                price = 0;
                validityTime = 0;
        }

        Log.i(TAG, "onClick:  Data: "+view.getId()+"  "+dataAmount + ", Price: "+price + ", Validity: "+validityTime);


        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setTitle("Confirm Purchase")
                .setMessage("Volume: "+dataAmount +" MB"+"\n"+ "Price: "+price +" tk" +"\n"+ "Validity: "+validityTime+" Days" )
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Log.i(TAG, "onClick: Data Pack Purchased");
                        buyDataPack(dataAmount, price);
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

    private void buyDataPack(int dataAmount, int packPrice){
        {

            Document queryFilter = new Document().append("MobileNumber", phoneNumber );

            collection.findOne(queryFilter).getAsync(result -> {
                if (result.isSuccess()){
                    Document resultData = result.get();
                    Document simPack = (Document) resultData.get("simPackage");
                    int amountInt = (int) Float.parseFloat(simPack.getString("Balance"));
                    int dataBalance = Integer.parseInt(simPack.getString("DataPack"));
                    if (packPrice > amountInt){
                        Toast.makeText(getActivity(), "Insufficient balance. Please recharge", Toast.LENGTH_SHORT).show();
                    } else {
                        String finalAmount = String.valueOf((amountInt - packPrice));
                        String dataBalanceFinal = String.valueOf(dataBalance+ dataAmount);
                        simPack.append("Balance", finalAmount);
                        simPack.append("DataPack", dataBalanceFinal);
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