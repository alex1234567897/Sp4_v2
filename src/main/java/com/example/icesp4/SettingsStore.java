package com.example.icesp4;


import java.util.prefs.Preferences;

public class SettingsStore {
    private static final Preferences prefs = Preferences.userNodeForPackage(SettingsStore.class);

    private static final String KEY_SERVER_URL = "serverBaseUrl";

    public static void saveServerBaseUrl(String url) {
        prefs.put(KEY_SERVER_URL, url);
    }

    public static String loadServerBaseUrl(String fallback) {
        return prefs.get(KEY_SERVER_URL, fallback);
    }
}