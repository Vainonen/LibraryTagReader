package com.kirja.xxx.reader;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.NfcV;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.math.BigInteger;
import java.util.HashMap;

public class TagReader extends AppCompatActivity {

    private NfcAdapter nfcAdapter;
    TextView textViewInfo, textViewTagInfo;
    LinearLayout linearLayout;
    Item item;
    Speaker speaker;

    //for testing bytesToHex method:
    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray(); // for testing NFC tag converting binary to hex

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading);
        textViewInfo = (TextView) findViewById(R.id.info);
        textViewTagInfo = (TextView) findViewById(R.id.info);
        linearLayout = (LinearLayout) findViewById(R.id.data);
        //linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
        //      LinearLayout.LayoutParams.MATCH_PARENT));
        //linearLayout.setOrientation(LinearLayout.VERTICAL);

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

        Intent intent = getIntent();
        String action = intent.getAction();

        if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {
            Toast.makeText(this,
                    "RFID löytyi",
                    Toast.LENGTH_SHORT).show();

            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            if (tag == null) {
                textViewInfo.setText("tag == null");
            } else {
                getData(tag);
            }

        } else {
            Toast.makeText(this,
                    action,
                    Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onPause(){
        super.onPause();
        //try {executorService.shutdown();}
        //catch (Exception e) {Log.e("executorService", "was interrupted");}
        /*
        try {
            executorService.awaitTermination(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Log.e("InterruptedException", e.toString()
            );
        }
        */
    }
    /*
        @Override
        protected void onStop() {
            super.onStop();
            Log.i("shutup", "shutup");
            MainActivity.person.shutUp();
            //executorService.shutdown();
        }
    */
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
                    (byte)0x60,                  // flags: addressed (= UID field present)
                    (byte)0x23,                  // command: READ MULTIPLE BLOCKS
                    (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,  // placeholder for tag UID
                    (byte)(offset & 0x0ff),      // first block number
                    (byte)((blocks - 1) & 0x0ff) // number of blocks (-1 as 0x00 means one block)
            };
            System.arraycopy(tag.getId(), 0, cmd, 2, 8);
            byte[] data = nfcv.transceive(cmd);

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

            String barcode = new BigInteger(number).toString();

            // make an array of bytes 30, 32-35, 37 of the NFCV tag where ISBN is located:
            number[0] = data[30];
            for (int i = 1; i < 6; i++) {
                number[i] = data[i+31];
            }
            number[5] = data[37];

            String isbn = new BigInteger(number).toString();

            TextView tv = new TextView(this);
            //tv.setText("Viivakoodi: " + barcode);
            tv.setText("ISBN: " + isbn);
            linearLayout.addView(tv);

            XMLHandler xmlh = new XMLHandler();
            APICall ac = new APICall();
            if (!isbn.equals("0")) ac.getData(this.getApplicationContext(), xmlh, isbn);
            String title = xmlh.getResults();
            //Log.i("title", title);
            //tv.setText(title);
            //HashMap<String, String> fields = xmlh.getEntry();
            //if(fields.containsKey("series")) ac.getSeries(this.getApplicationContext(), fields.get("series"));

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

    private  static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }
}