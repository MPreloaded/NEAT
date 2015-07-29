package de.kaping.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Zusammenfassung aller Netzwerke zu einer Population.
 * Sorgt dafür, dass Operationen über die gesamte Population durchgeführt werden 
 * können, und somit sichergestellt werden kann, dass sich die Population in die
 * richtige Richtung entwickelt.
 * 
 * @author MPreloaded
 */
public class Pool {
	
	private List<Species> species;
	
	private int           currentSpecies;
	private int           currentGenome;
	
	private double        topFitness;
	private int           generation;
	
	/**
	 * Konstruktor
	 */
	public Pool()
	{
		super();
		this.currentGenome  = 0;
		this.currentSpecies = 0;
		this.topFitness     = 0;
		this.generation     = 0;
		
		this.species        = new ArrayList<Species>();
	}
	
	/**
	 * Gibt alle Spezies der Population innerhalb dieser Generation zurück.
	 * @return Liste aller Spezies
	 */
	public List<Species> getSpecies()
	{
		return species;
	}
	
	/**
	 * Setzt die Liste aller Spezies für die Population neu.
	 * VORSICHT! Kann bei falscher Anwendung die Population resetten.
	 * @param species neue Liste von Spezies
	 */
	public void setSpecies(List<Species> species)
	{
		this.species = species;
	}

	/**
	 * Gibt die aktuell in der Simulation zu bearbeitende Species aus.
	 * @return zu simulierende Species
	 */
	public int getCurrentSpecies()
	{
		return currentSpecies;
	}

	/** 
	 * Setzt die aktuell zu simulierenden Spezies neu.
	 * @param currentSpecies zu simulierende Species
	 */
	public void setCurrentSpecies(int currentSpecies)
	{
		this.currentSpecies = currentSpecies;
	}

	/**
	 * Gibt das aktuell in der Simulation zu bearbeitende Netzwerk aus.
	 * @return zu simulierendes Netzwerk
	 */
	public int getCurrentGenome()
	{
		return currentGenome;
	}

	/**
	 * Setzen des aktuell zu simulierende Netzwerk.
	 * @param currentGenome zu simulierendes Netzwerk
	 */
	public void setCurrentGenome(int currentGenome)
	{
		this.currentGenome = currentGenome;
	}

	/**
	 * Gibt die Bewertung des erfolgreichsten Netzwerkes der Population aus.
	 * @return beste Bewertung eines Netzwerkes der Population
	 */
	public double getTopFitness()
	{
		return topFitness;
	}

	/**
	 * Setzt eine neue Bewertung als höchste innerhalb der Population.
	 * @param topFitness neue höchste Bewertung
	 */
	public void setTopFitness(double topFitness)
	{
		this.topFitness = topFitness;
	}

	/**
	 * Gibt die aktuelle Generationsnummer zurück.
	 * @return Generationsnummer
	 */
	public int getGeneration()
	{
		return generation;
	}

	/**
	 * Setzt die Generationsnummer neu.
	 * @param generation neue Generationsnummer
	 */
	public void setGeneration(int generation)
	{
		this.generation = generation;
	}
	
	/**
	 * Fügt eine neue Spezies zur Population hinzu.
	 * TODO: Abfrage, ob maxPopulation überschritten wird.
	 * @param species neue Spezies
	 * @return Wahrheitswert, ob Spezies hinzugefügt werden konnte
	 */
	public boolean addSpecies(Species species)
	{
		if(!this.species.contains(species))
			return this.species.add(species);
		
		return false;
	}

}
