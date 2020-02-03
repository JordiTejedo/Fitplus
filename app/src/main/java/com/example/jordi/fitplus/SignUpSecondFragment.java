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

import java.util.regex.Pattern;

/**
 * Created by Jordi on 06/08/2018.
 */

public class SignUpSecondFragment extends Fragment {

    EditText dni_nie_et, phone_et, address_et, postal_code_et;
    Button end_b;
    String dniRegexp = "\\d{8}[A-HJ-NP-TV-Z]";
    String nieRegexp = "\\d[XYZ][0-9]{7}[A-Z]";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sign_up_second_fragment, container, false);

        dni_nie_et = view.findViewById(R.id.dni_nie_et);
        phone_et = view.findViewById(R.id.phone_et);
        address_et = view.findViewById(R.id.address_et);
        postal_code_et = view.findViewById(R.id.postal_code_et);

        end_b = view.findViewById(R.id.end_b);
        end_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String dniNie, phone, address, postalCode;
                dniNie = dni_nie_et.getText().toString();
                phone = phone_et.getText().toString();
                address = address_et.getText().toString();
                postalCode = postal_code_et.getText().toString();

                if(dniNie.isEmpty()) {
                    dni_nie_et.setError(getString(R.string.empty_dni_nie));
                    dni_nie_et.requestFocus();
                } else if (!Pattern.matches(dniRegexp, dniNie) && !Pattern.matches(nieRegexp, dniNie)) {
                    dni_nie_et.setError(getString(R.string.wrong_dni_nie));
                    dni_nie_et.requestFocus();
                } else if(phone.isEmpty()) {
                    phone_et.setError(getString(R.string.empty_phone));
                    phone_et.requestFocus();
                } else if (!Patterns.PHONE.matcher(phone).matches()) {
                    phone_et.setError(getString(R.string.wrong_phone));
                    phone_et.requestFocus();
                } else if(address.isEmpty()) {
                    address_et.setError(getString(R.string.empty_address));
                    address_et.requestFocus();
                } else if(postalCode.isEmpty()) {
                    postal_code_et.setError(getString(R.string.empty_postal_code));
                    postal_code_et.requestFocus();
                } else {
                    ((SignUpActivity) getActivity()).user.dniNie = dniNie;
                    ((SignUpActivity) getActivity()).user.phone = phone;
                    ((SignUpActivity) getActivity()).user.address = address;
                    ((SignUpActivity) getActivity()).user.postalCode = postalCode;
                    ((SignUpActivity) getActivity()).signUp();
                }

            }
        });

        return view;
    }
}
