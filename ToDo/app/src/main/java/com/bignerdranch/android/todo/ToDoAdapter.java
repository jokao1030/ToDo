package com.bignerdranch.android.todo;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import com.bignerdranch.android.todo.RoomDB.RoomDB;
import com.bignerdranch.android.todo.RoomDB.ToDo;
import com.bignerdranch.android.todo.todoremote.RequestHandler;
import com.bignerdranch.android.todo.todoremote.ToDoR;
import com.google.android.material.card.MaterialCardView;

import java.text.SimpleDateFormat;
import java.util.*;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.ToDoViewHolder>{

    Context context;
    List<ToDo> mToDoList;
    RoomDB mRoomDB;
    AlertDialog alertDialog;
    //RequestHandler mRequestHandler = new RequestHandler();
    protected static String logger = "ToDoAdapter";
    ToDoR mToDoR = new ToDoR();
    List<ToDoR> mToDoRList;
    public ToDoAdapter(Context context, List<ToDo> todoList, RoomDB roomDB , List<ToDoR> todorList){
        this.context = context;
        this.mToDoList = todoList;
        this.mRoomDB = roomDB;
        this.mToDoRList = todorList;
    }


    @NonNull
    @Override
    public ToDoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.to_do_list_item, parent, false);
        return new ToDoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ToDoViewHolder holder, int position) {
        //Get the ToDoModel from List
        final ToDo toDoModel= mToDoList.get(position);
        //Set Data for Remote Data
        mToDoR = mToDoRList.get(position);
        //Set the text (Values from ToDoModel) for the TextViews (Name and Description) in ViewHolder
        holder.toDoItem.setText(toDoModel.getItem());
        holder.toDoDes.setText(toDoModel.getDescription());
        //Format for the Date and Time
        final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm a", Locale.ENGLISH);
        //Set the text (Values from ToDoModel) for the TextViews (Date and Time) in ViewHolder
        String strDate = dateFormat.format(toDoModel.getDate());
        holder.toDoDate.setText(strDate);


        //The overdue ToDoItems have red edge and item name in red
        mToDoList.get(position).setIsRed();
        if(mToDoList.get(position).isRed()){
            holder.mCardView.setStrokeColor(Color.parseColor("#DD2C00"));
            holder.toDoItem.setTextColor(Color.parseColor("#DD2C00"));

        }else{
            holder.mCardView.setStrokeColor(Color.parseColor("#2A52BE"));
            holder.toDoItem.setTextColor(Color.parseColor("#2A52BE"));
        }

        String strTime = timeFormat.format(toDoModel.getTime());
        holder.toDoTime.setText(strTime);


        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context);
                builder.setCancelable(true);
                builder.setMessage("Are you sure you want to delete?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //Continue with delete
                        mRoomDB.mToDoDao().delete(toDoModel);
                        mToDoList=mRoomDB.mToDoDao().collectList();
                        //new DeleteAsync().execute();
                        notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //Close the dialog
                        dialog.cancel();
                    }
                });
                builder.show();
            }
        });

        holder.completed.setChecked(toDoModel.isCompleted());
        holder.favorite.setChecked(toDoModel.isFavorite());
        //Paint the ToDoName if it is completed
        if(toDoModel.isCompleted()){
            holder.toDoItem.setPaintFlags(holder.toDoItem.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }else{
            holder.toDoItem.setPaintFlags(0);
        }

        //holder.dfSort.setChecked(toDoModel.whichSort() == 2);
        //holder.fdSort.setChecked(toDoModel.whichSort() == 3);




        holder.completed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox checkBox = (CheckBox)v;
                if(checkBox.isChecked()){
                    toDoModel.setCompleted(true);
                    mToDoR.setCompleted(true);
                    checkBox.setChecked(true);
                }else{
                    toDoModel.setCompleted(false);
                    mToDoR.setCompleted(false);
                }
                mRoomDB.mToDoDao().update(toDoModel);
                mToDoList=mRoomDB.mToDoDao().collectList();
                notifyDataSetChanged();
            }
        });
        holder.favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox checkBox = (CheckBox)v;
                if(checkBox.isChecked()){
                    toDoModel.setFavorite(true);
                    mToDoR.setFavorite(true);
                }else{
                    toDoModel.setFavorite(false);
                    mToDoR.setFavorite(false);
                }
                mRoomDB.mToDoDao().update(toDoModel);
                mToDoList=mRoomDB.mToDoDao().collectList();
                notifyDataSetChanged();
            }
        });

        //Detail view called by edit button
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context);
                builder.setCancelable(true);
                //Inflate the detail view
                View nView = LayoutInflater.from(context).inflate(R.layout.updateitem_layout, null);
                //Name field
                final EditText itemName = nView.findViewById(R.id.itemName);
                itemName.setText(toDoModel.getItem());
                //Description field
                final EditText description = nView.findViewById(R.id.description);
                description.setText(toDoModel.getDescription());
                //Completed Checkbox
                final CheckBox completedU = nView.findViewById(R.id.complete_u);
                completedU.setChecked(toDoModel.isCompleted());
                //Favorite Checkbox
                final CheckBox favoriteU = nView.findViewById(R.id.favorite_u);
                favoriteU.setChecked(toDoModel.isFavorite());
                //Date field
                final TextView toDoDate = nView.findViewById(R.id.toDoDate);
                toDoDate.setText(dateFormat.format(toDoModel.getDate()));
                //Time field
                final TextView toDoTime = nView.findViewById(R.id.toDoTime);
                toDoTime.setText(timeFormat.format(toDoModel.getTime()));
                Button setToDoDate = nView.findViewById(R.id.toDoDateBtn);
                setToDoDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        java.util.Calendar calendar = java.util.Calendar.getInstance();
                        int year = calendar.get(Calendar.YEAR);
                        int month = calendar.get(Calendar.MONTH);
                        int day = calendar.get(Calendar.DAY_OF_MONTH);
                        DatePickerDialog datePickerDialog;
                        datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                final java.util.Calendar c = java.util.Calendar.getInstance(Locale.ENGLISH);
                                c.set(java.util.Calendar.YEAR, year);
                                c.set(java.util.Calendar.MONTH, month);
                                c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                final String strDate = dateFormat.format(c.getTime());
                                toDoDate.setText(strDate);
                                toDoModel.setDate(c.getTime());
                                mToDoR.setDate(strDate);
                            }
                        }, year, month, day);
                        datePickerDialog.show();
                    }
                });

                Button setToDoTime = nView.findViewById(R.id.toDoTimeBtn);
                setToDoTime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Calendar calendar = java.util.Calendar.getInstance();
                        int hours = calendar.get(Calendar.HOUR_OF_DAY);
                        int minutes = calendar.get(Calendar.MINUTE);
                        TimePickerDialog timePickerDialog;
                        timePickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                Calendar c = Calendar.getInstance();
                                c.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                c.set(Calendar.MINUTE, minute);
                                c.setTimeZone(TimeZone.getDefault());
                                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm a", Locale.ENGLISH);
                                String strTime = timeFormat.format(c.getTime());
                                toDoTime.setText(strTime);
                                toDoModel.setTime(c.getTime());
                                mToDoR.setTime(strTime);
                            }
                        }, hours, minutes, false);
                        timePickerDialog.show();
                    }
                });

                Button deleteItem = nView.findViewById(R.id.deleteItemBtn);
                deleteItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context);
                        builder.setCancelable(true);
                        builder.setMessage("Are you sure you want to delete?");
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //Continue with delete
                                mRoomDB.mToDoDao().delete(toDoModel);
                                mToDoList=mRoomDB.mToDoDao().collectList();
                                notifyDataSetChanged();
                                //new DeleteAsync().execute();
                                alertDialog.dismiss();
                            }
                        });
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //Close the dialog
                                dialog.cancel();
                            }
                        });
                        builder.show();
                    }
                });

                Button updateItem = nView.findViewById(R.id.saveNewItemBtn);
                updateItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String item = itemName.getText().toString();
                        if (TextUtils.isEmpty(item)) {
                            Toast.makeText(context, "Insert a Task Name!", Toast.LENGTH_SHORT).show();
                        } else if (toDoModel.getDate() == null) {
                            Toast.makeText(context, "Select a Date!", Toast.LENGTH_SHORT).show();
                        }else if(toDoModel.getTime() == null){
                            Toast.makeText(context, "Select Time!", Toast.LENGTH_SHORT).show();
                        } else {
                                String des = description.getText().toString();
                                toDoModel.setItem(item);
                                toDoModel.setDescription(des);
                                toDoModel.setCompleted(completedU.isChecked());
                                toDoModel.setFavorite(favoriteU.isChecked());
                                mRoomDB.mToDoDao().update(toDoModel);
                                mToDoList=mRoomDB.mToDoDao().collectList();
                                notifyDataSetChanged();
                                //new UpdateAsync().execute();
                                alertDialog.dismiss();
                        }
                    }
                });
                builder.setView(nView);
                alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }


    @Override
    public int getItemCount() {
        return mToDoList.size();
    }


    class ToDoViewHolder extends RecyclerView.ViewHolder{

        TextView toDoItem,toDoDate,toDoTime,toDoDes;
        Button edit,delete;
        CheckBox completed;
        CheckBox favorite;
        MaterialCardView mCardView;


        public ToDoViewHolder(@NonNull View itemView) {
            super(itemView);
            toDoItem = itemView.findViewById(R.id.toDoItem);
            toDoDate = itemView.findViewById(R.id.toDoDate);
            toDoTime = itemView.findViewById(R.id.toDoTime);
            edit = itemView.findViewById(R.id.edit);
            delete = itemView.findViewById(R.id.delete);
            completed = itemView.findViewById(R.id.complete);
            favorite = itemView.findViewById(R.id.favorite);
            toDoDes = itemView.findViewById(R.id.toDoDes);
            mCardView = (MaterialCardView) itemView.findViewById(R.id.card);
        }

    }

    //Asynchronous class for deleting  todos
    //Here not working...
    /*public class DeleteAsync extends AsyncTask<ToDoR,Void, Boolean> {
        @Override
        protected Boolean doInBackground(ToDoR... todor) {

            try{
                boolean temp = mRequestHandler.deleteToDo(mToDoR.getId());
                return temp;
            } catch (Exception e) {
                Log.e(logger, "deleteToDo(): got exception: " + e);
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean temp) {
            if (temp == true) {
                Log.i(logger,"delete todo:" + mToDoR.toString());
                mToDoRList.remove(mToDoR);
            }else{
                Log.i(logger,"Null!");
            }
        }
    }

    //Asynchronous class for updating  todos
    public class UpdateAsync extends AsyncTask<ToDoR,Void, ToDoR> {
        @Override
        protected ToDoR doInBackground(ToDoR... todor) {

            try{
                mRequestHandler.updateToDo(mToDoR);
                return mToDoR;
            } catch (Exception e) {
                Log.e(logger, "updateToDo(): got exception: " + e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(ToDoR todor) {
            if (todor == null) {
                Log.i(logger,"update todo:" + todor.toString());
            }else{
                Log.i(logger,"Null!");
            }
        }
    }*/

}
