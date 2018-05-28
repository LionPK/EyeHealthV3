package com.crud.singl.eyehealthv3.fragment;

import android.annotation.TargetApi;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.crud.singl.eyehealthv3.R;
import com.crud.singl.eyehealthv3.adapter.StatsListAdapter;
import com.crud.singl.eyehealthv3.introHealth.ImpactActivity;
import com.crud.singl.eyehealthv3.loader.DBStatProvider;
import com.crud.singl.eyehealthv3.loader.NativeStatProvider;
import com.crud.singl.eyehealthv3.loader.StatProvider;
import com.crud.singl.eyehealthv3.model.Period;
import com.crud.singl.eyehealthv3.model.StatEntry;
import com.crud.singl.eyehealthv3.util.Prefs;
import com.crud.singl.eyehealthv3.util.Utils;

import java.util.List;


/**
 * @Copyright by Mr.Praneed Klanboon
 * Email: Praneed.kla@northbkk.ac.th
 * */
public class StatsFragment extends Fragment {

    private Context context;
    private LoadStatsTask loadTask;
    private StatProvider loader;

    private TextView tvPeriod;
    private ListView listView;
    private TextView hour_time;
    private TextView minute_time;
    public  TextView cur_time;
    private Button calculat_impact;
    private List<StatEntry> statList;
    private ProgressBar progressBar;

