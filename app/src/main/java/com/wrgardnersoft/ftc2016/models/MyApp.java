package com.wrgardnersoft.ftc2016.models;

import android.app.Application;

import java.io.BufferedReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

/**
 * Created by Bill on 2/2/2015.
 */
public class MyApp extends Application {

    public static final int NUM_ALLIANCES = 2;
    public static final int RED = 0;
    public static final int BLUE = 1; // BLUE MUST BE 1-RED!!!

    public static final int TEAMS_PER_MATCH = 2;
    public static final int TEAMS_PER_ALLIANCE = 3;
    public static final int NUM_SCORE_TYPES = ScoreType.values().length;
    static MyApp myAppInstance;
    //public class MyApp extends Application {
    public String[] serverAddressString = new String[4];
    public String tournamentCode;
    /*   public MyApp() {
           myAppInstance = this;
       }*/
    public boolean[] useAdvancedStats = new boolean[2];
    public String tournamentName;
    public String[] divisionName = new String[2];
    public ArrayList<Integer> selectedTeams;
    public Stat.Type detailType;
    public ArrayList<Team>[] team = (ArrayList<Team>[]) new ArrayList[2];  // full team info from team server page
    public ArrayList<TeamFtcRanked>[] teamFtcRanked = (ArrayList<TeamFtcRanked>[]) new ArrayList[2];
    public ArrayList<Match>[] match = (ArrayList<Match>[]) new ArrayList[2];
    public boolean enableMatchPrediction;
    public Stat.Type predictionType;
    public ArrayList<TeamStatRanked>[] teamStatRanked = (ArrayList<TeamStatRanked>[]) new ArrayList[2];
    public double meanOffenseScoreTotal[][] = new double[2][NUM_SCORE_TYPES];
    public double offenseVariance;
    public int currentTeamNumber, currentMatchNumber;
    private boolean dualDivision;
    private int division;
    public boolean useTrueMean;

    public MyApp() {

        myAppInstance = this;

        this.tournamentName = "";
        this.divisionName[0] = "";
        this.divisionName[1] = "";

        this.division = 0;
        this.dualDivision = false;

        this.serverAddressString[0] = "scoring.pennfirst.org/ftc";
        this.serverAddressString[1] = "Rankings_East_Super-Regional_Hopper";
        this.serverAddressString[2] = "Match_Results_East_Super-Regional_Hopper";
        this.tournamentCode = "2015esrho";

        this.serverAddressString[0] = "scoring.pennfirst.org/cache";
        this.serverAddressString[1] = "Rankings_2016_World_Championship_Edison";
        this.serverAddressString[2] = "MatchResultsDetails_2016_World_Championship_Edison";
        this.tournamentCode = "2015cmped";

//    this.serverAddressString[0] = "www.ftceast.org/tournament/tournament-results/2014-2015-results";
//    this.serverAddressString[1] = "tesla-team-rankings";
//    this.serverAddressString[2] = "tesla-match-results-details";
        this.serverAddressString[3] = "";
        this.selectedTeams = new ArrayList<>();

        this.team[0] = new ArrayList<>();
        this.team[1] = new ArrayList<>();

        this.teamFtcRanked[0] = new ArrayList<>();
        this.teamFtcRanked[1] = new ArrayList<>();

        this.teamStatRanked[0] = new ArrayList<>();
        this.teamStatRanked[1] = new ArrayList<>();

        this.useAdvancedStats[0] = false;
        this.useAdvancedStats[1] = false;

        this.match[0] = new ArrayList<>();
        this.match[1] = new ArrayList<>();

        this.currentTeamNumber = -1;

        this.detailType = Stat.Type.OPR;

        this.enableMatchPrediction = false; //true;
        this.predictionType = Stat.Type.OPR;

        this.useTrueMean = true;
    }

    public static MyApp getInstance() {
        return myAppInstance;
    }

    public static boolean saveTournamentData(OutputStreamWriter fw) {
        MyApp myApp = MyApp.getInstance();
        try {

            String saveOutput;

            int numDivisions = 1;
            if (myApp.dualDivision()) numDivisions = 2;
            saveOutput = String.valueOf(numDivisions) + System.getProperty("line.separator");

            for (int i = 0; i < numDivisions; i++) {
                saveOutput = saveOutput + String.valueOf(i) + System.getProperty("line.separator");
                saveOutput = saveOutput + String.valueOf(myApp.team[i].size()) + System.getProperty("line.separator");

                if (myApp.team[i].size() > 0) {
                    for (Team t : myApp.team[i]) {
                        saveOutput = saveOutput + t;
                    }
                    for (TeamFtcRanked t : myApp.teamFtcRanked[i]) {
                        saveOutput = saveOutput + t;
                    }
                    saveOutput = saveOutput + String.valueOf(myApp.match[i].size()) + System.getProperty("line.separator");
                    for (Match m : myApp.match[i]) {
                        saveOutput = saveOutput + m;
                    }
                }
            }
            fw.write(saveOutput);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    public static void loadTournamentData(BufferedReader br) {
        MyApp myApp = MyApp.getInstance();
        try {
            int numDivisions = Integer.valueOf(br.readLine());
            myApp.setDivision(0);
            myApp.dualDivision = (numDivisions == 2);
            for (int i = 0; i < numDivisions; i++) {
                int div = Integer.valueOf(br.readLine());
                int numTeams = Integer.valueOf(br.readLine());
                if (numTeams > 0) {
                    myApp.team[div].clear(); // made it this far, so hopefully good data coming
                    myApp.teamFtcRanked[div].clear();
                    myApp.match[div].clear();
                    myApp.teamStatRanked[div].clear();

                    for (int j = 0; j < numTeams; j++) {
                        myApp.team[div].add(Team.readFromBR(br));
                    }
                    for (int j = 0; j < numTeams; j++) {
                        myApp.teamFtcRanked[div].add(TeamFtcRanked.readFromBR(br));
                    }
                    int numMatches = Integer.valueOf(br.readLine());
                    if (numMatches > 0) {
                        for (int j = 0; j < numMatches; j++) {
                            myApp.match[div].add(Match.readFromBR(br));
                            myApp.match[div].get(j).number = j;
                        }
                    }
                    Stat.computeAll(div);
                }
            }
        } catch (Exception e) {
            //Log.i("MyApp.loadTournamentData", "Error loading");
        }
    }

    public int division() {
        return this.division;
    }

    public void setDivision(int div) {
        this.division = div;
    }

    public String serverAddressString(int i) {
        return serverAddressString[i];
    }

    public void setServerAddressString(int i, String addressString) {
        this.serverAddressString[i] = addressString;
        if (i == 0) {
            if (addressString.startsWith("http://")) {
                this.serverAddressString[0]=addressString.substring(7);
            }
            if (addressString.startsWith("https://")) {
                this.serverAddressString[0]=addressString.substring(8);
            }
        }
    }

    public boolean dualDivision() {
        return this.dualDivision;
    }

    public void setDualDivision(boolean dualDiv) {
        this.dualDivision = dualDiv;
    }

    public enum ScoreType { // TOTAL MUST BE FIRST, PENALTY MUST BE LAST!!!!!
        TOTAL, AUTONOMOUS, AUTO_BONUS, TELEOP, END_GAME, PENALTY
    }
}
