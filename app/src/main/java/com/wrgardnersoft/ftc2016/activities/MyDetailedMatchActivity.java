package com.wrgardnersoft.ftc2016.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wrgardnersoft.ftc2016.models.Match;
import com.wrgardnersoft.ftc2016.models.MyApp;
import com.wrgardnersoft.ftc2016.models.Stat;
import com.wrgardnersoft.ftc2016.interfaces.AsyncResponse;
import com.wrgardnersoft.ftc2016.models.DetailedMatch;
import com.wrgardnersoft.ftc2016.views.VerticalTextView;
import com.wrgardnersoft.ftc2016.R;
import com.wrgardnersoft.ftc2016.internet.ClientTask;

import java.util.ArrayList;

/**
 * Created by Bill on 2/11/2015.
 */
public class MyDetailedMatchActivity extends CommonMenuActivity implements AsyncResponse {

    ClientTask clientTask;

    public ArrayList<Match> myMatch;
    public ArrayList<DetailedMatch> myDetailedMatch;
    public Match mmPartial[];

    Stat.Type displayType;

    public MyDetailedMatchActivity() {

        this.myMatch = new ArrayList<>();
        this.myDetailedMatch = new ArrayList<>();
        this.mmPartial = new Match[MyApp.TEAMS_PER_ALLIANCE];
        for (int i = 0; i < MyApp.TEAMS_PER_ALLIANCE; i++) {
            this.mmPartial[i] = new Match();
        }
        this.displayType = Stat.Type.OPR;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        MyApp myApp = (MyApp) getApplication();

        for (Match m : myApp.match[myApp.division()]) {

            if (m.number == myApp.currentMatchNumber) {
                Match mm = new Match(m);
                myMatch.add(mm);
                for (int i = 0; i < MyApp.TEAMS_PER_ALLIANCE; i++) {
                    this.mmPartial[i] = mm;
                }
                myDetailedMatch.add(new DetailedMatch(this.mmPartial));
            }
            //          Log.i("Size of match find", String.valueOf(myMatch.size()));
        }


        setTitle(" Match: " + myMatch.get(0).title);
        setContentView(R.layout.activity_my_detailed_match);

        inflateMe();
    }

    public void processFinish(int result) {
        //this you will received result fired from async class of onPostExecute(result) method.
        MyApp myApp = MyApp.getInstance();

        myMatch.clear();
        myDetailedMatch.clear();

        for (Match mm : myApp.match[myApp.division()]) {
            if (mm.number == myApp.currentMatchNumber) {
                myMatch.add(mm);
                for (int i = 0; i < MyApp.TEAMS_PER_ALLIANCE; i++) {
                    this.mmPartial[i] = mm;
                }
                myDetailedMatch.add(new DetailedMatch(this.mmPartial));
            }
            //          Log.i("Size of match find", String.valueOf(myMatch.size()));
        }
        inflateMe();

    }

