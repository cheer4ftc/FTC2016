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
import com.wrgardnersoft.ftc2016.models.Team;
import com.wrgardnersoft.ftc2016.R;

import java.util.ArrayList;

/**
 * Created by Bill on 2/3/2015.
 */
public class TeamListAdapter extends ArrayAdapter<Team> {
    Context context;
    int layoutResourceId;
    ArrayList<Team> team;

    public TeamListAdapter(Context context, int resource, ArrayList<Team> teamData) {
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

    public Team getItem(int position) {
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
            holder.numView = (TextView) row.findViewById(R.id.team_number);
            holder.nameView = (TextView) row.findViewById(R.id.team_name);
            holder.schoolView = (TextView) row.findViewById(R.id.team_school);
            holder.locView = (TextView) row.findViewById(R.id.team_location);

            row.setTag(holder);
        } else {
            holder = (TeamHolder) row.getTag();
        }

        Team thisTeam = team.get(position);

        MyApp myApp = MyApp.getInstance();

        if (myApp.selectedTeams.contains(thisTeam.number)) {
            row.setBackgroundColor(Color.YELLOW);
        } else {
            row.setBackgroundColor(Color.WHITE);
        }
        holder.numView.setText(Integer.toString(thisTeam.number));
        holder.nameView.setText(thisTeam.name);
        holder.schoolView.setText(thisTeam.school);
        holder.locView.setText(thisTeam.city + ", " +
                thisTeam.state + ", " +
                thisTeam.country);
        return row;

    }

    static class TeamHolder {
        TextView numView, nameView, schoolView, locView;
    }
}
