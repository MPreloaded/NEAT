package de.kaping.model;

import java.util.ArrayList;
import java.util.List;

/** 
 * Zusammenfassung verschiedener Netzwerke einer gewissen Ähnlichkeit zu einer
 * Spezies.
 * Spezien dienen dazu verschiedene Evolutionen zu schützen, die in der ersten 
 * Generation noch nicht zu einer Verbesserung führen, aber vielleicht in einer
 * späteren dafür zu einem gewaltigen Schritt. Somit müssen sich diese Netzwerke
 * zum Anfang nicht im Kontext der gesamten Population beweisen, sondern 
 * zunächst nur im Umfeld ihrer Spezies.
 * 
 * @author MPreloaded
 */
public class Species {
	
	private List<Genome> genomes;
	
	private double       topFitness;
	private double       staleness;
	private double       averageFitness;
	
	/**
	 * Konstruktor
	 */
	public Species()
	{
		super();
		this.topFitness     = 0;
		this.staleness      = 0;
		this.averageFitness = 0;
		this.genomes        = new ArrayList<Genome>();
	}

	/**
	 * Gibt die höchste Fitness eines Netzwerkes dieser Spezies zurück.
	 * @return höchste Fitness
	 */
	public double getTopFitness() 
	{
		return topFitness;
	}

	/**
	 * Setzt einen Floatingpointwert als aktuell höchste Fitness eines Netzwerkes
	 * innerhalb dieser Spezies.
	 * @param topFitness neue höchste Bewertung
	 */
	public void setTopFitness(double topFitness) 
	{
		this.topFitness = topFitness;
	}

	/**
	 * Gibt zurück, inwieweit sich die höchste Bewertung innerhalb der Spezies 
	 * in den letzten Generationen verbessert hat.
	 * @return Stagnationsindex
	 */
	public double getStaleness() 
	{
		return staleness;
	}
	
	/**
	 * Setzen des aktuellen Stagnationsindexes, der angibt, inwieweit sich 
	 * innerhalb der Spezies in den letzten Generationen eine Verbesserung 
	 * eingestellt hat.
	 * @param staleness neuer Stagnationsindex
	 */
	public void setStaleness(double staleness) 
	{
		this.staleness = staleness;
	}

	/**
	 * Gibt die durchschnittliche Bewertung der Netzwerke inenrhalb dieser 
	 * Spezies zurück.
	 * @return durchschnittliche Bewertung
	 */
	public double getAverageFitness() 
	{
		return averageFitness;
	}

	/**
	 * Setzt die durchschnittliche Bewertung der Netzwerke innerhalb dieser 
	 * Spezies.
	 * @param averageFitness neue durchschnittliche Bewertung
	 */
	public void setAverageFitness(double averageFitness) 
	{
		this.averageFitness = averageFitness;
	}

	/**
	 * Gibt eine Liste aller in dieser Spezies gruppierten Netzwerke zurück.
	 * @return Liste der Netzwerke dieser Spezies
	 */
	public List<Genome> getGenomes()
	{
		return genomes;
	}

	/**
	 * Setzt eine neue Liste von Netzwerken für diese Spezies.
	 * @param genomes neue Liste von Netzwerken für diese Spezies
	 */
	public void setGenomes(List<Genome> genomes)
	{
		this.genomes = genomes;
	}
	
	/**
	 * Fügt ein Netzwerk zu dieser Spezies hinzu. Eine Kontrolle, ob das Netzwerk
	 * den Ähnlichkeitsgrad der Spezies erfüllt passiert nicht.
	 * @param genome neues Netzwerk
	 * @return Wahrheitswert, ob Netzwerk hinzugefügt werden konnte
	 */
	public boolean addGenome(Genome genome)
	{
		if(!genomes.contains(genome))
			return genomes.add(genome);
		
		return false;
	}
	
	/**
	 * Entfernen eines bestimmten Netzwerkes aus der Liste aller Netzwerke dieser 
	 * Spezies.
	 * TODO: Wenn letztes Netzwerk, dann Löschen von Spezies (?)
	 * @param genome zu entfernendes Netzwerk aus dieser Spezies
	 * @return Wahrheitswert, ob Netzwerk entfernt werden konnte
	 */
	public boolean deleteGenome(Genome genome)
	{
		return genomes.remove(genome);
	}

}
