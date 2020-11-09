package com.example.chcook.YangJie.GUI.Payment;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.chcook.Domain.Payment;
//import com.example.chcook.KahHeng.EndUser.Pay;
import com.example.chcook.R;
import com.example.chcook.YangJie.GUI.StaffMainPage;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class showIncomeStatus extends AppCompatActivity implements View.OnClickListener {

    private String position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_income_status);
        Button back = findViewById(R.id.btnShowIncomeBack);
        TextView title = findViewById(R.id.txtIncomeTitle);
        final PieChart pieChart = findViewById(R.id.IncomePieChart);
        ArrayList<Payment> pay = new ArrayList<>();
        String year = null;
        final Bundle extras = getIntent().getExtras();
        year = extras.getString("year");
        position = extras.getString("position");
        title.setText("Income Status in "+year);
        back.setOnClickListener(this);


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Payment");
        final String finalYear = year;
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Integer tM1 = 0, tM2 = 0, tM3 = 0, tM4 = 0, tM5 = 0, tM6 = 0, tM7 = 0, tM8 = 0, tM9 = 0, tM10 = 0, tM11 = 0, tM12 = 0;
                Boolean M1 = false, M2 = false, M3 = false, M4 = false, M5 = false, M6 = false, M7 = false, M8 = false, M9 = false, M10 = false, M11 = false, M12 = false;

                if (dataSnapshot.exists()) {
                    for (DataSnapshot income : dataSnapshot.getChildren()) {
                        Long dd = income.child("PayDate").getValue(Long.class);
                        SimpleDateFormat df = new SimpleDateFormat("yyyy");
                        String latestDate = df.format(dd);
                        if (latestDate.equals(finalYear)) {
                            Long dateMonth = income.child("PayDate").getValue(Long.class);
                            SimpleDateFormat dM = new SimpleDateFormat("MM");
                            Integer price = income.child("Price").getValue(Integer.class);
                            String month = dM.format(dd);
                            switch (month) {
                                case "01":
                                    tM1 = tM1 + price;
                                    M1 = true;
                                    break;
                                case "02":
                                    tM2 = tM2 + price;
                                    M2 = true;
                                    break;
                                case "03":
                                    tM3 = tM3 + price;
                                    M3 = true;
                                    break;
                                case "04":
                                    tM4 = tM4 + price;
                                    M4 = true;
                                    break;
                                case "05":
                                    tM5 = tM5 + price;
                                    M5 = true;
                                    break;
                                case "06":
                                    tM6 = tM6 + price;
                                    M6 = true;
                                    break;
                                case "07":
                                    tM7 = tM7 + price;
                                    M7 = true;
                                    break;
                                case "08":
                                    tM8 = tM8 + price;
                                    M8 = true;
                                    break;
                                case "09":
                                    tM9 = tM9 + price;
                                    M9 = true;
                                    break;
                                case "10":
                                    tM10 = tM10 + price;
                                    M10 = true;
                                    break;
                                case "11":
                                    tM11 = tM11 + price;
                                    M11 = true;
                                    break;
                                case "12":
                                    tM12 = tM12 + price;
                                    M12 = true;
                                    break;
                            }
                        }
                    }
                    ArrayList<PieEntry> income = new ArrayList<>();
                    if (M1.equals(true)) {
                        income.add(new PieEntry(tM1, "January"));
                    }
                    if (M2.equals(true)) {
                        income.add(new PieEntry(tM2, "February"));
                    }
                    if (M3.equals(true)) {
                        income.add(new PieEntry(tM3, "March"));
                    }
                    if (M4.equals(true)) {
                        income.add(new PieEntry(tM4, "April"));
                    }
                    if (M5.equals(true)) {
                        income.add(new PieEntry(tM5, "May"));
                    }
                    if (M6.equals(true)) {
                        income.add(new PieEntry(tM6, "June"));
                    }
                    if (M7.equals(true)) {
                        income.add(new PieEntry(tM7, "July"));
                    }
                    if (M8.equals(true)) {
                        income.add(new PieEntry(tM8, "August"));
                    }
                    if (M9.equals(true)) {
                        income.add(new PieEntry(tM9, "September"));
                    }
                    if (M10.equals(true)) {
                        income.add(new PieEntry(tM10, "October"));
                    }
                    if (M11.equals(true)) {
                        income.add(new PieEntry(tM11, "November"));
                    }
                    if (M12.equals(true)) {
                        income.add(new PieEntry(tM12, "December"));
                    }


                    PieDataSet pieDataSet = new PieDataSet(income,"");
                    pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                    pieDataSet.setValueTextColor(Color.BLACK);
                    pieDataSet.setValueTextSize(18f);

                    PieData pieData = new PieData(pieDataSet);

                    pieChart.setData(pieData);

                    pieChart.getDescription().setText("*All the number are in RM currency");
                    pieChart.setCenterText("Income in "+finalYear+" (RM)");
                    pieChart.animate();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    @Override
    public void onClick(View v) {
        AlertDialog.Builder builderR = new AlertDialog.Builder(showIncomeStatus.this);

        builderR.setTitle("Back");
        builderR.setMessage("Are you sure want to back?");
        builderR.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(getApplicationContext(), StaffMainPage.class);
                        intent.putExtra("page", "income");
                        intent.putExtra("position",position);
                startActivity(intent);
            }
        });
        builderR.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog dialogR = builderR.create();
        dialogR.show();
    }
}
