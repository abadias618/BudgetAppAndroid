package com.byui.budgetappandroid;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {


    Button _newExpense;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _newExpense = findViewById(R.id.newExpenseButton);

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

    }

    //david push test
    //rachel push test 2.0
}
