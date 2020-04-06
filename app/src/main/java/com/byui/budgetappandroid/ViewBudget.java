package com.byui.budgetappandroid;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;


import java.util.ArrayList;
import java.util.List;

public class ViewBudget extends AppCompatActivity {

    Button _returnButton;
    ListView _expenseList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_budget);

        //get list with elements from the db

        GetFromDatabase getFromDatabase = new GetFromDatabase();
        List<Expense> expenses = new ArrayList<>(getFromDatabase.getExpenseAllRecords());

        System.out.println("view budget expenses"+expenses.toString());

        //get fields from xml
        _expenseList = findViewById(R.id.expenseList);
        _returnButton = findViewById(R.id.returnFromViewBudget);


        ViewBudgetAdapter adapter = new ViewBudgetAdapter(ViewBudget.this, expenses);
        _expenseList.setAdapter(adapter);

        _returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        });

    }
}
