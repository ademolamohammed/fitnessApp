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

import com.example.fitnessproject.R;
import com.example.fitnessproject.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {
    TextView welcomeText, caloriesValue, sleepValue, stepValueTextView;
    private static final String PREFS_NAME = "LoginPrefs";
    private static final String PREFS_CALORIES = "caloriesPref";
    private static final String PREFS_SLEEP = "SleepGoalPrefs";

    private static final String KEY_FIRSTNAME = "firstName";
    private static final String KEY_CALORIES = "calories";
    private static final String KEY_WATER = "water";
    private static final String KEY_SLEEP_TIME = "sleep_goal_time";

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Retrieve SharedPreferences
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences sharedPreferencesCalories = requireContext().getSharedPreferences(PREFS_CALORIES, Context.MODE_PRIVATE);
        SharedPreferences sharedPreferencesSleep = requireContext().getSharedPreferences(PREFS_SLEEP, Context.MODE_PRIVATE);
        SharedPreferences stepSharedPreferences = requireContext().getSharedPreferences("myPrefs", Context.MODE_PRIVATE);

        // Retrieve values
        String firstName = sharedPreferences.getString(KEY_FIRSTNAME, null);
        String calories = sharedPreferencesCalories.getString(KEY_CALORIES, null);
        String water = sharedPreferencesCalories.getString(KEY_WATER, null);
        String sleepGoalTime = sharedPreferencesSleep.getString(KEY_SLEEP_TIME, "Not set");

        float savedSteps = stepSharedPreferences.getFloat("key1", 0f); // Default value is 0 if no data is saved

        // Find UI components
        welcomeText = root.findViewById(R.id.welcomeText);
        caloriesValue = root.findViewById(R.id.caloriesValue);
        sleepValue = root.findViewById(R.id.sleepValue); // Assume sleepValue is defined in the layout
        stepValueTextView = root.findViewById(R.id.stepCovered);

        // Set welcome text
        if (firstName != null && !firstName.isEmpty()) {
            welcomeText.setText("Welcome, " + firstName.substring(0, 1).toUpperCase() + firstName.substring(1));
        } else {
            welcomeText.setText("Welcome, User");
        }

        // Display Calories and Water
        if (calories == null || water == null) {
            caloriesValue.setText("No calorie data available.");
        } else {
            int num1 = Integer.parseInt(calories);
            int num2 = Integer.parseInt(water);
            int sum = num1 + num2;
            caloriesValue.setText(String.valueOf(sum) + " Calories");
        }

        // Display Steps
        if (savedSteps > 0) {
            stepValueTextView.setText(String.format("Steps: %.0f", savedSteps));
        } else {
            stepValueTextView.setText("No step data available.");
        }

        // Display Sleep Goal
        sleepValue.setText("Sleep Goal: " + sleepGoalTime);

        Log.d("HomeFragment", "Calories: " + calories);
        Log.d("HomeFragment", "Water: " + water);
        Log.d("HomeFragment", "Sleep Goal: " + sleepGoalTime);
        Log.d("HomeFragment", "Steps: " + savedSteps);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
