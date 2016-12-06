package com.wrgardnersoft.ftc2016.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.wrgardnersoft.ftc2016.R;
import com.wrgardnersoft.ftc2016.adapters.FtcRankingsListAdapter;
import com.wrgardnersoft.ftc2016.interfaces.AsyncResponse;
import com.wrgardnersoft.ftc2016.internet.ClientTask;
import com.wrgardnersoft.ftc2016.models.MyApp;
import com.wrgardnersoft.ftc2016.models.TeamFtcRanked;


public class FtcRankingsActivity extends CommonMenuActivity implements AsyncResponse {

  ClientTask clientTask;
  private ListView listView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);

    MyApp myApp = (MyApp) getApplication();

    setTitle(" " + getString(R.string.ftcRankings));


    setContentView(R.layout.activity_ftc_rankings);

    if (myApp.teamFtcRanked[myApp.division()].size() > 0) {
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
      setTitle(" " + getString(R.string.ftcRankings) + ", Division " + Integer.toString(myApp.division() + 1)
          + ": " + myApp.divisionName[myApp.division()]);
    }

    FtcRankingsListAdapter adapter = new FtcRankingsListAdapter(this,
        R.layout.list_item_ftc_ranking, myApp.teamFtcRanked[myApp.division()]);
    listView = (ListView) findViewById(R.id.ftc_rankings_list_view);
    listView.setAdapter(adapter);
    listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
      @Override
      public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        TeamFtcRanked teamPicked = (TeamFtcRanked) parent.getItemAtPosition(position);

        MyApp myApp = (MyApp) getApplication();

        if (myApp.selectedTeams.contains(teamPicked.number)) {
          myApp.selectedTeams.remove(Integer.valueOf(teamPicked.number));
        } else {
          myApp.selectedTeams.add(teamPicked.number);
        }
        listView.invalidateViews();
        return true;
      }
    });

    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TeamFtcRanked teamPicked = (TeamFtcRanked) parent.getItemAtPosition(position);

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
    setContentView(R.layout.activity_ftc_rankings);

    MyApp myApp = (MyApp) getApplication();
    if (myApp.teamFtcRanked[myApp.division()].size() > 0) {
      inflateMe();
    }
  }

  public void processFinish(int result) {
    //this you will received result fired from async class of onPostExecute(result) method.
    MyApp myApp = (MyApp) getApplication();
    if (myApp.teamFtcRanked[myApp.division()].size() > 0) {
      inflateMe();
    }

  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    super.onCreateOptionsMenu(menu);

    MenuItem item = menu.findItem(R.id.action_ftc_rankings);
    item.setVisible(false);

    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.

    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_refresh) {
      clientTask = new ClientTask(this);
      clientTask.delegate = this;
      clientTask.execute();
      return true;
    }
    return super.onOptionsItemSelected(item);
  }
}
