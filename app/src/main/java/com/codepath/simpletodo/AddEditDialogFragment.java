package com.codepath.simpletodo;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Locale;


public class AddEditDialogFragment extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    private static final String ITEM = "item";

    private Item item;

    public AddEditDialogFragment() {
        // Required empty public constructor
    }

    public static AddEditDialogFragment newInstance(Item item) {
        AddEditDialogFragment fragment = new AddEditDialogFragment();
        Bundle itemBunddle = new Bundle();
        itemBunddle.putBundle(ITEM, Item.getBundle(item));
        fragment.setArguments(itemBunddle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            item = Item.getItem(getArguments().getBundle(ITEM));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.dialog_add_edit_item, container, false);

        final EditText titleET = v.findViewById(R.id.txtEditItem);
        final EditText dueDateET = v.findViewById(R.id.dueDate);
        final RadioGroup priorityRG = (RadioGroup) v.findViewById(R.id.radio_group_proity);

        Button actionButton = v.findViewById(R.id.btnSave);
        Button cancelButton = v.findViewById(R.id.btnCancel);

        String title = item.title;
        String date = item.dueDate;
        int id = item._id;
        int priority = item.priority;

        if (title == null) {
            getDialog().setTitle("New Item");
            actionButton.setText(R.string.create);
        }
        else {
            getDialog().setTitle("Edit Item");
            actionButton.setText(R.string.save);
            titleET.setText(title);
            titleET.setSelection(title.length());
            dueDateET.setText(date);
            switch (priority) {
                case 0:
                    priorityRG.check(R.id.proity_low);
                    break;
                case 2:
                    priorityRG.check(R.id.proity_high);
                    break;
                default:
                    priorityRG.check(R.id.proity_medium);
            }
        }

        actionButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // When button is clicked, call up to owning activity.
                item.title = titleET.getText().toString();
                if (item.title.trim().equals("")) {
                    Toast.makeText(getActivity(), R.string.missing_title, Toast.LENGTH_SHORT).show();
                    return;
                }
                item.dueDate = dueDateET.getText().toString();
                if (!validDate(item.dueDate)) {
                    Toast.makeText(getActivity(), R.string.missing_date, Toast.LENGTH_SHORT).show();
                    return;
                }
                switch(priorityRG.getCheckedRadioButtonId()) {
                    case R.id.proity_low:
                        item.priority = 0;
                        break;
                    case R.id.proity_high:
                        item.priority = 2;
                        break;
                    default:
                        item.priority = 1;
                }
                if (getTag().equals("new")) {
                    Toast.makeText(getActivity(), "new item added ...", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getActivity(), "item updated ...", Toast.LENGTH_SHORT).show();
                }
                item.save();
                ((MainActivity) getActivity()).todoAdapter.changeCursor(Item.getCursor()); //.notifyDataSetChanged();

                dismiss();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "cancelled ...", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });

        return v;
    }

    private boolean validDate(String dueDate) {
        DateFormat formatter = DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault());
        if (dueDate == null || dueDate.trim().equals(""))
            return false;
        formatter.setLenient(false);
        try {
            formatter.parse(dueDate);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        final EditText editText = (EditText) getView().findViewById(R.id.txtEditItem);
        editText.requestFocus();
        editText.postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
            }
        }, 300);
    }
}
