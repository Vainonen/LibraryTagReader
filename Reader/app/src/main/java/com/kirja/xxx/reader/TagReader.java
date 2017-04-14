package com.kirja.xxx.reader;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.NfcV;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;

import java.io.IOException;
import java.math.BigInteger;

public class TagReader extends AppCompatActivity {

    private NfcAdapter nfcAdapter;

    //for testing bytesToHex method:
    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray(); // for testing NFC tag converting binary to hex

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading);
        WebView webview = (WebView) findViewById(R.id.start); //get the WebView from the layout XML
        webview.loadUrl("file:///android_asset/main.html");
        webview.getSettings().setJavaScriptEnabled(true);
        webview.addJavascriptInterface(new WebAppInterface(this), "Android");

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter == null) {
            Toast.makeText(this,
                    "Laitteessa ei ole NFC-toimintoa!",
                    Toast.LENGTH_LONG).show();
            finish();
        } else if (!nfcAdapter.isEnabled()) {
            Toast.makeText(this,
                    "Kytke laitteen NFC-toiminto päälle!",
                    Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        PendingIntent pendingIntent = PendingIntent.getActivity(
                this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, null, null);
    }

    @Override
    public void onPause() {
        super.onPause();
        NfcAdapter adapter = NfcAdapter.getDefaultAdapter(this);
        adapter.disableForegroundDispatch(this);
    }

    @Override
    public void onNewIntent(Intent intent) {
        String action = intent.getAction();
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action) || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {
            Toast.makeText(this,
                    "RFID löytyi",
                    Toast.LENGTH_SHORT).show();

            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            if (tag != null) getData(tag);
        } else {
            Toast.makeText(this,
                    action,
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void getData(Tag tag) {
        NfcV nfcv = NfcV.get(tag);

        try {
            nfcv.connect();
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(),
                    "Yhteys ei toimi!",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            /*
            this should fix the problem reading NFCV tags with different devices
            this solution tis from stackoverflow answer:
            http://stackoverflow.com/questions/28405558/android-nfc-read-iso15693-rfid-tag
            */
            int offset = 0;  // offset of first block to read
            int blocks = 8;  // number of blocks to read
            byte[] cmd = new byte[]{
                    (byte) 0x60,                  // flags: addressed (= UID field present)
                    (byte) 0x23,                  // command: READ MULTIPLE BLOCKS
                    (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,  // placeholder for tag UID
                    (byte) (offset & 0x0ff),      // first block number
                    (byte) ((blocks - 1) & 0x0ff) // number of blocks (-1 as 0x00 means one block)
            };
            System.arraycopy(tag.getId(), 0, cmd, 2, 8);
            byte[] data = nfcv.transceive(cmd);

            if (data.length > 0) {

                // make an array of bytes 30, 32-35, 37 of the NFCV tag where ISBN is located:

                String datat = new BigInteger(data).toString();
                Log.i("datat", bytesToHex(data));

                byte[] number = new byte[6];

                number[0] = data[4];
                number[1] = data[5];
                number[2] = data[7];
                number[3] = data[8];
                number[4] = data[9];
                number[5] = data[10];

                //String barcode = new BigInteger(number).toString();

                // make an array of bytes 30, 32-35, 37 of the NFCV tag where ISBN is located:
                number[0] = data[30];
                for (int i = 1; i < 6; i++) {
                    number[i] = data[i + 31];
                }
                number[5] = data[37];

                String isbn = new BigInteger(number).toString();
                if (!isbn.equals("0")) {
                    //Intent intent = new Intent(this, ViewItem.class);
                    //intent.putExtra("ISBN", isbn);
                    //startActivity(intent);
                    Intent newIntent = new Intent(this, ViewItem.class);
                    newIntent.putExtra("ISBN", isbn);
                    startActivity(newIntent);
                } else {
                    Toast.makeText(getApplicationContext(),
                            "RFID-tarrassa ei ole ISBN-tunnusta",
                            Toast.LENGTH_SHORT).show();
                }
            }

        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "Lukeminen epäonnistui!", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            nfcv.close();
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "Yhteyttä ei voitu sulkea!", Toast.LENGTH_SHORT).show();
            return;
        }
    }


    private static String bytesToHex(byte[] bytes) {
        String s = "";
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    class WebAppInterface {
        Context mContext;

        WebAppInterface(Context c) {
            mContext = c;
        }

        @JavascriptInterface
        public void getItem(String isbn) {
            Intent newIntent = new Intent(getApplicationContext(), ViewItem.class);
            newIntent.putExtra("ISBN", isbn);
            startActivity(newIntent);
        }
    }
}