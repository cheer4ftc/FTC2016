package com.wrgardnersoft.ftc2016.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;

import com.wrgardnersoft.ftc2016.models.Match;
import com.wrgardnersoft.ftc2016.models.MyApp;
import com.wrgardnersoft.ftc2016.interfaces.AsyncResponse;
import com.wrgardnersoft.ftc2016.R;
import com.wrgardnersoft.ftc2016.adapters.MatchesExpandableListAdapter;
import com.wrgardnersoft.ftc2016.internet.ClientTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MatchesActivity extends CommonMenuActivity implements AsyncResponse {


    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<Match>> listDataChild;
    ClientTask clientTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        MyApp myApp = (MyApp) getApplication();

        setTitle(" " + getString(R.string.matches));



        setContentView(R.layout.activity_matches);

        if (myApp.match[myApp.division()].size() > 0) {
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
            setTitle(" " + getString(R.string.matches) + ", Division " + Integer.toString(myApp.division() + 1)
                    + ": " + myApp.divisionName[myApp.division()]);
        }


        ArrayList<Match> localMatch = new ArrayList<>();

        for (Match m : myApp.match[myApp.division()]) {

            Match mm = new Match(m);
            //int teamsPerMatch = 2;
            if ((mm.score[MyApp.RED][MyApp.ScoreType.TOTAL.ordinal()] < 0) && (myApp.enableMatchPrediction)) {
                mm.predict();
            }
            localMatch.add(mm);

        }

        expListView = (ExpandableListView) findViewById(R.id.matches_expListView);
        //  Log.i("exp list view", expListView.toString());

        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();
        Match.prepareListData(localMatch, listDataHeader, listDataChild);
        MatchesExpandableListAdapter listAdapter = new MatchesExpandableListAdapter(this,
                listDataHeader, listDataChild);
        expListView.setAdapter(listAdapter);

        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View view, int groupPosition, int childPosition, long id) {
//Log.i("ELA Group", String.valueOf(groupPosition));
                Match matchPicked = (Match) parent.getExpandableListAdapter().getChild(groupPosition, childPosition);

                MyApp myApp = (MyApp) getApplication();
                myApp.currentMatchNumber = matchPicked.number;

                Intent getNameScreenIntent = new Intent(view.getContext(), MyMatchActivity.class);
                startActivity(getNameScreenIntent);
                return true;

            }

        });


    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setContentView(R.layout.activity_matches);

        MyApp myApp = (MyApp) getApplication();
        if (myApp.match[myApp.division()].size() > 0) {
            inflateMe();
        }
    }


    public void processFinish(int result) {
        //this you will received result fired from async class of onPostExecute(result) method.
        MyApp myApp = (MyApp) getApplication();

        if (myApp.match[myApp.division()].size() > 0) {
            inflateMe();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        super.onCreateOptionsMenu(menu);

       // MyApp myApp = MyApp.getInstance();

        MenuItem item = menu.findItem(R.id.action_matches);
        item.setVisible(false);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        boolean saveReturn;

        //MyApp myApp = MyApp.getInstance();

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            clientTask = new ClientTask(this);
            clientTask.delegate = this;
            clientTask.execute();
            return true;
        }
        saveReturn =  super.onOptionsItemSelected(item);

        if ((id == R.id.action_load)&&saveReturn) { // just loaded data, so refresh
            processFinish(0);
        }

        return saveReturn;
    }
}

