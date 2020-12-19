package com.example.babyneeds.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;

import com.example.babyneeds.ListActivity;
import com.example.babyneeds.MainActivity;
import com.example.babyneeds.R;
import com.example.babyneeds.data.DatabaseHandler;
import com.example.babyneeds.model.Item;
import com.google.android.material.snackbar.Snackbar;

import java.text.MessageFormat;
import java.util.List;

public class RecyclerViewAdapter extends Adapter<RecyclerViewAdapter.ViewHolder> {

    private Context context;
    private List<Item> itemList;
    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private LayoutInflater inflater;

    public RecyclerViewAdapter(Context context, List<Item> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row,parent,false);

        return new ViewHolder(view,context);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {

        Item item = itemList.get(position);
        holder.itemName.setText(item.getItemName());
        holder.quantity.setText(MessageFormat.format("Quantity:{0}", item.getQuantity()));
        holder.colour.setText(MessageFormat.format("Colour:{0}", item.getColour()));
        holder.size.setText(MessageFormat.format("Size:{0}", item.getSize()));
        holder.dateAdded.setText(MessageFormat.format("Added On:{0}", item.getDateAdded()));

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView itemName;
        public TextView quantity;
        public TextView colour;
        public TextView size;
        public TextView dateAdded;
        public Button editButton;
        public Button deleteButton;
        private int id;


        public ViewHolder(@NonNull View itemView,Context ctx) {
            super(itemView);

            context=ctx;

            itemName= itemView.findViewById(R.id.itemname_id);
            quantity= itemView.findViewById(R.id.itemquantity_id);
            colour =  itemView.findViewById(R.id.itemcolour_id);
            size= itemView.findViewById(R.id.itemsize_id);
            dateAdded=itemView.findViewById(R.id.itemdate_id);
            editButton = itemView.findViewById(R.id.editButton_id);
            deleteButton = itemView.findViewById(R.id.deleteButton_id);

            editButton.setOnClickListener(this);
            deleteButton.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {

            int position=getAdapterPosition();
            Item item = itemList.get(position);
            switch (view.getId()){

                case R.id.editButton_id:
                    //edit data
                    updateItem(item);
                    break;

                case R.id.deleteButton_id:
                    //delete item
                    deleteItem(item.getId());
                    break;
            }

        }

        private void updateItem(Item item) {

            builder = new AlertDialog.Builder(context);
            inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.popup,null);

            builder.setView(view);
            dialog = builder.create();
            dialog.show();

            final TextView itemName = view.findViewById(R.id.item_name_id);
            final TextView itemQuantity = view.findViewById(R.id.quantity_id);
            final TextView itemColour = view.findViewById(R.id.colour_id);
            final TextView itemSize = view.findViewById(R.id.size_id);
            Button saveButton = view.findViewById(R.id.save_button_id);
            TextView title = view.findViewById(R.id.title_id);

            saveButton.setText(R.string.update_text);
            title.setText(R.string.update_item_text);
            itemName.setText(item.getItemName());
            itemQuantity.setText(item.getQuantity());
            itemColour.setText(item.getColour());
            itemSize.setText(item.getSize());

            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {

                    if(!itemName.getText().toString().isEmpty()
                            && !itemQuantity.getText().toString().isEmpty()
                            && !itemColour.getText().toString().isEmpty()
                            && !itemSize.getText().toString().isEmpty()) {
                        Item item = new Item(id,String.valueOf(itemName.getText()),itemQuantity.getText().toString(),itemColour.getText().toString(),
                                itemSize.getText().toString(),"date");

                        DatabaseHandler db = new DatabaseHandler(context);
                        db.updateItem(item);
                        Snackbar.make(view, "Item updated" , Snackbar.LENGTH_SHORT).show();
                        itemList.set(getAdapterPosition(), item);
                        notifyDataSetChanged();
                        //notifyItemChanged(getAdapterPosition(),item);

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                //code to be run
                                dialog.dismiss();
                                //TODO: move to next screen - details screen

                                context.startActivity(new Intent(view.getContext() , ListActivity.class));
                            }
                        },800);
                    }
                    else
                        Snackbar.make(view,"Empty fields not allowed",Snackbar.LENGTH_SHORT).show();
                }
            });
        }

        private void deleteItem(final int id) {

            builder = new AlertDialog.Builder(context);
            inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.confirmation_popup,null);

            builder.setView(view);
            dialog = builder.create();
            dialog.show();

            TextView yesView = view.findViewById(R.id.conf_yes_id);
            TextView noView = view.findViewById(R.id.conf_no_id);

            yesView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    DatabaseHandler db = new DatabaseHandler(context);
                    db.deleteItem(id);
                    itemList.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());

                    dialog.dismiss();

                    if(itemList.isEmpty()){
                        Intent intent = new Intent(view.getContext(), MainActivity.class);
                        view.getContext().startActivity(intent);

                    }
                }
            });
            noView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
        }
    }
}
