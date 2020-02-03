package com.example.jordi.fitplus;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.jordi.fitplus.Models.Training;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddTrainingFragment extends Fragment implements  DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener{

    TextView title_et, trainer_tv, min_people_tv, max_people_tv, from_tv, to_tv, description_et;
    Button create_b;

    // from = true, to = false;
    boolean fromTo;

    SimpleDateFormat dateFormat =  new SimpleDateFormat("dd/MM/yyyy H:mm");
    Calendar calendarDateSetTimeSet;

    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private DatabaseReference trainingsRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_training, container, false);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        trainingsRef = mFirebaseDatabase.getReference().child("Trainings");

        title_et = view.findViewById(R.id.title_et);

        trainer_tv = view.findViewById(R.id.trainer_tv);
        trainer_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setItems(R.array.trainers_array, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        trainer_tv.setText(getResources().getStringArray(R.array.trainers_array)[which]);
                    }
                });
                builder.show();
            }
        });

        final NumberPicker minNumberPicker = new NumberPicker(getActivity());
        minNumberPicker.setMinValue(1);
        minNumberPicker.setMaxValue(20);
        minNumberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                min_people_tv.setText(String.valueOf(i1));
            }
        });

        min_people_tv = view.findViewById(R.id.min_people_tv);
        min_people_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setView(minNumberPicker);
                builder.setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ((ViewGroup) minNumberPicker.getParent()).removeView(minNumberPicker);
                    }
                });
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ((ViewGroup) minNumberPicker.getParent()).removeView(minNumberPicker);
                    }
                });
                builder.show();
            }
        });

        final NumberPicker maxNumberPicker = new NumberPicker(getActivity());
        maxNumberPicker.setMinValue(1);
        maxNumberPicker.setMaxValue(20);
        maxNumberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                max_people_tv.setText(String.valueOf(i1));
            }
        });

        max_people_tv = view.findViewById(R.id.max_people_tv);
        max_people_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setView(maxNumberPicker);
                builder.setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ((ViewGroup) maxNumberPicker.getParent()).removeView(maxNumberPicker);
                    }
                });
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ((ViewGroup) maxNumberPicker.getParent()).removeView(maxNumberPicker);
                    }
                });
                builder.show();
            }
        });

        from_tv = view.findViewById(R.id.from_tv);
        from_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fromTo = true;

                Calendar calendar = Calendar.getInstance();

                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), AddTrainingFragment.this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });

        to_tv = view.findViewById(R.id.to_tv);
        to_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fromTo = false;

                Calendar calendar = Calendar.getInstance();

                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), AddTrainingFragment.this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });

        description_et = view.findViewById(R.id.description_et);

        create_b = view.findViewById(R.id.create_b);
        create_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title, trainer, minPeople, maxPeople, from, to, description;

                title = title_et.getText().toString();
                trainer = trainer_tv.getText().toString();
                minPeople = min_people_tv.getText().toString();
                maxPeople = max_people_tv.getText().toString();
                from = from_tv.getText().toString();
                to = to_tv.getText().toString();
                description = description_et.getText().toString();

                if (title.isEmpty()) {
                    title_et.setError(getString(R.string.empty_title));
                    title_et.requestFocus();
                } else if (trainer.isEmpty()) {
                    trainer_tv.setError(getString(R.string.empty_trainer));
                    trainer_tv.requestFocus();
                } else if (minPeople.isEmpty()) {
                    min_people_tv.setError(getString(R.string.empty_min_people));
                    min_people_tv.requestFocus();
                } else if (maxPeople.isEmpty()) {
                    max_people_tv.setError(getString(R.string.empty_max_people));
                    max_people_tv.requestFocus();
                } else if (Integer.parseInt(minPeople) > Integer.parseInt(maxPeople)) {
                    min_people_tv.setError(getString(R.string.wrong_min));
                    min_people_tv.requestFocus();
                } else if (from.isEmpty()) {
                    from_tv.setError(getString(R.string.empty_from));
                    from_tv.requestFocus();
                } else if (to.isEmpty()) {
                    to_tv.setError(getString(R.string.empty_to));
                    to_tv.requestFocus();
                } else {
                    Training training = new Training();
                    training.title = title;
                    training.trainer = trainer;
                    training.minPeople = Integer.parseInt(minPeople);
                    training.maxPeople = Integer.parseInt(maxPeople);
                    try {
                        training.from = dateFormat.parse(from);
                        training.to = dateFormat.parse(to);
                    } catch (Exception e) {
                        e.toString();
                    }
                    training.description = description;

                    trainingsRef.child("").setValue(training);

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage(getString(R.string.training_created))
                            .setCancelable(false)
                            .setPositiveButton(getString(R.string.accept), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    getActivity().getSupportFragmentManager().popBackStack();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }
        });

        return view;
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

        calendarDateSetTimeSet = Calendar.getInstance();
        calendarDateSetTimeSet.set(Calendar.YEAR, i);
        calendarDateSetTimeSet.set(Calendar.MONTH, i1 + 1);
        calendarDateSetTimeSet.set(Calendar.DAY_OF_MONTH, i2);

        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), this, calendarDateSetTimeSet.get(Calendar.HOUR_OF_DAY), calendarDateSetTimeSet.get(Calendar.MINUTE), android.text.format.DateFormat.is24HourFormat(getActivity()));
        timePickerDialog.show();
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {
        if (fromTo) {
            calendarDateSetTimeSet.set(Calendar.HOUR_OF_DAY, i);
            calendarDateSetTimeSet.set(Calendar.MINUTE, i1);

            from_tv.setText(dateFormat.format(calendarDateSetTimeSet.getTime()));
        } else {
            calendarDateSetTimeSet.set(Calendar.HOUR_OF_DAY, i);
            calendarDateSetTimeSet.set(Calendar.MINUTE, i1);

            to_tv.setText(dateFormat.format(calendarDateSetTimeSet.getTime()));
        }
    }
}
