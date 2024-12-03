package com.example.fitnessproject.ui.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.fitnessproject.R;
import com.example.fitnessproject.databinding.FragmentActivityBinding;
import com.example.fitnessproject.ui.calories.CaloriesViewModel;

public class ActivityFragment extends Fragment implements SensorEventListener {

    private FragmentActivityBinding binding;
    private SensorManager sensorManager;
    private boolean running = false;
    private float totalSteps = 0f;
    private float previousTotalSteps = 0f;
    Button btnSave;
    Button btnReset;
    TextView tvLastSaved;
    Button btnClear;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentActivityBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        btnReset = root.findViewById(R.id.btnReset);
        btnSave = root.findViewById(R.id.btnSave);
        tvLastSaved = root.findViewById(R.id.tvLastSaved);
        btnClear = root.findViewById(R.id.btnClear);
        loadData();
        resetSteps();
        clearData();

        sensorManager = (SensorManager) requireActivity().getSystemService(Context.SENSOR_SERVICE);

        return root;




    }

    @Override
    public void onResume() {
        super.onResume();
        running = true;

        Sensor stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        if (stepSensor == null) {
            Toast.makeText(getContext(), "No step detector detected on this device", Toast.LENGTH_SHORT).show();
        } else {
            sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (running) {
            if (sensorEvent.sensor.getType() == Sensor.TYPE_STEP_DETECTOR) {
                // Increment the step count each time the sensor detects a step
                totalSteps++;
                binding.tvStepsTaken.setText(String.valueOf(totalSteps)); // Update the UI with the accumulated step count
            }
        }
    }

    public void resetSteps() {

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Toast.makeText(getContext(), "Long tap to reset steps", Toast.LENGTH_SHORT).show();

            }
        });

        btnReset.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                previousTotalSteps = totalSteps;
                totalSteps = 0;
                binding.tvStepsTaken.setText(String.valueOf(0));
                saveData();
                return true;
            }
        });


    }

    private void saveData() {
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = requireContext().getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putFloat("key1", previousTotalSteps);
                editor.putLong("keyTime", System.currentTimeMillis()); // Save the current time
                editor.apply();

                // Update the tvLastSaved TextView after saving
                updateLastSavedText();
                Toast.makeText(getContext(), "Data is saved", Toast.LENGTH_SHORT).show();
                // loadData();
            }
        });

    }

    private void updateLastSavedText() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        long savedTime = sharedPreferences.getLong("keyTime", 0);

        String formattedTime;
        if (savedTime != 0) {
            // Format the saved time into a readable date-time string
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault());
            formattedTime = sdf.format(new java.util.Date(savedTime));
        } else {
            formattedTime = "No save data available";
        }

        // Update the TextView with steps and time
        tvLastSaved.setText(String.format("Saved Steps: %d\nSaved At: %s", (int) previousTotalSteps, formattedTime));
    }

    private void loadData() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        previousTotalSteps = sharedPreferences.getFloat("key1", 0f);

        // Update the tvLastSaved TextView with loaded data
        updateLastSavedText();
    }

    // Set up the click listener for the Clear button



    private void clearData() {
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = requireContext().getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                // Clear the saved steps and timestamp
                editor.clear();
                editor.apply();

                // Reset both previousTotalSteps and totalSteps to 0
                previousTotalSteps = 0f;
                totalSteps = 0f;

                // Reset steps and update the UI
                binding.tvStepsTaken.setText(String.valueOf(0));

                // Update the tvLastSaved TextView to reflect cleared data
                tvLastSaved.setText("No saved data");

                Toast.makeText(getContext(), "Data cleared successfully", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        // No action needed for this use case
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}