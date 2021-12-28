package com.example.unitel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.unitel.Dashboard.BuyInternetPackFragment;
import com.example.unitel.Dashboard.BuyVoicePackFragment;
import com.example.unitel.Dashboard.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class DashBoardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);


        getSupportFragmentManager().beginTransaction().replace(R.id.dashboard_fragment_container,
                new HomeFragment()).commit();
    }




    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;
            //String fragmentTag = "";

            switch (item.getItemId()){
                case R.id.nav_home:
                    selectedFragment = new HomeFragment();
                    break;
                case R.id.nav_internet:
                    selectedFragment = new BuyInternetPackFragment();
                    break;
                case R.id.nav_bundle:
                    selectedFragment = new BuyVoicePackFragment();
                    break;
            }

            getSupportFragmentManager().beginTransaction().replace(R.id.dashboard_fragment_container,
                    selectedFragment).commit();
            return true;
        }
    };
}