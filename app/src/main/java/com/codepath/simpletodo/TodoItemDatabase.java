package com.codepath.simpletodo;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * Created by kaamel on 8/3/17.
 */

@Database(name = TodoItemDatabase.NAME, version = TodoItemDatabase.VERSION)

public class TodoItemDatabase {


    public static final String NAME = "ItemsDatabase";

    public static final int VERSION = 1;

}
