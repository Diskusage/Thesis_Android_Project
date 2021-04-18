package com.example.sqliteapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    //references to layout controls
    Button btn_login, btn_toDB;
    EditText et_fName, et_sName, et_IDNP;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_login = findViewById(R.id.btn_login);
        btn_toDB = findViewById(R.id.btn_toDB);
        et_fName = findViewById(R.id.et_firstName);
        et_sName = findViewById(R.id.et_secondName);
        et_IDNP = findViewById(R.id.et_IDNP);
        databaseHelper = new DatabaseHelper(MainActivity.this);

        //button listeners
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_fName.getText().toString().length() == 0 || et_sName.getText().toString().length() == 0  || et_IDNP.getText().toString().length() == 0 ){
                    Toast.makeText(MainActivity.this, "Please fill all the fields.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (databaseHelper.checkLogin(new PersonModel(et_fName.getText().toString(), et_sName.getText().toString(), et_IDNP.getText().toString()))){
                    Intent intent = new Intent(MainActivity.this, Logged_in_2nd.class);
                    String[] toPut = databaseHelper.getFromDb(et_IDNP.getText().toString());
                    intent.putExtra("VaccinationType", toPut[0]);
                    intent.putExtra("VaccinationDate", toPut[1]);
                    intent.putExtra("IDNP", toPut[2]);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(MainActivity.this, "No account found", Toast.LENGTH_SHORT).show();
                }

            }
        });

        btn_toDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, DatabaseActivity.class);
                startActivity(intent);


            }
        });


    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        databaseHelper = null;
    }


}