package com.example.chcook.KahHeng.EndUser;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.braintreepayments.cardform.view.CardForm;
import com.example.chcook.KahHeng.EndUser.DA.UserDA;
import com.example.chcook.R;
import com.google.firebase.auth.FirebaseAuth;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.math.BigDecimal;

public class Pay extends AppCompatActivity {
    private CardForm cardForm;
    private Button btnPay;
    private AlertDialog.Builder alertBuilder;
    private FirebaseAuth firebaseAuth;
    private String price="15";
    public static final int PAYPAL_REQUEST_CODE=7171;
    private static  final String PAYPAL_CLIANT_ID="AavoT6futTDug1UjDqQb2SNhxcv4es037vbEHN03rIq4i2J-pMqjd3h2leejKaimovIr85qipFkIVttc";
    private static PayPalConfiguration config=new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(PAYPAL_CLIANT_ID);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);

        firebaseAuth = FirebaseAuth.getInstance();

        Intent intent=new Intent(this,PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config);
        startService(intent);

        cardForm = findViewById(R.id.card_form);
        btnPay = findViewById(R.id.btnPay);

        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makePayment();
            }
        });
//        cardForm.cardRequired(true)
//                .expirationRequired(true)
//                .cvvRequired(true)
//                .postalCodeRequired(true)
//                .mobileNumberRequired(true)
//                .mobileNumberExplanation("SMS is required on this number")
//                .setup(Pay.this);
//        cardForm.getCvvEditText().setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
//        btnPay.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (cardForm.isValid()) {
//                    alertBuilder = new AlertDialog.Builder(Pay.this);
//                    alertBuilder.setTitle("Confirm before purchase");
//                    alertBuilder.setMessage("Card number: " + cardForm.getCardNumber() + "\n" +
//                            "Card expiry date: " + cardForm.getExpirationDateEditText().getText() + "\n" +
//                            "Card CVV: " + cardForm.getCvv() + "\n" +
//                            "Postal code: " + cardForm.getPostalCode() + "\n" +
//                            "Phone number: " + cardForm.getMobileNumber());
//                    alertBuilder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.dismiss();
//                            Toast.makeText(Pay.this, "Now you are premium user.", Toast.LENGTH_SHORT).show();
//                            String uid=firebaseAuth.getCurrentUser().getUid();
//                            final FirebaseDatabase database = FirebaseDatabase.getInstance();
//                            DatabaseReference ref = database.getReference("Users").child(uid);
//
//                            Map<String, Object> premiumUpdates = new HashMap<>();
//                            premiumUpdates.put("type", "Premium");
//                            ref.updateChildren(premiumUpdates);
//
//                            startActivity(new Intent(getApplicationContext(),MainPage.class));
//                        }
//                    });
//                    alertBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.dismiss();
//                        }
//                    });
//                    AlertDialog alertDialog = alertBuilder.create();
//                    alertDialog.show();
//                } else {
//                    Toast.makeText(Pay.this, "Please Complete the form.", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
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
                    Toast.makeText(this, "Your have becomce Premium User", Toast.LENGTH_SHORT).show();
                    UserDA userDA=new UserDA();
                    userDA.addPremiumStatus();
                    startActivity(new Intent(getApplicationContext(),MainPage.class));
                }
            }
        }
    }

    private void makePayment(){
        PayPalPayment payPalPayment=new PayPalPayment(new BigDecimal(String.valueOf(price)),"MYR","Make Pay For Premium",PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent=new Intent(this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT,payPalPayment);
        startActivityForResult(intent,PAYPAL_REQUEST_CODE);

    }
}
