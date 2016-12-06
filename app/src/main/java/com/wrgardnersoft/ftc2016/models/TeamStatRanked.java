package com.wrgardnersoft.ftc2016.models;

import java.util.Comparator;

/**
 * Created by Bill on 2/3/2015.
 */
public class TeamStatRanked {

    public int number;
    public int ftcRank;
    public double winPercent;
    public double oprA[];
    public double dprA[];
    public double ccwmA[];
    public String name;


    public TeamStatRanked() {
        this.number = 0;
        this.ftcRank = 0;
        this.winPercent = 0;
        this.oprA = new double[MyApp.ScoreType.values().length];
        this.dprA = new double[MyApp.ScoreType.values().length];
        this.ccwmA = new double[MyApp.ScoreType.values().length];
        this.name="";
    }

    public TeamStatRanked(TeamStatRanked t) {
        this.number = t.number;
        this.ftcRank = t.ftcRank;
        this.winPercent = t.winPercent;
        this.oprA = new double[MyApp.ScoreType.values().length];
        this.dprA = new double[MyApp.ScoreType.values().length];
        this.ccwmA = new double[MyApp.ScoreType.values().length];
        for (int i=0; i<MyApp.ScoreType.values().length; i++) {
            this.oprA[i]=t.oprA[i];
            this.dprA[i]=t.dprA[i];
            this.ccwmA[i]=t.ccwmA[i];
        }
        this.name=t.name;
    }

    public String toString() {
        String output;
        output = this.number + "," + this.ftcRank + "," + this.winPercent;
        output = output + "," + this.ccwmA[MyApp.ScoreType.TOTAL.ordinal()] + "," + this.oprA[MyApp.ScoreType.TOTAL.ordinal()]
                + "," + this.dprA[MyApp.ScoreType.TOTAL.ordinal()];
        output = output + System.getProperty("line.separator");
        return output;
    }

    public static Comparator<TeamStatRanked> getComparator(SortParameter... sortParameters) {
        return new TeamStatRankedComparator(sortParameters);
    }

    public enum SortParameter {
        NUMBER_SORT, FTCRANK_SORT, WINPERCENT_SORT, OPR_SORT, DPR_SORT, CCWM_SORT, NAME_SORT,
        OFF_AUTO_SORT, OFF_TELE_SORT, OFF_ENDG_SORT, OFF_PEN_SORT,
        DEF_AUTO_SORT, DEF_TELE_SORT, DEF_ENDG_SORT, DEF_PEN_SORT,
        CMB_AUTO_SORT, CMB_TELE_SORT, CMB_ENDG_SORT, CMB_PEN_SORT
    }

    private static class TeamStatRankedComparator implements Comparator<TeamStatRanked> {
        private SortParameter[] parameters;

        private TeamStatRankedComparator(SortParameter[] parameters) {
            this.parameters = parameters;
        }

