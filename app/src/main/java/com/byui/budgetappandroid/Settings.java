package com.byui.budgetappandroid;

import android.app.VoiceInteractor;
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
import androidx.annotation.Nullable;
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
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
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

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        _returnButton = findViewById(R.id.returnFromSettings);
        _submitButton = findViewById(R.id.submit);
        _logoutButton = findViewById(R.id.logout);
        Intent intent = getIntent();


        //The user's login info
        final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        //If the user's not logged in, go back to the login page. Otherwise, continue.
        if (firebaseAuth.getCurrentUser() == null) {
            startActivity((new Intent(getApplicationContext(), Login.class)));
            finish();
        }

        //get the current user's id from Firebase
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        //Event listener to read info from the database. In this case, that info is the current currency (in case we change it)
        _database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //loop through all of the users
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    //if the ID of our current user matches the one we're looking at, copy their
                    //currency to a variable, then break out of the loop
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
                if(_pickedCurrency != null) {

                    //save new currency
                    _user.child(userId).child("currency").setValue(_pickedCurrency);
                    //call API through currencyConversion()
                    try {
                        List<Expense> expenses = (ArrayList<Expense>) getIntent().getSerializableExtra("listOfExpenses");
                        Toast.makeText(Settings.this, String.valueOf(expenses.size()),
                                Toast.LENGTH_SHORT).show();
                        ArrayList<Double> newExpenses = currencyConversion(expenses);

                        for(int i = 1; i <= newExpenses.size(); i++){
                            _user.child(userId).child("expenses").child("expense_" + i).child("amount").setValue(newExpenses.get(i - 1));
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

//                startActivity((new Intent(getApplicationContext(), MainActivity.class)));
//                finish();

            }

        });
        _returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                startActivity((new Intent(getApplicationContext(), MainActivity.class)));
                finish();
            }

        });

        _logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        final ArrayList<Double> finalNewExpenses = new ArrayList<Double>();

        //Download each cost and concat to the end of the URL, replacing the original value
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        //Retrieving the amount paid for the given expense


        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONObject rates = response.getJSONObject("rates");
                        double oldCur = rates.getDouble(_oldCurrency);
                        double newCur = rates.getDouble(_pickedCurrency);

                    //Loop through each cost value stored in database
                        for(int i = 0; i < databaseExpenses.size(); i++){
                            double amount = databaseExpenses.get(i).getAmount();
                            double newAmount = ((amount / oldCur) * newCur);
                            Toast.makeText(Settings.this, String.valueOf(newAmount),
                                    Toast.LENGTH_SHORT).show();
                            finalNewExpenses.add(newAmount);
                        }


                    Toast.makeText(Settings.this, "SUCCESS",
                            Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    Toast.makeText(Settings.this, "ERROR: " + e,
                            Toast.LENGTH_SHORT).show();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Settings.this, "ERROR: " + error,
                                Toast.LENGTH_LONG).show();
                    }
                }
        );
        requestQueue.add(objectRequest);

        return finalNewExpenses;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selection = ((TextView)view).getText().toString();
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
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}
