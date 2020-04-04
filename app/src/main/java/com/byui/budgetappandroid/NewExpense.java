package com.byui.budgetappandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class NewExpense extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_expense);

        // Spinner element
        Spinner mySpinner = findViewById(R.id.categorySpinner);


        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(NewExpense.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.categories));

        // specify adapter to dropdown list
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //This line allow the adapter to show the adapter inside the spinner.
        mySpinner.setAdapter(myAdapter);


    }

}
