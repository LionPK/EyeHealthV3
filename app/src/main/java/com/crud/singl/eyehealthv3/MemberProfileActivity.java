package com.crud.singl.eyehealthv3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.crud.singl.eyehealthv3.helper.SQLiteHandler;
import com.crud.singl.eyehealthv3.helper.SessionManager;

import java.lang.reflect.Member;
import java.util.HashMap;

public class MemberProfileActivity extends AppCompatActivity {
    private TextView txtName;
    private TextView txtSurname;
    private TextView txtEmail;
    private Button btnLogout;
    private ImageView UpdateButton;

    private SQLiteHandler db;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eye_activity_member_profile);

        //Tool bar back menu
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar_sign_up);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MemberProfileActivity.this,
                        MoresActivity.class);
                startActivity(intent);
            }
        });

        txtName = (TextView) findViewById(R.id.name);
        txtSurname = (TextView) findViewById(R.id.surname);
        txtEmail = (TextView) findViewById(R.id.email);
        btnLogout = (Button) findViewById(R.id.btnLogout);

        UpdateButton = (ImageView)findViewById(R.id.edit_user);

        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // session manager
        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }

        // Fetching user details from sqlite
        HashMap<String, String> user = db.getUserDetails();

        final String uid = user.get("uid");
        Log.d("TAG","id "+uid);
        final String name = user.get("name");
        final String surname = user.get("surname");
        final String email = user.get("email");


        UpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MemberProfileActivity.this,UpdateProfileActivity.class);

                // Sending user Id, Name, Surname and Email to next UpdateActivity.
                intent.putExtra("uid", uid);
                intent.putExtra("name", name);
                intent.putExtra("surname", surname);
                intent.putExtra("email", email);

                startActivity(intent);

                // Finishing current activity after opening next activity.
                finish();

            }
        });

        // Displaying the user details on the screen
        txtName.setText(name);
        txtSurname.setText(surname);
        txtEmail.setText(email);

        // Logout button click event
        btnLogout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });
    }

    /**
     * Logging out the user. Will set isLoggedIn flag to false in shared
     * preferences Clears the user data from sqlite users table
     * */
    private void logoutUser() {
        session.setLogin(false);

        db.deleteUsers();

        // Launching the login activity
        Intent intent = new Intent(MemberProfileActivity.this, SignInActivity.class);
        startActivity(intent);
        finish();
    }
}
