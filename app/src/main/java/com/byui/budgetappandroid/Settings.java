package com.byui.budgetappandroid;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class Settings extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    String _oldCurrency;
    private String pickedCurrency;
    private DatabaseReference _database = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        //The user's login info
        final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        //If the user's not logged in, go back to the login page. Otherwise, continue.
/*        if (firebaseAuth.getCurrentUser() == null) {
            startActivity((new Intent(getApplicationContext(), Login.class)));
            finish();
        }
*/


        final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        //save current currency from Firebase to a variable
//        _oldCurrency = _database.child("users").child(userId).child("currency").getValue(pickedCurrency);

        //Retrieve any input from the "currency" field
        Spinner spinner = findViewById(R.id.currencyInput);
        List<String> currencies = new ArrayList<>();
        currencies.add("Euros");
        currencies.add("Aus Dollars");
        currencies.add("US Dollars");

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, currencies);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        //Listen for when/if the user selects an item
        spinner.setOnItemSelectedListener(this);


        final TextView _submitButton = findViewById(R.id.submitButton);


        _submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                if(pickedCurrency != null) {

                    //save new currency in a variable (returned by onItemSelected)
 //                   onItemSelected(adapter, );
                    _database.child("users").child(userId).child("currency").setValue(pickedCurrency);

                    //call API through currencyConversion()
                }

                startActivity((new Intent(getApplicationContext(), MainActivity.class)));
                finish();

            }

        });

    }

    public void currencyConversion(String oldCurrency, String newCurrency) throws IOException {
        //Setting up the URL for the API. NOTE: The "amount" value is not yet added to the URL string
        URL url = new URL("http://data.fixer.io/api/convert?access_key=edbef9eb81e730c20186f2be117dec47&from="
                + oldCurrency + "&to=" + newCurrency + "&amount=");

        //Loop through each cost value stored in database
        //Download each cost and concat to the end of the URL, replacing the original value
        //

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("GET");



        Toast.makeText(Settings.this, "New",
                Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selection = ((TextView)view).getText().toString();
        switch (selection) {
            case "US Dollars":
                pickedCurrency = "USD";
                break;
            case "Aus Dollars":
                pickedCurrency = "AUD";
                break;
            default:
                pickedCurrency = "EUR";
        }
        Toast.makeText(Settings.this, pickedCurrency,
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
