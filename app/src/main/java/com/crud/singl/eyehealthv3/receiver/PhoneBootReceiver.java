package com.crud.singl.eyehealthv3.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.crud.singl.eyehealthv3.service.UsageService;
import com.crud.singl.eyehealthv3.util.Utils;

/**
 * @Copyright by Mr.Praneed Klanboon
 * Email: Praneed.kla@northbkk.ac.th
 * */
public class PhoneBootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(Utils.LOG_TAG, "received android.intent.action.BOOT_COMPLETED in "
                + PhoneBootReceiver.class.getSimpleName());

        Intent startServiceIntent = new Intent(context, UsageService.class);
        context.startService(startServiceIntent);
    }
}