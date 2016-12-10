package com.wrgardnersoft.ftc2016.internet;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.wrgardnersoft.ftc2016.R;
import com.wrgardnersoft.ftc2016.interfaces.AsyncResponse;
import com.wrgardnersoft.ftc2016.models.Match;
import com.wrgardnersoft.ftc2016.models.MyApp;
import com.wrgardnersoft.ftc2016.models.Stat;
import com.wrgardnersoft.ftc2016.models.Team;
import com.wrgardnersoft.ftc2016.models.TeamFtcRanked;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Bill on 2/7/2015.
 */
// AsyncTask
public class ClientTask extends AsyncTask<Void, Void, Void> {
  public AsyncResponse delegate;
  ProgressDialog mProgressDialog;
  boolean serverOK;
  private Context mContext;

  public ClientTask(Context context) {
    mContext = context;
  }


  @Override
  protected void onPreExecute() {
    super.onPreExecute();

    mProgressDialog = new ProgressDialog(mContext);
    mProgressDialog.setTitle(mContext.getString(R.string.app_name));
    mProgressDialog.setMessage("Loading data...");
    mProgressDialog.setIndeterminate(false);
    mProgressDialog.show();
  }

  @Override
  protected Void doInBackground(Void... params) {
    String teamsUrl;
    String[] urlSuffix = {""}; //, ":8080"};

    MyApp myApp = MyApp.getInstance();
    Context context = myApp.getApplicationContext();

    String lastDataFileName = context.getString(R.string.lastReceivedData);

    serverOK = false;
    String[] suffix = {"", ".html"};

    for (int i = 0; (i < urlSuffix.length) && (!serverOK); i++) {

      String teamsUrlHeader = "http://" + myApp.serverAddressString(0) +
          urlSuffix[i] + "/";
      try {
        Document document;

        Comparator<Team> ct = Team.getComparator(Team.SortParameter.NUMBER_SORT);
        Collections.sort(myApp.team[myApp.division()], ct);

        if (myApp.serverAddressString(1).length()>0) {
          try {
            teamsUrl = teamsUrlHeader + myApp.serverAddressString(1) + suffix[0];
            document = Jsoup.connect(teamsUrl).get();
          } catch (Exception e1) {
            teamsUrl = teamsUrlHeader + myApp.serverAddressString(1) + suffix[1];
            document = Jsoup.connect(teamsUrl).get();
          }
        } else {
          teamsUrl = teamsUrlHeader.substring(0,teamsUrlHeader.length() -1);
          document = Jsoup.connect(teamsUrl).get();
        }

        int tableIndex, rowIndex;
        Element table; //select the first table.
        Elements rows;
        boolean tableOK;

        tableIndex = 0;
        tableOK = false;

        table = document.select("table").get(tableIndex);
        rows = table.select("tr");

        rowIndex = 0;
        while (!tableOK) {
          //Log.i("Checking Table", Integer.toString(tableIndex));
          table = document.select("table").get(tableIndex);
          rows = table.select("tr");
          for (rowIndex = 0; (rowIndex < rows.size()) && ((rows.get(rowIndex).select("th").isEmpty()) || (!rows.get(rowIndex).select("th").get(0).text().contentEquals("Rank"))); rowIndex++) {

          }
          if (rowIndex < rows.size()) {
            tableOK = true;
          } else {
            tableIndex++;
            table = document.select("table").get(tableIndex);
            rows = table.select("tr");
          }
        }

        table = rows.get(rowIndex).select("th").get(0);
        while (!((table == null) || (table.tagName().equalsIgnoreCase("table")))) {
          table = table.parent();
        }
        rows = table.select("tr");


        myApp.team[myApp.division()].clear(); // made it this far, so hopefully good data coming
        myApp.teamFtcRanked[myApp.division()].clear();
        myApp.match[myApp.division()].clear();
        myApp.teamStatRanked[myApp.division()].clear();

        for (int j = 1; j < rows.size(); j++) { //first row is the col names so skip it.
          Element row = rows.get(j);
          Elements cols = row.select("td");

          myApp.teamFtcRanked[myApp.division()].add(new TeamFtcRanked(Integer.parseInt(cols.get(0).text()),
              Integer.parseInt(cols.get(1).text()),
              cols.get(2).text(),
              Integer.parseInt(cols.get(3).text()),
              Integer.parseInt(cols.get(4).text()),
              Integer.parseInt(cols.get(5).text()),
              Integer.parseInt(cols.get(6).text())
          ));

          myApp.team[myApp.division()].add(new Team(Integer.parseInt(cols.get(1).text()),
                  cols.get(2).text(),
                  "",
                  "",
                  "",
                  "")
          );
        }

        //  Log.i("Got rankings", "OK");
        myApp.teamStatRanked[myApp.division()].clear();
        Stat.computeAll(myApp.division());

        //Log.i("Using", "Details");
        if (myApp.serverAddressString(2).length()>0) {
          try {
            teamsUrl = teamsUrlHeader + myApp.serverAddressString(2) + suffix[0];
            document = Jsoup.connect(teamsUrl).get();
          } catch (Exception e1) {
            teamsUrl = teamsUrlHeader + myApp.serverAddressString(2) + suffix[1];
            document = Jsoup.connect(teamsUrl).get();
          }
        } else {
          teamsUrl = teamsUrlHeader.substring(0,teamsUrlHeader.length() -1);
          document = Jsoup.connect(teamsUrl).get();
        }

        tableOK = false;

        try { // look for details page
          tableIndex = 0;
          table = document.select("table").get(tableIndex);
          rows = table.select("tr");

          rowIndex = 0;

          while (!tableOK) {
            table = document.select("table").get(tableIndex);
            rows = table.select("tr");
            for (rowIndex = 0; (rowIndex < rows.size()) && ((rows.get(rowIndex).select("th").size() < 5) ||
//              (!rows.get(rowIndex).select("th").get(0).text().contentEquals("Match"))); rowIndex++) {
                (!rows.get(rowIndex).select("th").get(4).text().contentEquals("Red Scores"))); rowIndex++) {
         /*     if (rows.get(rowIndex).select("th").size() >= 3) {
                Log.i("Table Row Read 0", rows.get(rowIndex).select("th").get(0).text());
                Log.i("Table Row Read 1", rows.get(rowIndex).select("th").get(1).text());
                Log.i("Table Row Read 2", rows.get(rowIndex).select("th").get(2).text());
              } else {
                Log.i("Table row w/ no headers", String.format("%d", rows.get(rowIndex).select("th").size()));
              }*/
            }
            try {
              if (rows.get(rowIndex).select("th").get(4).text().contentEquals("Red Scores")) {
                rowIndex++;
              }
            } catch (Exception e1) {
            }

            if (rowIndex < rows.size()) {
              //           Log.i("Row with Match",Integer.toString(rowIndex));
              tableOK = true;
            } else {
              tableIndex++;
              table = document.select("table").get(tableIndex);
              rows = table.select("tr");
            }
          }
        } catch (Exception e1) { // look for Match Results page
          tableIndex = 0;
          table = document.select("table").get(tableIndex);
          rows = table.select("tr");

          rowIndex = 0;

          while (!tableOK) {
            table = document.select("table").get(tableIndex);
            rows = table.select("tr");
            for (rowIndex = 0; (rowIndex < rows.size()) && ((rows.get(rowIndex).select("th").size() < 4) ||
              (!rows.get(rowIndex).select("th").get(0).text().contentEquals("Match"))); rowIndex++) {
//                (!rows.get(rowIndex).select("th").get(4).text().contentEquals("Red Scores"))); rowIndex++) {
            }

            if (rowIndex < rows.size()) {
              //           Log.i("Row with Match",Integer.toString(rowIndex));
              tableOK = true;
            } else {
              tableIndex++;
              table = document.select("table").get(tableIndex);
              rows = table.select("tr");
            }
          }
        }

        table = rows.get(rowIndex).select("th").get(0);
        while (!((table == null) || (table.tagName().equalsIgnoreCase("table")))) {
          table = table.parent();
        }
        rows = table.select("tr");

        if (rowIndex != 0) { // using match details
          for (int j = 2; j < rows.size(); j++) { //first row is the col names so skip it.
            Element row = rows.get(j);
            Elements cols = row.select("td");

            // special handling for 2 OR 3 team alliances
            String redTeam[] = {"0", "0", "0"};
            String blueTeam[] = {"0", "0", "0"};

        //    Log.i("Cols", String.format("%d", cols.size()));
            if (cols.size() == 16) {// teams are packed into one element
              String redTeamString = cols.get(2).text();
              String blueTeamString = cols.get(3).text();
              String redTeamResult[] = redTeamString.split("\\s+");
              String blueTeamResult[] = blueTeamString.split("\\s+");

              int k = 0;
              for (String team : redTeamResult) {
                redTeam[k] = redTeamResult[k];
                k++;
              }
              k = 0;
              for (String team : blueTeamResult) {
                blueTeam[k] = blueTeamResult[k];
                k++;
              }

              myApp.match[myApp.division()].add(new Match(j - 1,
                  cols.get(0).text(),
                  cols.get(1).text(),
                  redTeam[0],
                  redTeam[1],
                  redTeam[2],
                  blueTeam[0],
                  blueTeam[1],
                  blueTeam[2],
                  cols.get(4).text(),
                  cols.get(5).text(),
                  cols.get(6).text(),
                  cols.get(7).text(),
                  cols.get(8).text(),
                  cols.get(9).text(),
                  cols.get(10).text(),
                  cols.get(11).text(),
                  cols.get(12).text(),
                  cols.get(13).text(),
                  cols.get(14).text(),
                  cols.get(15).text(),
                  false // predicted = false: real match!
              ));
            } else if (cols.size() == 18) {// 2 teams per alliance
              String red2, blue2;
              red2 = cols.get(4).text();
              blue2 = cols.get(7).text();
              if (red2.contentEquals("")) {
                red2 = "0";
              }
              if (blue2.contentEquals("")) {
                blue2 = "0";
              }
              myApp.match[myApp.division()].add(new Match(j - 1,
                  cols.get(0).text(),
                  cols.get(1).text(),
                  cols.get(2).text(),
                  cols.get(3).text(),
                  "0",
                  cols.get(4).text(),
                  cols.get(5).text(),
                  "0",
                  cols.get(6).text(),
                  cols.get(7).text(),
                  cols.get(8).text(),
                  cols.get(9).text(),
                  cols.get(10).text(),
                  cols.get(11).text(),
                  cols.get(12).text(),
                  cols.get(13).text(),
                  cols.get(14).text(),
                  cols.get(15).text(),
                  cols.get(16).text(),
                  cols.get(17).text(),
                  false // predicted = false: real match!
              ));
            } else { // teams are split into 3 elements per alliance, I hope
              String red2, blue2;
              red2 = cols.get(4).text();
              blue2 = cols.get(7).text();
              if (red2.contentEquals("")) {
                red2 = "0";
              }
              if (blue2.contentEquals("")) {
                blue2 = "0";
              }
              myApp.match[myApp.division()].add(new Match(j - 1,
                  cols.get(0).text(),
                  cols.get(1).text(),
                  cols.get(2).text(),
                  cols.get(3).text(),
                  red2,
                  cols.get(5).text(),
                  cols.get(6).text(),
                  blue2,
                  cols.get(8).text(),
                  cols.get(9).text(),
                  cols.get(10).text(),
                  cols.get(11).text(),
                  cols.get(12).text(),
                  cols.get(13).text(),
                  cols.get(14).text(),
                  cols.get(15).text(),
                  cols.get(16).text(),
                  cols.get(17).text(),
                  cols.get(18).text(),
                  cols.get(19).text(),
                  false // predicted = false: real match!
              ));
            }
          }
        } else { // using match results
          //         Log.i("Using", "Results, getting data");
          for (int j = 1; j < rows.size(); ) { //first row is the col names so skip it.
            //           Log.i("Row", Integer.toString(j));
            Element row = rows.get(j);
            Elements cols = row.select("td");

            // special handling for 2 OR 3 team alliances
            String redTeam[] = {"0", "0", "0"};
            String blueTeam[] = {"0", "0", "0"};

            String title, result;

            int matchRowNum = 0;

            title = cols.get(0).text();
            result = cols.get(1).text();
            //Log.i("title", title);
            //Log.i("result", result);

            String redResult, blueResult;
            if (result.length() > 1) {
              String teamResult1[] = result.split("-");
//              Log.i("R1", teamResult1[0]);
              String teamResult2[] = teamResult1[1].split(" ");
//              Log.i("R2", teamResult2[0]);
              redResult = teamResult1[0];
              blueResult = teamResult2[0];
            } else {
              redResult = "";
              blueResult = "";
            }

            redTeam[matchRowNum] = cols.get(2).text();
            blueTeam[matchRowNum] = cols.get(3).text();
            j++;

            int teamCount;
            if (cols.get(0).text().startsWith("S") || cols.get(0).text().startsWith("F")) {
              teamCount = 3;
            } else {
              teamCount = 2;
            }

            for (matchRowNum = 1; matchRowNum < teamCount; matchRowNum++) {
              row = rows.get(j);
              cols = row.select("td");
              redTeam[matchRowNum] = cols.get(0).text();
              blueTeam[matchRowNum] = cols.get(1).text();
              j++;
            }
            try {
              Integer.parseInt(redTeam[2]);
            } catch (Exception e) { // empty 3rd team: small tournament
              redTeam[2]="0";
              blueTeam[2]="0";
            }

            myApp.match[myApp.division()].add(new Match(j - 1,
                title,
                result,
                redTeam[0],
                redTeam[1],
                redTeam[2],
                blueTeam[0],
                blueTeam[1],
                blueTeam[2],
                redResult,
                "0",
                "0",
                redResult,
                "0",
                "0",
                blueResult,
                "0",
                "0",
                blueResult,
                "0",
                "0",
                false // predicted = false: real match!
            ));
          }
        }

        //    Log.i("Got matches", "OK");

        myApp.teamStatRanked[myApp.division()].clear();
        Stat.computeAll(myApp.division());
        serverOK = true;

        try {
          context.deleteFile(lastDataFileName);
        } catch (Exception e) {
          //Log.i("No file to delete", "OK");
        }
        try {
          FileOutputStream fOut = context.openFileOutput(lastDataFileName, Context.MODE_PRIVATE);
          OutputStreamWriter fw = new OutputStreamWriter(fOut);

          MyApp.saveTournamentData(fw);

          fw.close();
        } catch (Exception e) {
          e.printStackTrace();
        }
      } catch (Exception e) {
        e.printStackTrace();
        if (!serverOK) {
          if (myApp.team[myApp.division()].size() == 0) {
            try {
              FileInputStream fi = context.openFileInput(lastDataFileName);
              InputStreamReader fr = new InputStreamReader(fi);

              BufferedReader br = new BufferedReader(fr);

              MyApp.loadTournamentData(br);

              fr.close();
            } catch (Exception f) {
              //Log.i("Error", "Can't open saved tournament data");
            }
          }
        }

        serverOK = false;
      }
    }


    return null;
  }


  @Override
  protected void onPostExecute(Void result) {

    mProgressDialog.dismiss();
    delegate.processFinish(Activity.RESULT_OK);

    if (!serverOK) {
      CharSequence text = "Error downloading data.\nTry tapping refresh.";
      int duration = Toast.LENGTH_LONG;
      Toast.makeText(mContext, text, duration).show();


    }
  }
}
