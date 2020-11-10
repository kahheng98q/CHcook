package com.example.chcook.YangJie.GUI.StaffLoginAndManagement;

import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.chcook.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class Fragment_deleteStaff extends Fragment {
    private Spinner spinner,reason;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private ArrayList<String> arrayList = new ArrayList<>();
    private ArrayList<String> idList = new ArrayList<>();
    private CircleImageView checkStaffImage;
    private TextView email,name,status,Id;
    private Button btnDismiss;
    private ProgressBar pg;
    private HashMap<String,String> staff = new HashMap<String,String>();
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_deletestaff, container, false);
        spinner = view.findViewById(R.id.spinnerStaffName);
        reason = view.findViewById(R.id.spinnerReason);
        checkStaffImage = view.findViewById(R.id.checkStaffImage);
        email = view.findViewById(R.id.checkEmail);
        name = view.findViewById(R.id.checkStaffName);
        Id = view.findViewById(R.id.txtId);
        status = view.findViewById(R.id.checkStaffStatus);
        btnDismiss = view.findViewById(R.id.btnDismiss);
        pg = view.findViewById(R.id.progressBarDelete);

        String[] arraySpinner = new String[] {"Resign", "Retired"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity().getBaseContext(), R.layout.style_spinner, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        reason.setAdapter(adapter);

        showStaffNameSpinner();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                pg.setVisibility(view.VISIBLE);
               String username = arrayList.get(position);
               String key = idList.get(position);
               name.setText("Name : "+username);
               Query query = FirebaseDatabase.getInstance().getReference("Staff")
                       .orderByChild("StaffId")
                       .equalTo(key);
               query.addListenerForSingleValueEvent(valueEventListener);
            }
            ValueEventListener valueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                            pg.setVisibility(View.GONE);
                                String semail = snapshot.child("StaffEmail").getValue(String.class);
                                String sstatus = snapshot.child("Status").getValue(String.class);
                                String sImage = snapshot.child("Image").getValue(String.class);
                                String uid = snapshot.child("StaffId").getValue(String.class);
                                email.setText("Email : "+semail);
                                status.setText("Status : "+sstatus);
                                Glide.with(getActivity())
                                        .load(Uri.parse(sImage))
                                        .into(checkStaffImage);
                                Id.setText(uid);

                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btnDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builderR = new AlertDialog.Builder(getActivity());
                builderR.setTitle("Confirm");
                builderR.setMessage("Are you sure want to confirm?");
                builderR.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Query query = FirebaseDatabase.getInstance().getReference("Staff")
                                .child(Id.getText().toString());

                        HashMap hashMap = new HashMap();
                        hashMap.put("Status",reason.getSelectedItem().toString());
                        query.getRef().updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                            @Override
                            public void onSuccess(Object o) {
                                Toast.makeText(getActivity(),"updated",Toast.LENGTH_SHORT).show();
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

    private void showStaffNameSpinner() {
        databaseReference.child("Staff").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayList.clear();
                idList.clear();
                for(DataSnapshot item : dataSnapshot.getChildren()){

                    String status = item.child("Status").getValue(String.class);
                    String position = item.child("StaffType").getValue(String.class);
                    if(status.equals("Working")&&position.equals("Staff")){
                        arrayList.add(item.child("Name").getValue(String.class));
                        idList.add(item.getKey());
                    }

                }
                if(getActivity()!=null) {
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity().getBaseContext(), R.layout.style_spinner, arrayList);
                    spinner.setAdapter(arrayAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
