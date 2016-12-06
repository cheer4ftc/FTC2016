package com.wrgardnersoft.ftc2016.models;

import android.util.Log;

import java.io.BufferedReader;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Bill on 2/3/2015.
 */
public class TeamFtcRanked {
    public int rank,number;
    public String name;
    public int qp,rp,highest,matches;

    public TeamFtcRanked() {
        this.rank=0;
        this.number=0;
        this.name="";
        this.qp=0;
        this.rp=0;
        this.highest=0;
        this.matches=0;
    }
    public TeamFtcRanked(int rank,int num,String sName,int qp,int rp,int highest,int matches) {
        this.rank=rank;
        this.number=num;
        this.name=sName;
        this.qp=qp;
        this.rp=rp;
        this.highest=highest;
        this.matches=matches;
    }
    public static String shareHeader() {
        String output;
        output = "Rank,Number,Name,QP,RP,Highest,Matches"+ System.getProperty("line.separator");
        return output;
    }
    public String toString() {
        String output;
        output = this.rank + "," + this.number + "," + this.name + "," + this.qp + "," + this.rp + "," + this.highest + "," + this.matches + System.getProperty("line.separator");
        return output;
    }
    public static TeamFtcRanked readFromBR(BufferedReader fr) {
        TeamFtcRanked t = new TeamFtcRanked();
        try {
            String input = fr.readLine();
            List<String> param = Arrays.asList(input.split(","));
            t.rank = Integer.valueOf(param.get(0));
            t.number = Integer.valueOf(param.get(1));
            t.name = param.get(2);
            t.qp = Integer.valueOf(param.get(3));
            t.rp = Integer.valueOf(param.get(4));
            t.highest = Integer.valueOf(param.get(5));
            t.matches = Integer.valueOf(param.get(6));
        } catch (Exception e) {
            Log.i("TeamFtcRanked.readFromBR", "Error reading");
        }
        return t;
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

