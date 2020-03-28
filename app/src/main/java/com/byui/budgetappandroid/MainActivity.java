package com.byui.budgetappandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Spinner mySpinner = findViewById(R.id.spinner1);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> myAdapter = new ArrayAdapter<>(MainActivity.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.categories));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // specify adapter to dropdown list
                myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                //This line allow the adapter to show the adapter inside the spinner.
                mySpinner.setAdapter(myAdapter);

        final Button _settings = findViewById(R.id.settingsButton);


        _settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                startActivity((new Intent(getApplicationContext(), Settings.class)));
                finish();

            }

        });

    }



    //david push test
    //rachel push test 2.0
}
