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
import com.wrgardnersoft.ftc2016.interfaces.AsyncResponse;
import com.wrgardnersoft.ftc2016.R;
import com.wrgardnersoft.ftc2016.internet.ClientTask;

import java.util.ArrayList;


public class MyMatchActivity extends CommonMenuActivity implements AsyncResponse {

    ClientTask clientTask;

    public ArrayList<Match> myMatch;

    public MyMatchActivity() {
        this.myMatch = new ArrayList<>();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        MyApp myApp = (MyApp) getApplication();

        for (Match m : myApp.match[myApp.division()]) {

            if (m.number == myApp.currentMatchNumber) {
                Match mm = new Match(m);
                myMatch.add(mm);
            }
            //          Log.i("Size of match find", String.valueOf(myMatch.size()));
        }


        setTitle(" Match: " + myMatch.get(0).title);
        setContentView(R.layout.activity_my_match);

        inflateMe();
    }

    public void processFinish(int result) {
        //this you will received result fired from async class of onPostExecute(result) method.
        MyApp myApp = MyApp.getInstance();

        myMatch.clear();

        for (Match m : myApp.match[myApp.division()]) {
            if (m.number == myApp.currentMatchNumber) {
                myMatch.add(m);
            }
            //          Log.i("Size of match find", String.valueOf(myMatch.size()));
        }
        inflateMe();

    }

