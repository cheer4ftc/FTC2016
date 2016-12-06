package com.wrgardnersoft.ftc2016.models;

import android.util.Log;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Bill on 2/3/2015.
 */
public class Match {
  public int number;
  public String title, resultStr;
  public int teamNumber[][];
  public double score[][];
  public boolean predicted;
  public double probRedWin;

  public Match() {
    this.number = 0;
    this.title = null;
    this.resultStr = null;

    this.teamNumber = new int[MyApp.NUM_ALLIANCES][MyApp.TEAMS_PER_ALLIANCE];
    this.score = new double[MyApp.NUM_ALLIANCES][MyApp.NUM_SCORE_TYPES];

    this.predicted = false;
    this.probRedWin = 0.5;
  }

  public Match(Match m) {
    this.number = m.number;
    this.title = m.title;
    this.resultStr = m.resultStr;

    this.teamNumber = new int[MyApp.NUM_ALLIANCES][MyApp.TEAMS_PER_ALLIANCE];
    this.score = new double[MyApp.NUM_ALLIANCES][MyApp.NUM_SCORE_TYPES];

    for (int i = 0; i < MyApp.NUM_ALLIANCES; i++) {
      for (int j = 0; j < MyApp.TEAMS_PER_ALLIANCE; j++) {
        this.teamNumber[i][j] = m.teamNumber[i][j];
      }
      for (int j = 0; j < MyApp.NUM_SCORE_TYPES; j++) {
        this.score[i][j] = m.score[i][j];
      }
    }
    for (int i = 0; i < MyApp.NUM_ALLIANCES; i++) {
      for (int j = 0; j < MyApp.TEAMS_PER_ALLIANCE; j++) {
        this.teamNumber[i][j] = m.teamNumber[i][j];
      }
      for (int j = 0; j < MyApp.NUM_SCORE_TYPES; j++) {
        this.score[i][j] = m.score[i][j];
      }
    }
    this.predicted = m.predicted;
    this.probRedWin = 0.5;
  }

