package com.example.dailyexpenseproject;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("Dashboard");
        init();
        configBottomNavView();
        replaceFragment(new DashboardFragment());

    }

    private void init() {
        bottomNavigationView = findViewById(R.id.bottom_nav_view);
    }

    private void configBottomNavView() {
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.nav_menu_dashboard:
                    replaceFragment(new DashboardFragment());
                    setTitle("Home Dashboard");
                    return true;

                case R.id.nav_menu_Expenses:
                    replaceFragment(new ExpenseShowFragment());
                    setTitle("Expense list");
                    return true;
            }
            return false;
        }
    };

    private void replaceFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,fragment);
        fragmentTransaction.commit();
    }
}
