package com.bignerdranch.android.todo.RoomDB;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.Calendar;
import java.util.Date;

@Entity(tableName = "todo")
public class ToDo {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;
    @ColumnInfo(name = "item")
    private String item;
    @ColumnInfo(name = "description")
    private String description;
    @ColumnInfo(name = "date")
    @TypeConverters(DateConverter.class)
    private Date date;
    @ColumnInfo(name = "time")
    @TypeConverters(DateConverter.class)
    private Date time;
    @ColumnInfo(name = "completed")
    private boolean completed;
    @ColumnInfo(name = "favorite")
    private boolean favorite;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    //Attribute and Method only for marking overdue tasks
    boolean isRed;
    public void setIsRed(){
        Date today = Calendar.getInstance().getTime();
        if(this.getDate().compareTo(today) < 0 && this.getTime().compareTo(today) < 0) {
            this.isRed = true;
        }else{
            this.isRed = false;
        }
    }
    public boolean isRed(){
        return isRed;
    }

}
