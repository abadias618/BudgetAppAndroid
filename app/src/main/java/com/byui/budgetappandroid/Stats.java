package com.byui.budgetappandroid;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Stats extends AppCompatActivity {

    //firebase
    DatabaseReference reference;
    double balance;
    String _userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    //Database handle


    AnyChartView anyChartView;
    Button _statsLabel;
    TextView _balanceAmount;

    //This data will show in the Pie chart
    String[] months = {"Income", "Expenses", "Debt/Savings"};
    int[] earnings = { 50, 40, 10};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        anyChartView = findViewById(R.id.any_chart_view);
        _statsLabel = findViewById((R.id.statsLabel));
        //get list with elements from the db from the previous activity

        final List<Expense> expensesFromDb = (ArrayList<Expense>) getIntent().getSerializableExtra("listOfExpenses");
        //
        reference = FirebaseDatabase.getInstance().getReference();

        reference.child("users").child(_userId).child("balance").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    balance = (double)(long) dataSnapshot.getValue();
                    System.out.println("balance leaving the snap:"+balance);
                    Pie pie = AnyChart.pie();
                    List<DataEntry> dataEntries = new ArrayList<>();
                    //setupPieChart(expensesFromDb, balance);

                    double homeCat = getExpenseCategorySum("Home", expensesFromDb);
                    double healthCat = getExpenseCategorySum("Health", expensesFromDb);
                    double transportCat = getExpenseCategorySum("Transport", expensesFromDb);
                    double foodCat = getExpenseCategorySum("Food", expensesFromDb);
                    double groceriesCat = getExpenseCategorySum("Groceries", expensesFromDb);
                    double donationCat = getExpenseCategorySum("Donation", expensesFromDb);
                    //double incomeCat = getExpenseCategorySum("Income", expenseList);
                    double otherCat = getExpenseCategorySum("Other", expensesFromDb);


                    //get the balance amount
                    System.out.println("balance before: "+balance);
                    balance = balance - (homeCat+healthCat+transportCat+foodCat+groceriesCat+donationCat+otherCat);
                    System.out.println("balance after: "+balance);
                    // set fields
                    dataEntries.add(new ValueDataEntry("Home", homeCat));
                    dataEntries.add(new ValueDataEntry("Health", healthCat));
                    dataEntries.add(new ValueDataEntry("Transport", transportCat));
                    dataEntries.add(new ValueDataEntry("Food", foodCat));
                    dataEntries.add(new ValueDataEntry("Groceries", groceriesCat));
                    dataEntries.add(new ValueDataEntry("Donation", donationCat));
                    dataEntries.add(new ValueDataEntry("Remaining Income", balance));
                    dataEntries.add(new ValueDataEntry("Other", otherCat));
                    //uses the PI data method to pass this data interest
                    pie.data(dataEntries);
                    pie.title("Budget Stats");
                    anyChartView.setChart(pie);

                    _balanceAmount = findViewById(R.id.balanceAmount);
                    _balanceAmount.setText(String.valueOf(balance));

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });



        //sets up the back space button in the Stats
        _statsLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        });


    }


    private double getExpenseCategorySum (String category, List<Expense> expenses){

        List<Double> elementsToSum = new ArrayList<>();
        if (category.equals("Home")) {
            for (int i = 0; i < expenses.size(); i++) {
                if (expenses.get(i).getCategory().equals("Home")) {
                    elementsToSum.add(expenses.get(i).getAmount());
                }
            }
        }
        else if (category.equals("Health")) {
            for (int i = 0; i < expenses.size(); i++) {
                if (expenses.get(i).getCategory().equals("Health")) {
                    elementsToSum.add(expenses.get(i).getAmount());
                }
            }

        }
        else if (category.equals("Transport")) {
            for (int i = 0; i < expenses.size(); i++) {
                if (expenses.get(i).getCategory().equals("Transport")) {
                    elementsToSum.add(expenses.get(i).getAmount());
                }
            }

        }
        else if (category.equals("Food")) {
            for (int i = 0; i < expenses.size(); i++) {
                if (expenses.get(i).getCategory().equals("Food")) {
                    elementsToSum.add(expenses.get(i).getAmount());
                }
            }

        }
        else if (category.equals("Groceries")) {
            for (int i = 0; i < expenses.size(); i++) {
                if (expenses.get(i).getCategory().equals("Groceries")) {
                    elementsToSum.add(expenses.get(i).getAmount());
                }
            }

        }
        else if (category.equals("Donation")) {
            for (int i = 0; i < expenses.size(); i++) {
                if (expenses.get(i).getCategory().equals("Donation")) {
                    elementsToSum.add(expenses.get(i).getAmount());
                }
            }

        }
        else if (category.equals("Income")) {
            for (int i = 0; i < expenses.size(); i++) {
                if (expenses.get(i).getCategory().equals("Income")) {
                    elementsToSum.add(expenses.get(i).getAmount());
                }
            }

        }
        else if (category.equals("Other")) {
            for (int i = 0; i < expenses.size(); i++) {
                if (expenses.get(i).getCategory().equals("Other")) {
                    elementsToSum.add(expenses.get(i).getAmount());
                }
            }

        }
        //add all the elements of the list
        double sum = 0;
        for (int i = 0; i < elementsToSum.size() ; i++) {
            sum += elementsToSum.get(i);
        }

        return sum;
    }


}
