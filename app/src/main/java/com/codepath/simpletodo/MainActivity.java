package com.codepath.simpletodo;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {
    TodoCursorAdapter todoAdapter;
    ListView lvItems;
    SearchView searchView;

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
        todoAdapter = new TodoCursorAdapter(this, Item.getCursor());
        lvItems.setAdapter(todoAdapter);

        setupListViewListener();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        // Inflate menu to add items to action bar if it is present.
        inflater.inflate(R.menu.main_menu, menu);
        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                todoAdapter.filter(newText);
                return true;
            }
        });

        return true;
    }

    private void setupListViewListener() {
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int pos, long id) {
                Item item = todoAdapter.getItemByPosition(pos);
                item.delete();
                todoAdapter.changeCursor(Item.getCursor());
                return true;
            }
        });

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                Item item = todoAdapter.getItemByPosition(pos);
                DialogFragment dialog = AddEditDialogFragment.newInstance(item);
                dialog.show(getSupportFragmentManager(), String.valueOf(pos));
            }
        });

    }

    public void onAddItem(View V) {
        DialogFragment dialog = AddEditDialogFragment.newInstance(new Item());
        dialog.show(getSupportFragmentManager(), "new");
    }
}
