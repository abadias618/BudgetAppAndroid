package com.byui.budgetappandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ViewBudget extends AppCompatActivity {

    Button _returnButton;
    ListView _expenseList;



    private Task<Void> allTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_budget);

        //get list with elements from the db from the previous activity

        List<Expense> expenses = (ArrayList<Expense>) getIntent().getSerializableExtra("listOfExpenses");

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
