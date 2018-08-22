package com.crud.singl.eyehealthv3;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

public class UpdateProfileActivity extends AppCompatActivity {

    String HttpURL = "http://eyehealthimpact.com/android_login_api/update.php/";
    ProgressDialog progressDialog;
    String finalResult;
    HashMap<String,String> hashMap = new HashMap<>();
    HttpParse httpParse = new HttpParse();
    EditText MemberName, MemberSurname, MemberEmail;
    Button UpdateMember;
    String UidHolder, MemberNameHolder, MemberSurnameHolder, MemberEmailHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eye_activity_update_profile);


        MemberName = (EditText)findViewById(R.id.editName);
        MemberSurname = (EditText)findViewById(R.id.editSurname);
        MemberEmail = (EditText)findViewById(R.id.editEmail);

        UpdateMember = (Button)findViewById(R.id.UpdateButton);

        // Receive Member UID, Name , Surname , Email Send by previous MemberProfileActivity.
        UidHolder = getIntent().getStringExtra("uid");
        MemberNameHolder = getIntent().getStringExtra("name");
        MemberSurnameHolder = getIntent().getStringExtra("surname");
        MemberEmailHolder = getIntent().getStringExtra("email");

        // Setting Received Member Name, Surname, Class into EditText.
        MemberName.setText(MemberNameHolder);
        MemberSurname.setText(MemberSurnameHolder);
        MemberEmail.setText(MemberEmailHolder);

        // Adding click listener to update button .
        UpdateMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Getting data from EditText after button click.
                GetDataFromEditText();

                // Sending Member Name, Surname, Email to method to update on server.
                MemberRecordUpdate(UidHolder,MemberNameHolder,MemberSurnameHolder, MemberEmailHolder);

            }
        });
    }

    // Method to get existing data from EditText.
    public void GetDataFromEditText(){

        MemberNameHolder = MemberName.getText().toString();
        MemberSurnameHolder = MemberSurname.getText().toString();
        MemberEmailHolder = MemberEmail.getText().toString();

    }

    // Method to Update Member Record.
    public void MemberRecordUpdate(final String ID, final String U_name, final String U_surname, final String U_email){

        class MemberRecordUpdateClass extends AsyncTask<String,Void,String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog = ProgressDialog.show(UpdateProfileActivity.this,"กำลังโหลดข้อมูล",null,true,true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);

                progressDialog.dismiss();

                Toast.makeText(UpdateProfileActivity.this,httpResponseMsg.toString(), Toast.LENGTH_LONG).show();

            }

            @Override
            protected String doInBackground(String... params) {

                hashMap.put("MemId",params[0]);
                hashMap.put("MemName",params[1]);
                hashMap.put("MemSurname",params[2]);
                hashMap.put("MemEmail",params[3]);
                finalResult = httpParse.postRequest(hashMap, HttpURL);

                return finalResult;
            }
        }

        MemberRecordUpdateClass memberRecordUpdateClass = new MemberRecordUpdateClass();

        memberRecordUpdateClass.execute(ID,U_name,U_surname,U_email);
    }
}
