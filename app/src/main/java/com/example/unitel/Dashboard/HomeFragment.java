package com.example.unitel.Dashboard;
import static android.content.ContentValues.TAG;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.example.unitel.R;
import com.example.unitel.RechargeActivity;

import org.bson.Document;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;
import io.realm.mongodb.mongo.iterable.FindIterable;

public class HomeFragment extends Fragment implements View.OnClickListener {

    String appId = "unitel-app-ofugf";

    MongoDatabase mongoDatabase;
    MongoClient mongoClient;
    User user;
    MongoCollection<Document> mongoCollection1, mongoCollection2;

    TextView nametextView, balanceTextView, dataTextView, talkTimeTextView, smsTextView;
    Button seeInfoButton, rechargeButton, simReplaceButton, buySimButton, buyTalktimeButton;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        SharedPreferences sharedPref = getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE);


        balanceTextView = view.findViewById(R.id.balanceTextView);
        nametextView = view.findViewById(R.id.nameTextView);
        seeInfoButton = view.findViewById(R.id.seeInfoButton);
        dataTextView = view.findViewById(R.id.data_internet_tv);
        talkTimeTextView = view.findViewById(R.id.data_minute_tv);
        smsTextView = view.findViewById(R.id.data_sms_tv);

        rechargeButton = view.findViewById(R.id.rechargeNowButton);
        rechargeButton.setOnClickListener(this);
        simReplaceButton = view.findViewById(R.id.simReplaceButton);
        simReplaceButton.setOnClickListener(this);
        buySimButton = view.findViewById(R.id.buyNewSIMButton);
        buySimButton.setOnClickListener(this);
        buyTalktimeButton = view.findViewById(R.id.buyTalkTimeButton);
        buyTalktimeButton.setOnClickListener(this);




        String phoneNumber = sharedPref.getString("PhoneNumber", "0000");


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
                mongoCollection1 = mongoDatabase.getCollection("Personal_Info");
                mongoCollection2 = mongoDatabase.getCollection("SIM_Info");
                Log.i(TAG, "onResult: Logged in Successfully in Home");
                getUserBalanceData(phoneNumber);

            } else {
                Log.i(TAG, "onResult: Failed to Log in Home");
            }
        });



//        seeInfoButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Document queryFilter = new Document().append("MobileNumber", phoneNumber );
//
//                //FindIterable<Document> doc = mongoCollection2.find()
//
//                mongoCollection1.findOne(queryFilter).getAsync(result -> {
//                    if (result.isSuccess()){
//                        Toast.makeText(getActivity(), "Data Found", Toast.LENGTH_SHORT).show();
//                        Document resultData = result.get();
//                        //String phone = resultData.getString("PhoneNumber");
//                        String fName = resultData.getString("FirstName");
//                        String lName = resultData.getString("LastName");
//                        //String fathersName = resultData.getString("FathersName");
//
//                        nametextView.setText(fName+" "+lName);
//                    }
//                    else {
//                        Toast.makeText(getActivity(), "Data Not Found", Toast.LENGTH_SHORT).show();
//                    }
//                });
//
//                mongoCollection2.findOne(queryFilter).getAsync(result -> {
//                    if (result.isSuccess()){
//                        Toast.makeText(getActivity(), "SIM Data Found", Toast.LENGTH_SHORT).show();
//                        Document resultData = result.get();
//                        //String arrayList = resultData.getString("simPackage");
//                        //String slNo = resultData.getString("SerialNumber");
//
//
//                        Document simPack = (Document) resultData.get("simPackage");
//                        String balance = simPack.getString("Balance");
//                        String dataBalance = simPack.getString("DataPack");
//                        String talkTime = simPack.getString("Talk_Time");
//                        String smsPack = simPack.getString("SMS_Pack");
//                        balanceTextView.setText(balance);
//
//                        dataTextView.setText(dataBalance+" MB");
////                        if (dataBalance != null){
////
////                        } else {
////                            Log.i(TAG, "onClick: Data Balance Not Found");
////                        }
//
//                        talkTimeTextView.setText(talkTime + " Min");
//                        smsTextView.setText(smsPack + " SMS");
//                        Log.i(TAG, "onClick: SIM Package "+balance);
//
//
//
//                    }
//                });
//
//            }
//        });

        return view;
    }

    private void getUserBalanceData(String phoneNumber){

        Document queryFilter = new Document().append("MobileNumber", phoneNumber );

        mongoCollection1.findOne(queryFilter).getAsync(result -> {
            if (result.isSuccess()){
                Toast.makeText(getActivity(), "Data Found", Toast.LENGTH_SHORT).show();
                Document resultData = result.get();
                String fName = resultData.getString("FirstName");
                String lName = resultData.getString("LastName");
                nametextView.setText(fName+" "+lName);
            }
            else {
                Toast.makeText(getActivity(), "Data Not Found", Toast.LENGTH_SHORT).show();
            }
        });

        mongoCollection2.findOne(queryFilter).getAsync(result -> {
            if (result.isSuccess()){
                Toast.makeText(getActivity(), "SIM Data Found", Toast.LENGTH_SHORT).show();
                Document resultData = result.get();
                Document simPack = (Document) resultData.get("simPackage");
                String balance = simPack.getString("Balance");
                String dataBalance = simPack.getString("DataPack");
                String talkTime = simPack.getString("Talk_Time");
                String smsPack = simPack.getString("SMS_Pack");
                balanceTextView.setText(balance);
                dataTextView.setText(dataBalance+" MB");
                talkTimeTextView.setText(talkTime + " Min");
                smsTextView.setText(smsPack + " SMS");
                Log.i(TAG, "onClick: SIM Package "+balance);

            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rechargeNowButton:
                Intent intent = new Intent(getActivity(), RechargeActivity.class);
                startActivity(intent);
        }

    }
}