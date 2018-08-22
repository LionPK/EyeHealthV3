package com.crud.singl.eyehealthv3;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

public class FirebaseActivity extends AppCompatActivity {
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase);
    }

    public void showToken(View view) {
        // แสดง token มาให้ดูหน่อยเสะ
        mTextView.setText(FirebaseInstanceId.getInstance().getToken());
        Log.i("token", FirebaseInstanceId.getInstance().getToken());
    }
    public void subscribe(View view) {
        // สับตะไคร้หัวข้อ news
        FirebaseMessaging.getInstance().subscribeToTopic("news");
//        mTextView.setText(R.string.subscribed);
    }
    public void unsubscribe(View view) {
        // ยกเลิกสับตะไคร้หัวข้อ news
        FirebaseMessaging.getInstance().unsubscribeFromTopic("news");
//        mTextView.setText(R.string.unsubscribed);
    }
}
