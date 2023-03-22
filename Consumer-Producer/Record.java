package com.example.os;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

public class Record extends AppCompatActivity {
    TableLayout tb;
    ArrayList<RecordEntry> buffer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
	       //Clearing out the contents of the TableLayout and preparing it for new data to be added.
        tb = (TableLayout) findViewById(R.id.table_main);
        buffer = Solution.r_array;
        tb.removeAllViews();
        tb.invalidate();
        tb.refreshDrawableState();
      	//Adding rows in tablelayout
        for (int i = 0; i < buffer.size(); i++) {
            TableRow tbrow = new TableRow(Record.this);
            TextView t1v = new TextView(Record.this);

            t1v.setText(" " + buffer.get(i).thread+buffer.get(i).action+ " at index " +buffer.get(i).arr_index);
            t1v.setTextColor(Color.WHITE);
            t1v.setGravity(Gravity.CENTER);
            tbrow.addView(t1v);
            tb.addView(tbrow);
        }
    }
}