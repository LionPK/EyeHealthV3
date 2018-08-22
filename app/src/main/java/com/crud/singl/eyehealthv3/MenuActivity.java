package com.crud.singl.eyehealthv3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


/**
 * @Copyright by Mr.Praneed Klanboon
 * Email: Praneed.kla@northbkk.ac.th
 * */
public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eye_menu_activity);


        //set this activity to a
        final Activity a = MenuActivity.this;

        // when click on button round to page that user want
        findViewById(R.id.button_screen).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(a, MainActivity.class));
            }
        });

        findViewById(R.id.button_new).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(a, KnowledgeActivity.class));
            }
        });


        // set bottom navigation view
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation_member);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //set point of bottom bar selected
        Menu menu = navigation.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);

    }

    //set bottom bar when click action
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.screen_menu:

                    break;
                case R.id.screen_id:
                    Intent intent1 = new Intent(MenuActivity.this, MainActivity.class);
                    startActivity(intent1);
                    break;
                case R.id.intro_id:
                    Intent intent2 = new Intent(MenuActivity.this, KnowledgeActivity.class);
                    startActivity(intent2);
                    break;
//                case R.id.eye_id:
//                    getSupportFragmentManager().beginTransaction()
//                            .replace(R.id.guestContentContainer, new GuestSignInFragment())
//                            .commit();
//                    break;
                case R.id.mor_id:
                    Intent intent4 = new Intent(MenuActivity.this, MoresActivity.class);
                    startActivity(intent4);
                    break;
            }
            return false;
        }
    };

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if(id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    public void onBackPressed() {
        finishAffinity();
        System.exit(0);
    }

}
