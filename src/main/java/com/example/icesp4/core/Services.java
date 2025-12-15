package com.example.icesp4.core;

import com.example.icesp4.db.LocalDatabase;
import com.example.icesp4.net.HighscoreClient;

public class Services {

    public final AppState state;
    public final HighscoreManager highscores;

    public Services() throws Exception {
        this.state = new AppState();

        LocalDatabase localDb = new LocalDatabase("local_scores.sqlite");
        localDb.init();

        HighscoreClient client = new HighscoreClient(state.getServerBaseUrl());
        this.highscores = new HighscoreManager(state, localDb, client);
    }
}
