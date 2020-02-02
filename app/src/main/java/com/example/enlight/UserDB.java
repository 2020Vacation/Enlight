package com.example.enlight;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


public class UserDB {

    static SharedPreferences getSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void add(Context context, UserModel data) { //유저 정보를 저장하는 함수
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        //에디터에 받아온 값 저장
        editor.putString("name", data.getName())
                .putString("email", data.getEmail())
                .putString("userkey", data.getUserkey())
                .apply();
    }

    public String getUserName(Context context) {
        return getSharedPreferences(context).getString("name", "");
    }

    public String getUserEmail(Context context) {
        return getSharedPreferences(context).getString("email", "");
    }

    public String getUserKey(Context context) {
        return getSharedPreferences(context).getString("userkey", "");
    }
}
