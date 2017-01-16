package com.kirja.xxx.reader;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
        setContentView(R.layout.activity_reading);
        textViewInfo = (TextView) findViewById(R.id.info);
        textViewTagInfo = (TextView) findViewById(R.id.info);
        linearLayout = (LinearLayout) findViewById(R.id.data);

        isbn = getIntent().getStringExtra("ISBN");
        isbntext = new TextView(this);
        isbntext.setText("ISBN: " + isbn);
        linearLayout.addView(isbntext);

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

    public void getSeries(String name) {
        String url = "https://api.finna.fi/v1/search?lookfor=series:" + name + "&type=Series&field[]=cleanIsbn&field[]=title";
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
        Log.i("viesti", "tehty");
        String authorname = xmlh.getAuthor();
        if (authorname != null) {
            author = new TextView(this);
            author.setText("Tekijä: " + authorname);
            author.setOnClickListener(this);
            author.setId(0);
            linearLayout.addView(author);
        }
        String booktitle = xmlh.getTitle();
        if (authorname != null) {
            title = new TextView(this);
            title.setText("Teos: " + booktitle);
            linearLayout.addView(title);
        }
        String seriesname = xmlh.getSeriesName();
        if (seriesname != null) {
            series = new TextView(this);
            series.setText("Kuuluu sarjaan: " + seriesname);
            linearLayout.addView(series);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == 0) {
            Intent intent = new Intent(getBaseContext(), HTMLActivity.class);
            intent.putExtra("Author", xmlh.getAuthor());
            startActivity(intent);
        }
    }
}