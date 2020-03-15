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

public class Login extends AppCompatActivity {

    EditText _emailInput , _passwordInput;
    TextView _loginSubtitle, _emailLabel, _passwordLabel, _signUpTitle, _signupLink;
    Button _submitButton;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        _emailInput = findViewById(R.id.emailInput);
        _passwordInput = findViewById(R.id.passwordInput);
        _loginSubtitle = findViewById(R.id.loginSubtitle);
        _emailLabel = findViewById(R.id.emailLabel);
        _passwordLabel = findViewById(R.id.passwordLabel);
        _signUpTitle = findViewById(R.id.signUpTitle);
        _submitButton = findViewById(R.id.submitButton);
        _signupLink = findViewById(R.id.signupLink);

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
                // authenticate the credentials provided

                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Login.this, "You are logged in!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        }
                        else {
                            Toast.makeText(Login.this, "Wrong Email or Password, try again..."+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SignUp.class));
            }
        });
    }
}
