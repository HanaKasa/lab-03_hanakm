package com.example.listycitylab3;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements AddCityFragment.AddCityDialogListener {

    private ArrayList<City> dataList;
    private ListView cityList;
    private CityArrayAdapter cityAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize city data
        String[] cities = { "Edmonton", "Vancouver", "Toronto" };
        String[] provinces = { "AB", "BC", "ON" };
        dataList = new ArrayList<>();
        for (int i = 0; i < cities.length; i++) {
            dataList.add(new City(cities[i], provinces[i]));
        }

        // Setup ListView
        cityList = findViewById(R.id.city_list);
        cityAdapter = new CityArrayAdapter(this, dataList);
        cityList.setAdapter(cityAdapter);

        // Click on city to edit
        cityList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                City selectedCity = dataList.get(position);
                // Pass position to fragment for proper editing
                AddCityFragment fragment = AddCityFragment.newInstance(selectedCity, position);
                fragment.show(getSupportFragmentManager(), "edit_city");
            }
        });

        // FloatingActionButton to add new city
        FloatingActionButton fab = findViewById(R.id.button_add_city);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddCityFragment fragment = new AddCityFragment(); // adding new city
                fragment.show(getSupportFragmentManager(), "add_city");
            }
        });
    }

    // Adding a new city
    @Override
    public void addCity(City city) {
        dataList.add(city);
        cityAdapter.notifyDataSetChanged();
    }

    @Override
    public void editCity(City city, int position) {
        if (position != -1) { // if initial position is set
            dataList.set(position, city);
            cityAdapter.notifyDataSetChanged();
        }
    }
}