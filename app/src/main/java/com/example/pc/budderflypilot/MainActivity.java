package com.example.pc.budderflypilot;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.pc.budderflypilot.fragments.MainFragment;


public class MainActivity extends AppCompatActivity {

    public static String TAG = MainActivity.class.getSimpleName();

    public static int pageIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        openMainFragment();
    }

    private void openMainFragment() {
        getSupportFragmentManager().beginTransaction().replace(R.id.container, MainFragment.newInstance(), MainFragment.class.getSimpleName()).commit();
    }

    @Override
    public void onBackPressed() {
        if (pageIndex == 0)
            super.onBackPressed();
        else
            openMainFragment();
    }
}
