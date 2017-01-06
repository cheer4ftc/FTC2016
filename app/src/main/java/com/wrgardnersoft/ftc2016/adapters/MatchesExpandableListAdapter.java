package com.wrgardnersoft.ftc2016.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wrgardnersoft.ftc2016.activities.MyTeamActivity;
import com.wrgardnersoft.ftc2016.models.Match;
import com.wrgardnersoft.ftc2016.models.MyApp;
import com.wrgardnersoft.ftc2016.R;

import java.util.HashMap;
import java.util.List;

public class MatchesExpandableListAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<Match>> _listDataChild;
    private int boldTeam;

    public MatchesExpandableListAdapter(Context context, List<String> listDataHeader,
                                        HashMap<String, List<Match>> listChildData, int boldTeam) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
        this.boldTeam=boldTeam;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        View row = convertView;
        MatchHolder holder;

//        final String childText = (String) getChild(groupPosition, childPosition);
        final Match child = (Match) getChild(groupPosition, childPosition);

        if (row == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = infalInflater.inflate(R.layout.list_item_match, null);

            holder = new MatchHolder();
            holder.titleView = (TextView) row.findViewById(R.id.match_title);
            holder.red0View = (TextView) row.findViewById(R.id.red1);
            holder.red1View = (TextView) row.findViewById(R.id.red2);
            holder.red2View = (TextView) row.findViewById(R.id.red3);
            holder.blue0View = (TextView) row.findViewById(R.id.blue1);
            holder.blue1View = (TextView) row.findViewById(R.id.blue2);
            holder.blue2View = (TextView) row.findViewById(R.id.blue3);
            holder.redTotView = (TextView) row.findViewById(R.id.red_score);
            holder.blueTotView = (TextView) row.findViewById(R.id.blue_score);

            row.setTag(holder);
        } else {
            holder = (MatchHolder) row.getTag();
        }

        holder.titleView.setText(child.title);
        holder.red0View.setText(String.valueOf(child.teamNumber[MyApp.RED][0]));
        holder.red1View.setText(String.valueOf(child.teamNumber[MyApp.RED][1]));
        holder.red2View.setText(String.valueOf(child.teamNumber[MyApp.RED][2]));
        holder.blue0View.setText(String.valueOf(child.teamNumber[MyApp.BLUE][0]));
        holder.blue1View.setText(String.valueOf(child.teamNumber[MyApp.BLUE][1]));
        holder.blue2View.setText(String.valueOf(child.teamNumber[MyApp.BLUE][2]));

        if (child.teamNumber[MyApp.RED][0] == boldTeam) {
            holder.red0View.setTypeface(null,Typeface.BOLD);
        } else {
            holder.red0View.setTypeface(null,Typeface.NORMAL);
        }
        if (child.teamNumber[MyApp.RED][1] == boldTeam) {
            holder.red1View.setTypeface(null,Typeface.BOLD);
        } else {
            holder.red1View.setTypeface(null,Typeface.NORMAL);
        }
        if (child.teamNumber[MyApp.RED][2] == boldTeam) {
            holder.red2View.setTypeface(null,Typeface.BOLD);
        } else {
            holder.red2View.setTypeface(null,Typeface.NORMAL);
        }

        if (child.teamNumber[MyApp.BLUE][0] == boldTeam) {
            holder.blue0View.setTypeface(null,Typeface.BOLD);
        } else {
            holder.blue0View.setTypeface(null,Typeface.NORMAL);
        }
        if (child.teamNumber[MyApp.BLUE][1] == boldTeam) {
            holder.blue1View.setTypeface(null,Typeface.BOLD);
        } else {
            holder.blue1View.setTypeface(null,Typeface.NORMAL);
        }
        if (child.teamNumber[MyApp.BLUE][2] == boldTeam) {
            holder.blue2View.setTypeface(null,Typeface.BOLD);
        } else {
            holder.blue2View.setTypeface(null,Typeface.NORMAL);
        }

        final MyApp myApp = MyApp.getInstance();

        holder.red0View.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                myApp.currentTeamNumber = child.teamNumber[MyApp.RED][0];

                Intent getNameScreenIntent = new Intent(v.getContext(), MyTeamActivity.class);
                v.getContext().startActivity(getNameScreenIntent);
            }
        });
        holder.red1View.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                myApp.currentTeamNumber = child.teamNumber[MyApp.RED][1];

                Intent getNameScreenIntent = new Intent(v.getContext(), MyTeamActivity.class);
                v.getContext().startActivity(getNameScreenIntent);
            }
        });
        holder.red2View.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                myApp.currentTeamNumber = child.teamNumber[MyApp.RED][2];

                Intent getNameScreenIntent = new Intent(v.getContext(), MyTeamActivity.class);
                v.getContext().startActivity(getNameScreenIntent);
            }
        });

        holder.blue0View.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                myApp.currentTeamNumber = child.teamNumber[MyApp.BLUE][0];

                Intent getNameScreenIntent = new Intent(v.getContext(), MyTeamActivity.class);
                v.getContext().startActivity(getNameScreenIntent);
            }
        });
        holder.blue1View.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                myApp.currentTeamNumber = child.teamNumber[MyApp.BLUE][1];

                Intent getNameScreenIntent = new Intent(v.getContext(), MyTeamActivity.class);
                v.getContext().startActivity(getNameScreenIntent);
            }
        });
        holder.blue2View.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                myApp.currentTeamNumber = child.teamNumber[MyApp.BLUE][2];

                Intent getNameScreenIntent = new Intent(v.getContext(), MyTeamActivity.class);
                v.getContext().startActivity(getNameScreenIntent);
            }
        });


        if (myApp.selectedTeams.contains(child.teamNumber[MyApp.RED][0])) {
            holder.red0View.setBackgroundResource(R.color.yellow);
        } else {
            holder.red0View.setBackgroundResource(R.color.lighter_red);
        }
        if (myApp.selectedTeams.contains(child.teamNumber[MyApp.RED][1])) {
            holder.red1View.setBackgroundResource(R.color.yellow);
        } else {
            holder.red1View.setBackgroundResource(R.color.lighter_red);
        }
        if (myApp.selectedTeams.contains(child.teamNumber[MyApp.RED][2])) {
            holder.red2View.setBackgroundResource(R.color.yellow);
        } else {
            holder.red2View.setBackgroundResource(R.color.lighter_red);
        }

        if (myApp.selectedTeams.contains(child.teamNumber[MyApp.BLUE][0])) {
            holder.blue0View.setBackgroundResource(R.color.yellow);
        } else {
            holder.blue0View.setBackgroundResource(R.color.lighter_blue);
        }
        if (myApp.selectedTeams.contains(child.teamNumber[MyApp.BLUE][1])) {
            holder.blue1View.setBackgroundResource(R.color.yellow);
        } else {
            holder.blue1View.setBackgroundResource(R.color.lighter_blue);
        }
        if (myApp.selectedTeams.contains(child.teamNumber[MyApp.BLUE][2])) {
            holder.blue2View.setBackgroundResource(R.color.yellow);
        } else {
            holder.blue2View.setBackgroundResource(R.color.lighter_blue);
        }
        if (child.teamNumber[MyApp.RED][2] > 0) {
            LinearLayout.LayoutParams param = (LinearLayout.LayoutParams) holder.red2View.getLayoutParams();
            param.weight = 1;
            holder.red2View.setLayoutParams(param);
            param = (LinearLayout.LayoutParams) holder.blue2View.getLayoutParams();
            param.weight = 1;
            holder.blue2View.setLayoutParams(param);
        } else {
            LinearLayout.LayoutParams param = (LinearLayout.LayoutParams) holder.red2View.getLayoutParams();
            param.weight = 0;
            holder.red2View.setLayoutParams(param);
            param = (LinearLayout.LayoutParams) holder.blue2View.getLayoutParams();
            param.weight = 0;
            holder.blue2View.setLayoutParams(param);
        }


        holder.redTotView.setBackgroundResource(R.drawable.no_border_red);
        holder.blueTotView.setBackgroundResource(R.drawable.no_border_blue);
