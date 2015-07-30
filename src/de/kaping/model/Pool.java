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
	
	private final int StaleSpecies = 15;
	private final int Population   = 300;
	
	private List<Species> species;
	
	private int           currentSpecies;
	private int           currentGenome;
	
	private double        topFitness;
	private int           generation;
	private int           historicalMarking;
	
	private List<Gene>    generationMarkings;
	
	/**
	 * Konstruktor
	 */
	public Pool()
	{
		super();
		this.currentGenome     = 0;
		this.currentSpecies    = 0;
		this.topFitness        = 0;
		this.generation        = 0;
		this.historicalMarking = 0;
		
		this.species            = new ArrayList<Species>();
		this.generationMarkings = new ArrayList<Gene>();
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
	 * Erhöht die aktuelle Innovationsnummer, da eine neue Verbindung vorkommt.
	 * @return neue Innovationsnummer
	 */
	public int newInnovation()
	{
		return ++this.historicalMarking;
	}
	
	/**
	 * Fügt eine neue Verbindung in die Liste aller neuen Verbindungen dieser 
	 * Generation ein.
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
			if(spe.getStaleness() > StaleSpecies && !(spe.getTopFitness() >= 
																	this.topFitness))
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
	 * Sucht innerhalb der Population nach einer geeigneten Species oder erstellt
	 * gegebenenfalls eine neue.
	 * @param child einzuteilendes Netzwerk
	 */
	public void addChildToSpecies(Genome child)
	{
		for(Species s : species)
			if (s.isSameSpecies(child))
			{
				s.addGenome(child);
				return;
			}
		
		Species newSpecies = new Species();
		newSpecies.addGenome(child);
	}
	
	/**
	 * Kontrolliert, ob die Verbindung innerhalb dieser Generation bereits 
	 * aufgetreten ist und verteilt die gleiche, oder eine neue Innovationsnummer
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
		gen.setHistoricalMarking(this.newInnovation());
		this.addNewGene(gen);
	}
		
	/* Berechnet die Summe aller durchschnittlichen Bewertungen */
	private double getTotalAverageFitness()
	{
		double sum = 0.;
		
		for(Species s : species)
			sum += s.getAverageFitness();

		return sum;
	}
	
}
