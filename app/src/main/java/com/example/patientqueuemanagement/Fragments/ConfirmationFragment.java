package com.example.patientqueuemanagement.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chinalwb.slidetoconfirmlib.ISlideListener;
import com.chinalwb.slidetoconfirmlib.SlideToConfirm;
import com.example.patientqueuemanagement.HomePageActivity;
import com.example.patientqueuemanagement.Interfaces.DataListener;
import com.example.patientqueuemanagement.R;
import com.example.patientqueuemanagement.Utils.EnglishHebrewTranslator;
import com.example.patientqueuemanagement.Utils.ServiceNotify;
import com.example.patientqueuemanagement.Utils.SharedPreferences;
import com.example.patientqueuemanagement.Model.Appointment;
import com.example.patientqueuemanagement.Model.User;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.Year;
import java.util.ArrayList;

public class ConfirmationFragment extends Fragment {
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private SharedPreferences sharedPreferences = SharedPreferences.getInstance();
    private MaterialTextView Confirmation_TXT_Details;
    private SlideToConfirm Confirmation_SLIDE_Confirm;
    private int day;
    private int month;
    private int year;
    private String hour;
    private String dayName;
    private String date;
    private String name;
    private String age;
    private String address;
    private String phoneNumber;
    private String otherDoctor;
    private String notes;
    private Appointment appointment;
    private String appointmentID;
    private Intent intent;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_confirmation, container, false);
        findViews(v);
        initViews();
        intent = new Intent(getActivity(), ServiceNotify.class);
        return v;
    }

    private void initViews() {
        loadSharedPreferences();
        setTranslatedDay();
        initSlider();

    }

    /**
     * Slide to confirm initialization
     */
    private void initSlider() {
        Confirmation_SLIDE_Confirm.setSlideListener(new ISlideListener() {
            @Override
            public void onSlideStart() {
            }

            @Override
            public void onSlideMove(float percent) {
            }

            @Override
            public void onSlideCancel() {
            }

            @Override
            public void onSlideDone() {
                createNotification();
                addAppointment();
            }
        });
    }

    /**
     * A callback to translate the day name to hebrew
     */
    private void setTranslatedDay() {
        if (((HomePageActivity) getActivity()).getLocaleLanguage().equals("he")) {
            EnglishHebrewTranslator.translate(new DataListener() {
                @Override
                public void getStringVal(String s) {
                    dayName = s;
                    setDateText();
                }

                @Override
                public void getDataSnapshot(DataSnapshot dataSnapshot) {
                }

                @Override
                public void fail() {
                    Log.e("ERROR", "failed translating");
                }
            }, dayName);
        } else {
            setDateText();
        }
    }

    private void loadSharedPreferences() {
        day = Integer.parseInt(sharedPreferences.getString(SharedPreferences.DAY).replace("\"", ""));
        month = Integer.parseInt(sharedPreferences.getString(SharedPreferences.MONTH).replace("\"", ""));
        year = Integer.parseInt(sharedPreferences.getString(SharedPreferences.YEAR).replace("\"", ""));
        hour = sharedPreferences.getString(SharedPreferences.HOUR).replace("\"", "");
        dayName = sharedPreferences.getString(SharedPreferences.DAY_NAME).replace("\"", "");
    }

    /**
     * creating a confirm notification for the appointment
     */
    private void createNotification() {
        intent.setAction(ServiceNotify.ACTION_START);
        getActivity().startService(intent);
    }

    private void setDateText() {
        date = String.format("%d/%d/%d\n%s,%s", day, month, year, dayName, hour);
        Confirmation_TXT_Details.setText(date);
    }

    /**
     * Adding the appointment to the user's reference in the firebase
     */
    private void addAppointment() {
        DatabaseReference databaseReference = firebaseDatabase.getReference("setAppointments");
        setAppointmentDetails();
        addAppointmentToDBList(databaseReference);
        DatabaseReference usersRef = firebaseDatabase.getReference("users");
        final User[] user = new User[1];
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot d : snapshot.getChildren()) {
                    if (d.getKey().equals(firebaseUser.getUid())) {
                        user[0] = d.getValue(User.class);
                        if (user[0] != null) {
                            if (user[0].getAppointments() == null) {
                                user[0].setAppointments(new ArrayList<Appointment>());
                            }
                            ArrayList<Appointment> userAppointments = user[0].getAppointments();
                            userAppointments.add(appointment);
                            usersRef.child(firebaseUser.getUid()).child("appointments").setValue(userAppointments);
                            backToHomePage();
                            SharedPreferences.getInstance().clearInputsText();
                        }
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("ERROR", "failed connecting to firebase");
            }
        });
    }

    private void addAppointmentToDBList(DatabaseReference databaseReference) {
        appointment = new Appointment(firebaseUser.getUid(), hour.replace("\"", ""), name.replace("\"", ""), age.replace("\"", ""), address.replace("\"", ""), phoneNumber.replace("\"", ""), otherDoctor.replace("\"", ""), notes.replace("\"", ""), day, month, year, dayName);
        String key = String.format("%d,%d,%d", day, month, year);
        DatabaseReference appointmentDateRef = databaseReference.child(key);
        DatabaseReference appointmentIDRef = appointmentDateRef.child(hour);
        appointmentIDRef.setValue(appointment);
        appointmentID = appointmentIDRef.getKey();
    }

    /**
     * Getting the values from shared preferences and formatting the appointment details for the UI
     */
    private void setAppointmentDetails() {
        String firstName = sharedPreferences.getString(SharedPreferences.FIRST_NAME);
        String lastName = sharedPreferences.getString(SharedPreferences.LAST_NAME);
        name = String.format("%s %s", firstName, lastName);
        int yearOfBirth = Integer.parseInt(sharedPreferences.getString(SharedPreferences.YEAR_OF_BIRTH).replace("\"", ""));
        int thisYear = Integer.parseInt(Year.now().toString());
        age = String.valueOf(thisYear - yearOfBirth);
        address = sharedPreferences.getString(SharedPreferences.ADDRESS);
        phoneNumber = sharedPreferences.getString(SharedPreferences.PHONE);
        otherDoctor = sharedPreferences.getString(SharedPreferences.ANOTHER_DOCTOR);
        notes = sharedPreferences.getString(SharedPreferences.NOTES);
    }

    private void backToHomePage() {
        ((HomePageActivity) getActivity()).replaceFragments(WelcomePageFragment.class);
    }

    private void findViews(View v) {
        Confirmation_SLIDE_Confirm = v.findViewById(R.id.Confirmation_SLIDE_Confirm);
        Confirmation_TXT_Details = v.findViewById(R.id.Confirmation_TXT_Details);
    }
}