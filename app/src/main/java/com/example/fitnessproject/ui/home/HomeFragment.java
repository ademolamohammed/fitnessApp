package com.example.fitnessproject.ui.home;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.fitnessproject.R;
import com.example.fitnessproject.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {
    TextView welcomeText,caloriesValue;
    private static final String PREFS_NAME = "LoginPrefs";
    private static final String PREFS_CALORIES = "caloriesPref";

    private static final String KEY_FIRSTNAME = "firstName";
    private static final String KEY_CALORIES = "calories";
    private static final String KEY_WATER = "water";

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        HomeViewModel homeViewModel =
//                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

//        final TextView textView = binding.textHome;
//        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences sharedPreferencesCalories = requireContext().getSharedPreferences(PREFS_CALORIES, Context.MODE_PRIVATE);

        String firstName = sharedPreferences.getString(KEY_FIRSTNAME, null);
        String calories = sharedPreferencesCalories.getString(KEY_CALORIES, null);
        String water = sharedPreferencesCalories.getString(KEY_WATER, null);

        welcomeText = root.findViewById(R.id.welcomeText);
        caloriesValue = root.findViewById(R.id.caloriesValue);
        welcomeText.setText("Welcome, " + firstName.substring(0, 1).toUpperCase() + firstName.substring(1));

        if (calories == null || water == null) {
            // If either value is -1, it means the user hasn't set them yet.
            caloriesValue.setText("No data available.");
        } else {
            // If both values are set, display them
            int num1 = Integer.parseInt(calories);
            int num2 = Integer.parseInt(water);
            int sum = num1 + num2;
            caloriesValue.setText(String.valueOf(sum) + " Calories");
        }

        Log.d("calories", String.valueOf(calories));
        Log.d("water", String.valueOf(water));


        return root;


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}