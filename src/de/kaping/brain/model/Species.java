package de.kaping.brain.model;

import java.util.Random;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Model class for a Species.
 *
 * @author Keydara
 */
public class Species {

    private final StringProperty ID;
    private final DoubleProperty topFitness;
    private final DoubleProperty averageFitness;
    private final IntegerProperty staleness;
    
    private final DoubleProperty CrossOverChance;
    private final DoubleProperty DeltaDisjoint;
    private final DoubleProperty DeltaWeight;
    private final DoubleProperty DeltaThreshold;    
    
    private final ListProperty<Genome> genomes;

    /**
     * Default constructor.
     */
    public Species() {
        this(0.0, 0, 0.0, "#");
    }

    /**
     * Constructor with some initial data.
     * 
     * @param topFitness
     * @param staleness
     * @param averageFitness
     */
    public Species(double topFitness, int staleness, double averagefitness, String ID) {
    	this.ID = new SimpleStringProperty(ID);
        this.topFitness = new SimpleDoubleProperty(topFitness);
        this.staleness = new SimpleIntegerProperty(staleness);
        this.averageFitness = new SimpleDoubleProperty(averagefitness);
        this.genomes = new SimpleListProperty<Genome>();

        /* TODO: Hardcoding entfernen */
        this.CrossOverChance = new SimpleDoubleProperty(0.75);
        this.DeltaDisjoint = new SimpleDoubleProperty(2.0);
        this.DeltaWeight = new SimpleDoubleProperty(0.4);
        this.DeltaThreshold = new SimpleDoubleProperty(1.0);
    }

    /**
	 * Gibt die ID (Name) der Spezies zurück
	 * @return ID der Spezies
	 */
    public String getID() {
        return ID.get();
    }

    /**
	 * Setzt einen String als ID dieser Spezies
	 * @param ID neue ID
	 */
    public void setID(String ID) {
        this.ID.set(ID);
    }

    public StringProperty idProperty() {
        return ID;
    }
    
    /**
	 * Gibt die höchste Fitness eines Netzwerkes dieser Spezies zurück.
	 * @return höchste Fitness
	 */
    public double getTopFitness() {
        return topFitness.get();
    }

    /**
	 * Setzt einen Floatingpointwert als aktuell höchste Fitness eines Netzwerkes
	 * innerhalb dieser Spezies.
	 * @param topFitness neue höchste Bewertung
	 */
    public void setTopFitness(double topFitness) {
        this.topFitness.set(topFitness);
    }

    public DoubleProperty topFitnessProperty() {
        return topFitness;
    }
    
    /**
	 * Gibt zurück, inwieweit sich die höchste Bewertung innerhalb der Spezies 
	 * in den letzten Generationen verbessert hat.
	 * @return Stagnationsindex
	 */
    public int getStaleness() {
        return staleness.get();
    }

    /**
	 * Setzt den aktuellen Stagnationsindex, der angibt, inwieweit sich 
	 * innerhalb der Spezies in den letzten Generationen eine Verbesserung 
	 * eingestellt hat.
	 * @param staleness neuer Stagnationsindex
	 */
    public void setStaleness(int staleness) {
        this.staleness.set(staleness);
    }

    public IntegerProperty stalenessProperty() {
        return staleness;
    }
    
    /**
	 * Gibt die durchschnittliche Bewertung der Netzwerke innerhalb dieser 
	 * Spezies zurück.
	 * @return durchschnittliche Bewertung
	 */
    public double getAverageFitness() {
        return averageFitness.get();
    }

    /**
	 * Setzt die durchschnittliche Bewertung der Netzwerke innerhalb dieser 
	 * Spezies.
	 * @param averageFitness neue durchschnittliche Bewertung
	 */
    public void setAverageFitness(double averageFitness) {
        this.averageFitness.set(averageFitness);
    }

    public DoubleProperty averageFitnessProperty() {
        return averageFitness;
    }
    
    /**
	 * Gibt eine Liste aller in dieser Spezies gruppierten Netzwerke zurück.
	 * @return Liste der Netzwerke dieser Spezies
	 */
    public ObservableList<Genome> getGenomes() {
        return genomes.get();
    }

    /**
	 * Setzt eine neue Liste von Netzwerken für diese Spezies.
	 * @param genomes neue Liste von Netzwerken für diese Spezies
	 */
    public void setGenomes(ObservableList<Genome> genomes) {
        this.genomes.set(genomes);
    }

    public ListProperty<Genome> genomesProperty() {
        return genomes;
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
		
		this.averageFitness.set(sum / genomes.size());
		
		return averageFitness.get();
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
		
		this.genomes.set(FXCollections.observableList(this.genomes.subList(0, keep)));
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
		
		if(Math.random() < CrossOverChance.get() && genomes.size() > 1)
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
		
		double deltaD = genome.deltaStructure(speciesGenome) * DeltaDisjoint.get();
		double deltaW = genome.deltaWeight(speciesGenome) * DeltaWeight.get();
		
		return ((deltaD + deltaW) < DeltaThreshold.get());
	}

}