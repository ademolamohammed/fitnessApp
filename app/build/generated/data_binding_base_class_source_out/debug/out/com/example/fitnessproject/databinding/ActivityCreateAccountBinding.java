// Generated by view binder compiler. Do not edit!
package com.example.fitnessproject.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.fitnessproject.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityCreateAccountBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final Button createAccountButton;

  @NonNull
  public final ConstraintLayout main;

  @NonNull
  public final EditText passwordEditText;

  @NonNull
  public final EditText usernameEditText;

  private ActivityCreateAccountBinding(@NonNull ConstraintLayout rootView,
      @NonNull Button createAccountButton, @NonNull ConstraintLayout main,
      @NonNull EditText passwordEditText, @NonNull EditText usernameEditText) {
    this.rootView = rootView;
    this.createAccountButton = createAccountButton;
    this.main = main;
    this.passwordEditText = passwordEditText;
    this.usernameEditText = usernameEditText;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityCreateAccountBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityCreateAccountBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_create_account, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityCreateAccountBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.createAccountButton;
      Button createAccountButton = ViewBindings.findChildViewById(rootView, id);
      if (createAccountButton == null) {
        break missingId;
      }

      ConstraintLayout main = (ConstraintLayout) rootView;

      id = R.id.passwordEditText;
      EditText passwordEditText = ViewBindings.findChildViewById(rootView, id);
      if (passwordEditText == null) {
        break missingId;
      }

      id = R.id.usernameEditText;
      EditText usernameEditText = ViewBindings.findChildViewById(rootView, id);
      if (usernameEditText == null) {
        break missingId;
      }

      return new ActivityCreateAccountBinding((ConstraintLayout) rootView, createAccountButton,
          main, passwordEditText, usernameEditText);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}