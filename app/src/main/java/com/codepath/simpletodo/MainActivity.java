package com.codepath.simpletodo;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
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
        todoAdapter = new TodoCursorAdapter(this);
        lvItems.setAdapter(todoAdapter);

        setupListViewListener();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (todoAdapter.isIncludeCompleted()) {
            menu.findItem(R.id.menu_show_completed).setVisible(false);
            menu.findItem(R.id.menu_hide_completed).setVisible(true);
        }
        else {
            menu.findItem(R.id.menu_show_completed).setVisible(true);
            menu.findItem(R.id.menu_hide_completed).setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
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
                todoAdapter.getFilterQueryProvider().runQuery(newText);
                return true;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getTitle().equals(getString(R.string.show_completed))) {
            todoAdapter.setIncludeCompleted(true);
            return true;
        }
        else if (item.getTitle().equals(getString(R.string.hide_completed))) {
            todoAdapter.setIncludeCompleted(false);
            return true;
        }
        if (item.getItemId() == R.id.sort) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.
                    setCancelable(true).setTitle("Sort Items").
                    setMessage("in order by").
                    setNeutralButton("Title, due date, priority", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            todoAdapter.setSortOrder(TodoCursorAdapter.SortOrder.title);
                            todoAdapter.refresh();
                            dialogInterface.dismiss();

                        }
                    }).
                    setNegativeButton("Due date, priority, title", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            todoAdapter.setSortOrder(TodoCursorAdapter.SortOrder.dueDate);
                            todoAdapter.refresh();
                            dialogInterface.dismiss();
                        }
                    }).
                    setPositiveButton("Priority, due date, title", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            todoAdapter.setSortOrder(TodoCursorAdapter.SortOrder.proirity);
                            todoAdapter.refresh();
                            dialogInterface.dismiss();
                        }
                    });
            final AlertDialog alertDialog = builder.create();
            alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialogInterface) {
                    Button title = alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL);
                    Button duedate = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                    Button priority = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);

                    switch (todoAdapter.getSortOrder()) {
                        case title:
                            title.setTypeface(null, Typeface.BOLD_ITALIC);
                            break;
                        case proirity:
                            priority.setTypeface(null, Typeface.BOLD_ITALIC);
                            break;
                        default:
                            duedate.setTypeface(null, Typeface.BOLD_ITALIC);
                    }
                }
            });
            alertDialog.show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupListViewListener() {
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int pos, long id) {
                final Item item = todoAdapter.getItemByPosition(pos);
                if (item.completed) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setCancelable(true).setTitle("Delete Item?").setMessage("or just mark it incomplete").
                            setNeutralButton("Delete", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    item.delete();
                                    todoAdapter.refresh();
                                    dialogInterface.dismiss();
                                }
                            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    }).setPositiveButton("Incomoplete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            item.completed = false;
                            item.save();
                            todoAdapter.refresh();
                            dialogInterface.dismiss();
                        }
                    }).show();
                }
                else {
                    item.completed = true;
                    item.save();
                    todoAdapter.refresh();
                }
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
