package com.example.sqliteapp;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class DatabaseActivity extends AppCompatActivity {
    //references to layout controls
    final int IDNP_LENGTH = 13;
    EditText db_IDNP, db_sName, db_fName, db_vaccine;
    Button  db_add, db_back;
    ListView db_lView;
    ArrayAdapter personArrayAdapter;
    DatabaseHelper databaseHelper;
    TextView db_date_selector;
    DatePickerDialog.OnDateSetListener db_dateListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.database_screen);

        db_IDNP = findViewById(R.id.database_IDNP);
        db_sName = findViewById(R.id.database_secondName);
        db_fName = findViewById(R.id.database_firstName);
        db_vaccine = findViewById(R.id.database_vacc);
        db_add = findViewById(R.id.btn_add);
        db_back = findViewById(R.id.db_back);
        db_lView = findViewById(R.id.database_listView);
        db_date_selector = findViewById(R.id.date_id);
        databaseHelper = new DatabaseHelper(DatabaseActivity.this);
        ShowCustomersOnList(databaseHelper);


        db_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        db_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isDateSet = false;
                if (!db_date_selector.getText().equals("Date of vaccination"))
                    isDateSet = true;
                if (!checkFields())
                    return;
                PersonModel personModel = null;
                try {
                    if (db_vaccine.length() == 0)
                        personModel = new PersonModel(db_fName.getText().toString(), db_sName.getText().toString(), db_IDNP.getText().toString());
                    else if (isDateSet)
                        personModel = new PersonModel(db_fName.getText().toString(), db_sName.getText().toString(), db_IDNP.getText().toString(), db_vaccine.getText().toString(), db_date_selector.getText().toString() );
                    else Toast.makeText(DatabaseActivity.this, "Choose the date", Toast.LENGTH_SHORT).show();
                }
                catch (VaccineDoesntExist e){
                    Toast.makeText(DatabaseActivity.this, "Enter correct vaccine name", Toast.LENGTH_SHORT).show();
                }
                catch (Exception e){
                    Toast.makeText(DatabaseActivity.this, "Error adding a person", Toast.LENGTH_SHORT).show();
                }
                if (personModel == null)
                    return;
                DatabaseHelper databaseHelper = new DatabaseHelper(DatabaseActivity.this);
                boolean success = false;
                try {
                    success = databaseHelper.addOne(personModel);
                }
                catch (MatchingVaccinationException e){
                    Toast.makeText(DatabaseActivity.this, "This person was already vaccinated on that date", Toast.LENGTH_SHORT).show();
                }
                catch (EmptyRecordAfterExistingVaccinationException e){
                    Toast.makeText(DatabaseActivity.this, "Can't add an empty record if the person was vaccinated in the past", Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(DatabaseActivity.this, "Success= " + success, Toast.LENGTH_SHORT).show();
                ShowCustomersOnList(databaseHelper);
            }
        });

        db_lView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PersonModel clickedPerson = (PersonModel) parent.getItemAtPosition(position);
                databaseHelper.deleteOne(clickedPerson);
                ShowCustomersOnList(databaseHelper);
                Toast.makeText(DatabaseActivity.this, "Deleted " + clickedPerson.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        db_date_selector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR), month = cal.get(Calendar.MONTH), day = cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(DatabaseActivity.this, android.R.style.Theme_Holo_Light_Dialog, db_dateListener, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        db_dateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month++;
                String date = dayOfMonth + "/" + month + "/" + year;
                db_date_selector.setText(date);

            }
        };
    }


    private void ShowCustomersOnList(DatabaseHelper databaseHelper2) {
        personArrayAdapter = new ArrayAdapter<PersonModel>(DatabaseActivity.this, android.R.layout.simple_list_item_1, databaseHelper2.getAll());
        db_lView.setAdapter(personArrayAdapter);
    }

    private boolean checkFields(){
        if (db_IDNP.length() == 0 || db_fName.length() == 0 || db_sName.length() == 0){
            Toast.makeText(DatabaseActivity.this, "Please fill all the fields.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (db_IDNP.length() != IDNP_LENGTH){
            Toast.makeText(DatabaseActivity.this, "IDNP should have 13 characters", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        databaseHelper = null;
    }
    public Vaccines sToEn(String s) throws VaccineDoesntExist{
        s = s.toLowerCase();
        for (Vaccines c: Vaccines.values()){
            if (c.name().toLowerCase().equals(s))
                return c;
        }
        throw new VaccineDoesntExist();
    }
}
