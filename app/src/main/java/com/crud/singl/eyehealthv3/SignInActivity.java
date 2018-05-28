package com.crud.singl.eyehealthv3;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.crud.singl.eyehealthv3.app.AppConfig;
import com.crud.singl.eyehealthv3.app.AppController;
import com.crud.singl.eyehealthv3.helper.SQLiteHandler;
import com.crud.singl.eyehealthv3.helper.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * @Copyright by Mr.Praneed Klanboon
 * Email: Praneed.kla@northbkk.ac.th
 * */
public class SignInActivity extends AppCompatActivity {
    private static final String TAG = SignInActivity.class.getSimpleName();
    private Button btnLogin;
    private Button btnLinkToRegister;
    private EditText inputEmail;
    private EditText inputPassword;
    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eye_sign_in_activity);

        inputEmail = (EditText) findViewById(R.id.email_edit_text);
        inputPassword = (EditText) findViewById(R.id.password_edit_text);
        btnLogin = (Button) findViewById(R.id.sign_in_button);
        btnLinkToRegister = (Button) findViewById(R.id.sign_up_button);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // Session manager
        session = new SessionManager(getApplicationContext());

        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to Menu activity
            Intent intent = new Intent(SignInActivity.this, MenuActivity.class);
            startActivity(intent);
            finish();
        }

        //skip login
        findViewById(R.id.guest_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignInActivity.this, GuestUserActivity.class));
            }
        });

        // Login button Click Event
        btnLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                // Verify data in fields
                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                String emailPattern = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"" +
                        "(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])" +
                        "*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]" +
                        "|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:" +
                        "(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";

                final TextInputLayout eError = findViewById(R.id.email_text_input);
                final TextInputLayout pError = findViewById(R.id.password_text_input);

                eError.setError(null);
                pError.setError(null);


//                if (!email.isEmpty() && !password.isEmpty()) {
//                    // login user
//                    checkLogin(email, password);
//                } else {
//                    // Prompt user to enter credentials
//                    Toast.makeText(getApplicationContext(),"กรุณากรอกข้อมูล!", Toast.LENGTH_LONG).show();
//                }
                if (email.isEmpty() && !password.isEmpty()) {
//                    Toast.makeText(getApplicationContext(),"กรุณากรอกข้อมูล!", Toast.LENGTH_LONG).show();
                    eError.setError(getString(R.string.eye_error_not_enter_email));

                }else if ((!email.isEmpty() && !email.matches(emailPattern)) && password.isEmpty()) {
                    eError.setError(getString(R.string.eye_error_not_match_email));
                    pError.setError(getString(R.string.eye_error_not_enter_password));

                }else if (password.isEmpty() && !email.isEmpty()) {
                    pError.setError(getString(R.string.eye_error_not_enter_password));

                }else if ((!password.isEmpty() && password.length() < 8) && email.isEmpty()) {
                    pError.setError(getString(R.string.eye_error_password_less));
                    eError.setError(getString(R.string.eye_error_not_enter_email));

                }else if (email.isEmpty() && password.isEmpty()) {
                    eError.setError(getString(R.string.eye_error_not_enter_email));
                    pError.setError(getString(R.string.eye_error_not_enter_password));

                }else if ((!email.isEmpty() && !email.matches(emailPattern)) && (!password.isEmpty() && password.length() < 8)) {
                    eError.setError(getString(R.string.eye_error_not_match_email));
                    pError.setError(getString(R.string.eye_error_password_less));

                }else if ((!email.isEmpty() && !email.matches(emailPattern)) && (!password.isEmpty() && password.length() >= 8)) {
                    eError.setError(getString(R.string.eye_error_not_match_email));

                }else if ((!email.isEmpty() && email.matches(emailPattern)) && (!password.isEmpty() && password.length() < 8)) {
                    pError.setError(getString(R.string.eye_error_password_less));

                }else if ((!email.isEmpty() && email.matches(emailPattern)) && (!password.isEmpty() && password.length() >= 8)) {
                    // login user
                    checkLogin(email, password);
                }
            }

        });

        // Link to Register Screen
        btnLinkToRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        SignUpActivity.class);
                startActivity(i);
                finish();
            }
        });

    }

    /**
     * function to verify login details in mysql db
     * */
    private void checkLogin(final String email, final String password) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";

        pDialog.setMessage("Logging in ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_LOGIN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        // user successfully logged in
                        // Create login session
                        session.setLogin(true);

                        // Now store the user in SQLite
                        String uid = jObj.getString("uid");

                        JSONObject user = jObj.getJSONObject("user");
                        String name = user.getString("name");
                        String surname = user.getString("surname");
                        String email = user.getString("email");
                        String created_at = user
                                .getString("created_at");

                        // Inserting row in users table
                        db.addUser(name,surname, email, uid, created_at);

                        // Launch menu activity
                        Intent intent = new Intent(SignInActivity.this,
                                MenuActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                params.put("password", password);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
