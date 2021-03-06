package com.crud.singl.eyehealthv3;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.crud.singl.eyehealthv3.adapter.MyFragmentPagerAdapter;
import com.crud.singl.eyehealthv3.fragment.InstalledDialogFragment;
import com.crud.singl.eyehealthv3.model.StatEntry;
import com.crud.singl.eyehealthv3.receiver.PhoneBootReceiver;
import com.crud.singl.eyehealthv3.service.UsageService;
import com.crud.singl.eyehealthv3.util.Utils;
import com.crud.singl.eyehealthv3.view.SlidingTabLayout;

/**
 * @Copyright by Mr.Praneed Klanboon
 * Email: Praneed.kla@northbkk.ac.th
 * */
public class MainActivity extends AppCompatActivity implements InstalledDialogFragment.ChooserFragmentInterface {

    private static final int OFFSCREEN_PAGE_LIMIT = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eye_activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
//        toolbar.setLogo(R.mipmap.ic_launcher);
        toolbar.setTitle(R.string.app_name);
        toolbar.setTitleTextColor(getResources().getColor(R.color.productGridBackgroundColor));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), MainActivity.this));
        viewPager.setOffscreenPageLimit(OFFSCREEN_PAGE_LIMIT);

        // Give the TabLayout the ViewPager **Old method of slide page
        SlidingTabLayout slidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);
        slidingTabLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        slidingTabLayout.setDistributeEvenly(true);
        slidingTabLayout.setViewPager(viewPager);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // disable PhoneBootReceiver on Lollipop+
            ComponentName component = new ComponentName(this, PhoneBootReceiver.class);
            getPackageManager().setComponentEnabledSetting(component, PackageManager.COMPONENT_ENABLED_STATE_DISABLED , PackageManager.DONT_KILL_APP);
        }

        // set bottom navigation view
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation_member);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //set point of bottom bar selected
        Menu menu = navigation.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);
    }

    //set bottom bar when click action
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.screen_menu:
                    Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                    startActivity(intent);
                    break;
                case R.id.screen_id:

                    break;
                case R.id.intro_id:
                    Intent intent2 = new Intent(MainActivity.this, KnowledgeActivity.class);
                    startActivity(intent2);
                    break;
//                case R.id.eye_id:
//                    getSupportFragmentManager().beginTransaction()
//                            .replace(R.id.guestContentContainer, new GuestSignInFragment())
//                            .commit();
//                    break;
                case R.id.mor_id:
                    Intent intent4 = new Intent(MainActivity.this, MoresActivity.class);
                    startActivity(intent4);
                    break;
            }
            return false;
        }
    };

    @Override
    protected void onStart() {
        super.onStart();

        Log.d(Utils.LOG_TAG, "MainActivity.onStart()");

        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            Log.d(Utils.LOG_TAG, "pre-Lollipop");
            startService(new Intent(this, UsageService.class));
        }
    }

    @Override
    public void onChooserFragmentResult(int choice, Object dataObject) {
        StatEntry entry = (StatEntry)dataObject;
        String packageName = entry.getPackageName();

        switch (choice) {
            case Utils.DIALOG_LAUNCH:
                Intent launchIntent = getPackageManager().getLaunchIntentForPackage(packageName);
                if (launchIntent != null) {
                    startActivity(launchIntent);
                } else {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.installed_cannot_be_launched), Toast.LENGTH_SHORT).show();
                }
                break;

            case Utils.DIALOG_DETAILS:
                Intent intent = new Intent(this, DetailsActivity.class);
                intent.putExtra(Utils.INTENT_PACKAGE_NAME, packageName);
                startActivity(intent);
                break;

            case Utils.DIALOG_SYSTEM_MENU:
                openSystemMenu(packageName);
                break;

            case Utils.DIALOG_UNINSTALL:
                Uri packageURI = Uri.parse("package:" + packageName);
                Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);
                startActivity(uninstallIntent);
                break;

            default:
                break;
        }
    }

    private void openSystemMenu(String packageName) {
        try {
            // Open the specific App Info page:
            Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.parse("package:" + packageName));
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            // Open the generic Apps page:
            Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS);
            startActivity(intent);
        }
    }

}
