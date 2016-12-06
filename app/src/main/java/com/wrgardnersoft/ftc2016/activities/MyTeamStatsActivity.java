package com.wrgardnersoft.ftc2016.activities;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.wrgardnersoft.ftc2016.interfaces.AsyncResponse;
import com.wrgardnersoft.ftc2016.R;
import com.wrgardnersoft.ftc2016.adapters.StatDetailsListAdapter;
import com.wrgardnersoft.ftc2016.internet.ClientTask;
import com.wrgardnersoft.ftc2016.models.MyApp;
import com.wrgardnersoft.ftc2016.models.Stat;
import com.wrgardnersoft.ftc2016.models.Team;
import com.wrgardnersoft.ftc2016.models.TeamStatRanked;

import java.util.ArrayList;


public class MyTeamStatsActivity extends CommonMenuActivity implements AsyncResponse {

    private ListView offLV, difLV, wmLV;
    ClientTask clientTask;

    public ArrayList<Team> myTeam;  // full team info from team server page
    public ArrayList<TeamStatRanked> myTeamStatRanked;

    public MyTeamStatsActivity() {
        this.myTeam = new ArrayList<>();
        this.myTeamStatRanked = new ArrayList<>();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        MyApp myApp = (MyApp) getApplication();

        //     team = new ArrayList<Team>();


        setTitle(" Team " + Integer.toString(myApp.currentTeamNumber) + " " + getString(R.string.detailedStats));


        setContentView(R.layout.activity_my_team_stats);

        if (myApp.team[myApp.division()].size() > 0) {
            inflateMeAll();
        } else {
            clientTask = new ClientTask(this);
            clientTask.delegate = this;
            clientTask.execute();
        }
    }

    public void processFinish(int result) {
        //this you will received result fired from async class of onPostExecute(result) method.
        MyApp myApp = (MyApp) getApplication();
        if (myApp.team[myApp.division()].size() > 0) {
            inflateMeAll();
        }

    }

    private void inflateMeAll() {
        MyApp myApp = MyApp.getInstance();

        TextView tv = (TextView) findViewById(R.id.head_number);
        tv.setText(getString(R.string.type));

        for (Team t : myApp.team[myApp.division()]) {
            if (t.number == myApp.currentTeamNumber) {
                myTeam.add(new Team(t));
            }
        }

        if (myApp.teamStatRanked[myApp.division()].size() > 0) {

            for (TeamStatRanked t : myApp.teamStatRanked[myApp.division()]) {
                if (t.number == myApp.currentTeamNumber) {
                    myTeamStatRanked.add(new TeamStatRanked(t));
                }
            }

            if (myTeamStatRanked.size() > 0) {
                myTeamStatRanked.get(0).number = -1;

                StatDetailsListAdapter adaptero = new StatDetailsListAdapter(this,
                        R.layout.list_item_stat_details, myTeamStatRanked, Stat.Type.OPR);
                offLV = (ListView) findViewById(R.id.stat_details_list_view_off);
                offLV.setAdapter(adaptero);

                StatDetailsListAdapter adapterd = new StatDetailsListAdapter(this,
                        R.layout.list_item_stat_details, myTeamStatRanked, Stat.Type.DPR);
                difLV = (ListView) findViewById(R.id.stat_details_list_view_dif);
                difLV.setAdapter(adapterd);

                StatDetailsListAdapter adapterw = new StatDetailsListAdapter(this,
                        R.layout.list_item_stat_details, myTeamStatRanked, Stat.Type.CCWM);
                wmLV = (ListView) findViewById(R.id.stat_details_list_view_wm);
                wmLV.setAdapter(adapterw);

            }
        }
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setContentView(R.layout.activity_my_team_stats);

        inflateMeAll();
    }

    public void onClickNumberTextView(View view) {
    }

    public void onClickAutoTextView(View view) {
    }

    public void onClickTeleopTextView(View view) {
    }

    public void onClickEndGameTextView(View view) {
    }

    public void onClickPenaltyTextView(View view) {
    }

    public void onClickTotalTextView(View view) {
    }

    public void onClickNameTextView(View view) {
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
        }
        saveReturn = super.onOptionsItemSelected(item);

        if ((id == R.id.action_load) && saveReturn) { // just loaded data, so refresh
            processFinish(0);
        }

        return saveReturn;
    }

}


