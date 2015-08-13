package de.kaping.brain.view;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import de.kaping.brain.MainApp;
import de.kaping.brain.model.Gene;
import de.kaping.brain.model.Genome;
import de.kaping.brain.model.GenomeHistory;
import de.kaping.brain.model.Neuron;
import de.kaping.brain.model.Species;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.paint.Color;

public class BrainOverviewController {
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
	private Label headLabel; // Wird gerade ein Genome oder eine Spezies
								// angezeigt?
	@FXML
	private Label generationLabel; // Wievielte Generation?
	@FXML
	private Label idLabel; // ID der Sezies/Genome
	@FXML
	private Label neuronsLabel; // Anzahl der Neuronen in Spezies/Genom
	@FXML
	private Label genesLabel; // Anzahl der Genes im Genome / Genomes in Spezies
	@FXML
	private Label fitnessLabel; // Best Fitness in Spezies / Current Fitness des
								// Genomes
	@FXML
	private Label stalenessLabel; // Best Staleness in Spezies / Current
									// Staleness in Genome
	@FXML
	private LineChart<int[], double[]> fitHistoryChart; // Canvas zum Anzeigen
														// des Verlaufs der
														// Fitness für das Genom
	@FXML
	private Canvas infoCanvas; // Canvas im Infotab
	@FXML
	private Canvas bestCanvas; // Canvas im unteren Bereich, welches das beste
								// Genome anzeigen soll
	@FXML
	private Label avgFitnessLabel; // Average Fitness von Spezies/Genome
	@FXML
	private Label avgStalenessLabel; // Average Staleness von Spezies/Genome

	@FXML
	private Label bestIDLabel; // ID des besten Genomes
	@FXML
	private Label bestNeuronsLabel; // Anzahl der Neuronen im besten Genome
	@FXML
	private Label bestGenesLabel; // # Genes des besten Genomes
	@FXML
	private Label bestFitnessLabel; // Fitness des besten Genomes
	@FXML
	private Label bestStalenessLabel; // Staleness des besten Genomes

	// Reference to the main application.
	private MainApp mainApp;

