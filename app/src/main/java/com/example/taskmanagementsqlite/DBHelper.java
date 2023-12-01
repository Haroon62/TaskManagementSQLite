package com.example.taskmanagementsqlite;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.example.Models.TaskModel;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME="TasksDb";
    private static final int VERSION=1;
    Context context;


    // Table name and column names
    public static final String TABLE_USERS = "users";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWORD = "password";


    private  static final String TASK_TABLE="Tasks";
    private  static final String CATEGORIES_TABLE="Categories";
    private  static final String PRIORITIES_TABLE="Priorities";
    private  static final String TASK_NAME="task_name";
    private  static final String TASK_ID="task_id";
    private  static final String DESCRIPTION="description";
    private  static final String DEADLINE="deadline";
    private  static final String PRIORITY_ID_TASK= " priority_id_task ";
    private  static final String CATEGORY_ID_TASK= " category_id_task ";
    private  static final String CATEGORY_NAME="category_name";
    private  static final String CATEGORY_ID= " category_id ";
    private  static final String PRIORITY_NAME="priority_name";
    private  static final String PRIORITY_ID= " priority_id ";



    public DBHelper(Context context) {
        super(context ,DATABASE_NAME , null, VERSION);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TASK_TABLE + " (" +
                TASK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TASK_NAME + " TEXT, " +
                DEADLINE + " TEXT, " +
                DESCRIPTION + " TEXT, " +
                CATEGORY_ID_TASK + " INTEGER, " +
                PRIORITY_ID_TASK + " INTEGER )");


        db.execSQL("CREATE TABLE " + CATEGORIES_TABLE + " (" +
                CATEGORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                CATEGORY_NAME + " TEXT)");



        db.execSQL("CREATE TABLE " + PRIORITIES_TABLE + " (" +
                PRIORITY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT , " +
                PRIORITY_NAME + " TEXT)");

        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_USERNAME + " TEXT, "
                + COLUMN_PASSWORD + " TEXT"
                + ")";
        db.execSQL(CREATE_USERS_TABLE);
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }
    public void addTask(String taskName, String taskDescription, String deadlineMillis, String categoryName, String priorityName) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Get category and priority IDs by name
        long categoryId = getCategoryIDByName(db, categoryName);
        long priorityId = getPriorityIDByName(db, priorityName);

        ContentValues values = new ContentValues();
        values.put(TASK_NAME, taskName);
        values.put(DESCRIPTION, taskDescription);
        values.put(DEADLINE, deadlineMillis);
        values.put(CATEGORY_ID_TASK, categoryId);
        values.put(PRIORITY_ID_TASK, priorityId);

        long rowseffected = db.insert(TASK_TABLE, null, values);
        if (rowseffected > 0) {
            Toast.makeText(context, "added", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Not added", Toast.LENGTH_SHORT).show();
        }
    }

    public void updateData(int id, String newName, String newDescription,String deadline,String priorityname,String catogryname) {
        SQLiteDatabase db = this.getWritableDatabase();

        long categoryId = getCategoryIDByName(db, catogryname);
        long priorityId = getPriorityIDByName(db, priorityname);

        ContentValues values = new ContentValues();
        values.put(TASK_NAME, newName);
        values.put(DESCRIPTION, newDescription);
        values.put(DEADLINE, deadline);
        values.put(CATEGORY_ID_TASK, categoryId);
        values.put(PRIORITY_ID_TASK, priorityId);

        String whereClause = TASK_ID + " = ?";
        String[] whereArgs = {String.valueOf(id)}; // Replace id with the desired row's ID

        int rowsAffected = db.update(TASK_TABLE, values, whereClause, whereArgs);

        if (rowsAffected > 0) {
            Toast.makeText(context, "Updated", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Not Updated", Toast.LENGTH_SHORT).show();
        }

        db.close();
    }


    public void deleteContact(int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        String whereClause = TASK_ID + " = ?";
        String[] whereArgs = { String.valueOf(id) };

        int rowsAffected =  db.delete(TASK_TABLE, whereClause, whereArgs);

        if (rowsAffected > 0) {
            Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, " Not Deleted", Toast.LENGTH_SHORT).show();
        }

        db.close();
    }

    public ArrayList<TaskModel> fetchData() {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT task." + TASK_ID + ", task." + TASK_NAME + ", task." + DESCRIPTION + ", task." + DEADLINE +
                ", categories." + CATEGORY_NAME + ", priorities." + PRIORITY_NAME +
                " FROM " + TASK_TABLE + " task " +
                " JOIN " + CATEGORIES_TABLE + " categories ON task." + CATEGORY_ID_TASK + " = categories." + CATEGORY_ID +
                " JOIN " + PRIORITIES_TABLE + " priorities ON task." + PRIORITY_ID_TASK + " = priorities." + PRIORITY_ID;

        Cursor cursor = db.rawQuery(query, null);
        ArrayList<TaskModel> arrayList = new ArrayList<>();
        while (cursor.moveToNext()) {
            TaskModel modelClass = new TaskModel();
            modelClass.setId(cursor.getInt(0));
            modelClass.setName(cursor.getString(1));
            modelClass.setDescription(cursor.getString(2));
            modelClass.setDeadline(cursor.getString(3));
            modelClass.setCategorname(cursor.getString(4));
            modelClass.setPeriorityname(cursor.getString(5));
            arrayList.add(modelClass);
        }
        cursor.close();
        db.close();
        return arrayList;
    }




    public long getCategoryIDByName(SQLiteDatabase db, String categoryName) {
        long categoryId = -1;

        String query = "SELECT " + CATEGORY_ID + " FROM " + CATEGORIES_TABLE + " WHERE " + CATEGORY_NAME + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{categoryName});
        Log.d("DBHelper", "Category ID for " + categoryName + ": " + categoryId);
        if (cursor.moveToFirst()) {
            categoryId = cursor.getLong(0);
        }

        cursor.close();
        return categoryId;
    }

    public long getPriorityIDByName(SQLiteDatabase db, String priorityName) {
        long priorityId = -1;

        String query = "SELECT " + PRIORITY_ID + " FROM " + PRIORITIES_TABLE + " WHERE " + PRIORITY_NAME + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{priorityName});
        Log.d("DBHelper", "Priority ID for " + priorityName + ": " + priorityId);

        if (cursor.moveToFirst()) {
            priorityId = cursor.getLong(0);
        }

        cursor.close();
        return priorityId;
    }

    public void addCategories(String category) {
        SQLiteDatabase db = this.getWritableDatabase();

            ContentValues categoryValues = new ContentValues();
            categoryValues.put(CATEGORY_NAME, category);
            db.insert(CATEGORIES_TABLE, null, categoryValues);

        db.close();
    }
    public void addPeriorities(String priority) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues priorityValues = new ContentValues();
        priorityValues.put(PRIORITY_NAME, priority);
        db.insert(PRIORITIES_TABLE, null, priorityValues);
        db.close();

    }


    public List<String> getAllCategories() {
        List<String> categories = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String[] columns = {CATEGORY_NAME};
        Cursor cursor = db.query(CATEGORIES_TABLE, columns, null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String categoryName = cursor.getString(cursor.getColumnIndex(CATEGORY_NAME));
                categories.add(categoryName);
            } while (cursor.moveToNext());

            cursor.close();
        }

        db.close();
        return categories;
    }

    public List<String> getAllPriorities() {
        List<String> categories = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String[] columns = {PRIORITY_NAME};
        Cursor cursor = db.query(PRIORITIES_TABLE, columns, null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String categoryName = cursor.getString(cursor.getColumnIndex(PRIORITY_NAME));
                categories.add(categoryName);
            } while (cursor.moveToNext());

            cursor.close();
        }

        db.close();
        return categories;
    }




}
