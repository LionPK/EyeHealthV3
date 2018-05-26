package com.crud.singl.eyehealthv3.guestMenu;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.crud.singl.eyehealthv3.MenuActivity;
import com.crud.singl.eyehealthv3.R;
import com.crud.singl.eyehealthv3.app.AppConfig;
import com.crud.singl.eyehealthv3.app.AppController;
import com.crud.singl.eyehealthv3.helper.SQLiteHandler;
import com.crud.singl.eyehealthv3.helper.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class GuestSignInFragment extends Fragment {
    private static final String TAG = GuestSignInFragment.class.getSimpleName();
    private Button btnLogin;
    private EditText inputEmail;
    private EditText inputPassword;
    private TextInputLayout eError;
    private TextInputLayout pError;
    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;
    Callback mCallback;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (Callback) context;
        }catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement Callback");
        }
    }

    public GuestSignInFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.eye_fragment_guest_sign_in, container, false);
        view.findViewById(R.id.email_edit_text);
        view.findViewById(R.id.password_edit_text);
        view.findViewById(R.id.email_text_input);
        view.findViewById(R.id.password_text_input);

        inputEmail = (EditText) view.findViewById(R.id.email_edit_text);
        inputPassword = (EditText) view.findViewById(R.id.password_edit_text);
        btnLogin = (Button) view.findViewById(R.id.sign_in_button);

        eError = view.findViewById(R.id.email_text_input);
        pError = view.findViewById(R.id.password_text_input);

        // Progress dialog
        pDialog = new ProgressDialog(this.getActivity());
        pDialog.setCancelable(false);

        // SQLite database handler
        db = new SQLiteHandler(getContext().getApplicationContext());

        // Session manager
        session = new SessionManager(getContext().getApplicationContext());

        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
        // User is already logged in. Take him to Menu activity
        Intent intent = new Intent(GuestSignInFragment.this.getActivity(), MenuActivity.class);
        startActivity(intent);
        getActivity().finish();
        }

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

        eError.setError(null);
        pError.setError(null);

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

        return view;
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
                        Intent intent = new Intent(GuestSignInFragment.this.getActivity(),
                                MenuActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getContext().getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getContext().getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getContext().getApplicationContext(),
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
