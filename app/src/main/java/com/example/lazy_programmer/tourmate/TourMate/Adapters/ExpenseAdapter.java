package com.example.lazy_programmer.tourmate.TourMate.Adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lazy_programmer.tourmate.R;

import java.util.ArrayList;

/**
 * Created by Lazy-Programmer on 10/24/2017.
 */

public class ExpenseAdapter extends ArrayAdapter<ExpenseModel> {

    private Context context;
    private ArrayList<ExpenseModel> expenses;

    public ExpenseAdapter(Context context, ArrayList<ExpenseModel> expenses) {
        super(context, R.layout.tourmate_expense_row_design, expenses);
        this.context=context;
        this.expenses = expenses;
    }
    private static class ViewHolder{
        TextView currentTimeTv,expenseNameTv,expenseCostTv;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if(convertView==null){
            LayoutInflater layoutInflater=LayoutInflater.from(context);
            convertView=layoutInflater.inflate(R.layout.tourmate_expense_row_design,parent,false);
            viewHolder=new ViewHolder();
            viewHolder.currentTimeTv= (TextView) convertView.findViewById(R.id.expenseDateTimeTv);
            viewHolder.expenseNameTv= (TextView) convertView.findViewById(R.id.expenseDetailsTv);
            viewHolder.expenseCostTv= (TextView) convertView.findViewById(R.id.expenseCostTv);
            convertView.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) convertView.getTag();
        }
        viewHolder.currentTimeTv.setText(expenses.get(position).getTime());
        viewHolder.expenseNameTv.setText(expenses.get(position).getExpenseName());
        viewHolder.expenseCostTv.setText(expenses.get(position).getExpenseCost());



        return convertView;
    }
}
