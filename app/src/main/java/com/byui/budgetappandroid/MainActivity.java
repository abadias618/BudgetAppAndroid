package com.byui.budgetappandroid;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;


public class MainActivity extends AppCompatActivity {

    //link buttons
    Button _newExpense;
    Button _settings;
    Button _viewBudget;
    Button _stats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _newExpense = findViewById(R.id.newExpenseButton);
        _settings = findViewById(R.id.settings);
        _viewBudget = findViewById(R.id.viewBudgetButton);
        _stats = findViewById(R.id.statsButton);

        _newExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), NewExpense.class));
            }
        });

        _settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Settings.class));
            }
        });

        _viewBudget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SplashScreenActivity.class);
                intent.putExtra("route","ViewBudget");
                startActivity(intent);
            }
        });

        _stats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Stats.class));
            }
        });
    }

    //when the app is destroyed it automatically signs you out
    @Override
    protected void onDestroy() {
        super.onDestroy();
        FirebaseAuth.getInstance().signOut();
    }

    //david push test
    //rachel push test 2.0
}
