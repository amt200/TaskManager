package com.myapplicationdev.android.taskmanager;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddTaskActivity extends AppCompatActivity {

    EditText etName, etDescription, etSeconds;
    Button btnAdd, btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        etName = findViewById(R.id.etName);
        etDescription = findViewById(R.id.etDescription);
        etSeconds = findViewById(R.id.etSeconds);

        btnAdd = findViewById(R.id.btnAdd);
        btnCancel = findViewById(R.id.btnCancel);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBHelper db = new DBHelper(AddTaskActivity.this);
                if(etName.getText().toString().length() > 0 && etDescription.getText().toString().length() > 0 && etSeconds.getText().toString().length() > 0){
                    String name = etName.getText().toString();
                    String description = etDescription.getText().toString();
                    int seconds = Integer.parseInt(etSeconds.getText().toString());
                    db.insertTask(name, description);
                    db.close();
                    setResult(RESULT_OK);
                    Toast.makeText(AddTaskActivity.this, "Inserted successfully", Toast.LENGTH_LONG).show();
                    finish();
                }
                else{
                    Toast.makeText(AddTaskActivity.this, "Incomplete data", Toast.LENGTH_LONG).show();
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

}
