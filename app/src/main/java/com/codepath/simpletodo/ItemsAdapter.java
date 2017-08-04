package com.codepath.simpletodo;

import android.content.Context;
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

    int listLayout;

    public ItemsAdapter(Context context, int resource, List<Item> items) {
        super(context, resource, items);
        listLayout = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Item item = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(listLayout, parent, false);
        }
        // Lookup view for data population
        TextView title = (TextView) convertView.findViewById(R.id.item_title);
        TextView dueDate = (TextView) convertView.findViewById(R.id.item_due_date);
        TextView priority = (TextView) convertView.findViewById(R.id.item_priority);
        title.setText(item.title);
        switch (item.priority) {
            case 0:
                priority.setText("Priority: Low");
                break;
            case 2:
                priority.setText("Priority: High");
                break;
            default:
                priority.setText("Priority: Medium");
                break;

        }
        dueDate.setText("Due " + item.dueDate);
        return convertView;
    }

}
