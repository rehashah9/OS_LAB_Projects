package com.example.os;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    ImageView problem;
    ImageView solution;
    ImageView contactus;
    ImageView feedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        	//Get imageview id from xml file      	//Get imageview id from xml file
        problem = (ImageView) findViewById(R.id.problem);
        solution = (ImageView) findViewById(R.id.solution);
        contactus = (ImageView) findViewById(R.id.contactus);
        feedback = (ImageView) findViewById(R.id.feedback);

      	//By clicking on problem button it will redirect to the problem page

        problem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,Problem.class);
                startActivity(i);
            }
        });
        	//By clicking on solution button it will redirect to the solution page
        solution.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,Solution.class);
                startActivity(i);
            }
        });
      	//By clicking on contactus button it will redirect to the contactus page
        contactus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(MainActivity.this,Contactus.class);
                startActivity(i);
            }
        });
      	//By clicking on feedback button it will redirect to the feedback page

        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,Feedback.class);
                startActivity(i);
            }
        });
    }

}