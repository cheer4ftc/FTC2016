package com.wrgardnersoft.ftc2016.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.wrgardnersoft.ftc2016.R;
import com.wrgardnersoft.ftc2016.interfaces.AsyncResponse;
import com.wrgardnersoft.ftc2016.internet.TournamentPagesTask;
import com.wrgardnersoft.ftc2016.models.MyApp;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;


public class SetupActivity extends ActionBarActivity implements AsyncResponse{

    private EditText[] serverAddressEditText = new EditText[4];
    private EditText tournamentCodeEditText;
    private String setupFileName = "FtcOnlineSetupInfo";

    TournamentPagesTask tournamentPagesTask;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(" " + getString(R.string.setup));
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_launcher);
        setContentView(R.layout.activity_setup);

        serverAddressEditText[0] = (EditText) findViewById(R.id.baseAddressEditText);
        serverAddressEditText[1] = (EditText) findViewById(R.id.rankingsPageEditText);
        serverAddressEditText[2] = (EditText) findViewById(R.id.matchPageEditText);

        tournamentCodeEditText = (EditText) findViewById(R.id.tournamentCodeEditText);

        MyApp myApp = (MyApp) getApplication();

        try {
            FileInputStream fi = openFileInput(setupFileName);
            InputStreamReader fr = new InputStreamReader(fi);
            BufferedReader br = new BufferedReader(fr);

            serverAddressEditText[0].setText(br.readLine());
            serverAddressEditText[1].setText(br.readLine());
            serverAddressEditText[2].setText(br.readLine());
            tournamentCodeEditText.setText(br.readLine());

            fr.close();
        } catch (IOException e) {
            //           Log.i("Setup Activity", "Exception reading setup data");
            serverAddressEditText[0].setText(myApp.serverAddressString[0], TextView.BufferType.EDITABLE);
            serverAddressEditText[1].setText(myApp.serverAddressString[1], TextView.BufferType.EDITABLE);
            serverAddressEditText[2].setText(myApp.serverAddressString[2], TextView.BufferType.EDITABLE);
            tournamentCodeEditText.setText(myApp.tournamentCode, TextView.BufferType.EDITABLE);
        }

        myApp.setServerAddressString(0, String.valueOf(serverAddressEditText[0].getText()));
        myApp.setServerAddressString(1, String.valueOf(serverAddressEditText[1].getText()));
        myApp.setServerAddressString(2, String.valueOf(serverAddressEditText[2].getText()));
        myApp.tournamentCode = String.valueOf(tournamentCodeEditText.getText());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_exit_only, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_exit) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onClickEnterCodeButton(View view) {

        // save all setup info to globals in myApp class
        MyApp myApp = (MyApp) getApplication();

        String tournamentCode = String.valueOf(tournamentCodeEditText.getText());

        tournamentPagesTask = new TournamentPagesTask(this, tournamentCode);
        tournamentPagesTask.delegate = this;
        tournamentPagesTask.execute();

        // do this in an async task??
        /*
        try {
            String tournamentPagesUrl = "https://docs.google.com/spreadsheets/d/1fKt9F-Wh82Zdd-Xk3BZQB-IuZIjAxkpgxXTVh0mEG1A/gviz/tq?tqx=out:html&tq=SELECT%20F%2C%20G%2C%20H%20WHERE%20E%20CONTAINS%20%27"
                + tournamentCode + "%27&gid=2";
            Log.i("Tournament Code",tournamentCode);
            Log.i("Tournament Pages URL",tournamentPagesUrl);
            Document doc = Jsoup.connect(tournamentPagesUrl).get();
            Log.i("Checking","X");
            Element table = doc.select("table").get(0);
            Log.i("Checking","X");
            Elements rows = table.select("tr");
            Log.i("Checking","X");
            Element row = rows.get(1);
            Log.i("Checking","X");
            Elements cols = row.select("td");
            Log.i("Checking","X");

            serverAddressEditText[0].setText(cols.get(0).text());
            serverAddressEditText[1].setText(cols.get(1).text());
            serverAddressEditText[2].setText(cols.get(2).text());

            myApp.setServerAddressString(0, String.valueOf(serverAddressEditText[0].getText()));
            myApp.setServerAddressString(1, String.valueOf(serverAddressEditText[1].getText()));
            myApp.setServerAddressString(2, String.valueOf(serverAddressEditText[2].getText()));

        } catch (Exception e) {
            // should put a toast error message here
            Log.i("Reading tournament code","FAILED");
            return;
        }*/

    }

    public void processFinish(int result) {
        //this you will received result fired from async class of onPostExecute(result) method.
        MyApp myApp = (MyApp) getApplication();

        serverAddressEditText[0].setText(myApp.serverAddressString(0));
        serverAddressEditText[1].setText(myApp.serverAddressString(1));
        serverAddressEditText[2].setText(myApp.serverAddressString(2));

    }


    public void onClickSetupDoneButton(View view) {

        // save all setup info to globals in myApp class
        MyApp myApp = (MyApp) getApplication();

        myApp.setDualDivision(false);

        myApp.setDivision(0);

        myApp.setServerAddressString(0, String.valueOf(serverAddressEditText[0].getText()));
        myApp.setServerAddressString(1, String.valueOf(serverAddressEditText[1].getText()));
        myApp.setServerAddressString(2, String.valueOf(serverAddressEditText[2].getText()));
        myApp.tournamentCode=String.valueOf(tournamentCodeEditText.getText());

        try {
            this.deleteFile(setupFileName);
            FileOutputStream fOut = openFileOutput(setupFileName, MODE_PRIVATE);
            OutputStreamWriter fw = new OutputStreamWriter(fOut);

        /*    fw.write(serverAddressEditText[0].getText().toString() + "\n");
            fw.write(serverAddressEditText[1].getText().toString() + "\n");
            fw.write(serverAddressEditText[2].getText().toString() + "\n");*/
            fw.write(myApp.serverAddressString(0) + "\n");
            fw.write(myApp.serverAddressString(1) + "\n");
            fw.write(myApp.serverAddressString(2) + "\n");
            fw.write(myApp.tournamentCode+"\n");

            //        fw.write(myApp.serverAddressString[0] + "\n");
            //        fw.write(myApp.serverAddressString[1] + "\n");
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // jump to next activity
        Intent getNameScreenIntent = new Intent(this, FtcRankingsActivity.class);
        startActivity(getNameScreenIntent);
        //     finish();
    }
}
