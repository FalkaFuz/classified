package sk.zuzmat.classified.gui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sk.zuzmat.classified.backend.Mission;

/**
 * Created by zuz on 12-May-16.
 */
public class MissionDialogController {

    private Mission mission;
    private boolean okClicked = false;
    private Stage stage;

    @FXML
    private TextField nameField;
    @FXML
    private TextField locationField;

    @FXML
    private void handleOkButton() {
        if (isInputValid()) {
            mission.setCodeName(nameField.getText());
            mission.setLocation(locationField.getText());
            okClicked = true;
            stage.close();
        }
    }

    @FXML
    private void handleCancelButton() {
        stage.close();
    }

    private boolean isInputValid() {
        return !nameField.getText().trim().isEmpty() && !locationField.getText().trim().isEmpty();
    }


    public void setStage(Stage stage) {
        this.stage = stage;
    }


    public void setMission(Mission mission) {
        this.mission = mission;
        nameField.setText(mission.getCodeName() != null ? mission.getCodeName() : "");
        locationField.setText(mission.getLocation() != null ? mission.getLocation() : "");
    }

    public boolean isOkClicked() {
        return okClicked;
    }
}
