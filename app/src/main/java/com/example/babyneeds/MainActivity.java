package com.example.babyneeds;

import android.content.Intent;
import android.os.Bundle;

import com.example.babyneeds.data.DatabaseHandler;
import com.example.babyneeds.model.Item;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private Button saveButton;
    private EditText babyItem;
    private EditText itemQuantity;
    private EditText itemColour;
    private EditText itemSize;

    private DatabaseHandler db;

    //private ArrayList<Item> itemArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        db = new DatabaseHandler(MainActivity.this);
        byPassActivity();

//        db.addItem(new Item("Cloth","1","Red","small", "1 Jan 2020"));
//        List<Item> itemList= db.getAllItems();
//        for (Item item: itemList)
//            Log.d("Items added", "onCreate: "+item.getDateAdded());


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                createPopupDialog();
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });
    }

    private void byPassActivity() {

        if(db.getCount()>0){
            startActivity(new Intent(MainActivity.this,ListActivity.class));
            finish();
        }
    }

    private void saveItem(View view) {

        //TODO: save each baby item to db

        db.addItem(new Item(String.valueOf(babyItem.getText()),itemQuantity.getText().toString(),itemColour.getText().toString(),
                itemSize.getText().toString(),"date"));
        Snackbar.make(view, "Item added" , Snackbar.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                //code to be run
                dialog.dismiss();
                //TODO: move to next screen - details screen

                startActivity(new Intent(MainActivity.this , ListActivity.class));
            }
        },800);

    }

    private void createPopupDialog() {

        builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.popup,null);

        babyItem = view.findViewById(R.id.item_name_id);
        itemQuantity = view.findViewById(R.id.quantity_id);
        itemColour = view.findViewById(R.id.colour_id);
        itemSize = view.findViewById(R.id.size_id);
        saveButton = view.findViewById(R.id.save_button_id);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!babyItem.getText().toString().isEmpty()
                && !itemQuantity.getText().toString().isEmpty()
                && !itemColour.getText().toString().isEmpty()
                && !itemSize.getText().toString().isEmpty())
                    saveItem(view);
                else
                    Snackbar.make(view,"Empty fields not allowed",Snackbar.LENGTH_SHORT).show();
            }
        });


        builder.setView(view);
        dialog = builder.create();
        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
