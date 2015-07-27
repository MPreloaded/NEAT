package de.kaping.model;

import java.util.List;

/*------------------------------------------------------------------------------
 * Class Pool
 * Umfasst ein Netzwerk aus Neuronen und Verbindungen
------------------------------------------------------------------------------*/
public class Pool {
	
	private List<Species> species;
	
	private int           currentSpecies;
	private int           currentGenome;
	
	private double        topFitness;
	private int           generation;
	
	public Pool()
	{
		super();
	}
	
	/* Rückgabe aller im Pool vorhandenen Spezien */
	public List<Species> getSpecies()
	{
		return species;
	}
	
	/* Setzen aller Spezien für den Pool */
	public void setSpecies(List<Species> species)
	{
		this.species = species;
	}

	/* Rückgabe der aktuell ausgewählten Spezies */
	public int getCurrentSpecies()
	{
		return currentSpecies;
	}

	/* Setzen der ausgewählten Spezies */
	public void setCurrentSpecies(int currentSpecies)
	{
		this.currentSpecies = currentSpecies;
	}

	/* Rückgabe des aktuellen Netzwerkes */
	public int getCurrentGenome()
	{
		return currentGenome;
	}

	/* Setzen des aktuellen Netzwerkes */
	public void setCurrentGenome(int currentGenome)
	{
		this.currentGenome = currentGenome;
	}

	/* Rückgabe der höchsten Fitness im Pool */
	public double getTopFitness()
	{
		return topFitness;
	}

	/* Setzen der höchsten Fitness im Pool */
	public void setTopFitness(double topFitness)
	{
		this.topFitness = topFitness;
	}

	/* Rückgabe der aktuellen Generationsnummer */
	public int getGeneration()
	{
		return generation;
	}

	/* Setzen der aktuellen Generationsnummer */
	public void setGeneration(int generation)
	{
		this.generation = generation;
	}
	
	/* Hinzufügen einer Spezies zum Pool */
	public boolean addSpecies(Species species)
	{
		if(!this.species.contains(species))
			return this.species.add(species);
		
		return false;
	}

}
