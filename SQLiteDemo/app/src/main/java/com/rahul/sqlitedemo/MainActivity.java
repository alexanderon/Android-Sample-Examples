package com.rahul.sqlitedemo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.rahul.sqlitedemo.database.*;
import com.rahul.sqlitedemo.database.FeedReaderContract.*;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity  implements View.OnClickListener{

    private EditText etName;
    private EditText etDetail;
    private Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        btnSubmit.setOnClickListener(this);
    }

    private void init(){
        etName = (EditText) findViewById(R.id.etName);
        etDetail = (EditText) findViewById(R.id.etDetail);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnSubmit:
                if (validateFields()){
                    addItemToDatabase();
                }else {
                    Toast.makeText(this,"Validation not successfull",Toast.LENGTH_SHORT).show();
                }
                listDatabase();
                break;
        }
    }

    private Boolean validateFields() {
        if(( etName.getText().length() != 0 ) && ( etDetail.getText().length() != 0 )) {
            return true;
        }
        return false;
    }

    private void addItemToDatabase() {
        FeedReaderDbHelper dbHelper = new FeedReaderDbHelper(getApplicationContext());

        SQLiteDatabase database = dbHelper.getWritableDatabase();

        //Create the map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE, etName.getText().toString());
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_SUBTITLE,etDetail.getText().toString());

        //Insert the new row, returning the primary key value of the row
        long newRowId = database.insert(FeedReaderContract.FeedEntry.TABLE_NAME, null, values);

        if (newRowId > 0) {
            Toast.makeText(getApplicationContext(), "Entry Successfully entered into the database",Toast.LENGTH_SHORT);
            clearFields();
        }

    }

    private void clearFields(){
        etName.getText().clear();
        etDetail.getText().clear();
    }

    private void listDatabase() {
        FeedReaderDbHelper databaseHelper = new FeedReaderDbHelper(this);

        SQLiteDatabase database = databaseHelper.getWritableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.

        String[] projection = {
                FeedEntry._ID ,
                FeedEntry.COLUMN_NAME_TITLE,
                FeedEntry.COLUMN_NAME_SUBTITLE
        };

        Cursor cursor = database.query(FeedEntry.TABLE_NAME,projection, null, null, null, null, null);

        List itemIds = new ArrayList<>();
        while (cursor.moveToNext()) {
            long itemId = cursor.getLong(
                    cursor.getColumnIndexOrThrow(FeedEntry._ID));
            itemIds.add(itemId);
            Log.d("TAG", Long.toString(itemId));
        }
        cursor.close();



    }
}
