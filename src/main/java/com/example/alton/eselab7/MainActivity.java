package com.example.alton.eselab7;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Spinner spin;
    EditText e,e2,e3;
    Button b,b1;
    RadioGroup sex;
    RadioButton selected_sex;
    ArrayList<Integer> age;
    int selected_age=0;
    SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        spin=(Spinner) findViewById(R.id.spin);
        e=(EditText) findViewById(R.id.editText);
        e2=(EditText) findViewById(R.id.editText2);
        e3=(EditText) findViewById(R.id.editText3);
        b=(Button) findViewById(R.id.button);
        b1=(Button) findViewById(R.id.button2);
        sex=(RadioGroup) findViewById(R.id.sex);
        db=openOrCreateDatabase("Health",MODE_PRIVATE,null);
        Log.i("Database:","Created");
        age=new ArrayList<Integer>();
        for(int i=0;i<99;i++)
        {
            age.add(i+1);
        }
        Log.i("Array:","Initialized");
        db.execSQL("CREATE TABLE IF NOT EXISTS PATIENT(NAME VARCHAR,NUMBER VARCHAR,AGE VARCHAR,SEX VARCHAR,HEALTH_ISSUE VARCHAR)");
        Log.i("Database:","Table created");
        spin.setOnItemSelectedListener(this);
        ArrayAdapter age1=new ArrayAdapter(this,android.R.layout.simple_spinner_item,age);
        age1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(age1);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=e.getText().toString();
                int age_entered=age.get(selected_age);
                int select_id=sex.getCheckedRadioButtonId();
                selected_sex=(RadioButton) findViewById(select_id);
                String sex=selected_sex.getText().toString();
                String number=e2.getText().toString();
                String issue=e3.getText().toString();
                db.execSQL("INSERT INTO PATIENT VALUES('"+name+"','"+number+"','"+age_entered+"','"+sex+"','"+issue+"')");
                Log.i("Database:","Entry added");
                Toast.makeText(getApplicationContext(),"Registered",+Toast.LENGTH_SHORT).show();
            }
        });
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor c= db.rawQuery("Select * from PATIENT",null);
                c.moveToPosition(-1);
                StringBuffer s=new StringBuffer();
                s.append("Name\tNumber\tAge\tSex\tIssue\n");
                while(c.moveToNext())
                {
                    s.append(c.getString(0)+"\t"+c.getString(1)+"\t"+c.getString(2)+"\t"+c.getString(3)+"\n");
                }
                showMessage("Patients",s.toString());
            }
        });
    }
    public void showMessage(String title,String message)
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setMessage(message);
        builder.setTitle(title);
        builder.show();
    }
    @Override
    public void onItemSelected(AdapterView<?> arg0, View view, int postion,long id)
    {
        selected_age=postion;
    }
    @Override
    public void onNothingSelected(AdapterView<?> arg0)
    {

    }
}