//Log.i("Adapter", String.valueOf(child.score[MyApp.RED][MyApp.ScoreType.TOTAL.ordinal()]));
        if ((child.score[MyApp.RED][MyApp.ScoreType.TOTAL.ordinal()] >= 0)||(child.predicted)) {


            if (child.predicted) {
                holder.redTotView.setText(String.format("%3.0f: %2.0f%%", child.score[MyApp.RED][MyApp.ScoreType.TOTAL.ordinal()], 100*child.probRedWin));
                holder.blueTotView.setText(String.format("%3.0f: %2.0f%%", child.score[MyApp.BLUE][MyApp.ScoreType.TOTAL.ordinal()], 100*(1.0-child.probRedWin)));
                holder.redTotView.setTypeface(null, Typeface.ITALIC);
                holder.blueTotView.setTypeface(null, Typeface.ITALIC);
            } else {
                holder.redTotView.setText(String.format("%3.0f", child.score[MyApp.RED][MyApp.ScoreType.TOTAL.ordinal()]));
                holder.blueTotView.setText(String.format("%3.0f", child.score[MyApp.BLUE][MyApp.ScoreType.TOTAL.ordinal()]));
                holder.redTotView.setTypeface(null, Typeface.BOLD);
                holder.blueTotView.setTypeface(null, Typeface.BOLD);
                if (child.score[MyApp.RED][MyApp.ScoreType.TOTAL.ordinal()] > child.score[MyApp.BLUE][MyApp.ScoreType.TOTAL.ordinal()]) {
                    holder.redTotView.setBackgroundResource(R.drawable.red_border);
                } else if (child.score[MyApp.RED][MyApp.ScoreType.TOTAL.ordinal()] < child.score[MyApp.BLUE][MyApp.ScoreType.TOTAL.ordinal()]) {
                    holder.blueTotView.setBackgroundResource(R.drawable.blue_border);
                }
            }
        } else {
            holder.redTotView.setText("");
            holder.blueTotView.setText("");
        }

        return row;
    }


    static class MatchHolder {
        TextView titleView, red0View, red1View, red2View, blue0View, blue1View, blue2View, redTotView, blueTotView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.group_header_matches, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.matches_group_header);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


}

