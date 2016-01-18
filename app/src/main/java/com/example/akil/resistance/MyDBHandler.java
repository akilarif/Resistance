package com.example.akil.resistance;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class MyDBHandler extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION=1;
    private static final String DATABASE_NAME="Warriors.db";
    private static final String TABLE_WARRIORS="Warriors";
    private static final String COLUMN_ID="_id";
    private static final String COLUMN_NAME="name";
    private static final String COLUMN_CONTACTNO="contactno";
    private static final String COLUMN_IMGPATH="imgpath";
    private static final String COLUMN_AFFILIATION="affiliation";
    private static final String COLUMN_SPECIES="species";
    private static final String COLUMN_GENDER="gender";
    private static final String COLUMN_DATE="date";
    private static final String COLUMN_PLANET="planet";

    public MyDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String query = "CREATE TABLE " + TABLE_WARRIORS + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY, " +
                COLUMN_NAME + " TEXT , " +
                COLUMN_CONTACTNO + " TEXT , " +
                COLUMN_IMGPATH + " TEXT , " +
                COLUMN_AFFILIATION + " TEXT , " +
                COLUMN_SPECIES + " TEXT , " +
                COLUMN_GENDER + " TEXT , " +
                COLUMN_DATE + " TEXT , " +
                COLUMN_PLANET + " TEXT " +
                ")";

        db.execSQL(query);

        db.execSQL("CREATE INDEX NAMENUM " + COLUMN_NAME + " , " + COLUMN_CONTACTNO + " ON " + TABLE_WARRIORS);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WARRIORS);
        onCreate(db);

    }

    //Add a new warrior (row) to the database
    public int addWarrior(Warriors warrior){

        ContentValues values=new ContentValues();

        values.put(COLUMN_NAME, warrior.get_name());
        values.put(COLUMN_CONTACTNO, warrior.get_contactno());
        values.put(COLUMN_IMGPATH, warrior.get_imgpath());
        values.put(COLUMN_AFFILIATION, warrior.get_affiliation());
        values.put(COLUMN_SPECIES, warrior.get_species());
        values.put(COLUMN_GENDER, warrior.get_gender());
        values.put(COLUMN_DATE, warrior.get_date());
        values.put(COLUMN_PLANET, warrior.get_planet());

        SQLiteDatabase db=getWritableDatabase();
        db.insert(TABLE_WARRIORS, null, values);
        Cursor c=db.rawQuery("SELECT * FROM " + TABLE_WARRIORS, null);
        c.moveToLast();
        int w_ID=c.getInt(c.getColumnIndex(COLUMN_ID));
        warrior.set_id(w_ID);
        c.close();
        db.close();
        return w_ID;

    }

    //Delete a warrior (row) from the database
    public void deleteWarrior(int warrior_id){

        SQLiteDatabase db=getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_WARRIORS + " WHERE " + COLUMN_ID + " = \"" + warrior_id + "\"");

    }

    //Print the warrior details as a string - For sending the details of a warrior through Whatsapp
    public String warriorDetailsToString(int warrior_id){

        String warriorDetailsString="";
        SQLiteDatabase db=getWritableDatabase();
        String query="SELECT * FROM " + TABLE_WARRIORS + " WHERE " + COLUMN_ID + " = \"" + warrior_id + "\"";

        Cursor cursor=db.rawQuery(query,null);
        cursor.moveToFirst();
        String[] columnNameArray={COLUMN_NAME,COLUMN_CONTACTNO,COLUMN_AFFILIATION,COLUMN_SPECIES,COLUMN_GENDER,
                COLUMN_DATE,COLUMN_PLANET};
        String[] columnDisplayArray={"NAME : ", "CONTACT NUMBER : ", "AFFILIATION : ", "SPECIES : ", "GENDER : ",
                "LAST SPOTTED ON : ", "LAST KNOWN PRESENCE : "};
        for(int i=0;i<columnNameArray.length;i++) {
            warriorDetailsString += columnDisplayArray[i] + cursor.getString(cursor.getColumnIndex(columnNameArray[i]));
            warriorDetailsString +="\n";
        }
        cursor.close();
        db.close();
        return warriorDetailsString;
    }

    //Getting the names of all the warriors in the database
    public Warriors[] warriorsArrayGetter(){

        SQLiteDatabase db=getWritableDatabase();
        String query="SELECT * FROM " + TABLE_WARRIORS;

        Cursor cursor=db.rawQuery(query,null);
        cursor.moveToFirst();

        int count=cursor.getCount();
        Warriors[] warriors=new Warriors[count];
        int f=0;

        while (!cursor.isAfterLast()){
            warriors[f]=new Warriors(cursor.getString(cursor.getColumnIndex(COLUMN_NAME)),
                                    cursor.getString(cursor.getColumnIndex(COLUMN_CONTACTNO)),
                                    cursor.getString(cursor.getColumnIndex(COLUMN_IMGPATH)),
                                    cursor.getString(cursor.getColumnIndex(COLUMN_AFFILIATION)),
                                    cursor.getString(cursor.getColumnIndex(COLUMN_SPECIES)),
                                    cursor.getString(cursor.getColumnIndex(COLUMN_GENDER)),
                                    cursor.getString(cursor.getColumnIndex(COLUMN_DATE)),
                                    cursor.getString(cursor.getColumnIndex(COLUMN_PLANET)));
            warriors[f].set_id(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
            f++;
            cursor.moveToNext();
        }
        cursor.close();
        db.close();

        return warriors;

    }

    public Warriors warriorDetailsAsWarriorObject(int wID){

        SQLiteDatabase db=getWritableDatabase();
        String query="SELECT * FROM " + TABLE_WARRIORS + " WHERE " + COLUMN_ID + " = \"" + wID + "\"";

        Cursor cursor=db.rawQuery(query,null);
        cursor.moveToFirst();
        Warriors warrior=new Warriors(cursor.getString(cursor.getColumnIndex(COLUMN_NAME)),
                cursor.getString(cursor.getColumnIndex(COLUMN_CONTACTNO)),
                cursor.getString(cursor.getColumnIndex(COLUMN_IMGPATH)),
                cursor.getString(cursor.getColumnIndex(COLUMN_AFFILIATION)),
                cursor.getString(cursor.getColumnIndex(COLUMN_SPECIES)),
                cursor.getString(cursor.getColumnIndex(COLUMN_GENDER)),
                cursor.getString(cursor.getColumnIndex(COLUMN_DATE)),
                cursor.getString(cursor.getColumnIndex(COLUMN_PLANET)));
        warrior.set_id(wID);
        cursor.close();

        return warrior;

    }

    public boolean iswarriorexist(String name,String contactnum){

        contactnum=contactnum.trim();
        boolean warriorexist;
        SQLiteDatabase db=getWritableDatabase();
        String query="SELECT " + COLUMN_NAME + "," + COLUMN_CONTACTNO + " FROM " + TABLE_WARRIORS + " WHERE " +
                COLUMN_NAME + " = \"" + name + "\" COLLATE NOCASE AND " + COLUMN_CONTACTNO + " = \"" + contactnum + "\"";
        Cursor cursor=db.rawQuery(query,null);
        cursor.moveToFirst();
        if(cursor.getCount()<=0)
            warriorexist= false;
        else
            warriorexist= true;

        cursor.close();
        return warriorexist;

    }

}
