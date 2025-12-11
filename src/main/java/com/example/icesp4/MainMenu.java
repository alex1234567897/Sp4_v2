package com.example.icesp4;

import com.sun.tools.javac.Main;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.lang.reflect.Member;

public class MainMenu extends Application {


    private static Stage primaryStage;

    private static Scene mainMenuScene;
    private static Scene settingsMenuScene;

    public int size = 3;
    public final int width = 224 * size;
    public final int height = 256 * size;

    @Override
    public void start(Stage stage) throws IOException {

        primaryStage = stage;

        Font.loadFont(getClass().getResourceAsStream("PressStart2P.ttf"), 12);

        Parent mainMenuRoot = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));

        Parent settingsMenuRoot = FXMLLoader.load(getClass().getResource("SettingsMenu.fxml"));

        mainMenuScene = new Scene(mainMenuRoot);
        settingsMenuScene = new Scene(settingsMenuRoot);

        Image image = new Image(getClass().getResource("Logo.png").toExternalForm());

        Audio.playBackgroundMusic();

        mainMenuScene.getStylesheets().add(MainMenu.class.getResource("Style.css").toExternalForm());
        settingsMenuScene.getStylesheets().add(MainMenu.class.getResource("Style.css").toExternalForm());
        stage.setTitle("ARCADE MACHINE");
        stage.getIcons().add(image);
        stage.setScene(mainMenuScene);
        stage.setResizable(false);
        stage.setWidth(width);
        stage.setHeight(height);
        stage.show();

    }

    public static void showMainMenu() {
        primaryStage.setScene(mainMenuScene);
    }

    public static void showSettingsMenu() {
        primaryStage.setScene(settingsMenuScene);
    }

}
