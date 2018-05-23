package com.crud.singl.eyehealthv3.json;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.GridView;
import android.widget.Toast;

import com.crud.singl.eyehealthv3.adapter.KnowledgeOneAdapter;
import com.crud.singl.eyehealthv3.entities.Knowledge;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class JSONParser extends AsyncTask<Void,Void,Boolean>{

    Context k;
    String jsonData;
    GridView gv;

    ProgressDialog pd;
    ArrayList<Knowledge> knowledges=new ArrayList<>();

    public JSONParser(Context k, String jsonData, GridView gv) {
        this.k = k;
        this.jsonData = jsonData;
        this.gv = gv;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        pd=new ProgressDialog(k);
        pd.setTitle("Parse");
        pd.setMessage("Parsing...Please wait");
        pd.show();
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        return parse();
    }

    @Override
    protected void onPostExecute(Boolean isParsed) {
        super.onPostExecute(isParsed);

        pd.dismiss();
        if(isParsed)
        {
            //BIND
            gv.setAdapter(new KnowledgeOneAdapter(k,knowledges));
        }else
        {
            Toast.makeText(k, "Unable To Parse,Check Your Log output", Toast.LENGTH_SHORT).show();
        }

    }

    private Boolean parse()
    {
        try
        {
            JSONArray ja=new JSONArray(jsonData);
            JSONObject jo;

            knowledges.clear();
            Knowledge knowledge;

            for (int i=0;i<ja.length();i++)
            {
                jo=ja.getJSONObject(i);

                String name=jo.getString("name");
                String detail=jo.getString("detail");
                String image=jo.getString("image");

                knowledge=new Knowledge();

                knowledge.setName(name);
                knowledge.setDetail(detail);
                knowledge.setImage(image);

                knowledges.add(knowledge);
            }

            return true;

        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }


}