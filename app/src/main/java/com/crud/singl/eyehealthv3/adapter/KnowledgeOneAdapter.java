package com.crud.singl.eyehealthv3.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.crud.singl.eyehealthv3.R;
import com.crud.singl.eyehealthv3.entities.Knowledge;
import com.crud.singl.eyehealthv3.introHealth.DetailActivity;

import java.util.ArrayList;


/**
 * @Copyright by Mr.Praneed Klanboon
 * Email: Praneed.kla@northbkk.ac.th
 * */
public class KnowledgeOneAdapter extends BaseAdapter {

    Context k;
    ArrayList<Knowledge> knowledges;

    public KnowledgeOneAdapter(Context k, ArrayList<Knowledge> knowledges) {
        this.k = k;
        this.knowledges = knowledges;
    }

    @Override
    public int getCount() {
        return knowledges.size();
    }

    @Override
    public Object getItem(int i) {
        return knowledges.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view==null)
        {
            view= LayoutInflater.from(k).inflate(R.layout.eye_knowledge_model,viewGroup,false);
        }

        TextView nameTxt= (TextView) view.findViewById(R.id.nameTxt);
        ImageView imageKnow = (ImageView) view.findViewById(R.id.imageKnow);

        Knowledge knowledge= (Knowledge) this.getItem(i);

        final String name=knowledge.getName();
        final String detail=knowledge.getDetail();
        final String image=knowledge.getImage();
        Glide.with(k).load(image).into(imageKnow);

        nameTxt.setText(name);


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open detail activity and display name, detail and image on card view

                openDetailActivity(name,detail,image);
            }
        });

        return view;
    }

    //open activity
    private void openDetailActivity(String...details)
    {
        Intent i=new Intent(k,DetailActivity.class);
        i.putExtra("NAME_KEY",details[0]);
        i.putExtra("DETAIL_KEY",details[1]);
        i.putExtra("IMAGE_KEY",details[2]);

        k.startActivity(i);

    }
}













