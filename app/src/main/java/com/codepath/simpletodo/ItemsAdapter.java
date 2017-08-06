package com.codepath.simpletodo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by kaamel on 8/4/17.
 */

public class ItemsAdapter extends ArrayAdapter<Item> {

    private int listLayout;

    public ItemsAdapter(Context context, int resource, List<Item> items) {
        super(context, resource, items);
        listLayout = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        Item item = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(listLayout, parent, false);
        }
        // Lookup view for data population
        TextView title = convertView.findViewById(R.id.item_title);
        TextView dueDate = convertView.findViewById(R.id.item_due_date);
        TextView priority = convertView.findViewById(R.id.item_priority);

        title.setText(item.title);
        switch (item.priority) {
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
        dueDate.setText("Due " + item.dueDate);
        return convertView;
    }

}
