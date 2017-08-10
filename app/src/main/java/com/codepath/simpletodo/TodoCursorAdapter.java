package com.codepath.simpletodo;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Paint;
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

    private static String titleFilter = "";
    private static boolean includeCompleted;

    public TodoCursorAdapter(Context context) {
        super(context, Item.getCursor(titleFilter, includeCompleted), 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.item, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Lookup view for data population
        TextView title = view.findViewById(R.id.item_title);
        title.setPaintFlags(0);
        title.setTextColor(Color.BLACK);
        TextView dueDate = view.findViewById(R.id.item_due_date);
        dueDate.setPaintFlags(0);
        dueDate.setTextColor(Color.BLACK);
        TextView priority = view.findViewById(R.id.item_priority);
        priority.setPaintFlags(0);
        priority.setTextColor(Color.BLACK);

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
        boolean status = ((FlowCursor) cursor).getBoolean(cursor.getColumnIndexOrThrow(Item.ITEM_ACTION_COMPLETED));
        if (!status) {
            if (date == Utils.getTodayLong()) {
                dueDate.setText(R.string.due_today);
                dueDate.setTextColor(Color.BLUE);
            } else if (date < Utils.getTodayLong()) {
                dueDate.setTextColor(Color.RED);
                if (Utils.getYesterdayLong() == date) {
                    dueDate.setText(R.string.past_due);
                } else {
                    dueDate.setText(String.format("%s %s", context.getString(R.string.past_due_since), Utils.longToDateString(date)));
                }
            } else {
                dueDate.setTextColor(Color.BLACK);
                if (Utils.getTomorrowLong() == date) {
                    dueDate.setText(R.string.due_tomorrow);
                } else {
                    dueDate.setText(String.format("%s %s", context.getString(R.string.due), Utils.longToDateString(date)));
                }
            }
        }
        else {
            title.setTextColor(Color.GRAY);
            title.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            priority.setTextColor(Color.GRAY);
            dueDate.setTextColor(Color.GRAY);
            dueDate.setText(String.format("%s %s", context.getString(R.string.due), Utils.longToDateString(date)));
        }
    }

    public void setIncludeCompleted(boolean includeCompleted) {
        if (TodoCursorAdapter.includeCompleted != includeCompleted) {
            TodoCursorAdapter.includeCompleted = includeCompleted;
            refresh();
        }
    }

    public boolean isIncludeCompleted() {
        return includeCompleted;
    }

    private int getItemIdByPosition(int pos) {
        return ((FlowCursor) getItem(pos)).getInt(getCursor().getColumnIndexOrThrow(Item.ITEM_ID));
    }

    public Item getItemByPosition(int pos) {
        return Item.findItemById(getItemIdByPosition(pos));
    }

    @Override
    public FilterQueryProvider getFilterQueryProvider() {
        return new TodoFilterQueryProvider();
    }

    private class TodoFilterQueryProvider implements FilterQueryProvider {

        @Override
        public Cursor runQuery(CharSequence charSequence) {
            if (charSequence == null || charSequence.toString().trim().equals("")) {
                titleFilter = "";
            }
            else {
                titleFilter = charSequence.toString();
            }
            refresh();
            return Item.getCursor(titleFilter, includeCompleted);
        }
    }

    public void refresh() {
        changeCursor(Item.getCursor(titleFilter, includeCompleted));
    }
}
