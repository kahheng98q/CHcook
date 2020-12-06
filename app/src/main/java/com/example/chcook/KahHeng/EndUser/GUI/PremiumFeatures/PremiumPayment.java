package com.example.chcook.KahHeng.EndUser.GUI.PremiumFeatures;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.braintreepayments.cardform.view.CardForm;
import com.example.chcook.KahHeng.EndUser.DA.UserDA;
import com.example.chcook.KahHeng.EndUser.GUI.MainPage;
import com.example.chcook.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class PremiumPayment extends AppCompatActivity {
    private Button btnPay;
    private TextView txtPrice;
    private AlertDialog.Builder alertBuilder;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;

    private String price="";
    public static final int PAYPAL_REQUEST_CODE=7171;
    private static  final String PAYPAL_CLIANT_ID="AavoT6futTDug1UjDqQb2SNhxcv4es037vbEHN03rIq4i2J-pMqjd3h2leejKaimovIr85qipFkIVttc";
    private static PayPalConfiguration config=new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(PAYPAL_CLIANT_ID);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        connectFirebase();
        getPrice();
//        firebaseAuth = FirebaseAuth.getInstance();

        Intent intent=new Intent(this,PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config);
        startService(intent);
        txtPrice=findViewById(R.id.txtPrice);
//        textView.setText("");

        btnPay = findViewById(R.id.btnPay);

        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makePayment();
            }
        });

    }

    @Override
    protected void onDestroy() {
        stopService(new Intent(this,PayPalService.class));
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PAYPAL_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirmation != null) {
                    Toast.makeText(this, "Your have become Premium User", Toast.LENGTH_SHORT).show();
                    setPaymentRecord(price);
                    UserDA userDA=new UserDA();
                    userDA.addPremiumStatus();
                    startActivity(new Intent(getApplicationContext(), MainPage.class));
                }
            }
        }
    }

    private void makePayment(){
        PayPalPayment payPalPayment=new PayPalPayment(new BigDecimal(String.valueOf(price)),
                "MYR","Make PremiumPayment For Premium",PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent=new Intent(this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT,payPalPayment);
        startActivityForResult(intent,PAYPAL_REQUEST_CODE);

    }

    private void getPrice(){
//        DatabaseReference ref = database.getReference("Users").limitToLast(3);
        DatabaseReference priceRef = database.getReference("ManagePrice");
        priceRef.orderByChild("EditTime").limitToLast(1).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.exists()) {
                    price = dataSnapshot.child("Price").getValue(String.class);
                    txtPrice.setText("Your only need to pay RM "+price);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void connectFirebase() {
        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
    }

    public void setPaymentRecord(String price) {
        String uid = firebaseAuth.getCurrentUser().getUid();
        DatabaseReference ref = database.getReference("Payment").push();
        Map<String, Object> addPay = new HashMap<>();
        addPay.put("Date", getCurrentTimeStamp());
        addPay.put("Price", price);
        addPay.put("UserID",firebaseAuth.getUid());
        ref.updateChildren(addPay);
    }
    private long getCurrentTimeStamp() {
        long timestamp = System.currentTimeMillis() / 1000;
        return timestamp;
    }
}
