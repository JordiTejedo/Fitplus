package com.example.jordi.fitplus;

import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.jordi.fitplus.Models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SettingsFragment extends Fragment {

    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private DatabaseReference myRef;

    User user;

    TextView change_name_tv, change_last_name_tv, change_email_tv, change_telephone_tv, change_address_tv, change_postal_code_tv;
    LinearLayout change_name_ll, change_last_name_ll;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        change_name_tv = view.findViewById(R.id.change_name_tv);
        change_last_name_tv = view.findViewById(R.id.change_last_name_tv);
        change_email_tv = view.findViewById(R.id.change_email_tv);
        change_telephone_tv = view.findViewById(R.id.change_telephone_tv);
        change_address_tv = view.findViewById(R.id.change_address_tv);
        change_postal_code_tv = view.findViewById(R.id.change_postal_code_tv);

        change_name_ll = view.findViewById(R.id.change_name_ll);
        change_name_ll.setOnClickListener(onChangeNameListener);

        change_last_name_ll = view.findViewById(R.id.change_last_name_ll);
        change_last_name_ll.setOnClickListener(onChangeLastNameListener);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        DatabaseReference reference = myRef.child("Users").child(currentUser.getUid());

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                user = dataSnapshot.getValue(User.class);

                change_name_tv.setText(user.name);
                change_last_name_tv.setText(user.lastName);
                change_email_tv.setText(user.email);
                change_telephone_tv.setText(user.phone);
                change_address_tv.setText(user.address);
                change_postal_code_tv.setText(user.postalCode);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };

        reference.addValueEventListener(postListener);

        return view;
    }


    View.OnClickListener onChangeNameListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            DialogFragment changeNameDialogFragment = ChangeNameDialogFragment.newInstance(user.name);
            changeNameDialogFragment.show(getFragmentManager(), "");
        }
    };

    View.OnClickListener onChangeLastNameListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            DialogFragment changeLastNameDialogFragment = ChangeLastNameDialogFragment.newInstance(user.lastName);
            changeLastNameDialogFragment.show(getFragmentManager(), "");
        }
    };
}
