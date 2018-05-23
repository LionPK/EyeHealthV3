package com.crud.singl.eyehealthv3.introHealth;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.crud.singl.eyehealthv3.R;


/**
 * @Copyright by Mr.Praneed Klanboon
 * Email: Praneed.kla@northbkk.ac.th
 * */
public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        Bundle bundle = getIntent().getExtras();
        String text = bundle.getString("Message");

        TextView textData = (TextView)findViewById(R.id.textData);
        textData.setText(text);
    }
}
