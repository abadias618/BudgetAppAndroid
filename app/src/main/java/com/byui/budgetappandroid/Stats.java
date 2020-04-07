package com.byui.budgetappandroid;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;

import java.util.ArrayList;
import java.util.List;

public class Stats extends AppCompatActivity {

    AnyChartView anyChartView;
    Button _statsLabel;

    //This data will show in the Pie chart
    String[] months = {"Income", "Expenses", "Debt/Savings"};
    int[] earnings = { 50, 40, 10};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        anyChartView = findViewById(R.id.any_chart_view);
        _statsLabel = findViewById((R.id.statsLabel));
        setupPieChart ();

        //sets up the back space button in the Stats
        _statsLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        });
    }

    // Set up pie method
    public void setupPieChart () {

        Pie pie = AnyChart.pie();
        List<DataEntry> dataEntries = new ArrayList<>();

        //runs the moths and earnings in loops
        for (int i = 0; i < months.length; i++){
            dataEntries.add(new ValueDataEntry(months[i], earnings[i]));
        }

        //uses the PI data method to pass this data interest
        pie.data(dataEntries);
        pie.title("Earnings");
        anyChartView.setChart(pie);
    }


}
