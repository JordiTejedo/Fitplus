package com.example.jordi.fitplus;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

public class LoginActivity extends AppCompatActivity {

    EditText email_et, password_et;
    Button login_b;

    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email_et = (EditText) findViewById(R.id.email_et);
        password_et = (EditText) findViewById(R.id.password_et);
        login_b = (Button) findViewById(R.id.login_b);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
    }

    public void onLoginClick (View view) {
        String email = email_et.getText().toString();
        String password = password_et.getText().toString();

        if (email.isEmpty()) {
            email_et.setError(getString(R.string.empty_email));
            email_et.requestFocus();
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            email_et.setError(getString(R.string.wrong_email));
            email_et.requestFocus();
        } else if (password.isEmpty()) {
            password_et.setError(getString(R.string.empty_password));
            password_et.requestFocus();
        } else if (password.length() < 6) {
            password_et.setError(getString(R.string.too_short_password));
            password_et.requestFocus();
        } else {
            final ProgressDialog progress = new ProgressDialog(this);
            progress.setMessage(getString(R.string.connecting));
            progress.show();
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        myRef.child("Users").child(task.getResult().getUser().getUid()).child("lastLogin").setValue(new Date());

                        Intent intent = new Intent(getApplicationContext(), SlideMenuActivity.class);
                        startActivity(intent);
                        progress.dismiss();
                        finish();
                    } else {
                        progress.dismiss();

                        try {
                            throw task.getException();
                        } catch (FirebaseAuthInvalidCredentialsException e) {
                            password_et.setError(getString(R.string.wrong_password));
                            password_et.requestFocus();
                        } catch (FirebaseAuthInvalidUserException e) {
                            email_et.setError(getString(R.string.not_found_email));
                            email_et.requestFocus();
                        } catch (Exception e) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                            builder.setMessage(getString(R.string.error_signing_up_user))
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
    }

    public void onCreateUserClick (View view) {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

    public void onForgotPassword (View view) {
        Intent intent = new Intent(getApplicationContext(), ForgottenPasswordActivity.class);
        startActivity(intent);
    }
}
