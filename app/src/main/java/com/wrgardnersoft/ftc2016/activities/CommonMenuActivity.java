package com.wrgardnersoft.ftc2016.activities;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.wrgardnersoft.ftc2016.interfaces.AsyncResponse;
import com.wrgardnersoft.ftc2016.models.Match;
import com.wrgardnersoft.ftc2016.models.MyApp;
import com.wrgardnersoft.ftc2016.models.Stat;
import com.wrgardnersoft.ftc2016.models.Team;
import com.wrgardnersoft.ftc2016.models.TeamFtcRanked;
import com.wrgardnersoft.ftc2016.R;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Created by Bill on 2/10/2015.
 */
public class CommonMenuActivity extends ActionBarActivity implements AsyncResponse {



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_launcher);

        loadServerInfo();

    }

    void loadServerInfo() {
        MyApp myApp = (MyApp) getApplication();
        String setupFileName = "FtcOnlineSetupInfo";

        try {
            FileInputStream fi = openFileInput(setupFileName);
            InputStreamReader fr = new InputStreamReader(fi);
            BufferedReader br = new BufferedReader(fr);

            myApp.setServerAddressString(0, String.valueOf(br.readLine()));
            myApp.setServerAddressString(1, String.valueOf(br.readLine()));
            myApp.setServerAddressString(2, String.valueOf(br.readLine()));

            fr.close();
        } catch (IOException e) {
            //           Log.i("Setup Activity", "Exception reading setup data");
        }
    }

    @Override
    protected void onResume() {

        super.onResume();

//        Log.i("onResume", "called");

    }

    @Override
    protected void onStart() {

        super.onStart();

//        Log.i("onStart", "called");

    }

    @Override
    protected void onRestart() {

        super.onRestart();

        loadServerInfo();

//        Log.i("onRestart","called");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MyApp myApp = MyApp.getInstance();
        if (!myApp.dualDivision()) {
            MenuItem item = menu.findItem(R.id.action_change_division);
            item.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        MyApp myApp = MyApp.getInstance();
        String tournamentDataFileName = "SavedTournamentData";
        AssetManager assetManager = getAssets();

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_setup) {
            Intent getNameScreenIntent = new Intent(this, SetupActivity.class);
            startActivity(getNameScreenIntent);
            return true;
        } else if (id == R.id.action_clear_all) {
            for (int d = 0; d < 2; d++) {
                myApp.team[d].clear();
                myApp.teamFtcRanked[d].clear();
                myApp.match[d].clear();
                myApp.teamStatRanked[d].clear();
            }
            try {
                this.deleteFile(this.getString(R.string.lastReceivedData));
            } catch (Exception e) {
                //     Log.i("No file to delete", "OK");
            }

            Intent getNameScreenIntent = new Intent(this, FtcRankingsActivity.class);
            getNameScreenIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(getNameScreenIntent);
            finish();
            return true;
        } else if (id == R.id.action_ftc_rankings) {
            Intent getNameScreenIntent = new Intent(this, FtcRankingsActivity.class);
            startActivity(getNameScreenIntent);
            return true;
        } else if (id == R.id.action_matches) {
            Intent getNameScreenIntent = new Intent(this, MatchesActivity.class);
            startActivity(getNameScreenIntent);
            return true;
        } else if (id == R.id.action_stat_rankings) {
            Intent getNameScreenIntent = new Intent(this, StatRankingsActivity.class);
            startActivity(getNameScreenIntent);
            return true;
        } else if (id == R.id.action_teams) {
            Intent getNameScreenIntent = new Intent(this, TeamsActivity.class);
            startActivity(getNameScreenIntent);
            return true;
        } else if (id == R.id.action_stat_info) {
            Intent getNameScreenIntent = new Intent(this, StatInfoActivity.class);
            startActivity(getNameScreenIntent);
            return true;
        } else if (id == R.id.action_forecast_disable) {
            myApp.enableMatchPrediction = false;
            item.setChecked(myApp.enableMatchPrediction);
            Intent getNameScreenIntent = new Intent(this, FtcRankingsActivity.class);
            getNameScreenIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(getNameScreenIntent);
            finish();
            return true;
        } else if (id == R.id.action_forecast_off) {
            myApp.enableMatchPrediction = true;
            myApp.predictionType= Stat.Type.OPR;
            item.setChecked(myApp.enableMatchPrediction);
            Intent getNameScreenIntent = new Intent(this, FtcRankingsActivity.class);
            getNameScreenIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(getNameScreenIntent);
            finish();
            return true;
        } else if (id == R.id.action_forecast_wm) {
            myApp.enableMatchPrediction = true;
            myApp.predictionType= Stat.Type.CCWM;
            item.setChecked(myApp.enableMatchPrediction);
            Intent getNameScreenIntent = new Intent(this, FtcRankingsActivity.class);
            getNameScreenIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(getNameScreenIntent);
            finish();
            return true;
        } else if (id == R.id.action_save) {

            try {
                this.deleteFile(tournamentDataFileName);
            } catch (Exception e) {
                //     Log.i("No file to delete", "OK");
            }
            try {
                FileOutputStream fOut = openFileOutput(tournamentDataFileName, MODE_PRIVATE);
                OutputStreamWriter fw = new OutputStreamWriter(fOut);

                MyApp.saveTournamentData(fw);

                fw.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;

        } else if (id == R.id.action_load) {

            try {
                FileInputStream fi = openFileInput(tournamentDataFileName);
                InputStreamReader fr = new InputStreamReader(fi);

                BufferedReader br = new BufferedReader(fr);

                MyApp.loadTournamentData(br);

                fr.close();
            } catch (Exception e) {
                try {
                    InputStream iS = assetManager.open(tournamentDataFileName);
                    InputStreamReader fr = new InputStreamReader(iS);

                    BufferedReader br = new BufferedReader(fr);

                    MyApp.loadTournamentData(br);

                    fr.close();

                    CharSequence text = "Can't load your saved data.\nLoading sample tournament.";
                    int duration = Toast.LENGTH_LONG;
                    Toast.makeText(this, text, duration).show();
                } catch (Exception e2) {
                    CharSequence text = "Error: Can't load any tournament data!!!";
                    int duration = Toast.LENGTH_LONG;
                    Toast.makeText(this, text, duration).show();
                }
            }
            Intent getNameScreenIntent = new Intent(this, FtcRankingsActivity.class);
            getNameScreenIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(getNameScreenIntent);
            finish();
            return true;
        } else if (id == R.id.action_test1) {

            try {
                InputStream iS = assetManager.open("TestPaPartWatchFtcFmt.csv");
                InputStreamReader fr = new InputStreamReader(iS);

                BufferedReader br = new BufferedReader(fr);

                MyApp.loadTournamentData(br);

                fr.close();

                CharSequence text = "Loading test1 tournament.";
                int duration = Toast.LENGTH_LONG;
                Toast.makeText(this, text, duration).show();
            } catch (Exception e2) {
                CharSequence text = "Error: Can't load tournament data!!!";
                int duration = Toast.LENGTH_LONG;
                Toast.makeText(this, text, duration).show();
            }
            Intent getNameScreenIntent = new Intent(this, FtcRankingsActivity.class);
            getNameScreenIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(getNameScreenIntent);
            finish();
        } else if (id == R.id.action_test2) {

            try {
                InputStream iS = assetManager.open("TestPaFullWatchFtcFmt.csv");
                InputStreamReader fr = new InputStreamReader(iS);

                BufferedReader br = new BufferedReader(fr);

                MyApp.loadTournamentData(br);

                fr.close();

                CharSequence text = "Loading test2 tournament.";
                int duration = Toast.LENGTH_LONG;
                Toast.makeText(this, text, duration).show();
            } catch (Exception e2) {
                CharSequence text = "Error: Can't load tournament data!!!";
                int duration = Toast.LENGTH_LONG;
                Toast.makeText(this, text, duration).show();
            }
            Intent getNameScreenIntent = new Intent(this, FtcRankingsActivity.class);
            getNameScreenIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(getNameScreenIntent);
            finish();
        } else if (id == R.id.action_test3) {

            try {
                InputStream iS = assetManager.open("TestVa2015Kamen.csv");
                InputStreamReader fr = new InputStreamReader(iS);

                BufferedReader br = new BufferedReader(fr);

                MyApp.loadTournamentData(br);

                fr.close();

                CharSequence text = "Loading test3 tournament.";
                int duration = Toast.LENGTH_LONG;
                Toast.makeText(this, text, duration).show();
            } catch (Exception e2) {
                CharSequence text = "Error: Can't load tournament data!!!";
                int duration = Toast.LENGTH_LONG;
                Toast.makeText(this, text, duration).show();
            }
            Intent getNameScreenIntent = new Intent(this, FtcRankingsActivity.class);
            getNameScreenIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(getNameScreenIntent);
            finish();
        } else if (id == R.id.action_share) {
            String shareOutput = "";
            if (myApp.team[0].size() > 0) {
                shareOutput = shareOutput + "Division,0";
                if (!myApp.divisionName[0].contentEquals("")) {
                    shareOutput = shareOutput + "," + myApp.divisionName[0];
                }
                shareOutput = shareOutput + System.getProperty("line.separator");
                shareOutput = shareOutput + "Teams," + myApp.team[0].size() + System.getProperty("line.separator");
                shareOutput = shareOutput + Team.shareHeader();
                for (Team t : myApp.team[0]) {
                    shareOutput = shareOutput + t;
                }
                shareOutput = shareOutput + TeamFtcRanked.shareHeader();
                for (TeamFtcRanked t : myApp.teamFtcRanked[0]) {
                    shareOutput = shareOutput + t;
                }
                shareOutput = shareOutput + Match.shareHeader();
                for (Match m : myApp.match[0]) {
                    shareOutput = shareOutput + m;
                }
            }
            if (myApp.team[1].size() > 0) {
                shareOutput = shareOutput + "Division,1";
                if (!myApp.divisionName[1].contentEquals("")) {
                    shareOutput = shareOutput + "," + myApp.divisionName[1];
                }
                shareOutput = shareOutput + System.getProperty("line.separator");
                shareOutput = shareOutput + "Teams," + myApp.team[1].size() + System.getProperty("line.separator");
                shareOutput = shareOutput + Team.shareHeader();
                for (Team t : myApp.team[1]) {
                    shareOutput = shareOutput + t;
                }
                shareOutput = shareOutput + TeamFtcRanked.shareHeader();
                for (TeamFtcRanked t : myApp.teamFtcRanked[1]) {
                    shareOutput = shareOutput + t;
                }
                shareOutput = shareOutput + Match.shareHeader();
                for (Match m : myApp.match[1]) {
                    shareOutput = shareOutput + m;
                }
            }
            if ((myApp.team[0].size() > 0) || (myApp.team[1].size() > 0)) {
                Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                intent.setType("message/rfc822");

// Add data to the intent, the receiving app will decide what to do with it.
                String subject = "FTC Tournament Data";
                if (!myApp.tournamentName.contentEquals("")) {
                    subject = subject + ": " + myApp.tournamentName;
                }
                intent.putExtra(Intent.EXTRA_SUBJECT, subject);
                intent.putExtra(Intent.EXTRA_TEXT, shareOutput);
                startActivity(Intent.createChooser(intent, "Select share option"));
            }
            return true;

        } else if (myApp.dualDivision()) {
            if (id == R.id.action_change_division) {
                if (myApp.division() == 0) {
                    myApp.setDivision(1);
                } else {
                    myApp.setDivision(0);
                }
                // restart activity to load data from new division
                Intent intent = new Intent(this, FtcRankingsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void processFinish(int result) {

    }
}

