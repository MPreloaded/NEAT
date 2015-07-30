package de.kaping.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Beschreibung eines Netzwerkes durch Zusammenfassung von Neuronen und Genes.
 * Ein Netzwerk wird durch seine Performance bewertet (<code>fitness</code>).
 * Zusätzlich werden die Mutierungsraten hier gespeichert und durch Aufrufe von 
 * außen verändert.
 * <p>
 * Die <code>adjustedFitness</code> steht für eine veränderte, angepasste 
 * Performance-Berwertung, die für die tatsächliche Auswahl überlebender 
 * Netzwerke verwendet wird.
 * @author MPreloaded
 */
public class Genome implements Comparable<Genome>{
	
	private List<Neuron> neurons;
	private List<Gene>   genes;
	
	private double       fitness;
	private double       adjustedFitness;
	
	/* Enthält alle Mutations-/ relevanten Raten für das Netzwerk*/
	private double[]     rates;
	
	/**
	 * Konstruktor
	 */
	public Genome()
	{
		super();
		
		/* TODO: Entfernen des Hardcoden */

		rates    = new double[6];
		rates[0] = 0.25;  /* Ändern aller Gewichtungen */
		rates[1] = 2.0;   /* Hinzufügen von Verbindungen */
		rates[2] = 0.5;   /* Trennen einer Verbindung durch Einfügen Neuron */
		rates[3] = 0.4;   /* Hinzufügen von BIAS-Verbindung */
	   rates[4] = 0.4;   /* Deaktivieren einer aktiven Verbindung */
	   rates[5] = 0.2;   /* Aktivieren einer inaktiven Verbindung */
	   
	   neurons  = new ArrayList<Neuron>();
	   genes    = new ArrayList<Gene>();
	   fitness  = 0.0;
	   adjustedFitness = 0.0;
	}
	
	/**
	 * Setzt die aktuelle Bewertung des Netzwerkes neu.
	 * @param fitness neue Bewertung
	 */
	public void setFitness(double fitness)
	{
		this.fitness = fitness;
	}
	
	/**
	 *  Gibt die aktuelle Bewertung des Netzwerkes zurück.
	 *  @return aktuelle Bewertung
	 */
	public double getFitness()
	{
		return this.fitness;
	}
	
	/**
	 * Setzt die tatsächliche Bewertung des Netzwerkes neu.
	 * @param fitness neue Bewertung
	 */
	public void setAdjustedFitness(double fitness)
	{
		this.adjustedFitness = fitness;
	}
	
	/**
	 * Gibt die tatsächliche Bewertung des Netzwerkes zurück.
	 * @return tatsächliche Bewertung
	 */
	public double getAdjustedFitness()
	{
		return this.adjustedFitness;
	}
	
	/**
	 * Setzt eine Liste von Neuronen des Netzwerkes.
	 * Praktisch, falls ein Netzwerk kopiert werden soll.
	 * @param neurons Liste von Neuronen
	 */
	public void setNeurons(List<Neuron> neurons)
	{
		this.neurons = neurons;
	}
	
	/**
	 * Gibt eine Liste aller Neuronen des Netzwerkes zurück.
	 * @return Liste aller Neuronen
	 */
	public List<Neuron> getNeurons()
	{
		return this.neurons;
	}
	
	/**
	 * Setzt eine Liste von Verbindungen des Netzwerkes.
	 * @param genes Liste von Verbindungen
	 */
	public void setGenes(List<Gene> genes)
	{
		this.genes = genes;
	}
	
	/**
	 * Gibt eine Liste aller Verbindungen des Netzwerkes zurück.
	 * @return Liste aller Verbindungen
	 */
	public List<Gene> getGenes()
	{
		return this.genes;
	}
	
	/**
	 * Fügt eine einzelne Verbindung zum Netzwerk hinzu.
	 * @param gene neue Verbindung
	 * @return Wahrheitswert, ob Verbindung hinzugefügt werden konnte
	 */
	public boolean addGene(Gene gene)
	{
		if(!genes.contains(gene))
			if(neurons.contains(gene.getInto()) && 
					neurons.contains(gene.getOrigin()))
				return genes.add(gene);
		
		return false;				
	}
	
	/** 
	 * Fügt ein neues Neuron zum Netzwerk hinzu.
	 * @param neuron neues Neuron
	 * @return Wahrheitswert, ob Neuron hinzugefügt werden konnte
	 */
	public boolean addNeuron(Neuron neuron)
	{
		if(!neurons.contains(neuron))
			return neurons.add(neuron);
		
		return false;
	}
	
	/**
	 * Kopiert dieses Netzwerk.
	 * @return kopiertes Objekt
	 */
	public Genome copyGenome()
	{
		Genome copy = new Genome();
		
		copy.setNeurons(neurons);
		copy.setGenes(genes);
		
		return copy;
	}
	
