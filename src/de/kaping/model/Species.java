package de.kaping.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
	
	/* TODO: Hardcoding entfernen */
	private final double CrossOverChance = 0.75;
	private final double DeltaDisjoint   = 2.0;
	private final double DeltaWeight     = 0.4;
	private final double DeltaThreshold  = 1.0;
	
	private List<Genome> genomes;
	
	private double       topFitness;
	private int          staleness;
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
	public int getStaleness() 
	{
		return staleness;
	}
	
	/**
	 * Setzt den aktuellen Stagnationsindex, der angibt, inwieweit sich 
	 * innerhalb der Spezies in den letzten Generationen eine Verbesserung 
	 * eingestellt hat.
	 * @param staleness neuer Stagnationsindex
	 */
	public void setStaleness(int staleness) 
	{
		this.staleness = staleness;
	}

	/**
	 * Gibt die durchschnittliche Bewertung der Netzwerke innerhalb dieser 
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
	 * Entfernt ein bestimmtes Netzwerk aus der Liste aller Netzwerke dieser 
	 * Spezies.
	 * TODO: Wenn letztes Netzwerk, dann Löschen von Spezies (?)
	 * @param genome zu entfernendes Netzwerk aus dieser Spezies
	 * @return Wahrheitswert, ob Netzwerk entfernt werden konnte
	 */
	public boolean deleteGenome(Genome genome)
	{
		return genomes.remove(genome);
	}
	
	/**
	 * Berechnet die durchschnittliche Bewertung aller Netzwerke dieser Spezies 
	 * und speichert sie in <code>averageFitness</code>. Zusätzlich wird der Wert
	 * auch zurückgegeben.
	 * @return durchschnittliche Bewertung
	 */
	public double calculateAverageFitness()
	{
		double sum = 0.;
		
		for(Genome gen : this.genomes)
			sum += gen.getFitness();
		
		this.averageFitness = sum / genomes.size();
		
		return averageFitness;
	}
	
	/**
	 * Entfernt die schlechtesten Netzwerke der Spezies.
	 * @param leaveOne Gibt an, ob die Hälfte oder alle bis auf ein Netzwerk 
	 *        entfernt werden
	 */
	public void removeWeakGenomes(boolean leaveOne)
	{
		/* die obere Hälfte (aufgerundet) wird behalten */
		int keep = (this.genomes.size() / 2) + (this.genomes.size() % 2);
		
		if(leaveOne)
			keep = 1;
		
		this.genomes.sort(null);
		
		this.genomes = this.genomes.subList(0, keep);
	}
	
	/**
	 * Erzeugt ein neues Netzwerk auf Basis dieser Spezies.
	 * Per Zufall wird ausgewählt, ob dabei zwei Netzwerke zusammengeführt werden
	 * oder nur ein einzelnes Netzwerk als Basis dient. 
	 * Auf jeden Fall wird am Ende ein Mutationszyklus durchlaufen.
	 * @return
	 */
	public Genome breedChild()
	{
		Random rn    = new Random();
		Genome child = null;
		
		if(Math.random() < CrossOverChance && genomes.size() > 1)
		{
			Genome parent1 = genomes.get(rn.nextInt(genomes.size()));
			Genome parent2 = genomes.get(rn.nextInt(genomes.size()));
			
			child = parent1.matchGenomes(parent2);
		}
		else
		{
			Genome parent1 = genomes.get(rn.nextInt(genomes.size()));
			
			child = parent1.copyGenome();
		}
		
		child.mutateGenome();
		
		return child;
	}
	
	/**
	 * Prüft, ob ein gegebenes Netzwerk ähnlich genug ist, um Teil des Netzwerkes 
	 * zu sein. Hierbei wird immer nur die Ähnlichkeit zu einem Netzwerk der 
	 * Species geprüft.
	 * @param genome zu prüfende Netzwerk
	 * @return Wahrheitswert, ob Netzwerk ähnlich genug ist
	 */
	public boolean isSameSpecies(Genome genome)
	{
		genomes.sort(null);
		Genome speciesGenome = genomes.get(0);
		
		double deltaD = genome.deltaStructure(speciesGenome) * DeltaDisjoint;
		double deltaW = genome.deltaWeight(speciesGenome) * DeltaWeight;
		
		return ((deltaD + deltaW) < DeltaThreshold);
	}

}
