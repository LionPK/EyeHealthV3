package com.crud.singl.eyehealthv3.fragment;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.ActivityChooserView;
import android.support.v7.widget.ShareActionProvider;
import android.view.View;

import com.crud.singl.eyehealthv3.R;


/**
 * @Copyright by Mr.Praneed Klanboon
 * Email: Praneed.kla@northbkk.ac.th
 * */

public class CustomShareActionProvider extends ShareActionProvider {
    private final Context context;

    public CustomShareActionProvider(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public View onCreateActionView() {
        ActivityChooserView chooserView = (ActivityChooserView) super.onCreateActionView();

        Drawable icon = context.getResources().getDrawable(R.drawable.share);

        chooserView.setExpandActivityOverflowButtonDrawable(icon);

        return chooserView;
    }
}
