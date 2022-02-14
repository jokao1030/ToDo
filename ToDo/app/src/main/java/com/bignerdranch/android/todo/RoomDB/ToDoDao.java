package com.bignerdranch.android.todo.RoomDB;


import androidx.room.*;

import java.util.List;

@Dao
public interface ToDoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ToDo toDoListTable);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(ToDo toDoListTable);

    @Delete()
    void delete(ToDo toDoListTable);

    @Query("select * from todo ORDER BY completed DESC")
    List<ToDo> collectList();

    @Query("select * from todo ORDER BY completed DESC, favorite DESC, date ASC")
    List<ToDo> collectListFD();

    @Query("select * from todo ORDER BY completed DESC, date ASC , favorite DESC")
    List<ToDo> collectListDF();

    @Query("DELETE FROM todo")
    void deleteAllItems();
}