    private void inflateMe() {
        TextView tv;

        MyApp myApp = (MyApp) getApplication();
        Match mm = myMatch.get(0);

        if ((mm.score[MyApp.RED][MyApp.ScoreType.TOTAL.ordinal()] < 0) && (myApp.enableMatchPrediction)) {
            mm.predict();
        }

        if (mm.predicted) {
            tv = (TextView) findViewById(R.id.my_match_title);
            tv.setText(getString(R.string.predictedResult) + " ");
            tv.setTypeface(null, Typeface.BOLD_ITALIC);
        } else if (mm.score[MyApp.RED][MyApp.ScoreType.TOTAL.ordinal()] < 0) {
            tv = (TextView) findViewById(R.id.my_match_title);
            tv.setText(getString(R.string.noResultYet));
            tv.setTypeface(null, Typeface.NORMAL);
        }

        tv = (TextView) findViewById(R.id.mm_rT0);
        tv.setText(String.valueOf(mm.teamNumber[MyApp.RED][0]));
        if (myApp.selectedTeams.contains(mm.teamNumber[MyApp.RED][0])) {
            tv.setBackgroundResource(R.color.yellow);
        } else {
            tv.setBackgroundResource(R.color.lighter_red);
        }
        tv = (TextView) findViewById(R.id.mm_rT1);
        tv.setText(String.valueOf(mm.teamNumber[MyApp.RED][1]));
        if (myApp.selectedTeams.contains(mm.teamNumber[MyApp.RED][1])) {
            tv.setBackgroundResource(R.color.yellow);
        } else {
            tv.setBackgroundResource(R.color.lighter_red);
        }
        tv = (TextView) findViewById(R.id.mm_rT2);
        if (mm.teamNumber[MyApp.RED][2] > 0) {
            TextView tv2 = (TextView) findViewById(R.id.mm_rT0);
            LinearLayout.LayoutParams param2 = (LinearLayout.LayoutParams) tv2.getLayoutParams();
            LinearLayout.LayoutParams param = (LinearLayout.LayoutParams) tv.getLayoutParams();
            param.height = param2.height;
            tv.setLayoutParams(param);
            tv.setText(String.valueOf(mm.teamNumber[MyApp.RED][2]));
            if (myApp.selectedTeams.contains(mm.teamNumber[MyApp.RED][2])) {
                tv.setBackgroundResource(R.color.yellow);
            } else {
                tv.setBackgroundResource(R.color.lighter_red);
            }
        } else {
            tv.setText("");
            tv.setBackgroundResource(R.color.lighter_red);
            LinearLayout.LayoutParams param = (LinearLayout.LayoutParams) tv.getLayoutParams();
            param.height = 0;
            tv.setLayoutParams(param);
        }

        tv = (TextView) findViewById(R.id.mm_bT0);
        tv.setText(String.valueOf(mm.teamNumber[MyApp.BLUE][0]));
        if (myApp.selectedTeams.contains(mm.teamNumber[MyApp.BLUE][0])) {
            tv.setBackgroundResource(R.color.yellow);
        } else {
            tv.setBackgroundResource(R.color.lighter_blue);
        }
        tv = (TextView) findViewById(R.id.mm_bT1);
        tv.setText(String.valueOf(mm.teamNumber[MyApp.BLUE][1]));
        if (myApp.selectedTeams.contains(mm.teamNumber[MyApp.BLUE][1])) {
            tv.setBackgroundResource(R.color.yellow);
        } else {
            tv.setBackgroundResource(R.color.lighter_blue);
        }
        tv = (TextView) findViewById(R.id.mm_bT2);
        if (mm.teamNumber[MyApp.BLUE][2] > 0) {
            TextView tv2 = (TextView) findViewById(R.id.mm_bT0);
            LinearLayout.LayoutParams param2 = (LinearLayout.LayoutParams) tv2.getLayoutParams();
            LinearLayout.LayoutParams param = (LinearLayout.LayoutParams) tv.getLayoutParams();
            param.height = param2.height;
            tv.setLayoutParams(param);
            tv.setText(String.valueOf(mm.teamNumber[MyApp.BLUE][2]));
            if (myApp.selectedTeams.contains(mm.teamNumber[MyApp.BLUE][2])) {
                tv.setBackgroundResource(R.color.yellow);
            } else {
                tv.setBackgroundResource(R.color.lighter_blue);
            }
        } else {
            tv.setText("");
            tv.setBackgroundResource(R.color.lighter_blue);
            LinearLayout.LayoutParams param = (LinearLayout.LayoutParams) tv.getLayoutParams();
            param.height = 0;
            tv.setLayoutParams(param);
        }

        int tvId[][] = {{R.id.mm_rTot, R.id.mm_rAuto, R.id.mm_rAutoB, R.id.mm_rTele, R.id.mm_rEndG, R.id.mm_rPen},
                {R.id.mm_bTot, R.id.mm_bAuto, R.id.mm_bAutoB, R.id.mm_bTele, R.id.mm_bEndG, R.id.mm_bPen}};

        if ((mm.score[MyApp.RED][MyApp.ScoreType.TOTAL.ordinal()] >= 0) || (mm.predicted)) {
            if (mm.predicted) {
                tv = (TextView) findViewById(R.id.mm_rTot);
                tv.setTypeface(null, Typeface.ITALIC);
                tv = (TextView) findViewById(R.id.mm_bTot);
                tv.setTypeface(null, Typeface.ITALIC);
            } else {
                tv = (TextView) findViewById(R.id.mm_rTot);
                tv.setTypeface(null, Typeface.BOLD);
                tv = (TextView) findViewById(R.id.mm_bTot);
                tv.setTypeface(null, Typeface.BOLD);
                if (mm.score[MyApp.RED][MyApp.ScoreType.TOTAL.ordinal()] >
                        mm.score[MyApp.BLUE][MyApp.ScoreType.TOTAL.ordinal()]) {
                    tv = (TextView) findViewById(R.id.mm_rTot);
                    tv.setBackgroundResource(R.drawable.red_border);
                } else {
                    tv = (TextView) findViewById(R.id.mm_rTot);
                    tv.setBackgroundResource(R.drawable.no_border_red);
                }

                if (mm.score[MyApp.RED][MyApp.ScoreType.TOTAL.ordinal()] <
                        mm.score[MyApp.BLUE][MyApp.ScoreType.TOTAL.ordinal()]) {
                    tv = (TextView) findViewById(R.id.mm_bTot);
                    tv.setBackgroundResource(R.drawable.blue_border);
                } else {
                    tv = (TextView) findViewById(R.id.mm_bTot);
                    tv.setBackgroundResource(R.drawable.no_border_blue);
                }
            }

            for (int i = 0; i < MyApp.NUM_ALLIANCES; i++) {
                for (int j = 0; j < MyApp.NUM_SCORE_TYPES; j++) {
                    tv = (TextView) findViewById(tvId[i][j]);
                    tv.setText(String.format("%.0f", mm.score[i][j]));
                    if (mm.predicted) {
                        tv.setTypeface(null, Typeface.ITALIC);
                    }
                }
            }
        } else {
            tv = (TextView) findViewById(R.id.mm_rTot);
            tv.setBackgroundResource(R.drawable.no_border_red);

            tv = (TextView) findViewById(R.id.mm_bTot);
            tv.setBackgroundResource(R.drawable.no_border_blue);

            for (int i = 0; i < MyApp.NUM_ALLIANCES; i++) {
                for (int j = 0; j < MyApp.NUM_SCORE_TYPES; j++) {
                    tv = (TextView) findViewById(tvId[i][j]);
                    tv.setText("");
                }
            }
        }

        TextView alR0View = (TextView) findViewById(R.id.mm_rT0);
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

        TextView alR1View = (TextView) findViewById(R.id.mm_rT1);
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

        TextView alR2View = (TextView) findViewById(R.id.mm_rT2);
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

        TextView alB0View = (TextView) findViewById(R.id.mm_bT0);
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


        TextView alB1View = (TextView) findViewById(R.id.mm_bT1);
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


        TextView alB2View = (TextView) findViewById(R.id.mm_bT2);
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

        LinearLayout tableView = (LinearLayout) findViewById(R.id.tableLayout);
        tableView.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View v) {
                                           if (MyApp.getInstance().enableMatchPrediction) {
                                               Intent getNameScreenIntent = new Intent(v.getContext(),
                                                       MyDetailedMatchActivity.class);
                                               startActivity(getNameScreenIntent);
                                           }
                                       }
                                   }
        );

    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setContentView(R.layout.activity_my_match);

        inflateMe();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        boolean saveReturn;

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            clientTask = new ClientTask(this);
            clientTask.delegate = this;
            clientTask.execute();
            return true;
        }
        saveReturn = super.onOptionsItemSelected(item);

        if ((id == R.id.action_load) && saveReturn) { // just loaded data, so refresh
            processFinish(0);
        }

        return saveReturn;
    }

}

