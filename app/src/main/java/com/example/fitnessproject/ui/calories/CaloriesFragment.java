package com.example.fitnessproject.ui.calories;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.fitnessproject.CreateAccountActivity;
import com.example.fitnessproject.R;
import com.example.fitnessproject.databinding.FragmentCaloriesBinding;


public class CaloriesFragment extends Fragment {

    CardView cardViewCalories, cardView2;
    LinearLayout hiddenLinearLayout2, hiddenLinearLayout;
    EditText editTextNumber, editTextNumber2;
    Button button, button2;

    private FragmentCaloriesBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        CaloriesViewModel notificationsViewModel =
                new ViewModelProvider(this).get(CaloriesViewModel.class);

        binding = FragmentCaloriesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Initialize CardViews and LinearLayouts
        cardViewCalories = root.findViewById(R.id.cardViewCalories);
        hiddenLinearLayout = root.findViewById(R.id.hiddenLinearLayout);
        hiddenLinearLayout2 = root.findViewById(R.id.hiddenLinearLayout2);
        cardView2 = root.findViewById(R.id.cardView2);

        // Initialize EditTexts
        editTextNumber = root.findViewById(R.id.editTextNumber);
        editTextNumber2 = root.findViewById(R.id.editTextNumber2);

        // Initialize Buttons
        button = root.findViewById(R.id.button);
        button2 = root.findViewById(R.id.button2);

        // Set OnClickListener for cardViewCalories
        cardViewCalories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toggle the visibility of hiddenLinearLayout when CardView is clicked
                if (hiddenLinearLayout.getVisibility() == View.GONE) {
                    hiddenLinearLayout.setVisibility(View.VISIBLE);  // Show the LinearLayout
                }
            }
        });

        // Set OnClickListener for cardView2
        cardView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toggle the visibility of hiddenLinearLayout2 when CardView is clicked
                if (hiddenLinearLayout2.getVisibility() == View.GONE) {
                    hiddenLinearLayout2.setVisibility(View.VISIBLE);  // Show the LinearLayout
                }
            }
        });

        // Set OnClickListener for button (Save data to SharedPreferences)
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get values from EditTexts
                String calories = editTextNumber.getText().toString();
                String water = editTextNumber2.getText().toString();

                if (!calories.isEmpty() && !water.isEmpty()) {
                    // Save data to SharedPreferences
                    SharedPreferences sharedPreferences = requireContext().getSharedPreferences("caloriesPref", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("calories", calories);
                    editor.putString("water", water);
                    editor.apply();
                    Toast.makeText(getContext(), "Calories Successfully Added", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}