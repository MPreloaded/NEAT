package de.kaping.model;

/*------------------------------------------------------------------------------
 * Class Gene
 * Stellt eine gewichtete Verbindung zwischen Neuronen dar.
------------------------------------------------------------------------------*/
public class Gene {
	
	private Neuron into;
	private Neuron origin;
	private double weight;
	private boolean enabled;
	
	public Gene(Neuron origin, Neuron into)
	{
		this(origin,  into,  0.0);
	}
	
	public Gene(Neuron origin, Neuron into, double weight)
	{
		super();
		this.enabled = true;
		this.weight  = weight;
		this.origin  = origin;
		this.into    = into;
	}
	
	/* Setzen einer neuen Gewichtung für die Verbindung */
	public void setWeight(double weight)
	{
		this.weight = weight;
	}
	
	/* Rückgabe der aktuellen Gewichtung */
	public double getWeight()
	{
		return this.weight;
	}
	
	/* Setzen, ob die Verbindung aktiv oder inaktiv ist */
	public void setEnabled(boolean enabled)
	{
		this.enabled = enabled;
	}
	
	/* Rückgabe, ob die aktuelle Verbindung aktiv ist */
	public boolean getEnabled()
	{
		return this.enabled;
	}
	
	/* Setzen eines neuen Zielneurons */
	public void setInto(Neuron into)
	{
		this.into = into;
	}
	
	/* Rückgabe auf welches Neuron die Verbindung zeigt */
	public Neuron getInto()
	{
		return this.into;
	}
	
	/* Setzen eines neuen Ursprungs */
	public void setOrigin(Neuron origin)
	{
		this.origin = origin;
	}
	
	/* Rückgabe aus welchem Neuron die Verbindung entspringt */
	public Neuron getOrigin()
	{
		return this.origin;
	}
	
	/* Überprüfung, ob zwei Genes identische Ursprünge und Ziele haben */
	public boolean isEqual(Gene gene2)
	{
		if((this.origin == gene2.origin) && (this.into == gene2.into))
			return true;
		
		return false;
	}
}
