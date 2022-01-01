package com.example.unitel;

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
import android.widget.EditText;
import android.widget.Toast;

import org.bson.Document;

import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;

public class LoginFragment extends Fragment {

    String appId = "unitel-app-ofugf";

    MongoDatabase mongoDatabase;
    MongoClient mongoClient;
    User user;
    MongoCollection<Document> mongoCollection2;

    EditText phoneNumberEditText;
    Button loginButton;

    SharedPreferences sharedPref;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        sharedPref = getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE);;


        phoneNumberEditText = view.findViewById(R.id.phnNumberEditText);
        loginButton = view.findViewById(R.id.loginButton);


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
                mongoCollection2 = mongoDatabase.getCollection("SIM_Info");
                Log.i(TAG, "onResult: Logged in Successfully in Login Page");

            } else {
                Log.i(TAG, "onResult: Failed to Log in Home");
            }
        });



        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNo = phoneNumberEditText.getText().toString();

                find_user(phoneNo);
            }
        });





        return view;
    }

    private void find_user(String phoneNumber){
        Document queryFilter = new Document().append("MobileNumber", phoneNumber );
        mongoCollection2.findOne(queryFilter).getAsync(result -> {
            if (result.isSuccess()){

                if (result.get() != null){
                    Document resultData = result.get();
                    if (resultData.get("simPackage") != null){
                        Document simPack = (Document) resultData.get("simPackage");
                        if (simPack.getString("Balance") != null){
                            //String balance = simPack.getString("Balance");

                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString("PhoneNumber", phoneNumber).apply();
                            Intent intent = new Intent(getContext(), DashBoardActivity.class);
                            startActivity(intent);
                            Toast.makeText(getActivity(), "Log in successful", Toast.LENGTH_SHORT).show();
                        } else {
                            loginfailed();
                        }
                    } else {
                        loginfailed();
                    }
                } else {
                    loginfailed();
                }



            } else {
                loginfailed();
            }
        });
    }

    private void loginfailed(){
        Toast.makeText(getActivity(), "User Not Found", Toast.LENGTH_SHORT).show();
    }
}