    private int[] im_picture = {
            R.drawable.feel_level_one,
            R.drawable.feel_level_two,
            R.drawable.feel_level_three,
            R.drawable.feel_level_four

    };

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_stats, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        switch (item.getItemId()) {
            case R.id.choose_submenu_day:
                prefs.edit().putInt(Prefs.STATS_PERIOD, Period.DAY.asInt()).commit();
                refreshList();
                return true;

            case R.id.choose_submenu_yesterday:
                prefs.edit().putInt(Prefs.STATS_PERIOD, Period.YESTERDAY.asInt()).commit();
                refreshList();
                return true;

            case R.id.choose_submenu_week:
                prefs.edit().putInt(Prefs.STATS_PERIOD, Period.WEEK.asInt()).commit();
                refreshList();
                return true;

            case R.id.choose_submenu_year:
                prefs.edit().putInt(Prefs.STATS_PERIOD, Period.YEAR.asInt()).commit();
                refreshList();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup group, Bundle saved) {
        return inflater.inflate(R.layout.fragment_stats, group, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setHasOptionsMenu(true);
        context = getActivity().getApplicationContext();

        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            loader = new DBStatProvider(context);
        } else {
            loader = new NativeStatProvider(context);
        }

        progressBar = (ProgressBar) getView().findViewById(R.id.stats_progress_bar);
        tvPeriod = (TextView) getView().findViewById(R.id.stats_tv_period);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Period period = Period.fromInt(prefs.getInt(Prefs.STATS_PERIOD, 1));
        setTextMark(period);

        listView = (ListView) getView().findViewById(R.id.stats_list);
        hour_time = (TextView) getView().findViewById(R.id.hour_time);
        minute_time = (TextView) getView().findViewById(R.id.minute_time);

        //display cur time
        String mydate = (DateFormat.format("dd-MM-yyyy hh:mm:ss", new java.util.Date()).toString());
        cur_time = (TextView) getView().findViewById(R.id.cur_time);
        cur_time.setText("ใช้งานล่าสุด " + mydate);


        calculat_impact = (Button) getView().findViewById(R.id.calculate_impact);

        calculat_impact.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                int hour = Integer.parseInt(hour_time.getText().toString());
                int minute = Integer.parseInt(minute_time.getText().toString());
                String mydate = (DateFormat.format("dd-MM-yyyy hh:mm:ss", new java.util.Date()).toString());

                if (hour < 1 && minute < 30) {
                    Log.d("TAGE", "จากการวิเคราะห์พบว่าคุณยังไม่มีอาการทางสายตาที่เกิดขึ้นระหว่างการเล่นสมาร์ทโฟน");
                    Log.d("TAGE","" +mydate);
                    Log.d("TAGE",""+im_picture[0]);


                    final String EMImpact = "จากการวิเคราะห์พบว่าคุณยังไม่มีอาการทางสายตาที่เกิดขึ้นระหว่างการเล่นสมาร์ทโฟน";
                    final String Date = mydate;
                    Intent intent = new Intent(StatsFragment.this.getActivity(), ImpactActivity.class);
                    intent.putExtra("Message", EMImpact);
                    intent.putExtra("Date", mydate);
                    intent.putExtra("Picture",im_picture[0]);
                    startActivity(intent);

                } else if (hour < 1 && (minute >= 30 && minute < 60)) {
                    Log.d("TAGE", "จากการใช้งานสมาร์ทโฟนเป็นระยะเวลา " + hour + " ชั่วโมง " + minute + " นาที คุณอาจจะมีอาการปวดกระบอกตา ตาพร่า หรือการมองเห็นภาพซ้อน");
                    Log.d("TAGE","" +mydate);
                    Log.d("TAGE",""+im_picture[1]);

                    final String EMImpact = "จากการใช้งานสมาร์ทโฟนเป็นระยะเวลา " + hour + " ชั่วโมง " + minute + " นาที คุณอาจจะมีอาการปวดกระบอกตา ตาพร่า หรือการมองเห็นภาพซ้อน";
                    Intent intent = new Intent(StatsFragment.this.getActivity(), ImpactActivity.class);
                    intent.putExtra("Message", EMImpact);
                    intent.putExtra("Date", mydate);
                    intent.putExtra("Picture",im_picture[1]);
                    startActivity(intent);

                } else if ((hour >= 1 && minute < 30) && (hour < 2)) {
                    Log.d("TAGE", "จากการใช้งานสมาร์ทโฟนเป็นระยะเวลา " + hour + " ชั่วโมง " + minute + " นาที คุณอาจจะมีอาการปวดกระบอกตา ตาพร่า หรือการเคืองตา");
                    Log.d("TAGE","" +mydate);
                    Log.d("TAGE",""+im_picture[1]);

                    final String EMImpact = "จากการใช้งานสมาร์ทโฟนเป็นระยะเวลา " + hour + " ชั่วโมง " + minute + " นาที คุณอาจจะมีอาการปวดกระบอกตา ตาพร่า หรือการเคืองตา";
                    Intent intent = new Intent(StatsFragment.this.getActivity(), ImpactActivity.class);
                    intent.putExtra("Message", EMImpact);
                    intent.putExtra("Date", mydate);
                    intent.putExtra("Picture",im_picture[1]);
                    startActivity(intent);

                } else if ((hour >= 1 && (minute >= 30 && minute < 60)) && (hour < 2)) {
                    Log.d("TAGE", "จากการใช้งานสมาร์ทโฟนเป็นระยะเวลา " + hour + " ชั่วโมง " + minute + " นาที คุณอาจจะมีอาการปวดกระบอกตา ตาพร่า หรือการเคืองตา");
                    Log.d("TAGE","" +mydate);
                    Log.d("TAGE",""+im_picture[1]);

                    final String EMImpact = "จากการใช้งานสมาร์ทโฟนเป็นระยะเวลา " + hour + " ชั่วโมง " + minute + " นาที คุณอาจจะมีอาการปวดกระบอกตา ตาพร่า หรือการเคืองตา";
                    final String Date = mydate;
                    Intent intent = new Intent(StatsFragment.this.getActivity(), ImpactActivity.class);
                    intent.putExtra("Message", EMImpact);
                    intent.putExtra("Date", mydate);
                    intent.putExtra("Picture",im_picture[1]);
                    startActivity(intent);

                } else if ((hour >= 2 && minute < 30) && (hour < 3)) {
                    Log.d("TAGE", "จากการใช้งานสมาร์ทโฟนเป็นระยะเวลา " + hour + " ชั่วโมง " + minute + " นาที คุณอาจจะมีอาการปวดกระบอกตา ตาพร่า หรือการเคืองตา");
                    Log.d("TAGE","" +mydate);
                    Log.d("TAGE",""+im_picture[1]);

                    final String EMImpact = "จากการใช้งานสมาร์ทโฟนเป็นระยะเวลา " + hour + " ชั่วโมง " + minute + " นาที คุณอาจจะมีอาการปวดกระบอกตา ตาพร่า หรือการเคืองตา";
                    Intent intent = new Intent(StatsFragment.this.getActivity(), ImpactActivity.class);
                    intent.putExtra("Message", EMImpact);
                    intent.putExtra("Date", mydate);
                    intent.putExtra("Picture",im_picture[1]);
                    startActivity(intent);

                } else if ((hour >= 2 && (minute >= 30 && minute < 60)) && (hour < 3)) {
                    Log.d("TAGE", "จากการใช้งานสมาร์ทโฟนเป็นระยะเวลา " + hour + " ชั่วโมง " + minute + " นาที คุณอาจจะมีอาการปวดกระบอกตา ตาพร่า หรือการเคืองตา");
                    Log.d("TAGE","" +mydate);
                    Log.d("TAGE",""+im_picture[1]);

                    final String EMImpact = "จากการใช้งานสมาร์ทโฟนเป็นระยะเวลา " + hour + " ชั่วโมง " + minute + " นาที คุณอาจจะมีอาการปวดกระบอกตา ตาพร่า หรือการเคืองตา";
                    Intent intent = new Intent(StatsFragment.this.getActivity(), ImpactActivity.class);
                    intent.putExtra("Message", EMImpact);
                    intent.putExtra("Date", mydate);
                    intent.putExtra("Picture",im_picture[1]);
                    startActivity(intent);

                } else if ((hour >= 3 && minute < 30) && (hour < 4)) {
                    Log.d("TAGE", "จากการใช้งานสมาร์ทโฟนเป็นระยะเวลา " + hour + " ชั่วโมง " + minute + " นาที คุณอาจจะมีอาการปวดกระบอกตา เคืองตา หรืออาการน้ำตาไหล");
                    Log.d("TAGE","" +mydate);
                    Log.d("TAGE",""+im_picture[1]);

                    final String EMImpact = "จากการใช้งานสมาร์ทโฟนเป็นระยะเวลา " + hour + " ชั่วโมง " + minute + " นาที คุณอาจจะมีอาการปวดกระบอกตา เคืองตา หรืออาการน้ำตาไหล";
                    Intent intent = new Intent(StatsFragment.this.getActivity(), ImpactActivity.class);
                    intent.putExtra("Message", EMImpact);
                    intent.putExtra("Date", mydate);
                    intent.putExtra("Picture",im_picture[1]);
                    startActivity(intent);

                } else if ((hour >= 3 && (minute >= 30 && minute < 60)) && (hour < 4)) {
                    Log.d("TAGE", "จากการใช้งานสมาร์ทโฟนเป็นระยะเวลา " + hour + " ชั่วโมง " + minute + " นาที คุณอาจจะมีอาการปวดกระบอกตา เคืองตา หรืออาการน้ำตาไหล");
                    Log.d("TAGE","" +mydate);
                    Log.d("TAGE",""+im_picture[1]);

                    final String EMImpact = "จากการใช้งานสมาร์ทโฟนเป็นระยะเวลา " + hour + " ชั่วโมง " + minute + " นาที คุณอาจจะมีอาการปวดกระบอกตา เคืองตา หรืออาการน้ำตาไหล";
                    Intent intent = new Intent(StatsFragment.this.getActivity(), ImpactActivity.class);
                    intent.putExtra("Message", EMImpact);
                    intent.putExtra("Date", mydate);
                    intent.putExtra("Picture",im_picture[1]);
                    startActivity(intent);

                } else if ((hour >= 4 && minute < 30) && (hour < 5)) {
                    Log.d("TAGE", "จากการใช้งานสมาร์ทโฟนเป็นระยะเวลา " + hour + " ชั่วโมง " + minute + " นาที คุณอาจจะมีอาการปวดกระบอกตา เคืองตา หรือการมองเห็นภาพซ้อน");
                    Log.d("TAGE","" +mydate);
                    Log.d("TAGE",""+im_picture[2]);

                    final String EMImpact = "จากการใช้งานสมาร์ทโฟนเป็นระยะเวลา " + hour + " ชั่วโมง " + minute + " นาที คุณอาจจะมีอาการปวดกระบอกตา เคืองตา หรือการมองเห็นภาพซ้อน";
                    Intent intent = new Intent(StatsFragment.this.getActivity(), ImpactActivity.class);
                    intent.putExtra("Message", EMImpact);
                    intent.putExtra("Date", mydate);
                    intent.putExtra("Picture",im_picture[2]);
                    startActivity(intent);

                } else if ((hour >= 4 && (minute >= 30 && minute < 60)) && (hour < 5)) {
                    Log.d("TAGE", "จากการใช้งานสมาร์ทโฟนเป็นระยะเวลา " + hour + " ชั่วโมง " + minute + " นาที คุณอาจจะมีอาการปวดกระบอกตา เคืองตา หรืออาการน้ำตาไหล");
                    Log.d("TAGE","" +mydate);
                    Log.d("TAGE",""+im_picture[2]);

                    final String EMImpact = "จากการใช้งานสมาร์ทโฟนเป็นระยะเวลา " + hour + " ชั่วโมง " + minute + " นาที คุณอาจจะมีอาการปวดกระบอกตา เคืองตา หรืออาการน้ำตาไหล";
                    Intent intent = new Intent(StatsFragment.this.getActivity(), ImpactActivity.class);
                    intent.putExtra("Message", EMImpact);
                    intent.putExtra("Date", mydate);
                    intent.putExtra("Picture",im_picture[2]);
                    startActivity(intent);

                } else if ((hour >= 5 && minute < 30) && (hour < 6)) {
                    Log.d("TAGE", "จากการใช้งานสมาร์ทโฟนเป็นระยะเวลา " + hour + " ชั่วโมง " + minute + " นาที คุณอาจจะมีอาการปวดกระบอกตา เคืองตา หรืออาการน้ำตาไหล");
                    Log.d("TAGE","" +mydate);
                    Log.d("TAGE",""+im_picture[2]);

                    final String EMImpact = "จากการใช้งานสมาร์ทโฟนเป็นระยะเวลา " + hour + " ชั่วโมง " + minute + " นาที คุณอาจจะมีอาการปวดกระบอกตา เคืองตา หรืออาการน้ำตาไหล";
                    Intent intent = new Intent(StatsFragment.this.getActivity(), ImpactActivity.class);
                    intent.putExtra("Message", EMImpact);
                    intent.putExtra("Date", mydate);
                    intent.putExtra("Picture",im_picture[2]);
                    startActivity(intent);

                } else if ((hour >= 5 && (minute >= 30 && minute < 60)) && (hour < 6)) {
                    Log.d("TAGE", "จากการใช้งานสมาร์ทโฟนเป็นระยะเวลา " + hour + " ชั่วโมง " + minute + " นาที  ุณอาจจะมีอาการปวดกระบอกตา เคืองตา หรืออาการน้ำตาไหล");
                    Log.d("TAGE","" +mydate);
                    Log.d("TAGE",""+im_picture[2]);

                    final String EMImpact = "จากการใช้งานสมาร์ทโฟนเป็นระยะเวลา " + hour + " ชั่วโมง " + minute + " นาที คุณอาจจะมีอาการปวดกระบอกตา เคืองตา หรืออาการน้ำตาไหล";
                    Intent intent = new Intent(StatsFragment.this.getActivity(), ImpactActivity.class);
                    intent.putExtra("Message", EMImpact);
                    intent.putExtra("Date", mydate);
                    intent.putExtra("Picture",im_picture[2]);
                    startActivity(intent);

                } else if ((hour >= 6 && minute < 30) && (hour < 7)) {
                    Log.d("TAGE", "จากการใช้งานสมาร์ทโฟนเป็นระยะเวลา 6 " + hour + " ชั่วโมง " + minute + " นาที คุณอาจจะมีอาการปวดกระบอกตา เคืองตา หรืออาการน้ำตาไหล");
                    Log.d("TAGE","" +mydate);
                    Log.d("TAGE",""+im_picture[2]);

                    final String EMImpact = "จากการใช้งานสมาร์ทโฟนเป็นระยะเวลา " + hour + " ชั่วโมง " + minute + " นาที คุณอาจจะมีอาการปวดกระบอกตา เคืองตา หรืออาการน้ำตาไหล";
                    Intent intent = new Intent(StatsFragment.this.getActivity(), ImpactActivity.class);
                    intent.putExtra("Message", EMImpact);
                    intent.putExtra("Date", mydate);
                    intent.putExtra("Picture",im_picture[2]);
                    startActivity(intent);

                } else if ((hour >= 6 && (minute >= 30 && minute < 60)) && (hour < 7)) {
                    Log.d("TAGE", "จากการใช้งานสมาร์ทโฟนเป็นระยะเวลา " + hour + " ชั่วโมง " + minute + " นาที คุณอาจจะมีอาการปวดกระบอกตา ตาพร่า หรืออาการน้ำตาไหล");
                    Log.d("TAGE","" +mydate);
                    Log.d("TAGE",""+im_picture[2]);

                    final String EMImpact = "จากการใช้งานสมาร์ทโฟนเป็นระยะเวลา " + hour + " ชั่วโมง " + minute + " นาที คุณอาจจะมีอาการปวดกระบอกตา ตาพร่า หรืออาการน้ำตาไหล";
                    Intent intent = new Intent(StatsFragment.this.getActivity(), ImpactActivity.class);
                    intent.putExtra("Message", EMImpact);
                    intent.putExtra("Date", mydate);
                    intent.putExtra("Picture",im_picture[2]);
                    startActivity(intent);

                } else if ((hour >= 7 && minute < 30) && (hour < 8)) {
                    Log.d("TAGE", "จากการใช้งานสมาร์ทโฟนเป็นระยะเวลา " + hour + " ชั่วโมง " + minute + " นาที คุณอาจจะมีอาการปวดกระบอกตา เคืองตา หรืออาการน้ำตาไหล");
                    Log.d("TAGE","" +mydate);
                    Log.d("TAGE",""+im_picture[2]);

                    final String EMImpact = "จากการใช้งานสมาร์ทโฟนเป็นระยะเวลา " + hour + " ชั่วโมง " + minute + " นาที คุณอาจจะมีอาการปวดกระบอกตา เคืองตา หรืออาการน้ำตาไหล";
                    Intent intent = new Intent(StatsFragment.this.getActivity(), ImpactActivity.class);
                    intent.putExtra("Message", EMImpact);
                    intent.putExtra("Date", mydate);
                    intent.putExtra("Picture",im_picture[2]);
                    startActivity(intent);

                } else if ((hour >= 7 && (minute >= 30 && minute < 60)) && (hour < 8)) {
                    Log.d("TAGE", "จากการใช้งานสมาร์ทโฟนเป็นระยะเวลา " + hour + " ชั่วโมง " + minute + " นาที คุณอาจจะมีอาการปวดกระบอกตา เคืองตา หรืออาการน้ำตาไหล");
                    Log.d("TAGE","" +mydate);
                    Log.d("TAGE",""+im_picture[2]);

                    final String EMImpact = "จากการใช้งานสมาร์ทโฟนเป็นระยะเวลา " + hour + " ชั่วโมง " + minute + " นาที คุณอาจจะมีอาการปวดกระบอกตา เคืองตา หรืออาการน้ำตาไหล";
                    Intent intent = new Intent(StatsFragment.this.getActivity(), ImpactActivity.class);
                    intent.putExtra("Message", EMImpact);
                    intent.putExtra("Date", mydate);
                    intent.putExtra("Picture",im_picture[2]);
                    startActivity(intent);

                } else if ((hour >= 8 && minute < 30) && (hour < 9)) {
                    Log.d("TAGE", "จากการใช้งานสมาร์ทโฟนเป็นระยะเวลา " + hour + " ชั่วโมง " + minute + " นาที คุณอาจจะมีอาการปวดกระบอกตา เคืองตา หรืออาการน้ำตาไหล");
                    Log.d("TAGE","" +mydate);
                    Log.d("TAGE",""+im_picture[2]);

                    final String EMImpact = "จากการใช้งานสมาร์ทโฟนเป็นระยะเวลา " + hour + " ชั่วโมง " + minute + " นาที คุณอาจจะมีอาการปวดกระบอกตา เคืองตา หรืออาการน้ำตาไหล";
                    Intent intent = new Intent(StatsFragment.this.getActivity(), ImpactActivity.class);
                    intent.putExtra("Message", EMImpact);
                    intent.putExtra("Date", mydate);
                    intent.putExtra("Picture",im_picture[2]);
                    startActivity(intent);

                } else if ((hour > 8 && minute > 30) && hour < 9) {
                    Log.d("TAGE", "จากการใช้งานสมาร์ทโฟนเป็นระยะเวลา " + hour + " ชั่วโมง " + minute + " นาที คุณอาจจะมีอาการปวดกระบอกตา เคืองตา หรือการมองเห็นภาพซ้อน");
                    Log.d("TAGE","" +mydate);
                    Log.d("TAGE",""+im_picture[2]);

                    final String EMImpact = "จากการใช้งานสมาร์ทโฟนเป็นระยะเวลา " + hour + " ชั่วโมง " + minute + " นาที คุณอาจจะมีอาการปวดกระบอกตา เคืองตา หรือการมองเห็นภาพซ้อน";
                    Intent intent = new Intent(StatsFragment.this.getActivity(), ImpactActivity.class);
                    intent.putExtra("Message", EMImpact);
                    intent.putExtra("Date", mydate);
                    intent.putExtra("Picture",im_picture[2]);
                    startActivity(intent);

                } else if (hour > 9) {
                    Log.d("TAGE", "จากการใช้งานสมาร์ทโฟนเป็นระยะเวลา " + hour + " ชั่วโมง " + minute + " นาที สุขภาพสายตาของคุณมีความเสียงเป็นอย่างมาก ควรพักสายทุกครั้งหลังจากเป็นงานสมาร์ทโฟนติดต่อกันเป็นเวลา 30 นาที");
                    Log.d("TAGE","" +mydate);
                    Log.d("TAGE",""+im_picture[3]);

                    final String EMImpact = "จากการใช้งานสมาร์ทโฟนเป็นระยะเวลา " + hour + " ชั่วโมง " + minute + " นาที สุขภาพสายตาของคุณมีความเสียงเป็นอย่างมาก ควรพักสายทุกครั้งหลังจากเป็นงานสมาร์ทโฟนติดต่อกันเป็นเวลา 30 นาที";
                    Intent intent = new Intent(StatsFragment.this.getActivity(), ImpactActivity.class);
                    intent.putExtra("Message", EMImpact);
                    intent.putExtra("Date", mydate);
                    intent.putExtra("Picture",im_picture[3]);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshList();
    }

    @Override
    public void onStop() {
        super.onStop();
        stopLoadTask();
    }

    private void setTextMark(Period period) {
        switch (period) {
            case DAY:
                tvPeriod.setText(getResources().getString(R.string.stats_day));
                break;
            case YESTERDAY:
                tvPeriod.setText(getResources().getString(R.string.stats_yesterday));
                break;
            case WEEK:
                tvPeriod.setText(getResources().getString(R.string.stats_week));
                break;
            case YEAR:
                tvPeriod.setText(getResources().getString(R.string.stats_year));
                break;
        }
    }

    private void refreshList() {
        Log.d(Utils.LOG_TAG, "Stats - refreshList()");

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
                && !permissionGranted()) {
            return;
        }

        runLoadTask();
    }

