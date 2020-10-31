package com.example.chcook.KahHeng.EndUser;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.SearchView;

import com.example.chcook.KahHeng.EndUser.DA.HomeAdapter;
import com.example.chcook.Domain.Videos;
import com.example.chcook.KahHeng.EndUser.DA.VideoDA;
import com.example.chcook.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class Home extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private View view;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;
    private ArrayList<Videos> videos;
    private ProgressBar progressBar;
    private VideoDA videoDA=new VideoDA();

    public Home() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ArrayAdapter<String> ad;
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_home, container, false);
        database = FirebaseDatabase.getInstance();
        videos =new ArrayList<>();
        progressBar=view.findViewById(R.id.progressBarHome);
        progressBar.setVisibility(View.VISIBLE);

        videoDA.getAllVidoaInHome(new VideoDA.VideoCallback() {
            @Override
            public ArrayList<Videos> onCallback(ArrayList<Videos> videos) {
                recyclerView=view.findViewById(R.id.homeRecyclerView);
                recyclerView.setHasFixedSize(true);
                adapter=new HomeAdapter(getContext(),videos);
                layoutManager=new LinearLayoutManager(getActivity());
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
                return videos;
            }
        });

//         ad=new ArrayAdapter<>(getActivity(),android);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu( Menu menu, MenuInflater inflater) {

        getActivity().getMenuInflater().inflate(R.menu.searchbar,menu);
        MenuItem menuItem=menu.findItem(R.id.app_bar_search);
        SearchView searchView=(SearchView)menuItem.getActionView();
        searchView.setQueryHint("Search");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
//                Log.i("onQueryTextChange", query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
//                Log.i("onQueryTextChange", newText);

                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }
    private String getDate(Long timeStamp) {
        Calendar cal = Calendar.getInstance(Locale.getDefault());
        cal.setTimeInMillis(timeStamp * 1000);
        android.text.format.DateFormat df = new android.text.format.DateFormat();
        String date = df.format("dd-MM-yyyy HH:mm", cal).toString();
        return date;
    }
}
