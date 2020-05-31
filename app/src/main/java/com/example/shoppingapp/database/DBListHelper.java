package com.example.shoppingapp.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.provider.SearchRecentSuggestions;
import android.util.Log;

import com.example.shoppingapp.model.ChildList;
import com.example.shoppingapp.model.ParentList;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DBListHelper extends SQLiteOpenHelper {
    private static final String TAG = DBListHelper.class.getSimpleName();
    private static final String DB_NAME = "listitems.db";
    public static  String DATABASE_PATH = "";
    private static final int DB_VER = 3;
    private static final String TABLE_NAME = "ListItems";
    private static final String UD_COLUMN_0 = "id";
    public static final String UD_COLUMN_01 = "productlist";
    private SQLiteDatabase dbSqlite;
    private final Context myContext;




/*
     public DBListHelper(Context context) {
        super(context, DB_NAME, null, DB_VER);

    }
    public ArrayList<ChildList> getListItems(){
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        String[] sqlselect = {"title","listItems"};
        String tableName = "ListItems";

        queryBuilder.setTables(tableName);
        Cursor cursor = queryBuilder.query(db,sqlselect,null,null,null,null,null);
        ArrayList<ChildList> result = new ArrayList<>();
        if (cursor.moveToFirst()){
            do {
                ChildList list = new ChildList();
                list.setListItemsTitle(cursor.getString(cursor.getColumnIndex("listItems")));
                result.add(list);
            }while (cursor.moveToNext());

        }
        return result;
    }
public ArrayList<ParentList> getList(){
    Log.d(TAG, "getListonclick: ");
    SQLiteDatabase db = getReadableDatabase();
    SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

    String[] sqlselect = {"title"};
    String tableName = "ListItems";

    queryBuilder.setTables(tableName);
    Cursor cursor = queryBuilder.query(db,sqlselect,null,null,null,null,null);
    ArrayList<ParentList> result = new ArrayList<>();
    if (cursor.moveToFirst()){
        do {
            ParentList ParentList = new ParentList();
            ParentList.setTitle(cursor.getString(cursor.getColumnIndex("listItems")));
            result.add(ParentList);
            Log.d(TAG, "getList: "+result);

        }while (cursor.moveToNext());

    }
    return result;

}
*/

    @SuppressLint("ObsoleteSdkInt")
    public DBListHelper(Context context) {
        super(context, DB_NAME, null, DB_VER);
        this.myContext = context;

        if(android.os.Build.VERSION.SDK_INT >= 17){
            DATABASE_PATH = context.getApplicationInfo().dataDir + "/assests" + "/database/" ;
            Log.d(TAG, "DATABASE_PATH: "+DATABASE_PATH);
        }
        else
        {
            DATABASE_PATH = "/data/data/" + context.getPackageName() + "/database/";

            Log.d(TAG, "DBListHelperDATABASE_PATH: "+DATABASE_PATH);
        }


    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_MENU = "create table " + TABLE_NAME + "(" + UD_COLUMN_0 +  " INTEGER PRIMARY KEY," +   UD_COLUMN_01 + " TEXT NOT NULL)";
        Log.d(TAG, "CREATE_TABLE_MENU: "+ CREATE_TABLE_MENU);

        db.execSQL(CREATE_TABLE_MENU);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

    }

    //Insert Data to User Table
    public boolean insertToUser(String id,String list) throws SQLiteException
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(UD_COLUMN_0, id);
        contentValues.put(UD_COLUMN_01,list);

        long result = db.insert(TABLE_NAME, null, contentValues);
        db.close();
        return result != -1;

    }

/*
    public void createDatabase() {

        createDB();

    }

    private void createDB(){

        boolean dbExist = DBExists();

        if(!dbExist){
            Log.d(TAG, "createDB: "+dbExist);

            this.getReadableDatabase();

            copyDBFromResource();

        }

    }



    private boolean DBExists() {

        SQLiteDatabase db = null;

        try{
            String databasePath =  DB_NAME;
            Log.d(TAG, "DBExistsdatabasePath: "+databasePath);
            db = SQLiteDatabase.openDatabase(databasePath, null, SQLiteDatabase.OPEN_READWRITE);
            db.setLocale(Locale.getDefault());
            db.setLockingEnabled(true);
            db.setVersion(3);
            */
