package com.byui.budgetappandroid;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entdata);

        Spinner mySpinner = findViewById(R.id.spinner1);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.categories));
        // specify adapter to dropdown list
                myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                //This line allow the adapter to show the adapter inside the spinner.
                mySpinner.setAdapter(myAdapter);



    }

    //david push test
    //rachel push test 2.0
}
