package de.kaping.model;

import java.util.List;

/*------------------------------------------------------------------------------
 * Class Genome
 * Umfasst ein Netzwerk aus Neuronen und Verbindungen
------------------------------------------------------------------------------*/
public class Genome {
	
	private List<Neuron> neurons;
	private List<Gene>   genes;
	
	private double       fitness;
	private double       adjustedFitness;
	
	public Genome()
	{
		super();
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
			if(neurons.contains(gene.getInto()) && neurons.contains(gene.getOrigin()))
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

}
