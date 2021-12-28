package com.example.unitel;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.bson.Document;

import java.util.Objects;

import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.RealmResultTask;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;
import io.realm.mongodb.mongo.iterable.MongoCursor;

public class TestActivity extends AppCompatActivity {

    String appId = "unitel-app-ofugf";

    MongoDatabase mongoDatabase;
    MongoClient mongoClient;
    User user;

    private EditText dataEditText;
    private Button sendDataButton, seeYourDataButton, gotoMainButton;
    private TextView dataTextView;

    MongoCollection<Document> mongoCollection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        dataEditText = findViewById(R.id.dataEditText);
        sendDataButton = findViewById(R.id.sendDataButton);
        seeYourDataButton = findViewById(R.id.seeYourDataButton);
        gotoMainButton = findViewById(R.id.gotoMainButton);
        dataTextView = findViewById(R.id.dataTextView);





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
                mongoCollection = mongoDatabase.getCollection("Personal_Info");
                Log.i(TAG, "onResult: Logged in Successfully");

            } else {
                Log.i(TAG, "onResult: Failed to Log in");
            }
        });


        sendDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = app.currentUser();
                mongoClient = user.getMongoClient("mongodb-atlas");
                mongoDatabase = mongoClient.getDatabase("Customer");
                MongoCollection<Document> mongoCollection = mongoDatabase.getCollection("SIM_Info");

                mongoCollection.insertOne(new Document("userid", user.getId()).append("data",dataEditText.getText().toString()).append("id",1234)).getAsync(result -> {
                    if (result.isSuccess()){
                        Log.i(TAG, "onClick: Data Inserted Successfully");
                    } else {
                        Log.i(TAG, "onClick: Data Inserted Failed");
                    }
                } );
            }
        });

        seeYourDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Document queryFilter = new Document().append("MobileNumber", "01274351444" );

                mongoCollection.findOne(queryFilter).getAsync(result -> {
                    if (result.isSuccess()){
                        Toast.makeText(getApplicationContext(), "Data Found", Toast.LENGTH_SHORT).show();
                        Document resultData = result.get();
                        String phone = resultData.getString("PhoneNumber");
                        String fName = resultData.getString("FirstName");
                        String lName = resultData.getString("LastName");
                        String fathersName = resultData.getString("FathersName");

                        dataTextView.setText(phone+"\n"+ "Name: "+fName+" "+lName+ "\n" + "Father's Neme: "+fathersName);
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Data Not Found", Toast.LENGTH_SHORT).show();
                    }
                });

//                String data;
//                findTask.getAsync(task ->{
//                    if (task.isSuccess()){
//                        MongoCursor<Document> results = task.get();
//                        while (results.hasNext()){
//                            Document currentDoc = results.next();
//                            if (currentDoc.getString("data") != null){
//                                data = data+currentDoc
//                            }
//                        }
//
//                    } else {
//                        Log.i(TAG, "onClick: Task Error");
//                    }
//                });

            }
        });

        gotoMainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TestActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });


    }
}