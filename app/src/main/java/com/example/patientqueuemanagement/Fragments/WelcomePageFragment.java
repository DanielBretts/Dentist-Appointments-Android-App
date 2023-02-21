package com.example.patientqueuemanagement.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.patientqueuemanagement.AppointmentsAdapter;
import com.example.patientqueuemanagement.Interfaces.DataListener;
import com.example.patientqueuemanagement.R;
import com.example.patientqueuemanagement.Utils.DataManager;
import com.example.patientqueuemanagement.Model.Appointment;
import com.example.patientqueuemanagement.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class WelcomePageFragment extends Fragment {
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
    private TextView welcomePage_TXT_welcome;
    private RecyclerView recyclerViewAppointments;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private User user;
    private ArrayList<Appointment> userAppointments;
    private View view;
    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_welcome_page, container, false);
        findViews();
        initUserFromDB();
        return view;
    }

    private void initUserFromDB() {
        DatabaseReference userRef = firebaseDatabase.getReference("users");
        DataManager.readDataFromFirebase(userRef, new DataListener() {
            @Override
            public void getStringVal(String s) {
            }

            @Override
            public void getDataSnapshot(DataSnapshot dataSnapshot) {
                user = dataSnapshot.child(firebaseAuth.getUid()).getValue(User.class);
                welcomePage_TXT_welcome.setText(user.getName());
                recyclerViewAppointments.setAdapter(new AppointmentsAdapter(user.getAppointments()));
                //if the user is signed in for the first time - create a new user object and insert to firebase
                if (user == null) {
                    user = new User(firebaseUser.getDisplayName(), firebaseUser.getEmail(), firebaseUser.getPhoneNumber());
                }
                userRef.child(firebaseUser.getUid()).setValue(user);
                recyclerViewAppointments.setAdapter(new AppointmentsAdapter(user.getAppointments()));
                String name = user.getName().isEmpty() ? getString(R.string.user) : user.getName();
                welcomePage_TXT_welcome.setText(name);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void fail() {
                Log.e("ERROR", "failed translating");
            }
        });
    }

    private void findViews() {
        welcomePage_TXT_welcome = view.findViewById(R.id.welcomePage_TXT_welcome);
        recyclerViewAppointments = view.findViewById(R.id.welcomePage_RV_Appointments);
        progressBar = view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        recyclerViewAppointments.setHasFixedSize(true);
        recyclerViewAppointments.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

    }
}