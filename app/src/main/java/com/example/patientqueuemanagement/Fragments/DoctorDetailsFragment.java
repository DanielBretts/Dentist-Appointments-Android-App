package com.example.patientqueuemanagement.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.patientqueuemanagement.R;
import com.example.patientqueuemanagement.Utils.Constants;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class DoctorDetailsFragment extends Fragment {
    private FloatingActionButton doctorDetails_FAB_call;
    private FloatingActionButton DoctorDetails_FAB_mail;
    private FloatingActionButton DoctorDetails_FAB_waze;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_doctor_details, container, false);
        findViews(v);
        initViews();
        return v;
    }

    private void initViews() {
        DoctorDetails_FAB_waze.setOnClickListener(v1 -> openWaze());
        doctorDetails_FAB_call.setOnClickListener(v1 -> callDoctor());
        DoctorDetails_FAB_mail.setOnClickListener(v1 -> sendMail());
    }

    private void sendMail() {
        String[] recipient = {Constants.EMAIL};
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.putExtra(Intent.EXTRA_EMAIL, recipient);
        startActivity(emailIntent);
    }

    private void openWaze() {
        String url = String.format("waze://?q=%s", Constants.LOCATION);
        Intent wazeIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(wazeIntent);
    }

    private void callDoctor() {
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse(String.format("tel:%s", Constants.PHONE_NUMBER)));
        startActivity(callIntent);
    }

    private void findViews(View v) {
        DoctorDetails_FAB_waze = v.findViewById(R.id.DoctorDetails_FAB_waze);
        doctorDetails_FAB_call = v.findViewById(R.id.DoctorDetails_FAB_call);
        DoctorDetails_FAB_mail = v.findViewById(R.id.DoctorDetails_FAB_mail);
    }
}