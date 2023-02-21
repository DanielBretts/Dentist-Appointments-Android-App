package com.example.patientqueuemanagement.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.patientqueuemanagement.HomePageActivity;
import com.example.patientqueuemanagement.R;
import com.example.patientqueuemanagement.Utils.SharedPreferences;
import com.google.android.material.imageview.ShapeableImageView;

public class MainAppointmentFragment extends Fragment {
    private EditText Appointment_ET_firstName;
    private EditText Appointment_ET_lastName;
    private EditText Appointment_ET_Year;
    private EditText Appointment_ET_Address;
    private EditText Appointment_ET_Phone;
    private RadioGroup Appointment_RG_FirstTimeVisiting;
    private EditText Appointment_ET_Notes;
    private LinearLayout Appointment_LL_AnotherDoctor;
    private EditText Appointment_ET_OtherDoctorName;
    private ShapeableImageView Appointment_BTN_schedule;
    private SharedPreferences sharedPreferences;
    private int radioButtonIndex;
    EditText[] inputs;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main_appointment, container, false);
        findViews(v);
        initViews();
        sharedPreferences = SharedPreferences.getInstance();
        inputs = new EditText[]{Appointment_ET_firstName, Appointment_ET_lastName, Appointment_ET_Year, Appointment_ET_Phone,Appointment_ET_OtherDoctorName, Appointment_ET_Address};
        initInputsText();

        return v;
    }

    private void initInputsText() {
        //the order in the KEYS array is same as the inputs array
        int arrayInputsLength = inputs.length;
        for (int i = 0; i < arrayInputsLength; i++) {
            String key = SharedPreferences.KEYS[i];
            String strFromSP = sharedPreferences.getString(key);
            strFromSP = strFromSP.replace("\"", "").replace("\\","");
            inputs[i].setText(strFromSP);
        }
        Appointment_ET_Notes.setText(sharedPreferences.getString(SharedPreferences.NOTES).replace("\"", ""));
    }

    private void initViews() {
        checkImagesDirection();
        Appointment_BTN_schedule.setOnClickListener(v1 -> openSchedule());
        Appointment_RG_FirstTimeVisiting.check(R.id.Appointment_RB_yes);
        initRadioGroup();
    }

    private void checkImagesDirection() {
        if(((HomePageActivity)getActivity()).getLocaleLanguage().equals("he")){
            Appointment_BTN_schedule.setRotation(180);
        }
    }

    private void initRadioGroup() {
        Appointment_RG_FirstTimeVisiting.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                View radioButton = Appointment_RG_FirstTimeVisiting.findViewById(checkedId);
                radioButtonIndex = Appointment_RG_FirstTimeVisiting.indexOfChild(radioButton);
                if(radioButtonIndex == 1)
                    Appointment_LL_AnotherDoctor.setVisibility(View.VISIBLE);
                else {
                    Appointment_LL_AnotherDoctor.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    private void openSchedule() {
        if(checkInputValid()){
            setSharedPreferences();
            ((HomePageActivity) getActivity()).replaceFragments(ScheduleFragment.class);
        }
    }

    private void setSharedPreferences() {
        //the order in the KEYS array is parallel to the inputs array
        int arrayInputsLength = inputs.length;
        for (int i = 0; i < arrayInputsLength; i++) {
            if(inputs[i].equals(Appointment_ET_OtherDoctorName)){
                if(Appointment_LL_AnotherDoctor.getVisibility() == View.INVISIBLE){
                    Appointment_ET_OtherDoctorName.setText(null);
                }
            }
            String value = inputs[i].getText().toString();
            if(!checkEmpty(value)){
                String key = sharedPreferences.KEYS[i];
                sharedPreferences.setString(key,value);
            }
        }
        sharedPreferences.setString(SharedPreferences.KEYS[SharedPreferences.KEYS.length-1], Appointment_ET_Notes.getText().toString());
    }

    private boolean checkInputValid() {
        //an array of edit text elements without the "otherdoctor" edit text
        EditText[] inputsWithoutOtherDoctor = new EditText[]{Appointment_ET_firstName, Appointment_ET_lastName, Appointment_ET_Year, Appointment_ET_Phone, Appointment_ET_Address};
        for (EditText input : inputsWithoutOtherDoctor) {
            if (checkEmpty(input.getText().toString())) {
                shakeElement(input);
                return false;
            }
        }
        if (Appointment_LL_AnotherDoctor.getVisibility() == View.VISIBLE) {
            if (checkEmpty(Appointment_ET_OtherDoctorName.getText().toString())) {
                shakeElement(Appointment_ET_OtherDoctorName);
                return false;
            }
        }
        return true;
    }

    private boolean checkEmpty(String text) {
        return text.trim().isEmpty();
    }

    private void shakeElement(View view) {
        YoYo.with(Techniques.Shake)
                .duration(500)
                .repeat(1)
                .playOn(view);
    }

    private void findViews(View v) {
        Appointment_ET_firstName = v.findViewById(R.id.Appointment_ET_firstName);
        Appointment_ET_lastName = v.findViewById(R.id.Appointment_ET_lastName);
        Appointment_ET_Year = v.findViewById(R.id.Appointment_ET_Year);
        Appointment_ET_Address = v.findViewById(R.id.Appointment_ET_Address);
        Appointment_ET_Phone = v.findViewById(R.id.Appointment_ET_Phone);
        Appointment_RG_FirstTimeVisiting = v.findViewById(R.id.Appointment_RG_FirstTimeVisiting);
        Appointment_ET_OtherDoctorName = v.findViewById(R.id.Appointment_ET_OtherDoctorName);
        Appointment_ET_Notes = v.findViewById(R.id.Appointment_ET_Notes);
        Appointment_LL_AnotherDoctor = v.findViewById(R.id.Appointment_LL_AnotherDoctor);
        Appointment_BTN_schedule = v.findViewById(R.id.Appointment_BTN_schedule);
    }
}