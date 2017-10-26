package com.nitsilchar.hp.passwordStorage;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shaloin on 1/6/17.
 */

public final class PasswordDatabase extends SQLiteOpenHelper {

    String data1;

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "UserCredentials.db";
    public static final String TABLE_NAME = "Credentials";
    public static final String COLUMN_PASSWORD = "Password";
    public static final String COLUMN_ACCOUNT = "Account";
    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME;
    private static final String TABLE_CREATE = "CREATE TABLE "
            + TABLE_NAME + " (" +  COLUMN_ACCOUNT  + " TEXT, " + COLUMN_PASSWORD
            + " TEXT,UNIQUE("+ COLUMN_ACCOUNT + "));";

    public PasswordDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion){
        onUpgrade(db, oldVersion, newVersion);
    }
    public void addCredentials(Context context,String account, String password){
        SQLiteDatabase db = this.getWritableDatabase();
        long newRowId=0;
        Boolean flag=false;
        ContentValues values = new ContentValues();
        values.put(COLUMN_ACCOUNT, account);
        values.put(COLUMN_PASSWORD, password);
            newRowId = db.insert(TABLE_NAME, null, values);
    }

    public void deleteRow(String account){
        SQLiteDatabase db=this.getWritableDatabase();
        String whereClause=COLUMN_ACCOUNT+"=?";
        String[] whereArgs=new String[] {account};
        db.delete(TABLE_NAME,whereClause,whereArgs);
    }

    public void modify(String account,String pass){
        SQLiteDatabase db=this.getWritableDatabase();
        String sql="UPDATE "+ TABLE_NAME +" SET " +COLUMN_PASSWORD +" = " +pass + " WHERE "+ COLUMN_ACCOUNT +" = " +  account;
        db.execSQL(sql);

    }
    public int modifyCredentials(String account,String newPass){

        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(COLUMN_PASSWORD,newPass);
        String whereClause=COLUMN_ACCOUNT + " =?";
        String[] whereArgs=new String[]{account};
        int update=db.update(TABLE_NAME,contentValues,whereClause,whereArgs);
        return update;
    }

    public void deleteAllCredentials(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
    }

    public boolean checkdb(){
        SQLiteDatabase db;
        db=this.getWritableDatabase();
        Cursor cursor=db.rawQuery("SELECT * FROM"+TABLE_NAME,null);
        Boolean rowExists;
        if (cursor.moveToFirst()){
            rowExists=false;

        }
        else {
            rowExists=true;
        }
        return rowExists;
    }
    public List<String> getAcc(){
        SQLiteDatabase db=this.getReadableDatabase();;
        List<String> collection=new ArrayList<>();
        String acc=null;
        Cursor c=null;
        try{
            String query="SELECT " + COLUMN_ACCOUNT + " FROM " + TABLE_NAME;
            c=db.rawQuery(query,null);
            if(c!=null){
                if(c.moveToFirst()){
                    do{
                        acc=c.getString(c.getColumnIndex(COLUMN_ACCOUNT));
                        collection.add(acc);
                    }
                    while (c.moveToNext());
                }
            }
        }
        finally {
            if(c!=null){
                c.close();
            }
            if(db!=null){
                db.close();
            }
        }
        return collection;
    }

    public String getData(String data){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[] { COLUMN_ACCOUNT,COLUMN_PASSWORD
                }, COLUMN_ACCOUNT + " = ?", new String[] { data },
                null, null, null, null);
        if (cursor!=null && cursor.moveToFirst()){
            do{
                data1=cursor.getString(1);
            }while (cursor.moveToNext());
        }
        return data1;
    }


}
