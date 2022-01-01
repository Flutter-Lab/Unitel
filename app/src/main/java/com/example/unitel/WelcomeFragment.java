package com.example.unitel;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class WelcomeFragment extends Fragment {

    Button loginOptionButton, buyNewSimButton;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_welcome, container, false);

        loginOptionButton = view.findViewById(R.id.log_in);
        buyNewSimButton = view.findViewById(R.id.buy_new_sim_wc);



        loginOptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getFragmentManager().beginTransaction().replace(R.id.linearLayout_container,
                        new LoginFragment()).commit();
            }
        });

        buyNewSimButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(getActivity(), BuyNewSimActivity.class);
                startActivity(intent2);
            }
        });














        return view;
    }
}