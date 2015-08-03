package de.kaping.brain.view;

import de.kaping.brain.MainApp;
import de.kaping.brain.model.Species;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class PoolOverviewController {
    @FXML
    private TableView<Species> poolTable;
    @FXML
    private TableColumn<Species, String> speciesColumn;
    @FXML
    private TableColumn<Species, String> genomeColumn;

    @FXML
    private Label idLabel;
    @FXML
    private Label neuronsLabel;
    @FXML
    private Label genesLabel;
    @FXML
    private Label fitnessCodeLabel;

    // Reference to the main application.
    @SuppressWarnings("unused")
	private MainApp mainApp;

    /**
     * The constructor.
     * The constructor is called before the initialize() method.
     */
    public PoolOverviewController() {
    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
        // Initialize the person table with the two columns.
        speciesColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty());
        genomeColumn.setCellValueFactory(cellData -> cellData.getValue().countGenomesProperty().asString());
    }

    /**
     * Is called by the main application to give a reference back to itself.
     * 
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;

        // Add observable list data to the table
        poolTable.setItems(mainApp.getPoolSpecies());
    }
}