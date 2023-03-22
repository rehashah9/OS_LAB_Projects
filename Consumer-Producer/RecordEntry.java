package com.example.os;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
//It is used for storing data
public class RecordEntry {
    String thread;
    int arr_index;
    String action;
    RecordEntry(String t,int a,String n)
    {
        thread=t;
        arr_index=a;
        action=n;
    }
}