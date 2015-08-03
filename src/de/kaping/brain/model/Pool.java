package de.kaping.brain.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Zusammenfassung aller Netzwerke (Genomes) zu einer Population. Sorgt dafür,
 * dass Operationen über die gesamte Population durchgeführt werden können, und
 * somit sichergestellt werden kann, dass sich die Population in die richtige
 * Richtung entwickelt.
 * 
 * @author MPreloaded
 */
public class Pool {

	// @SuppressWarnings("unused")
	private static final Logger log = LogManager.getLogger();

	private final int StaleSpecies = 15;
	private final int Population = 100;

	private static Pool instance;

	private IntegerProperty currentSpecies;
	private IntegerProperty currentGenome;

	private DoubleProperty topFitness;
	private IntegerProperty generation;
	private IntegerProperty historicalMarking;

	private ObservableList<Species> species;
	private ObservableList<Gene> generationMarkings;
	private List<Neuron> newNeurons;

	/**
	 * Konstruktor
	 */
	private Pool()
	{
		super();
		this.currentGenome = new SimpleIntegerProperty(0);
		this.currentSpecies = new SimpleIntegerProperty(0);
		this.topFitness = new SimpleDoubleProperty(0.0);
		this.generation = new SimpleIntegerProperty(0);
		this.historicalMarking = new SimpleIntegerProperty(0);

		this.species = FXCollections.observableArrayList();
		this.generationMarkings = FXCollections.observableArrayList();

		this.newNeurons = new ArrayList<Neuron>();
	}

	/**
	 * Initialisiert eine Population mit <code>input</code> Inputneuronen und
	 * output Outputneuronen. Die Liste der zurückgegebenen Neuronen enthält
	 * zunächst alle Inputneuronen (index 0 - (input-1) dann das Biasneuron
	 * (index input) und zuletzt die Ouputneuronen (index (input + 1) - (input +
	 * output)). Werte der Inputneuronen sind Standardmäßig auf 1, und die der
	 * Outputneuronen auf 0 gesetzt.
	 * 
	 * @param input Anzahl Inputneuronen
	 * @param output Anzahl Outputneuronen
	 * @return Liste der erstellten Neuronen, um Werte zu übertragen und zu
	 *         empfangen
	 */
	public ObservableList<Neuron> initializePool(int input, int output)
	{
		log.trace("ENTER " + this.getClass().getName() + ".initializePool()");

		ObservableList<Neuron> neurons = FXCollections.observableArrayList();
		int inn = -1;

		/* Inputneuronen */
		for (int i = 0; i < input; i++)
		{
			Neuron in = new Neuron(Type.INPUT, 1., null, inn--);
			neurons.add(in);
		}

		/* Biasneuron */
		Neuron bias = new Neuron(Type.BIAS, 1., null, inn--);
		neurons.add(bias);

		/* Outputneuronen */
		for (int i = 0; i < output; i++)
		{
			Neuron out = new Neuron(Type.OUTPUT, 0., null, inn--);
			neurons.add(out);
		}

		for (int i = 0; i < Population; i++)
		{
			/* Erstelle eigene Liste für jedes Netzwerk */
			ObservableList<Neuron> ownNeurons = FXCollections
				.observableArrayList();
			for (Neuron n : neurons)
			ownNeurons.add(n);
			Genome genome = new Genome(ownNeurons, true);
			this.addChildToSpecies(genome);
		}

		log.trace(" EXIT " + this.getClass().getName() + ".initializePool()");
		return neurons;
	}

	/**
	 * Simuliert alle Netzwerke innerhalb der Population und weißt ihnen eine
	 * Bewertung zu.
	 * 
	 * @param function Implementierung einer Bewertungsfunktion
	 * @param args weitere Argumente, die an die Bewertungsfunktion übergeben
	 *           werden
	 */
	public void evaluateGenomes(AbstractFunction function, Object[] args)
	{
		for (Species s : this.species)
			for (Genome g : s.getGenomes())
			g.setFitness(function.evaluateNetwork(g, args));
	}

	/**
	 * Gibt alle Spezies der Population innerhalb dieser Generation zurück.
	 * 
	 * @return Liste aller Spezies
	 */
	public ObservableList<Species> getSpecies()
	{
		return species;
	}

	/**
	 * Setzt die Liste aller Spezies für die Population neu. VORSICHT! Kann bei
	 * falscher Anwendung die Population resetten.
	 * 
	 * @param species neue Liste von Spezies
	 */
	public void setSpecies(ObservableList<Species> species)
	{
		this.species = species;
	}

