package com.bignerdranch.android.todo;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import com.bignerdranch.android.todo.RoomDB.RoomDB;
import com.bignerdranch.android.todo.RoomDB.ToDo;
import com.bignerdranch.android.todo.todoremote.RequestHandler;
import com.bignerdranch.android.todo.todoremote.ToDoR;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class NewToDoActivity extends AppCompatActivity {

    private Button mSaveBt;
    private EditText mItemName, mDescription;
    private TextView mDate, mTime;
    private CheckBox mCompleteU, mFavoriteU;
    private RoomDB mRoomDB;
    final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
    final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm a", Locale.ENGLISH);
    private RequestHandler mRequestHandler = new RequestHandler();
    protected static String logger = "NewItemActivity";
    final ToDo mToDo = new ToDo();
    final ToDoR mToDoR = new ToDoR();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addnewitem);
        //Set Fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        //Go back to MainActivity when the back button on toolbar is clicked
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewToDoActivity.this, ToDoListActivity.class);
                startActivity(intent);
                finish();
            }
        });

        mSaveBt = (Button) findViewById(R.id.saveNewItemBtn);
        mItemName = (EditText) findViewById(R.id.itemName);
        mDescription = (EditText) findViewById(R.id.description);
        mDate = (TextView) findViewById(R.id.toDoDate);
        mTime = (TextView) findViewById(R.id.toDoTime);
        mCompleteU = (CheckBox) findViewById(R.id.complete_u);
        mFavoriteU = (CheckBox) findViewById(R.id.favorite_u);
        mRoomDB = RoomDB.getInstance(getApplicationContext());



        Button setToDoDate = findViewById(R.id.toDoDateBtn);
        setToDoDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                java.util.Calendar calendar = java.util.Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog;
                datePickerDialog = new DatePickerDialog(NewToDoActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        final java.util.Calendar c = java.util.Calendar.getInstance(Locale.ENGLISH);
                        c.set(java.util.Calendar.YEAR, year);
                        c.set(java.util.Calendar.MONTH, month);
                        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        final String strDate = dateFormat.format(c.getTime());
                        mDate.setText(strDate);
                        mToDoR.setDate(strDate);
                        mToDo.setDate(c.getTime());
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });

        Button setToDoTime = findViewById(R.id.toDoTimeBtn);
        setToDoTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = java.util.Calendar.getInstance();
                int hours = calendar.get(Calendar.HOUR_OF_DAY);
                int minutes = calendar.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog;
                timePickerDialog = new TimePickerDialog(NewToDoActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        Calendar c = Calendar.getInstance();
                        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        c.set(Calendar.MINUTE, minute);
                        c.setTimeZone(TimeZone.getDefault());
                        String strTime = timeFormat.format(c.getTime());
                        mTime.setText(strTime);
                        mToDoR.setTime(strTime);
                        mToDo.setTime(c.getTime());
                    }
                }, hours, minutes, false);
                timePickerDialog.show();
            }
        });

        mSaveBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String item = mItemName.getText().toString();
                if (TextUtils.isEmpty(item)) {
                    Toast.makeText(NewToDoActivity.this, "Insert Item Name!", Toast.LENGTH_SHORT).show();
                } else {
                    String des = mDescription.getText().toString();
                    if (mToDo.getDate() == null) {
                        Toast.makeText(NewToDoActivity.this, "Select Date!", Toast.LENGTH_SHORT).show();
                    } else if(mToDo.getTime() == null){
                        Toast.makeText(NewToDoActivity.this, "Select Time!", Toast.LENGTH_SHORT).show();
                    }else {
                        mToDo.setItem(item);
                        mToDo.setDescription(des);
                        mToDo.setCompleted(mCompleteU.isChecked());
                        mToDo.setFavorite(mFavoriteU.isChecked());
                        mToDoR.setItem(item);
                        mToDoR.setDescription(des);
                        mToDoR.setCompleted(mCompleteU.isChecked());
                        mToDoR.setFavorite(mFavoriteU.isChecked());
                        mRoomDB.mToDoDao().insert(mToDo);
                        new SaveAsync().execute();
                        startActivity(new Intent(NewToDoActivity.this, ToDoListActivity.class));
                        finish();
                    }
                }
            }
        });

    }

    //Asynchronous class for creating new todos
    public class SaveAsync extends AsyncTask<ToDoR,Void, ToDoR> {
        @Override
        protected ToDoR doInBackground(ToDoR... todor) {

            try{
                return mRequestHandler.createToDo(mToDoR);
            } catch (Exception e) {
                Log.e(logger, "createToDo(): got exception: " + e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(ToDoR todor) {
            if (todor != null) {
                Log.i(logger,"create todo!");
            }else{
                Log.i(logger,"Null!");
            }
        }
    }
}