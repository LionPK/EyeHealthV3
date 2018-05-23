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

public class JSONDownloaderOne extends AsyncTask<Void,Void,String> {

    Context k;
    String jsonURL;
    GridView gv;

    ProgressDialog pd;

    public JSONDownloaderOne(Context k, String jsonURL, GridView gv) {
        this.k = k;
        this.jsonURL = jsonURL;
        this.gv = gv;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        pd=new ProgressDialog(k);
        pd.setTitle("โหลดเนื้อหา");
        pd.setMessage("กำลังโหลด...กรุณารอสักครู่");
        pd.show();

    }

    @Override
    protected String doInBackground(Void... voids) {
        return download();
    }

    @Override
    protected void onPostExecute(String jsonData) {
        super.onPostExecute(jsonData);

        pd.dismiss();
        if(jsonData.startsWith("Error"))
        {
            String error=jsonData;
            Toast.makeText(k, error, Toast.LENGTH_SHORT).show();
        }else
        {
            //PARSER
            new com.crud.singl.eyehealthv3.json.JSONParser(k,jsonData,gv).execute();
        }

    }

    //DOWNLOAD
    private String download()
    {
        Object connection= com.crud.singl.eyehealthv3.json.Connector.connect(jsonURL);
        if(connection.toString().startsWith("Error"))
        {
            return connection.toString();
        }

        try
        {
            HttpURLConnection con= (HttpURLConnection) connection;
            if(con.getResponseCode()==con.HTTP_OK)
            {
                //GET INPUT FROM STREAM
                InputStream is=new BufferedInputStream(con.getInputStream());
                BufferedReader br=new BufferedReader(new InputStreamReader(is));

                String line;
                StringBuffer jsonData=new StringBuffer();

                //READ
                while ((line=br.readLine()) != null)
                {
                    jsonData.append(line+"\n");
                }

                //CLOSE RESOURCES
                br.close();
                is.close();

                //RETURN JSON
                return jsonData.toString();

            }else
            {
                return "Error "+con.getResponseMessage();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "Error "+e.getMessage();

        }
    }

}

