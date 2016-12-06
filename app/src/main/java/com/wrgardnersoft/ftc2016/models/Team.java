package com.wrgardnersoft.ftc2016.models;

import android.util.Log;

import java.io.BufferedReader;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Bill on 2/3/2015.
 */
public class Team {
    public int number;
    public String name, school, city, state, country;

    public Team() {
        this.number=0;
        this.name="";
        this.school="";
        this.city="";
        this.state="";
        this.country="";
    }
    public Team(Team t) {
        this.number=t.number;
        this.name=t.name;
        this.school=t.school;
        this.city=t.city;
        this.state=t.state;
        this.country=t.country;
    }
    public Team(int num, String sName, String sSchool, String sCity, String sState, String sCountry) {
        this.number=num;
        this.name=sName;
        this.school=sSchool;
        this.city=sCity;
        this.state=sState;
        this.country=sCountry;
    }

    public static String shareHeader() {
        String output;
        output = "Number,Name,School,City,State,Country"+ System.getProperty("line.separator");
        return output;
    }
    public String toString() {
        String output;
        output = this.number + "," + this.name + "," + this.school + "," + this.city + "," + this.state + "," + this.country + System.getProperty("line.separator");
        return output;
    }
    public static Team readFromBR(BufferedReader fr) {
        Team t = new Team();
        try {
            String input = fr.readLine();
            List<String> param = Arrays.asList(input.split(","));
            t.number = Integer.valueOf(param.get(0));
            t.name = param.get(1);
            t.school = param.get(2);
            t.city = param.get(3);
            t.state = param.get(4);
            t.country = param.get(5);
        } catch (Exception e) {
            Log.i("Team.readFromBR", "Error reading");
        }
        return t;
    }
    public static Comparator<Team> getComparator(SortParameter... sortParameters) {
        return new TeamComparator(sortParameters);
    }

    public enum SortParameter {
        NUMBER_SORT, NAME_SORT
    }

    private static class TeamComparator implements Comparator<Team> {
        private SortParameter[] parameters;

        private TeamComparator(SortParameter[] parameters) {
            this.parameters = parameters;
        }

        public int compare(Team o1, Team o2) {
            int comparison;
            for (SortParameter parameter: parameters) {
                switch (parameter) {
                    case NUMBER_SORT:
                        comparison = o1.number - o2.number;
                        if (comparison!=0) return comparison;
                        break;
                    case NAME_SORT:
                        comparison = o1.name.compareTo(o2.name);
                        if (comparison!=0) return comparison;
                        break;
                }
            }
            return 0;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o!=null && o instanceof TeamFtcRanked) {
            if (this.number == ((TeamFtcRanked) o).number) {
                return true;
            } else {
                return false;
            }
        }
        if (o!=null && o instanceof TeamStatRanked) {
            if (this.number == ((TeamStatRanked) o).number) {
                return true;
            } else {
                return false;
            }
        }
        if (o!=null && o instanceof Team) {
            if (this.number == ((Team) o).number) {
                return true;
            } else {
                return false;
            }
        }

        return false;
    }

}