  public Match(int num, String sName, String sResult,
               String rT0,
               String rT1,
               String bT0,
               String bT1,
               String rTot,
               String rAuto,
               String rAutoB,
               String rTele,
               String rEndG,
               String rPen,
               String bTot,
               String bAuto,
               String bAutoB,
               String bTele,
               String bEndG,
               String bPen,
               boolean predicted) {

    this.number = num;
    this.title = sName;
    this.teamNumber = new int[MyApp.NUM_ALLIANCES][MyApp.TEAMS_PER_ALLIANCE];
    this.score = new double[MyApp.NUM_ALLIANCES][MyApp.NUM_SCORE_TYPES];

    try {
      this.teamNumber[MyApp.RED][0] = Integer.parseInt(rT0);
    } catch (NumberFormatException e) { // catch surrogate match *
      this.teamNumber[MyApp.RED][0] = Integer.parseInt(rT0.substring(0, rT0.length() - 1));
    }
    try {
      this.teamNumber[MyApp.RED][1] = Integer.parseInt(rT1);
    } catch (NumberFormatException e) { // catch surrogate match *
      this.teamNumber[MyApp.RED][1] = Integer.parseInt(rT1.substring(0, rT1.length() - 1));
    }
    this.teamNumber[MyApp.RED][2] = 0;
    try {
      this.teamNumber[MyApp.BLUE][0] = Integer.parseInt(bT0);
    } catch (NumberFormatException e) { // catch surrogate match *
      this.teamNumber[MyApp.BLUE][0] = Integer.parseInt(bT0.substring(0, bT0.length() - 1));
    }
    try {
      this.teamNumber[MyApp.BLUE][1] = Integer.parseInt(bT1);
    } catch (NumberFormatException e) { // catch surrogate match *
      this.teamNumber[MyApp.BLUE][1] = Integer.parseInt(bT1.substring(0, bT1.length() - 1));
    }
    this.teamNumber[MyApp.BLUE][2] = 0;

    if ((sResult == null) || (sResult.startsWith(" "))) {
      this.resultStr = "No Result Yet";
    } else {
      this.resultStr = sResult;
      try {
        if (rTot != null)
          this.score[MyApp.RED][MyApp.ScoreType.TOTAL.ordinal()] = Double.parseDouble(rTot);
      } catch (NumberFormatException e) {
        this.score[MyApp.RED][MyApp.ScoreType.TOTAL.ordinal()] = -1;
      }
      try {
        if (rAuto != null)
          this.score[MyApp.RED][MyApp.ScoreType.AUTONOMOUS.ordinal()] = Double.parseDouble(rAuto);
      } catch (NumberFormatException e) {
        this.score[MyApp.RED][MyApp.ScoreType.AUTONOMOUS.ordinal()] = -1;
      }
      try {
        if (rAutoB != null)
          this.score[MyApp.RED][MyApp.ScoreType.AUTO_BONUS.ordinal()] = Double.parseDouble(rAutoB);
      } catch (NumberFormatException e) {
        this.score[MyApp.RED][MyApp.ScoreType.AUTO_BONUS.ordinal()] = -1;
      }
      try {
        if (rTele != null)
          this.score[MyApp.RED][MyApp.ScoreType.TELEOP.ordinal()] = Double.parseDouble(rTele);
      } catch (NumberFormatException e) {
        this.score[MyApp.RED][MyApp.ScoreType.TELEOP.ordinal()] = -1;
      }
      try {
        if (rEndG != null)
          this.score[MyApp.RED][MyApp.ScoreType.END_GAME.ordinal()] = Double.parseDouble(rEndG);
      } catch (NumberFormatException e) {
        this.score[MyApp.RED][MyApp.ScoreType.END_GAME.ordinal()] = -1;
      }
      try {
        if (rPen != null)
          this.score[MyApp.RED][MyApp.ScoreType.PENALTY.ordinal()] = Double.parseDouble(rPen);
      } catch (NumberFormatException e) {
        this.score[MyApp.RED][MyApp.ScoreType.PENALTY.ordinal()] = -1;
      }
      try {
        if (bTot != null)
          this.score[MyApp.BLUE][MyApp.ScoreType.TOTAL.ordinal()] = Double.parseDouble(bTot);
      } catch (NumberFormatException e) {
        this.score[MyApp.BLUE][MyApp.ScoreType.TOTAL.ordinal()] = -1;
      }
      try {
        if (bAuto != null)
          this.score[MyApp.BLUE][MyApp.ScoreType.AUTONOMOUS.ordinal()] = Double.parseDouble(bAuto);
      } catch (NumberFormatException e) {
        this.score[MyApp.BLUE][MyApp.ScoreType.AUTONOMOUS.ordinal()] = -1;
      }
      try {
        if (bAutoB != null)
          this.score[MyApp.BLUE][MyApp.ScoreType.AUTO_BONUS.ordinal()] = Double.parseDouble(bAutoB);
      } catch (NumberFormatException e) {
        this.score[MyApp.BLUE][MyApp.ScoreType.AUTO_BONUS.ordinal()] = -1;
      }
      try {
        if (bTele != null)
          this.score[MyApp.BLUE][MyApp.ScoreType.TELEOP.ordinal()] = Double.parseDouble(bTele);
      } catch (NumberFormatException e) {
        this.score[MyApp.BLUE][MyApp.ScoreType.TELEOP.ordinal()] = -1;
      }
      try {
        if (bEndG != null)
          this.score[MyApp.BLUE][MyApp.ScoreType.END_GAME.ordinal()] = Double.parseDouble(bEndG);
      } catch (NumberFormatException e) {
        this.score[MyApp.BLUE][MyApp.ScoreType.END_GAME.ordinal()] = -1;
      }
      try {
        if (bPen != null)
          this.score[MyApp.BLUE][MyApp.ScoreType.PENALTY.ordinal()] = Double.parseDouble(bPen);
      } catch (NumberFormatException e) {
        this.score[MyApp.BLUE][MyApp.ScoreType.PENALTY.ordinal()] = -1;
      }
    }
    this.predicted = predicted;
    this.probRedWin = 0.5;
  }