/*File file = new File(databasePath);
            Log.d(TAG, "DBExistsfile: "+!file.isDirectory());
            if (file.exists() && !file.isDirectory()) {
                Log.d(TAG, "file.isDirectory(): ");
                db = SQLiteDatabase.openDatabase(databasePath, null, SQLiteDatabase.OPEN_READONLY);
            }


            if (db != null) {
                db.close();
            }*//*


        } catch (SQLiteException e) {

            Log.d("DBExists", "database not found");
        }

        if (db != null){

            db.close();
        }

        return db != null ? true : false;

    }



    private void copyDBFromResource(){

        InputStream inputStream = null;
        OutputStream outStream = null;
        String dbFilePath =  DB_NAME;
        Log.d(TAG, "copyDBFromResourcedbFilePath: "+dbFilePath);
        try{

            inputStream = myContext.getAssets().open(DB_NAME);
            Log.d(TAG, "inputStream: "+inputStream);

            outStream = new FileOutputStream(dbFilePath);
            Log.d(TAG, "outStream: "+ outStream);

            byte [] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0){
                outStream.write(buffer, 0, length);

            }

            outStream.flush();
            outStream.close();
            inputStream.close();

        } catch (IOException e){

            e.toString();
            Log.d(TAG, "IOException: "+ e.toString());

//            throw new Error("Problem copying database from resource file.");
        }

    }



    @Override
    public synchronized void close () {

        if (dbSqlite !=null){

            dbSqlite.close();
        }

        super.close();

    }
*/
void addContact(ChildList list) {
    SQLiteDatabase db = this.getWritableDatabase();

    ContentValues values = new ContentValues();
    values.put(UD_COLUMN_0, list.getProductid()); // Contact Name
    values.put(UD_COLUMN_01, list.getListItemsTitle()); // Contact Phone

    // Inserting Row
    db.insert(TABLE_NAME, null, values);
    //2nd argument is String containing nullColumnHack
    db.close(); // Closing database connection
}
    public Cursor getContact(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, new String[] { UD_COLUMN_0,
                        UD_COLUMN_01 }, UD_COLUMN_0 + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        ArrayList<ParentList> result = new ArrayList<>();
        if (cursor.moveToFirst()){
            do {
                ParentList ParentList = new ParentList();
                ParentList.setTitle(cursor.getString(cursor.getColumnIndex("listItems")));
                result.add(ParentList);
                Log.d(TAG, "getList: "+result);

            }while (cursor.moveToNext());

        }
        return cursor;


    }

    // code to get all contacts in a list view
    public List<ChildList> getAllContacts() {
        List<ChildList> contactList = new ArrayList<ChildList>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ChildList childList = new ChildList();
                childList.setProductid(String.valueOf(Integer.parseInt(cursor.getString(0))));
                childList.setListItemsTitle(cursor.getString(1));
                // Adding contact to list
                contactList.add(childList);
            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }

    // code to update the single contact
    public int updateContact(ChildList contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(UD_COLUMN_0, contact.getProductid());
        values.put(UD_COLUMN_01, contact.getListItemsTitle());

        // updating row
        return db.update(TABLE_NAME, values, UD_COLUMN_0 + " = ?",
                new String[] { String.valueOf(contact.getProductid()) });
    }

    // Deleting single contact
    public void deleteContact(ChildList contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, UD_COLUMN_0 + " = ?",
                new String[] { String.valueOf(contact.getProductid()) });
        db.close();
    }

    // Getting contacts Count
    public int getContactsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

    public void insertToUser(SearchRecentSuggestions suggestions) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(UD_COLUMN_01, suggestions.toString()); // Product name
        Log.d(TAG, "insertToUser: "+ values);
//        values.put(UD_COLUMN_01, list.getListItemsTitle());
        // Inserting Row
        db.insert(TABLE_NAME, null, values);
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection

    }

/*

    public Cursor getCursor() {
        Log.d(TAG, "getListonclick: ");

//        String[] sqlselect = {"title"};
        SQLiteDatabase db = this.getReadableDatabase();

        String sql = "SELECT * FROM " + TABLE_NAME + " ORDER BY " + UD_COLUMN_01 + " ASC;";
        Log.d(TAG, "getCursorsql: "+sql);

        return db.rawQuery(sql, null);

      */
/*  queryBuilder.setTables(tableName);
        Cursor cursor = queryBuilder.query(db,null,null,null,null,null,null);
        ArrayList<ParentList> result = new ArrayList<>();
        if (cursor.moveToFirst()){
            do {
                ParentList ParentList = new ParentList();
                ParentList.setTitle(cursor.getString(cursor.getColumnIndex("listItems")));
                result.add(ParentList);
                Log.d(TAG, "getList: "+result);

            }while (cursor.moveToNext());

        }
        return cursor;*//*


    }
*/
/*
    public String getName (Cursor c){
        return(c.getString(1));


    }
*/

}
