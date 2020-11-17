package com.example.chcook.YangJie.GUI.Payment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.chcook.Domain.Payment;
import com.example.chcook.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class Fragment_ShowIncome extends Fragment {
    private Spinner year;
    private Boolean gotRecord=false;
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_showincome, container, false);
        Button showStatus = view.findViewById(R.id.btnShow);
        final Bundle argument = getArguments();

        year = view.findViewById(R.id.incomeStatusYear);
//        Button set = view.findViewById(R.id.button3);
        String position="";
        final ArrayList<Payment> pay = new ArrayList<>();

//        final Bundle extras =getArguments();


//        String[] month = new String[] {"January","February","March","April","May","June","July","August","September","October","November","December"};
        String[] arrayYear = new String[] {"2020","2021","2022","2023","2024","2025","2026","2027","2028","2029","2030","2031"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity().getBaseContext(),R.layout.style_spinner, arrayYear);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        year.setAdapter(adapter);



//                Query query = FirebaseDatabase.getInstance().getReference("Payment");
//
//                Map<String,Object> hashMap = new HashMap();
//
//
//                    hashMap.put("Price",15);
//                    hashMap.put("PayDate", ServerValue.TIMESTAMP);
//                    hashMap.put("UserId","-MJhCRYpRTBdxGd1Mv73");
//
//
//                query.getRef().push().setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        Toast.makeText(getContext(),"succss",Toast.LENGTH_SHORT).show();
//                    }
//                });



        final String finalPosition = position;
        showStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Payment");

                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()) {
//                            Integer abc = 0;
                            for (DataSnapshot income : dataSnapshot.getChildren()) {
                                Long dd = income.child("Date").getValue(Long.class);

                                if(getDate(dd).equals(year.getSelectedItem().toString())){
                                    gotRecord=true;
                                    Intent intent = new Intent(getActivity(), showIncomeStatus.class);
                                    intent.putExtra("year",year.getSelectedItem().toString());
                                    intent.putExtra("position", argument.getString("position"));
                                    startActivity(intent);
                                }

                            }
                        }
                        if(gotRecord.equals(false)){
                            Toast.makeText(getActivity(),"No record found",Toast.LENGTH_SHORT).show();
                        }
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });
        return view;
    }
    private String getDate(Long timeStamp) {
        Calendar cal = Calendar.getInstance(Locale.getDefault());
        cal.setTimeInMillis(timeStamp * 1000);
        android.text.format.DateFormat df = new android.text.format.DateFormat();
        String date = df.format("yyyy", cal).toString();
        return date;
    }
}
