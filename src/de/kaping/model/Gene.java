package de.kaping.model;

/**
 * Erzeugt gewichtete Verbindungen zwischen Neuronen.
 * Die Verbindungen sind das Herzstück des Netzwerkes. Durch die Verbindung von
 * den Neuronen können diese Daten miteinander austauschen (Ihre Werte) die über
 * die <code>weight</code> (Gewichtung) der Verbindung Einfluss auf den 
 * Kommunikationspartner ausüben.
 * <p>
 * Verbindungen sind stets einseitig, sie gehen vom <code>origin</code> zum 
 * <code>into</code>. Sollte eine Verbindung inaktiv sein, so wird dies über
 * <code>enabled=false</code> dargestellt. Dies wird gespeichert, da durch 
 * Mutierungen des Netzwerkes inaktive Verbindung reaktiviert werden können.
 * 
 * @author MPreloaded
 */
public class Gene {
	
	private Neuron into;
	private Neuron origin;
	private double weight;
	private boolean enabled;
	
	/**
	 * Standard Konstruktor
	 */
	public Gene()
	{
		this(null, null, 0.0);
	}
	
	/**
	 * Konstruktor, der Ursprung und Ziel definiert.
	 * @param origin Ursprung der Verbindung
	 * @param into Ziel der Verbindung
	 */
	public Gene(Neuron origin, Neuron into)
	{
		this(origin,  into,  0.0);
	}
	
	/**
	 * Konstruktor, der zusätzlich zu Ursprung und Ziel bereits die Gewichtung
	 * definiert.
	 * @param origin Ursprung der Verbindung
	 * @param into Ziel der Verbindung
	 * @param weight Gewichtung der Verbindung
	 */
	public Gene(Neuron origin, Neuron into, double weight)
	{
		super();
		this.enabled = true;
		this.weight  = weight;
		this.origin  = origin;
		this.into    = into;
	}
	
	/**
	 * Verändert die Gewichtung.
	 * @param weight Neue Gewichtung
	 */
	public void setWeight(double weight)
	{
		this.weight = weight;
	}
	
	/**
	 * Gibt die aktuelle Gewichtung der Verbindung zurück.
	 * @return aktuelle Gewichtung
	 */
	public double getWeight()
	{
		return this.weight;
	}
	
	/**
	 * Aktivieren oder Deaktivieren der Verbindung.
	 * @param enabled neuer Aktivitätsmodus der Verbindung
	 */
	public void setEnabled(boolean enabled)
	{
		this.enabled = enabled;
	}
	
	/**
	 * Gibt den aktuellen Aktivitätsmodus der Verbindung zurück.
	 * @return aktueller Aktivitätsmodus
	 */
	public boolean getEnabled()
	{
		return this.enabled;
	}
	
	/**
	 * Setzen eines neuen Zielneurons.
	 * Sollte an sich nicht/selten verwendet werden, da für eine Veränderung
	 * des Netzwerkes neue Verbindungen erstellt werden.
	 * @param into neues Zielneuron
	 */
	public void setInto(Neuron into)
	{
		this.into = into;
	}
	
	/**
	 * Gibt aktuelles Zielneuron zurück.
	 * @return Zielneuron
	 */
	public Neuron getInto()
	{
		return this.into;
	}
	
	/**
	 * Setzen eines neuen Ursprungsneurons.
	 * Siehe setInto.
	 * @param origin neues Ursprungsneuron
	 */
	public void setOrigin(Neuron origin)
	{
		this.origin = origin;
	}
	
	/**
	 * Gibt aktuelles Ursprungsneuron zurück.
	 * @return Ursprungsneuron
	 */
	public Neuron getOrigin()
	{
		return this.origin;
	}
	
	/**
	 * Überprüfen, ob ein zweites Gene die gleiche verbindung darstellt.
	 * Implementiert, da <code>equals</code> auf Objektgleichheit prüft, hier
	 * aber nur überprüft werden soll, ob der Ursprung und das Ziel identisch sind.
	 * @param gene2 Zweite Verbindung, deren Ähnlichkeit überprüft werden soll
	 * @return Wahrheitswert, ob beide Gene die gleiche Verbindung darstellen.
	 */
	public boolean isEqual(Gene gene2)
	{
		if((this.origin == gene2.origin) && (this.into == gene2.into))
			return true;
		
		return false;
	}
}