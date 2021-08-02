package com.card.carder;

import android.content.Context;
import android.content.SharedPreferences;

public class MyCarderSaver {

    public SharedPreferences sharedPreferences;

    public MyCarderSaver(Context context){
        sharedPreferences = context.getSharedPreferences("GamePreffs", context.MODE_PRIVATE);
    }

    public String getUrlReference() {
        return sharedPreferences.getString("point", "");
    }

    public void setUrlReference(String url) {
        sharedPreferences.edit().putString("point", url).apply();
    }

    public boolean getFirst() {
        return sharedPreferences.getBoolean("first", true);
    }

    public void setFirst(boolean firstReference) {
        sharedPreferences.edit().putBoolean("first", firstReference).apply();
    }

    public boolean getFirstPlay() {
        return sharedPreferences.getBoolean("firstGame", true);
    }

    public void setFirstPlay(boolean firstPlay) {
        sharedPreferences.edit().putBoolean("firstGame", firstPlay).apply();
    }

    public boolean getFirstFlyerRecived() {
        return sharedPreferences.getBoolean("fflyer", true);
    }

    public void setFirstFlyerRecived(boolean firstReference) {
        sharedPreferences.edit().putBoolean("fflyer", firstReference).apply();
    }
}
