package com.example.chcook.KahHeng.EndUser.GUI;


import android.app.AlertDialog;
import android.content.DialogInterface;
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

import com.example.chcook.KahHeng.EndUser.Adapter.HomeAdapter;
import com.example.chcook.Domain.Videos;
import com.example.chcook.KahHeng.EndUser.DA.VideoDA;
import com.example.chcook.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class Home extends Fragment {
    private RecyclerView recyclerView;
//    RecyclerView.Adapter
    private HomeAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private View view;
    private SearchView.OnQueryTextListener queryTextListener;
    private SearchView searchView = null;
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
        getActivity().setTitle("Home");
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
                adapter.filter(newText);
//                Log.i("onQueryTextChange", newText);
//                homeAdapter.filter(newText);
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
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.app_bar_search:
                return true;
            case R.id.app_bar_filter:
                AlertDialog.Builder category = new AlertDialog.Builder(getActivity());
                category.setTitle("Select Category");


                String[] cat = {"All","Western","Pasta","Rice","Sandwiches","Soup","Pancake","Dinner","Breakfast","Dessert","Pizza","Other"};
                category.setItems(cat, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String getCat="";
                        dialog.dismiss();
                        switch (which){
                            case 0:
                                getCat = "All";
                                break;
                            case 1:
                                getCat = "Western";
                                break;
                            case 2:
                                getCat="Pasta";
                                break;
                            case 3:
                                getCat = "Rice";
                                break;
                            case 4:
                                getCat="Sandwiches";
                                break;
                            case 5:
                                getCat="Soup";
                                break;
                            case 6:
                                getCat="Pancake";
                                break;
                            case 7:
                                getCat="Dinner";
                                break;
                            case 8:
                                getCat="Breakfast";
                                break;
                            case 9:
                                getCat="Dessert";
                                break;
                            case 10:
                                getCat="Pizza";
                                break;
                            case 11:
                                getCat="Other";
                                break;
                        }
                        adapter.category(getCat);
                    }
                });
                category.show();

                return true;
            default:
                break;

        }
        searchView.setOnQueryTextListener(queryTextListener);
        return super.onOptionsItemSelected(item);
    }

}
