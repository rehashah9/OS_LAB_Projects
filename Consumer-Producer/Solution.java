package com.example.os;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

//defines several instance variables
public class Solution extends AppCompatActivity {
  static ArrayList<RecordEntry> r_array = new ArrayList<>();
  EditText producer;
  EditText consumer;
  EditText size;
  Button start;
  Button end;
  Button record;
  TableLayout tb;
  SyncPC sync = new SyncPC();
  String[] buffer;
  DatabaseAdapter db = new DatabaseAdapter(this);

  // This method is called when activity is created
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_solution);

    producer = (EditText) findViewById(R.id.producer_id);
    consumer = (EditText) findViewById(R.id.consumer_id);
    size = (EditText) findViewById(R.id.size);
    start = (Button) findViewById(R.id.start);
    end = (Button) findViewById(R.id.end);
    record = (Button) findViewById(R.id.record);
    tb = (TableLayout) findViewById(R.id.table_main);
    // click listener that launches a new activity called Record when it is clicked.
    record.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent i = new Intent(Solution.this, Record.class);
        startActivity(i);
      }
    });
    start.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        String s = size.getText().toString();
        if (s.compareTo("") != 0) {
          int l = 0;
          try {
            l = Integer.parseInt(s.trim());
          } catch (Exception e) {
            Toast.makeText(Solution.this, "Buffer size should be integer", Toast.LENGTH_LONG).show();
          }
          if (l > 0) {
            buffer = new String[l];
            for (int i = 0; i < l; i++) {
              buffer[i] = "-";
            }
            for (int i = 0; i < buffer.length; i++) {
              TableRow tbrow = new TableRow(Solution.this);
              TextView t1v = new TextView(Solution.this);
              t1v.setText("" + buffer[i]);
              t1v.setTextColor(Color.WHITE);
              t1v.setGravity(Gravity.CENTER);
              tbrow.addView(t1v);
              tb.addView(tbrow);
            }
            r_array.clear();
            sync.z = 1;
            try {
              sync.simulate(l, buffer, tb, Solution.this);
            } catch (Exception e) {
            }
          } else {
            Toast.makeText(Solution.this, "Buffer size should be positive", Toast.LENGTH_LONG).show();
          }
        } else {
          Toast.makeText(Solution.this, "Enter buffer size", Toast.LENGTH_LONG).show();
        }
      }
    });
    end.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        tb.removeAllViews();
        size.setText("");
        producer.setText("");
        consumer.setText("");
        sync.z = 0;
      }
    });
  }

  // implementing the functionality of a producer-consumer problem using a
  // thread-safe buffer
  public static void update_table(String[] array, TableLayout tb, Solution m) {
    tb.removeAllViews();
    for (int i = 0; i < array.length; i++) {
      TableRow tbrow = new TableRow(m);
      TextView t1v = new TextView(m);
      t1v.setText("" + array[i]);
      t1v.setTextColor(Color.WHITE);
      t1v.setGravity(Gravity.CENTER);
      tbrow.addView(t1v);
      tb.addView(tbrow);
    }
  }
}