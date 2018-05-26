package com.crud.singl.eyehealthv3.introHealth;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.crud.singl.eyehealthv3.MenuActivity;
import com.crud.singl.eyehealthv3.R;

/**
 * @Copyright by Mr.Praneed Klanboon
 * Email: Praneed.kla@northbkk.ac.th
 * */
public class UserDetailKnowledgeActivity extends AppCompatActivity {

    TextView nameTxt;
    TextView detailTxt;
    ImageView imageKnow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eye_activity_user_detail_knowledge);

        nameTxt = (TextView) findViewById(R.id.nameDetailTxt);
        detailTxt= (TextView) findViewById(R.id.descDetailTxt);
        imageKnow = (ImageView) findViewById(R.id.articleDetailImg);

        // Tool bar back menu
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar_detail);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserDetailKnowledgeActivity.this,
                        MenuActivity.class);
                startActivity(intent);
            }
        });


        //GET INTENT
        Intent i=this.getIntent();

        //RECEIVE DATA
        String name=i.getExtras().getString("NAME_KEY");
        String desc=i.getExtras().getString("DETAIL_KEY");
        String image=i.getExtras().getString("IMAGE_KEY");

        //BIND DATA
        nameTxt.setText(name);
        detailTxt.setText(desc);
        Glide.with(this).load(image).into(imageKnow);
    }
}
