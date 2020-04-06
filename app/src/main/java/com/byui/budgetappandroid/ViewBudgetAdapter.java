package com.byui.budgetappandroid;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ViewBudgetAdapter extends ArrayAdapter<Expense> {

    public ViewBudgetAdapter (Context context, List<Expense> expenses) {
        super(context,0,expenses);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItemView = convertView;

        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.expense_item, parent, false
            );
        }
        Expense currentExpense = getItem(position);
        TextView numberView = listItemView.findViewById(R.id.recordNumber);
        numberView.setText(currentExpense.getId());

        TextView nameView = listItemView.findViewById(R.id.recordName);
        nameView.setText(currentExpense.getName());

        TextView dateView = listItemView.findViewById(R.id.recordDate);
        dateView.setText(currentExpense.getDate());

        TextView amountView = listItemView.findViewById(R.id.recordAmount);
        amountView.setText(String.valueOf(currentExpense.getAmount()));

        TextView categoryView = listItemView.findViewById(R.id.recordCategory);
        categoryView.setText(currentExpense.getCategory());

        return listItemView;

    }
}
