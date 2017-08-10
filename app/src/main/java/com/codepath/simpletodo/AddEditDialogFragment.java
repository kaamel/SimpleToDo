package com.codepath.simpletodo;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.Calendar;


public class AddEditDialogFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    // TODO: Rename parameter arguments, choose names that match
    private static final String ITEM = "item";

    private Item item;
    private EditText dueDateET;

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
        dueDateET = v.findViewById(R.id.dueDate);
        final RadioGroup priorityRG = (RadioGroup) v.findViewById(R.id.radio_group_proity);

        Button actionButton = v.findViewById(R.id.btnSave);
        Button cancelButton = v.findViewById(R.id.btnCancel);

        String title = item.title;
        long date = item.dueDate;
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
            dueDateET.setText(Utils.longToDateString(date));
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
                if (dueDateET.getText().toString().trim().equals("")) {
                    item.dueDate = Utils.todayToLong();
                }
                else if (!Utils.validDate(dueDateET.getText().toString())) {
                    Toast.makeText(getActivity(), R.string.missing_date, Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    item.dueDate = Utils.dateToLong(dueDateET.getText().toString());
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
                ((MainActivity) getActivity()).todoAdapter.refresh();

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

        final Calendar c = Calendar.getInstance();
        int startYear = c.get(Calendar.YEAR);
        int starthMonth = c.get(Calendar.MONTH);
        int startDay = c.get(Calendar.DAY_OF_MONTH);
        final DatePickerDialog datePickerDialog = new DatePickerDialog(
                getContext(), this, startYear, starthMonth, startDay);
        datePickerDialog.getDatePicker().setCalendarViewShown(true);
        datePickerDialog.getDatePicker().setSpinnersShown(false);

        dueDateET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    datePickerDialog.show();
                }
            }
        });

        return v;
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
        }, 500);
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        dueDateET.setText(Utils.longToDateString(calendar.getTime().getTime()));
    }
}
