package com.edipo.uni7java;

import android.app.Application;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.edipo.uni7java.local.AppDatabase;
import com.edipo.uni7java.local.Credentials;
import com.edipo.uni7java.local.CredentialsDAO;

public class RegisterFragment extends Fragment {

    private TextInputLayout etName;
    private TextInputLayout etPassword;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etName = view.findViewById(R.id.edit_name);
        etPassword = view.findViewById(R.id.edit_password);
        view.findViewById(R.id.button_register).setOnClickListener(v -> onRegisterClick());
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null && activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (getFragmentManager() != null) {
                getFragmentManager().popBackStack();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void onRegisterClick() {
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
            RegisterTask registerTask = new RegisterTask(getActivity().getApplication(), getFragmentManager());
            registerTask.execute(name, password);
        }
    }

    private static class RegisterTask extends AsyncTask<String, Void, Boolean> {

        private final Application application;
        private final FragmentManager fragmentManager;

        private RegisterTask(Application application, FragmentManager fragmentManager) {
            this.application = application;
            this.fragmentManager = fragmentManager;
        }

        @Override
        protected Boolean doInBackground(String... params) {
            String name = params[0];
            String password = params[1];
            CredentialsDAO dao = AppDatabase.getInstance(application).getDatabase().getCredentialsDAO();
            if (dao.findByName(name) != null) {
                return false;
            }
            dao.insertAll(new Credentials(name, password));
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result) {
                Toast.makeText(application, R.string.success_register, Toast.LENGTH_LONG).show();
                if (fragmentManager != null) {
                    fragmentManager.popBackStack();
                }
            } else {
                Toast.makeText(application, R.string.error_name_used, Toast.LENGTH_LONG).show();
            }
        }
    }

}
