package com.example.patientqueuemanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.example.patientqueuemanagement.Fragments.DoctorDetailsFragment;
import com.example.patientqueuemanagement.Fragments.DoctorFragment;
import com.example.patientqueuemanagement.Fragments.MainAppointmentFragment;
import com.example.patientqueuemanagement.Fragments.SettingsFragment;
import com.example.patientqueuemanagement.Fragments.WelcomePageFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Locale;

public class HomePageActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FloatingActionButton floatingActionButton;
    private WelcomePageFragment welcomePageFragment;
    private MainAppointmentFragment appointmentFragment;
    private SettingsFragment settingsFragment;
    private DoctorDetailsFragment doctorDetailsFragment;
    private DoctorFragment accountFragment;
    private static BottomNavigationView bottomNavigationView;
    private static FrameLayout homepage_FRAME_main;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        initFragments();
        findViews();
        initViews();
    }

    public static BottomNavigationView getBottomNavigationView() {
        return bottomNavigationView;
    }

    public static FrameLayout getHomepage_FRAME_main() {
        return homepage_FRAME_main;
    }

    private void initFragments() {
        welcomePageFragment = new WelcomePageFragment();
        appointmentFragment = new MainAppointmentFragment();
        settingsFragment = new SettingsFragment();
        doctorDetailsFragment = new DoctorDetailsFragment();
        accountFragment = new DoctorFragment();
    }

    private void initViews() {
        floatingActionButton.setOnClickListener(v -> {openAppointmentFragment();});
        bottomNavigationView.setOnNavigationItemSelectedListener((BottomNavigationView.OnNavigationItemSelectedListener) this);
        bottomNavigationView.setSelectedItemId(R.id.homepage_ITEM_1);
    }

    private void openAppointmentFragment() {
        clearFocusFromNavigationBar();
        getSupportFragmentManager().beginTransaction().replace(R.id.homepage_FRAME_main, appointmentFragment).commit();
    }

    private void clearFocusFromNavigationBar() {
        Menu menu = bottomNavigationView.getMenu();
        for (int i = 0; i < menu.size(); i++) {
            MenuItem menuItem = menu.getItem(i);
            if (menuItem.isChecked())
                menuItem.setCheckable(false);
        }
    }

    /**
     * Switch between fragments by inflating the activity's frame
     *
     * @param  fragmentClass   inflated the frame with this fragment
     */
    public void replaceFragments(Class fragmentClass) {
        Fragment fragment = null;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.homepage_FRAME_main, fragment)
                .commit();
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        bottomNavigationView.getMenu().setGroupCheckable(0, true, true);
        switch (item.getItemId()) {
            case R.id.homepage_ITEM_1:
                getSupportFragmentManager().beginTransaction().replace(R.id.homepage_FRAME_main, welcomePageFragment).commit();
                return true;

            case R.id.homepage_ITEM_2:
                getSupportFragmentManager().beginTransaction().replace(R.id.homepage_FRAME_main, doctorDetailsFragment).commit();
                return true;

            case R.id.homepage_ITEM_3:
                getSupportFragmentManager().beginTransaction().replace(R.id.homepage_FRAME_main, accountFragment).commit();
                return true;

            case R.id.homepage_ITEM_4:
                getSupportFragmentManager().beginTransaction().replace(R.id.homepage_FRAME_main, settingsFragment).commit();
                return true;
        }
        return false;
    }

    private void findViews() {
        homepage_FRAME_main = findViewById(R.id.homepage_FRAME_main);
        floatingActionButton = findViewById(R.id.homepage_FAB_make_appointment);
        bottomNavigationView = findViewById(R.id.homepage_BottomNavigationView);
    }

    @Override
    public void recreate() {
        finish();
        startActivity(getIntent());
    }

    public static void setViewMode(Context context, Activity activity) {
            int nightModeFlags = context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
            switch (nightModeFlags) {
                case Configuration.UI_MODE_NIGHT_YES:
                    activity.finish();
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    break;

                case Configuration.UI_MODE_NIGHT_NO:
                    activity.finish();
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    break;
            }

    }

    /**
     * a function for checking what is the current language
     *
     * @return String "he" for hebrew or "en" for english
     */
    public String getLocaleLanguage() {
        Locale currentLocale = getResources().getConfiguration().locale;
        return currentLocale.getLanguage().equals("en") ? "en" : "he";
    }

}
