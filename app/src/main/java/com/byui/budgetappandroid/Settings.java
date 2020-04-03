package com.byui.budgetappandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class Settings extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private String pickedCurrency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        //The user's login info
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        //If the user's not logged in, go back to the login page. Otherwise, continue.
/*        if (firebaseAuth.getCurrentUser() == null) {
            startActivity((new Intent(getApplicationContext(), Login.class)));
            finish();
        }
*/
        //Retrieve any input from the "currency" field
        Spinner spinner = findViewById(R.id.currencyInput);
        ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource(this, R.array.currencies, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        //Listen for when/if the user selects an item
        spinner.setOnItemSelectedListener(this);


        final TextView _submitButton = findViewById(R.id.submit);


        _submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                if(pickedCurrency != null) {

                    //save current currency from Firebase to a variable
                    //save new currency in a variable (returned by onItemSelected)
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
