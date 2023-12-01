package com.example.taskmanagementsqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.taskmanagementsqlite.DBHelper;

public class UserDAO {

    private SQLiteDatabase database;
    private DBHelper dbHelper;

    public UserDAO(Context context) {
        dbHelper = new DBHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    // Insert a new user into the database
    public long insertUser(User user) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_USERNAME, user.getUsername());
        values.put(DBHelper.COLUMN_PASSWORD, user.getPassword());
        return database.insert(DBHelper.TABLE_USERS, null, values);
    }

    // Check if a user with the given username and password exists
    public boolean authenticateUser(String username, String password) {
        String[] columns = {DBHelper.COLUMN_ID};
        String selection = DBHelper.COLUMN_USERNAME + " = ? AND " + DBHelper.COLUMN_PASSWORD + " = ?";
        String[] selectionArgs = {username, password};
        Cursor cursor = database.query(DBHelper.TABLE_USERS, columns, selection, selectionArgs, null, null, null);

        boolean result = cursor.moveToFirst();
        cursor.close();
        return result;
    }
}
