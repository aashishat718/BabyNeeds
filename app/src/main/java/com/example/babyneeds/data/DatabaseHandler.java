package com.example.babyneeds.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.babyneeds.R;
import com.example.babyneeds.model.Item;
import com.example.babyneeds.util.Util;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {


    private final Context context;

    public DatabaseHandler(@Nullable Context context) {
        super(context, Util.DATABASE_NAME, null, Util.DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String CREATE_ITEM_TABLE = "CREATE TABLE "+ Util.TABLE_NAME + "(" +
                Util.KEY_ID + " INTEGER PRIMARY KEY," + Util.KEY_ITEM_NAME + " TEXT," +
                Util.KEY_QUANTITY + " TEXT," + Util.KEY_COLOUR + " TEXT," + Util.KEY_SIZE
                + " TEXT," + Util.KEY_DATE_ADDED +" TEXT" + ")";
        sqLiteDatabase.execSQL(CREATE_ITEM_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        String DROP_TABLE = String.valueOf((R.string.drop_table));
        sqLiteDatabase.execSQL(DROP_TABLE, new String[]{Util.DATABASE_NAME});

        //create a table again
        onCreate(sqLiteDatabase);
    }

    /*
    CRUD
     */

    //add item
    public void addItem(Item item)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values= new ContentValues();
        values.put(Util.KEY_ITEM_NAME,item.getItemName());
        values.put(Util.KEY_QUANTITY,item.getQuantity());
        values.put(Util.KEY_COLOUR,item.getColour());
        values.put(Util.KEY_SIZE,item.getSize());
        values.put(Util.KEY_DATE_ADDED, java.lang.System.currentTimeMillis());//timestamp of system

        db.insert(Util.TABLE_NAME,null,values);
        db.close();
    }

    //get item
    public Item getItem(int id)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Util.TABLE_NAME,new String[]{Util.KEY_ID,Util.KEY_ITEM_NAME,Util.KEY_QUANTITY,
        Util.KEY_COLOUR,Util.KEY_SIZE,Util.KEY_DATE_ADDED},Util.KEY_ID + "=?" , new String[]{String.valueOf(id)},
                null,null,null);

        if(cursor!=null)
            cursor.moveToFirst();

        Item item=new Item();
        item.setId(Integer.parseInt(cursor.getString(0)));
        item.setItemName(cursor.getString(1));
        item.setQuantity(cursor.getString(2));
        item.setColour(cursor.getString(3));
        item.setSize(cursor.getString(4));
        //convert timestamp to readable
        DateFormat dateFormat= DateFormat.getDateInstance();
        String formattedDate = dateFormat.format(new Date(cursor.getLong(5)).getTime());
        item.setDateAdded(formattedDate);

        return item;
    }

    //get all items
    public List<Item> getAllItems(){
        List<Item> itemList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        //Select all items
        Cursor cursor = db.query(Util.TABLE_NAME,new String[]{Util.KEY_ID,Util.KEY_ITEM_NAME,Util.KEY_QUANTITY,
                        Util.KEY_COLOUR,Util.KEY_SIZE,Util.KEY_DATE_ADDED},null , null,
                null,null,Util.KEY_DATE_ADDED + " DESC");

//        String selectAll = "SELECT * FROM " + Util.TABLE_NAME;
//        Cursor cursor= db.rawQuery(selectAll,null);

        //Loop through our data
        if (cursor.moveToFirst()) {
            do {
                Item item = new Item("Cloth", "1","Red","small", "1 Jan 2020");
                item.setId(Integer.parseInt(cursor.getString(0)));
                item.setItemName(cursor.getString(1));
                item.setQuantity(cursor.getString(2));
                item.setColour(cursor.getString(3));
                item.setSize(cursor.getString(4));
                //convert timestamp to readable
                DateFormat dateFormat= DateFormat.getDateInstance();
                String formattedDate = dateFormat.format(new Date(cursor.getLong(5)).getTime());
                item.setDateAdded(formattedDate);

                //add contact objects to our list
                itemList.add(item);
            }while (cursor.moveToNext());
        }

        return itemList;
    }

    //update item
    public int updateItem(Item item)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values=new ContentValues();
        values.put(Util.KEY_ITEM_NAME,item.getItemName());
        values.put(Util.KEY_QUANTITY,item.getQuantity());
        values.put(Util.KEY_COLOUR,item.getColour());
        values.put(Util.KEY_SIZE,item.getSize());
        values.put(Util.KEY_DATE_ADDED,java.lang.System.currentTimeMillis());

        //update the row
        return db.update(Util.TABLE_NAME,values,Util.KEY_ID + "=?",
                new String[]{String.valueOf(item.getId())});
    }

    //delete item
    public void deleteItem(int id)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(Util.TABLE_NAME,Util.KEY_ID +"=?",
                new String[]{String.valueOf(id)});
        db.close();
    }

    //get Count
    public int getCount()
    {
        String countQuery = "SELECT * FROM " + Util.TABLE_NAME;
        SQLiteDatabase db= this.getReadableDatabase();
        Cursor cursor= db.rawQuery(countQuery,null);

        return cursor.getCount();
    }
}
