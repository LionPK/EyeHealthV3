package com.crud.singl.eyehealthv3;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eye_activity_settings);

        //Tool bar back menu
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar_sign_up);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingsActivity.this,
                        MenuActivity.class);
                startActivity(intent);
            }
        });

        // when click on button round to page that user want
        findViewById(R.id.menu_one).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this, MemberProfileActivity.class));
            }
        });

        findViewById(R.id.menu_two).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this, MainActivity.class));
            }
        });

        findViewById(R.id.menu_three).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this, KnowledgeActivity.class));
            }
        });



        // set bottom navigation view
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation_member);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //set point of bottom bar selected
        Menu menu = navigation.getMenu();
        MenuItem menuItem = menu.getItem(4);
        menuItem.setChecked(true);
    }

    //set bottom bar when click action
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.screen_menu:
                    Intent intent = new Intent(SettingsActivity.this, MenuActivity.class);
                    startActivity(intent);
                    break;
                case R.id.screen_id:
                    Intent intent1 = new Intent(SettingsActivity.this, MainActivity.class);
                    startActivity(intent1);
                    break;
                case R.id.intro_id:
                    Intent intent2 = new Intent(SettingsActivity.this, KnowledgeActivity.class);
                    startActivity(intent2);
                    break;
                case R.id.eye_id:
//                    getSupportFragmentManager().beginTransaction()
//                            .replace(R.id.guestContentContainer, new GuestSignInFragment())
//                            .commit();
                    break;
                case R.id.mor_id:

                    break;
            }
            return false;
        }
    };

}
