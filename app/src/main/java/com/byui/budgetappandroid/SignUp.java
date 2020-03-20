package com.byui.budgetappandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUp extends AppCompatActivity {

    EditText _emailInput , _passwordInput;
    TextView _signUpTitle, _loginSubtitle, _emailLabel, _passwordSubtitle, _loginLink;
    Button _submitButton;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        _emailInput = findViewById(R.id.emailInput);
        _passwordInput = findViewById(R.id.passwordInput);
        _signUpTitle = findViewById(R.id.signUpTitle);
        _loginSubtitle = findViewById(R.id.loginSubtitle);
        _emailLabel = findViewById(R.id.emailLabel);
        _passwordSubtitle = findViewById(R.id.passwordLabel);
        _loginLink = findViewById(R.id.loginLink);
        _submitButton = findViewById(R.id.submitButton);

        firebaseAuth = FirebaseAuth.getInstance();
        //if user is logged in re-route to main activity

        if (firebaseAuth.getCurrentUser() != null) {
            startActivity((new Intent(getApplicationContext(), MainActivity.class)));
            finish();
        }
        // listener for the SUBMIT button
        _submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = _emailInput.getText().toString().trim();
                String password = _passwordInput.getText().toString().trim();

                //check if the input fields are not empty
                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) ) {
                    _emailInput.setError("You are missing something...");
                }
                //create a user on the firebase
                 firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                     @Override
                     public void onComplete(@NonNull Task<AuthResult> task) {
                         if (task.isSuccessful()) {
                             Toast.makeText(SignUp.this, "User Created Successfully",
                                     Toast.LENGTH_SHORT).show();
                             startActivity(new Intent(getApplicationContext(), MainActivity.class));
                         }
                         else {
                             Toast.makeText(SignUp.this, "An Error Occurred"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                         }
                     }
                 });

            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Login.class));
            }
        });

    }
}