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
                    final String IntroHealth = "อาหารประเภทสารเคอร์ซิทิน เป็น “ตัวช่วยต้านการเสื่อมของจอประสาทตา และป้องกันการเกิดต้อหิน” พบมากในหัวหอมแดง สุขภาพสายตาที่ดี เป็นสิ่งที่หลายคนปรารถนา ฉะนั้นการดูแล และบำรุงดวงตา คุณเองก็สามารถทำได้ ด้วยการรับประทานอาหารให้ครบทั้ง 5 หมู่ และรับประทานอาหารที่ช่วยบำรุงสายตาอย่างสม่ำเสมอ พร้อมทั้งหลีกเลี่ยงปัจจัยที่จะส่งผลให้ดวงตาเสื่อมก่อนวัย เพียงเท่านี้คุณก็จะมีดวงตาสดใส สุขภาพดี สามารถใช้งานได้นานเท่านาน";
                    final String Date = mydate;
                    Intent intent = new Intent(StatsFragment.this.getActivity(), ImpactActivity.class);
                    intent.putExtra("Message", EMImpact);
                    intent.putExtra("Date", mydate);
                    intent.putExtra("Picture",im_picture[0]);
                    intent.putExtra("intorHealth",IntroHealth);
                    startActivity(intent);

                } else if (hour < 1 && (minute >= 30 && minute < 60)) {
                    Log.d("TAGE", "จากการใช้งานสมาร์ทโฟนเป็นระยะเวลา " + hour + " ชั่วโมง " + minute + " นาที คุณอาจจะมีอาการปวดกระบอกตา ตาพร่า หรือการมองเห็นภาพซ้อน");
                    Log.d("TAGE","" +mydate);
                    Log.d("TAGE",""+im_picture[1]);

                    final String EMImpact = "จากการใช้งานสมาร์ทโฟนเป็นระยะเวลา " + hour + " ชั่วโมง " + minute + " นาที คุณอาจจะมีอาการปวดกระบอกตา ตาพร่า หรือการมองเห็นภาพซ้อน";
                    final String IntroHealth = "อาการปวดกระบอกตา เกิดจากการใช้สายตาเป็นเวลานานๆ ในการจ้อง และเพ่งหน้าจอเกินกว่า 6 ชั่วโมงเป็นสาเหตุสาคัญที่ทำให้เกิดอาการปวดกระบอกตา วิธีแก้ไขทั่วไป: หลีกเลี่ยงการนอนเล่นสมาร์ทโฟนบนเตียง ที่สาคัญ หนุ่มสาวยุคดิจิทัล ไม่ควรติดแชท ติดเกมบนมือถือจนนอนหลับพักผ่อนไม่เพียงพอ ควรพักสายตาทุกๆ 15 – 20 นาที ปรับความสว่างหน้าจอให้เหมาะสม ไม่จ้าจนเกินไป";
                    Intent intent = new Intent(StatsFragment.this.getActivity(), ImpactActivity.class);
                    intent.putExtra("Message", EMImpact);
                    intent.putExtra("Date", mydate);
                    intent.putExtra("Picture",im_picture[1]);
                    intent.putExtra("intorHealth",IntroHealth);
                    startActivity(intent);

                } else if ((hour >= 1 && minute < 30) && (hour < 2)) {
                    Log.d("TAGE", "จากการใช้งานสมาร์ทโฟนเป็นระยะเวลา " + hour + " ชั่วโมง " + minute + " นาที คุณอาจจะมีอาการปวดกระบอกตา ตาพร่า หรือการเคืองตา");
                    Log.d("TAGE","" +mydate);
                    Log.d("TAGE",""+im_picture[1]);

                    final String EMImpact = "จากการใช้งานสมาร์ทโฟนเป็นระยะเวลา " + hour + " ชั่วโมง " + minute + " นาที คุณอาจจะมีอาการปวดกระบอกตา ตาพร่า หรือการเคืองตา";
                    final String IntroHealth = "อาการโรคตาแห้ง เป็นภาวะตาแห้งเป็นภาวะที่พบได้บ่อยและพบได้ในทุกเพศทุกวัย แม้ว่าจะดูเหมือนไม่ใช่อาการรุนแรง แต่การปล่อยทิ้งไว้ในระยะยาวโดยไม่ทาการรักษาจะทาให้อาการเคืองตารุนแรงยิ่งขึ้น จนอาจเกิดการอักเสบและการดึงรั้งของเปลือกตาทาให้ขนตาลงมาทิ่มตา ซึ่งหากเกิดการระคายเคืองจนกระจกตาเป็นแผลอาจต้องทาการผ่าตัดเพื่อแก้ไขในที่สุด สาเหตุ: ภาวะตาแห้งเกิดจากความเสื่อมของต่อมน้าตาไมโบเมียน (Meibomian Gland Dysfunction: MGD) ซึ่งเป็นต่อมที่อยู่ที่เปลือกตาทาหน้าที่สร้างน้าตามาหล่อลื่นดวงตา ความไม่สมดุลของฮอร์โมนในร่างกาย วิธีแก้ไขทั่วไป: การใช้น้าตาเทียมเพิ่มความชุ่มชื้นให้ดวงตาการใช้ยาหยอดตากลุ่มสเตียรอยด์เพื่อลดการอักเสบของผิวนัยน์ตาหรือผิวเยื่อบุตา และช่วยบรรเทาอาการคันระคายเคืองตา";
                    Intent intent = new Intent(StatsFragment.this.getActivity(), ImpactActivity.class);
                    intent.putExtra("Message", EMImpact);
                    intent.putExtra("Date", mydate);
                    intent.putExtra("Picture",im_picture[1]);
                    intent.putExtra("intorHealth",IntroHealth);
                    startActivity(intent);

                } else if ((hour >= 1 && (minute >= 30 && minute < 60)) && (hour < 2)) {
                    Log.d("TAGE", "จากการใช้งานสมาร์ทโฟนเป็นระยะเวลา " + hour + " ชั่วโมง " + minute + " นาที คุณอาจจะมีอาการปวดกระบอกตา ตาพร่า หรือการเคืองตา");
                    Log.d("TAGE","" +mydate);
                    Log.d("TAGE",""+im_picture[1]);

                    final String EMImpact = "จากการใช้งานสมาร์ทโฟนเป็นระยะเวลา " + hour + " ชั่วโมง " + minute + " นาที คุณอาจจะมีอาการปวดกระบอกตา ตาพร่า หรือการเคืองตา";
                    final String IntroHealth = "เคืองตา เป็นอาการหนึ่งของโรคตาแห้งซึ่งเป็นภาวะตาแห้งเป็นภาวะที่พบได้บ่อยและพบได้ในทุกเพศทุกวัย แม้ว่าจะดูเหมือนไม่ใช่อาการรุนแรง แต่การปล่อยทิ้งไว้ในระยะยาวโดยไม่ทาการรักษาจะทาให้อาการเคืองตารุนแรงยิ่งขึ้น จนอาจเกิดการอักเสบและการดึงรั้งของเปลือกตาทาให้ขนตาลงมาทิ่มตา ซึ่งหากเกิดการระคายเคืองจนกระจกตาเป็นแผลอาจต้องทาการผ่าตัดเพื่อแก้ไขในที่สุด สาเหตุ: ภาวะตาแห้งเกิดจากความเสื่อมของต่อมน้าตาไมโบเมียน (Meibomian Gland Dysfunction: MGD) ซึ่งเป็นต่อมที่อยู่ที่เปลือกตาทาหน้าที่สร้างน้าตามาหล่อลื่นดวงตา ความไม่สมดุลของฮอร์โมนในร่างกาย วิธีแก้ไขทั่วไป: การใช้น้าตาเทียมเพิ่มความชุ่มชื้นให้ดวงตาการใช้ยาหยอดตากลุ่มสเตียรอยด์เพื่อลดการอักเสบของผิวนัยน์ตาหรือผิวเยื่อบุตา และช่วยบรรเทาอาการคันระคายเคืองตา";
                    Intent intent = new Intent(StatsFragment.this.getActivity(), ImpactActivity.class);
                    intent.putExtra("Message", EMImpact);
                    intent.putExtra("Date", mydate);
                    intent.putExtra("Picture",im_picture[1]);
                    intent.putExtra("intorHealth",IntroHealth);
                    startActivity(intent);

                } else if ((hour >= 2 && minute < 30) && (hour < 3)) {
                    Log.d("TAGE", "จากการใช้งานสมาร์ทโฟนเป็นระยะเวลา " + hour + " ชั่วโมง " + minute + " นาที คุณอาจจะมีอาการปวดกระบอกตา ตาพร่า หรือการเคืองตา");
                    Log.d("TAGE","" +mydate);
                    Log.d("TAGE",""+im_picture[1]);

                    final String EMImpact = "จากการใช้งานสมาร์ทโฟนเป็นระยะเวลา " + hour + " ชั่วโมง " + minute + " นาที คุณอาจจะมีอาการปวดกระบอกตา ตาพร่า หรือการเคืองตา";
                    final String IntroHealth = "วิธีการบรรเทาอาการปวดตาให้หาย หนึ่งในอาการยอดฮิตในยุคสมัยนี้ที่ผู้คนต่างพากันจ้องคอม จ้องมือถือกันทั้งวัน ยิ่งเล่นเป็นเวลานานยิ่งทำให้ปวดตามากเท่านั้น ยิ่งบางคนชอบเล่นมือถือ หรือคอมพิวเตอร์ตอนปิดไฟ เพื่อนๆรู้หรือไม่ว่าการทำพฤติกรรมเหล่านั้นส่งผลเสียต่อดวงตาเป็นอย่างมาก นอกจากอาการปวดตาที่รู้สึกได้แล้วอาจมีอาการปวดหัวเข้ามาเกี่ยวข้องด้วย ซึ่งถ้าเพื่อนๆใช้ดวงตาเป็นเวลานานอาจก่อให้เกิดผลเสียอื่นๆตามมา วิธีแก่อาการปวดตานั้นสามารถทำได้ง่ายๆ ดังนี้ 1. พักสายตา วิธีการคือให้พักสายตาทุก 20 นาที โดยอาจมองไปนอกหน้าต่าง มองธรรมชาติประมาณ 20 นาที จะทำให้ดวงตาคุณผ่อนคลายมากยิ่งขึ้น 2. กรอกตาไปมา วิธีนี้จะช่วยลดอาการปวดตาได้ดีเลยทีเดียว โดยวิธีกรอกตาควรกรอกจากซ้ายไปขวา จากบนลงล่าง ทำประมาณ 5-10 ที อาการปวดตาจะค่อยๆหายไป 3. นวดตาเพื่อบรรเทา วิธีนี้เป็นวิธีที่สามารถทำได้ง่ายๆ คือ เราต้องหลับตาและใช้นิ้วชี้นวดไปที่บริเวณรอบดวงตาเบาๆ ประมาณ 5-10 นาที จะช่วยผ่อนคลายอาการเมื่อยล้าดวงตาของเราได้ 4. นอนหลับสักพัก วิธีที่แก้ได้ดีที่สุดคือการนอนหลับพักสายตา จะช่วยทำให้ดวงตาที่เหนื่อยล้าได้พักผ่อนอย่างเต็มที่ อาจใช้เวลา 15-30 นาทีก็จะช่วยให้ดวงตาของคุณหายจากอาการปวดตา 5. หยดน้ำตาเทียม สำหรับใครที่จ้องหน้าจอคอมหรือสมาร์ทโฟนนานๆอาจทำให้เกิดอาการตาแห้ง จนอาจทำให้เพื่อนๆรู้สึกปวดตาขึ้นมาได้ วิธีการแก้คือ หาน้ำตาเทียมติดกระเป๋าไว้ หากเมื่อไหร่ที่รู้สึกว่าตาของเราแห้งก็นำมาหยอดเพื่อคงความชุ่มชื้นของดวงตาเอาไว้นั่นเอง";
                    Intent intent = new Intent(StatsFragment.this.getActivity(), ImpactActivity.class);
                    intent.putExtra("Message", EMImpact);
                    intent.putExtra("Date", mydate);
                    intent.putExtra("Picture",im_picture[1]);
                    intent.putExtra("intorHealth",IntroHealth);
                    startActivity(intent);

                } else if ((hour >= 2 && (minute >= 30 && minute < 60)) && (hour < 3)) {
                    Log.d("TAGE", "จากการใช้งานสมาร์ทโฟนเป็นระยะเวลา " + hour + " ชั่วโมง " + minute + " นาที คุณอาจจะมีอาการปวดกระบอกตา ตาพร่า หรือการเคืองตา");
                    Log.d("TAGE","" +mydate);
                    Log.d("TAGE",""+im_picture[1]);

                    final String EMImpact = "จากการใช้งานสมาร์ทโฟนเป็นระยะเวลา " + hour + " ชั่วโมง " + minute + " นาที คุณอาจจะมีอาการปวดกระบอกตา ตาพร่า หรือการเคืองตา";
                    final String IntroHealth = "อาการปวดกระบอกตา เกิดจากการใช้สายตาเป็นเวลานานๆ ในการจ้อง และเพ่งหน้าจอเกินกว่า 6 ชั่วโมงเป็นสาเหตุสาคัญที่ทำให้เกิดอาการปวดกระบอกตา วิธีแก้ไขทั่วไป: หลีกเลี่ยงการนอนเล่นสมาร์ทโฟนบนเตียง ที่สาคัญ หนุ่มสาวยุคดิจิทัล ไม่ควรติดแชท ติดเกมบนมือถือจนนอนหลับพักผ่อนไม่เพียงพอ ควรพักสายตาทุกๆ 15 – 20 นาที ปรับความสว่างหน้าจอให้เหมาะสม ไม่จ้าจนเกินไป ";
                    Intent intent = new Intent(StatsFragment.this.getActivity(), ImpactActivity.class);
                    intent.putExtra("Message", EMImpact);
                    intent.putExtra("Date", mydate);
                    intent.putExtra("Picture",im_picture[1]);
                    intent.putExtra("intorHealth",IntroHealth);
                    startActivity(intent);

                } else if ((hour >= 3 && minute < 30) && (hour < 4)) {
                    Log.d("TAGE", "จากการใช้งานสมาร์ทโฟนเป็นระยะเวลา " + hour + " ชั่วโมง " + minute + " นาที คุณอาจจะมีอาการปวดกระบอกตา เคืองตา หรืออาการน้ำตาไหล");
                    Log.d("TAGE","" +mydate);
                    Log.d("TAGE",""+im_picture[1]);

                    final String EMImpact = "จากการใช้งานสมาร์ทโฟนเป็นระยะเวลา " + hour + " ชั่วโมง " + minute + " นาที คุณอาจจะมีอาการปวดกระบอกตา เคืองตา หรืออาการน้ำตาไหล";
                    final String IntroHealth = "อาการน้ำตาไหล เป็นสาเหตุหนึ่งจองโรคตาแห้ง ซึ่งเกิดจากภาวะตาแห้งเป็นภาวะที่พบได้บ่อยและพบได้ในทุกเพศทุกวัย แม้ว่าจะดูเหมือนไม่ใช่อาการรุนแรง แต่การปล่อยทิ้งไว้ในระยะยาวโดยไม่ทาการรักษาจะทาให้อาการเคืองตารุนแรงยิ่งขึ้น จนอาจเกิดการอักเสบและการดึงรั้งของเปลือกตาทาให้ขนตาลงมาทิ่มตา ซึ่งหากเกิดการระคายเคืองจนกระจกตาเป็นแผลอาจต้องทาการผ่าตัดเพื่อแก้ไขในที่สุด สาเหตุ: ภาวะตาแห้งเกิดจากความเสื่อมของต่อมน้าตาไมโบเมียน (Meibomian Gland Dysfunction: MGD) ซึ่งเป็นต่อมที่อยู่ที่เปลือกตาทาหน้าที่สร้างน้าตามาหล่อลื่นดวงตา ความไม่สมดุลของฮอร์โมนในร่างกาย วิธีแก้ไขทั่วไป: การใช้น้าตาเทียมเพิ่มความชุ่มชื้นให้ดวงตาการใช้ยาหยอดตากลุ่มสเตียรอยด์เพื่อลดการอักเสบของผิวนัยน์ตาหรือผิวเยื่อบุตา และช่วยบรรเทาอาการคันระคายเคืองตา";
                    Intent intent = new Intent(StatsFragment.this.getActivity(), ImpactActivity.class);
                    intent.putExtra("Message", EMImpact);
                    intent.putExtra("Date", mydate);
                    intent.putExtra("Picture",im_picture[1]);
                    intent.putExtra("intorHealth",IntroHealth);
                    startActivity(intent);

                } else if ((hour >= 3 && (minute >= 30 && minute < 60)) && (hour < 4)) {
                    Log.d("TAGE", "จากการใช้งานสมาร์ทโฟนเป็นระยะเวลา " + hour + " ชั่วโมง " + minute + " นาที คุณอาจจะมีอาการปวดกระบอกตา เคืองตา หรืออาการน้ำตาไหล");
                    Log.d("TAGE","" +mydate);
                    Log.d("TAGE",""+im_picture[1]);

                    final String EMImpact = "จากการใช้งานสมาร์ทโฟนเป็นระยะเวลา " + hour + " ชั่วโมง " + minute + " นาที คุณอาจจะมีอาการปวดกระบอกตา เคืองตา หรืออาการน้ำตาไหล";
                    final String IntroHealth = "อาการปวดกระบอกตา เกิดจากการใช้สายตาเป็นเวลานาน ๆ ในการจ้องและเพ่งหน้าจอเกินกว่า 6 ชั่วโมง เป็นสาเหตุสำคัญที่ทำให้เกิดอาการปวดกระบอกตา โดยจะมีอาการปวดบริเวณระหว่างหัวคิ้ว ไปจนถึงศีรษะ หากยังคงใช้สายตาจ้องมองต่อไป จะทำให้มีอาการรุนแรงมากขึ้น เช่น ตามัว วิงเวียน และมีอาการคลื่นไส้ อาเจียนได้ สัญญาณเตือนก่อนเป็นโรคมีอะไรบ้าง สัญญาณเตือนต่าง ๆ ของดวงตา อย่าคิดว่าเป็นเรื่องเล็กน้อย เพราะหากปล่อยทิ้งไว้อาจก่อให้เกิดอันตรายต่อดวงตาได้ในระยะยาว เสี่ยงต่อโรคดวงตาต่าง ๆ ไม่ว่าจะเป็น… จอประสาทตาเสื่อม ต้อกระจก ต้อหิน ซึ่งจะส่งผลให้ตาบอดเมื่อสูงอายุได้ ดังนั้น เราจึงควรหันมาให้ความใส่ใจดวงตาของเราตั้งแต่เนิ่นๆ เริ่มจากการปรับพฤติกรรมการใช้งานอุปกรณ์อิเล็กทรอนิกส์ โดยอย่าจ้องมองหน้าจออุปกรณ์เป็นเวลานานๆ ติดต่อกัน การปรับพฤติกรรมไม่ให้ปวดกระบอกตา ????หลีกเลี่ยงการนอนเล่นสมาร์ทโฟนบนเตียง ที่สำคัญ หนุ่มสาวยุคดิจิทัล ไม่ควรติดแชท ติดเกมบนมือถือจนนอนหลับพักผ่อนไม่เพียงพอ ควรพักสายตาทุกๆ 15 – 20 นาที • ปรับความสว่างหน้าจอให้เหมาะสม ไม่จ้าจนเกินไป อาจจะเลือกใช้ฟิล์มกรองแสงที่หน้าจอเพื่อลดแสงจ้าที่สะท้อนสู่ดวงตา • ไม่ควรใช้สมาร์ทโฟนในห้องนอนที่มืดหรือมีแสงสว่างไม่เพียงพอ ควรเว้นระยะห่างของหน้าจอและสายตาให้เหมาะสมโดยประมาณ 20-30 ซม. • ควรวางหน้าจอในมุมที่พอดีกับหน้า อย่าวางหน้าจอต่ำเกินไปหรือสูงเกินไป • หากพบความผิดปกติของดวงตา ควรรีบไปปรึกษาจักษุแพทย์โดยทันที การรักษาเบื้องต้น • กรณีที่ปวดตาจากกล้ามเนื้อตาล้า ควรจะพบจักษุแพทย์ก่อนเพื่อดูว่ามีปัญหาเกี่ยวกับสายตาหรือไม่ ถ้ามีก็ควรแก้ไข โดยใส่แว่นในขนาดที่ถูกต้อง • คนที่มีอายุ 40 ปี อาจจะต้องตัดแว่นดูใกล้ หรือแว่นสายตายาวเข้าช่วย • กรณีที่สายตาปกติดี หรือแก้ไขปัญหาสายตาแล้วยังปวดหัว หรือปวดตาอีก ควรฝึกการเพ่งกล้ามเนื้อ ซึ่งสามารถทำเองได้ง่าย ๆ โดยถือปากกาหรือดินสอให้ห่างสุดแขนแล้วเพ่งดูปลายปากกาหรือดินสอนั้น ค่อยๆ เอาปากกาหรือดินสอเข้าหาตัวช้าๆ ขณะเดียวกันก็เพ่งดูปลายปากกาหรือดินสอตลอดเวลา โดยต้องเห็นภาพปลายปากกาหรือดินสอนั้นชัดเจน และเป็นภาพเดียวกันตลอดเวลา ถ้าเป็น 2 ภาพ หรือเริ่มเห็นไม่ชัดเจนแสดงว่าตาเริ่มไม่รวมตัว หรือเพ่งไม่ได้แล้วต้องยืดแขนถอยออกไปจนกระทั่งเห็นภาพชัดใหม่ แล้วเริ่มเพ่งใหม่โดยค่อย ๆ เอาปากกาหรือดินสอเข้าหาตัวช้า ๆ ทำเช่นนี้ครั้งละ 10 – 15 หน วันละ 3 – 5 ครั้ง 1. ควรอ่านหนังสือ หรือทำงานที่ละเอียด หรือต้องใช้สายตาในที่ที่มีแสงสว่างเพียงพอ และควรมีการพักระหว่างทำงานบ้างเป็นช่วงๆ 2. สายตาผิดปกติ มักจะมีอาการปวดหัวไม่มาก แต่ในกรณีที่สายตา 2 ข้างไม่เท่ากัน หรือรายที่มีสายตาเอียงมากๆ อาจจะปวดหัว ปวดตาได้ การรักษาจำเป็นต้องตรวจเรื่องสายตาและแก้ไขด้วยการใช้แว่น";
                    Intent intent = new Intent(StatsFragment.this.getActivity(), ImpactActivity.class);
                    intent.putExtra("Message", EMImpact);
                    intent.putExtra("Date", mydate);
                    intent.putExtra("Picture",im_picture[1]);
                    intent.putExtra("intorHealth",IntroHealth);
                    startActivity(intent);

                } else if ((hour >= 4 && minute < 30) && (hour < 5)) {
                    Log.d("TAGE", "จากการใช้งานสมาร์ทโฟนเป็นระยะเวลา " + hour + " ชั่วโมง " + minute + " นาที คุณอาจจะมีอาการปวดกระบอกตา เคืองตา หรือการมองเห็นภาพซ้อน");
                    Log.d("TAGE","" +mydate);
                    Log.d("TAGE",""+im_picture[2]);

                    final String EMImpact = "จากการใช้งานสมาร์ทโฟนเป็นระยะเวลา " + hour + " ชั่วโมง " + minute + " นาที คุณอาจจะมีอาการปวดกระบอกตา เคืองตา หรือการมองเห็นภาพซ้อน";
                    final String IntroHealth = "อาการมองเห็นภาพซ้อน เกิดจากการมองเห็นภาพซ้อน (Diplopia) คือ การที่เรามองภาพข้างหน้าเพียงภาพเดียว แต่เรามองเห็นเป็น 2 ภาพ อย่างเช่น การมองวัตถุหนึ่งอันวางอยู่ แต่เห็นเหมือนกับมีสองอัน ผู้ที่เป็นอาจมีอาการปวดศรีษะ มึนงง สับสน รวมอยู่ด้วย การมองเห็นภาพซ้อน เป็นได้ทั้งจากการมองผ่านตาเดียว และการมองผ่านตาทั้งสองข้าง - การมองเห็นภาพซ้อนผ่านการมองจากตาข้างเดียว (Monocular Diplopia) มักเกิดจากการหักเหของแสงในดวงตาที่ผิดปกติ อาจมาจากอาการทางสายตาต่างๆ เช่น ภาวะสายตาเอียง ,ต้อกระจกระยะแรกๆ - การมองเห็นภาพซ้อนผ่านการมองจากตาทั้งสองข้าง (Binocular Diplopia) สาเหตุเกิดจาก กล้ามเนื้อที่ใช้ในการกลอกตาผิดปกติอาจเกิดจากการติดเชื้อที่เบ้าตา หรือ การได้รับอุบัติเหตุที่เบ้าตาหรือมีโรคประจำตัว เช่น เบาหวาน จนทำให้กล้ามเนื้อตามีปัญหา หรืออาจเกิดจากโรคทางสมอง เช่น เนื้องอกในสมอง หรือ หลอดเลือดสมองโปร่งพอง วิธีตรวจสอบว่ามองเห็นภาพซ้อนแบบใดสามารถทำได้โดยการ ปิดตาข้างหนึ่ง แล้วลองทำการมองถ้ายังเห็นภาพซ้อนอยู่แสดงว่าเป็น การมองเห็นภาพซ้อนผ่านการมองจากตาข้างเดียวแต่หากภาพซ้อนหายไป แสดงว่านี่คือ การมองเห็นภาพซ้อนผ่านการมองจากตาทั้งสองข้างอย่างไรก็ตามเพื่อความถูกต้อง หากเกิดอาการมองเห็นภาพซ้อนขึ้นมาควรไปพบจักษุแพทย์ เพื่อหาสาเหตุการเกิดและการรักษาที่ถูกต้อง";
                    Intent intent = new Intent(StatsFragment.this.getActivity(), ImpactActivity.class);
                    intent.putExtra("Message", EMImpact);
                    intent.putExtra("Date", mydate);
                    intent.putExtra("Picture",im_picture[2]);
                    intent.putExtra("intorHealth",IntroHealth);
                    startActivity(intent);

                } else if ((hour >= 4 && (minute >= 30 && minute < 60)) && (hour < 5)) {
                    Log.d("TAGE", "จากการใช้งานสมาร์ทโฟนเป็นระยะเวลา " + hour + " ชั่วโมง " + minute + " นาที คุณอาจจะมีอาการปวดกระบอกตา เคืองตา หรืออาการน้ำตาไหล");
                    Log.d("TAGE","" +mydate);
                    Log.d("TAGE",""+im_picture[2]);

                    final String EMImpact = "จากการใช้งานสมาร์ทโฟนเป็นระยะเวลา " + hour + " ชั่วโมง " + minute + " นาที คุณอาจจะมีอาการปวดกระบอกตา เคืองตา หรืออาการน้ำตาไหล";
                    final String IntroHealth = "อาการเคืองตาและน้ำตาไหล เป็นอาการหนึ่งของโรคาแห้งซึ่งเกิดจากภาวะตาแห้งเป็นภาวะที่พบได้บ่อยและพบได้ในทุกเพศทุกวัย แม้ว่าจะดูเหมือนไม่ใช่อาการรุนแรง แต่การปล่อยทิ้งไว้ในระยะยาวโดยไม่ทาการรักษาจะทาให้อาการเคืองตารุนแรงยิ่งขึ้น จนอาจเกิดการอักเสบและการดึงรั้งของเปลือกตาทาให้ขนตาลงมาทิ่มตา ซึ่งหากเกิดการระคายเคืองจนกระจกตาเป็นแผลอาจต้องทาการผ่าตัดเพื่อแก้ไขในที่สุด สาเหตุ: ภาวะตาแห้งเกิดจากความเสื่อมของต่อมน้าตาไมโบเมียน (Meibomian Gland Dysfunction: MGD) ซึ่งเป็นต่อมที่อยู่ที่เปลือกตาทาหน้าที่สร้างน้าตามาหล่อลื่นดวงตา ความไม่สมดุลของฮอร์โมนในร่างกาย วิธีแก้ไขทั่วไป: การใช้น้าตาเทียมเพิ่มความชุ่มชื้นให้ดวงตาการใช้ยาหยอดตากลุ่มสเตียรอยด์เพื่อลดการอักเสบของผิวนัยน์ตาหรือผิวเยื่อบุตา และช่วยบรรเทาอาการคันระคายเคืองตา";
                    Intent intent = new Intent(StatsFragment.this.getActivity(), ImpactActivity.class);
                    intent.putExtra("Message", EMImpact);
                    intent.putExtra("Date", mydate);
                    intent.putExtra("Picture",im_picture[2]);
                    intent.putExtra("intorHealth",IntroHealth);
                    startActivity(intent);

                } else if ((hour >= 5 && minute < 30) && (hour < 6)) {
                    Log.d("TAGE", "จากการใช้งานสมาร์ทโฟนเป็นระยะเวลา " + hour + " ชั่วโมง " + minute + " นาที คุณอาจจะมีอาการปวดกระบอกตา เคืองตา หรืออาการน้ำตาไหล");
                    Log.d("TAGE","" +mydate);
                    Log.d("TAGE",""+im_picture[2]);

                    final String EMImpact = "จากการใช้งานสมาร์ทโฟนเป็นระยะเวลา " + hour + " ชั่วโมง " + minute + " นาที คุณอาจจะมีอาการปวดกระบอกตา เคืองตา หรืออาการน้ำตาไหล";
                    final String IntroHealth = "อาการเคืองตาและน้ำตาไหล เป็นอาการหนึ่งของโรคาแห้งซึ่งเกิดจากภาวะตาแห้งเป็นภาวะที่พบได้บ่อยและพบได้ในทุกเพศทุกวัย แม้ว่าจะดูเหมือนไม่ใช่อาการรุนแรง แต่การปล่อยทิ้งไว้ในระยะยาวโดยไม่ทาการรักษาจะทาให้อาการเคืองตารุนแรงยิ่งขึ้น จนอาจเกิดการอักเสบและการดึงรั้งของเปลือกตาทาให้ขนตาลงมาทิ่มตา ซึ่งหากเกิดการระคายเคืองจนกระจกตาเป็นแผลอาจต้องทาการผ่าตัดเพื่อแก้ไขในที่สุด สาเหตุ: ภาวะตาแห้งเกิดจากความเสื่อมของต่อมน้าตาไมโบเมียน (Meibomian Gland Dysfunction: MGD) ซึ่งเป็นต่อมที่อยู่ที่เปลือกตาทาหน้าที่สร้างน้าตามาหล่อลื่นดวงตา ความไม่สมดุลของฮอร์โมนในร่างกาย วิธีแก้ไขทั่วไป: การใช้น้าตาเทียมเพิ่มความชุ่มชื้นให้ดวงตาการใช้ยาหยอดตากลุ่มสเตียรอยด์เพื่อลดการอักเสบของผิวนัยน์ตาหรือผิวเยื่อบุตา และช่วยบรรเทาอาการคันระคายเคืองตา";
                    Intent intent = new Intent(StatsFragment.this.getActivity(), ImpactActivity.class);
                    intent.putExtra("Message", EMImpact);
                    intent.putExtra("Date", mydate);
                    intent.putExtra("Picture",im_picture[2]);
                    intent.putExtra("intorHealth",IntroHealth);
                    startActivity(intent);

                } else if ((hour >= 5 && (minute >= 30 && minute < 60)) && (hour < 6)) {
                    Log.d("TAGE", "จากการใช้งานสมาร์ทโฟนเป็นระยะเวลา " + hour + " ชั่วโมง " + minute + " นาที  ุณอาจจะมีอาการปวดกระบอกตา เคืองตา หรืออาการน้ำตาไหล");
                    Log.d("TAGE","" +mydate);
                    Log.d("TAGE",""+im_picture[2]);

                    final String EMImpact = "จากการใช้งานสมาร์ทโฟนเป็นระยะเวลา " + hour + " ชั่วโมง " + minute + " นาที คุณอาจจะมีอาการปวดกระบอกตา เคืองตา หรืออาการน้ำตาไหล";
                    final String IntroHealth = "อาการเคืองตาและน้ำตาไหล เป็นอาการหนึ่งของโรคาแห้งซึ่งเกิดจากภาวะตาแห้งเป็นภาวะที่พบได้บ่อยและพบได้ในทุกเพศทุกวัย แม้ว่าจะดูเหมือนไม่ใช่อาการรุนแรง แต่การปล่อยทิ้งไว้ในระยะยาวโดยไม่ทาการรักษาจะทาให้อาการเคืองตารุนแรงยิ่งขึ้น จนอาจเกิดการอักเสบและการดึงรั้งของเปลือกตาทาให้ขนตาลงมาทิ่มตา ซึ่งหากเกิดการระคายเคืองจนกระจกตาเป็นแผลอาจต้องทาการผ่าตัดเพื่อแก้ไขในที่สุด สาเหตุ: ภาวะตาแห้งเกิดจากความเสื่อมของต่อมน้าตาไมโบเมียน (Meibomian Gland Dysfunction: MGD) ซึ่งเป็นต่อมที่อยู่ที่เปลือกตาทาหน้าที่สร้างน้าตามาหล่อลื่นดวงตา ความไม่สมดุลของฮอร์โมนในร่างกาย วิธีแก้ไขทั่วไป: การใช้น้าตาเทียมเพิ่มความชุ่มชื้นให้ดวงตาการใช้ยาหยอดตากลุ่มสเตียรอยด์เพื่อลดการอักเสบของผิวนัยน์ตาหรือผิวเยื่อบุตา และช่วยบรรเทาอาการคันระคายเคืองตา";
                    Intent intent = new Intent(StatsFragment.this.getActivity(), ImpactActivity.class);
                    intent.putExtra("Message", EMImpact);
                    intent.putExtra("Date", mydate);
                    intent.putExtra("Picture",im_picture[2]);
                    intent.putExtra("intorHealth",IntroHealth);
                    startActivity(intent);

                } else if ((hour >= 6 && minute < 30) && (hour < 7)) {
                    Log.d("TAGE", "จากการใช้งานสมาร์ทโฟนเป็นระยะเวลา 6 " + hour + " ชั่วโมง " + minute + " นาที คุณอาจจะมีอาการปวดกระบอกตา เคืองตา หรืออาการน้ำตาไหล");
                    Log.d("TAGE","" +mydate);
                    Log.d("TAGE",""+im_picture[2]);

                    final String EMImpact = "จากการใช้งานสมาร์ทโฟนเป็นระยะเวลา " + hour + " ชั่วโมง " + minute + " นาที คุณอาจจะมีอาการปวดกระบอกตา เคืองตา หรืออาการน้ำตาไหล";
                    final String IntroHealth = "อาการเคืองตาและน้ำตาไหล เป็นอาการหนึ่งของโรคาแห้งซึ่งเกิดจากภาวะตาแห้งเป็นภาวะที่พบได้บ่อยและพบได้ในทุกเพศทุกวัย แม้ว่าจะดูเหมือนไม่ใช่อาการรุนแรง แต่การปล่อยทิ้งไว้ในระยะยาวโดยไม่ทาการรักษาจะทาให้อาการเคืองตารุนแรงยิ่งขึ้น จนอาจเกิดการอักเสบและการดึงรั้งของเปลือกตาทาให้ขนตาลงมาทิ่มตา ซึ่งหากเกิดการระคายเคืองจนกระจกตาเป็นแผลอาจต้องทาการผ่าตัดเพื่อแก้ไขในที่สุด สาเหตุ: ภาวะตาแห้งเกิดจากความเสื่อมของต่อมน้าตาไมโบเมียน (Meibomian Gland Dysfunction: MGD) ซึ่งเป็นต่อมที่อยู่ที่เปลือกตาทาหน้าที่สร้างน้าตามาหล่อลื่นดวงตา ความไม่สมดุลของฮอร์โมนในร่างกาย วิธีแก้ไขทั่วไป: การใช้น้าตาเทียมเพิ่มความชุ่มชื้นให้ดวงตาการใช้ยาหยอดตากลุ่มสเตียรอยด์เพื่อลดการอักเสบของผิวนัยน์ตาหรือผิวเยื่อบุตา และช่วยบรรเทาอาการคันระคายเคืองตา";
                    Intent intent = new Intent(StatsFragment.this.getActivity(), ImpactActivity.class);
                    intent.putExtra("Message", EMImpact);
                    intent.putExtra("Date", mydate);
                    intent.putExtra("Picture",im_picture[2]);
                    intent.putExtra("intorHealth",IntroHealth);
                    startActivity(intent);

                } else if ((hour >= 6 && (minute >= 30 && minute < 60)) && (hour < 7)) {
                    Log.d("TAGE", "จากการใช้งานสมาร์ทโฟนเป็นระยะเวลา " + hour + " ชั่วโมง " + minute + " นาที คุณอาจจะมีอาการปวดกระบอกตา ตาพร่า หรืออาการน้ำตาไหล");
                    Log.d("TAGE","" +mydate);
                    Log.d("TAGE",""+im_picture[2]);

                    final String EMImpact = "จากการใช้งานสมาร์ทโฟนเป็นระยะเวลา " + hour + " ชั่วโมง " + minute + " นาที คุณอาจจะมีอาการปวดกระบอกตา ตาพร่า หรืออาการน้ำตาไหล";
                    final String IntroHealth = "อาการตาพร่าและน้ำตาไหล เป็นอาการหนึ่งของโรคาแห้งซึ่งเกิดจากภาวะตาแห้งเป็นภาวะที่พบได้บ่อยและพบได้ในทุกเพศทุกวัย แม้ว่าจะดูเหมือนไม่ใช่อาการรุนแรง แต่การปล่อยทิ้งไว้ในระยะยาวโดยไม่ทาการรักษาจะทาให้อาการเคืองตารุนแรงยิ่งขึ้น จนอาจเกิดการอักเสบและการดึงรั้งของเปลือกตาทาให้ขนตาลงมาทิ่มตา ซึ่งหากเกิดการระคายเคืองจนกระจกตาเป็นแผลอาจต้องทาการผ่าตัดเพื่อแก้ไขในที่สุด สาเหตุ: ภาวะตาแห้งเกิดจากความเสื่อมของต่อมน้าตาไมโบเมียน (Meibomian Gland Dysfunction: MGD) ซึ่งเป็นต่อมที่อยู่ที่เปลือกตาทาหน้าที่สร้างน้าตามาหล่อลื่นดวงตา ความไม่สมดุลของฮอร์โมนในร่างกาย วิธีแก้ไขทั่วไป: การใช้น้าตาเทียมเพิ่มความชุ่มชื้นให้ดวงตาการใช้ยาหยอดตากลุ่มสเตียรอยด์เพื่อลดการอักเสบของผิวนัยน์ตาหรือผิวเยื่อบุตา และช่วยบรรเทาอาการคันระคายเคืองตา";
                    Intent intent = new Intent(StatsFragment.this.getActivity(), ImpactActivity.class);
                    intent.putExtra("Message", EMImpact);
                    intent.putExtra("Date", mydate);
                    intent.putExtra("Picture",im_picture[2]);
                    intent.putExtra("intorHealth",IntroHealth);
                    startActivity(intent);

                } else if ((hour >= 7 && minute < 30) && (hour < 8)) {
                    Log.d("TAGE", "จากการใช้งานสมาร์ทโฟนเป็นระยะเวลา " + hour + " ชั่วโมง " + minute + " นาที คุณอาจจะมีอาการปวดกระบอกตา เคืองตา หรืออาการน้ำตาไหล");
                    Log.d("TAGE","" +mydate);
                    Log.d("TAGE",""+im_picture[2]);

                    final String EMImpact = "จากการใช้งานสมาร์ทโฟนเป็นระยะเวลา " + hour + " ชั่วโมง " + minute + " นาที คุณอาจจะมีอาการปวดกระบอกตา เคืองตา หรืออาการน้ำตาไหล";
                    final String IntroHealth = "อาการเคืองตาและน้ำตาไหล เป็นอาการหนึ่งของโรคาแห้งซึ่งเกิดจากภาวะตาแห้งเป็นภาวะที่พบได้บ่อยและพบได้ในทุกเพศทุกวัย แม้ว่าจะดูเหมือนไม่ใช่อาการรุนแรง แต่การปล่อยทิ้งไว้ในระยะยาวโดยไม่ทาการรักษาจะทาให้อาการเคืองตารุนแรงยิ่งขึ้น จนอาจเกิดการอักเสบและการดึงรั้งของเปลือกตาทาให้ขนตาลงมาทิ่มตา ซึ่งหากเกิดการระคายเคืองจนกระจกตาเป็นแผลอาจต้องทาการผ่าตัดเพื่อแก้ไขในที่สุด สาเหตุ: ภาวะตาแห้งเกิดจากความเสื่อมของต่อมน้าตาไมโบเมียน (Meibomian Gland Dysfunction: MGD) ซึ่งเป็นต่อมที่อยู่ที่เปลือกตาทาหน้าที่สร้างน้าตามาหล่อลื่นดวงตา ความไม่สมดุลของฮอร์โมนในร่างกาย วิธีแก้ไขทั่วไป: การใช้น้าตาเทียมเพิ่มความชุ่มชื้นให้ดวงตาการใช้ยาหยอดตากลุ่มสเตียรอยด์เพื่อลดการอักเสบของผิวนัยน์ตาหรือผิวเยื่อบุตา และช่วยบรรเทาอาการคันระคายเคืองตา";
                    Intent intent = new Intent(StatsFragment.this.getActivity(), ImpactActivity.class);
                    intent.putExtra("Message", EMImpact);
                    intent.putExtra("Date", mydate);
                    intent.putExtra("Picture",im_picture[2]);
                    intent.putExtra("intorHealth",IntroHealth);
                    startActivity(intent);

                } else if ((hour >= 7 && (minute >= 30 && minute < 60)) && (hour < 8)) {
                    Log.d("TAGE", "จากการใช้งานสมาร์ทโฟนเป็นระยะเวลา " + hour + " ชั่วโมง " + minute + " นาที คุณอาจจะมีอาการปวดกระบอกตา เคืองตา หรืออาการน้ำตาไหล");
                    Log.d("TAGE","" +mydate);
                    Log.d("TAGE",""+im_picture[2]);

                    final String EMImpact = "จากการใช้งานสมาร์ทโฟนเป็นระยะเวลา " + hour + " ชั่วโมง " + minute + " นาที คุณอาจจะมีอาการปวดกระบอกตา เคืองตา หรืออาการน้ำตาไหล";
                    final String IntroHealth = "อาการเคืองตาและน้ำตาไหล เป็นอาการหนึ่งของโรคาแห้งซึ่งเกิดจากภาวะตาแห้งเป็นภาวะที่พบได้บ่อยและพบได้ในทุกเพศทุกวัย แม้ว่าจะดูเหมือนไม่ใช่อาการรุนแรง แต่การปล่อยทิ้งไว้ในระยะยาวโดยไม่ทาการรักษาจะทาให้อาการเคืองตารุนแรงยิ่งขึ้น จนอาจเกิดการอักเสบและการดึงรั้งของเปลือกตาทาให้ขนตาลงมาทิ่มตา ซึ่งหากเกิดการระคายเคืองจนกระจกตาเป็นแผลอาจต้องทาการผ่าตัดเพื่อแก้ไขในที่สุด สาเหตุ: ภาวะตาแห้งเกิดจากความเสื่อมของต่อมน้าตาไมโบเมียน (Meibomian Gland Dysfunction: MGD) ซึ่งเป็นต่อมที่อยู่ที่เปลือกตาทาหน้าที่สร้างน้าตามาหล่อลื่นดวงตา ความไม่สมดุลของฮอร์โมนในร่างกาย วิธีแก้ไขทั่วไป: การใช้น้าตาเทียมเพิ่มความชุ่มชื้นให้ดวงตาการใช้ยาหยอดตากลุ่มสเตียรอยด์เพื่อลดการอักเสบของผิวนัยน์ตาหรือผิวเยื่อบุตา และช่วยบรรเทาอาการคันระคายเคืองตา";
                    Intent intent = new Intent(StatsFragment.this.getActivity(), ImpactActivity.class);
                    intent.putExtra("Message", EMImpact);
                    intent.putExtra("Date", mydate);
                    intent.putExtra("Picture",im_picture[2]);
                    intent.putExtra("intorHealth",IntroHealth);
                    startActivity(intent);

                } else if ((hour >= 8 && minute < 30) && (hour < 9)) {
                    Log.d("TAGE", "จากการใช้งานสมาร์ทโฟนเป็นระยะเวลา " + hour + " ชั่วโมง " + minute + " นาที คุณอาจจะมีอาการปวดกระบอกตา เคืองตา หรืออาการน้ำตาไหล");
                    Log.d("TAGE","" +mydate);
                    Log.d("TAGE",""+im_picture[2]);

                    final String EMImpact = "จากการใช้งานสมาร์ทโฟนเป็นระยะเวลา " + hour + " ชั่วโมง " + minute + " นาที คุณอาจจะมีอาการปวดกระบอกตา เคืองตา หรืออาการน้ำตาไหล";
                    final String IntroHealth = "วิธีการบรรเทาอาการปวดตาให้หาย 1. พักสายตา วิธีการคือให้พักสายตาทุก 20 นาที โดยอาจมองไปนอกหน้าต่าง มองธรรมชาติประมาณ 20 นาที จะทำให้ดวงตาคุณผ่อนคลายมากยิ่งขึ้น 2. กรอกตาไปมา วิธีนี้จะช่วยลดอาการปวดตาได้ดีเลยทีเดียว โดยวิธีกรอกตาควรกรอกจากซ้ายไปขวา จากบนลงล่าง ทำประมาณ 5-10 ที อาการปวดตาจะค่อยๆหายไป 3. นวดตาเพื่อบรรเทา วิธีนี้เป็นวิธีที่สามารถทำได้ง่ายๆ คือ เราต้องหลับตาและใช้นิ้วชี้นวดไปที่บริเวณรอบดวงตาเบาๆ ประมาณ 5-10 นาที จะช่วยผ่อนคลายอาการเมื่อยล้าดวงตาของเราได้ 4. นอนหลับสักพัก วิธีที่แก้ได้ดีที่สุดคือการนอนหลับพักสายตา จะช่วยทำให้ดวงตาที่เหนื่อยล้าได้พักผ่อนอย่างเต็มที่ อาจใช้เวลา 15-30 นาทีก็จะช่วยให้ดวงตาของคุณหายจากอาการปวดตา 5. หยดน้ำตาเทียม สำหรับใครที่จ้องหน้าจอคอมหรือสมาร์ทโฟนนานๆอาจทำให้เกิดอาการตาแห้ง จนอาจทำให้เพื่อนๆรู้สึกปวดตาขึ้นมาได้ วิธีการแก้คือ หาน้ำตาเทียมติดกระเป๋าไว้ หากเมื่อไหร่ที่รู้สึกว่าตาของเราแห้งก็นำมาหยอดเพื่อคงความชุ่มชื้นของดวงตาเอาไว้นั่นเอง";
                    Intent intent = new Intent(StatsFragment.this.getActivity(), ImpactActivity.class);
                    intent.putExtra("Message", EMImpact);
                    intent.putExtra("Date", mydate);
                    intent.putExtra("Picture",im_picture[2]);
                    intent.putExtra("intorHealth",IntroHealth);
                    startActivity(intent);

                } else if ((hour > 8 && minute > 30) && hour < 9) {
                    Log.d("TAGE", "จากการใช้งานสมาร์ทโฟนเป็นระยะเวลา " + hour + " ชั่วโมง " + minute + " นาที คุณอาจจะมีอาการปวดกระบอกตา เคืองตา หรือการมองเห็นภาพซ้อน");
                    Log.d("TAGE","" +mydate);
                    Log.d("TAGE",""+im_picture[2]);

                    final String EMImpact = "จากการใช้งานสมาร์ทโฟนเป็นระยะเวลา " + hour + " ชั่วโมง " + minute + " นาที คุณอาจจะมีอาการปวดกระบอกตา เคืองตา หรือการมองเห็นภาพซ้อน";
                    final String IntroHealth = "อาการเคืองตาและน้ำตาไหล เป็นอาการหนึ่งของโรคาแห้งซึ่งเกิดจากภาวะตาแห้งเป็นภาวะที่พบได้บ่อยและพบได้ในทุกเพศทุกวัย แม้ว่าจะดูเหมือนไม่ใช่อาการรุนแรง แต่การปล่อยทิ้งไว้ในระยะยาวโดยไม่ทาการรักษาจะทาให้อาการเคืองตารุนแรงยิ่งขึ้น จนอาจเกิดการอักเสบและการดึงรั้งของเปลือกตาทาให้ขนตาลงมาทิ่มตา ซึ่งหากเกิดการระคายเคืองจนกระจกตาเป็นแผลอาจต้องทาการผ่าตัดเพื่อแก้ไขในที่สุด สาเหตุ: ภาวะตาแห้งเกิดจากความเสื่อมของต่อมน้าตาไมโบเมียน (Meibomian Gland Dysfunction: MGD) ซึ่งเป็นต่อมที่อยู่ที่เปลือกตาทาหน้าที่สร้างน้าตามาหล่อลื่นดวงตา ความไม่สมดุลของฮอร์โมนในร่างกาย วิธีแก้ไขทั่วไป: การใช้น้าตาเทียมเพิ่มความชุ่มชื้นให้ดวงตาการใช้ยาหยอดตากลุ่มสเตียรอยด์เพื่อลดการอักเสบของผิวนัยน์ตาหรือผิวเยื่อบุตา และช่วยบรรเทาอาการคันระคายเคืองตา";
                    Intent intent = new Intent(StatsFragment.this.getActivity(), ImpactActivity.class);
                    intent.putExtra("Message", EMImpact);
                    intent.putExtra("Date", mydate);
                    intent.putExtra("Picture",im_picture[2]);
                    intent.putExtra("intorHealth",IntroHealth);
                    startActivity(intent);

                } else if (hour > 9) {
                    Log.d("TAGE", "จากการใช้งานสมาร์ทโฟนเป็นระยะเวลา " + hour + " ชั่วโมง " + minute + " นาที สุขภาพสายตาของคุณมีความเสียงเป็นอย่างมาก ควรพักสายทุกครั้งหลังจากเป็นงานสมาร์ทโฟนติดต่อกันเป็นเวลา 30 นาที");
                    Log.d("TAGE","" +mydate);
                    Log.d("TAGE",""+im_picture[3]);

                    final String EMImpact = "จากการใช้งานสมาร์ทโฟนเป็นระยะเวลา " + hour + " ชั่วโมง " + minute + " นาที สุขภาพสายตาของคุณมีความเสียงเป็นอย่างมาก ควรพักสายทุกครั้งหลังจากเป็นงานสมาร์ทโฟนติดต่อกันเป็นเวลา 30 นาที";
                    final String IntroHealth = "ประโยชน์ของวิตามินเอ วิตามินเอช่วยไม่ให้เยื่อบุตาแห้ง ลดการปวดกระบอกตา บารุงสายตาป้องกันอาการตาบอดตอนกลางคืน เพิ่มประสิทธิภาพในการมองเห็น เสริมสร้างเม็ดสีที่มีคุณสมบัติไวต่อแสงในตา ช่วยป้องกันโรคเกี่ยวกับจอตา วิตามินเอ แบบสารวจเราเรียกว่า เรตินอล พบได้ในอาหารที่มาจากสัตว์เท่านั้น ส่วนแคโรทิน พบในอาหารที่มาจากพืช และสัตว์ วิตามินเอพบมากในผักบุ้ง ตาลึง คะน้า พริกหวาน กระเจี๋ยบเขียว ฟักทอง แครอต บีตรู้ต แตงโม มะละกอสุก น้ามันตับป่า ตับ นม ไข่";
                    Intent intent = new Intent(StatsFragment.this.getActivity(), ImpactActivity.class);
                    intent.putExtra("Message", EMImpact);
                    intent.putExtra("Date", mydate);
                    intent.putExtra("Picture",im_picture[3]);
                    intent.putExtra("intorHealth",IntroHealth);
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
