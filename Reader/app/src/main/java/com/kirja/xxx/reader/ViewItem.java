package com.kirja.xxx.reader;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ViewItem extends Activity implements TaskDelegate, View.OnClickListener {

    TextView textViewInfo, textViewTagInfo;
    LinearLayout linearLayout;
    XMLHandler xmlh;
    TextView isbntext;
    TextView author;
    TextView title;
    TextView series;
    String isbn;
    APICall ac = new APICall();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewitem);
        //textViewInfo = (TextView) findViewById(R.id.info);
        //textViewTagInfo = (TextView) findViewById(R.id.info);
        linearLayout = (LinearLayout) findViewById(R.id.data);
        author = (TextView) findViewById(R.id.author);
        series = (TextView) findViewById(R.id.series);
        isbntext = (TextView) findViewById(R.id.isbn);
        isbn = getIntent().getStringExtra("ISBN");
        isbntext.setText("ISBN: " + isbn);

        xmlh = new XMLHandler();

        getData(isbn);

    }

    private void getData(String isbn) {
        String url = "https://api.finna.fi/v1/search?lookfor="+ isbn +"&field[]=fullRecord";
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
        xmlh.setResult(result);
        String authorname = xmlh.getAuthor();
        if (authorname != null) {
            author.setText("Tekijä: " + authorname);
            author.setOnClickListener(this);
        }
        String booktitle = xmlh.getTitle();
        if (authorname != null) {
            title = new TextView(this);
            title.setText("Teos: " + booktitle);
            linearLayout.addView(title);
        }
        String seriesname = xmlh.getSeriesName();
        if (seriesname != null) {
            series.setText("Kuuluu sarjaan: " + seriesname);
            series.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.author: {
                Intent intent = new Intent(getBaseContext(), HTMLActivity.class);
                intent.putExtra("Keyword", xmlh.getAuthor());
                startActivity(intent);
            }
            case R.id.series: {
                Intent intent = new Intent(getBaseContext(), ViewSeries.class);
                intent.putExtra("Series", xmlh.getSeriesName());
                startActivity(intent);
            }
        }

    }
}