	/**
	 * Gibt die aktuell in der Simulation zu bearbeitende Species aus.
	 * 
	 * @return zu simulierende Species
	 */
	public int getCurrentSpecies()
	{
		return currentSpecies.get();
	}

	/**
	 * Setzt die aktuell zu simulierenden Spezies neu.
	 * 
	 * @param currentSpecies zu simulierende Species
	 */
	public void setCurrentSpecies(int currentSpecies)
	{
		this.currentSpecies.set(currentSpecies);
	}

	/**
	 * Gibt das aktuell in der Simulation zu bearbeitende Netzwerk aus.
	 * 
	 * @return zu simulierendes Netzwerk
	 */
	public int getCurrentGenome()
	{
		return currentGenome.get();
	}

	/**
	 * Setzen des aktuell zu simulierende Netzwerk.
	 * 
	 * @param currentGenome zu simulierendes Netzwerk
	 */
	public void setCurrentGenome(int currentGenome)
	{
		this.currentGenome.set(currentGenome);
	}

	/**
	 * Gibt die Bewertung des erfolgreichsten Netzwerkes der Population aus.
	 * 
	 * @return beste Bewertung eines Netzwerkes der Population
	 */
	public double getTopFitness()
	{
		return topFitness.get();
	}

	/**
	 * Setzt eine neue Bewertung als höchste innerhalb der Population.
	 * 
	 * @param topFitness neue höchste Bewertung
	 */
	public void setTopFitness(double topFitness)
	{
		this.topFitness.set(topFitness);
	}

	/**
	 * Gibt die aktuelle Generationsnummer zurück.
	 * 
	 * @return Generationsnummer
	 */
	public int getGeneration()
	{
		return generation.get();
	}

	/**
	 * Setzt die Generationsnummer neu.
	 * 
	 * @param generation neue Generationsnummer
	 */
	public void setGeneration(int generation)
	{
		this.generation.set(generation);
	}
	
	public IntegerProperty getGenerationProperty() {
		return this.generation;
	}

	/**
	 * Gibt die Liste der neuen Neuronen dieser Generation zuück.
	 * 
	 * @return Liste der neuen Neuronen
	 */
	public List<Neuron> getNewNeurons()
	{
		return newNeurons;
	}

	/**
	 * Setzt die Liste der neuen Neuronen dieser Generation.
	 * 
	 * @param newNeurons neue Liste von Neuronen
	 */
	public void setNewNeurons(List<Neuron> newNeurons)
	{
		this.newNeurons = newNeurons;
	}

	/**
	 * Erhöht die aktuelle Innovationsnummer, da eine neue Verbindung vorkommt.
	 * 
	 * @return neue Innovationsnummer
	 */
	public int newInnovation()
	{
		log.debug("   Neue Innovation!");
		this.historicalMarking.set(this.historicalMarking.get() + 1);
		return this.historicalMarking.get();
	}

	/**
	 * Gibt die aktuelle Innovationsnummer zurück.
	 * 
	 * @return Innovationsnummer
	 */
	public int getHistoricalMarking()
	{
		return this.historicalMarking.get();
	}

	/**
	 * Fügt eine neue Verbindung in die Liste aller neuen Verbindungen dieser
	 * Generation ein.
	 * 
	 * @param gen neue Verbindung
	 * @return Wahrheitswert, ob Verbindung hinzugefügt werden konnte
	 */
	public boolean addNewGene(Gene gen)
	{
		if (!generationMarkings.contains(gen))
			return generationMarkings.add(gen);
		return false;
	}

	/**
	 * Fügt ein neues Neuron in die Liste aller neuen Neuronen dieser Generation
	 * ein.
	 * 
	 * @param neuron neues Neuron
	 * @return Wahrheitswert, ob das Neuron hinzugefügt werden konnte
	 */
	public boolean addNewNeuron(Neuron neuron)
	{
		log.debug("   adding new Neuron to this generation!");
		if (!newNeurons.contains(neuron))
			return newNeurons.add(neuron);
		return false;
	}

	/**
	 * Fügt eine neue Spezies zur Population hinzu. TODO: Abfrage, ob
	 * maxPopulation überschritten wird.
	 * 
	 * @param species neue Spezies
	 * @return Wahrheitswert, ob Spezies hinzugefügt werden konnte
	 */
	public boolean addSpecies(Species species)
	{
		if (!this.species.contains(species))
			return this.species.add(species);

		return false;
	}

