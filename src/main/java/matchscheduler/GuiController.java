package matchscheduler;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import matchscheduler.exceptions.*;

import java.io.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * Controls the functions of the user interface.
 *
 */
public class GuiController {

    @FXML
    public Accordion schedulerAccordion;

    @FXML
    public Button deleteTeamButton;

    @FXML
    public Button saveListButton;

    @FXML
    private TextField enterTeamNameTextField;

    @FXML
    private TextField leagueNameTextField;

    @FXML
    private Label leagueNameLabel;

    @FXML
    private DatePicker leagueStartDatePicker;

    @FXML
    private DatePicker leagueEndDatePicker;

    @FXML
    private ListView<String> teamsList;

    @FXML
    private ListView<String> gamePairingsListView;

    @FXML
    public RadioButton mondayRadioButton;

    @FXML
    public RadioButton tuesdayRadioButton;

    @FXML
    public RadioButton wednesdayRadioButton;

    @FXML
    public RadioButton thursdayRadioButton;

    @FXML
    public RadioButton fridayRadioButton;

    @FXML
    public RadioButton saturdayRadioButton;

    @FXML
    public RadioButton sundayRadioButton;

    @FXML
    public ComboBox<String> matchesPerDayComboBox;

    @FXML
    TitledPane matchesTitledPane;

    /**
     * in this method gui settings can be initialized.
     */
    @FXML
    public void initialize() {
        sundayRadioButton.setSelected(true);
        matchesPerDayComboBox.getItems().addAll("1", "2", "3", "4");
        gamePairingsListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        loadDefaultSettings();
        activateAutoSubmit(leagueStartDatePicker);
        activateAutoSubmit(leagueEndDatePicker);
    }

    @FXML
    protected void onAddTeamButtonClick() {
        String teamName = enterTeamNameTextField.getText();
        if (!teamName.trim().equals("")) {
            enterTeamNameTextField.clear();
            teamsList.getItems().add(teamName);
        }
    }

    @FXML
    protected void onDeleteTeamButtonClick() {
        int selectedIndex = teamsList.getSelectionModel().getSelectedIndex();
        if (selectedIndex != -1) {
            teamsList.getItems().remove(selectedIndex);
            teamsList.getSelectionModel().select(-1);
        }
    }

