package com.byui.budgetappandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class NewExpense extends AppCompatActivity {

    //xml input
    EditText _expenseName;
    EditText _expenseDate;
    EditText _expenseAmount;
    Spinner _categorySpinner;
    Button _submitExpense;

    //firebase
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_expense);

        // Spinner element
        Spinner mySpinner = findViewById(R.id.categorySpinner);


        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(NewExpense.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.categories));

        // specify adapter to dropdown list
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //This line allows the adapter to show the adapter inside the spinner.
        mySpinner.setAdapter(myAdapter);

        //get values

        _expenseName = findViewById(R.id.expenseName);
        _expenseDate = findViewById(R.id.expenseDate);
        _expenseAmount = findViewById(R.id.expenseAmount);
        _categorySpinner = findViewById(R.id.categorySpinner);
        _submitExpense = findViewById(R.id.submitExpense);

        //Database handle
        reference = FirebaseDatabase.getInstance().getReference();

        String expenseName = _expenseName.toString().trim();
        String expenseDate = _expenseDate.toString().trim();
        Float expenseAmount = Float.valueOf(_expenseAmount.toString());
        String categorySpinner = _categorySpinner.toString().trim();

        //create an object to hold all those values
        final Expense expense = new Expense();
        expense.setDate(expenseDate);
        expense.setName(expenseName);
        expense.setAmount(expenseAmount);
        expense.setCategory(categorySpinner);


        //insert values into database when "Record" button is pressed

        _submitExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                reference.child("users").child(userId).child("expense").setValue(expense);
            }
        });



    }

}
