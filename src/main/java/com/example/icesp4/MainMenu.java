package com.example.icesp4;

import com.example.icesp4.core.Services;

import javafx.application.Application;

import javafx.concurrent.Service;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;

import javafx.scene.text.Font;
import javafx.stage.Stage;


public class MainMenu extends Application {


    private static Stage primaryStage;

    private static Scene mainMenuScene;
    private static Scene settingsMenuScene;
    private static Scene chooseGameMenuScene;
    private static Scene chooseGameSubMenuScene;

    public int size = 3;
    public final int width = 224 * size;
    public final int height = 256 * size;

    private static Services services;

    @Override
    public void start(Stage stage) throws Exception {

        primaryStage = stage;

        services = new Services();

        Font.loadFont(getClass().getResourceAsStream("PressStart2P.ttf"), 12);

        Parent mainMenuRoot = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
        FXMLLoader settingsLoader = new FXMLLoader(getClass().getResource("SettingsMenu.fxml"));
        FXMLLoader chooseGameMenuLoader = new FXMLLoader(getClass().getResource("ChooseGameMenu.fxml"));
        FXMLLoader chooseGameSubMenuLoader = new FXMLLoader(getClass().getResource("ChooseGameSubMenu.fxml"));

        Parent settingsMenuRoot = settingsLoader.load();
        Parent chooseGameMenuRoot = chooseGameMenuLoader.load();
        Parent chooseGameSubMenuRoot = chooseGameSubMenuLoader.load();

        SettingsMenuController settingsController = settingsLoader.getController();
        settingsController.setHighscoreManager(services.highscores);

        ChooseGameSubMenuController chooseSubController = chooseGameSubMenuLoader.getController();
        chooseSubController.setHighscoreManager(services.highscores);

        mainMenuScene = new Scene(mainMenuRoot);
        settingsMenuScene = new Scene(settingsMenuRoot);
        chooseGameMenuScene = new Scene(chooseGameMenuRoot);
        chooseGameSubMenuScene = new Scene(chooseGameSubMenuRoot);


        Image image = new Image(getClass().getResource("Logo.png").toExternalForm());
        Image settingsImage = new Image(getClass().getResource("SettingsLogo.png").toExternalForm());

        Audio.playBackgroundMusic();

        mainMenuScene.getStylesheets().add(MainMenu.class.getResource("Style.css").toExternalForm());
        settingsMenuScene.getStylesheets().add(MainMenu.class.getResource("Style.css").toExternalForm());
        chooseGameMenuScene.getStylesheets().add(MainMenu.class.getResource("Style.css").toExternalForm());
        chooseGameSubMenuScene.getStylesheets().add(MainMenu.class.getResource("Style.css").toExternalForm());
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

    public static void showChooseGameMenu(){
        primaryStage.setScene(chooseGameMenuScene);
    }

    public static void showChooseGameSubMenu(){
        primaryStage.setScene(chooseGameSubMenuScene);
    }

    public static Services getServices() {
        return services;
    }

}
