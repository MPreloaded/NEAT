package de.kaping.model;

import java.util.List;

/*------------------------------------------------------------------------------
 * Class Neuron
 * Unterste Einheit eines Netzwerkes --> Stellt nur einen Knoten da.
------------------------------------------------------------------------------*/
public class Neuron {
	
	/* TODO: Change Type into enum */
	private int        type; /* input, output, hidden, bias */
	private List<Gene> incoming;
	private double     value;

	public Neuron(int type) 
	{
		this(type, 0.);
	}

	public Neuron(int type, double value)
	{
		super();
		this.type  = type;
		this.value = value;
	}
	
	/* Hinzufügen einer neuen eingehenden Verbindung */
	public boolean addIncoming(Gene inc)
	{                                             
		if(!incoming.contains(inc))
			return incoming.add(inc);
		
		return false;
		
	}
	
	/* Entfernen einer eingehenden Verbindung */
	public boolean deleteIncoming(Gene inc)
	{
		return incoming.remove(inc);
	}
	
	/* Setzen einer Liste aller bestehenden Verbindungen */
	public void setIncoming(List<Gene> incoming)
	{
		this.incoming = incoming;
	}
	
	/* Rückgabe aller bestehenden Verbindungen */
	public List<Gene> getIncoming()
	{
		return this.incoming;
	}
	
	/* Setzen eines Wertes (woher der kommt ist außerhalb[?]) */
	public void setValue(double value)
	{
		this.value = value;
	}
	
	/* Rückgabe des Wertes eines Neurons */
	public double getValue()
	{
		return this.value;
	}
	
	/* Rückgabe des Typen eines Neurons */
	public int getType()
	{
		return this.type;
	}
}
