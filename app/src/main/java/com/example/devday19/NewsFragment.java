package com.example.devday19;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewsFragment extends Fragment {

    private RecyclerView allnewsList;
    private DatabaseReference mDatabaseReference;
    private DatabaseReference bucketReference;
    private FirebaseAuth mAuth;


    public NewsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_news, container, false);

        allnewsList = (RecyclerView) view.findViewById(R.id.all_news_list);
        allnewsList.setHasFixedSize(true);
        allnewsList.setLayoutManager(new LinearLayoutManager(getActivity()));

        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("AddUpdate");

        //Offline
        mDatabaseReference.keepSynced(true);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();


    }

    public static class AllNewsViewHolder extends RecyclerView.ViewHolder{

        View mView;
       // TextView  email;
        TextView location;
        TextView  type;
        TextView  details;
        TextView time;

        public AllNewsViewHolder(@NonNull View itemView) {
            super(itemView);
             mView=itemView;

        }

        public void setEmail (String email){

          TextView  Email = mView.findViewById(R.id.username);
          Email.setText(email);

        }

        public void setLocation(String location){
            TextView Location = mView.findViewById(R.id.location);
            Location.setText(location);


        }

        public void setType(String  type) {
            TextView  Type = mView.findViewById(R.id.type);
            Type.setText(type);
        }

        public void setDetails(String details) {
            TextView  Details = mView.findViewById(R.id.details);
            Details.setText(details);

        }

        public void setTime(String time) {
            TextView  Time = mView.findViewById(R.id.time);
            Time.setText(time);
        }
    }
}
