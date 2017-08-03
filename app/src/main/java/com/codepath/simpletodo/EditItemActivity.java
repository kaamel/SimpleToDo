package com.codepath.simpletodo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class EditItemActivity extends AppCompatActivity {

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

        EditText editText = (EditText) findViewById(R.id.txtEditItem);

        String item = getIntent().getStringExtra("item");
        position = getIntent().getIntExtra("pos", -1);
        if (position < 0) {
            setResult(RESULT_CANCELED, null);
            finish();
        }
        if (item == null)
            item = "";

        editText.setText(item);
        editText.setSelection(item.length());
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
        data.putExtra("editedItem", editText.getText().toString());
        data.putExtra("position", position);
        setResult(RESULT_OK, data); // set result code and bundle data for response
        finish();
    }
}