	/**
	 * Kombiniert dieses Netzwerk mit einem zweiten, um ein neues Netzwerk zu 
	 * erzeugen.
	 * @param gen2 zweites Netzwerk zur Kombination
	 * @return neu generiertes Netzwerk
	 */
	public Genome matchGenomes(Genome gen2)
	{
		return null;
	}
	
	/**
	 * Führt einen Mutationszyklus aus.
	 * Das bedeutet:
	 * <ul>
	 * <li>Verändern der Mutationsraten (Zufällig erhöhen oder verringern)</li>
	 * <li>Durch Zufall auswählen, ob die jeweiligen Mutationen ausgeführt werden
	 * </li>
	 * <li>Linkmutation (Hinzufügen neuer Verbindungen) </li>
	 * <li>Neuronmutation (Trennen einer Verbindung in zwei) </li>
	 * <li>Biasmutation (Hinzufügen neuer Verbindung ausgehend vom Biasneuron) </li>
	 * <li>Disablemutation (Deaktivieren einer aktiven Verbindung) </li>
	 * <li>Enablemutation (Aktivieren einer inaktiven Verbindung) </li>
	 * </ul>
	 */
	public void mutateGenome()
	{
		/* erst Raten modifizieren... */
		this.alterRates();
		
		/* ... dann gegebenenfalls Mutierungen durchführen */
		if(Math.random() < rates[0])
			this.mutateConnections();
		
		for(int i = 1; i < 6; i++)
		{
			double rate = rates[i];
			
			while(rate > 1.)
			{
				if(Math.random() < rate)
				{
					switch(i)
					{
					case 1:
						mutateLink(false);     break;
					case 2:
						mutateNode();          break;
					case 3:
						mutateLink(true);      break;
					case 4:
						mutateEnable(true);    break;
					case 5:
						mutateEnable(false);   break;
					default:
						/* Das kann nicht passieren!!! */
					}
				}
				
				rate -= 1.;
			}
		}		
	}
	
	/**
	 * Berechnet einen Wert, in wieweit sich zwei Netzwerke in ihrer Struktur
	 * unterscheiden.
	 * @param gen2 zweites Netzwerk
	 * @return Strukturunterscheidungsgrad
	 */
	public double deltaStructure(Genome gen2)
	{
		int structuralDifferences = 0;
		int size1                 = this.genes.size();
		int size2                 = gen2.getGenes().size();
		double n                  = (double) Math.max(size1, size2);
		
		this.genes.sort(null);
		gen2.getGenes().sort(null);
		
		/* Berechne die maximale Innovationsnummer */
		int maxInn = Math.max(this.genes.get(size1-1).getHistoricalMarking(), 
							gen2.getGenes().get(size2-1).getHistoricalMarking());
		
		boolean[] inn1 = new boolean[maxInn];
		boolean[] inn2 = new boolean[maxInn];
		
		for(Gene g : this.genes)
			inn1[g.getHistoricalMarking() - 1] = true;
		for(Gene g : gen2.getGenes())
			inn2[g.getHistoricalMarking() - 1] = true;
		
		for(int i = 0; i < maxInn; i++)
			if(inn1[i] != inn2[i])
				structuralDifferences++;
		
		return structuralDifferences / n;
	}
	
	/**
	 * Berechnet einen Wert, in wieweit sich bei übereinstimmenden Verbindungen
	 * zwischen zwei Netzwerken die Gewichtungen im Durchschnitt unterscheiden.
	 * @param gen2 zweites Netzwerk
	 * @return durchschnittlicher Gewichtungsunterschied
	 */
	public double deltaWeight(Genome gen2)
	{
		double deltaWeight = 0.;
		int size1          = this.genes.size();
		int size2          = gen2.getGenes().size();
		int    n           = 0;
		
		this.genes.sort(null);
		gen2.getGenes().sort(null);
		
		/* Berechne die maximale Innovationsnummer */
		int maxInn = Math.max(this.genes.get(size1-1).getHistoricalMarking(), 
							gen2.getGenes().get(size2-1).getHistoricalMarking()) + 1;
		
		Gene[] inn = new Gene[maxInn];
		
		for(Gene g : gen2.getGenes())
			inn[g.getHistoricalMarking()] = g;
		
		for(Gene g : this.genes)
			if(inn[g.getHistoricalMarking()] != null)
			{
				Gene g2 = inn[g.getHistoricalMarking()];
				deltaWeight += Math.abs(g.getWeight() - g2.getWeight());
				n++;
			}
		
		return deltaWeight / n;
	}

