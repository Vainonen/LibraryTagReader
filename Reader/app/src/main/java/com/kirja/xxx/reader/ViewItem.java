package com.kirja.xxx.reader;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ViewItem extends Activity implements TaskDelegate, View.OnClickListener {

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
        linearLayout = (LinearLayout) findViewById(R.id.data);
        author = (TextView) findViewById(R.id.author);
        title = (TextView) findViewById(R.id.title);
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
        if (xmlh.getAuthor() != null) {
            String authorname = "Tekijä: " + xmlh.getAuthor();
            SpannableString content = new SpannableString(authorname);
            content.setSpan(new UnderlineSpan(), 8, authorname.length(), 0);
            content.setSpan(new ForegroundColorSpan(Color.BLUE), 8, authorname.length(), 0);
            author.setText(content);
            author.setOnClickListener(this);
        }
        if (xmlh.getTitle() != null) {
            String booktitle = "Teos: " + xmlh.getTitle();
            SpannableString content = new SpannableString(booktitle);
            content.setSpan(new UnderlineSpan(), 6, booktitle.length(), 0);
            content.setSpan(new ForegroundColorSpan(Color.BLUE), 6, booktitle.length(), 0);
            title.setText(content);
            title.setOnClickListener(this);
        }
        if (xmlh.getSeriesName() != null) {
            String seriesname = "Kuuluu sarjaan: " + xmlh.getSeriesName();
            SpannableString content = new SpannableString(seriesname);
            content.setSpan(new UnderlineSpan(), 16, seriesname.length(), 0);
            content.setSpan(new ForegroundColorSpan(Color.BLUE), 16, seriesname.length(), 0);
            series.setText(content);
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
                break;
            }
            case R.id.title: {
                Intent intent = new Intent(getBaseContext(), HTMLActivity.class);
                intent.putExtra("Keyword", xmlh.getTitle());
                startActivity(intent);
                break;
            }
            case R.id.series: {
                Intent intent = new Intent(getBaseContext(), ViewSeries.class);
                intent.putExtra("Series", xmlh.getSeriesName());
                startActivity(intent);
                break;
            }
        }

    }
}
