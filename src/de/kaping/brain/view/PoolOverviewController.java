package de.kaping.brain.view;

import de.kaping.brain.MainApp;
import de.kaping.brain.model.Genome;
import de.kaping.brain.model.Species;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class PoolOverviewController {
	// Species Table:
	@FXML
	private TableView<Species> poolTable;
	@FXML
	private TableColumn<Species, String> speciesColumn;
	@FXML
	private TableColumn<Species, String> genomeColumn;
	// Genome Table:
	@FXML
	private TableView<Genome> genomeTable;
	@FXML
	private TableColumn<Genome, String> genomeDetailColumn;

	// Labels für Anzeige von Details zu selektierter Species/Genome
	@FXML
	private Label headLabel;
	@FXML
	private Label generationLabel;
	@FXML
	private Label idLabel;
	@FXML
	private Label neuronsLabel;
	@FXML
	private Label genesLabel;
	@FXML
	private Label fitnessCodeLabel;
	@FXML
	private Label stalenessLabel;

	// Reference to the main application.
	private MainApp mainApp;

	/**
	 * The constructor. The constructor is called before the initialize()
	 * method.
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
		speciesColumn.setCellValueFactory(
			cellData -> cellData.getValue().getIDProperty().asString());
		genomeColumn.setCellValueFactory(
			cellData -> cellData.getValue().countGenomesProperty().asString());
		genomeDetailColumn.setCellValueFactory(
			cellData -> cellData.getValue().getIDProperty().asString());
		// Clear person details.
		showSpeciesDetails(null);

		// Listen for selection changes in poolTable and show the person details when
		// changed.
		poolTable.getSelectionModel().selectedItemProperty().addListener(
			(observable, oldValue, newValue) -> showSpeciesDetails(newValue));
		
		// Listen for selection changes in genomeTable and show the person details when
		// changed.
		genomeTable.getSelectionModel().selectedItemProperty().addListener(
			(observable, oldValue, newValue) -> showGenomeDetails(newValue));
	}

	/**
	 * Zeigt eine Liste aller Genome der ausgewählten Spezies an sowie einige
	 * Details zur ausgewählten Spezies
	 * 
	 * @param species,
	 *           welche die Daten bereitstellt
	 */
	private void showSpeciesDetails(Species species) {
		if (species != null) {
			// Labels mit Daten füllen, Genome zur Auswahl anzeigen
			headLabel.setText("SPECIES");
			idLabel.setText(String.valueOf(species.getID()));
			neuronsLabel.setText("");
			genesLabel.setText("");
			fitnessCodeLabel
				.setText(String.valueOf(species.getAverageFitness()));
			stalenessLabel.setText(String.valueOf(species.getStaleness()));
			genomeTable.setItems(species.getGenomes());
		} else {
			// Labels und Listen clearen
			headLabel.setText("");
			idLabel.setText("");
			neuronsLabel.setText("");
			genesLabel.setText("");
			fitnessCodeLabel.setText("");
			stalenessLabel.setText("");
			genomeTable.setItems(null);
		}
	}

	/**
	 * Zeigt Details zum ausgewählten Genome an
	 * 
	 * @param genome
	 */
	private void showGenomeDetails(Genome genome) {
		if (genome != null) {
			// Labels mit Daten füllen, Genome zur Auswahl anzeigen
			headLabel.setText("GENOME");
			idLabel.setText(String.valueOf(genome.getID()));
			neuronsLabel.setText(String.valueOf(genome.getNeurons().size()));
			genesLabel.setText(String.valueOf(genome.getGenes().size()));
			fitnessCodeLabel
				.setText(String.valueOf(genome.getAdjustedFitness()));
		}
	}

	/**
	 * Is called by the main application to give a reference back to itself.
	 * 
	 * @param mainApp
	 */
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;

		// Bind Properties to Labels
		// Aktuelle Generation anzeigen
		generationLabel.textProperty()
			.bind(mainApp.myPool.getGenerationProperty().asString());

		// Add observable list data to the table
		poolTable.setItems(mainApp.getPoolSpecies());
	}

	/**
	 * Called when the user clicks on the "new generation" button.
	 */
	@FXML
	private void handleNewGeneration() {
		mainApp.execNewGeneration();
	}
}