	/* Änderung aller Gewichtungen */
	private void mutateConnections()
	{
		/* TODO: Vielleicht Hard Coding entfernen und einen Parameter einführen */
		double step      = 0.1;
		double lowChange = 0.9;
				
		for(int i = 0; i < genes.size(); i++)
		{
			Gene gene = genes.get(i);
			if(Math.random() < lowChange)
				gene.setWeight(gene.getWeight() + Math.random()*2*step - step);
			else
				gene.setWeight(Math.random()*4 - 2);
		}
	}
	
	/* Hinzufügen einer neuen Verbindung */
	private void mutateLink(boolean bias)
	{
		Random rn = new Random();
		Neuron neuron1 = neurons.get(rn.nextInt(neurons.size()));
		Neuron neuron2 = neurons.get(rn.nextInt(neurons.size()));
		Neuron origin, into;
		
		/* wenn beide zufälligen Neuronen Input sind, dann Abbruch */
		if(neuron1.getType() == 0 && neuron2.getType() == 0)
			return;
		
		/* sollte ein Neuron Input sein, so soll es der Origin werden */
		if(neuron2.getType() == 0)
		{
			origin = neuron2;
			into   = neuron1;
		}
		else
		{
			origin = neuron1;
			into   = neuron2;
		}
		
		if(bias)
			origin = this.getBias();
		
		Gene newGene = new Gene(origin, into);
		
		/* Besteht diese Verbindung bereits, dann Abbruch */
		for(int i = 0; i < genes.size(); i++)
			if(newGene.isEqual(genes.get(i)))
				return;
			
		/* Alle Tests erfolgreich: neue verbindung einrichten */
		newGene.setWeight(rn.nextDouble()*4 - 2);
		this.addGene(newGene);
	}
	
	/* Trennen einer Verbindung durch Einfügen eines Neurons */
	private void mutateNode()
	{
		Random rn = new Random();
		Gene gene = genes.get(rn.nextInt(genes.size()));
		
		/* bei inaktiver Verbindung wird die Mutierung abgebrochen */
		if(!gene.getEnabled())
			return;
		
		gene.setEnabled(false);
		Neuron newNeuron = new Neuron(2 /* hidden */);
		
		Gene newGene1 = new Gene(gene.getOrigin(), newNeuron, 1.0);
		Gene newGene2 = new Gene(newNeuron, gene.getInto(), gene.getWeight());
		
		this.addGene(newGene1);
		this.addGene(newGene2);
		this.addNeuron(newNeuron);
	}
	
	/* (De-)Aktivieren einer (in-)aktiven Verbindung */
	private void mutateEnable(boolean enable)
	{
		Random rn = new Random();
		List<Gene> candidates = new ArrayList<Gene>();
		
		for(int i = 0; i < genes.size(); i++)
			if (genes.get(i).getEnabled() == enable)
				candidates.add(genes.get(i));
		
		/* Wenn es keine (in-)aktive Verbindung gibt, Abbruch */
		if (candidates.size() == 0)
			return;
		
		Gene gene = candidates.get(rn.nextInt(candidates.size()));
		gene.setEnabled(!gene.getEnabled());
	}
	
	/* Ändern der Raten zur Entwicklung verschiedenster Netzwerke */
	private void alterRates()
	{
		/* 0.95 * 1.05263 ~= 1 
		 * TODO: Vielleicht Hardcoding entfernen und einen Parameter einführen */
		for(int i = 0; i < 6; i++)
			if (Math.random() < 0.5)
				rates[i] *= 0.95;
			else
				rates[i] *= 1.05263;
	}
	
	
	/* Suchen des Biasneurons innerhalb des Netzwerkes */
	private Neuron getBias()
	{
		Neuron bias = null;
		
		/* Es sollte nur ein Biasneuron vorhanden sein. Sollten mehrere 
		 * existieren, so wird das erste ausgewählt.
		 */
		for(int i = 0; i < neurons.size(); i++)
			if(neurons.get(i).getType() == 3)
			{
				bias = neurons.get(i);
				break;
			}
		
		/* Eigentlich nicht nötig, zur Sicherheit */
		if(bias == null)
		{
			bias = new Neuron(3);
			this.addNeuron(bias);
		}
		
		return bias;
	}

	@Override
	/**
	 * Vergleicht die Bewertungen der beiden Netzwerke.
	 * Gibt -1 zurück, wenn die Bewertung dieses Netzwerkes besser ist als die 
	 * von <code>o</code>, 1 wenn schlechter und 0 wenn identisch. Somit
	 * wird sicher gestellt, dass die höchste Fitness an erster Stelle steht.
	 */
	public int compareTo(Genome o)
	{
		/* TODO: fitness oder adjustedFitness ? */
		double deltaFitness = this.fitness - o.getFitness();
		
		if(deltaFitness > 0)
			return -1;
		else if (deltaFitness < 0)
			return 1;
		else
			return 0;
	}
}
