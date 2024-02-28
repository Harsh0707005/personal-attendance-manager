package com.thattechyguy.personalattendancemanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class homeBottomNav extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_bottom_nav);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        frameLayout = findViewById(R.id.fragment);

        bottomNavigationView.setItemIconTintList(null);


        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId()==R.id.dashboard_menu){
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment, new DashboardFragment())
                            .commit();
                    return true;
                }else if (item.getItemId()==R.id.schedule_manage_menu){
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment, new ScheduleManageFragment())
                            .commit();
                    return true;
                }else if (item.getItemId()==R.id.settings_menu){
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment, new SettingsFragment())
                            .commit();
                    return true;
                }

                return false;
            }
        });
    }
}