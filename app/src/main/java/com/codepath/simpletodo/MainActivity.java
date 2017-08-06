package com.codepath.simpletodo;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int EIDT_REQUEST_CODE = 102;
    private static final int ADD_REQUEST_CODE = 101;

    public static final String ITEM_POSITION = "position";

    List<Item> items;
    ItemsAdapter itemsAdapter;
    ListView lvItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar sab = getSupportActionBar();
        if (sab != null) {
            sab.setDisplayShowHomeEnabled(true);
            sab.setLogo(R.mipmap.ic_launcher);
            sab.setDisplayUseLogoEnabled(true);
        }

        setContentView(R.layout.activity_main);
        lvItems = (ListView) findViewById(R.id.lvItem);
        items = new ArrayList<>();
        readItems();
        itemsAdapter = new ItemsAdapter(this, R.layout.item, items);
        lvItems.setAdapter(itemsAdapter);
        setupListViewListener();
    }

    private void setupListViewListener() {
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int pos, long id) {
                Item item = items.get(pos);
                item.delete();
                items.remove(pos);
                itemsAdapter.notifyDataSetChanged();
                return true;
            }
        });

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View item, int pos, long id) {
                Item i = items.get(pos);
                DialogFragment dialog = AddEditDialogFragment.newInstance(i);
                dialog.show(getSupportFragmentManager(), String.valueOf(pos));
            }
        });

    }

    public void onAddItem(View V) {
        DialogFragment dialog = AddEditDialogFragment.newInstance(new Item());
        dialog.show(getSupportFragmentManager(), "new");
    }

    private void readItems() {
        items = Item.readAllItems();

    }
}
