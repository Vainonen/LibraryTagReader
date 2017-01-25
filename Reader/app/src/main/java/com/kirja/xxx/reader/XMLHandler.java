package com.kirja.xxx.reader;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.Scanner;

public class XMLHandler {

    public JSONObject json;
    public JSONArray records;
    public String results;
    public String xml;
    public XmlPullParser xpp;

    public XMLHandler() {

    }

    public void setResult(String result) {
        Log.i("result2", result);
        try {
            json = new JSONObject(result);
            results = json.getString("resultCount");
            records = json.getJSONArray("records");
            xml = records.getJSONObject(0).getString("fullRecord");

            Log.i("viesti", this.getAuthor());

        } catch (Exception e) {
            Log.e("XML error", e.toString());
        }
    }

    //TODO: infinite loop if xml is malformed?
    private String getDatafield(String number, String value) {

        String result = null;
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            xpp = factory.newPullParser();
            xpp.setInput(new StringReader(xml));

            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    try {
                        if (xpp.getName().equals("datafield") && xpp.getAttributeValue(0).equals(number)) {
                            result = getSubfield(value);
                        }
                    } catch (Exception e) {
                        Log.e("xml error", "attribute not found");
                    }
                }
                eventType = xpp.next();
            }
        } catch (Exception e) {
            Log.e("xml error", "what?");
        }
        return result;
    }

    private String getSubfield (String value) {
        String result = null;
        try {
            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_TAG) {
                eventType = xpp.next();
                if (eventType == XmlPullParser.START_TAG) {
                    if (xpp.getName().equals("subfield") && xpp.getAttributeValue(0).equals(value)) {
                        eventType = xpp.next();
                        if (eventType == XmlPullParser.TEXT) {
                            result = xpp.getText();
                        }
                    }
                }
            }
        } catch (Exception e) {
            Log.e ("xml error", e.toString());
        }
        return result;
    }

    public int getPageNumber () {
        String result = getDatafield("300", "a");
        int pages = 0;
        Scanner sc = new Scanner(result);
        try {
            pages = sc.nextInt();
        } catch (Exception e) {
            Log.e("error", "number not in correct format");
        }
        return pages;
    }

    public String getTitle () {
        String title = "";
        if (getDatafield("245", "a") != null) title += getDatafield("245", "a");
        if (getDatafield("245", "b") != null) title += getDatafield("245", "b");
        if (title != null) title = title.replaceAll("\\s*\\p{Punct}+\\s*$", "");
        return title;
    }

    //TODO: author name may be also in datafields 700, a & 245, c:
    public String getAuthor () {
        String author;
        author = getDatafield("100", "a");
        if (author != null) author = author.replaceAll("\\s*\\p{Punct}+\\s*$", "");
        return author;
    }

    public String getSeriesName () {
        String name;
        name = getDatafield("490", "a");
        if (name != null) return name;
        name = getDatafield("651", "a");
        if (name != null) return name;
        name = getDatafield("830", "a");
        if (name != null) name = name.replaceAll("\\s*\\p{Punct}+\\s*$", "");
        return name;
    }

    public String getResults() {
        return results;
    }

}