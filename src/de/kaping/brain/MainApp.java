package de.kaping.brain;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.kaping.brain.model.Pool;
import de.kaping.brain.model.Species;
import de.kaping.brain.view.PoolOverviewController;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MainApp extends Application {

	private static final Logger log = LogManager.getLogger();
	private Stage primaryStage;
	private BorderPane rootLayout;

	private Pool myPool = Pool.getInstance();

	@Override
	public void start(Stage primaryStage)
	{
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("NEAT Brain - unstableSandbox");

		initRootLayout();

		showPoolOverview();
	}

	public MainApp()
	{
		myPool.initializePool(2, 1, 10000);
		log.debug(
			"Pool initialized with " + myPool.getSpecies().size() + " species");
	}

	public ObservableList<Species> getPoolSpecies()
	{
		return myPool.getSpecies();
	}

	/**
	 * Initializes the root layout.
	 */
	public void initRootLayout()
	{
		try
		{
			// Load root layout from fxml file.
			FXMLLoader loader = new FXMLLoader();
			loader
				.setLocation(MainApp.class.getResource("view/RootLayout.fxml"));
			rootLayout = (BorderPane) loader.load();

			// Show the scene containing the root layout.
			Scene scene = new Scene(rootLayout);
			primaryStage.setScene(scene);
			primaryStage.show();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Shows the pool overview inside the root layout.
	 */
	public void showPoolOverview()
	{
		try
		{
			// Load pool overview.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(
				MainApp.class.getResource("view/PoolOverview.fxml"));
			AnchorPane personOverview = (AnchorPane) loader.load();

			// Set pool overview into the center of root layout.
			rootLayout.setCenter(personOverview);

			// Give the controller access to the main app.
			PoolOverviewController controller = loader.getController();
			controller.setMainApp(this);

		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Returns the main stage.
	 * 
	 * @return
	 */
	public Stage getPrimaryStage()
	{
		return primaryStage;
	}

	public static void main(String[] args)
	{
		launch(args);
	}
}