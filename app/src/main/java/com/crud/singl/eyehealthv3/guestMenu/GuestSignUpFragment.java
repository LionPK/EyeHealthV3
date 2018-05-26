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
import com.crud.singl.eyehealthv3.SignInActivity;
import com.crud.singl.eyehealthv3.SignUpActivity;
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
public class GuestSignUpFragment extends Fragment {
    private static final String TAG = GuestSignUpFragment.class.getSimpleName();
    private Button btnRegister;
    private EditText inputName;
    private EditText inputSurname;
    private EditText inputEmail;
    private EditText inputPassword;
    private EditText inputRePassword;
    private TextInputLayout nError;
    private TextInputLayout sError;
    private TextInputLayout eError;
    private TextInputLayout pError;
    private TextInputLayout rError;
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



    public GuestSignUpFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.eye_fragment_guest_sign_up, container, false);
        view.findViewById(R.id.name_edit_text);
        view.findViewById(R.id.name_text_input);
        view.findViewById(R.id.surname_edit_text);
        view.findViewById(R.id.surname_text_input);
        view.findViewById(R.id.email_edit_text);
        view.findViewById(R.id.email_text_input);
        view.findViewById(R.id.password_edit_text);
        view.findViewById(R.id.password_text_input);
        view.findViewById(R.id.re_enter_password_edit_text);
        view.findViewById(R.id.re_enter_password_text_input);

        //declare type link to id on view .xml file
        inputName = (EditText) view.findViewById(R.id.name_edit_text);
        inputSurname = (EditText) view.findViewById(R.id.surname_edit_text);
        inputEmail = (EditText) view.findViewById(R.id.email_edit_text);
        inputPassword = (EditText) view.findViewById(R.id.password_edit_text);
        inputRePassword = (EditText) view.findViewById(R.id.re_enter_password_edit_text);
        btnRegister = (Button) view.findViewById(R.id.sign_up_button);

        nError = view.findViewById(R.id.name_text_input);
        sError = view.findViewById(R.id.surname_text_input);
        eError = view.findViewById(R.id.email_text_input);
        pError = view.findViewById(R.id.password_text_input);
        rError = view.findViewById(R.id.re_enter_password_text_input);

        // Progress dialog
        pDialog = new ProgressDialog(this.getActivity());
        pDialog.setCancelable(false);

        // Session manager
        session = new SessionManager(getContext().getApplicationContext());

        // SQLite database handler
        db = new SQLiteHandler(getActivity().getApplicationContext());

        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to Menu activity
            Intent intent = new Intent(GuestSignUpFragment.this.getActivity(),
                    MenuActivity.class);
            startActivity(intent);
            getActivity().finish();
        }


        // Register Button Click event
        btnRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String name = inputName.getText().toString().trim();
                String surname = inputSurname.getText().toString().trim();
                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();
                String rePassword = inputRePassword.getText().toString().trim();

                String emailPattern = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"" +
                        "(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])" +
                        "*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]" +
                        "|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:" +
                        "(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";

                nError.setError(null);
                sError.setError(null);
                eError.setError(null);
                pError.setError(null);
                rError.setError(null);

                if (name.isEmpty() && surname.isEmpty() && email.isEmpty() && password.isEmpty() && rePassword.isEmpty()) {
                    nError.setError(getString(R.string.eye_error_not_enter_name));
                    sError.setError(getString(R.string.eye_error_not_enter_surname));
                    eError.setError(getString(R.string.eye_error_not_enter_email));
                    pError.setError(getString(R.string.eye_error_not_enter_pass));
                    rError.setError(getString(R.string.eye_error_not_enter_re_pass));

                }else if (name.isEmpty() &&!surname.isEmpty() && !email.isEmpty() && !password.isEmpty() && !rePassword.isEmpty()) {
                    nError.setError(getString(R.string.eye_error_not_enter_name));

                }else if (!name.isEmpty() && surname.isEmpty() && !email.isEmpty() && !password.isEmpty() && !rePassword.isEmpty()) {
                    sError.setError(getString(R.string.eye_error_not_enter_surname));

                }else if (!name.isEmpty() &&!surname.isEmpty() && email.isEmpty() && !password.isEmpty() && !rePassword.isEmpty()) {
                    eError.setError(getString(R.string.eye_error_not_enter_email));

                }else if (name.isEmpty() &&surname.isEmpty() && (!email.isEmpty() && !email.matches(emailPattern)) && password.isEmpty() && rePassword.isEmpty()) {
                    eError.setError(getString(R.string.eye_error_not_match_email));

                }else if (!name.isEmpty() &&!surname.isEmpty() && (!email.isEmpty() && !email.matches(emailPattern)) && (!password.isEmpty() && password.length() < 8) && (!rePassword.isEmpty() && !password.equals(rePassword))) {
                    eError.setError(getString(R.string.eye_error_not_match_email));
                    pError.setError(getString(R.string.eye_error_password_less));
                    rError.setError(getString(R.string.eye_error_not_enter_re_pass_notmatch));

                }else if (!name.isEmpty() &&!surname.isEmpty() && (!email.isEmpty() && email.matches(emailPattern)) && (!password.isEmpty() && password.length() < 8) && (!rePassword.isEmpty() && !password.equals(rePassword))) {
                    pError.setError(getString(R.string.eye_error_password_less));
                    rError.setError(getString(R.string.eye_error_not_enter_re_pass_notmatch));

                }else if (!name.isEmpty() &&!surname.isEmpty() && (!email.isEmpty() && email.matches(emailPattern)) && (!password.isEmpty() && password.length() >= 8) && (!rePassword.isEmpty() && !password.equals(rePassword))) {
                    rError.setError(getString(R.string.eye_error_not_enter_re_pass_notmatch));

                }else if (!name.isEmpty() &&!surname.isEmpty() && !email.isEmpty() && password.isEmpty() && !rePassword.isEmpty()) {
                    pError.setError(getString(R.string.eye_error_not_enter_pass));

                }else if (!name.isEmpty() &&!surname.isEmpty() && !email.isEmpty() && !password.isEmpty() && rePassword.isEmpty()) {
                    rError.setError(getString(R.string.eye_error_not_enter_re_pass));

                }else if (!name.isEmpty() &&!surname.isEmpty() && (!email.isEmpty() && email.matches(emailPattern)) && (!password.isEmpty() && password.length() >= 8) && (!rePassword.isEmpty() && password.equals(rePassword))) {
                    registerUser(name, surname, email, password);
                }
            }
        });

        return view;
    }

    /**
     * Function to store user in MySQL database will post params(tag, name,
     * email, password) to register url
     * */
    private void registerUser(final String name, final String surname, final String email,
                              final String password) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        pDialog.setMessage("Registering ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_REGISTER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite
                        String uid = jObj.getString("uid");

                        JSONObject user = jObj.getJSONObject("user");
                        String name = user.getString("name");
                        String surname = user.getString("surname");
                        String email = user.getString("email");
                        String created_at = user
                                .getString("created_at");

                        // Inserting row in users table
                        db.addUser(name, surname, email, uid, created_at);

                        Toast.makeText(getContext().getApplicationContext(), "ผู้ใช้ลงทะเบียนเรียบร้อยแล้ว ลองเข้าสู่ระบบทันที!", Toast.LENGTH_LONG).show();

                        // Launch login activity
                        Intent intent = new Intent(
                                GuestSignUpFragment.this.getActivity(),
                                SignInActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getContext().getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(getContext().getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", name);
                params.put("surname", surname);
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