  public Match(int num, String sName, String sResult,
               String rT0,
               String rT1,
               String rT2,
               String bT0,
               String bT1,
               String bT2,
               String rTot,
               String rAuto,
               String rAutoB,
               String rTele,
               String rEndG,
               String rPen,
               String bTot,
               String bAuto,
               String bAutoB,
               String bTele,
               String bEndG,
               String bPen,
               boolean predicted) {
    this.number = num;
    this.title = sName;
    this.teamNumber = new int[MyApp.NUM_ALLIANCES][MyApp.TEAMS_PER_ALLIANCE];
    this.score = new double[MyApp.NUM_ALLIANCES][MyApp.NUM_SCORE_TYPES];

    try {
      this.teamNumber[MyApp.RED][0] = Integer.parseInt(rT0);
    } catch (NumberFormatException e) { // catch surrogate match *
      this.teamNumber[MyApp.RED][0] = Integer.parseInt(rT0.substring(0, rT0.length() - 1));
    }
    try {
      this.teamNumber[MyApp.RED][1] = Integer.parseInt(rT1);
    } catch (NumberFormatException e) { // catch surrogate match *
      this.teamNumber[MyApp.RED][1] = Integer.parseInt(rT1.substring(0, rT1.length() - 1));
    }
    this.teamNumber[MyApp.RED][2] = Integer.parseInt(rT2); // can't be surrogate

    try {
      this.teamNumber[MyApp.BLUE][0] = Integer.parseInt(bT0);
    } catch (NumberFormatException e) { // catch surrogate match *
      this.teamNumber[MyApp.BLUE][0] = Integer.parseInt(bT0.substring(0, bT0.length() - 1));
    }
    try {
      this.teamNumber[MyApp.BLUE][1] = Integer.parseInt(bT1);
    } catch (NumberFormatException e) { // catch surrogate match *
      this.teamNumber[MyApp.BLUE][1] = Integer.parseInt(bT1.substring(0, bT1.length() - 1));
    }
    this.teamNumber[MyApp.BLUE][2] = Integer.parseInt(bT2); // can't be surrogate

    if ((sResult == null) || (sResult.startsWith(" "))) {
      this.resultStr = "No Result Yet";
    } else {
      this.resultStr = sResult;
      try {
        if (rTot != null)
          this.score[MyApp.RED][MyApp.ScoreType.TOTAL.ordinal()] = Double.parseDouble(rTot);
      } catch (NumberFormatException e) {
        this.score[MyApp.RED][MyApp.ScoreType.TOTAL.ordinal()] = -1;
      }
      try {
        if (rAuto != null)
          this.score[MyApp.RED][MyApp.ScoreType.AUTONOMOUS.ordinal()] = Double.parseDouble(rAuto);
      } catch (NumberFormatException e) {
        this.score[MyApp.RED][MyApp.ScoreType.AUTONOMOUS.ordinal()] = -1;
      }
      try {
        if (rAutoB != null)
          this.score[MyApp.RED][MyApp.ScoreType.AUTO_BONUS.ordinal()] = Double.parseDouble(rAutoB);
      } catch (NumberFormatException e) {
        this.score[MyApp.RED][MyApp.ScoreType.AUTO_BONUS.ordinal()] = -1;
      }
      try {
        if (rTele != null)
          this.score[MyApp.RED][MyApp.ScoreType.TELEOP.ordinal()] = Double.parseDouble(rTele);
      } catch (NumberFormatException e) {
        this.score[MyApp.RED][MyApp.ScoreType.TELEOP.ordinal()] = -1;
      }
      try {
        if (rEndG != null)
          this.score[MyApp.RED][MyApp.ScoreType.END_GAME.ordinal()] = Double.parseDouble(rEndG);
      } catch (NumberFormatException e) {
        this.score[MyApp.RED][MyApp.ScoreType.END_GAME.ordinal()] = -1;
      }
      try {
        if (rPen != null)
          this.score[MyApp.RED][MyApp.ScoreType.PENALTY.ordinal()] = Double.parseDouble(rPen);
      } catch (NumberFormatException e) {
        this.score[MyApp.RED][MyApp.ScoreType.PENALTY.ordinal()] = -1;
      }
      try {
        if (bTot != null)
          this.score[MyApp.BLUE][MyApp.ScoreType.TOTAL.ordinal()] = Double.parseDouble(bTot);
      } catch (NumberFormatException e) {
        this.score[MyApp.BLUE][MyApp.ScoreType.TOTAL.ordinal()] = -1;
      }
      try {
        if (bAuto != null)
          this.score[MyApp.BLUE][MyApp.ScoreType.AUTONOMOUS.ordinal()] = Double.parseDouble(bAuto);
      } catch (NumberFormatException e) {
        this.score[MyApp.BLUE][MyApp.ScoreType.AUTONOMOUS.ordinal()] = -1;
      }
      try {
        if (bAutoB != null)
          this.score[MyApp.BLUE][MyApp.ScoreType.AUTO_BONUS.ordinal()] = Double.parseDouble(bAutoB);
      } catch (NumberFormatException e) {
        this.score[MyApp.BLUE][MyApp.ScoreType.AUTO_BONUS.ordinal()] = -1;
      }
      try {
        if (bTele != null)
          this.score[MyApp.BLUE][MyApp.ScoreType.TELEOP.ordinal()] = Double.parseDouble(bTele);
      } catch (NumberFormatException e) {
        this.score[MyApp.BLUE][MyApp.ScoreType.TELEOP.ordinal()] = -1;
      }
      try {
        if (bEndG != null)
          this.score[MyApp.BLUE][MyApp.ScoreType.END_GAME.ordinal()] = Double.parseDouble(bEndG);
      } catch (NumberFormatException e) {
        this.score[MyApp.BLUE][MyApp.ScoreType.END_GAME.ordinal()] = -1;
      }
      try {
        if (bPen != null)
          this.score[MyApp.BLUE][MyApp.ScoreType.PENALTY.ordinal()] = Double.parseDouble(bPen);
      } catch (NumberFormatException e) {
        this.score[MyApp.BLUE][MyApp.ScoreType.PENALTY.ordinal()] = -1;
      }
    }
    this.predicted = predicted;
    this.probRedWin = 0.5;

  }

