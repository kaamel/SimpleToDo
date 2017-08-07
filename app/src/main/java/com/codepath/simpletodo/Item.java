package com.codepath.simpletodo;

import android.os.Bundle;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.raizlabs.android.dbflow.structure.database.FlowCursor;

import java.util.List;

/**
 * Created by kaamel on 8/3/17.
 */

@Table(database = TodoItemDatabase.class)
public class Item extends BaseModel {

    public static final String ITEM_TITLE = "title";
    public static final String ITEM_DUE_DATE = "dueDate";
    public static final String ITEM_PRIORITY = "priority";
    public static final String ITEM_STATUS = "status";
    public static final String ITEM_ID = "_id";

    @Column
    @PrimaryKey(autoincrement = true)
    int _id;

    @Column
    public String title;

    @Column
    public int priority;

    @Column
    public boolean status;

    @Column
    public String dueDate;

    public static List<Item> readAllItems() {
        return SQLite.select().
                from(Item.class).queryList();
    }

    public static Item findItemById(int id) {
        return SQLite.select().
                from(Item.class).
                where(Item_Table._id.is(id)).
                querySingle();
    }

    public static List<Item> findCompletedItems(boolean completed) {
        return SQLite.select().
                from(Item.class).
                where(Item_Table.status.is(completed)).
                queryList();
    }

    public static Bundle getBundle(Item item) {
        Bundle bundle = new Bundle();
        if (item != null) {
            bundle.putString(ITEM_TITLE, item.title);
            bundle.putString(ITEM_DUE_DATE, item.dueDate);
            bundle.putInt(ITEM_PRIORITY, item.priority);
            bundle.putInt(ITEM_ID, item._id);
            bundle.putBoolean(ITEM_STATUS, item.status);
        }
        return bundle;
    }

    public static Item getItem(Bundle bundle) {
        Item item = new Item();
        if (bundle != null) {
            item.dueDate = bundle.getString(ITEM_DUE_DATE);
            item.title = bundle.getString(ITEM_TITLE);
            item.priority = bundle.getInt(ITEM_PRIORITY);
            item.status = bundle.getBoolean(ITEM_STATUS);
            item._id = bundle.getInt(ITEM_ID);
        }
        return item;
    }

    public static FlowCursor getCursor() {
        return SQLite.select().from(Item.class).query();
    }

    public static FlowCursor getCursor(String filter) {
        return SQLite.select().
                from(Item.class).
                where(Item_Table.title.like("%" + filter + "%")).query();
    }
}
