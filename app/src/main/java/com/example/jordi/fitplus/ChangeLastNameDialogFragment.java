package com.example.jordi.fitplus;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.jordi.fitplus.Models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Jordi on 21/08/2018.
 */

public class ChangeLastNameDialogFragment extends DialogFragment {

    private DatabaseReference myRef;

    EditText change_last_name_et;
    Button cancel_b, change_b;
    Context context;

    public static ChangeLastNameDialogFragment newInstance(String lastName) {
        ChangeLastNameDialogFragment frag = new ChangeLastNameDialogFragment();
        Bundle args = new Bundle();
        args.putString("lastName", lastName);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        myRef = mFirebaseDatabase.getReference().child("Users").child(currentUser.getUid());

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.dialog_fragment_change_last_name, null);
        builder.setView(dialogView);

        change_last_name_et = (EditText) dialogView.findViewById(R.id.change_last_name_et);
        change_b = (Button) dialogView.findViewById(R.id.change_b);
        cancel_b = (Button) dialogView.findViewById(R.id.cancel_b);

        change_last_name_et.setText(String.valueOf(getArguments().getString("lastName")));
        change_b.setOnClickListener(changeButtonListener);
        cancel_b.setOnClickListener(cancelButtonListener);

        context = getActivity();

        return builder.create();
    }

    private View.OnClickListener cancelButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss();
        }
    };

    private View.OnClickListener changeButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String lastName = change_last_name_et.getText().toString();

            if(lastName.isEmpty()) {
                change_last_name_et.setError(getString(R.string.empty_last_name));
                change_last_name_et.requestFocus();
            } else {
                myRef.child("lastName").setValue(lastName);
                dismiss();
            }
        }
    };
}
