package com.example.jordi.fitplus;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import com.example.jordi.fitplus.Models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

public class SignUpActivity extends AppCompatActivity {

    User user;
    String password;

    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private DatabaseReference myRef;

    private ViewPager mViewPager;

    SectionsStatePagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();

        mViewPager = findViewById(R.id.fragments_container_vp);

        user = new User();

        setupViewPager(mViewPager);
    }

    @Override
    public void onBackPressed() {
        if (mViewPager.getCurrentItem() == 1) {
            setmViewPager(0);
        }else{
            finish();
        }
    }

    public void signUp () {
        mAuth.createUserWithEmailAndPassword(user.email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    user.creationDate = new Date();
                    user.lastLoginDate = new Date();
                    user.type = "user";
                    myRef.child("Users").child(task.getResult().getUser().getUid()).setValue(user);

                    Intent intent = new Intent(getApplicationContext(), SlideMenuActivity.class);
                    intent.putExtra("FROM_ACTIVITY", "SignUpActivity");
                    startActivity(intent);
                    finish();
                } else {
                    try {
                        throw task.getException();
                    } catch(FirebaseAuthWeakPasswordException e) {
                        EditText password_et = adapter.getItem(0).getView().findViewById(R.id.password_et);
                        password_et.setError(getString(R.string.too_short_password));
                        password_et.requestFocus();
                        setmViewPager(0);
                    } catch(FirebaseAuthInvalidCredentialsException e) {
                        EditText email_et = adapter.getItem(0).getView().findViewById(R.id.email_et);
                        email_et.setError(getString(R.string.email_invalid));
                        email_et.requestFocus();
                        setmViewPager(0);
                    } catch(FirebaseAuthUserCollisionException e) {
                        EditText email_et = adapter.getItem(0).getView().findViewById(R.id.email_et);
                        email_et.setError(getString(R.string.email_exist));
                        email_et.requestFocus();
                        setmViewPager(0);
                    } catch(Exception e) {
                        Log.e("Error creating user", e.getMessage());

                        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                        builder.setMessage(getString(R.string.error_creating_user))
                                .setCancelable(false)
                                .setPositiveButton(getString(R.string.accept), new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                }
            }
        });
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new SectionsStatePagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new SignUpFirstFragment(), "Fragment1");
        adapter.addFragment(new SignUpSecondFragment(), "Fragment2");
        viewPager.setAdapter(adapter);
    }

    public void setmViewPager(int fragmentNumber) {
        mViewPager.setCurrentItem(fragmentNumber);
    }
}
