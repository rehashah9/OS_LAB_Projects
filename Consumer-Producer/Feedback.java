package com.example.os;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class Feedback extends AppCompatActivity {
    DatabaseAdapter myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
//Calling object from xml file
        Button b = (Button) findViewById(R.id.bt1);
        RatingBar r = (RatingBar) findViewById(R.id.rt);
        TextView t = (TextView) findViewById(R.id.tv1);
        myDb =  new DatabaseAdapter(this);

//This activity is done when we click the rating bar
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String rate  = String.valueOf(r.getRating());
                t.setText(rate);
                boolean isInserted = myDb.insertFeedback(t.getText().toString());
                if(isInserted == true)
                    Toast.makeText(Feedback.this,"insert",Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(Feedback.this,"Fail",Toast.LENGTH_LONG).show();
            }
        });
    }
}