	/**
	 * Entfernt stagnierende Spezies und setzt den Index für danach existierende
	 * neu.
	 */
	public void removeStaleSpecies()
	{
		for (Species spe : species)
		{
			spe.getGenomes().sort(null);

			if (spe.getGenomes().get(0).getFitness() > spe.getTopFitness())
			{
			spe.setTopFitness(spe.getGenomes().get(0).getFitness());
			spe.setStaleness(0);
			}
			else
			{
			spe.setStaleness(spe.getStaleness() + 1);
			}

			/* Entferne nur stagnierende, die nicht die beste Species sind */
			if (spe.getStaleness() > StaleSpecies
				&& !(spe.getTopFitness() >= this.topFitness.get()))
			{
			species.remove(spe);
			}
		}
	}

	/**
	 * Entfernt bei vielen Species die schwächsten.
	 */
	public void removeWeakSpecies()
	{
		double total = getTotalAverageFitness();

		for (Species spe : species)
		{
			spe.getGenomes().sort(null);

			if (spe.getGenomes().get(0).getFitness() > spe.getTopFitness())
			spe.setTopFitness(spe.getGenomes().get(0).getFitness());

			double strength = (spe.getAverageFitness() / total * Population);

			if (strength < 1.)
			species.remove(spe);
		}
	}

	/**
	 * Sucht innerhalb der Population nach einer geeigneten Species oder
	 * erstellt gegebenenfalls eine neue.
	 * 
	 * @param child einzuteilendes Netzwerk
	 */
	public void addChildToSpecies(Genome child)
	{
		for (Species s : species)
		{
			if (s.isSameSpecies(child))
			{
			s.addGenome(child);
			return;
			}
		}

		log.debug("  neue Species erstellt, da keine passende gefunden");
		Species newSpecies = new Species();
		newSpecies.addGenome(child);
		this.addSpecies(newSpecies);
	}

	/**
	 * Kontrolliert, ob die Verbindung innerhalb dieser Generation bereits
	 * aufgetreten ist und verteilt die gleiche, oder eine neue
	 * Innovationsnummer
	 * 
	 * @param gen neue Verbindung
	 */
	public void defineHistoricalMarking(Gene gen)
	{
		/* durchsuche alle neuen Verbindungen dieser Generation */
		for (Gene g : generationMarkings)
		{
			if (gen.isEqual(g))
			{
			gen.setHistoricalMarking(g.getHistoricalMarking());
			return;
			}
		}

		/* erstes Auftreten der Verbindung: neue Innovationsnummer und einfügen
		 * in Liste der neuen Verbindungen dieser Generation. */
		log.debug("   new Link: " + gen.getOrigin().getInnovation() + " zu "
			+ gen.getInto().getInnovation());
		gen.setHistoricalMarking(this.newInnovation());
		this.addNewGene(gen);
	}

	/**
	 * Leitet eine neue Generation ein.
	 */
	public void newGeneration()
	{
		log.trace("ENTER " + this.getClass().getName() + ".newGeneration()");
		Random rn = new Random();

		List<Genome> newGen = new ArrayList<Genome>();

		for (Species s : species)
			s.removeWeakGenomes(false);

		this.removeStaleSpecies();
		this.removeWeakSpecies();

		double total = getTotalAverageFitness();

		for (Species s : species)
		{
			int breed = (int) (s.getAverageFitness() / total * Population) - 1;
			for (int i = 0; i < breed; i++)
			newGen.add(s.breedChild());
		}

		for (Species s : species)
			s.removeWeakGenomes(true);

		/* Fülle restliche Plätze auf */
		while (newGen.size() + species.size() < Population)
		{
			Species s = species.get(rn.nextInt(species.size()));
			newGen.add(s.breedChild());
		}

		for (Genome child : newGen)
			this.addChildToSpecies(child);

		/* TODO: Reset newNeurons / generationMarkings */
		this.generation.set(this.generation.get() + 1);

		log.trace(" EXIT " + this.getClass().getName() + ".newGeneration()");
	}

	/* Berechnet die Summe aller durchschnittlichen Bewertungen */
	private double getTotalAverageFitness()
	{
		double sum = 0.;

		for (Species s : species)
		{
			sum += s.calculateAverageFitness();
		}

		return sum;
	}

	public static Pool getInstance()
	{
		if (instance == null)
			instance = new Pool();

		return instance;
	}

}
