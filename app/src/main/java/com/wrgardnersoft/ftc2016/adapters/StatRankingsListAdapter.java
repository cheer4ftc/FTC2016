package com.wrgardnersoft.ftc2016.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.wrgardnersoft.ftc2016.models.MyApp;
import com.wrgardnersoft.ftc2016.models.TeamStatRanked;
import com.wrgardnersoft.ftc2016.R;

import java.util.ArrayList;

/**
 *   Created by Bill on 2/3/2015.
 */
public class StatRankingsListAdapter extends ArrayAdapter<TeamStatRanked> {
    Context context;
    int layoutResourceId;
    ArrayList<TeamStatRanked> team;

    public StatRankingsListAdapter(Context context, int resource, ArrayList<TeamStatRanked> teamData) {
        super(context, resource, teamData);
        this.layoutResourceId = resource;
        this.context = context;
        this.team = teamData;
    }

    /**
     * ***** What is the size of Passed Arraylist Size ***********
     */
    public int getCount() {

        if (team.size() <= 0)
            return 1;
        return team.size();
    }

    public TeamStatRanked getItem(int position) {
        return team.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        TeamHolder holder;


        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new TeamHolder();
            holder.numView = (TextView) row.findViewById(R.id.number);
            holder.ftcRankView = (TextView) row.findViewById(R.id.ftcRank);
            holder.winPercentView = (TextView) row.findViewById(R.id.winPercent);
            holder.oprView = (TextView) row.findViewById(R.id.opr);
            holder.dprView = (TextView) row.findViewById(R.id.dpr);
            holder.ccwmView = (TextView) row.findViewById(R.id.ccwm);
            holder.nameView = (TextView) row.findViewById(R.id.name);

            row.setTag(holder);
        } else {
            holder = (TeamHolder) row.getTag();
        }

        TeamStatRanked thisTeam = team.get(position);

        MyApp myApp = MyApp.getInstance();

        if (myApp.selectedTeams.contains(thisTeam.number)) {
            row.setBackgroundColor(Color.YELLOW);
        } else {
            row.setBackgroundColor(Color.WHITE);
        }
   //     Log.i("Ftc adapter", "got here");
        holder.numView.setText(String.format("%5d",thisTeam.number));
        holder.ftcRankView.setText("#"+String.format("%d", thisTeam.ftcRank));
        holder.winPercentView.setText(String.format("%3.0f",thisTeam.winPercent));
        holder.oprView.setText(String.format("%3d",Math.round(thisTeam.oprA[MyApp.ScoreType.TOTAL.ordinal()])));
        holder.dprView.setText(String.format("%3d",Math.round(thisTeam.dprA[MyApp.ScoreType.TOTAL.ordinal()])));
        holder.ccwmView.setText(String.format("%3d",Math.round(thisTeam.ccwmA[MyApp.ScoreType.TOTAL.ordinal()])));
        holder.nameView.setText(thisTeam.name);

        return row;

    }

    static class TeamHolder {
        TextView numView, ftcRankView, winPercentView, oprView, dprView, ccwmView, nameView;
    }
}

