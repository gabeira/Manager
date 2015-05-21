package com.podio.manager;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Pair;
import com.podio.manager.util.SymmetricAlgorithmAES;
import java.util.Date;

public class ManagerApp extends Application {

    private static final String PREF_ACCESS_TOKEN = "access_token";
    private static final String PREF_EXPIRES_IN = "expires_in";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    public void onCreate() {
        super.onCreate();

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = sharedPreferences.edit();
    }

    public void saveToken(Pair<String, Date> token){
        editor.putString(PREF_ACCESS_TOKEN, SymmetricAlgorithmAES.getInstance().encode(token.first));
        editor.putLong(PREF_EXPIRES_IN, new Date().getTime() + (token.second.getTime()*1000));
        editor.commit();
    }

    public String getToken(){
        return SymmetricAlgorithmAES.getInstance().decode(sharedPreferences.getString(PREF_ACCESS_TOKEN,""));
    }

    public boolean isTokenValid(){
        return new Date().before(new Date(sharedPreferences.getLong(PREF_EXPIRES_IN, 0)));
    }

    public void clearTokenData(){
        editor.clear();
        editor.commit();
    }
}
