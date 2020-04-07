package com.byui.budgetappandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ArrayAdapter;

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

/*Used this @link https://firebase.googleblog.com/2016/10/become-a-firebase-taskmaster-part-4.html
  as a guide to solve the problem of the firebase functions being asynchronous*/
public class SplashScreenActivity extends AppCompatActivity {

    //data-fields
    //using ArrayList instead of List because it makes
    //it easier to pass to the new intent as a parameter
    private ArrayList<Expense> _expenses = new ArrayList<>();
    private int _currentRecordsNumber;
    private String _routeTo;

    //firebase currentUser Id
    private  String _userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    //Database handle
    private DatabaseReference _reference = FirebaseDatabase.getInstance().getReference();

    //tasking remote config

    private Task<Void> fetchTask;

    //tasking realtime database (firebase)
    private TaskCompletionSource<DataSnapshot> dbSource = new TaskCompletionSource<>();
    private Task dbTask = dbSource.getTask();


    //tasking the delay

    private TaskCompletionSource<Void> delaySource = new TaskCompletionSource<>();
    private Task<Void> delayTask = delaySource.getTask();

    //gathering all the threads

    private Task<Void> allTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        /**HERE WE GET THE INTENT TO RE-ROUTE TO THE NET ACTIVITY THAT NEEDS THIS DATA*/

        _routeTo = getIntent().getStringExtra("route");

        /**The Remote Config fetch is easy, because it will give you a Task
         * you can use to listen to the availability your values*/
        fetchTask = FirebaseRemoteConfig.getInstance().fetch();

        /**we can use the TaskCompletionSource to trigger
         * a placeholder task when the data is available*/
        _reference.child("users").child(_userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    dbSource.setResult(dataSnapshot);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                dbSource.setException(databaseError.toException());
                //
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        /**there's the minimum two second delay for the splash screen to stay up.
         * We can also represent that delay as a Task using TaskCompletionSource */

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                delaySource.setResult(null);
            }
        }, 2000);

        /**Now, we have three Tasks, all operating in parallel, and we can use
         * Tasks.whenAll() to create another Task that triggers when they're
         * all successful: */

        allTask = Tasks.whenAll(fetchTask, dbTask, delayTask);
        allTask.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                //this is our data after fetched from the db
                DataSnapshot data = (DataSnapshot) dbTask.getResult();
                //Get the number of records the user currently has to iterate
                _currentRecordsNumber = (int) (long) data.child("record_number").getValue();
                //iterate and build a list to pass in the Intent with all the expenses as Expense objects
                for (int i = 1; i <= _currentRecordsNumber; i++) {
                    Expense expense = data.child("expenses").child("expense_"+i).getValue(Expense.class);
                    _expenses.add(expense);

                }
                //Create and Intent and pass our expenses ArrayList to the _route
                if (_routeTo.equals("ViewBudget")) {
                    Intent myIntent = new Intent(SplashScreenActivity.this, ViewBudget.class);
                    myIntent.putExtra("listOfExpenses", _expenses);
                    startActivity(myIntent);
                }
                else if (_routeTo.equals("Stats")) {
                    Intent myIntent = new Intent(SplashScreenActivity.this, Stats.class);
                    myIntent.putExtra("listOfExpenses", _expenses);
                    startActivity(myIntent);
                }
                else if (_routeTo.equals("Settings")) {
                    Intent myIntent = new Intent(SplashScreenActivity.this, Settings.class);
                    myIntent.putExtra("listOfExpenses", _expenses);
                    startActivity(myIntent);
                }


            }
        });
        allTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                System.out.println("failed sorry!");
            }
        });

    }
}
