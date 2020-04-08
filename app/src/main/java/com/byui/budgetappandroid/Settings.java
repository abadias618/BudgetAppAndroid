package com.byui.budgetappandroid;

import android.content.Intent;
import android.os.Bundle;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.json.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLOutput;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
//                Toast.makeText(Settings.this, String.valueOf(USD),
//                        Toast.LENGTH_LONG).show();

public class Settings extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    String _oldCurrency;
    private String _pickedCurrency;
    private Button _returnButton, _submitButton, _logoutButton;
    private DatabaseReference _database = FirebaseDatabase.getInstance().getReference();
    //get a reference to the "users" level of the database
    DatabaseReference _user = FirebaseDatabase.getInstance().getReference("users");
    //private List<Expense> newExpenses;
    String userId;

    double USD;
    double AUD;
    int recordNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Connect the activity to the XML script
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //Connect to buttons from the XML script
        _returnButton = findViewById(R.id.returnFromSettings);
        _submitButton = findViewById(R.id.submit);
        _logoutButton = findViewById(R.id.logout);
        USD = 0;
        AUD = 0;
        recordNumber = 0;

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

/*
        _database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //loop through all of the users

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
*/

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

        //Setting up the URL for the API. The end amount will be added later
        String url = ("https://data.fixer.io/api/latest?access_key=edbef9eb81e730c20186f2be117dec47");
        //An ArrayList to store our new values in
        //Arrays with a length of one to store the conversion rates in

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //API call
        StringRequest objectRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //In the event that we get a response, try to retrieve the conversion rates
                // from the API, and save them
                try{
                JSONObject json = new JSONObject(response);
                // parsing file "JSONExample.json"
                USD = json.getJSONObject("rates").getDouble("USD");
                AUD = json.getJSONObject("rates").getDouble("AUD");}
                catch (JSONException e) {
                    e.printStackTrace();
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

        _submitButton.setOnClickListener(new View.OnClickListener() {
                                             @Override
                                             public void onClick(View view) {
                                                 //If the user has picked a new currency
                                                 if ((_pickedCurrency != null) ) {
                                                     //save new currency

                                                     //Retrieve a list of the user's expenses from the database
                                                     _database.addListenerForSingleValueEvent(new ValueEventListener() {
                                                         @Override
                                                         public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                             final ArrayList<Double> oldAmounts = new ArrayList<Double>();
                                                             final ArrayList<Double> newAmounts = new ArrayList<Double>();

                                                             /*
                                                             for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                                                 //if the ID of our current user matches the one we're looking at, copy their
                                                                 //currency to a variable, then break out of the loop.
                                                                 //This copy of the old currency will help us if the user changes their currency
                                                                 if(ds.getKey().equals(userId)){
                                                                     _oldCurrency = (String) ds.child("currency").getValue();
                                                                     System.out.println(_oldCurrency);
                                                                     break;
                                                                 }
                                                             }
                                                             */

                                                              _oldCurrency = (String) dataSnapshot.child("users").child(userId).child("currency").getValue();
                                                             _user.child(userId).child("currency").setValue(_pickedCurrency);

                                                             System.out.println(_oldCurrency);

                                                             if (dataSnapshot.exists()) {
                                                                 recordNumber = (int) (long) dataSnapshot.child("users").child(userId).child("record_number").getValue();
                                                             }
                                                             for (int i = 1; i <= recordNumber; i++) {
                                                                 //double amount = ((double) (long) dataSnapshot.child("users").child(userId).child("expenses").child("expense_" + i).child("amount").getValue());
                                                                 oldAmounts.add((double) dataSnapshot.child("users").child(userId).child("expenses").child("expense_" + i).child("amount").getValue());
                                                                 //reference.child("users").child(_userId).child("record_number").setValue(recordNumber);
                                                             }


                                                             double oldCur;
                                                             double newCur;
                                                             switch (_pickedCurrency) {
                                                                 case "US Dollars":
                                                                     newCur = USD;
                                                                     break;
                                                                 case "Aus Dollars":
                                                                     newCur = AUD;
                                                                     break;
                                                                 default:
                                                                     newCur = 1;
                                                             }
                                                             switch (_oldCurrency) {
                                                                 case "US Dollars":
                                                                     oldCur = USD;
                                                                     break;
                                                                 case "Aus Dollars":
                                                                     oldCur = AUD;
                                                                     break;
                                                                 default:
                                                                     oldCur = 1;
                                                             }

                                                             for (int i = 0; i < oldAmounts.size(); i++) {
                                                                 //retrieve the "amount" from the current Expense
                                                                 double amount = oldAmounts.get(i);
                                                                 //convert it to the new amount
                                                                 double newAmount = ((amount / oldCur) * newCur);
                                                                 BigDecimal bd = new BigDecimal(newAmount).setScale(2, RoundingMode.HALF_UP);
                                                                 //add the new price to the newAmounts array
                                                                 newAmounts.add(newAmount);
                                                             }

                                                             //Send the user's expenses to currencyConversion to receive a list of doubles.
                                                             // These doubles are the amounts in the new currency.
                                                             //Loop through each amount and add replace the old amount with the new one

                                                             for (int i = 1; i <= newAmounts.size(); i++) {
                                                                 System.out.println("Inside");
                                                                 _database.child("users").child(userId).child("expenses").child("expense_" + i).child("amount").setValue(newAmounts.get(i - 1));
                                                             }
                                                         }

                                                         @Override
                                                         public void onCancelled(@NonNull DatabaseError databaseError) {

                                                         }

                                                     });


                                                     //Go back to the main activity
//                    startActivity((new Intent(getApplicationContext(), MainActivity.class)));
//                    finish();
                                                 }
                                             }
                                         });

        _returnButton.setOnClickListener(new View.OnClickListener() {
                                             @Override
                                             public void onClick(View view) {
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


/*
    public ArrayList<Double> currencyConversion() throws IOException {
        //Setting up the URL for the API. The end amount will be added later
        String url = ("https://data.fixer.io/api/latest?access_key=edbef9eb81e730c20186f2be117dec47");
        //An ArrayList to store our new values in
        final ArrayList<Double> newAmounts = new ArrayList<Double>();
        //Arrays with a length of one to store the conversion rates in

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //API call
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //In the event that we get a response, try to retrieve the conversion rates
                // from the API, and save them in the final double arrays above
                try {
                    JSONObject rates = response.getJSONObject("rates");
                    double oldCur = rates.getDouble(_oldCurrency);
                    double newCur = rates.getDouble(_pickedCurrency);

                    //Loop through each cost value stored in database
                    for(int i = 0; i < amounts.size(); i++){
                        //retrieve the "amount" from the current Expense
                        double amount = amounts.get(i);
                        //convert it to the new amount
                        double newAmount = ((amount / oldCur) * newCur);

                        Toast.makeText(Settings.this, String.valueOf(newAmount),
                                Toast.LENGTH_SHORT).show();
                        //add the new price to the newAmounts array
                        newAmounts.add(newAmount);
                    }
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

        //return the newExpenses array, which contains all of the converted prices
        return newAmounts;
    }
*/
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //Find the string that the user picked using the spinner
        String selection = ((TextView)view).getText().toString();
        //According to which string the user picked, set _pickedCurrency to the corresponding key.
        //The choice needs to be stored as one of these keys in order for the API to understand any requests.
        /*
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
           _pickedCurrency = _oldCurrency;
        }*/
        if(selection != "Select"){
        _pickedCurrency = selection;}
        else {
            _pickedCurrency = _oldCurrency;
        }
    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}
