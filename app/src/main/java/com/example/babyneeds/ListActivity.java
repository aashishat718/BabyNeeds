package com.example.babyneeds;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.babyneeds.data.DatabaseHandler;
import com.example.babyneeds.model.Item;
import com.example.babyneeds.ui.RecyclerViewAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    private static final String TAG = "ListActivity";
    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private List<Item> itemList;
    private DatabaseHandler db;

    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private FloatingActionButton fab;
    private Button saveButton;
    private EditText babyItem;
    private EditText itemQuantity;
    private EditText itemColour;
    private EditText itemSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        fab= findViewById(R.id.fab1);
        recyclerView = findViewById(R.id.recycler_view_id);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        db = new DatabaseHandler(this);

        itemList = new ArrayList<>();
        itemList=db.getAllItems();

//        for(Item item:itemList){
//            Log.d(TAG, "onCreate: "+item.getItemName());
//        }

        recyclerViewAdapter = new RecyclerViewAdapter(this,itemList);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.notifyDataSetChanged();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createPopupDialog();
            }
        });

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

    private void saveItem(View view) {

        db.addItem(new Item(String.valueOf(babyItem.getText()),itemQuantity.getText().toString(),itemColour.getText().toString(),
                itemSize.getText().toString(),"date"));
        Snackbar.make(view, "Item added" , Snackbar.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                //code to be run
                dialog.dismiss();
                //TODO: move to next screen - details screen

                startActivity(new Intent(ListActivity.this , ListActivity.class));
                finish();
            }
        },800);
    }
}