    private void inflateMe() {
        TextView tv;

        MyApp myApp = (MyApp) getApplication();

        Match mm = myMatch.get(0);
        DetailedMatch mdm = myDetailedMatch.get(0);

        int tvTeamId[][] = {
                {R.id.mdm_rT0, R.id.mdm_rT1, R.id.mdm_rT2},
                {R.id.mdm_bT0, R.id.mdm_bT1, R.id.mdm_bT2}
        };
        int tvScoreId[][] = {
                {R.id.mdm_r0_s0, R.id.mdm_r1_s0, R.id.mdm_r2_s0, R.id.mdm_b0_s0, R.id.mdm_b1_s0, R.id.mdm_b2_s0},
                {R.id.mdm_r0_s1, R.id.mdm_r1_s1, R.id.mdm_r2_s1, R.id.mdm_b0_s1, R.id.mdm_b1_s1, R.id.mdm_b2_s1},
                {R.id.mdm_r0_s2, R.id.mdm_r1_s2, R.id.mdm_r2_s2, R.id.mdm_b0_s2, R.id.mdm_b1_s2, R.id.mdm_b2_s2},
                {R.id.mdm_r0_s3, R.id.mdm_r1_s3, R.id.mdm_r2_s3, R.id.mdm_b0_s3, R.id.mdm_b1_s3, R.id.mdm_b2_s3},
                {R.id.mdm_r0_s4, R.id.mdm_r1_s4, R.id.mdm_r2_s4, R.id.mdm_b0_s4, R.id.mdm_b1_s4, R.id.mdm_b2_s4},
                {R.id.mdm_r0_s5, R.id.mdm_r1_s5, R.id.mdm_r2_s5, R.id.mdm_b0_s5, R.id.mdm_b1_s5, R.id.mdm_b2_s5}
        };

        mm.predicted = true;
        int teamsPerAllianceInThisMatch = 3;
        if (mm.teamNumber[MyApp.RED][2] <= 0) {
            teamsPerAllianceInThisMatch = 2;
        }

        for (int k = 0; k < teamsPerAllianceInThisMatch; k++) {
            mdm.m[k].predictFromTeamPosition(k, this.displayType);
        }

        tv = (TextView) findViewById(R.id.my_detailed_match_title);
        tv.setText(getString(R.string.teamBreakdown) + ": " + Stat.TypeDisplayString[displayType.ordinal()] + " ");
        tv.setTypeface(null, Typeface.BOLD_ITALIC);

        for (int k = 0; k < teamsPerAllianceInThisMatch; k++) {
            for (int i = 0; i < MyApp.NUM_ALLIANCES; i++) {
                TextView vtv = (TextView) findViewById(tvTeamId[i][k]);

                vtv.setText(String.valueOf(mm.teamNumber[i][k]));
                if (myApp.selectedTeams.contains(mm.teamNumber[i][k])) {
                    vtv.setBackgroundResource(R.color.yellow);
                } else {
                    if (i == MyApp.RED) {
                        vtv.setBackgroundResource(R.color.lighter_red);
                    } else {
                        vtv.setBackgroundResource(R.color.lighter_blue);
                    }
                }
                VerticalTextView vtv2 = (VerticalTextView) findViewById(tvTeamId[i][0]);
                LinearLayout.LayoutParams param2 = (LinearLayout.LayoutParams) vtv2.getLayoutParams();
                LinearLayout.LayoutParams param = (LinearLayout.LayoutParams) vtv.getLayoutParams();
                param.width = param2.width;
                vtv.setLayoutParams(param);
            }
        }

        for (int k = 0; k < teamsPerAllianceInThisMatch; k++) {
            for (int i = 0; i < MyApp.NUM_ALLIANCES; i++) {
                for (int j = 0; j < MyApp.NUM_SCORE_TYPES; j++) {
                    tv = (TextView) findViewById(tvScoreId[j][i * MyApp.TEAMS_PER_ALLIANCE + k]);
                    tv.setText(String.format("%.0f", mdm.m[k].score[i][j]));


                    TextView tv2 = (TextView) findViewById(tvScoreId[j][i * MyApp.TEAMS_PER_ALLIANCE]);
                    LinearLayout.LayoutParams param2 = (LinearLayout.LayoutParams) tv2.getLayoutParams();
                    LinearLayout.LayoutParams param = (LinearLayout.LayoutParams) tv.getLayoutParams();
                    param.width = param2.width;
                    tv.setLayoutParams(param);
                }
            }
        }
        TextView alR0View = (TextView) findViewById(R.id.mdm_rT0);
        alR0View.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            TextView tv = (TextView) v;
                                            MyApp myApp = MyApp.getInstance();
                                            myApp.currentTeamNumber = Integer.parseInt(tv.getText().toString());

                                            Intent getNameScreenIntent = new Intent(v.getContext(), MyTeamActivity.class);
                                            v.getContext().startActivity(getNameScreenIntent);
                                        }
                                    }
        );

        TextView alR1View = (TextView) findViewById(R.id.mdm_rT1);
        alR1View.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            TextView tv = (TextView) v;
                                            MyApp myApp = MyApp.getInstance();
                                            myApp.currentTeamNumber = Integer.parseInt(tv.getText().toString());

                                            Intent getNameScreenIntent = new Intent(v.getContext(), MyTeamActivity.class);
                                            v.getContext().startActivity(getNameScreenIntent);
                                        }
                                    }
        );

        TextView alR2View = (TextView) findViewById(R.id.mdm_rT2);
        alR2View.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            TextView tv = (TextView) v;
                                            MyApp myApp = MyApp.getInstance();
                                            myApp.currentTeamNumber = Integer.parseInt(tv.getText().toString());

                                            Intent getNameScreenIntent = new Intent(v.getContext(), MyTeamActivity.class);
                                            v.getContext().startActivity(getNameScreenIntent);
                                        }
                                    }
        );

        TextView alB0View = (TextView) findViewById(R.id.mdm_bT0);
        alB0View.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            TextView tv = (TextView) v;
                                            MyApp myApp = MyApp.getInstance();
                                            myApp.currentTeamNumber = Integer.parseInt(tv.getText().toString());

                                            Intent getNameScreenIntent = new Intent(v.getContext(), MyTeamActivity.class);
                                            v.getContext().startActivity(getNameScreenIntent);
                                        }
                                    }
        );


        TextView alB1View = (TextView) findViewById(R.id.mdm_bT1);
        alB1View.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            TextView tv = (TextView) v;
                                            MyApp myApp = MyApp.getInstance();
                                            myApp.currentTeamNumber = Integer.parseInt(tv.getText().toString());

                                            Intent getNameScreenIntent = new Intent(v.getContext(), MyTeamActivity.class);
                                            v.getContext().startActivity(getNameScreenIntent);
                                        }
                                    }
        );


        TextView alB2View = (TextView) findViewById(R.id.mdm_bT2);
        alB2View.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            TextView tv = (TextView) v;
                                            MyApp myApp = MyApp.getInstance();
                                            myApp.currentTeamNumber = Integer.parseInt(tv.getText().toString());

                                            Intent getNameScreenIntent = new Intent(v.getContext(), MyTeamActivity.class);
                                            v.getContext().startActivity(getNameScreenIntent);
                                        }
                                    }
        );
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setContentView(R.layout.activity_my_detailed_match);

        inflateMe();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        boolean saveReturn;

        //MyApp myApp = MyApp.getInstance();

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            clientTask = new ClientTask(this);
            clientTask.delegate = this;
            clientTask.execute();
            return true;
        }
        saveReturn =  super.onOptionsItemSelected(item);

        if ((id == R.id.action_load)&&saveReturn) { // just loaded data, so refresh
            processFinish(0);
        }

        return saveReturn;
    }

    public void onClickMdmOprButton(View view) {

        displayType = Stat.Type.OPR;
        inflateMe();
    }

    public void onClickMdmDprButton(View view) {

        displayType = Stat.Type.DPR;
        inflateMe();
    }

    public void onClickMdmCcwmButton(View view) {

        displayType = Stat.Type.CCWM;
        inflateMe();
    }


}


