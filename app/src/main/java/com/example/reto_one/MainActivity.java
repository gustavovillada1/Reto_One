package com.example.reto_one;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.reto_one.fragments.CalendarFragment;
import com.example.reto_one.fragments.MapsFragment;
import com.example.reto_one.fragments.PerfilFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    CalendarFragment calendarFragment = new CalendarFragment();
    MapsFragment mapsFragment = new MapsFragment();
    PerfilFragment perfilFragment = new PerfilFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigationView = findViewById(R.id.bottom_navigation);
        navigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);

        loadFragment(perfilFragment);

    }


    //Este listener es para cuando presionen algun fragmento.
    public final BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()){

                case R.id.calendarFragment:
                    loadFragment(calendarFragment);
                    return true;

                case R.id.mapFragment:
                    loadFragment(mapsFragment);
                    return true;


                case R.id.perfilFragment:
                    loadFragment(perfilFragment);
                    return true;

            }


            return false;
        }
    };


    /**
     * Este metodo se encarga de cargar el fragmento seleccionado al frame layout.
     * @param fragment
     */
    public void loadFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.commit();

    }

}