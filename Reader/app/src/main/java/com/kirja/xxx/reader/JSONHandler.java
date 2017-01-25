package com.kirja.xxx.reader;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.Parser;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

public class JSONHandler {

    JSONObject json;
    JSONArray records;
    String results;
    String xml;

    public JSONHandler() {

    }

    public void setResult(String result) {
        Log.i("result", result);
        try {
            json = new JSONObject(result);
            results = json.getString("resultCount");
            records = json.getJSONArray("records");
            xml = records.getJSONObject(0).getString("fullRecord");
        } catch (Exception e) {
            Log.e("JSON error", e.toString());
        }
    }

    public ArrayList<String []> getSeries() {

        ArrayList<String []> series = new ArrayList();

        for (int i = 0; i < records.length(); i++) {

            String data[] = new String[5];


            try {
                JSONArray seriesarray = records.getJSONObject(i).getJSONArray("series");
                data[0] = seriesarray.getJSONObject(0).getString("number");
            } catch (Exception e) {
                Log.e("JSON error", e.toString());
            }
            try {
                data[1] = records.getJSONObject(i).getString("title");
            } catch (Exception e) {
                Log.e("JSON error", e.toString());
            }
            try {
                data[2] = records.getJSONObject(i).getString("year");
            } catch (Exception e) {
                Log.e("JSON error", e.toString());
            }
            try {
                data[3] = records.getJSONObject(i).getString("cleanIsbn");
            } catch (Exception e) {
                Log.e("JSON error", e.toString());
            }
            try {
                JSONArray seriesarray = records.getJSONObject(i).getJSONArray("isbns");
                data[4] = "";
                String format = seriesarray.getString(0);
                if (format.contains("nid")) data[4] = "pokkari";
                if (format.contains("sid")) data[4] = "kovakantinen";
            } catch (Exception e) {
                Log.e("JSON error", e.toString());
            }
            for (int j = 0; j < data.length; j++) {
                if (data[j] == null) data[j] = "";
            }
            series.add(data);

        }

        Collections.sort(series, new Comparator<String[]>() {
            public int compare(String[] s1, String[] s2) {
                if (s1[0].equals(s2[0])) return s1[2].compareTo(s2[2]);
                if (s1[0].equals("") && s2[0].equals("")) return s1[2].compareTo(s2[2]);
                return s1[0].compareTo(s2[0]);
            }
        });

        return series;
    }

    //TODO: returns just the first author name in case of more authors in one title
    public String getAuthor() {
        try {
            JSONArray authors = records.getJSONObject(0).getJSONArray("nonPresenterAuthors");
            return authors.getJSONObject(0).getString("name");
        } catch (Exception e) {
            Log.e("JSON error", e.toString());
        }
        return ""; //returns empty String for Speech class
    }

    public String getTitle() {
        try {
            return records.getJSONObject(0).getString("title");
        } catch (Exception e) {
            Log.e("JSON error", e.toString());
        }
        return ""; //returns empty String for Speech class
    }

    //TODO:
    public static String getPageNumber() {
        return null;
    }

    public String getLanguage() {
        try {
            JSONArray languages = records.getJSONObject(0).getJSONArray("languages");
            return languages.getString(0);
        } catch (Exception e) {
            Log.e("JSON error", e.toString());
        }
        return ""; //returns empty String for Speech class
    }

    public String getResults() {
        return results;
    }
}
