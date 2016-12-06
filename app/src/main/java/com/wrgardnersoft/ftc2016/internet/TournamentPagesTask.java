package com.wrgardnersoft.ftc2016.internet;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.wrgardnersoft.ftc2016.R;
import com.wrgardnersoft.ftc2016.interfaces.AsyncResponse;
import com.wrgardnersoft.ftc2016.models.MyApp;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Created by Bill on 2/7/2015.
 */
// AsyncTask
public class TournamentPagesTask extends AsyncTask<Void, Void, Void> {
    public AsyncResponse delegate;
    ProgressDialog mProgressDialog;
    boolean serverOK;
    private Context mContext;
    String tournamentCode;

    public TournamentPagesTask(Context context, String code) {
        mContext = context;
        tournamentCode = code;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setTitle(mContext.getString(R.string.app_name));
        mProgressDialog.setMessage("Loading pages data...");
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.show();
    }

    @Override
    protected Void doInBackground(Void... params) {
        serverOK = false;

        try {
            MyApp myApp = MyApp.getInstance();
            String tournamentPagesUrl = "https://docs.google.com/spreadsheets/d/1fKt9F-Wh82Zdd-Xk3BZQB-IuZIjAxkpgxXTVh0mEG1A/gviz/tq?tqx=out:html&tq=SELECT%20F%2C%20G%2C%20H%20WHERE%20E%20=%20%27"
                + tournamentCode + "%27&gid=2";
            //Log.i("Tournament Code", tournamentCode);
            //Log.i("Tournament Pages URL", tournamentPagesUrl);
            Document doc = Jsoup.connect(tournamentPagesUrl).get();
            //Log.i("Checking", "X");
            Element table = doc.select("table").get(0);
            //Log.i("Checking", "X");
            Elements rows = table.select("tr");
            //Log.i("Checking", "X");
            Element row = rows.get(1);
            //Log.i("Checking", "X");
            Elements cols = row.select("td");
            //Log.i("Checking", "X");

            for (int i=0; i<3; i++) {
                myApp.setServerAddressString(i, String.valueOf(cols.get(i).text()));
                if (myApp.serverAddressString(i).length()<2) {
                    myApp.setServerAddressString(i,"");
                }
            }
            serverOK = true;
        } catch (Exception e) {

        }
        return null;
    }


    @Override
    protected void onPostExecute(Void result) {

        mProgressDialog.dismiss();
        delegate.processFinish(Activity.RESULT_OK);

        if (!serverOK) {
            CharSequence text = "Error downloading pages data.";
            int duration = Toast.LENGTH_LONG;
            Toast.makeText(mContext, text, duration).show();
        }
    }
}
