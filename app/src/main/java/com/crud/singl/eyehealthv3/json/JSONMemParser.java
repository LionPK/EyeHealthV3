package com.crud.singl.eyehealthv3.json;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.GridView;
import android.widget.Toast;

import com.crud.singl.eyehealthv3.adapter.KnowledgeMemAdapter;
import com.crud.singl.eyehealthv3.entities.Knowledge;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * @Copyright by Mr.Praneed Klanboon
 * Email: Praneed.kla@northbkk.ac.th
 * */
public class JSONMemParser extends AsyncTask<Void,Void,Boolean> {

    Context mem_k;
    String jsonMemData;
    GridView mem_gv;

    ProgressDialog mem_pd;
    ArrayList<Knowledge> knowledges = new ArrayList<>();

    public JSONMemParser(Context mem_k, String jsonMemData, GridView mem_gv) {

        this.mem_k = mem_k;
        this.jsonMemData = jsonMemData;
        this.mem_gv = mem_gv;
    }

    @Override
    protected void onPreExecute() {

        super.onPreExecute();

        mem_pd = new ProgressDialog(mem_k);
        mem_pd.setTitle("ประมวณผล");
        mem_pd.setMessage("กำลังประมาณผล...กรุณารอสักครู่");
        mem_pd.show();
    }


    @Override
    protected Boolean doInBackground(Void... voids) {
        return parse();
    }

    @Override
    protected void onPostExecute(Boolean isParsed) {
        super.onPostExecute(isParsed);

        mem_pd.dismiss();
        if (isParsed) {
            // Bind
            mem_gv.setAdapter(new KnowledgeMemAdapter(mem_k,knowledges));
        }else {
            Toast.makeText(mem_k, "ไม่สามารถประมาณผลได้,โปรดตรวจสอบข้อผิดพลาดที่ Log", Toast.LENGTH_SHORT).show();
        }
    }


    private Boolean parse() {
        try {

            JSONArray ja = new JSONArray(jsonMemData);
            JSONObject jo;

            knowledges.clear();
            Knowledge knowledge;

            for (int i=0; i<ja.length(); i++) {

                jo = ja.getJSONObject(i);

                String name = jo.getString("name");
                String detail = jo.getString("detail");
                String image = jo.getString("image");

                knowledge = new Knowledge();

                knowledge.setName(name);
                knowledge.setDetail(detail);
                knowledge.setImage(image);

                knowledges.add(knowledge);

            }

            return true;
        }catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }
}
