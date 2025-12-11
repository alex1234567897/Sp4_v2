package com.example.icesp4;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class SettingsMenuController implements Initializable {

    @FXML
    private ChoiceBox <String> screenSizeBox;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        screenSizeBox.getItems().addAll("768x672", "1024x896", "1280x1120");
        screenSizeBox.setValue("768x672");
        Platform.runLater(() -> screenSizeBox.setOnAction(this::ScreenSize));
    }

    @FXML
    protected void ScreenSize(ActionEvent event){
        Stage stage = null;

        if (event != null) {
            Node source = (Node) event.getSource();
            if (source.getScene() != null) {
                stage = (Stage) source.getScene().getWindow();
            }
        }

        if (stage == null) {
            // Scene not ready yet, ignore
            return;
        }

        String value = screenSizeBox.getValue();

        switch (value) {
            case "768x672" -> { stage.setWidth(672); stage.setHeight(768); }
            case "1024x896" -> { stage.setWidth(896); stage.setHeight(1024); }
            case "1280x1120" -> { stage.setWidth(1120); stage.setHeight(1280); }
            default -> { stage.setWidth(672); stage.setHeight(768); }
        }
    }

    @FXML
    protected void audioControl(){

    }

    @FXML
    protected void returnTOMenu() {
        MainMenu.showMainMenu();
    }


}
