package com.mbostic.employeeslist;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import static com.mbostic.employeeslist.DBHelper.EMPLOYEES_COLUMN_BIRTH_DATE;
import static com.mbostic.employeeslist.DBHelper.EMPLOYEES_COLUMN_GENDER;
import static com.mbostic.employeeslist.DBHelper.EMPLOYEES_COLUMN_SALARY;
import static com.mbostic.employeeslist.DBHelper.GENDER_MALE;

/**
 * Activity which displays analytics of all employees.
 */
public class AnalyticsActivity extends AppCompatActivity {

    /**
     * Calculates the median and average age of the users, max salary and male to female employee ratio
     * It puts the calculated information into the appropriate views.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analytics);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DBHelper dbHelper = new DBHelper(this);
        Cursor cursor = dbHelper.getAllEmployees();
        List<Integer> ages = new ArrayList<>();
        int ageSum = 0;
        while(!cursor.isAfterLast()){
            String dateStr = cursor.getString(cursor.getColumnIndex(EMPLOYEES_COLUMN_BIRTH_DATE));
            LocalDate birthDate = null;
            try {
                birthDate = new SimpleDateFormat("dd.MM.yyyy").parse(dateStr).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            int age = Period.between(birthDate, LocalDate.now()).getYears();
            ageSum += age;
            ages.add(age);
            cursor.moveToNext();
        }
        float avgAge = ((float)ageSum) / ((float)ages.size());
        ((TextView)findViewById(R.id.average_age)).setText(String.format(Locale.US, "%.2f", avgAge));
        ((TextView)findViewById(R.id.median_age)).setText(String.format(Locale.US, "%.2f", median(ages)));

        cursor.moveToFirst();
        float maxSalary = 0;
        while(!cursor.isAfterLast()){
            float salary = cursor.getFloat(cursor.getColumnIndex(EMPLOYEES_COLUMN_SALARY));
            if(salary > maxSalary){
                maxSalary = salary;
            }
            cursor.moveToNext();
        }
        ((TextView)findViewById(R.id.max_salary)).setText(String.format(Locale.US, "%.2f", maxSalary));

        cursor.moveToFirst();
        float maleNum = 0;
        float femaleNum = 0;
        while(!cursor.isAfterLast()){
            int gender = cursor.getInt(cursor.getColumnIndex(EMPLOYEES_COLUMN_GENDER));
            if(gender == GENDER_MALE){
                maleNum++;
            } else {
                femaleNum++;
            }
            cursor.moveToNext();
        }
        ((TextView)findViewById(R.id.male_vs_female_ratio)).setText(String.format(Locale.US, "%.2f", maleNum/femaleNum));

        cursor.close();
    }

    /**
     * Calculates the median of the input array of integers
     * @param values Values from which to calculate the median
     * @return Calculated median
     */
    static double median(List<Integer> values) {
        // sort array
        Collections.sort(values);
        double median;
        // get count of scores
        int totalElements = values.size();
        // check if total number of scores is even
        if (totalElements % 2 == 0) {
            int sumOfMiddleElements = values.get(totalElements / 2) +
                    values.get(totalElements / 2 - 1);
            // calculate average of middle elements
            median = ((double) sumOfMiddleElements) / 2;
        } else {
            // get the middle element
            median = (double) values.get(values.size() / 2);
        }
        return median;
    }
}
