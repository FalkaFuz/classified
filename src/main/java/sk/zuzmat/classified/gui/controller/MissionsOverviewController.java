package sk.zuzmat.classified.gui.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sk.zuzmat.classified.backend.Mission;
import sk.zuzmat.classified.backend.MissionManager;

import java.io.IOException;
import java.util.*;

/**
 * Created by zuz on 12-May-16.
 */
public class MissionsOverviewController {

    private MissionManager missionsManager;

    @FXML
    private TableView<Mission> missionTable;
    @FXML
    private TableColumn<Mission, String> nameColumn;
    @FXML
    private TableColumn<Mission, String> locationColumn;

    @FXML
    private void initialize() {

        missionsManager = new MissionManager() {

            Set<Mission> missions = new HashSet<>();

            @Override
            public void createMission(Mission mission) {
                missions.add(mission);
            }

            @Override
            public void updateMission(Mission mission) {
                missions.add(mission);
            }

            @Override
            public void deleteMission(Mission mission) {
                missions.remove(mission);
            }

            @Override
            public Mission findMissionById(Long id) {
                return null;
            }

            @Override
            public List<Mission> findAllMissions() {
                Mission mission = new Mission();
                mission.setCodeName("codeName1");
                mission.setId(1L);
                mission.setLocation("location1");

                Mission mission2 = new Mission();
                mission2.setCodeName("codeName2");
                mission2.setId(2L);
                mission2.setLocation("location2");

                missions.add(mission);
                missions.add(mission2);
                return new ArrayList<>(missions);
            }
        };

        nameColumn.setCellValueFactory(m -> new SimpleStringProperty(m.getValue().getCodeName()));
        locationColumn.setCellValueFactory(m -> new SimpleStringProperty(m.getValue().getLocation()));
        missionTable.setItems(FXCollections.observableArrayList(missionsManager.findAllMissions()));

        missionTable.setOnMousePressed(event -> {
            if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
                showMissionAgents(missionTable.getSelectionModel().getSelectedItem());
            }
        });
    }

    @FXML
    private void handleNewButton() {
        Mission mission = new Mission();
        if (showMissionDialog(mission)) {
            missionsManager.createMission(mission);
            missionTable.getItems().add(mission);
        }
    }

    @FXML
    private void handleEditButton() {
        Mission selected = missionTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            if (showMissionDialog(selected)) {
                missionsManager.updateMission(selected);
                missionTable.refresh();
            }
        }
    }

    @FXML
    private void handleDeleteButton() {
        Mission selected = missionTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            missionTable.getItems().remove(selected);
            missionsManager.deleteMission(selected);
        }
    }

    private boolean showMissionDialog(Mission mission) {
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/view/MissionDialog.fxml"), ResourceBundle.getBundle("localization/bundle"));
        Stage stage = new Stage();
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(missionTable.getScene().getWindow());
        AnchorPane pane;
        try {
            pane = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return false; // TODO
        }
        MissionDialogController controller = loader.getController();
        controller.setMission(mission);
        controller.setStage(stage);
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.showAndWait();
        return controller.isOkClicked();
    }

    private void showMissionAgents(Mission selectedItem) {

    }
}
