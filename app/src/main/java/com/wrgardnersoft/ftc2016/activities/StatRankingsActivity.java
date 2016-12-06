package com.wrgardnersoft.ftc2016.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.wrgardnersoft.ftc2016.interfaces.AsyncResponse;
import com.wrgardnersoft.ftc2016.models.MyApp;
import com.wrgardnersoft.ftc2016.models.Stat;
import com.wrgardnersoft.ftc2016.adapters.StatRankingsListAdapter;
import com.wrgardnersoft.ftc2016.models.TeamStatRanked;
import com.wrgardnersoft.ftc2016.R;
import com.wrgardnersoft.ftc2016.internet.ClientTask;

import java.util.Collections;
import java.util.Comparator;


public class StatRankingsActivity extends CommonMenuActivity implements AsyncResponse {

    private ListView listView;
    ClientTask clientTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        MyApp myApp = (MyApp) getApplication();

        //     team = new ArrayList<Team>();
        setTitle(" " + getString(R.string.stats));

        setContentView(R.layout.activity_stat_rankings);

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
            setTitle(" " + getString(R.string.stats) + ", Division " + Integer.toString(myApp.division() + 1)
                    + ": " + myApp.divisionName[myApp.division()]);
        }

        TextView tv = (TextView) findViewById(R.id.head_ccwm);
        tv.setOnLongClickListener(new View.OnLongClickListener() {
                                      @Override
                                      public boolean onLongClick(View v) {
                                          MyApp myApp = MyApp.getInstance();
                                          myApp.detailType = Stat.Type.CCWM;
                                          Intent getNameScreenIntent = new Intent(v.getContext(), StatDetailsActivity.class);
                                          startActivity(getNameScreenIntent);
                                          return true;
                                      }
                                  }


        );

        tv = (TextView) findViewById(R.id.head_opr);
        tv.setOnLongClickListener(new View.OnLongClickListener() {
                                      @Override
                                      public boolean onLongClick(View v) {
                                          MyApp myApp = MyApp.getInstance();
                                          myApp.detailType = Stat.Type.OPR;
                                          Intent getNameScreenIntent = new Intent(v.getContext(), StatDetailsActivity.class);
                                          startActivity(getNameScreenIntent);
                                          return true;
                                      }
                                  }


        );

        tv = (TextView) findViewById(R.id.head_dpr);
        tv.setOnLongClickListener(new View.OnLongClickListener() {
                                      @Override
                                      public boolean onLongClick(View v) {
                                          MyApp myApp = MyApp.getInstance();
                                          myApp.detailType = Stat.Type.DPR;
                                          Intent getNameScreenIntent = new Intent(v.getContext(), StatDetailsActivity.class);
                                          startActivity(getNameScreenIntent);
                                          return true;
                                      }
                                  }


        );

        StatRankingsListAdapter adapter = new StatRankingsListAdapter(this,
                R.layout.list_item_stat_ranking, myApp.teamStatRanked[myApp.division()]);
        listView = (ListView) findViewById(R.id.stat_rankings_list_view);
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
        setContentView(R.layout.activity_stat_rankings);

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

        listView = (ListView) findViewById(R.id.stat_rankings_list_view);
        listView.invalidateViews();
    }

    public void onClickFtcRankTextView(View view) {

        // save all setup info to globals in myApp class
        MyApp myApp = (MyApp) getApplication();

        Comparator<TeamStatRanked> ct = TeamStatRanked.getComparator(TeamStatRanked.SortParameter.FTCRANK_SORT);
        Collections.sort(myApp.teamStatRanked[myApp.division()], ct);

        listView = (ListView) findViewById(R.id.stat_rankings_list_view);
        listView.invalidateViews();
    }

    public void onClickWinPercentTextView(View view) {

        // save all setup info to globals in myApp class
        MyApp myApp = (MyApp) getApplication();

        Comparator<TeamStatRanked> ct =
                TeamStatRanked.getComparator(TeamStatRanked.SortParameter.WINPERCENT_SORT,
                        TeamStatRanked.SortParameter.OPR_SORT,
                        TeamStatRanked.SortParameter.CCWM_SORT,
                        TeamStatRanked.SortParameter.FTCRANK_SORT);
        Collections.sort(myApp.teamStatRanked[myApp.division()], ct);

        listView = (ListView) findViewById(R.id.stat_rankings_list_view);
        listView.invalidateViews();
    }

    public void onClickOprTextView(View view) {

        // save all setup info to globals in myApp class
        MyApp myApp = (MyApp) getApplication();

        Comparator<TeamStatRanked> ct = TeamStatRanked.getComparator(TeamStatRanked.SortParameter.OPR_SORT,
                TeamStatRanked.SortParameter.CCWM_SORT,
                TeamStatRanked.SortParameter.WINPERCENT_SORT,
                TeamStatRanked.SortParameter.FTCRANK_SORT);
        Collections.sort(myApp.teamStatRanked[myApp.division()], ct);

        listView = (ListView) findViewById(R.id.stat_rankings_list_view);
        listView.invalidateViews();
    }

    public void onLongClickOprTextView(View view) {

        // save all setup info to globals in myApp class
        MyApp myApp = (MyApp) getApplication();

        Comparator<TeamStatRanked> ct = TeamStatRanked.getComparator(TeamStatRanked.SortParameter.OPR_SORT,
                TeamStatRanked.SortParameter.CCWM_SORT,
                TeamStatRanked.SortParameter.WINPERCENT_SORT,
                TeamStatRanked.SortParameter.FTCRANK_SORT);
        Collections.sort(myApp.teamStatRanked[myApp.division()], ct);

        listView = (ListView) findViewById(R.id.stat_rankings_list_view);
        listView.invalidateViews();
    }

    public void onClickDprTextView(View view) {

        // save all setup info to globals in myApp class
        MyApp myApp = (MyApp) getApplication();

        Comparator<TeamStatRanked> ct = TeamStatRanked.getComparator(TeamStatRanked.SortParameter.DPR_SORT,
                TeamStatRanked.SortParameter.OPR_SORT,
                TeamStatRanked.SortParameter.WINPERCENT_SORT,
                TeamStatRanked.SortParameter.FTCRANK_SORT);
        Collections.sort(myApp.teamStatRanked[myApp.division()], ct);

        listView = (ListView) findViewById(R.id.stat_rankings_list_view);
        listView.invalidateViews();
    }

    public void onClickCcwmTextView(View view) {

        // save all setup info to globals in myApp class
        MyApp myApp = (MyApp) getApplication();

        Comparator<TeamStatRanked> ct = TeamStatRanked.getComparator(TeamStatRanked.SortParameter.CCWM_SORT,
                TeamStatRanked.SortParameter.OPR_SORT,
                TeamStatRanked.SortParameter.WINPERCENT_SORT,
                TeamStatRanked.SortParameter.FTCRANK_SORT);
        Collections.sort(myApp.teamStatRanked[myApp.division()], ct);

        listView = (ListView) findViewById(R.id.stat_rankings_list_view);
        listView.invalidateViews();
    }

    public void onClickNameTextView(View view) {

        // save all setup info to globals in myApp class
        MyApp myApp = (MyApp) getApplication();

        Comparator<TeamStatRanked> ct = TeamStatRanked.getComparator(TeamStatRanked.SortParameter.NAME_SORT,
                TeamStatRanked.SortParameter.NUMBER_SORT,
                TeamStatRanked.SortParameter.FTCRANK_SORT);
        Collections.sort(myApp.teamStatRanked[myApp.division()], ct);

        listView = (ListView) findViewById(R.id.stat_rankings_list_view);
        listView.invalidateViews();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        super.onCreateOptionsMenu(menu);

        MenuItem item = menu.findItem(R.id.action_stat_rankings);
        item.setVisible(false);

        item = menu.findItem(R.id.action_stat_info);
        item.setVisible(true);

        item = menu.findItem(R.id.action_forecast);
        item.setVisible(true);

        item = menu.findItem(R.id.action_test1);
        item.setVisible(true);

        item = menu.findItem(R.id.action_test2);
        item.setVisible(true);

        item = menu.findItem(R.id.action_test3);
        item.setVisible(true);

        item = menu.findItem(R.id.action_details);
        item.setVisible(true);

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
        }
        saveReturn = super.onOptionsItemSelected(item);

        if ((id == R.id.action_load) && saveReturn) { // just loaded data, so refresh
            processFinish(0);
        }

        return saveReturn;
    }
}
