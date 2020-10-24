package com.example.chcook.KahHeng.EndUser;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.chcook.KahHeng.EndUser.DA.HistoryAdapter;
import com.example.chcook.KahHeng.EndUser.DA.HistoryDA;
import com.example.chcook.KahHeng.EndUser.Domain.Histories;
import com.example.chcook.R;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryUI extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private View view;
    private ProgressBar progressBar;
    private ArrayList<Histories> tmpH;
    private HistoryDA historyDA;
//    private Videos video;

    public HistoryUI() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_history, container, false);

        tmpH= new ArrayList<>();
        progressBar = view.findViewById(R.id.progressBarHis);
        historyDA = new HistoryDA();
        progressBar.setVisibility(View.VISIBLE);
        historyDA.getHistoryRealData(new HistoryDA.HisCallback() {
            @Override
            public ArrayList<Histories> onCallback(ArrayList<Histories> histories) {
                tmpH=histories;
                recyclerView = view.findViewById(R.id.HistoryRecyclevView);
                recyclerView.setHasFixedSize(true);
                adapter = new HistoryAdapter(getContext(), tmpH);
                layoutManager = new LinearLayoutManager(getActivity());
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);

                adapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
                return histories;

            }
        });


        getActivity().setTitle("Histories");
        return view;
    }
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.searchbar, menu);
        super.onCreateOptionsMenu(menu, inflater);

    }

}
