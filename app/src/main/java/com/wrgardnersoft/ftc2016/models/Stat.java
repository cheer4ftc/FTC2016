package com.wrgardnersoft.ftc2016.models;


import android.util.Log;

import java.util.ArrayList;

import Jama.Matrix;

/**
 * Created by Bill on 2/8/2015.
 */
public class Stat {

  public static String TypeDisplayString[] = {"OPRm", "Dif", "CPRm"};

  private static double OprComponent(Match m, int color, MyApp.ScoreType type) {
    double retVal = 0;

    if (type == MyApp.ScoreType.TOTAL) {
      retVal = m.score[color][type.ordinal()] - m.score[color][MyApp.ScoreType.PENALTY.ordinal()] - m.score[1 - color][MyApp.ScoreType.PENALTY.ordinal()];
    } else if (type == MyApp.ScoreType.PENALTY) {
      retVal = -m.score[1 - color][type.ordinal()];
    } else {
      retVal = m.score[color][type.ordinal()];
    }
    return retVal;
  }

  public static void computeAll(int division) {
    MyApp myApp = MyApp.getInstance();

    myApp.teamStatRanked[division].clear();

    if (myApp.team[division].size() > 0) {
      for (TeamFtcRanked t : myApp.teamFtcRanked[division]) {
        myApp.teamStatRanked[division].add(new TeamStatRanked());
        TeamStatRanked tStat = myApp.teamStatRanked[division].get(myApp.teamStatRanked[division].size() - 1);
        tStat.number = t.number;
        tStat.name = t.name;
        tStat.ftcRank = t.rank;
        /////////////////////////////////////////
        // win percentage is normal percentage of wins of matches played.
        // if no matches played, return 50% (best guess)
        // Also, add eps for each match if win percent is >=50%
        //   and subtract eps for each match if win percent is <50%
        // This will make teams that are 5-0 have a higher win percentage than 4-0 teams,
        //   and similarly make teams that are 0-5 have a lower win percentage than 0-4 teams.
        if (t.matches > 0) {
          tStat.winPercent = (float) t.qp / (float) t.matches * (float) 100.0 / (float) (2.0) + 0.0005;
          if (tStat.winPercent >= 50.0) {
            tStat.winPercent += 0.00005 * t.matches; // reward more wins
          } else {
            tStat.winPercent -= 0.00005 * t.matches; // penalize more losses
          }
        } else {
          tStat.winPercent = 50; // best guess if no matches played
        }
      }

      ArrayList<TeamStatRanked> ts = myApp.teamStatRanked[division];
      ArrayList<Match> ms = myApp.match[division];
      ArrayList<Integer> atn = new ArrayList<>();

      int numTeams = ts.size();
      for (int i = 0; i < numTeams; i++) {
        atn.add(ts.get(i).number);
      }

      // only count SCORED QUALIFYING matches
      int numMatches = 0;
      for (int i = 0; i < myApp.match[division].size(); i++) {
        if ((ms.get(i).score[MyApp.RED][MyApp.ScoreType.TOTAL.ordinal()] >= 0) &&
            (ms.get(i).title.substring(0, 1).matches("Q"))) {
          numMatches++;
        }
      }

      // New and improved OPRm, CPRm, and Dif
      //////////////////////////////////////////
      // For these stats, counting penalties as negative for the alliance incurring the penalty!
      // So one alliance isn't rewarded if their opponent happens to get a lot of penalties.

      Matrix Ar = new Matrix(numMatches, numTeams);
      Matrix Ab = new Matrix(numMatches, numTeams);
      Matrix Mr = new Matrix(numMatches, 1);
      Matrix Mb = new Matrix(numMatches, 1);

      Matrix Ao = new Matrix(2 * numMatches, numTeams);
      Matrix Aw = new Matrix(numMatches, numTeams);
      Matrix Mo = new Matrix(2 * numMatches, 1);
      Matrix Mw = new Matrix(numMatches, 1);

      Matrix Oprm = new Matrix(numTeams, 1);
      Matrix Cprm = new Matrix(numTeams, 1);
      Matrix Dif = new Matrix(numTeams, 1);

      //        int meanOffenseMatchCount[] = new int[numTeams];
      for (int i = 0; i < MyApp.NUM_SCORE_TYPES; i++) {
        myApp.meanOffenseScoreTotal[division][i] = 0;
      }

      if (numMatches > 0) {

        int matchesPerTeam[] = new int[numTeams];

        int iM = 0;
        for (int i = 0; i < myApp.match[division].size(); i++) {
          Match m = ms.get(i);

          if ((m.score[MyApp.RED][MyApp.ScoreType.TOTAL.ordinal()] >= 0) &&
              (m.title.substring(0, 1).matches("Q"))) {

            Ar.set(iM, atn.indexOf(m.teamNumber[MyApp.RED][0]), 1.0);
            Ar.set(iM, atn.indexOf(m.teamNumber[MyApp.RED][1]), 1.0);
            Ab.set(iM, atn.indexOf(m.teamNumber[MyApp.BLUE][0]), 1.0);
            Ab.set(iM, atn.indexOf(m.teamNumber[MyApp.BLUE][1]), 1.0);
            iM++;

            matchesPerTeam[atn.indexOf(m.teamNumber[MyApp.RED][0])]++;
            matchesPerTeam[atn.indexOf(m.teamNumber[MyApp.RED][1])]++;
            matchesPerTeam[atn.indexOf(m.teamNumber[MyApp.BLUE][0])]++;
            matchesPerTeam[atn.indexOf(m.teamNumber[MyApp.BLUE][1])]++;

          }
        }

        Ao.setMatrix(0, numMatches - 1, 0, numTeams - 1, Ar);
        Ao.setMatrix(numMatches, 2 * numMatches - 1, 0, numTeams - 1, Ab);
        Aw = Ar.minus(Ab);

        // normal OPR Matrix AoTAoInv = Ao.transpose().times(Ao).inverse();
        // normal CPR Matrix AwTAwInv = Aw.transpose().times(Aw).inverse();

        double MmseMultFactor = 1; //2; // 0 for pure OPR, CPR
        // OPR MMSE
        Matrix AoTAoInv = Ao.transpose().times(Ao).plus(Matrix.identity(numTeams, numTeams).times(MmseMultFactor)).inverse();
        // CPR MMSE
        Matrix AwTAwInv = Aw.transpose().times(Aw).plus(Matrix.identity(numTeams, numTeams).times(2 * MmseMultFactor)).inverse();

        ////////////////////////
        // OPR:
        //
        // Compute A topr = B
        //   where A = rows with two 1s representing which teams were in each alliance
        //       A has 2 rows per match.
        //   and B is the offensive score minus penalties for that alliance.
        // Then, least squares topr solution is, solve A' A topr = A' b
        //   A' A is positive semidef, so use Cholesky decomposition to solve it.

        for (MyApp.ScoreType type : MyApp.ScoreType.values()) {

          double offensePerTeam[] = new double[numTeams];
          double marginPerTeam[] = new double[numTeams];

          iM = 0;
          for (int i = 0; i < myApp.match[division].size(); i++) {
            Match m = ms.get(i);

            if ((m.score[MyApp.RED][MyApp.ScoreType.TOTAL.ordinal()] >= 0) &&
                (m.title.substring(0, 1).matches("Q"))) {

              Mr.set(iM, 0, Stat.OprComponent(m, MyApp.RED, type));
              Mb.set(iM, 0, Stat.OprComponent(m, MyApp.BLUE, type));

              offensePerTeam[atn.indexOf(m.teamNumber[MyApp.RED][0])] += Mr.get(iM, 0);
              offensePerTeam[atn.indexOf(m.teamNumber[MyApp.RED][1])] += Mr.get(iM, 0);

              offensePerTeam[atn.indexOf(m.teamNumber[MyApp.BLUE][0])] += Mb.get(iM, 0);
              offensePerTeam[atn.indexOf(m.teamNumber[MyApp.BLUE][1])] += Mb.get(iM, 0);

              marginPerTeam[atn.indexOf(m.teamNumber[MyApp.RED][0])] += Mr.get(iM, 0) - Mb.get(iM, 0);
              marginPerTeam[atn.indexOf(m.teamNumber[MyApp.RED][1])] += Mr.get(iM, 0) - Mb.get(iM, 0);

              marginPerTeam[atn.indexOf(m.teamNumber[MyApp.BLUE][0])] -= Mr.get(iM, 0) - Mb.get(iM, 0);
              marginPerTeam[atn.indexOf(m.teamNumber[MyApp.BLUE][1])] -= Mr.get(iM, 0) - Mb.get(iM, 0);

              myApp.meanOffenseScoreTotal[division][type.ordinal()] += Mr.get(iM, 0);
              myApp.meanOffenseScoreTotal[division][type.ordinal()] += Mb.get(iM, 0);

              iM++;

            }
          }

          myApp.meanOffenseScoreTotal[division][type.ordinal()] /= 2 * numMatches * 2; // per team, 2 for red/blue, 2 for 2 teams per alliance
          for (int i = 0; i < numMatches; i++) {
            Mr.set(i, 0, Mr.get(i, 0) - 2 * myApp.meanOffenseScoreTotal[division][type.ordinal()]);
            Mb.set(i, 0, Mb.get(i, 0) - 2 * myApp.meanOffenseScoreTotal[division][type.ordinal()]);
          }

          for (int i = 0; i < numTeams; i++) {
            marginPerTeam[i] /= 2;
          }

          Mo.setMatrix(0, numMatches - 1, 0, 0, Mr);
          Mo.setMatrix(numMatches, 2 * numMatches - 1, 0, 0, Mb);
          Mw = Mr.minus(Mb);

          ////////////////////////
          // OPR:
          //
          // Compute A topr = B
          //   where A = rows with two 1s representing which teams were in each alliance
          //       A has 2 rows per match.
          //   and B is the offensive score minus penalties for that alliance.
          // Then, least squares topr solution is, solve A' A topr = A' b

          Oprm = AoTAoInv.times(Ao.transpose().times(Mo));
          Cprm = AwTAwInv.times(Aw.transpose().times(Mw));

          // put in to normalize like traditional OPR
          if (myApp.useTrueMean) {
            for (int i = 0; i < numTeams; i++) {
              //                   Log.i("O "+String.valueOf(AtAinvA1.times(A1.transpose()).get(0,i)),String.valueOf(BoprA.get(i,0)));
              Oprm.set(i, 0, Oprm.get(i, 0) + myApp.meanOffenseScoreTotal[division][type.ordinal()]);
              Cprm.set(i, 0, Cprm.get(i, 0) + myApp.meanOffenseScoreTotal[division][type.ordinal()]);

            }
          } else {
            Log.i("No mean add", "yeah");
          }


          for (int i = 0; i < numTeams; i++) {
            myApp.teamStatRanked[division].get(i).oprA[type.ordinal()] = Oprm.get(i, 0);

            myApp.teamStatRanked[division].get(i).ccwmA[type.ordinal()] = Cprm.get(i, 0);

            myApp.teamStatRanked[division].get(i).dprA[type.ordinal()] = Cprm.get(i, 0) - Oprm.get(i, 0);

          }

          if (type == MyApp.ScoreType.TOTAL) {
            iM = 0;
            double Ex2=0;

            for (int i = 0; i < myApp.match[division].size(); i++) {
              Match m = ms.get(i);

              if ((m.score[MyApp.RED][MyApp.ScoreType.TOTAL.ordinal()] >= 0) &&
                  (m.title.substring(0, 1).matches("Q"))) {

                Ex2+=Stat.OprComponent(m, MyApp.RED, type) * Stat.OprComponent(m, MyApp.RED, type);
                Ex2+=Stat.OprComponent(m, MyApp.BLUE, type) * Stat.OprComponent(m, MyApp.BLUE, type);

                iM++;

              }
            }

            myApp.offenseVariance = Ex2/(double)(2*iM-1)
                - myApp.meanOffenseScoreTotal[division][type.ordinal()]*myApp.meanOffenseScoreTotal[division][type.ordinal()]+1;
          }

        }

      }
    }

  }

  public enum Type {OPR, DPR, CCWM}
}
