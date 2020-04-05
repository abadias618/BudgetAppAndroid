package com.byui.budgetappandroid;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;


public class MainActivity extends AppCompatActivity {

    //link buttons
    Button _newExpense;
    Button _settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _newExpense = findViewById(R.id.newExpenseButton);
        _settings = findViewById(R.id.settings);

        /*

        Spinner mySpinner = findViewById(R.id.spinner1);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.categories));
        // specify adapter to dropdown list
                myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                //This line allow the adapter to show the adapter inside the spinner.
                mySpinner.setAdapter(myAdapter);


         */
        _newExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), NewExpense.class));
            }
        });

        _settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Settings.class));
            }
        });
    }

    //when the app is destroyed it automatically signs you out
    @Override
    protected void onStop() {
        super.onStop();
        FirebaseAuth.getInstance().signOut();
    }

    //david push test
    //rachel push test 2.0
}
