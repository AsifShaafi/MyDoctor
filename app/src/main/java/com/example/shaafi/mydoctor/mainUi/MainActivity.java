package com.example.shaafi.mydoctor.mainUi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.shaafi.mydoctor.R;

public class MainActivity extends AppCompatActivity {

    public static String USER_MODE;
    public static final String DOCTOR = "doctor";
    public static final String PATIENT = "patient";

    RadioGroup choiceRadioGroup;
    RadioButton doctorRadio;
    RadioButton patientRadio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        choiceRadioGroup = (RadioGroup) findViewById(R.id.choiceGroup);
        doctorRadio = (RadioButton) findViewById(R.id.doctor_radioButton);
        patientRadio = (RadioButton) findViewById(R.id.patient_radioButton);


        radioGroupCheckChangeListener();
    }

    private void radioGroupCheckChangeListener() {
        choiceRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.doctor_radioButton:
                        //Toast.makeText(getBaseContext(), "Doctor selected", Toast.LENGTH_SHORT).show();
                        doctorRadio.setBackgroundResource(R.drawable.radio_button_selected_bg);
                        patientRadio.setBackgroundResource(R.drawable.radio_button_bg);
                        break;
                    case R.id.patient_radioButton:
                        //Toast.makeText(getBaseContext(), "Patient selected", Toast.LENGTH_SHORT).show();
                        patientRadio.setBackgroundResource(R.drawable.radio_button_selected_bg);
                        doctorRadio.setBackgroundResource(R.drawable.radio_button_bg);
                        break;
                }
            }
        });
    }

    private boolean checkOptionSelected() {

        int selectedId = choiceRadioGroup.getCheckedRadioButtonId();
        switch (selectedId) {
            case R.id.doctor_radioButton:
                //Toast.makeText(getBaseContext(), "Doctor selected", Toast.LENGTH_SHORT).show();
                USER_MODE = DOCTOR;
                return true;
            case R.id.patient_radioButton:
                //Toast.makeText(getBaseContext(), "Patient selected", Toast.LENGTH_SHORT).show();
                USER_MODE = PATIENT;
                return true;
            default:
                return false;
        }
    }

    public void proceedToNextStep(View view) {
        if (checkOptionSelected()) {
            startActivity(new Intent(this, LoginActivity.class));
        } else {
            Toast.makeText(this, "Please select a option first.", Toast.LENGTH_LONG).show();
        }
    }
}
