package com.example.jordi.fitplus;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class ForgottenPasswordActivity extends AppCompatActivity {

    EditText email_et;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotten_password);

        email_et = (EditText) findViewById(R.id.email_et);

        mAuth = FirebaseAuth.getInstance();

        FirebaseAuth auth = FirebaseAuth.getInstance();
    }

    public void onSendClick (View view) {
        String email = email_et.getText().toString();

        if (email.isEmpty()) {
            email_et.setError(getString(R.string.empty_email));
            email_et.requestFocus();
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            email_et.setError(getString(R.string.wrong_email));
            email_et.requestFocus();
        } else {
            final ProgressDialog progress = new ProgressDialog(this);
            progress.setMessage(getString(R.string.wait_please));
            progress.show();
            mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        progress.dismiss();
                        Toast.makeText(ForgottenPasswordActivity.this, getString(R.string.message_sent), Toast.LENGTH_LONG).show();
                        finish();
                    } else {
                        progress.dismiss();

                        try {
                            throw task.getException();
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
}
