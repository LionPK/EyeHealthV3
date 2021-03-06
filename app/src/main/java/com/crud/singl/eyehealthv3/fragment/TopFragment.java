package com.crud.singl.eyehealthv3.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.crud.singl.eyehealthv3.R;
import com.crud.singl.eyehealthv3.adapter.TopListAdapter;
import com.crud.singl.eyehealthv3.loader.DBStatProvider;
import com.crud.singl.eyehealthv3.loader.NativeStatProvider;
import com.crud.singl.eyehealthv3.loader.StatProvider;
import com.crud.singl.eyehealthv3.model.Period;
import com.crud.singl.eyehealthv3.model.StatEntry;

import java.util.List;

/**
 * @Copyright by Mr.Praneed Klanboon
 * Email: Praneed.kla@northbkk.ac.th
 * */
public class TopFragment extends Fragment {

    private static final String START_URL = "https://play.google.com/store/apps/details?id=";
    private static final int LIST_SIZE = 5;

    private ListView listView;
    private ProgressBar progressBar;

    private List<StatEntry> appList;
    private StatProvider loader;
    private Context context;
    private LoadTopTask loadTask;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_top, menu);

        MenuItem shareItem = menu.findItem(R.id.top_menu_share);
        CustomShareActionProvider shareActionProvider = (CustomShareActionProvider) MenuItemCompat.getActionProvider(shareItem);

        Intent intent = getDefaultShareIntent();
        if (intent != null) {
            shareActionProvider.setShareIntent(intent);
        }
    }

    private Intent getDefaultShareIntent() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Share apps");
        String text = getResources().getString(R.string.top_share_header);

        if (appList != null && appList.size() == 5) {
            for (int i = 0; i < 5; i++) {
                text += appList.get(i).getTitle() + ": " + START_URL + appList.get(i).getPackageName() + "\n";
            }
        }

        intent.putExtra(Intent.EXTRA_TEXT, text);
        return intent;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_top, container, false);
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

        listView = (ListView) getView().findViewById(R.id.top_list);
        progressBar = (ProgressBar) getView().findViewById(R.id.top_progress_bar);
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshTop();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        loadTask.cancel(false);
    }

    private void refreshTop() {
        loadTask = new LoadTopTask();
        loadTask.execute();
    }

    private class LoadTopTask extends AsyncTask<Void, Void, Void> {

        TopListAdapter adapter;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            listView.setAdapter(null);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            appList = loader.loadStatsWithLimit(Period.WEEK, 5);

            if (appList.size() < 5) {
                padList(appList);
            }

            adapter = new TopListAdapter(context, appList);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            progressBar.setVisibility(View.GONE);
            listView.setAdapter(adapter);
        }

        private void padList(List<StatEntry> appList) {
            int lack = LIST_SIZE - appList.size();

            for (int i = 0; i < lack; i++) {
                StatEntry se = new StatEntry();
                se.setIcon(getResources().getDrawable(R.drawable.sym_def_app_icon));
                se.setTitle("---");

                appList.add(se);
            }
        }
    }

}
