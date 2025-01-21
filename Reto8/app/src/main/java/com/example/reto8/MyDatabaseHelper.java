package com.example.reto8;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class MyDatabaseHelper extends SQLiteOpenHelper {


    private Context context;
    public static final String DATABASE_NAME="DIRECTORIO.DB";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "tblempresas";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_NOMBRE = "nombre";
    private static final String COLUMN_URL = "url";
    private static final String COLUMN_TELEFONO = "telefono";
    private static final String COLUMN_MAIL = "mail";
    private static final String COLUMN_PRODUCTO="producto";
    private static final String COLUMN_CLASE="clase";

    public MyDatabaseHelper(@Nullable Context context) {
        super(context, TABLE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME +
                " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NOMBRE + " TEXT, " +
                COLUMN_URL + " TEXT, " +
                COLUMN_TELEFONO + " TEXT, " +
                COLUMN_MAIL + " TEXT, " +
                COLUMN_PRODUCTO + " TEXT, " +
                COLUMN_CLASE + " TEXT);";
        db.execSQL(query);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);

    }
    void addEmpresa(String nombre, String url, String telefono, String mail, String producto, String clase){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_NOMBRE, nombre);
        cv.put(COLUMN_URL, url);
        cv.put(COLUMN_TELEFONO, telefono);
        cv.put(COLUMN_MAIL, mail);
        cv.put(COLUMN_PRODUCTO, producto);
        cv.put(COLUMN_CLASE, clase);

        long result = db.insert(TABLE_NAME,null, cv);
        if(result == -1){
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "Added Successfully!", Toast.LENGTH_SHORT).show();
        }
    }
    Cursor readAllData(){
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    void updateData(String row_id, String nombre, String url, String telefono, String mail, String producto, String clase){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_NOMBRE, nombre);
        cv.put(COLUMN_URL, url);
        cv.put(COLUMN_TELEFONO, telefono);
        cv.put(COLUMN_MAIL, mail);
        cv.put(COLUMN_PRODUCTO, producto);
        cv.put(COLUMN_CLASE, clase);

        long result = db.update(TABLE_NAME, cv, "_id=?", new String[]{row_id});
        if(result == -1){
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "Updated Successfully!", Toast.LENGTH_SHORT).show();
        }

    }

    void deleteOneRow(String row_id){
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_NAME, "_id=?", new String[]{row_id});
        if(result == -1){
            Toast.makeText(context, "Failed to Delete.", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Successfully Deleted.", Toast.LENGTH_SHORT).show();
        }
    }

    public Cursor readDataByClase(String clase) {
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_CLASE + " = ?";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, new String[]{clase});
        }
        return cursor;
    }

}
