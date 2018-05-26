package com.crud.singl.eyehealthv3.json;


import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @Copyright by Mr.Praneed Klanboon
 * Email: Praneed.kla@northbkk.ac.th
 * */
public class ConnectorMen {

    public static Object connect(String jsonMEMURL) {
        try {
            URL url = new URL(jsonMEMURL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            //con props
            con.setRequestMethod("GET");
            con.setConnectTimeout(15000);
            con.setReadTimeout(15000);
            con.setDoInput(true);

            return con;
        }catch (MalformedURLException e) {
            e.printStackTrace();
            return "Error"+e.getMessage();
        }catch (IOException e) {
            e.printStackTrace();
            return "Error"+e.getMessage();
        }
    }
}