  public static void prepareListData(ArrayList<Match> m, List<String> listDataHeader,
                                     HashMap<String, List<Match>> listDataChild) {

    // Adding child data
    listDataHeader.add("Qualifier");
    listDataHeader.add("Semi-Final");
    listDataHeader.add("Final");

    // Adding child data
    List<Match> qual = new ArrayList<>();
    List<Match> semi = new ArrayList<>();
    List<Match> finals = new ArrayList<>();

    for (int i = 0; i < m.size(); i++) {
      if (m.get(i).title.startsWith("Q")) {
        qual.add(m.get(i));
      } else if (m.get(i).title.startsWith("S")) {
        semi.add(m.get(i));
      } else if (m.get(i).title.startsWith("F")) {
        finals.add(m.get(i));
      }
    }

    listDataChild.put(listDataHeader.get(0), qual); // Header, Child data
    listDataChild.put(listDataHeader.get(1), semi);
    listDataChild.put(listDataHeader.get(2), finals);
  }

  public static String shareHeader() {
    String output;
    output = "Name,Result,Red0,Red1,Red2,Blue0,Blue1,Blue2,RedTot,RedAuto,RedAutoB,RedTele,RedEndG,RedPen,BlueTot,BlueAuto,BlueAutoB,BlueTele,BlueEndG,BluePen" + System.getProperty("line.separator");
    return output;
  }

