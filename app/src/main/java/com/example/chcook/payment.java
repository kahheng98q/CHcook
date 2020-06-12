package com.example.chcook;


import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.braintreepayments.cardform.view.CardForm;


/**
 * A simple {@link Fragment} subclass.
 */
public class payment extends Fragment {
CardForm cardForm;
Button pay;
AlertDialog.Builder alertBuilder;

    public payment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_payment, container, false);

        return inflater.inflate(R.layout.fragment_payment, container, false);
    }

}
