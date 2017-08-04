package com.codepath.simpletodo;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.List;

/**
 * Created by kaamel on 8/3/17.
 */

@Table(database = TodoItemDatabase.class)
public class Item extends BaseModel {

    @Column
    @PrimaryKey(autoincrement = true)
    int id;

    @Column
    public String title;

    @Column
    public int priority;

    @Column
    public boolean completed;

    @Column
    public String dueDate;

    public static List<Item> readAllItems() {
        return SQLite.select().
                from(Item.class).queryList();
    }

    public static Item findItemById(int id) {
        return SQLite.select().
                from(Item.class).
                where(Item_Table.id.is(id)).
                querySingle();
    }

    public static List<Item> findCompletedItems(boolean reverse) {
        return SQLite.select().
                from(Item.class).
                where(Item_Table.completed.is(reverse)).
                queryList();
    }
}
