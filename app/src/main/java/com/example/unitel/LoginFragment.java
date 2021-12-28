package com.example.unitel;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class LoginFragment extends Fragment {

    EditText phoneNumberEditText;
    Button loginButton;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        SharedPreferences sharedPref = getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE);;


        phoneNumberEditText = view.findViewById(R.id.phnNumberEditText);
        loginButton = view.findViewById(R.id.loginButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNo = phoneNumberEditText.getText().toString();

                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("PhoneNumber", phoneNo).apply();

                Intent intent = new Intent(getContext(), DashBoardActivity.class);
                startActivity(intent);

            }
        });





        return view;
    }
}