    private void runLoadTask() {
        stopLoadTask();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        int period = prefs.getInt(Prefs.STATS_PERIOD, 1);

        loadTask = new LoadStatsTask();
        loadTask.execute(Period.fromInt(period));
    }

    private void stopLoadTask() {
        if (loadTask != null && loadTask.getStatus() == AsyncTask.Status.RUNNING) {
            loadTask.cancel(false);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private boolean permissionGranted() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        boolean granted = prefs.getBoolean(Prefs.STATS_PERMISSION_GRANTED, false);

        if (granted) {
            return true;
        }

        AppOpsManager appOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow("android:get_usage_stats",
                android.os.Process.myUid(), context.getPackageName());

        if (mode == AppOpsManager.MODE_ALLOWED) {
            prefs.edit().putBoolean(Prefs.STATS_PERMISSION_GRANTED, true).commit();
            return true;
        } else {
            openDialog();
            return false;
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void openDialog() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, context.getResources().getString(R.string.enable_usage_permission),
                        Toast.LENGTH_LONG).show();
            }
        });

        Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private class LoadStatsTask extends AsyncTask<Period, Void, List<StatEntry>> {

        StatsListAdapter adapter;
        Period period;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            listView.setAdapter(null);
            progressBar.setVisibility(View.VISIBLE);
        }

        //ตรงนี้จะไม่สามารถกำหนดค่าไปยัง textView ได้ เพราะมันยังทำงานอยู่ด้านหลัง จะเอาออกได้ต้องไปทำที่ onPostExecute
        protected List<StatEntry> doInBackground(Period... params) {
            period = params[0];
            statList = loader.loadStats(period);
            adapter = new StatsListAdapter(context, statList);
            return statList;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(List<StatEntry> result) {
            super.onPostExecute(result);

            //calculate total time for display screen
            int timeAll = 0;
            try {
                for (int i = 0; i < result.size(); i++) {
                    timeAll += result.get(i).getTime();

                }
                Log.e("TAG", "Total time :  " + timeAll);

                int hour = (timeAll / 3600);
                int minute = (timeAll / 60) - (hour * 60);
                Log.e("TAG", "Time : " + hour);
                Log.e("TAG", "Minute : " + minute);

                hour_time.setText("" + hour);
                minute_time.setText("" + minute);

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            progressBar.setVisibility(View.GONE);
            listView.setAdapter(adapter);
            setTextMark(period);
        }

    }
}
