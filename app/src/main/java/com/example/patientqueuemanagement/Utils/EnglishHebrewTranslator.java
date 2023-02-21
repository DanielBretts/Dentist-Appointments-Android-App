package com.example.patientqueuemanagement.Utils;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.patientqueuemanagement.Interfaces.DataListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;

/**
 * The EnglishHebrewTranslator class handles the translation part in specific places where the word is
 * dynamic. The rest of the values are translated in the @value/string.xml
 */
public class EnglishHebrewTranslator {

    //TranslatorOptions English to Hebrew
    static TranslatorOptions options =
            new TranslatorOptions.Builder()
                    .setSourceLanguage(TranslateLanguage.ENGLISH)
                    .setTargetLanguage(TranslateLanguage.HEBREW)
                    .build();
    static Translator englishHebrewTranslator = null;

    public static void connectToModel(){
        englishHebrewTranslator = Translation.getClient(options);
        DownloadConditions conditions = new DownloadConditions.Builder()
                .requireWifi()
                .build();
        englishHebrewTranslator.downloadModelIfNeeded(conditions)
                .addOnSuccessListener(
                        new OnSuccessListener() {
                            @Override
                            public void onSuccess(Object o) {
                                Log.d("onSuccess: ","model downloaded!");
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e("EnglishHebrewTranslator",e.toString());
                            }
                        });
        englishHebrewTranslator.close();
    }

    /**
     * This function uses the google translate API to translate from english to hebrew
     * @param dataListener the listener you implement in your function to get the translated word
     * @param str the string you want to translate
     */
    public static void translate(DataListener dataListener, String str){
        englishHebrewTranslator = Translation.getClient(options);
        englishHebrewTranslator.translate(str).addOnSuccessListener(new OnSuccessListener<String>() {
            @Override
            public void onSuccess(String s) {
                Log.d( "onSuccess: ",s);
                dataListener.getStringVal(s);
                englishHebrewTranslator.close();
            }
        });
    }
}
