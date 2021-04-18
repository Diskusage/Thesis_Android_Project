package com.example.sqliteapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;


public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String PERSON_TABLE = "PERSON_TABLE";
    public static final String COLUMN_PERSON_FIRSTNAME = "PERSON_FIRSTNAME";
    public static final String COLUMN_PERSON_SECONDNAME = "PERSON_SECONDNAME";
    public static final String COLUMN_PERSON_IDNP = "PERSON_IDNP";
    public static final String COLUMN_PERSON_VACCINE = "PERSON_VACCINE";
    public static final String COLUMN_VACCINATION_DATE = "PERSON_VACC_DATE";


    public DatabaseHelper(@Nullable Context context) {
        super(context, "Person DataBase", null, 1);
    }

    //called the first time you access database object
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement = "CREATE TABLE " + PERSON_TABLE + " (" + COLUMN_PERSON_FIRSTNAME + " TEXT, " + COLUMN_PERSON_SECONDNAME + " TEXT, " + COLUMN_PERSON_IDNP + " TEXT, " + COLUMN_PERSON_VACCINE + " TEXT, " + COLUMN_VACCINATION_DATE + " TEXT)";
        db.execSQL(createTableStatement);
    }

    //called when the version of the database changes (forward/backward compatability)
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean checkLogin(PersonModel personModel){
        String fName = personModel.getFirstName();
        String sName = personModel.getSecondName();
        String IDNP = personModel.getIDNP();
        String queryString = "SELECT * FROM " + PERSON_TABLE + " where " + COLUMN_PERSON_FIRSTNAME + " = '" + fName + "' AND " + COLUMN_PERSON_SECONDNAME + " = '" + sName + "' AND " + COLUMN_PERSON_IDNP + " = '" + IDNP + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);
        if (cursor.getCount() >= 1){
            cursor.close();
            db.close();
            return true;
        }
        cursor.close();
        db.close();
        return false;
    }

    public boolean addOne(PersonModel personModel) throws MatchingVaccinationException, EmptyRecordAfterExistingVaccinationException {
        if (checkExistingRecord(personModel.getIDNP(), personModel.getVacc_date())){
            throw new MatchingVaccinationException();
        }
        if (checkIDNP(personModel.getIDNP()) && personModel.getType().toString().equals("None"))
            throw new EmptyRecordAfterExistingVaccinationException();
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_PERSON_FIRSTNAME, personModel.getFirstName());
        cv.put(COLUMN_PERSON_SECONDNAME, personModel.getSecondName());
        cv.put(COLUMN_PERSON_IDNP, personModel.getIDNP());
        cv.put(COLUMN_PERSON_VACCINE, personModel.getType().toString());
        cv.put(COLUMN_VACCINATION_DATE, personModel.getVacc_date());
        long insert = db.insert(PERSON_TABLE, null, cv);
        return insert != -1;
    }

    public boolean checkIDNP(String idnp){
        String queryString = "SELECT * FROM " + PERSON_TABLE + " where " + COLUMN_PERSON_IDNP + " = " + idnp;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);
        if (cursor.getCount() <= 0){
            cursor.close();
            db.close();
            return false;
        }
        cursor.close();
        db.close();
        return true;
    }

    public boolean checkExistingRecord(String toCheck, String checkDate){
        String queryString = "SELECT * FROM " + PERSON_TABLE + " where " + COLUMN_PERSON_IDNP + " = '" + toCheck + "' AND " + COLUMN_VACCINATION_DATE + " = '" + checkDate + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);
        if (cursor.getCount() <= 0){
            cursor.close();
            db.close();
            return false;
        }
        cursor.close();
        db.close();
        return true;
    }

    public List<PersonModel> getAll(){
        List<PersonModel> returnList = new ArrayList<>();
        //get data from the database
        String queryString = "SELECT * FROM " + PERSON_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);
        if(cursor.moveToFirst()){
            //loop through results and create objects and insert into returnlist
            do {
                String personFName = cursor.getString(0);
                String personSName = cursor.getString(1);
                String personIDNP = cursor.getString(2);
                String personVaccine = cursor.getString(3);
                String personDate = cursor.getString(4);
                try {
                    PersonModel newPerson = new PersonModel(personFName, personSName, personIDNP, personVaccine, personDate);
                    returnList.add(newPerson);
                }
                catch (VaccineDoesntExist e){
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return returnList;
    }
    public List<PersonModel> getAll1Person(String idnp){
        List<PersonModel> returnList = new ArrayList<>();
        //get data from the database
        String queryString = "SELECT * FROM " + PERSON_TABLE + " WHERE " + COLUMN_PERSON_IDNP + " = " + idnp;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);
        if(cursor.moveToFirst()){
            //loop through results and create objects and insert into returnlist
            do {
                String personFName = cursor.getString(0);
                String personSName = cursor.getString(1);
                String personIDNP = cursor.getString(2);
                String personVaccine = cursor.getString(3);
                String personDate = cursor.getString(4);
                try {
                    PersonModel newPerson = new PersonModel(personFName, personSName, personIDNP, personVaccine, personDate);
                    returnList.add(newPerson);
                }
                catch (VaccineDoesntExist e){
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return returnList;
    }

    public void deleteOne(PersonModel personModel){
        //find and delete person, send false if not found
        SQLiteDatabase db = this.getWritableDatabase();
        String queryString = "DELETE FROM " + PERSON_TABLE + " WHERE " + COLUMN_PERSON_IDNP + " = " + personModel.getIDNP() + " AND " + COLUMN_VACCINATION_DATE + " = '" + personModel.getVacc_date() + "'";
        Cursor cursor = db.rawQuery(queryString, null);
        cursor.moveToFirst();
        cursor.close();
        db.close();
    }

    public String[] getFromDb(String idnp){
        String[] toReturn = new String[3];
        String queryString = "SELECT * FROM " + PERSON_TABLE + " where " + COLUMN_PERSON_IDNP + " = " + idnp;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);
        if (cursor.moveToLast()){
            toReturn[0] = cursor.getString(cursor.getColumnIndex(COLUMN_PERSON_VACCINE));
            toReturn[1] = cursor.getString(cursor.getColumnIndex(COLUMN_VACCINATION_DATE));
            toReturn[2] = idnp;
        }
        cursor.close();
        db.close();
        return toReturn;


    }



}
