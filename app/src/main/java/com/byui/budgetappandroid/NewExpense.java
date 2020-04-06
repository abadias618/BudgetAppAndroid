package com.byui.budgetappandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;



public class NewExpense extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    //xml input
    EditText _expenseName;
    EditText _expenseDate;
    EditText _expenseAmount;
    //you get the spinner value from a function so you don't need a Spinner element
    String _categorySpinner;
    Button _submitExpense;

    //firebase
    DatabaseReference reference;
    int recordNumber;
    String _userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_expense);

        //SPINNER
        // Spinner element
        Spinner mySpinner = findViewById(R.id.categorySpinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(NewExpense.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.categories));
        // specify adapter to dropdown list
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //This line allows the adapter to show the adapter inside the spinner.
        mySpinner.setAdapter(myAdapter);
        //check changes in the spinner
        mySpinner.setOnItemSelectedListener(this);


        //get values
        _expenseName = findViewById(R.id.expenseName);
        _expenseDate = findViewById(R.id.expenseDate);
        _expenseAmount = findViewById(R.id.expenseAmount);
        _submitExpense = findViewById(R.id.submitExpense);

        //Database handle
        reference = FirebaseDatabase.getInstance().getReference();

        //Get number of item from database

        reference.child("users").child(_userId).child("record_number").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    recordNumber = (int) (long) dataSnapshot.getValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //insert values into database when "Record" button is pressed

        _submitExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //set values
                String expenseName = _expenseName.getText().toString().trim();
                String expenseDate = _expenseDate.getText().toString().trim();
                double expenseAmount = Float.parseFloat(_expenseAmount.getText().toString().trim());
                //round the float to 2 decimals
                expenseAmount = Math.round(expenseAmount*100.0)/100.0;

                //create an object to hold all those values
                Expense expense = new Expense();
                expense.setDate(expenseDate);
                expense.setName(expenseName);
                expense.setAmount(expenseAmount);
                //TODO: this might be a problem if the user clicks on the button before selecting something in the Spinner
                expense.setCategory(_categorySpinner);

                //insert object into Firebase
                reference.child("users").child(_userId).child("expense_"+(++recordNumber)).setValue(expense);
                Toast.makeText(NewExpense.this, "Inserted Successfully...", Toast.LENGTH_SHORT).show();
                //store the updated record_number in firebase
                reference.child("users").child(_userId).child("record_number").setValue(recordNumber);

                //reset the fields
                _expenseName.getText().clear();
                _expenseDate.getText().clear();
                _expenseAmount.getText().clear();
            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        _categorySpinner = parent.getItemAtPosition(position).toString();
        //Toast.makeText(parent.getContext(), _categorySpinner, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
