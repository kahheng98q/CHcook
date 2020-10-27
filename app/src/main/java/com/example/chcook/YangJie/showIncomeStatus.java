package com.example.chcook.YangJie;

import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chcook.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class showIncomeStatus extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_income_status);
        PieChart pieChart = findViewById(R.id.IncomePieChart);
        ArrayList<PieEntry> income = new ArrayList<>();
        income.add(new PieEntry(500,"Jan","123"));
        income.add(new PieEntry(600,"Feb"));
        income.add(new PieEntry(700,"Mar"));
        income.add(new PieEntry(800,"Apr"));
        income.add(new PieEntry(900,"May"));

        PieDataSet pieDataSet = new PieDataSet(income,"Income");
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieDataSet.setValueTextColor(Color.BLACK);
        pieDataSet.setValueTextSize(16f);

        PieData pieData = new PieData(pieDataSet);

        pieChart.setData(pieData);
        pieChart.getDescription().setEnabled(false);
        pieChart.setCenterText("Income");
        pieChart.animate();
    }
}
