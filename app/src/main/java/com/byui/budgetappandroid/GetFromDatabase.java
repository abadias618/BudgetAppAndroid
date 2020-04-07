package com.byui.budgetappandroid;

import android.content.Intent;
import android.os.Handler;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import java.util.ArrayList;
import java.util.List;

public class GetFromDatabase {

    //data-fields
    private List<Expense> _expenses = new ArrayList<>();
    private int _currentRecordsNumber;
    //firebase
    private  String _userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    //Database handle
    private DatabaseReference _reference = FirebaseDatabase.getInstance().getReference();

    //2
    private TaskCompletionSource<DataSnapshot> dbSource = new TaskCompletionSource<>();
    private Task dbTask = dbSource.getTask();

    //1

    private Task<Void> fetchTask;

    //3

    private TaskCompletionSource<Void> delaySource = new TaskCompletionSource<>();
    private Task<Void> delayTask = delaySource.getTask();

    //4

    private Task<Void> allTask;

    public List<Expense> getExpenseAllRecords() {

        //1
        // during onCreate:
        fetchTask = FirebaseRemoteConfig.getInstance().fetch();

        //2
        _reference.child("users").child(_userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    dbSource.setResult(dataSnapshot);
                    //

                    //Get the number of records the user currently has to iterate
                    _currentRecordsNumber = (int) (long) dataSnapshot.child("record_number").getValue();
                    System.out.println("num records from function "+_currentRecordsNumber);
                    //iterate and build a list to return with all the expenses as Expense objects
                    for (int i = 0; i <= _currentRecordsNumber; i++) {
                        Expense expense = dataSnapshot.child("expenses").child("expense_"+i).getValue(Expense.class);
                        _expenses.add(expense);
                        System.out.println("record from function "+_expenses.toString());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                dbSource.setException(databaseError.toException());
                //
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        //3

        // during onCreate:
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                delaySource.setResult(null);
            }
        }, 2000);

        // 4



        System.out.println("view budget expenses from function "+_expenses.toString());
        return _expenses;
    }

}
