package de.kaping.model;

import java.util.List;

/*------------------------------------------------------------------------------
 * Class Species
 * Enthält verschiedene, ähnliche Netzwerke und fasst sie zusammen
 * Durch Species werden Innovationen, die erst durch weitere Evolution zu einer
 * besseren Lösung führen geschützt.
------------------------------------------------------------------------------*/
public class Species {
	
	private List<Genome> genomes;
	
	private double       topFitness;
	private double       staleness;
	private double       averageFitness;
	
	public Species()
	{
		super();
	}

	/* Rückgabe der bisher höchsten Fitness innerhalb der Spezies */
	public double getTopFitness() 
	{
		return topFitness;
	}

	/* Setzen der höchsten Fitness innerhalb der Spezies */
	public void setTopFitness(double topFitness) 
	{
		this.topFitness = topFitness;
	}

	/* Rückgabe der Staleness (Variable um darzustellen, ob längere Zeit keine 
	 * Verbesserung aufgetaucht ist */
	public double getStaleness() 
	{
		return staleness;
	}
	
	/* Setzen der Staleness (Erklärung beim Getter) */
	public void setStaleness(double staleness) 
	{
		this.staleness = staleness;
	}

	/* Rückgabe der durchschnittlichen Fitness innerhalb der Spezies */
	public double getAverageFitness() 
	{
		return averageFitness;
	}

	/* Setzen der durchschnittlichen Fitness innerhalb der Spezies */
	public void setAverageFitness(double averageFitness) 
	{
		this.averageFitness = averageFitness;
	}

	/* Rückgabe aller Netzwerke innerhalb der Spezies */
	public List<Genome> getGenomes()
	{
		return genomes;
	}

	/* Setzen einer Liste von Netzwerken für die Spezies */
	public void setGenomes(List<Genome> genomes)
	{
		this.genomes = genomes;
	}
	
	/* Hinzufügen eines Netzwerkes zur Spezies */
	public boolean addGenome(Genome genome)
	{
		if(!genomes.contains(genome))
			return genomes.add(genome);
		
		return false;
	}
	
	/* Entfernene eines Netzwerkes aus der Spezies */
	public boolean deleteGenome(Genome genome)
	{
		return genomes.remove(genome);
	}

}
