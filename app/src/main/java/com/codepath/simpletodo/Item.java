package com.codepath.simpletodo;

import android.database.Cursor;
import android.os.Bundle;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.sql.language.Where;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by kaamel on 8/3/17.
 */

@Table(database = TodoItemDatabase.class)
public class Item extends BaseModel {

    public static final String ITEM_TITLE = "title";
    public static final String ITEM_DUE_DATE = "dueDate";
    public static final String ITEM_PRIORITY = "priority";
    public static final String ITEM_ACTION_COMPLETED = "completed";
    public static final String ITEM_ID = "_id";

    @Column
    @PrimaryKey(autoincrement = true)
    int _id;

    @Column
    public String title;

    @Column
    public int priority;

    @Column
    public boolean completed;

    @Column
    public long dueDate;

    public static Item findItemById(int id) {
        return SQLite.select().
                from(Item.class).
                where(Item_Table._id.is(id)).
                querySingle();
    }

    public static Bundle getBundle(Item item) {
        Bundle bundle = new Bundle();
        if (item != null) {
            bundle.putString(ITEM_TITLE, item.title);
            bundle.putLong(ITEM_DUE_DATE, item.dueDate);
            bundle.putInt(ITEM_PRIORITY, item.priority);
            bundle.putInt(ITEM_ID, item._id);
            bundle.putBoolean(ITEM_ACTION_COMPLETED, item.completed);
        }
        return bundle;
    }

    public static Item getItem(Bundle bundle) {
        Item item = new Item();
        if (bundle != null) {
            item.dueDate = bundle.getLong(ITEM_DUE_DATE);
            item.title = bundle.getString(ITEM_TITLE);
            item.priority = bundle.getInt(ITEM_PRIORITY);
            item.completed = bundle.getBoolean(ITEM_ACTION_COMPLETED);
            item._id = bundle.getInt(ITEM_ID);
        }
        return item;
    }

    public static Cursor getCursor(String filter, boolean includeCompleted, TodoCursorAdapter.SortOrder sortOrder) {
        Where<Item> w;
        if (includeCompleted) {
            w = SQLite.select().from(Item.class).
                    where(Item_Table.title.like("%" + filter + "%"));
        }
        else {
            w = SQLite.select().from(Item.class).
                    where(Item_Table.title.like("%" + filter + "%")).and(Item_Table.completed.is(false));
        }

        switch (sortOrder) {
            case proirity:
                w = w.orderBy(Item_Table.priority, false).orderBy(Item_Table.dueDate, true).orderBy(Item_Table.title, true);
                break;

            case title:
                w = w.orderBy(Item_Table.title, true).orderBy(Item_Table.dueDate, true).orderBy(Item_Table.priority, false);
                break;
            default:
                w = w.orderBy(Item_Table.dueDate, true).orderBy(Item_Table.priority, false).orderBy(Item_Table.title, true);
                break;
        }

        return w.query();
    }
}
