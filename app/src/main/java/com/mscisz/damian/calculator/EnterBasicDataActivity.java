package com.mscisz.damian.calculator;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import java.util.Calendar;

public class EnterBasicDataActivity extends AppCompatActivity {

    private EditText inputName;
    private EditText inputWeight;
    private EditText inputGrowth;
    private RadioButton radioButtonMale;
    private RadioButton radioButtonFemale;
    private Spinner spinnerActivityLevel;
    private TextView inputDateBirth;
    private EditText inputTargetWeight;
    private EditText inputWeightLossByWeek;
    private RadioGroup sexRadioGroup;
    int day, month, year;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_basic_data);

        inputName = (EditText) findViewById(R.id.inputName);
        inputWeight = (EditText) findViewById(R.id.inputWeight);
        inputGrowth = (EditText) findViewById(R.id.inputGrowth);
        radioButtonMale = (RadioButton) findViewById(R.id.radioButtonMale);
        radioButtonFemale = (RadioButton) findViewById(R.id.radioButtonFemale);
        spinnerActivityLevel = (Spinner) findViewById(R.id.spinnerActivityLevel);
        inputDateBirth = (TextView) findViewById(R.id.inputDateBirth);
        inputTargetWeight = (EditText) findViewById(R.id.inputTargetWeight);
        inputWeightLossByWeek = (EditText) findViewById(R.id.inputWeightLossByWeek);
        sexRadioGroup = (RadioGroup) findViewById(R.id.sexRadioGroup);

        setDate();
    }

    private boolean validateInputName(){
        String pInputName = inputName.getText().toString().trim();

        if(pInputName.isEmpty()){
            inputName.setError("Wpisz imię");
            return false;
        }else if(!pInputName.matches("[a-zA-Z]{3,}")){
            inputName.setError("Błędne imię");
            return false;
        }else{
            return true;
        }
    }

    private boolean validateInputWeight(){
        String pInputWeight = inputWeight.getText().toString().trim();

        if(pInputWeight.isEmpty()){
            inputWeight.setError("Wpisz wagę");
            return false;
        }else if(Double.parseDouble(pInputWeight)>240){
            inputWeight.setError("Wartość wagi jest nieprawidłowa");
            return false;
        }else{
            return true;
        }
    }

    private boolean validateInputGrowth(){
        String pInputGrowth = inputGrowth.getText().toString().trim();

        if(pInputGrowth.isEmpty()){
            inputGrowth.setError("Wpisz wzrost");
            return false;
        }else if(Integer.parseInt(pInputGrowth)>230){
            inputGrowth.setError("Wartość wzrostu jest nieprawidłowa");
            return false;
        }else{
            return true;
        }
    }

    private boolean validateSexRadioGroup(){
        if(radioButtonFemale.isChecked() || radioButtonMale.isChecked()) {
            return true;
        }else{
            radioButtonFemale.setError("Wybierz płeć");
            return false;
        }
    }

    private boolean validateInputWeightLossByWeek(){
        String tmp = inputWeightLossByWeek.getText().toString();
        if(tmp.isEmpty()){
            inputWeightLossByWeek.setError("Wpisz wagę do utraty w ciągu tygodnia");
            return false;
        }
        else if(Double.parseDouble(tmp)>3.1){
            inputWeightLossByWeek.setError("Waga do utraty jest zbyt duża");
            return false;
        }else{
            return true;
        }
    }

    private boolean validateInputTargetWeight(){
        String targetWeight = inputTargetWeight.getText().toString().trim();
        String weight = inputWeight.getText().toString().trim();

        if(targetWeight.isEmpty()){
            inputTargetWeight.setError("Wpisz wagę docelową");
            return false;
        }else if(Double.parseDouble(targetWeight)>Double.parseDouble(weight)){
            inputTargetWeight.setError("Waga docelowa jest większa niż obecna");
            return false;
        }else if((Double.parseDouble(weight)-Double.parseDouble(targetWeight)) < Double.parseDouble(inputWeightLossByWeek.getText().toString())){
            inputTargetWeight.setError("Za duży tygodniowy cel do zrzucenia");
            return false;
        }else{
            return true;
        }

    }

    private boolean validateInputDate(){
        if(inputDateBirth.getText().toString().isEmpty()){
            inputDateBirth.setError("Wybierz datę urodzenia");
            return false;
        }else{
            return true;
        }
    }

    private void setDate(){

        Calendar mCurrentDate;
        mCurrentDate = Calendar.getInstance();
        day = mCurrentDate.get(Calendar.DAY_OF_MONTH);
        month = mCurrentDate.get(Calendar.MONTH)+1;
        year = mCurrentDate.get(Calendar.YEAR);

        inputDateBirth.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                DatePickerDialog datePicker = new DatePickerDialog(EnterBasicDataActivity.this,AlertDialog.THEME_HOLO_DARK,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                inputDateBirth.setText(String.valueOf(day)+"/"+String.valueOf(month)+"/"+String.valueOf(year));
                            }
                        }, year, month, day);
                datePicker.show();
            }
        });
    }

    public void buttonConfirmBasicData(View v) {

        if (!validateInputName() || !validateInputWeight() || !validateInputGrowth()
                || !validateSexRadioGroup() || !validateInputWeightLossByWeek() || !validateInputTargetWeight()
                || !validateInputDate()) {
            return;
        } else {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Działa! Idź dalej xD");
            alert.create().show();
        }
    }
}
