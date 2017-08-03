package com.codepath.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final int EIDT_REQUEST_CODE = 101;
    ArrayList<String> items;
    ArrayAdapter<String> itemsAdapter;
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
        itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
        lvItems.setAdapter(itemsAdapter);
        setupListViewListener();
    }

    private void setupListViewListener() {
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View item, int pos, long id) {
                items.remove(pos);
                itemsAdapter.notifyDataSetChanged();
                writeItems();
                return true;
            }
        });

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View item, int pos, long id) {
                Intent intent = new Intent(MainActivity.this, EditItemActivity.class);
                intent.putExtra("item", items.get(pos)); // pass arbitrary data to launched activity
                intent.putExtra("pos", pos);
                startActivityForResult(intent, EIDT_REQUEST_CODE);
            }
        });

    }

    public void onAddItem(View V) {
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        String itemText =etNewItem.getText().toString();
        if (!itemText.trim().equals("")) {
            itemsAdapter.add(itemText.trim());
            etNewItem.setText("");
            writeItems();
        }
        else {
            Toast.makeText(this, "Nothing to add ...", Toast.LENGTH_SHORT).show();
        }
    }

    private void readItems() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try {
            items = new ArrayList<>(FileUtils.readLines(todoFile));
        } catch (IOException e) {
            items = new ArrayList<>();
        }
    }

    private void writeItems() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try {
            FileUtils.writeLines(todoFile, items);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // REQUEST_CODE is defined above
        if (resultCode == RESULT_OK && requestCode == EIDT_REQUEST_CODE) {
            String item = data.getExtras().getString("editedItem");
            int pos = data.getExtras().getInt("position");
            items.set(pos, item);
            itemsAdapter.notifyDataSetChanged();
            writeItems();
            Toast.makeText(this, "item updated ...", Toast.LENGTH_SHORT).show();
        }
        else if (requestCode == EIDT_REQUEST_CODE) {
            Toast.makeText(this, "changes discarded ...", Toast.LENGTH_SHORT).show();
        }
    }
}
