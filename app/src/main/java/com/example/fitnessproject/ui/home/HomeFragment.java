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
    TextView welcomeText;
    private static final String PREFS_NAME = "LoginPrefs";
    private static final String KEY_FIRSTNAME = "firstName";
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
        String firstName = sharedPreferences.getString(KEY_FIRSTNAME, null);

        welcomeText = root.findViewById(R.id.welcomeText);
        welcomeText.setText("Welcome, " + firstName.substring(0, 1).toUpperCase() + firstName.substring(1));

        Log.d("firstName", firstName);
        Log.d("testing", firstName);
        return root;


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}