	/**
	 * The constructor. The constructor is called before the initialize()
	 * method.
	 */
	public BrainOverviewController() {
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

		// Listen for selection changes in poolTable and show the person details
		// when
		// changed.
		poolTable.getSelectionModel().selectedItemProperty().addListener(
			(observable, oldValue, newValue) -> showSpeciesDetails(newValue));

		// Listen for selection changes in genomeTable and show the person
		// details when
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
			fitnessLabel.setText(String.valueOf(species.getAverageFitness()));
			stalenessLabel.setText(String.valueOf(species.getStaleness()));
			genomeTable.setItems(species.getGenomes());
		} else {
			// Labels und Listen clearen
			headLabel.setText("");
			idLabel.setText("");
			neuronsLabel.setText("");
			genesLabel.setText("");
			fitnessLabel.setText("");
			stalenessLabel.setText("");
			genomeTable.setItems(null);
		}
	}

	/**
	 * Zeigt Details zum ausgewählten Genome an sowie die History, falls
	 * vorhanden
	 * 
	 * @param genome
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void showGenomeDetails(Genome genome) {
		if (genome != null) {
			// Labels mit Daten füllen, Genome zur Auswahl anzeigen
			headLabel.setText("GENOME");
			idLabel.setText(String.valueOf(genome.getID()));
			neuronsLabel.setText(String.valueOf(genome.getNeurons().size()));
			genesLabel.setText(String.valueOf(genome.getGenes().size()));
			fitnessLabel.setText(String.valueOf(genome.getFitness()));
			renderGenome(genome, infoCanvas);

			// History anzeigen
			fitHistoryChart.getData().clear();
			XYChart.Series fitSeries = new XYChart.Series<>();
			fitSeries.setName("Fitness");
			int c = 0;
			double[] histArray = GenomeHistory.INSTANCE
				.getFitnessHistory(genome);
			if (histArray != null) {
			for (Double d : histArray) {
				fitSeries.getData().add(new XYChart.Data("G" + (c+1), d));
				c++;
			}
			fitHistoryChart.getData().add(fitSeries);
			}
		}
	}

	/**
	 * Sortiert die NeuronenListe nach Type
	 * (Undefined->Bias->Input->hidden->Output)
	 * 
	 * @param neurons
	 *           Liste, welche sortiert werden soll
	 */
	private void sortNeurons(ObservableList<Neuron> neurons) {
		neurons.sort(new Comparator<Neuron>() {
			@Override
			public int compare(Neuron o1, Neuron o2) {
			return o1.getType().compareTo(o2.getType());
			}
		});
	}

	/**
	 * Rendert ein Genom und zeigt es auf dem Canvas an, das Canvas sollte
	 * Quadratisch sein
	 * 
	 * @param genome
	 */
	public void renderGenome(Genome genome, Canvas canvas) {
		GraphicsContext gc = canvas.getGraphicsContext2D();
		double w = canvas.getWidth();
		double h = canvas.getHeight();
		gc.clearRect(0, 0, w, h);
		int div = (int) (Math.sqrt(genome.getNeurons().size())) + 1;
		int x = 0;
		int y = 0;
		double Nwidth = Math.min(w / (div + 3), 25);
		double Nheight = Math.min(h / (div + 3), 25);

		// Neuronen so sortierem, dass sie in der Reihenfolge der Types
		// angezeigt werden (undefined, bias, input, hidden, output)
		sortNeurons(genome.getNeurons());

		Map<Neuron, Point2D> GridPos = new HashMap<Neuron, Point2D>();

		for (Neuron n : genome.getNeurons()) {
			// Verschiedene Typen von Neuronen, werden eingefärbt
			switch (n.getType()) {
			case BIAS:
			gc.setFill(Color.ORANGE);
			break;
			case HIDDEN:
			gc.setFill(Color.BLUE);
			break;
			case INPUT:
			gc.setFill(Color.GREEN);
			break;
			case OUTPUT:
			gc.setFill(Color.LIGHTGREEN);
			break;
			default:
			gc.setFill(Color.BLACK);
			break;
			}

			// Neuronen zeichnen
			gc.fillOval((w / div) * x, (h / div) * y, Nwidth, Nheight);

			// Position des Neurons speichern
			GridPos.put(n, new Point2D(x, y));

			// Nächste Position im Grid ermitteln
			x++;
			if (x >= div) {
			x = 0;
			y++;
			}
		}
		// Enabled Genes zeichnen
		for (Gene g : genome.getGenes()) {
			if (g.getEnabled() && GridPos.containsKey(g.getOrigin())
				&& GridPos.containsKey(g.getInto())) {
			// Die Neuronen der Verbindung existieren
			double x1 = (w / div) * GridPos.get(g.getOrigin()).getX()
					+ Nwidth / 2;
			double y1 = (h / div) * GridPos.get(g.getOrigin()).getY()
					+ Nheight / 2;
			double x2 = (w / div) * GridPos.get(g.getInto()).getX()
					+ Nwidth / 2;
			double y2 = (h / div) * GridPos.get(g.getInto()).getY()
					+ Nheight / 2;
			double dx = x2 - x1, dy = y2 - y1;
			double angle = Math.toDegrees(Math.atan2(dy, dx));
			int len = (int) Math.sqrt(dx * dx + dy * dy);
			len = 9;
			double wingsAngleDeg = 30; // wingspan of arrow
			double radB = Math.toRadians(angle + wingsAngleDeg + 180);
			double radC = Math.toRadians(angle - wingsAngleDeg + 180);
			// Line
			gc.strokeLine(x1, y1, x2, y2);
			// ArrowHead
			gc.beginPath();
			gc.moveTo(len * Math.cos(radB) + x2, len * Math.sin(radB) + y2);
			gc.lineTo(x2, y2);
			gc.lineTo(len * Math.cos(radC) + x2, len * Math.sin(radC) + y2);
			gc.stroke();
			gc.closePath();
			}
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
	 * Zeigt das beste Genome im unteren Bereich der GUI an
	 */
	private void showBestGenome() {
		Genome g = mainApp.myPool.getBestGenome();
		renderGenome(g, bestCanvas);
		// Labels mit Daten füllen, Genome zur Auswahl anzeigen
		bestIDLabel.setText(String.valueOf(g.getID()));
		bestNeuronsLabel.setText(String.valueOf(g.getNeurons().size()));
		bestGenesLabel.setText(String.valueOf(g.getGenes().size()));
		bestFitnessLabel.setText(String.valueOf(g.getFitness()));
	}

	/**
	 * Called when the user clicks on the "new generation" button.
	 */
	@FXML
	private void handleNewGeneration() {
		mainApp.execNewGeneration();
		showBestGenome();
	}

}