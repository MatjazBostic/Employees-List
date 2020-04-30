package com.mbostic.employeeslist;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * This activity enables the user to add a new employee. It contains a form into which the user enters
 * new employee's details. It also contains a floating action button, for saving the new employee and
 * then exiting the activity.
 */
public class AddEmployeeActivity extends AppCompatActivity {

    private TextView birthDateTV;

    /**
     * Sets up the birthday date label
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_employee);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        birthDateTV = findViewById(R.id.birthDateTV);

        updateBirthDateLabel();
    }

    private final Calendar birthDateCalendar = Calendar.getInstance();

    private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            birthDateCalendar.set(Calendar.YEAR, year);
            birthDateCalendar.set(Calendar.MONTH, monthOfYear);
            birthDateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateBirthDateLabel();
        }

    };

    /**
     * Sets up birth date label from the local variable "birthDateCalendar"
     * It is used after the variable is changed.
     */
    private void updateBirthDateLabel(){
        String myFormat = "dd.MM.yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        birthDateTV.setText(sdf.format(birthDateCalendar.getTime()));
    }

    /**
     * Opens a dialog for choosing the birth date of the new employee and then saves the date into
     * the local variable "birthDateCalendar"
     * @param view
     */
    public void onClickEditBirthDate(View view) {
        DatePickerDialog dialog = new DatePickerDialog(this, dateSetListener, birthDateCalendar
                .get(Calendar.YEAR), birthDateCalendar.get(Calendar.MONTH),
                birthDateCalendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
        dialog.getDatePicker().getTouchables().get( 0 ).performClick();
    }

    /**
     * Checks if new employee's information is well formatted, and then saves that information into the database.
     * After that, the activity is closed.
     * @param view
     */
    public void onClickSaveEmployee(View view) {
        DBHelper dbHelper = new DBHelper(this);

        String name = ((EditText)findViewById(R.id.nameET)).getText().toString();
        if(name.isEmpty()){
            Toast.makeText(this, R.string.name_empty_msg, Toast.LENGTH_LONG).show();
            return;
        }

        String salaryStr = ((EditText)findViewById(R.id.salaryET)).getText().toString();
        float salary;
        try {
            salary = Float.parseFloat(salaryStr);
        } catch (NumberFormatException ex){
            Toast.makeText(this, R.string.invalid_salary_format_msg, Toast.LENGTH_LONG).show();
            return;
        }

        dbHelper.insertEmployee(name,
                ((RadioButton)findViewById(R.id.radio_male)).isChecked() ? DBHelper.GENDER_MALE : DBHelper.GENDER_FEMALE,
                birthDateTV.getText().toString(),
                salary);
        finish();
    }
}
