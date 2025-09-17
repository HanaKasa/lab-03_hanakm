package com.example.listycitylab3;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.io.Serializable;

public class AddCityFragment extends DialogFragment {

    private City city;
    private int position = -1; // set to have undefined position inital
    private static final String ArgPosition = "arg_position";
    private static final String ArgCity = "arg_city";

    interface AddCityDialogListener {
        void addCity(City city);
        void editCity(City city, int position);
    }

    private AddCityDialogListener listener;

    public AddCityFragment() {}

    // Factory method to create fragment for editing
    public static AddCityFragment newInstance(City city, int position) {
        AddCityFragment fragment = new AddCityFragment();
        Bundle args = new Bundle();
        args.putSerializable(ArgCity, city);
        args.putInt(ArgPosition, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            city = (City) getArguments().getSerializable(ArgCity);
            position = getArguments().getInt(ArgPosition, -1);
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof AddCityDialogListener) {
            listener = (AddCityDialogListener) context;
        } else {
            throw new RuntimeException(context + " must implement AddCityDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_add_city, null);
        EditText editCityName = view.findViewById(R.id.edit_text_city_text);
        EditText editProvinceName = view.findViewById(R.id.edit_text_province_text);

        // Pre-fill fields if editing
        if (city != null) {
            editCityName.setText(city.getName());
            editProvinceName.setText(city.getProvince());
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle(city == null ? "Add a city" : "Edit city") // adding secondary part since its similar fragment
                .setNegativeButton("Cancel", null)
                .setPositiveButton(city == null ? "Add" : "Save", (dialog, which) -> { // secondary option 
                    String cityName = editCityName.getText().toString();
                    String provinceName = editProvinceName.getText().toString();

                    if (city != null) {
                        city.setName(cityName);
                        city.setProvince(provinceName);
                        listener.editCity(city, position); // pass position
                    } else {
                        listener.addCity(new City(cityName, provinceName));
                    }
                })
                .create();
    }
}
