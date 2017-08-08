package com.codepath.simpletodo;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.FilterQueryProvider;
import android.widget.TextView;

import com.raizlabs.android.dbflow.structure.database.FlowCursor;

/**
 * Created by kaamel on 8/7/17.
 */

public class TodoCursorAdapter extends CursorAdapter {

    static String titleFilter = "";

    public TodoCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.item, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Lookup view for data population
        TextView title = view.findViewById(R.id.item_title);
        TextView dueDate = view.findViewById(R.id.item_due_date);
        TextView priority = view.findViewById(R.id.item_priority);

        title.setText(cursor.getString(cursor.getColumnIndexOrThrow(Item.ITEM_TITLE)));
        switch (cursor.getInt(cursor.getColumnIndexOrThrow(Item.ITEM_PRIORITY))) {
            case 0:
                priority.setText(R.string.low_priority);
                break;
            case 2:
                priority.setText(R.string.high_priority);
                break;
            default:
                priority.setText(R.string.medium_priority);
                break;

        }
        long date = cursor.getLong(cursor.getColumnIndexOrThrow(Item.ITEM_DUE_DATE));

        dueDate.setText("Due " + Utils.longToDateString(date));
        if (date == Utils.getTodayLong()) {
            dueDate.setTextColor(Color.BLUE);
        }
        else if (date < Utils.getTodayLong()) {
            dueDate.setTextColor(Color.RED);
        }
        else {
            dueDate.setTextColor(Color.BLACK);
        }
    }

    public int getItemIdByPosition(int pos) {
        return ((FlowCursor) getItem(pos)).getInt(getCursor().getColumnIndexOrThrow(Item.ITEM_ID));
    }

    public Item getItemByPosition(int pos) {
        return Item.findItemById(getItemIdByPosition(pos));
    }

    @Override
    public FilterQueryProvider getFilterQueryProvider() {
        return new TodoFilterQueryProvider();
    }

    public class TodoFilterQueryProvider implements FilterQueryProvider {

        @Override
        public Cursor runQuery(CharSequence charSequence) {
            if (charSequence == null || charSequence.toString().trim().equals("")) {
                titleFilter = "";
                changeCursor(Item.getCursor());
                return Item.getCursor();
            }
            titleFilter = charSequence.toString();
            changeCursor(Item.getCursor(charSequence.toString()));
            return Item.getCursor(charSequence.toString());
        }
    }
}
