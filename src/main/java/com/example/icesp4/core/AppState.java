package com.example.icesp4.core;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class AppState {

    private final StringProperty serverBaseUrl = new SimpleStringProperty("http://localhost:8080");

    private final StringProperty playerTag = new SimpleStringProperty("AAA");

    public StringProperty serverBaseUrlProperty() {
        return serverBaseUrl;
    }

    public String getServerBaseUrl() {
        return serverBaseUrl.get();
    }

    public void setServerBaseUrl(String url) {
        serverBaseUrl.set(url);
    }

    public StringProperty playerTagProperty() {
        return playerTag;
    }

    public String getPlayerTag() {
        return playerTag.get();
    }

    public void setPlayerTag(String tag) {
        playerTag.set(tag);
    }
}