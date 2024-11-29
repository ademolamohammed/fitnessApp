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

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentActivityBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        btnReset = root.findViewById(R.id.btnReset);
        btnSave = root.findViewById(R.id.btnSave);
        tvLastSaved = root.findViewById(R.id.tvLastSaved);


        loadData();
        resetSteps();

        sensorManager = (SensorManager) requireActivity().getSystemService(Context.SENSOR_SERVICE);

        return root;


    }

    @Override
    public void onResume() {
        super.onResume();
        running = true;

        Sensor stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if (stepSensor == null) {
            Toast.makeText(getContext(), "No sensor detected on this device", Toast.LENGTH_SHORT).show();
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
            totalSteps = sensorEvent.values[0];
            int currentSteps = (int) (totalSteps - previousTotalSteps);
            binding.tvStepsTaken.setText(String.valueOf(currentSteps));
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
                binding.tvStepsTaken.setText(String.valueOf(0));
                saveData();
                return true;
            }
        });


    }

    private void saveData() {


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                previousTotalSteps = totalSteps - (totalSteps - previousTotalSteps);

                SharedPreferences sharedPreferences = requireContext().getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putFloat("key_steps", previousTotalSteps);
                editor.putLong("key_time", System.currentTimeMillis());
                editor.apply();


                android.util.Log.d("ActivityFragment", "Saved steps: " + previousTotalSteps);
                android.util.Log.d("ActivityFragment", "Saved time: " + System.currentTimeMillis());

                Toast.makeText(getContext(), "Steps and time saved", Toast.LENGTH_SHORT).show();
                loadData();
            }
        });

    }

    private void loadData() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        previousTotalSteps = sharedPreferences.getFloat("key_steps", 0f);
        long savedTime = sharedPreferences.getLong("key_time", 0L);

        android.util.Log.d("ActivityFragment", "Loaded steps: " + previousTotalSteps);
        android.util.Log.d("ActivityFragment", "Loaded time: " + savedTime);


        if (savedTime != 0L) {
            String formattedTime = android.text.format.DateFormat.format("yyyy-MM-dd HH:mm:ss", new java.util.Date(savedTime)).toString();
            binding.tvLastSaved.setText("Last saved: " + formattedTime + ", Steps: " + previousTotalSteps);
        } else {
            binding.tvLastSaved.setText("No saved data available");
        }

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