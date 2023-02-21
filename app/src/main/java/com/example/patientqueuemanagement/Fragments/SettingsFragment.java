package com.example.patientqueuemanagement.Fragments;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.patientqueuemanagement.HomePageActivity;
import com.example.patientqueuemanagement.MainActivity;
import com.example.patientqueuemanagement.R;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Locale;

public class SettingsFragment extends Fragment {
    MaterialButton Settings_BTN_Language;
    MaterialButton Settings_BTN_UIMode;
    private MaterialButton logout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_settings, container, false);
        findViews(v);
        initViews();
        return v;
    }

    private void initViews() {
        Settings_BTN_Language.setOnClickListener(v1->changeLanguage());
        Settings_BTN_UIMode.setOnClickListener(v1 -> ((HomePageActivity)getActivity()).setViewMode(getContext(),getActivity()));
        logout.setOnClickListener(v1-> logout());
    }


    private void changeLanguage() {
        Locale currentLocale = getResources().getConfiguration().locale;
        String displayLanguage = currentLocale.getLanguage().equals("en") ? "he" : "en";
        Locale locale = new Locale(displayLanguage);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getContext().getResources().updateConfiguration(config,
                getContext().getResources().getDisplayMetrics());
        int orientation = displayLanguage.equals("he") ? View.LAYOUT_DIRECTION_RTL : View.LAYOUT_DIRECTION_LTR;
        HomePageActivity.getHomepage_FRAME_main().setLayoutDirection(orientation);
        setBottomNavigationViewLanguage();
        refreshFragment();
    }

    private void refreshFragment() {
        ((HomePageActivity) getActivity()).replaceFragments(SettingsFragment.class);
    }

    private void setBottomNavigationViewLanguage() {
        Menu menu = HomePageActivity.getBottomNavigationView().getMenu();
        MenuItem homeItem = menu.findItem(R.id.homepage_ITEM_1);
        homeItem.setTitle(R.string.home);

        MenuItem aboutItem = menu.findItem(R.id.homepage_ITEM_2);
        aboutItem.setTitle(R.string.about);

        MenuItem accountItem = menu.findItem(R.id.homepage_ITEM_3);
        accountItem.setTitle(R.string.doctor_login);

        MenuItem settingsItem = menu.findItem(R.id.homepage_ITEM_4);
        settingsItem.setTitle(R.string.settings);
    }

    private void findViews(View v) {
        Settings_BTN_Language = v.findViewById(R.id.Settings_BTN_Language);
        Settings_BTN_UIMode = v.findViewById(R.id.Settings_SWTCH_UIMode);
        logout = v.findViewById(R.id.logout);
    }

    private void logout() {
        FirebaseAuth.getInstance().signOut();
        AuthUI.getInstance()
                .signOut(getContext())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        Intent intent = new Intent(getContext(), MainActivity.class);
                        startActivity(intent);
                    }
                });
    }
}