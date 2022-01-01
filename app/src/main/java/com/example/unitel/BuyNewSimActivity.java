package com.example.unitel;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class BuyNewSimActivity extends AppCompatActivity {
    Button buySimButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_new_sim);

        buySimButton = findViewById(R.id.buySimOrderButton_bs);

        buySimButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Buy SIM Order Successful", Toast.LENGTH_SHORT).show();
            }
        });
    }
}