  public static Match readFromBR(BufferedReader fr) {
    Match m = new Match();
    //int matchNum=0;
    try {
      String input = fr.readLine();
      List<String> param = Arrays.asList(input.split(","));
      int ind = 0;
      m.title = param.get(ind++);
      m.resultStr = param.get(ind++);
      for (int i = 0; i < MyApp.NUM_ALLIANCES; i++) {
        for (int j = 0; j < MyApp.TEAMS_PER_ALLIANCE; j++) {
          m.teamNumber[i][j] = Integer.valueOf(param.get(ind++));
        }
      }
      for (int i = 0; i < MyApp.NUM_ALLIANCES; i++) {
        for (int j = 0; j < MyApp.NUM_SCORE_TYPES; j++) {
          m.score[i][j] = Double.valueOf(param.get(ind++));
        }
      }
    } catch (Exception e) {
      Log.i("Match.readFromBR", "Error reading");
    }
    return m;
  }

  public void predict() {
    MyApp myApp = MyApp.getInstance();

    this.predicted = true;
    double sf = 1.0;
    if (this.teamNumber[MyApp.RED][2] > 0) { // 3 team alliance so scale scores by 2/3
      sf = 2.0 / 3.0;
    }
    for (int i = 0; i < MyApp.NUM_ALLIANCES; i++) {
      for (int j = 0; j < MyApp.NUM_SCORE_TYPES; j++) {
        this.score[i][j] = 0;
      }
    }
    for (int color = 0; color < MyApp.NUM_ALLIANCES; color++) {
      if (!myApp.useTrueMean) {
        for (int j = 0; j < MyApp.NUM_SCORE_TYPES - 1; j++) {
          this.score[color][j] += myApp.meanOffenseScoreTotal[myApp.division()][j] * MyApp.TEAMS_PER_MATCH;
          //   Log.i("Mean "+String.valueOf(j), String.valueOf(this.score[color][j]));
        }

        this.score[1 - color][MyApp.ScoreType.PENALTY.ordinal()] -=
            myApp.meanOffenseScoreTotal[myApp.division()][MyApp.ScoreType.PENALTY.ordinal()] * MyApp.TEAMS_PER_MATCH;
        this.score[1 - color][MyApp.ScoreType.TOTAL.ordinal()] -=
            myApp.meanOffenseScoreTotal[myApp.division()][MyApp.ScoreType.PENALTY.ordinal()] * MyApp.TEAMS_PER_MATCH;
        this.score[color][MyApp.ScoreType.TOTAL.ordinal()] -=
            myApp.meanOffenseScoreTotal[myApp.division()][MyApp.ScoreType.PENALTY.ordinal()] * MyApp.TEAMS_PER_MATCH;
      }
      //   Log.i("Mean 0**", String.valueOf(this.score[0][0]));
      //   Log.i("Mean 1**", String.valueOf(this.score[1][0]));
    }
    for (TeamStatRanked t : myApp.teamStatRanked[myApp.division()]) {
      for (int color = 0; color < MyApp.NUM_ALLIANCES; color++) {

        if ((this.teamNumber[color][0] == t.number) ||
            (this.teamNumber[color][1] == t.number) ||
            (this.teamNumber[color][2] == t.number)) {

          for (int i = 0; i < MyApp.NUM_SCORE_TYPES - 1; i++) {
            this.score[color][i] += sf * t.oprA[i];
          }
          this.score[1 - color][MyApp.ScoreType.PENALTY.ordinal()] -= sf *
              t.oprA[MyApp.ScoreType.PENALTY.ordinal()];
          this.score[1 - color][MyApp.ScoreType.TOTAL.ordinal()] -= sf *
              t.oprA[MyApp.ScoreType.PENALTY.ordinal()];
          this.score[color][MyApp.ScoreType.TOTAL.ordinal()] -= sf *
              t.oprA[MyApp.ScoreType.PENALTY.ordinal()];

          if (myApp.predictionType == Stat.Type.CCWM) {
            for (int i = 0; i < MyApp.NUM_SCORE_TYPES - 1; i++) {
              this.score[1 - color][i] -= sf * t.dprA[i];
            }
            this.score[color][MyApp.ScoreType.PENALTY.ordinal()] += sf *
                t.dprA[MyApp.ScoreType.PENALTY.ordinal()];
            this.score[color][MyApp.ScoreType.TOTAL.ordinal()] += sf *
                t.dprA[MyApp.ScoreType.PENALTY.ordinal()];

            this.score[1 - color][MyApp.ScoreType.TOTAL.ordinal()] += sf *
                t.dprA[MyApp.ScoreType.PENALTY.ordinal()];
          }

        }
      }
    }

    // estimate probability of red win

    // estimate 2/3 of overall variance is random and 1/3 is due to team performance
    this.probRedWin = gaussianCdf((this.score[MyApp.RED][MyApp.ScoreType.TOTAL.ordinal()]-this.score[MyApp.BLUE][MyApp.ScoreType.TOTAL.ordinal()])
        /(Math.sqrt(2*0.66666*myApp.offenseVariance+0.001))); // 2, because the RV is the difference between two offensive scores
//    this.probRedWin = gaussianCdf(1);

  }

