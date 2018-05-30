package com.crud.singl.eyehealthv3;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.crud.singl.eyehealthv3.guestMenu.Callback;
import com.crud.singl.eyehealthv3.guestMenu.GuestKnowledgeFragment;
import com.crud.singl.eyehealthv3.guestMenu.GuestSignInFragment;
import com.crud.singl.eyehealthv3.guestMenu.GuestSignUpFragment;

/**
 * @Copyright by Mr.Praneed Klanboon
 * Email: Praneed.kla@northbkk.ac.th
 * */
public class GuestUserActivity extends AppCompatActivity implements Callback {
//    String jsonURL="http://eyehealthimpact.com/android_knowlage_newapi/knowledge_one.php";
//    GridView gv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eye_knowledge_guest_activity);

        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.guestContentContainer, new GuestKnowledgeFragment())
                    .commit();
        }

        //Tool bar back menu
//        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar_view_knowledge);
//        setSupportActionBar(toolbar);
//
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                android.content.Intent intent = new android.content.Intent(GuestUserActivity.this,
//                        SignInActivity.class);
//                startActivity(intent);
//            }
//        });

        // Set grid action
//        gv= (GridView) findViewById(R.id.knowledge_gv);
//
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.knowledge_fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                new JSONDownloaderOne(GuestUserActivity.this,jsonURL,gv).execute();
//
//            }
//        });


        // Defind button bar
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_dasboard:
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.guestContentContainer, new GuestKnowledgeFragment())
                            .commit();
                    return true;
                case R.id.navigation_signIn:
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.guestContentContainer, new GuestSignInFragment())
                            .commit();
                    return true;
                case R.id.navigation_signUp:
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.guestContentContainer, new GuestSignUpFragment())
                            .commit();
                    return true;
            }
            return false;
        }
    };

    @Override
    public void someEvent(Fragment fragment) {
        replaceFragment(fragment);
    }

    private void replaceFragment(Fragment fragment) {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.guestContentContainer, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
