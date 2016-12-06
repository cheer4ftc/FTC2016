package com.wrgardnersoft.ftc2016.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.wrgardnersoft.ftc2016.models.MyApp;
import com.wrgardnersoft.ftc2016.models.Stat;
import com.wrgardnersoft.ftc2016.interfaces.AsyncResponse;
import com.wrgardnersoft.ftc2016.models.TeamStatRanked;
import com.wrgardnersoft.ftc2016.R;
import com.wrgardnersoft.ftc2016.adapters.StatDetailsListAdapter;
import com.wrgardnersoft.ftc2016.internet.ClientTask;

import java.util.Collections;
import java.util.Comparator;


public class StatDetailsActivity extends CommonMenuActivity implements AsyncResponse {

    private ListView listView;
    ClientTask clientTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        MyApp myApp = (MyApp) getApplication();

        setTitle(" " + getString(R.string.stats)+", "+ Stat.TypeDisplayString[myApp.detailType.ordinal()]);



        setContentView(R.layout.activity_stat_details);

        if (myApp.team[myApp.division()].size() > 0) {
            inflateMe();
        } else {
            clientTask = new ClientTask(this);
            clientTask.delegate = this;
            clientTask.execute();
        }
    }

    private void inflateMe() {
        MyApp myApp = MyApp.getInstance();
        if (myApp.dualDivision()) {
            setTitle(" " + getString(R.string.stats)+", "+Stat.TypeDisplayString[myApp.detailType.ordinal()] + ", Division " + Integer.toString(myApp.division() + 1)
                    + ": " + myApp.divisionName[myApp.division()]);
        }

        StatDetailsListAdapter adapter = new StatDetailsListAdapter(this,
                R.layout.list_item_stat_details, myApp.teamStatRanked[myApp.division()], myApp.detailType);
        listView = (ListView) findViewById(R.id.stat_details_list_view);
        listView.setAdapter(adapter);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                TeamStatRanked teamPicked = (TeamStatRanked) parent.getItemAtPosition(position);

                MyApp myApp = MyApp.getInstance();

                myApp.currentTeamNumber = teamPicked.number;
                Intent getNameScreenIntent = new Intent(view.getContext(), MyTeamStatsActivity.class);
                startActivity(getNameScreenIntent);
                return true;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TeamStatRanked teamPicked = (TeamStatRanked) parent.getItemAtPosition(position);

                MyApp myApp = (MyApp) getApplication();
                myApp.currentTeamNumber = teamPicked.number;

                Intent getNameScreenIntent = new Intent(view.getContext(), MyTeamActivity.class);
                startActivity(getNameScreenIntent);
            }
        });

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setContentView(R.layout.activity_stat_details);

        MyApp myApp = (MyApp) getApplication();
        if (myApp.teamStatRanked[myApp.division()].size() > 0) {
            inflateMe();
        }

    }

    public void processFinish(int result) {
        //this you will received result fired from async class of onPostExecute(result) method.
        MyApp myApp = (MyApp) getApplication();
        if (myApp.team[myApp.division()].size() > 0) {
            inflateMe();
        }

    }

    public void onClickNumberTextView(View view) {

        // save all setup info to globals in myApp class
        MyApp myApp = (MyApp) getApplication();

        Comparator<TeamStatRanked> ct = TeamStatRanked.getComparator(TeamStatRanked.SortParameter.NUMBER_SORT);
        Collections.sort(myApp.teamStatRanked[myApp.division()], ct);

        listView = (ListView) findViewById(R.id.stat_details_list_view);
        listView.invalidateViews();
    }

    public void onClickAutoTextView(View view) {

        // save all setup info to globals in myApp class
        MyApp myApp = (MyApp) getApplication();

        Comparator<TeamStatRanked> ct;
        if (myApp.detailType == Stat.Type.OPR) {
            ct = TeamStatRanked.getComparator(TeamStatRanked.SortParameter.OFF_AUTO_SORT);
        } else if (myApp.detailType == Stat.Type.DPR) {
            ct = TeamStatRanked.getComparator(TeamStatRanked.SortParameter.DEF_AUTO_SORT);
        } else {
            ct = TeamStatRanked.getComparator(TeamStatRanked.SortParameter.CMB_AUTO_SORT);
        }
        Collections.sort(myApp.teamStatRanked[myApp.division()], ct);

        listView = (ListView) findViewById(R.id.stat_details_list_view);
        listView.invalidateViews();
    }

    public void onClickTeleopTextView(View view) {

        // save all setup info to globals in myApp class
        MyApp myApp = (MyApp) getApplication();

        Comparator<TeamStatRanked> ct;
        if (myApp.detailType == Stat.Type.OPR) {
            ct = TeamStatRanked.getComparator(TeamStatRanked.SortParameter.OFF_TELE_SORT);
        } else if (myApp.detailType == Stat.Type.DPR) {
            ct = TeamStatRanked.getComparator(TeamStatRanked.SortParameter.DEF_TELE_SORT);
        } else {
            ct = TeamStatRanked.getComparator(TeamStatRanked.SortParameter.CMB_TELE_SORT);
        }
        Collections.sort(myApp.teamStatRanked[myApp.division()], ct);

        listView = (ListView) findViewById(R.id.stat_details_list_view);
        listView.invalidateViews();
    }

    public void onClickEndGameTextView(View view) {

        // save all setup info to globals in myApp class
        MyApp myApp = (MyApp) getApplication();

        Comparator<TeamStatRanked> ct;
        if (myApp.detailType == Stat.Type.OPR) {
            ct = TeamStatRanked.getComparator(TeamStatRanked.SortParameter.OFF_ENDG_SORT);
        } else if (myApp.detailType == Stat.Type.DPR) {
            ct = TeamStatRanked.getComparator(TeamStatRanked.SortParameter.DEF_ENDG_SORT);
        } else {
            ct = TeamStatRanked.getComparator(TeamStatRanked.SortParameter.CMB_ENDG_SORT);
        }
        Collections.sort(myApp.teamStatRanked[myApp.division()], ct);

        listView = (ListView) findViewById(R.id.stat_details_list_view);
        listView.invalidateViews();
    }

    public void onClickPenaltyTextView(View view) {

        // save all setup info to globals in myApp class
        MyApp myApp = (MyApp) getApplication();

        Comparator<TeamStatRanked> ct;
        if (myApp.detailType == Stat.Type.OPR) {
            ct = TeamStatRanked.getComparator(TeamStatRanked.SortParameter.OFF_PEN_SORT);
        } else if (myApp.detailType == Stat.Type.DPR) {
            ct = TeamStatRanked.getComparator(TeamStatRanked.SortParameter.DEF_PEN_SORT);
        } else {
            ct = TeamStatRanked.getComparator(TeamStatRanked.SortParameter.CMB_PEN_SORT);
        }
        Collections.sort(myApp.teamStatRanked[myApp.division()], ct);

        listView = (ListView) findViewById(R.id.stat_details_list_view);
        listView.invalidateViews();
    }

    public void onClickTotalTextView(View view) {

        // save all setup info to globals in myApp class
        MyApp myApp = (MyApp) getApplication();

        Comparator<TeamStatRanked> ct;
        if (myApp.detailType == Stat.Type.OPR) {
            ct = TeamStatRanked.getComparator(TeamStatRanked.SortParameter.OPR_SORT);
        } else if (myApp.detailType == Stat.Type.DPR) {
            ct = TeamStatRanked.getComparator(TeamStatRanked.SortParameter.DPR_SORT);
        } else {
            ct = TeamStatRanked.getComparator(TeamStatRanked.SortParameter.CCWM_SORT);
        }
        Collections.sort(myApp.teamStatRanked[myApp.division()], ct);

        listView = (ListView) findViewById(R.id.stat_details_list_view);
        listView.invalidateViews();
    }


    public void onClickNameTextView(View view) {

        // save all setup info to globals in myApp class
        MyApp myApp = (MyApp) getApplication();

        Comparator<TeamStatRanked> ct = TeamStatRanked.getComparator(TeamStatRanked.SortParameter.NAME_SORT,
                TeamStatRanked.SortParameter.NUMBER_SORT,
                TeamStatRanked.SortParameter.FTCRANK_SORT);
        Collections.sort(myApp.teamStatRanked[myApp.division()], ct);

        listView = (ListView) findViewById(R.id.stat_details_list_view);
        listView.invalidateViews();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        super.onCreateOptionsMenu(menu);

        MyApp myApp = MyApp.getInstance();
        MenuItem item = menu.findItem(R.id.action_stat_rankings);
        item.setVisible(false);

        item = menu.findItem(R.id.action_stat_info);
        item.setVisible(true);

        item = menu.findItem(R.id.action_toggle_forecast);
        item.setVisible(true);

        item = menu.findItem(R.id.action_test1);
        item.setVisible(true);

        item = menu.findItem(R.id.action_test2);
        item.setVisible(true);

        item = menu.findItem(R.id.action_details);
        item.setVisible(true);

        item = menu.findItem(R.id.action_change_stat_normalization);
        item.setVisible(true);

        if (myApp.enableMatchPrediction) {
            menu.findItem(R.id.action_toggle_forecast).setTitle("Disable Forecast");
        } else {
            menu.findItem(R.id.action_toggle_forecast).setTitle("Enable Forecast");
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        boolean saveReturn;

        // MyApp myApp = MyApp.getInstance();

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            clientTask = new ClientTask(this);
            clientTask.delegate = this;
            clientTask.execute();
            return true;
        } else if (id == R.id.action_details_off) {
            MyApp myApp = MyApp.getInstance();
            myApp.detailType = Stat.Type.OPR;
            Intent getNameScreenIntent = new Intent(this, StatDetailsActivity.class);
            startActivity(getNameScreenIntent);
        } else if (id == R.id.action_details_def) {
            MyApp myApp = MyApp.getInstance();
            myApp.detailType = Stat.Type.DPR;
            Intent getNameScreenIntent = new Intent(this, StatDetailsActivity.class);
            startActivity(getNameScreenIntent);
        } else if (id == R.id.action_details_cmb) {
            MyApp myApp = MyApp.getInstance();
            myApp.detailType = Stat.Type.CCWM;
            Intent getNameScreenIntent = new Intent(this, StatDetailsActivity.class);
            startActivity(getNameScreenIntent);
        } else if (id == R.id.action_stat_mean_true) {
            MyApp myApp = MyApp.getInstance();
            myApp.useTrueMean = true;
            Intent getNameScreenIntent = new Intent(this, StatDetailsActivity.class);
            startActivity(getNameScreenIntent);
        } else if (id == R.id.action_stat_mean_zero) {
            MyApp myApp = MyApp.getInstance();
            myApp.useTrueMean = false;
            //Log.i("Stat mean now", "false");
            Intent getNameScreenIntent = new Intent(this, StatDetailsActivity.class);
            startActivity(getNameScreenIntent);
        }
        saveReturn = super.onOptionsItemSelected(item);

        if ((id == R.id.action_load) && saveReturn) { // just loaded data, so refresh
            processFinish(0);
        }

        return saveReturn;
    }
}
