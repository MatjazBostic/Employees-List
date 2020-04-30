package com.example.employeeslist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * This class manages the database. Every time the database is accessed, it is done with this class
 */
public class DBHelper extends SQLiteOpenHelper {

    static final String DATABASE_NAME = "EMPLOYEES_LIST.db";
    static final String EMPLOYEES_TABLE_NAME = "EMPLOYEES";
    static final String EMPLOYEES_COLUMN_ID = "ID";
    static final String EMPLOYEES_COLUMN_NAME = "NAME";
    static final String EMPLOYEES_COLUMN_GENDER = "GENDER";
    static final String EMPLOYEES_COLUMN_BIRTH_DATE = "BIRTH_DATE";
    static final String EMPLOYEES_COLUMN_SALARY = "SALARY";

    static final int GENDER_MALE = 0;
    static final int GENDER_FEMALE = 1;

    /**
     * The constructor.
     * @param context The activity in which the class is constructed.
     */
    DBHelper(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }

    /**
     * Initializes the EMPLOYEES table
     * @param db the database object
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE EMPLOYEES(ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT, GENDER INTEGER, BIRTH_DATE TEXT, SALARY REAL);"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS EMPLOYEES");
        onCreate(db);
    }

    /**
     * Inserts a new employee into the table.
     * @param name Name of the employee
     * @param gender Gender of the employee (0 for male and 1 for female)
     * @param birthDate Birth date of the employee
     * @param salary Salary of the employee
     */
    void insertEmployee(String name, int gender, String birthDate, float salary){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(EMPLOYEES_COLUMN_NAME, name);
        contentValues.put(EMPLOYEES_COLUMN_GENDER, gender);
        contentValues.put(EMPLOYEES_COLUMN_BIRTH_DATE, birthDate);
        contentValues.put(EMPLOYEES_COLUMN_SALARY, salary);
        db.insert(EMPLOYEES_TABLE_NAME, null, contentValues);
    }

    /**
     * Returns the cursor which includes the user with the provided id
     * @param id Id of the user.
     * @return The cursor with requested information
     */
    Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery( "SELECT * FROM EMPLOYEES WHERE ID="+id, null );
    }

    /**
     * @return Cursor including all employees
     */
    Cursor getAllEmployees(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery( "SELECT * FROM EMPLOYEES", null );
        cursor.moveToFirst();
        return cursor;
    }

}
