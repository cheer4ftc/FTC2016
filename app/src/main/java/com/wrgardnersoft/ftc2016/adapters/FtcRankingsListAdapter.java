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
import com.wrgardnersoft.ftc2016.models.TeamFtcRanked;
import com.wrgardnersoft.ftc2016.R;

import java.util.ArrayList;

/**
 *   Created by Bill on 2/3/2015.
 */
public class FtcRankingsListAdapter extends ArrayAdapter<TeamFtcRanked> {
    Context context;
    int layoutResourceId;
    ArrayList<TeamFtcRanked> team;

    public FtcRankingsListAdapter(Context context, int resource, ArrayList<TeamFtcRanked> teamData) {
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

    public TeamFtcRanked getItem(int position) {
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
            holder.rankView = (TextView) row.findViewById(R.id.rank);
            holder.numView = (TextView) row.findViewById(R.id.number);
            holder.nameView = (TextView) row.findViewById(R.id.name);
            holder.qpView = (TextView) row.findViewById(R.id.qp);
            holder.rpView = (TextView) row.findViewById(R.id.rp);
            holder.mpView = (TextView) row.findViewById(R.id.mp);
            holder.highestView = (TextView) row.findViewById(R.id.highest);

            row.setTag(holder);
        } else {
            holder = (TeamHolder) row.getTag();
        }

        TeamFtcRanked thisTeam = team.get(position);

        MyApp myApp = MyApp.getInstance();

        if (myApp.selectedTeams.contains(thisTeam.number)) {
            row.setBackgroundColor(Color.YELLOW);
        } else {
            row.setBackgroundColor(Color.WHITE);
        }
   //     Log.i("Ftc adapter", "got here");
        holder.rankView.setText("#"+String.format("%d", thisTeam.rank));
        holder.numView.setText(String.format("%5d",thisTeam.number));
        holder.nameView.setText(thisTeam.name);
        holder.qpView.setText(String.format("%d",thisTeam.qp));
        holder.rpView.setText(String.format("%d", thisTeam.rp));
        holder.mpView.setText(String.format("%d", thisTeam.matches));
        holder.highestView.setText(String.format("%d",thisTeam.highest) );
        return row;

    }

    static class TeamHolder {
        TextView rankView, numView, nameView, qpView, rpView, mpView, highestView;
    }
}

