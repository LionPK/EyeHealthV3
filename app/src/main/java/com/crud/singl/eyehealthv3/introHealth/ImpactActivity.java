package com.crud.singl.eyehealthv3.introHealth;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.media.ImageReader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.crud.singl.eyehealthv3.KnowledgeActivity;
import com.crud.singl.eyehealthv3.MainActivity;
import com.crud.singl.eyehealthv3.R;

import org.w3c.dom.Text;


/**
 * @Copyright by Mr.Praneed Klanboon
 * Email: Praneed.kla@northbkk.ac.th
 * */
public class ImpactActivity extends AppCompatActivity {

    private Button btnReadMore;

    private int[] im_pic = {
            R.drawable.feel_level_one,
            R.drawable.feel_level_two,
            R.drawable.feel_level_three,
            R.drawable.feel_level_four

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eye_activity_impact);

        btnReadMore = (Button) findViewById(R.id.read_more_button);
        // Link to Register Screen
        btnReadMore.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        KnowledgeActivity.class);
                startActivity(i);
                finish();
            }
        });

        // Tool bar back menu
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar_impact);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ImpactActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        Bundle bundle = getIntent().getExtras();
        String im_data = bundle.getString("Message");
        String im_date = bundle.getString("Date");
        Integer pic = bundle.getInt("Picture");
        String introHealth = bundle.getString("intorHealth");

        TextView impact_data = (TextView)findViewById(R.id.impact_data);
        impact_data.setText(im_data);

        TextView date = (TextView)findViewById(R.id.im_date);
        date.setText(im_date);

        TextView intro = (TextView)findViewById(R.id.intro_data);
        intro.setText(introHealth);

        //check pic for display on view
        if (pic == im_pic[0]) {
            Bitmap pc = ((BitmapDrawable) getResources().getDrawable(R.drawable.feel_level_one)).getBitmap();
            ImageView image = (ImageView)findViewById(R.id.im_picture);
            image.setImageBitmap(pc);
        }else if (pic == im_pic[1]) {
            Bitmap pc = ((BitmapDrawable) getResources().getDrawable(R.drawable.feel_level_two)).getBitmap();
            ImageView image = (ImageView)findViewById(R.id.im_picture);
            image.setImageBitmap(pc);
        }else if (pic == im_pic[2]) {
            Bitmap pc = ((BitmapDrawable) getResources().getDrawable(R.drawable.feel_level_three)).getBitmap();
            ImageView image = (ImageView)findViewById(R.id.im_picture);
            image.setImageBitmap(pc);
        }else {
            Bitmap pc = ((BitmapDrawable) getResources().getDrawable(R.drawable.feel_level_four)).getBitmap();
            ImageView image = (ImageView)findViewById(R.id.im_picture);
            image.setImageBitmap(pc);
        }


    }
}
