package com.example.shunichiara.supsnap_todo;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.HashMap;
import java.util.StringTokenizer;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = (Button)findViewById(R.id.createButton);
        button.setOnClickListener(mCorkyListener);
        getApplicationContext().deleteDatabase("TODO_DB");

    }

    // Create an anonymous implementation of OnClickListener
    private View.OnClickListener mCorkyListener = new View.OnClickListener() {
        public void onClick(View v) {

            //入力したテキストの内容を取得
            EditText inputText = (EditText) findViewById(R.id.inputText);
            String s = inputText.getText().toString();

            insertTodo(s);
            getTodo();

        }
    };

    private  boolean insertTodo(String todo){
        TodoDBOpenHelper dbHelper = new TodoDBOpenHelper(getApplicationContext());
        SQLiteDatabase writableDatabase = dbHelper.getWritableDatabase();
        writableDatabase.execSQL("INSERT INTO todo(TODO) VALUES('" + todo + "');");
        return false;

    }

    private SparseArray<String> getTodo(){
        SparseArray<String> todo = new SparseArray<>();
        TodoDBOpenHelper dbHelper = new TodoDBOpenHelper(getApplicationContext());

        SQLiteDatabase readableDatabase = dbHelper.getReadableDatabase();
        try {

            Cursor cursor = readableDatabase.rawQuery("select * from todo;", null);
            while(cursor.moveToNext()){
                Log.d("Todo: ", cursor.getString(0) + cursor.getString(1));
                todo.append(cursor.getInt(0), cursor.getString(1));
            }
        } finally {
            readableDatabase.close();
        }
        return todo;

    }

    public class TodoDBOpenHelper extends SQLiteOpenHelper {

        private static final int DATABASE_VERSION = 2;
        private static final String Todo_TABLE_NAME = "todo";
        private static final String Todo_TABLE_CREATE =
                "CREATE TABLE " + Todo_TABLE_NAME + " (" +
                        "_id" + " integer primary key autoincrement," +
                        "TODO" + " TEXT );" ;

        TodoDBOpenHelper(Context context) {
            super(context, "TODO_DB", null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(Todo_TABLE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        }
    }



}


