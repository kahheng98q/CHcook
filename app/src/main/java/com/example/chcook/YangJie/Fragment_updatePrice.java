package com.example.chcook.YangJie;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.chcook.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class Fragment_updatePrice extends Fragment {
    private TextView cPrice,editor,date;
    private Spinner spinnerPrice;
    private Button btnUp;
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_updateprice, container, false);
        final FirebaseUser currentStaff = FirebaseAuth.getInstance().getCurrentUser();
        cPrice = view.findViewById(R.id.txtCurrentPrice);
        editor = view.findViewById(R.id.txtEditor);
        date = view.findViewById(R.id.txtDate);
        spinnerPrice = view.findViewById(R.id.spinnerPrice);
        btnUp = view.findViewById(R.id.btnUpdate);

        showDetail();
        String[] arraySpinner = new String[] {"10", "11","12", "13","14", "15","16", "17","18","19","20","21","22", "23","24", "25","26", "27","28","29","30","31","32", "33","34", "35","36", "37","38","39","40"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity().getBaseContext(),R.layout.style_spinner, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPrice.setAdapter(adapter);
        btnUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerPrice.getSelectedItem().toString();


                AlertDialog.Builder builderR = new AlertDialog.Builder(getActivity());
                builderR.setTitle("Update Price");
                builderR.setMessage("Are you sure want to update?");
                builderR.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Query query = FirebaseDatabase.getInstance().getReference("ManagePrice");

                        HashMap hashMap = new HashMap();
                        hashMap.put("Price", spinnerPrice.getSelectedItem().toString());
                        hashMap.put("Editor", currentStaff.getDisplayName().toString());
                        hashMap.put("EditTime", ServerValue.TIMESTAMP);
                        query.getRef().updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                            @Override
                            public void onSuccess(Object o) {
                                Toast.makeText(getActivity(), "updated", Toast.LENGTH_SHORT).show();
                                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.replace(R.id.container_staff, new Fragment_updatePrice());
                                fragmentTransaction.commit();
                            }
                        });
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

        });
        return view;
    }



    private void showDetail() {
        Query query = FirebaseDatabase.getInstance().getReference("ManagePrice");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String emailEditor = dataSnapshot.child("Editor").getValue(String.class);
                String getPrice = dataSnapshot.child("Price").getValue(String.class);
                Long getDate = dataSnapshot.child("EditTime").getValue(Long.class);

                SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                String latestDate = df.format(getDate);

                date.setText("Last Edit Time : "+latestDate);
                editor.setText("Last Editor : "+emailEditor);
                cPrice.setText("Current Price : RM "+getPrice);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