    @FXML
    protected void onSaveListButtonClick() {
        ObservableList<String> matchesList = gamePairingsListView.getItems();
        FileChooser fileChooser = new FileChooser();
        if (!leagueNameTextField.getText().isEmpty()) {
            fileChooser.setInitialFileName("GamePairings_" + leagueNameTextField.getText().replace(" ", "_")
                    + "_" + LocalDateTime.now().toString().replace(":", "_") + ".txt");
        } else {
            fileChooser.setInitialFileName("GamePairings_" + LocalDateTime.now().toString().replace(":", "_") + ".txt");
        }

        Stage stage = MatchSchedulerApplication.getStage();
        File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            try {
                PrintWriter writer = new PrintWriter(file);
                for (String s : matchesList) {
                    writer.println(s);
                }
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    protected void onSaveSettingsButtonClick() {
        ArrayList<DayOfWeek> selectedWeekdays = getRadioButtonsSettings();
        ArrayList<String> selectWeekdaysStrings = new ArrayList<>();
        for (DayOfWeek selectedWeekday : selectedWeekdays) {
            selectWeekdaysStrings.add(selectedWeekday.toString());
        }
        Settings settings = new Settings(new ArrayList<>(teamsList.getItems().stream().toList()),
                this.leagueStartDatePicker.getValue().toString(), this.leagueEndDatePicker.getValue().toString(),
                selectWeekdaysStrings, leagueNameTextField.getText(),
                matchesPerDayComboBox.getSelectionModel().getSelectedIndex() + 1);
        FileChooser fileChooser = new FileChooser();
        String initFilename;
        if (!leagueNameTextField.getText().isEmpty()) {
            initFilename = "settings_".concat(leagueNameTextField.getText().replace(" ", "_"))
                    .concat("_").concat(LocalDateTime.now().toString().replace(":", "_")).concat(".txt");
        } else {
            initFilename = "settings_".concat(LocalDateTime.now().toString().replace(":", "_")).concat(".txt");
        }

        fileChooser.setInitialFileName(initFilename);
        Stage stage = MatchSchedulerApplication.getStage();
        File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            try {
                FileOutputStream f = new FileOutputStream(file);
                ObjectOutputStream o = new ObjectOutputStream(f);
                o.writeObject(settings);
                o.close();
                f.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    protected void onLoadSettingsButtonClick() {
        teamsList.getItems().clear();
        FileChooser fileChooser = new FileChooser();
        Stage stage = MatchSchedulerApplication.getStage();
        File file = fileChooser.showOpenDialog(stage);

        if (file != null) {
            try {
                FileInputStream fi = new FileInputStream(file);
                ObjectInputStream oi = new ObjectInputStream(fi);
                Settings settings = (Settings) oi.readObject();
                ArrayList<String> teams = settings.getTeams();

                for (String team : teams) {
                    teamsList.getItems().add(team);
                }
                leagueStartDatePicker.setValue(settings.getLeagueStartDate());
                leagueEndDatePicker.setValue(settings.getLeagueEndDate());
                setRadioButtonsSettings(settings.getSelectedWeekdays());
                leagueNameTextField.setText(settings.getLeagueName());
                matchesPerDayComboBox.getSelectionModel().select(settings.getNumberOfMatchesPerDay() - 1);

                oi.close();
                fi.close();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    protected void onGenerateGamePairingsButtonClick() {

        // get radioButton settings
        ArrayList<DayOfWeek> selectedWeekdays = getRadioButtonsSettings();
        gamePairingsListView.getItems().clear();

        LocalDate leagueStartDate = leagueStartDatePicker.getValue();
        LocalDate leagueEndDate = leagueEndDatePicker.getValue();
        int numberOfMatchesPerDay = (matchesPerDayComboBox.getSelectionModel().getSelectedIndex() + 1);

        ArrayList<GamePairing> gamePairings;
        Scheduler scheduler = new Scheduler();
        ArrayList<String> allTeams = new ArrayList<>(teamsList.getItems().stream().toList());

        try {
            gamePairings = scheduler.calculateGamePairings(selectedWeekdays, allTeams, leagueStartDate, leagueEndDate, numberOfMatchesPerDay);
        } catch (LeagueEndExceededException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "last match day exceeds league end date - please adjust", ButtonType.OK);
            alert.show();
            return;
        } catch (LeagueStartNotSetException | LeagueEndNotSetException | TeamListEmptyException | NoWeekdaysSelectedException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.show();
            return;
        }

        //Now add the game pairings to the listView in the GUI

        if (!leagueNameTextField.getText().isEmpty()) {
            leagueNameLabel.setText(leagueNameTextField.getText());
        }

        for (GamePairing outputPairing : gamePairings) {
            gamePairingsListView.getItems().add(outputPairing.asString());
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Matches were generated", ButtonType.OK);
        alert.showAndWait();
        matchesTitledPane.setExpanded(true);
    }

    private ArrayList<DayOfWeek> getRadioButtonsSettings() {
        ArrayList<DayOfWeek> selectedWeekdays = new ArrayList<>();
        if (mondayRadioButton.isSelected())
            selectedWeekdays.add(DayOfWeek.MONDAY);
        if (tuesdayRadioButton.isSelected())
            selectedWeekdays.add(DayOfWeek.TUESDAY);
        if (wednesdayRadioButton.isSelected())
            selectedWeekdays.add(DayOfWeek.WEDNESDAY);
        if (thursdayRadioButton.isSelected())
            selectedWeekdays.add(DayOfWeek.THURSDAY);
        if (fridayRadioButton.isSelected())
            selectedWeekdays.add(DayOfWeek.FRIDAY);
        if (saturdayRadioButton.isSelected())
            selectedWeekdays.add(DayOfWeek.SATURDAY);
        if (sundayRadioButton.isSelected())
            selectedWeekdays.add(DayOfWeek.SUNDAY);
        return selectedWeekdays;
    }

    private void setRadioButtonsSettings(ArrayList<DayOfWeek> selectedWeekdays) {

        mondayRadioButton.setSelected(false);
        tuesdayRadioButton.setSelected(false);
        wednesdayRadioButton.setSelected(false);
        thursdayRadioButton.setSelected(false);
        fridayRadioButton.setSelected(false);
        saturdayRadioButton.setSelected(false);
        sundayRadioButton.setSelected(false);

        for (DayOfWeek weekday : selectedWeekdays) {
            if (weekday.equals(DayOfWeek.MONDAY))
                mondayRadioButton.setSelected(true);
            if (weekday.equals(DayOfWeek.TUESDAY))
                tuesdayRadioButton.setSelected(true);
            if (weekday.equals(DayOfWeek.WEDNESDAY))
                wednesdayRadioButton.setSelected(true);
            if (weekday.equals(DayOfWeek.THURSDAY))
                thursdayRadioButton.setSelected(true);
            if (weekday.equals(DayOfWeek.FRIDAY))
                fridayRadioButton.setSelected(true);
            if (weekday.equals(DayOfWeek.SATURDAY))
                saturdayRadioButton.setSelected(true);
            if (weekday.equals(DayOfWeek.SUNDAY))
                sundayRadioButton.setSelected(true);
        }
    }

    private void activateAutoSubmit(DatePicker picker) {

        picker.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                try {
                    picker.setValue(picker.getConverter().fromString(picker.getEditor().getText()));
                } catch (Exception e) {
                    picker.getEditor().setText(picker.getConverter().toString(picker.getValue()));
                }
            }
        });
    }

    private void loadDefaultSettings() {
        teamsList.getItems().add("FC Lost");
        teamsList.getItems().add("SC Kicking You");
        teamsList.getItems().add("The Hangovers");
        teamsList.getItems().add("AFC Beercelona");
        teamsList.getItems().add("The Wasted Talents");
        matchesPerDayComboBox.getSelectionModel().select(0);
        leagueNameTextField.setText("Kicks a Lot League");
        mondayRadioButton.setSelected(false);
        tuesdayRadioButton.setSelected(false);
        wednesdayRadioButton.setSelected(false);
        thursdayRadioButton.setSelected(false);
        fridayRadioButton.setSelected(false);
        saturdayRadioButton.setSelected(false);
        sundayRadioButton.setSelected(true);
        leagueStartDatePicker.setValue(LocalDate.now());
        leagueEndDatePicker.setValue(LocalDate.now().plusMonths(6));
    }
}