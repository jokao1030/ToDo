package com.bignerdranch.android.todo.RoomDB;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {ToDo.class},version = 1)
public abstract class RoomDB extends RoomDatabase {


    public abstract ToDoDao mToDoDao();
    public static RoomDB mRoomDBInstance;

    public static RoomDB getInstance(Context context){
           if(mRoomDBInstance == null){
               mRoomDBInstance = Room.databaseBuilder(context,RoomDB.class,"ToDoDatabase")
                               .fallbackToDestructiveMigration()
                               .allowMainThreadQueries()
                               .build();
           }
           return mRoomDBInstance;
    }
}
