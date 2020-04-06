package com.byui.budgetappandroid;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class GetFromDatabase {

    private static List<Expense> expenses = new ArrayList<>();
    private static int currentRecordsNumber;
    //firebase
    private static String _userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    //Database handle
    private static DatabaseReference reference = FirebaseDatabase.getInstance().getReference();


    public static List<Expense> getExpenseAllRecords() {

        System.out.println("userID: "+_userId);

        reference.child("users").child(_userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    //Get the number of records the user currently has to iterate
                    currentRecordsNumber = (int) (long) dataSnapshot.child("record_number").getValue();
                    System.out.println("num records from function "+currentRecordsNumber);
                    //iterate and build a list to return with all the expenses as Expense objects
                    for (int i = 0; i <= currentRecordsNumber; i++) {
                        Expense expense = dataSnapshot.child("expenses").child("expense_"+i).getValue(Expense.class);
                        expenses.add(expense);
                        System.out.println("record from function "+expenses.toString());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
        System.out.println("view budget expenses from function "+expenses.toString());
        return expenses;
    }

}