        public int compare(TeamStatRanked o1, TeamStatRanked o2) {
            int comparison;
            for (SortParameter parameter: parameters) {
                switch (parameter) {
                    case NUMBER_SORT:
                        comparison = o1.number - o2.number;
                        if (comparison!=0) return comparison;
                        break;
                    case FTCRANK_SORT:
                        comparison = o1.ftcRank - o2.ftcRank;
                        if (comparison!=0) return comparison;
                        break;
                    case WINPERCENT_SORT:
                        comparison = (int)(-100000 * (o1.winPercent - o2.winPercent));
                        if (comparison!=0) return comparison;
                        break;
                    case OPR_SORT:
                        comparison = (int)(-100000 * (o1.oprA[MyApp.ScoreType.TOTAL.ordinal()] - o2.oprA[MyApp.ScoreType.TOTAL.ordinal()]));
                        if (comparison!=0) return comparison;
                        break;
                    case DPR_SORT:
                        comparison = (int)(-100000 * (o1.dprA[MyApp.ScoreType.TOTAL.ordinal()] - o2.dprA[MyApp.ScoreType.TOTAL.ordinal()]));
                        if (comparison!=0) return comparison;
                        break;
                    case CCWM_SORT:
                        comparison = (int)(-100000 * (o1.ccwmA[MyApp.ScoreType.TOTAL.ordinal()] - o2.ccwmA[MyApp.ScoreType.TOTAL.ordinal()]));
                        if (comparison!=0) return comparison;
                        break;
                    case NAME_SORT:
                        comparison = o1.name.compareTo(o2.name);
                        if (comparison!=0) return comparison;

                        break;
                    case OFF_AUTO_SORT:
                        comparison = (int)(-100000 * (o1.oprA[MyApp.ScoreType.AUTONOMOUS.ordinal()] - o2.oprA[MyApp.ScoreType.AUTONOMOUS.ordinal()]));
                        if (comparison!=0) return comparison;
                        break;
                    case OFF_TELE_SORT:
                        comparison = (int)(-100000 * (o1.oprA[MyApp.ScoreType.TELEOP.ordinal()] - o2.oprA[MyApp.ScoreType.TELEOP.ordinal()]));
                        if (comparison!=0) return comparison;
                        break;
                    case OFF_ENDG_SORT:
                        comparison = (int)(-100000 * (o1.oprA[MyApp.ScoreType.END_GAME.ordinal()] - o2.oprA[MyApp.ScoreType.END_GAME.ordinal()]));
                        if (comparison!=0) return comparison;
                        break;
                    case OFF_PEN_SORT:
                        comparison = (int)(-100000 * (o1.oprA[MyApp.ScoreType.PENALTY.ordinal()] - o2.oprA[MyApp.ScoreType.PENALTY.ordinal()]));
                        if (comparison!=0) return comparison;
                        break;

                    case DEF_AUTO_SORT:
                        comparison = (int)(-100000 * (o1.dprA[MyApp.ScoreType.AUTONOMOUS.ordinal()] - o2.dprA[MyApp.ScoreType.AUTONOMOUS.ordinal()]));
                        if (comparison!=0) return comparison;
                        break;
                    case DEF_TELE_SORT:
                        comparison = (int)(-100000 * (o1.dprA[MyApp.ScoreType.TELEOP.ordinal()] - o2.dprA[MyApp.ScoreType.TELEOP.ordinal()]));
                        if (comparison!=0) return comparison;
                        break;
                    case DEF_ENDG_SORT:
                        comparison = (int)(-100000 * (o1.dprA[MyApp.ScoreType.END_GAME.ordinal()] - o2.dprA[MyApp.ScoreType.END_GAME.ordinal()]));
                        if (comparison!=0) return comparison;
                        break;
                    case DEF_PEN_SORT:
                        comparison = (int)(-100000 * (o1.dprA[MyApp.ScoreType.PENALTY.ordinal()] - o2.dprA[MyApp.ScoreType.PENALTY.ordinal()]));
                        if (comparison!=0) return comparison;
                        break;

                    case CMB_AUTO_SORT:
                        comparison = (int)(-100000 * (o1.ccwmA[MyApp.ScoreType.AUTONOMOUS.ordinal()] - o2.ccwmA[MyApp.ScoreType.AUTONOMOUS.ordinal()]));
                        if (comparison!=0) return comparison;
                        break;
                    case CMB_TELE_SORT:
                        comparison = (int)(-100000 * (o1.ccwmA[MyApp.ScoreType.TELEOP.ordinal()] - o2.ccwmA[MyApp.ScoreType.TELEOP.ordinal()]));
                        if (comparison!=0) return comparison;
                        break;
                    case CMB_ENDG_SORT:
                        comparison = (int)(-100000 * (o1.ccwmA[MyApp.ScoreType.END_GAME.ordinal()] - o2.ccwmA[MyApp.ScoreType.END_GAME.ordinal()]));
                        if (comparison!=0) return comparison;
                        break;
                    case CMB_PEN_SORT:
                        comparison = (int)(-100000 * (o1.ccwmA[MyApp.ScoreType.PENALTY.ordinal()] - o2.ccwmA[MyApp.ScoreType.PENALTY.ordinal()]));
                        if (comparison!=0) return comparison;
                        break;

                }
            }
            return 0;
        }
    }

}

