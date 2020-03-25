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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
//import org.json.simple.JSONObject;
import android.view.View;
import com.google.android.gms.tasks.OnCompleteListener;


public class Settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //Retrieve any input from the "currency" field
        final EditText _currencyInput = findViewById(R.id.currencyInput);
        final TextView _submitButton = findViewById(R.id.submit);

        //The user's login info
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        //If the user's not logged in, go back to the login page. Otherwise, continue.
        if (firebaseAuth.getCurrentUser() == null) {
            startActivity((new Intent(getApplicationContext(), Login.class)));
            finish();
        }

        _submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String currency = _currencyInput.getText().toString().trim();

                //Make sure the currency input field is not empty
                if (TextUtils.isEmpty(currency)) {
                    Toast.makeText(Settings.this, "User Created Successfully",
                            Toast.LENGTH_SHORT).show();
                }

                //Call API
            }
        });

  //Cannot work until JSONObject problem is resolved. I (Rachel), am working on it
        /*
        public static void main(String[] args) {
            JSONObject obj = new JSONObject();

            obj.put("name", "foo");
            obj.put("num", new Integer(100));
            obj.put("balance", new Double(1000.21));
            obj.put("is_vip", new Boolean(true));

            System.out.print(obj);
        }
*/

    }

}
