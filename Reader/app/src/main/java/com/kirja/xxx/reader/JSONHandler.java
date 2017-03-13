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
        //
        result = "{\"resultCount\":80,\"records\":[{\"title\":\"Askeleen j\u00e4ljess\u00e4\",\"cleanIsbn\":\"9516439381\",\"isbns\":[\"951-643-938-1 (nid.)\"],\"year\":\"1999\",\"series\":[{\"name\":\"Wallander\"}]},{\"title\":\"Pyramidi\",\"cleanIsbn\":\"951643990X\",\"isbns\":[\"951-643-990-X (sid.)\"],\"year\":\"2002\"},{\"title\":\"Hymyilev\u00e4 mies\",\"cleanIsbn\":\"9511201174\",\"isbns\":[\"951-1-20117-4 (sid.)\"],\"year\":\"2005\",\"series\":[{\"name\":\"Wallander\",\"number\":\"4\"}]},{\"title\":\"Kasvoton kuolema\",\"cleanIsbn\":\"9511237926\",\"isbns\":[\"978-951-1-23792-1 (nid.)\"],\"year\":\"2009\",\"series\":[{\"name\":\"Wallander\",\"number\":\"1\"}]},{\"title\":\"Ennen routaa\",\"cleanIsbn\":\"9524594978\",\"isbns\":[\"952-459-497-8 (nid.)\"],\"year\":\"2005\",\"series\":[{\"name\":\"Wallander\",\"number\":\"10\"}]},{\"title\":\"Pyramidi\",\"cleanIsbn\":\"9511201263\",\"isbns\":[\"951-1-20126-3 (sid.)\"],\"year\":\"2005\",\"series\":[{\"name\":\"Wallander\",\"number\":\"9\"}]},{\"title\":\"Palomuuri\",\"cleanIsbn\":\"9516439624\",\"isbns\":[\"951-643-962-4 (sid.)\"],\"year\":\"2000\"},{\"title\":\"Rauhaton mies\",\"cleanIsbn\":\"9511241192\",\"isbns\":[\"978-951-1-24119-5 (sid.)\"],\"year\":\"2009\",\"series\":[{\"name\":\"Wallander\"}]},{\"title\":\"V\u00e4\u00e4rill\u00e4 j\u00e4ljill\u00e4\",\"cleanIsbn\":\"9511156381\",\"isbns\":[\"951-1-15638-1 (nid.)\"],\"year\":\"1998\"},{\"title\":\"Rauhaton mies\",\"cleanIsbn\":\"9511239716\",\"isbns\":[\"978-951-1-23971-0 (sid.)\"],\"year\":\"2009\",\"series\":[{\"name\":\"Wallander\"}]},{\"title\":\"Palomuuri\",\"cleanIsbn\":\"9511201255\",\"isbns\":[\"951-1-20125-5 (sid.)\"],\"year\":\"2005\",\"series\":[{\"name\":\"Wallander\",\"number\":\"8\"}]},{\"title\":\"Viides nainen\",\"cleanIsbn\":\"9511201220\",\"isbns\":[\"951-1-20122-0 (sid.)\"],\"year\":\"2005\",\"series\":[{\"name\":\"Wallander\",\"number\":\"6\"}]},{\"title\":\"V\u00e4\u00e4rill\u00e4 j\u00e4ljill\u00e4\",\"cleanIsbn\":\"9511201182\",\"isbns\":[\"951-1-20118-2 (sid.)\"],\"year\":\"2005\",\"series\":[{\"name\":\"Wallander\",\"number\":\"5\"}]},{\"title\":\"Askeleen j\u00e4ljess\u00e4\",\"cleanIsbn\":\"9511201247\",\"isbns\":[\"951-1-20124-7 (sid.)\"],\"year\":\"2005\",\"series\":[{\"name\":\"Wallander\",\"number\":\"7\"}]},{\"title\":\"Valkoinen naarasleijona\",\"cleanIsbn\":\"9511201239\",\"isbns\":[\"951-1-20123-9 (sid.)\"],\"year\":\"2005\",\"series\":[{\"name\":\"Wallander\",\"number\":\"3\"}]},{\"title\":\"Hymyilev\u00e4 mies\",\"cleanIsbn\":\"9511201174\",\"isbns\":[\"951-1-20117-4 (sid.)\"],\"year\":\"2005\",\"series\":[{\"name\":\"Wallander\",\"number\":\"4\"}]},{\"title\":\"Riian verikoirat\",\"cleanIsbn\":\"9511201166\",\"isbns\":[\"951-1-20116-6 (sid.)\"],\"year\":\"2005\",\"series\":[{\"name\":\"Wallander\",\"number\":\"[2]\"}]},{\"title\":\"Kasvoton kuolema\",\"cleanIsbn\":\"9511201158\",\"isbns\":[\"951-1-20115-8 (sid.)\"],\"year\":\"2005\",\"series\":[{\"name\":\"Wallander\",\"number\":\"[1]\"}]},{\"title\":\"Perint\u00f6\",\"year\":\"2009\",\"series\":[{\"name\":\"Wallander\"}]},{\"title\":\"Peitenimi\",\"year\":\"2006\",\"series\":[{\"name\":\"Wallander\"}]}],\"status\":\"OK\"}";

        try {
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
