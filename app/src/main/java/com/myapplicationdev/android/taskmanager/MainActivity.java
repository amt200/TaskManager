package com.myapplicationdev.android.taskmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    ArrayAdapter arrayAdapter;
    ArrayList<String> arrayList;
    Button btnAdd;
    int reqCode = 12345;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DBHelper db = new DBHelper(MainActivity.this);
        listView = findViewById(R.id.lvTasks);

        btnAdd = findViewById(R.id.btnAddTask);

        arrayList = db.getAllTasks();

        arrayAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, arrayList);

        listView.setAdapter(arrayAdapter);

        arrayAdapter.notifyDataSetChanged();

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddTaskActivity.class);
                startActivityForResult(intent, 100);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        DBHelper db = new DBHelper(MainActivity.this);
        // Only handle when 2nd activity closed normally
        //  and data contains something
        if(resultCode == RESULT_OK){
            if (requestCode == 100) {
                // Get data passed back from 2nd activity
                arrayAdapter.clear();
                ArrayList<String> tasks = db.getAllTasks();
                arrayAdapter.addAll(tasks);
                arrayAdapter.notifyDataSetChanged();
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.SECOND, data.getIntExtra("seconds", 5));

                Intent intent = new Intent(MainActivity.this, NotificationReceiver.class);

                Task task = (Task) data.getSerializableExtra("task");

                if(task != null){
                    intent.putExtra("name", task.getName());
                    intent.putExtra("desc", task.getDescription());
                }

                PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, reqCode, intent, PendingIntent.FLAG_CANCEL_CURRENT);

                AlarmManager am = (AlarmManager) getSystemService(Activity.ALARM_SERVICE);
                am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
            }
        }
    }

}
