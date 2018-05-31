package com.crud.singl.eyehealthv3;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;

import com.crud.singl.eyehealthv3.json.JSONDownloaderMEM;

public class KnowledgeActivity extends AppCompatActivity {

    String jsonMEMURL="http://eyehealthimpact.com/android_knowlage_newapi/knowledge_one.php";
    GridView mem_gv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eye_activity_knowledge);

        //Tool bar back menu
//        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar_view_knowledge);
//        setSupportActionBar(toolbar);
//
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                android.content.Intent intent = new android.content.Intent(KnowledgeActivity.this,
//                        MenuActivity.class);
//                startActivity(intent);
//            }
//        });


        // Set grid action
        mem_gv= (GridView) findViewById(R.id.knowledge_gv);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.knowledge_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new JSONDownloaderMEM(KnowledgeActivity.this,jsonMEMURL,mem_gv).execute();

            }
        });

        // set bottom navigation view
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation_member);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //set point of bottom bar selected
        Menu menu = navigation.getMenu();
        MenuItem menuItem = menu.getItem(2);
        menuItem.setChecked(true);
    }

    //set bottom bar when click action
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.screen_menu:
                    Intent intent = new Intent(KnowledgeActivity.this, MenuActivity.class);
                    startActivity(intent);
                    break;
                case R.id.screen_id:
                    Intent intent1 = new Intent(KnowledgeActivity.this, MainActivity.class);
                    startActivity(intent1);
                    break;
                case R.id.intro_id:

                    break;
                case R.id.eye_id:
//                    getSupportFragmentManager().beginTransaction()
//                            .replace(R.id.guestContentContainer, new GuestSignInFragment())
//                            .commit();
                    break;
                case R.id.mor_id:
                    Intent intent4 = new Intent(KnowledgeActivity.this, MoresActivity.class);
                    startActivity(intent4);
                    break;
            }
            return false;
        }
    };
}
