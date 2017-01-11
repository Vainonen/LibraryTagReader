package com.kirja.xxx.reader;

import android.content.Context;
import android.speech.tts.TextToSpeech;

import java.util.Locale;

public class Speaker  {

    TextToSpeech tts;
    int counter = 2;
    Item item;

    public Speaker(Context context) {
        tts = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    tts.setLanguage(new Locale("fi"));
                    float pitch = 0.5f;
                    tts.setPitch(pitch);
                }
            }
        });
    }
        public void speak (Item item){
            String s = "";
            this.item = item;
            int totalcheckouts = item.getTotalCheckouts();
            int checkouts = item.getCheckouts();
            String barcode = item.getBarcode();
            String author = item.getAuthor();
            if (item.isFound()) {
                s = "Tämä ei ole Daniel le Steell, mutta ei tarvitse poistaa";
                if (item.getItemCount() > 1 && item.getCheckoutAverage() < 5) {
                    s = "Näitä on liikaa hyllyssä... heitä ainakin yksi pois";
                }

                if (item.getTotalCheckouts() == 0) {
                    s = "Tämä ei ole mennyt tänä vuonna lainaan... joutaa roskiin";
                }
                tts.setSpeechRate(0.5f);
                if (item.getAuthor().equals("Steel, Danielle.")) {
                    s = "aaaah";
                    tts.setSpeechRate(0.1f);
                    tts.setPitch(2.0f);
                }
            } else s = "Nidettä ei löytynyt Excel-tiedostosta";

            tts.speak(s, TextToSpeech.QUEUE_FLUSH, null);
            tts.setPitch(0.5f);
        }
/*
        public void shutUp () {
            if (tts != null) {
                tts.stop();
                tts.shutdown();
            }
        }
        */


}
