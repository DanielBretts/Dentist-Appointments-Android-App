package com.example.patientqueuemanagement.Fragments;

import android.icu.util.Calendar;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.NumberPicker;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.patientqueuemanagement.HomePageActivity;
import com.example.patientqueuemanagement.R;
import com.example.patientqueuemanagement.Utils.Constants;
import com.example.patientqueuemanagement.Utils.SharedPreferences;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ScheduleFragment extends Fragment {
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private String selectedDayName;
    private CalendarView Appointment_CalendarView;
    private NumberPicker Appointment_NP_hours;
    private ShapeableImageView Appointment_BTN_FillDetails;
    private ShapeableImageView Appointment_BTN_ConfirmAppointment;
    private MaterialTextView Appointment_TXT_Error;
    private ArrayList<String> availableHours;
    private int dayChosen;
    private int monthChosen;
    private int yearChosen;
    private int hourIndex;
    private String hour;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_schedule, container, false);
        findViews(v);
        initViews(v);
        setDayName();
        updateUIAvailableHours();
        //calling this function for the first hour that is displayed
        checkIfAppointmentTaken();
        return v;
    }

    private void initCalendar() {
        Calendar c = Calendar.getInstance();
        yearChosen = c.get(Calendar.YEAR);
        monthChosen = c.get(Calendar.MONTH);
        dayChosen = c.get(Calendar.DAY_OF_MONTH);
    }

    private void findViews(View v) {
        Appointment_BTN_FillDetails = v.findViewById(R.id.Appointment_BTN_FillDetails);
        Appointment_NP_hours = v.findViewById(R.id.Appointment_NP_hours);
        Appointment_CalendarView = v.findViewById(R.id.Appointment_CalendarView);
        Appointment_BTN_ConfirmAppointment = v.findViewById(R.id.Appointment_BTN_ConfirmAppointment);
        Appointment_TXT_Error = v.findViewById(R.id.Appointment_TXT_Error);
        checkImagesDirection();
    }

    private void checkImagesDirection() {
        if(((HomePageActivity)getActivity()).getLocaleLanguage().equals("he")){
            Appointment_BTN_ConfirmAppointment.setRotation(180);
            Appointment_BTN_FillDetails.setRotation(0);
        }
    }

    private void initViews(View v) {
        initCalendar();
        Appointment_BTN_FillDetails.setOnClickListener(v1 -> openDetails());
        setMinDateForCalendar();
        Appointment_CalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                dayChosen = dayOfMonth;
                monthChosen = month;
                yearChosen = year;
                Appointment_TXT_Error.setVisibility(View.INVISIBLE);
                //calling this function for the first hour that is displayed
                checkIfAppointmentTaken();
                setDayName();
            }
        });
        Appointment_BTN_ConfirmAppointment.setOnClickListener(v1 -> openConfirmation());


        Appointment_NP_hours.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                hour = availableHours.get(newVal);
                hourIndex = newVal;
                checkIfAppointmentTaken();
            }
        });

    }

    /**
     * Checking if the time that is displayed in the number picker is taken in that day
     */
    private void checkIfAppointmentTaken() {
        DatabaseReference databaseReference = firebaseDatabase.getReference("setAppointments");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String keyChosen = String.format("%d,%d,%d",dayChosen,monthChosen+1,yearChosen);
                for (DataSnapshot dayKeySnapshot: snapshot.getChildren()) {
                    if(dayKeySnapshot.getKey().equals(keyChosen)){
                        for (DataSnapshot appointmentKey:dayKeySnapshot.getChildren()) {
                            int visibility = hour.equals(appointmentKey.getKey()) ? View.VISIBLE : View.INVISIBLE;
                            Appointment_TXT_Error.setVisibility(visibility);
                            if(visibility == View.VISIBLE)
                                break;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    /**
     * Sets the option to see only the relevant dates to pick an appointment
     */
    private void setMinDateForCalendar() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DATE,Calendar.getInstance().getActualMinimum(Calendar.DATE));
        long date = calendar.getTime().getTime();
        Appointment_CalendarView.setMinDate(date);
    }

    /**
     * Opens the next activity. If the date that was picked is occupied - you can't move on
     */
    private void openConfirmation() {
        if(Appointment_TXT_Error.getVisibility() == View.INVISIBLE){
            saveAppointmentDate();
            ((HomePageActivity) getActivity()).replaceFragments(ConfirmationFragment.class);
        }else{
            shakeElement(Appointment_TXT_Error);
        }
    }

    private void shakeElement(View view) {
        YoYo.with(Techniques.Shake)
                .duration(500)
                .repeat(1)
                .playOn(view);
    }

    private void saveAppointmentDate() {
        SharedPreferences sharedPreferences = SharedPreferences.getInstance();
        sharedPreferences.setString(SharedPreferences.DAY,String.valueOf(dayChosen));
        sharedPreferences.setString(SharedPreferences.MONTH,String.valueOf(monthChosen+1));
        sharedPreferences.setString(SharedPreferences.YEAR,String.valueOf(yearChosen));
        sharedPreferences.setString(SharedPreferences.HOUR,hour);
        sharedPreferences.setString(SharedPreferences.DAY_NAME,selectedDayName);
    }

    private void openDetails() {
        ((HomePageActivity) getActivity()).replaceFragments(MainAppointmentFragment.class);
    }

    private void setDayName() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(yearChosen, monthChosen, dayChosen);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        selectedDayName = Constants.DAYS[dayOfWeek-1];
        updateUIAvailableHours();
    }

    private void updateUIAvailableHours() {
        Appointment_NP_hours.setDisplayedValues(null);
        Appointment_NP_hours.setVisibility(View.VISIBLE);
        if(selectedDayName.equals("Saturday"))
            Appointment_NP_hours.setVisibility(View.INVISIBLE);
        setAvailableHours(selectedDayName);
    }

    private void setAvailableHours(String selectedDayName) {
        DatabaseReference databaseReference = firebaseDatabase.getReference("appointments").child(selectedDayName).child("available");
        availableHours = new ArrayList<>();
        Appointment_NP_hours.setMinValue(0);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            availableHours.clear();
            for(DataSnapshot dataSnapshot: snapshot.getChildren())
                availableHours.add(dataSnapshot.getValue(String.class));

            String[] ahStrings = new String[availableHours.size()];
            for (int i = 0; i < availableHours.size(); i++) {
                ahStrings[i] = availableHours.get(i);
            }
            int hoursSize = availableHours.size();
            if(hoursSize > 1){
                Appointment_NP_hours.setMaxValue(hoursSize-1);
                Appointment_NP_hours.setMinValue(0);
                Appointment_NP_hours.setDisplayedValues(ahStrings);
            }
            if(!selectedDayName.equals(Constants.DAYS[Constants.DAYS.length-1]))
                hour = availableHours.get(0);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            Log.e("ERROR", "failed connecting to firebase");
        }
    });
    }
}