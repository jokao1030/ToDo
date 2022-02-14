package com.bignerdranch.android.todo;

/**
 * toolbar reference from https://guides.codepath.com/android/using-the-app-toolbar
 */

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.bignerdranch.android.todo.RoomDB.RoomDB;
import com.bignerdranch.android.todo.RoomDB.ToDo;
import com.bignerdranch.android.todo.todoremote.ToDoR;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ToDoListActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RoomDB mRoomDB;
    private RecyclerView mRecyclerView;
    private List<ToDo> mToDoList = new ArrayList<>();
    private List<ToDoR> mToDoRList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Set Fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);


        //Find the toolbar in the activity layout
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
        //Set the toolbar to act as the ActionBar for this Activity window.
        //Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(toolbar);

        mSwipeRefreshLayout = findViewById(R.id.swipe);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        //Initialize the FB addItem
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        //Call the NewItemActivity when addItem FB clicked
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ToDoListActivity.this, NewToDoActivity.class));
                finish();
            }
        });

        mRoomDB = mRoomDB.getInstance(getApplicationContext());

        mRecyclerView = findViewById(R.id.toDoRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);
        switch(whichSort()){
            case 1:
                collectToDo();
            case 2:
                collectToDoDF();
            case 3:
                collectToDoFD();
        }


    }

    //Menu icons are inflated just as they were with actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Adds items to the action bar
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    //Handle the options
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_default:
                collectToDo();
                item.setChecked(true);
                return true;
            case R.id.menu_df:
                collectToDoDF();
                item.setChecked(true);
                Toast.makeText(this, "Todo List sort by Date and Favorite", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.menu_fd:
                collectToDoFD();
                item.setChecked(true);
                Toast.makeText(this,"Todo List sort by Favorite and Date" , Toast.LENGTH_SHORT).show();
                return true;
            default:
                return true;
        }
    }

    //The ToDoItems sort by Complete, the uncompleted ToDoItems are on the top
    private void collectToDo(){
        mSwipeRefreshLayout.setRefreshing(true);
        mToDoList = mRoomDB.mToDoDao().collectList();
        for(int i = 0; i < mToDoList.size() ; i++){
            ToDoR todor;
            todor = converter(mToDoList.get(i));
            mToDoRList.add(todor);
        }
        RecyclerView.Adapter adapter = new ToDoAdapter(ToDoListActivity.this,mToDoList,mRoomDB, mToDoRList);
        mRecyclerView.setAdapter(adapter);
        setSort(1);
        adapter.notifyDataSetChanged();
        mSwipeRefreshLayout.setRefreshing(false);
    }

    //By User Choice: sort first by Complete and then Favorite, Date
    private void collectToDoFD(){
        mSwipeRefreshLayout.setRefreshing(true);
        mToDoList = mRoomDB.mToDoDao().collectListFD();
        RecyclerView.Adapter adapter = new ToDoAdapter(ToDoListActivity.this,mToDoList,mRoomDB,mToDoRList);
        mRecyclerView.setAdapter(adapter);
        setSort(2);
        adapter.notifyDataSetChanged();
        mSwipeRefreshLayout.setRefreshing(false);
    }

    //By User Choice: sort first by Complete and then Date, Favorite
    private void collectToDoDF(){
        mSwipeRefreshLayout.setRefreshing(true);
        mToDoList = mRoomDB.mToDoDao().collectListDF();
        RecyclerView.Adapter adapter = new ToDoAdapter(ToDoListActivity.this,mToDoList,mRoomDB,mToDoRList);
        mRecyclerView.setAdapter(adapter);
        setSort(3);
        adapter.notifyDataSetChanged();
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        switch(whichSort()){
            case 1:
                collectToDo();
            case 2:
                collectToDoDF();
            case 3:
                collectToDoFD();
        }
    }

    //Sort Way
    private int sort = 1;
    public void setSort(int sort){
        this.sort = sort;
    }
    public int whichSort(){
        return sort;
    }

    //Convert ToDo Object to ToDoR Object
    private ToDoR converter(ToDo todo){
        ToDoR todor = new ToDoR();
        todor.setId(todo.getId());
        todor.setItem(todo.getItem());
        todor.setDescription(todo.getDescription());
        todor.setDate(todo.getDate().toString());
        todor.setTime(todo.getTime().toString());
        todor.setCompleted(todo.isCompleted());
        todor.setFavorite(todo.isFavorite());
        return todor;
    }






}