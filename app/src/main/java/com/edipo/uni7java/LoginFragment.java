package com.edipo.uni7java;

import android.app.Application;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.edipo.uni7java.local.AppDatabase;
import com.edipo.uni7java.local.CredentialsDAO;

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
        if (etName.getEditText() == null || etPassword.getEditText() == null) {
            return;
        }
        String name = etName.getEditText().getText().toString();
        if (TextUtils.isEmpty(name)) {
            etName.setError(getString(R.string.error_name_empty));
            return;
        }
        String password = etPassword.getEditText().getText().toString();
        if (TextUtils.isEmpty(password)) {
            etPassword.setError(getString(R.string.error_password_empty));
            return;
        }
        if (getActivity() != null) {
            LoginTask loginTask = new LoginTask(getActivity().getApplication());
            loginTask.execute(name, password);
        }
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

    private static final class LoginTask extends AsyncTask<String, Void, String> {

        private final Application application;

        private LoginTask(Application application) {
            this.application = application;
        }

        @Override
        protected String doInBackground(String... params) {
            String name = params[0];
            String password = params[1];
            CredentialsDAO dao = AppDatabase.getInstance(application).getDatabase().getCredentialsDAO();
            if (dao.findByNamePassword(name, password) != null) {
                return name;
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(String username) {
            if (username != null) {
                application.startActivity(WeatherInfoActivity.getIntent(application, username));
            } else {
                Toast.makeText(application, R.string.error_credentials_invalid, Toast.LENGTH_LONG).show();
            }
        }

    }

}
