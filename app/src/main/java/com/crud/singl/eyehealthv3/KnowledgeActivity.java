package com.crud.singl.eyehealthv3;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.GridView;

import com.crud.singl.eyehealthv3.json.JSONDownloaderMEM;
import com.crud.singl.eyehealthv3.json.JSONDownloaderOne;

public class KnowledgeActivity extends AppCompatActivity {

    String jsonMEMURL="http://eyehealthimpact.com/android_knowlage_newapi/knowledge_one.php";
    GridView mem_gv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eye_activity_knowledge);

        //Tool bar back menu
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar_view_knowledge);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.content.Intent intent = new android.content.Intent(KnowledgeActivity.this,
                        MenuActivity.class);
                startActivity(intent);
            }
        });


        // Set grid action
        mem_gv= (GridView) findViewById(R.id.knowledge_gv);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.knowledge_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new JSONDownloaderMEM(KnowledgeActivity.this,jsonMEMURL,mem_gv).execute();

            }
        });
    }
}
