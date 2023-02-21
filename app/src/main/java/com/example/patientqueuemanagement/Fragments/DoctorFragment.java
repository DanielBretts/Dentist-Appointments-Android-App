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

import com.example.patientqueuemanagement.DoctorAppointmentsAdapter;
import com.example.patientqueuemanagement.HomePageActivity;
import com.example.patientqueuemanagement.Interfaces.DataListener;
import com.example.patientqueuemanagement.R;
import com.example.patientqueuemanagement.Utils.DataManager;
import com.example.patientqueuemanagement.Model.Appointment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class DoctorFragment extends Fragment {
    private RecyclerView Doctor_RV_Appointments;
    private final String TAG = "DoctorFragment";
    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_doctor, container, false);
        checkIfUserDoctor();
        findViews(v);
        return v;
    }

    private void findViews(View v) {
        Doctor_RV_Appointments = v.findViewById(R.id.Doctor_RV_Appointments);
        Doctor_RV_Appointments.setHasFixedSize(true);
        Doctor_RV_Appointments.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL, false));
        progressBar = v.findViewById(R.id.progressBar);
    }

    /**
     * This function checks if the current user has the permissions to enter the doctor's appointments
     */
    private void checkIfUserDoctor() {
        DatabaseReference doctorRef = FirebaseDatabase.getInstance().getReference("doctor");
        DataManager.readDataFromFirebase(doctorRef, new DataListener() {
            @Override
            public void getStringVal(String s) {}

            @Override
            public void getDataSnapshot(DataSnapshot dataSnapshot) {
                String docUID = dataSnapshot.getValue(String.class);
                if(FirebaseAuth.getInstance().getCurrentUser().getUid().equals(docUID)){
                    showAppointments();
                }else{
                    ((HomePageActivity) getActivity()).replaceFragments(WelcomePageFragment.class);
                }

            }

            @Override
            public void fail() {
                Log.e("ERROR", "failed reading from firebase");
            }
        });
    }

    private void showAppointments() {
        DatabaseReference appointmentsRef = FirebaseDatabase.getInstance().getReference("setAppointments");
        DataManager.readDataFromFirebase(appointmentsRef, new DataListener() {
            @Override
            public void getStringVal(String s) {}

            @Override
            public void getDataSnapshot(DataSnapshot dataSnapshot) {
                ArrayList<Appointment> allAppointments = new ArrayList<>();
                for(DataSnapshot date:dataSnapshot.getChildren()){
                    for(DataSnapshot appointmentDS : date.getChildren()){
                        allAppointments.add(appointmentDS.getValue(Appointment.class));
                    }
                }
                Doctor_RV_Appointments.setAdapter(new DoctorAppointmentsAdapter(allAppointments));
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void fail() {
                Log.e("ERROR", "failed translating");
            }
        });


    }
}