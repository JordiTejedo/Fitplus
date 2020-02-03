package com.example.jordi.fitplus;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Jordi on 06/08/2018.
 */

public class SignUpFirstFragment extends Fragment {

    EditText name_et, last_name_et, email_et, password_et, confirm_password_et;
    Button resume_b;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sign_up_first_fragment, container, false);

        name_et = view.findViewById(R.id.name_et);
        last_name_et = view.findViewById(R.id.last_name_et);
        email_et = view.findViewById(R.id.email_et);
        password_et = view.findViewById(R.id.password_et);
        confirm_password_et = view.findViewById(R.id.confirm_password_et);

        resume_b = view.findViewById(R.id.resume_b);
        resume_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name, lastName, email, password, confirmPassword;
                name = name_et.getText().toString();
                lastName = last_name_et.getText().toString();
                email = email_et.getText().toString();
                password = password_et.getText().toString();
                confirmPassword = confirm_password_et.getText().toString();

                if(name.isEmpty()) {
                    name_et.setError(getString(R.string.empty_name));
                    name_et.requestFocus();
                } else if (lastName.isEmpty()) {
                    last_name_et.setError(getString(R.string.empty_last_name));
                    last_name_et.requestFocus();
                } else if (email.isEmpty()) {
                    email_et.setError(getString(R.string.empty_email));
                    email_et.requestFocus();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    email_et.setError(getString(R.string.wrong_email));
                    email_et.requestFocus();
                } else if (password.isEmpty()) {
                    password_et.setError(getString(R.string.empty_password));
                    password_et.requestFocus();
                } else if (password.length() < 6) {
                    password_et.setError(getString(R.string.wrong_password));
                    password_et.requestFocus();
                } else if (!confirmPassword.equals(password)) {
                    confirm_password_et.setError(getString(R.string.password_no_match));
                    confirm_password_et.requestFocus();
                } else {
                    ((SignUpActivity) getActivity()).user.name = name;
                    ((SignUpActivity) getActivity()).user.lastName = lastName;
                    ((SignUpActivity) getActivity()).user.email = email;
                    ((SignUpActivity) getActivity()).password = password;
                    ((SignUpActivity) getActivity()).setmViewPager(1);
                }
            }
        });

        return view;
    }
}
