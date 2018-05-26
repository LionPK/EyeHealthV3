package com.crud.singl.eyehealthv3.json;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.GridView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

/**
 * @Copyright by Mr.Praneed Klanboon
 * Email: Praneed.kla@northbkk.ac.th
 * */
public class JSONDownloaderMEM extends AsyncTask<Void,Void,String> {

    Context mem_k;
    String jsonMEMURL;
    GridView mem_gv;

    ProgressDialog mem_pd;

    public JSONDownloaderMEM(Context mem_k, String jsonMEMURL, GridView mem_gv) {
        this.mem_k = mem_k;
        this.jsonMEMURL = jsonMEMURL;
        this.mem_gv = mem_gv;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        mem_pd = new ProgressDialog(mem_k);
        mem_pd.setTitle("โหลดเนื้อหา");
        mem_pd.setMessage("กำลังโหลด...กรุณารอสักครู่");
        mem_pd.show();
    }


    @Override
    protected String doInBackground(Void... voids) {
        return download();
    }

    @Override
    protected void onPostExecute(String jsonMemData) {
        super.onPostExecute(jsonMemData);

        mem_pd.dismiss();
        if(jsonMemData.startsWith("Error")) {

            String error = jsonMemData;
            Toast.makeText(mem_k, error, Toast.LENGTH_SHORT).show();
        }else {

            //PARSER
            new JSONMemParser(mem_k,jsonMemData,mem_gv).execute();
        }
    }

    //Download
    private String download() {

        Object connection = Connector.connect(jsonMEMURL);
        if(connection.toString().startsWith("Error")) {
            return connection.toString();
        }

        try {
            HttpURLConnection con = (HttpURLConnection) connection;
            if(con.getResponseCode() == con.HTTP_OK) {

                //Get input from stream
                InputStream is = new BufferedInputStream(con.getInputStream());
                BufferedReader br = new BufferedReader(new InputStreamReader(is));

                String line;
                StringBuffer jsonMemData = new StringBuffer();

                //Read
                while ((line = br.readLine()) != null) {
                    jsonMemData.append(line+"\n");
                }

                //Close resources
                br.close();
                is.close();

                //return json
                return jsonMemData.toString();

            }else {

                return  "Error "+con.getResponseMessage();
            }
        }catch (IOException e) {
            e.printStackTrace();
            return "Error "+e.getMessage();
        }
    }
}
