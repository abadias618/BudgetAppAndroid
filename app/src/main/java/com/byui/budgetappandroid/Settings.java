package com.byui.budgetappandroid;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import java.util.ArrayList;
import java.util.List;


public class Settings extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    String _oldCurrency;
    private String _pickedCurrency;
    private Button _returnButton, _submitButton, _logoutButton;
    private DatabaseReference _database = FirebaseDatabase.getInstance().getReference();
    //get a reference to the "users" level of the database
    DatabaseReference _user = FirebaseDatabase.getInstance().getReference("users");
//    private List<Expense> newExpenses;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Connect the activity to the XML script
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //Connect to buttons from the XML script
        _returnButton = findViewById(R.id.returnFromSettings);
        _submitButton = findViewById(R.id.submit);
        _logoutButton = findViewById(R.id.logout);

        //Save a copy of the user's info
        final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        //If the user's not logged in, go back to the login page. Otherwise, continue.
        if (firebaseAuth.getCurrentUser() == null) {
            startActivity((new Intent(getApplicationContext(), Login.class)));
            finish();
        }

        //Save the current user's id
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        //Event listener to read info from the database.
        _database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //loop through all of the users
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    //if the ID of our current user matches the one we're looking at, copy their
                    //currency to a variable, then break out of the loop.
                    //This copy of the old currency will help us if the user changes their currency
                    if(ds.getKey().equals(userId)){
                        _oldCurrency = ds.child(userId).child("currency").getValue(String.class);
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        //Create a spinner for the activity
        Spinner spinner = findViewById(R.id.currencyInput);
        //Create then populate a list for the spinner
        List<String> currencies = new ArrayList<>();
        currencies.add("Select");
        currencies.add("Euros");
        currencies.add("Aus Dollars");
        currencies.add("US Dollars");

        //An array adapter to let us know when the user interacts with our spinner
        final ArrayAdapter<String> adapter =
                new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, currencies);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        //Listen for when/if the user selects an item
        spinner.setOnItemSelectedListener(this);

        _submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                //If the user has picked a new currency
                if(_pickedCurrency != null) {
                    //save new currency
                    _user.child(userId).child("currency").setValue(_pickedCurrency);

                    try {
                        //Retrieve a list of the user's expenses from the database
                        List<Expense> expenses = (ArrayList<Expense>) getIntent().getSerializableExtra("listOfExpenses");
                        Toast.makeText(Settings.this, String.valueOf(expenses.size()),
                                Toast.LENGTH_SHORT).show();
                        //Send the user's expenses to currencyConversion to receive a list of doubles.
                        // These doubles are the amounts in the new currency.
                        ArrayList<Double> newAmounts = currencyConversion(expenses);
                        //Loop through each amount and add replace the old amount with the new one
                        for(int i = 1; i <= newAmounts.size(); i++){
                            _user.child(userId).child("expenses").child("expense_" + i).child("amount").setValue(newAmounts.get(i - 1));
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                //Go back to the main activity
                startActivity((new Intent(getApplicationContext(), MainActivity.class)));
                finish();

            }

        });
        _returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                //Return to the main activity
                startActivity((new Intent(getApplicationContext(), MainActivity.class)));
                finish();
            }

        });

        _logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log the user out, then return to the login activity
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                firebaseAuth.signOut();
                startActivity(new Intent(getApplicationContext(), Login.class));
                finish();
            }
        });
    }

    public ArrayList<Double> currencyConversion(final List<Expense> databaseExpenses) throws IOException {
        //Setting up the URL for the API. The end amount will be added later
        String url = ("https://data.fixer.io/api/latest?access_key=edbef9eb81e730c20186f2be117dec47");
        //An ArrayList to store our new values in
        final ArrayList<Double> newAmounts = new ArrayList<Double>();
        //Arrays with a length of one to store the conversion rates in
        final double[] oldCur = new double[1];
        final double[] newCur = new double[1];
        //Download each cost and concat to the end of the URL, replacing the original value
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        //Retrieving the amount paid for the given expense

        //API call
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //In the event that we get a response, try to retrieve the conversion rates
                // from the API, and save them in the final double arrays above
                try {
                    JSONObject rates = response.getJSONObject("rates");
                        oldCur[0] = rates.getDouble(_oldCurrency);
                        newCur[0] = rates.getDouble(_pickedCurrency);

                    Toast.makeText(Settings.this, "SUCCESS",
                            Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    Toast.makeText(Settings.this, "ERROR: " + e,
                            Toast.LENGTH_SHORT).show();
                }
            }
        },
                //In the event of an error, print the error
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Settings.this, "ERROR: " + error,
                                Toast.LENGTH_LONG).show();
                    }
                }
        );
        requestQueue.add(objectRequest);

        //Loop through each cost value stored in database
        for(int i = 0; i < databaseExpenses.size(); i++){
            //retrieve the "amount" from the current Expense
            double amount = databaseExpenses.get(i).getAmount();
            //convert it to the new amount
            double newAmount = ((amount / oldCur[0]) * newCur[0]);
            Toast.makeText(Settings.this, String.valueOf(newAmount),
                    Toast.LENGTH_SHORT).show();
            //add the new price to the newAmounts array
            newAmounts.add(newAmount);
        }


        //return the newExpenses array, which contains all of the converted prices
        return newAmounts;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //Find the string that the user picked using the spinner
        String selection = ((TextView)view).getText().toString();
        //According to which string the user picked, set _pickedCurrency to the corresponding key.
        //The choice needs to be stored as one of these keys in order for the API to understand any requests.
        switch (selection) {
            case "US Dollars":
                _pickedCurrency = "USD";
                break;
            case "Aus Dollars":
                _pickedCurrency = "AUD";
                break;
            case "Euros":
                _pickedCurrency = "EUR";
                break;
            default:
                //If the user hasn't picked a currency, simply set it to the old currency.
                //Worst case scenario, we set the currency back to itself.
                _pickedCurrency = _oldCurrency;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}
