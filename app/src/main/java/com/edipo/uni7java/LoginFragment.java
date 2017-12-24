package com.edipo.uni7java;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class LoginFragment extends Fragment {

    private TextInputLayout etName;
    private TextInputLayout etPassword;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etName = view.findViewById(R.id.edit_name);
        etPassword = view.findViewById(R.id.edit_password);
        view.findViewById(R.id.button_login).setOnClickListener(v -> onLoginClick());
        view.findViewById(R.id.button_register).setOnClickListener(v -> onRegisterClick());
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null && activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
    }

    private void onLoginClick() {

    }

    private void onRegisterClick() {
        if (getFragmentManager() != null) {
            getFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.set_slide_from_right, R.anim.set_slide_to_left,
                            android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                    .replace(R.id.container_login, new RegisterFragment())
                    .addToBackStack(null)
                    .commit();
        }
    }

}