  // fractional error less than x.xx * 10 ^ -4.
  // Algorithm 26.2.17 in Abromowitz and Stegun, Handbook of Mathematical.
  public static double erf(double z) {
    double t = 1.0 / (1.0 + 0.47047 * Math.abs(z));
    double poly = t * (0.3480242 + t * (-0.0958798 + t * (0.7478556)));
    double ans = 1.0 - poly * Math.exp(-z*z);
    if (z >= 0) return  ans;
    else        return -ans;
  }

  // cumulative normal distribution
  // See Gaussia.java for a better way to compute Phi(z)
  public static double gaussianCdf(double z) {
    return 0.5 * (1.0 + erf(z / (Math.sqrt(2.0))));
  }

  public void predictFromTeamPosition(int k, Stat.Type type) {

    MyApp myApp = MyApp.getInstance();

    for (int i = 0; i < MyApp.NUM_ALLIANCES; i++) {
      for (int j = 0; j < MyApp.NUM_SCORE_TYPES; j++) {
        this.score[i][j] = 0;
      }
    }

    for (TeamStatRanked t : myApp.teamStatRanked[myApp.division()]) {
      for (int color = 0; color < MyApp.NUM_ALLIANCES; color++) {

        if ((this.teamNumber[color][k] == t.number)) {

          for (int i = 0; i < MyApp.NUM_SCORE_TYPES; i++) {
            if (type == Stat.Type.OPR) {
              this.score[color][i] = t.oprA[i];
            } else if (type == Stat.Type.DPR) {
              this.score[color][i] = t.dprA[i];
            } else if (type == Stat.Type.CCWM) {
              this.score[color][i] = t.ccwmA[i];
            }

          }
                        /*
                        if (this.displayType == Stat.Type.OPR) {
                            this.score[color][MyApp.ScoreType.TOTAL.ordinal()] +=
                                    t.oprA[MyApp.ScoreType.PENALTY.ordinal()];
                        } else if (this.displayType == Stat.Type.DPR) {
                            this.score[color][MyApp.ScoreType.TOTAL.ordinal()] +=
                                    t.dprA[MyApp.ScoreType.PENALTY.ordinal()];
                        } else if (this.displayType == Stat.Type.CCWM) {
                            this.score[color][MyApp.ScoreType.TOTAL.ordinal()] +=
                                    t.ccwmA[MyApp.ScoreType.PENALTY.ordinal()];
                        }*/

        }

      }
    }

  }

  public String toString() {
    String output;
    output = this.title + "," + this.resultStr;
    for (int i = 0; i < MyApp.NUM_ALLIANCES; i++) {
      for (int j = 0; j < MyApp.TEAMS_PER_ALLIANCE; j++) {
        output = output + "," + this.teamNumber[i][j];
      }
    }
    for (int i = 0; i < MyApp.NUM_ALLIANCES; i++) {
      for (int j = 0; j < MyApp.NUM_SCORE_TYPES; j++) {
        output = output + "," + this.score[i][j];
      }
    }
    output = output + System.getProperty("line.separator");
    return output;
  }

}
