package com.codepath.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int EIDT_REQUEST_CODE = 102;
    private static final int ADD_REQUEST_CODE = 101;

    public static final String ITEM_TITLE = "title";
    public static final String ITEM_DUE_DATE = "due_date";
    public static final String ITEM_PRIORITY = "priority";
    public static final String ITEM_ID = "id";
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
                Intent intent = new Intent(MainActivity.this, EditItemActivity.class);
                intent.putExtra(ITEM_TITLE, items.get(pos).title);
                intent.putExtra(ITEM_DUE_DATE, items.get(pos).dueDate);
                intent.putExtra(ITEM_PRIORITY, items.get(pos).priority);
                intent.putExtra(ITEM_ID, items.get(pos).id);
                intent.putExtra("position", pos);
                startActivityForResult(intent, EIDT_REQUEST_CODE);
            }
        });

    }

    public void onAddItem(View V) {
        Intent intent = new Intent(MainActivity.this, EditItemActivity.class);
        startActivityForResult(intent, ADD_REQUEST_CODE);
    }

    private void readItems() {
        items = Item.readAllItems();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // REQUEST_CODE is defined above
        if (resultCode == RESULT_OK && requestCode == EIDT_REQUEST_CODE) {
            Item item = new Item();
            item.id = data.getExtras().getInt(ITEM_ID);
            if (addUpdateItem(item, data)) {

                int pos = data.getExtras().getInt(ITEM_POSITION);
                items.set(pos, item);
                itemsAdapter.notifyDataSetChanged();
                Toast.makeText(this, "item updated ...", Toast.LENGTH_SHORT).show();
            }
        }
        else if (resultCode == RESULT_OK && requestCode == ADD_REQUEST_CODE) {
            Item item = new Item();
            if (addUpdateItem(item, data)) {
                items.add(item);
                itemsAdapter.notifyDataSetChanged();
                Toast.makeText(this, "new item added ...", Toast.LENGTH_SHORT).show();
            }
        }
        else if (requestCode == EIDT_REQUEST_CODE || requestCode == ADD_REQUEST_CODE) {
            Toast.makeText(this, "cancelled ...", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean addUpdateItem(Item item, Intent data) {
        Bundle bundle = data.getExtras();
        String title = bundle.getString(ITEM_TITLE);
        if (title == null || title.trim().equals("")) {
            Toast.makeText(this, "item needs a title ...", Toast.LENGTH_SHORT).show();
            return false;
        }
        item.title = title;
        item.priority = bundle.getInt(ITEM_PRIORITY, 1);
        item.dueDate = bundle.getString(ITEM_DUE_DATE);
        item.save();
        return true;
    }
}
