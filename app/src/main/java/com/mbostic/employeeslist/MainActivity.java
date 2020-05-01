package com.mbostic.employeeslist;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import static android.widget.AdapterView.*;
import static com.mbostic.employeeslist.DBHelper.EMPLOYEES_COLUMN_NAME;

/**
 * This activity shows a list of employee names. When the user clicks on an employee,
 * he is redirected to a new activity which shows the selected employee's details.
 * The activity has 2 floating action buttons. One for viewing of analytics of all employees,
 * and the second for adding a new employee.
 */
public class MainActivity extends AppCompatActivity {

    private List employees;
    private ArrayAdapter employeesAdapter;

    /**
     * Sets up the employees' list view
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ListView employeesLV = findViewById(R.id.employee_list_view);

        employees = new ArrayList();

        employeesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, employees);
        employeesLV.setAdapter(employeesAdapter);

        employeesLV.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Opens an activity for viewing details of the selected employee
                Intent i = new Intent(MainActivity.this, EmployeeProfileActivity.class);
                // Employee Id equals employee's position in the list + 1 (positions starts with 0 and employees' Ids start with 1)
                // This is assuming that deletion of employees is not possible
                i.putExtra("employeeId", position+1);
                startActivity(i);
            }
        });
    }

    /**
     * Refreshes the list of employees, when application comes into view
     */
    @Override
    protected void onStart(){
        super.onStart();

        DBHelper dbHelper = new DBHelper(this);
        employees.clear();
        Cursor cursor = dbHelper.getAllEmployees();
        while(!cursor.isAfterLast()){
            employees.add(cursor.getString(cursor.getColumnIndex(EMPLOYEES_COLUMN_NAME)));
            cursor.moveToNext();
        }
        cursor.close();
        employeesAdapter.notifyDataSetChanged();
    }

    /**
     * Opens an activity for adding a new employee
     * @param view
     */
    public void onClickAddEmployee(View view) {
        Intent i = new Intent(this, AddEmployeeActivity.class);
        startActivity(i);
    }

    /**
     * Opens an activity for viewing analytics
     * @param view
     */
    public void onClickAnalytics(View view) {
        Intent i = new Intent(this, AnalyticsActivity.class);
        startActivity(i);
    }
}
