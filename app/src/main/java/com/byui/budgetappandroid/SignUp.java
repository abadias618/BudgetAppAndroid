package com.byui.budgetappandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SignUp extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    EditText _emailInput , _passwordInput;
    TextView _signUpTitle, _loginSubtitle, _emailLabel, _passwordSubtitle, _loginLink;
    Button _submitButton;
    FirebaseAuth firebaseAuth;
    Spinner _currencyInput;
    private DatabaseReference _database;
    private String pickedCurrency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        _emailInput = findViewById(R.id.emailInput);
        _passwordInput = findViewById(R.id.passwordInput);
        _currencyInput = findViewById(R.id.currencyInput);
        _signUpTitle = findViewById(R.id.signUpTitle);
        _loginSubtitle = findViewById(R.id.loginSubtitle);
        _emailLabel = findViewById(R.id.emailLabel);
        _passwordSubtitle = findViewById(R.id.passwordLabel);
        _loginLink = findViewById(R.id.loginLink);
        _submitButton = findViewById(R.id.submitButton);
        _database = FirebaseDatabase.getInstance().getReference();

        firebaseAuth = FirebaseAuth.getInstance();

        //if user is logged in re-route to main activity
        if (firebaseAuth.getCurrentUser() != null) {
            startActivity((new Intent(getApplicationContext(), MainActivity.class)));
            finish();
        }

        List<String> currencies = new ArrayList<>();
        currencies.add("Select");
        currencies.add("Euros");
        currencies.add("Aus Dollars");
        currencies.add("US Dollars");

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, currencies);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        _currencyInput.setAdapter(adapter);
        //Listen for when/if the user selects an item
        _currencyInput.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);


        // listener for the SUBMIT button
        _submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = _emailInput.getText().toString().trim();
                String password = _passwordInput.getText().toString().trim();

                //check if the input fields are not empty
                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || (pickedCurrency == null)) {
                    _emailInput.setError("You are missing something...");
                }
                //create a user on the firebase
                 firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                     @Override
                     public void onComplete(@NonNull Task<AuthResult> task) {
                         if (task.isSuccessful()) {
                             Toast.makeText(SignUp.this, "User Created Successfully",
                                     Toast.LENGTH_SHORT).show();
                          //   firebaseAuth.collection("Users");

                             String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                             _database.child("users").child(userId).child("currency").setValue(pickedCurrency);
                             ArrayList<Expense> expenses = new ArrayList<Expense>();
                             _database.child("users").child(userId).child("expenses").setValue(expenses);
                             startActivity(new Intent(getApplicationContext(), MainActivity.class));
                         }
                         else {
                             Toast.makeText(SignUp.this, "An Error Occurred"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                         }

                     }
                 });

            }
        });

        //link to the login page
        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Login.class));
            }
        });








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
        Toast.makeText(SignUp.this, pickedCurrency,
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
