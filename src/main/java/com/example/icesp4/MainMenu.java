package com.example.icesp4;


import Snake.SnakeGame;
import com.example.icesp4.core.Services;
import javafx.application.Application;
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
    private static Scene highscoreMenuScene;

    private static Scene highscoreSubmitScene;
    private static FXMLLoader highscoreSubmitLoader;

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
        FXMLLoader highscoreMenuLoader = new FXMLLoader(getClass().getResource("HighscoreMenu.fxml"));

        Parent settingsMenuRoot = settingsLoader.load();
        Parent chooseGameMenuRoot = chooseGameMenuLoader.load();
        Parent chooseGameSubMenuRoot = chooseGameSubMenuLoader.load();
        Parent highscoreMenuRoot = highscoreMenuLoader.load();

        SettingsMenuController settingsController = settingsLoader.getController();
        settingsController.setHighscoreManager(services.highscores);

        ChooseGameSubMenuController chooseSubController = chooseGameSubMenuLoader.getController();
        chooseSubController.setHighscoreManager(services.highscores);

        mainMenuScene = new Scene(mainMenuRoot);
        settingsMenuScene = new Scene(settingsMenuRoot);
        chooseGameMenuScene = new Scene(chooseGameMenuRoot);
        chooseGameSubMenuScene = new Scene(chooseGameSubMenuRoot);
        highscoreMenuScene = new Scene(highscoreMenuRoot);


        Image image = new Image(getClass().getResource("Logo.png").toExternalForm());
        Image settingsImage = new Image(getClass().getResource("SettingsLogo.png").toExternalForm());

        Audio.playBackgroundMusic();

        mainMenuScene.getStylesheets().add(MainMenu.class.getResource("Style.css").toExternalForm());
        settingsMenuScene.getStylesheets().add(MainMenu.class.getResource("Style.css").toExternalForm());
        chooseGameMenuScene.getStylesheets().add(MainMenu.class.getResource("Style.css").toExternalForm());
        chooseGameSubMenuScene.getStylesheets().add(MainMenu.class.getResource("Style.css").toExternalForm());
        highscoreMenuScene.getStylesheets().add(MainMenu.class.getResource("Style.css").toExternalForm());
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
    public static void showHighscoreMenu(){
        primaryStage.setScene(highscoreMenuScene);
    }

    public static Services getServices() {
        return services;
    }

    public static void showHighscoreSubmit(String gameId, int finalScore) {
        try {
            HighscoreSubmitMenuController c = highscoreSubmitLoader.getController();
            c.init(services.highscores, gameId, finalScore);
            primaryStage.setScene(highscoreSubmitScene);
        } catch (Exception e) {
            e.printStackTrace();
            showMainMenu();
        }
    }

    public static void showSpaceInvaders() {
        final int W = 224;
        final int H = 256;
        final int SCALE = 3;

        var game = new SpaceInvaders.GameLoop(W, H, SCALE, finalScore -> {
            showHighscoreSubmit(com.example.icesp4.core.GameId.SPACE_INVADERS, finalScore);
        });

        Scene gameScene = new Scene(game.getRoot(), W * SCALE, H * SCALE);

        primaryStage.setScene(gameScene);
        primaryStage.setResizable(false);
        primaryStage.sizeToScene();
        game.attachInput(gameScene);
        game.requestFocus();
        game.start();
    }
    public static void showSnake() {
        SnakeGame game = new SnakeGame(600, 600);

        Scene scene = new Scene(game.getRoot());
        primaryStage.setScene(scene);

        game.attachInput(scene);
    }


}
