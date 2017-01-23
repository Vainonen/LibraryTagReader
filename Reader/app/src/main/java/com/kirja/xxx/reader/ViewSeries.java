package com.kirja.xxx.reader;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ViewSeries extends Activity implements TaskDelegate, View.OnClickListener {

    TableLayout tableLayout;
    JSONHandler jsonh;
    String series;
    ArrayList<String []> al;
    APICall ac = new APICall();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewseries);
        tableLayout = (TableLayout) findViewById(R.id.data);
        series = getIntent().getStringExtra("Series");
        jsonh = new JSONHandler();
        Log.i("sarja", series);
        getData(series);

    }

    private void getData(String series) {
        String url = "https://api.finna.fi/v1/search?lookfor=series:" + series + "&language=fi&field[]=title&field[]=cleanIsbn&field[]=year&field[]=series";
        Log.i("sarja", url);
        ac.delegate = this;

        if (!isConnected())
            Toast.makeText(this.getApplicationContext(),
                    "Verkkoyhteys ei toimi!", Toast.LENGTH_LONG).show();
        else try {
            ac.execute(url);
        }
        catch (Exception e) {
            Log.e("error", "not result");
        }
    }

    public boolean isConnected() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }

    @Override
    public void TaskCompleted(String result) {
        jsonh.setResult(result);
        al = jsonh.getSeries();
        TableRow.LayoutParams trlp =
                new TableRow.LayoutParams
                        (TableRow.LayoutParams.FILL_PARENT,TableLayout.LayoutParams.WRAP_CONTENT);
        TextView [] link = new TextView[al.size()];
        TableRow [] tr = new TableRow[al.size()];

        for (int i = 0; i < al.size(); i++) {
            String [] seriesdata = al.get(i);
            String text = "";
            if (seriesdata[0] != null) text += seriesdata[0]+" ";
            if (seriesdata[1] != null) text += seriesdata[1]+" ";
            if (seriesdata[2] != null) text += seriesdata[2]+" ";
            link[i] = new TextView(this);
            tr[i] = new TableRow(this);
            link[i].setText(text);
            if (seriesdata[3] != null) link[i].setOnClickListener(this);
            else link[i].setTextColor(Color.RED);
            link[i].setId(i);
            link[i].setLayoutParams(trlp);
            trlp.setMargins(20, 20, 20, 20);
            tr[i].setBackgroundResource(R.drawable.row_border);
            tr[i].addView(link[i]);
            tableLayout.addView(tr[i]);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        String [] seriesdata = al.get(id);
        String isbn = "";
        try {
            isbn += seriesdata[3];
        }
        catch (Exception e) {
            Log.e("Series error", "no ISBN");
        }
        Intent intent = new Intent(getBaseContext(), HTMLActivity.class);
        intent.putExtra("Keyword", isbn);
        startActivity(intent);
    }
}