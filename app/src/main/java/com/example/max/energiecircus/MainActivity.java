package com.example.max.energiecircus;


import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity{

    SharedPreferences SharedPreferences;
    EditText name;
    EditText klas;
    EditText aantalLampen;
    Button registratieKnop;
    Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //File name and mode. Mode "0" is private mode. File name is .java class file name.
        SharedPreferences = getApplicationContext().getSharedPreferences("MainActivity", 0);
        editor = SharedPreferences.edit();

        //Check if school is still logged in
        SharedPreferences prefs = getSharedPreferences("MainActivity", 0);
        String naamRegistratie = prefs.getString("Naam", null);
        String klasRegistratie = prefs.getString("Klas", null);
        int aantalLampenRegistratie = prefs.getInt("AantalLampen", 0);
        if (naamRegistratie != null && klasRegistratie != null && aantalLampenRegistratie != 0) {
            Intent showActivity = new Intent(this, GraphActivity.class);
            startActivity(showActivity);
        }
        //set view
        setContentView(R.layout.activity_registration);

        //declare variables
        name = (EditText) findViewById(R.id.editText_name);
        klas = (EditText) findViewById(R.id.editText_klas);
        aantalLampen = (EditText) findViewById(R.id.editText_aantalLampen);
        registratieKnop = (Button) findViewById(R.id.registratie);
    }

    public void registreren(View v) {

        String nameTxt = name.getText().toString();
        String klasTxt = klas.getText().toString();
        int aantalLampenInt;
        if (aantalLampen.getText().toString().matches("")) {
            aantalLampenInt = 0;
        } else {
            aantalLampenInt = Integer.parseInt(aantalLampen.getText().toString());

        }
        boolean inputOk = true;

        /*Fool proof: als ze iets vergeten in te vullen. Gebruik maken van Toast.*/
        if (name.getText().length() <= 0) {
            Toast.makeText(MainActivity.this, "Kies een naam", Toast.LENGTH_SHORT).show();
            inputOk = false;
        } else if (klas.getText().length() <= 0) {
            Toast.makeText(MainActivity.this, "Kies een klas", Toast.LENGTH_SHORT).show();
            inputOk = false;
        } else if (aantalLampen.getText().length() <= 0) {
            Toast.makeText(MainActivity.this, "Hoeveel lampen telt je klas?", Toast.LENGTH_SHORT).show();
            inputOk = false;
        }


        if (inputOk) {
        /*Nodig voor SharedPreferences*/
            editor.putString("Naam", nameTxt);
            editor.putString("Klas", klasTxt);
            editor.putInt("AantalLampen", aantalLampenInt);
            editor.putInt("laatsteScore", 0);
            editor.commit();

        /*Lezen van sharedPreferences*/
            SharedPreferences prefs = getSharedPreferences("MainActivity", 0);
            String naamRegistratie = prefs.getString("Naam", null);
            String klasRegistratie = prefs.getString("Klas", null);
            int aantalLampenRegistratie = prefs.getInt("AantalLampen", 0);
            if (naamRegistratie != null && klasRegistratie != null && aantalLampenRegistratie != 0) {
                Log.e("naam", naamRegistratie);
                Log.e("klas", klasRegistratie);
                Log.e("aantal lasmpen", String.valueOf(aantalLampenRegistratie));
            }

            DatabaseHelper dbh = new DatabaseHelper(this);
            Classroom classroom = new Classroom();
            classroom.setGroepsnaam(naamRegistratie);
            classroom.setClassname(klasRegistratie);
            classroom.setHighscore(String.valueOf(0)); //Stringify double value
            dbh.addClassroom(classroom);
            Log.e("ClassRoom added: ", classroom.getGroepsnaam());
            Intent showActivity = new Intent(this, GraphActivity.class);
            showActivity.putExtra("classroomObject", classroom);
            startActivity(showActivity);
        }

    }


}
