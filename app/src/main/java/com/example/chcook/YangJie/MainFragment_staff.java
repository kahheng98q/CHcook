package com.example.chcook.YangJie;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.chcook.Domain.Videos;
import com.example.chcook.R;
import com.example.chcook.YangJie.DA.recyclerViewAdapter_banVideo;
import com.example.chcook.YangJie.DA.recyclerViewAdapter_staffMain;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class MainFragment_staff extends Fragment  {

    @Nullable

    private ArrayList<Videos> mVideo = new ArrayList<>();
    private SearchView searchView = null;
    private SearchView.OnQueryTextListener queryTextListener;
    recyclerViewAdapter_staffMain adapter;
    private RecyclerView recyclerView;
    Context mContext;
    private String position;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Videos");

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        final View view = inflater.inflate(R.layout.main_fragment_staff,container,false);
        final Bundle argument = getArguments();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {

                    for (DataSnapshot v : dataSnapshot.getChildren()) {

                        String videoName = v.child("name").getValue(String.class);

                        Long date = v.child("Uploaddate").getValue(Long.class);
                        String videoUrl = v.child("URL").getValue(String.class);
                        String user = v.child("userId").getValue(String.class);
                        String desc = v.child("description").getValue(String.class);
                        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                        String latestDate = df.format(date);
                        mVideo.add(new Videos(v.getKey(),videoName,latestDate,videoUrl,desc));

                    }
                }
                if(argument!=null){
                    position = argument.getString("position");
//            Toast.makeText(getActivity(),position,Toast.LENGTH_SHORT).show();
                }
                recyclerView = view.findViewById(R.id.StaffMainRecyclerView);
                adapter = new recyclerViewAdapter_staffMain(getContext(),mVideo,position);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }


        });



        return view;
    }




    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.staffsearchbar,menu);
        MenuItem item = menu.findItem(R.id.action_search_staff);
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);

        if(item!=null){
            searchView = (SearchView) item.getActionView();
        }
        if(item!=null){
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
            searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
            queryTextListener = new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    adapter.filter(newText);
//                    adapter.getFilter().filter(newText);
                    return true;
                }
            };
            searchView.setOnQueryTextListener(queryTextListener);
        }
        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search_staff:
//                Toast.makeText(getActivity(),"1",Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_filter_staff:
//                Toast.makeText(getActivity(),"2",Toast.LENGTH_SHORT).show();
                AlertDialog.Builder category = new AlertDialog.Builder(getActivity());
                category.setTitle("Select Category");
                String[] cat = {"All","American","Malaysia","Indonesia"};
                category.setItems(cat, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String getCat="";
                        dialog.dismiss();
                        switch (which){
                            case 0:
                                getCat = "all";
                                //                                Toast.makeText(getActivity(),"All",Toast.LENGTH_SHORT).show();
                                break;
                            case 1:
                                getCat = "video";
//                                Toast.makeText(getActivity(),"American",Toast.LENGTH_SHORT).show();
                                break;
                            case 2:
                                getCat = "hk";
//                                Toast.makeText(getActivity(),"Malaysia",Toast.LENGTH_SHORT).show();
                                break;
                            case 3:
//                                Toast.makeText(getActivity(),"Indonesia",Toast.LENGTH_SHORT).show();
                                break;
                        }
//                        Toast.makeText(getActivity(),getCat,Toast.LENGTH_SHORT).show();
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



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }


}
