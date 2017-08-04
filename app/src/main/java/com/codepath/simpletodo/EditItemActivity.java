package com.codepath.simpletodo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

public class EditItemActivity extends AppCompatActivity {

    private int id;
    private EditText titleET;
    private EditText dueDateET;
    private RadioGroup priorityRG;

    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        ActionBar sab = getSupportActionBar();
        if (sab != null) {
            sab.setDisplayShowHomeEnabled(true);
            sab.setLogo(R.drawable.ic_action_edit);
            sab.setDisplayUseLogoEnabled(true);
        }

        TextView headerTV = (TextView) findViewById(R.id.itemTitleDialog);
        titleET = (EditText) findViewById(R.id.txtEditItem);
        dueDateET = (EditText) findViewById(R.id.dueDate);
        priorityRG = (RadioGroup) findViewById(R.id.radio_group_proity);

        Button button = (Button) findViewById(R.id.btnSave);

        String title = getIntent().getStringExtra(MainActivity.ITEM_TITLE);
        String date = getIntent().getStringExtra(MainActivity.ITEM_DUE_DATE);
        id = getIntent().getIntExtra(MainActivity.ITEM_ID, -1);
        int priority = getIntent().getIntExtra(MainActivity.ITEM_PRIORITY, 1);

        position = getIntent().getIntExtra(MainActivity.ITEM_POSITION, -1);

        if (title == null) {
            title = "";
            headerTV.setText("Enter New Item");
            button.setText("Create");
        }
        else {
            headerTV.setText("Edit Item");
            button.setText("Save");
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
    }

    @Override
    protected void onResume() {
        super.onResume();
        final EditText editText = (EditText) findViewById(R.id.txtEditItem);
        editText.requestFocus();
        editText.postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
            }
        }, 300);
    }

    public void onSaveItem(View v) {

        EditText editText = (EditText) findViewById(R.id.txtEditItem);
        Intent data = new Intent();
        // Pass relevant data back as a result
        data.putExtra(MainActivity.ITEM_TITLE, editText.getText().toString());
        data.putExtra(MainActivity.ITEM_DUE_DATE, dueDateET.getText().toString());
        switch(priorityRG.getCheckedRadioButtonId()) {
            case R.id.proity_low:
                data.putExtra(MainActivity.ITEM_PRIORITY, 0);
                break;
            case R.id.proity_high:
                data.putExtra(MainActivity.ITEM_PRIORITY, 2);
                break;
            default:
                data.putExtra(MainActivity.ITEM_PRIORITY, 1);
        }
        data.putExtra(MainActivity.ITEM_ID, id);
        data.putExtra(MainActivity.ITEM_POSITION, position);
        setResult(RESULT_OK, data);
        finish();
    }
}
