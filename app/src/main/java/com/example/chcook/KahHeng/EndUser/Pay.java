package com.example.chcook.KahHeng.EndUser;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.braintreepayments.cardform.view.CardForm;
import com.example.chcook.Domain.User;
import com.example.chcook.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class Pay extends AppCompatActivity {
    private CardForm cardForm;
    private Button btnPay;
    private AlertDialog.Builder alertBuilder;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);

        firebaseAuth = FirebaseAuth.getInstance();

        cardForm = findViewById(R.id.card_form);
        btnPay = findViewById(R.id.btnPay);
        cardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .postalCodeRequired(true)
                .mobileNumberRequired(true)
                .mobileNumberExplanation("SMS is required on this number")
                .setup(Pay.this);
        cardForm.getCvvEditText().setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (cardForm.isValid()) {
                    alertBuilder = new AlertDialog.Builder(Pay.this);
                    alertBuilder.setTitle("Confirm before purchase");
                    alertBuilder.setMessage("Card number: " + cardForm.getCardNumber() + "\n" +
                            "Card expiry date: " + cardForm.getExpirationDateEditText().getText() + "\n" +
                            "Card CVV: " + cardForm.getCvv() + "\n" +
                            "Postal code: " + cardForm.getPostalCode() + "\n" +
                            "Phone number: " + cardForm.getMobileNumber());
                    alertBuilder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            Toast.makeText(Pay.this, "Now you are premium user.", Toast.LENGTH_SHORT).show();
                            String uid=firebaseAuth.getCurrentUser().getUid();
                            final FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference ref = database.getReference("Users").child(uid);

                            Map<String, Object> premiumUpdates = new HashMap<>();
                            premiumUpdates.put("type", "Premium");
                            ref.updateChildren(premiumUpdates);

                            startActivity(new Intent(getApplicationContext(),MainPage.class));
                        }
                    });
                    alertBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog alertDialog = alertBuilder.create();
                    alertDialog.show();
                } else {
                    Toast.makeText(Pay.this, "Please Complete the form.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
