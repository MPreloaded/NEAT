package de.kaping.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/*------------------------------------------------------------------------------
 * Class Genome
 * Umfasst ein Netzwerk aus Neuronen und Verbindungen
------------------------------------------------------------------------------*/
public class Genome {
	
	private List<Neuron> neurons;
	private List<Gene>   genes;
	
	private double       fitness;
	private double       adjustedFitness;
	
	/* Enthält alle Mutations-/ relevanten Raten für das Netzwerk*/
	private double[]     rates;
	
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
	
	/* Setzen der aktuellen Fitness des Netzwerkes */
	public void setFitness(double fitness)
	{
		this.fitness = fitness;
	}
	
	/* Rückgabe der aktuellen Fitness des Netzwerkes */
	public double getFitness()
	{
		return this.fitness;
	}
	
	/* Setzen der gewichteten Fitness des Netzwerkes */
	public void setAdjustedFitness(double fitness)
	{
		this.adjustedFitness = fitness;
	}
	
	/* Rückgabe der gewichteten Fitness des Netzwerkes */
	public double getAdjustedFitness()
	{
		return this.adjustedFitness;
	}
	
	/* Setzen einer neuen Liste Neuronen */
	public void setNeurons(List<Neuron> neurons)
	{
		this.neurons = neurons;
	}
	
	/* Rückgabe der Liste aller Neuronen des Netzwerkes */
	public List<Neuron> getNeurons()
	{
		return this.neurons;
	}
	
	/* Setzen einer neuen Liste Verbindungen */
	public void setGenes(List<Gene> genes)
	{
		this.genes = genes;
	}
	
	/* Rückgabe der Liste aller Verbindungen des Netzwerkes */
	public List<Gene> getGenes()
	{
		return this.genes;
	}
	
	/* Hinzufügen einer neuen Verbindung zum Netzwerk */
	public boolean addGene(Gene gene)
	{
		if(!genes.contains(gene))
			if(neurons.contains(gene.getInto()) && 
					neurons.contains(gene.getOrigin()))
				return genes.add(gene);
		
		return false;				
	}
	
	/* Hinzufügen eines neuen Neurons zum Netzwerk */
	public boolean addNeuron(Neuron neuron)
	{
		if(!neurons.contains(neuron))
			return neurons.add(neuron);
		
		return false;
	}
	
	/* Ausführen eines Mutationszyklus */
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
		
		Gene newGene1 = new Gene(gene.getOrigin(), newNeuron, gene.getWeight());
		Gene newGene2 = new Gene(newNeuron, gene.getInto(), 1.0);
		
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
}
