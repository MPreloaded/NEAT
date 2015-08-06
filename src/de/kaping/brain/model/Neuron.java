package de.kaping.brain.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Erzeugt Neuronen für das Netzwerk, die jeweils einen Knoten darstellen. Die
 * einzelnen Neuronen können dabei Input, Hidden, Bias oder Outputneuronen sein.
 * <p>
 * Inputneuronen bekommen ihren <code>value</code> von außen und stellen somit
 * eine Art Sensor der Umgebung dar. Hiddenneuronen stellen eine Kommunikations-
 * und Datenverarbeitungsebene dar. Das Biasneuron ist ein besonderes
 * Inputneuron, das den fixen <code>value</code> 1.0 besitzt. Dadurch können
 * Verschiebungen der Funktionen dargestellt werden. Die Outputneuronen sind die
 * Kommunikation an die Außenwelt. Mit diesen werden die Ergebnisse des
 * Neuronalen Netzes in "sichtbare" Handlungen umgesetzt. Über diese kann dann
 * auch gewichtet werden, wie erfolgreich das Netz die gestellte Aufgabe
 * bewältigt hat.
 * 
 * @author MPreloaded
 */
public class Neuron {

	//@SuppressWarnings("unused")
	private static final Logger log = LogManager.getLogger();

	private Type type;
	private List<Gene> incoming;
	private double value;
	private int innovation;
	private boolean calculated;

	/**
	 * Konstruktor
	 */
	public Neuron()
	{
		this(Type.UNDEFINED, 0., null, -1);
	}

	/**
	 * Konstruktor, der den Typ des Neurons bereits festlegt.
	 * 
	 * @param type Typ des Neurons
	 */
	public Neuron(Type type)
	{
		this(type, 0., null, -1);
	}

	/**
	 * Konstruktor, der sowohl Typ als auch Wert des Neurons festlegt.
	 * 
	 * @param type Typ des Neurons
	 * @param value Wert des Neurons
	 */
	public Neuron(Type type, double value)
	{
		this(type, value, null, -1);
	}

	/**
	 * Konstruktor, der Typ und Ursprung dieses Neurons festlegt.
	 * 
	 * @param type Type des Neurons
	 * @param innovation Ursprung des Neurons
	 */
	public Neuron(Type type, int innovation)
	{
		this(type, 0., null, innovation);
	}

	/**
	 * Konstruktor, der Typ, Wert und eingehende Verbindungen festlegt.
	 * 
	 * @param type Typ des Neurons
	 * @param value Wert des Neurons
	 * @param inc Eingehende Verbindungen des Neurons
	 */
	public Neuron(Type type, double value, List<Gene> inc)
	{
		this(type, value, inc, -1);
	}

	/**
	 * Konstruktor, der bei Entstehung von Hiddennodes verwendet werden sollte.
	 * 
	 * @param type Typ des Neurons
	 * @param value Wert des Neurons
	 * @param inc Eingehende Verbindungen des Neurons
	 * @param innovation Ursprung des Neurons
	 */
	public Neuron(Type type, double value, List<Gene> inc, int innovation)
	{
		super();

		this.type = type;
		this.value = value;
		this.setInnovation(innovation);
		this.calculated = false;

		if (inc != null)
			this.incoming = inc;
		else
			this.incoming = new ArrayList<Gene>();
	}

	/**
	 * Fügt eine einzelne neue Verbindung hinzu. Enthält bis jetzt keine
	 * Exception für ungültige Verbindungen (wie eine eingehende Verbindung in
	 * ein Inputneuron).
	 * 
	 * @param inc Neue eingehende Verbindung
	 * @return Wahrheitswert, ob alles funktioniert hat
	 */
	public boolean addIncoming(Gene inc)
	{
		if (!incoming.contains(inc))
			return incoming.add(inc);

		return false;
	}

	/**
	 * Löscht alle eingehenden Verbindungen für dieses Netzwerk
	 * 
	 * @return Wahrheitswert, ob alle Verbindungen gelöscht werden konnten
	 */
	public boolean resetIncoming()
	{
		incoming.clear();
		this.calculated = false;
		
		return incoming.isEmpty();
	}

	/**
	 * Ersetzt die Liste der eingehenden Verbindungen. VORSICHT! Alte
	 * Verbindungen werden nicht gespeichert.
	 * 
	 * @param incoming Neue Liste von eingehenden Verbindungen.
	 */
	public void setIncoming(List<Gene> incoming)
	{
		this.incoming = incoming;
	}

	/**
	 * Gibt die aktuelle Liste aller eingehenden Verbindungen zurück
	 * 
	 * @return Liste der eingehenden Verbindungen
	 */
	public List<Gene> getIncoming()
	{
		return this.incoming;
	}

	/**
	 * Setzt den Wert des Neurons. Keine Berechnung!
	 * 
	 * @param value neuer Wert des Neurons
	 */
	public void setValue(double value)
	{
		this.value = value;
		this.calculated = true;
	}

	/**
	 * Gibt den aktuellen Wert des Neurons zurück. Wichtig für jegliche
	 * Berechnungen oder Outputneuronen.
	 * 
	 * @return Wert des Neurons
	 */
	public double getValue()
	{
		return this.value;
	}

	/**
	 * Gibt den Typen eines Neurons zurück (Input, Hidden, Bias, Output)
	 * 
	 * @return Typ des Neurons
	 */
	public Type getType()
	{
		return this.type;
	}

	/**
	 * Setzt den Typ eines Neurons
	 * 
	 * @param type neuer Typ des Neurons
	 */
	public void setType(Type type)
	{
		this.type = type;
	}

	/**
	 * Gibt den der Innovation dieses Neurons zurück. <code>-1</code> heißt,
	 * dass das Neuron nicht durch die Aufteilung einer Verbindung entstanden
	 * ist.
	 * 
	 * @return Innovationsnummer der ursprünglichen Verbindung
	 */
	public int getInnovation()
	{
		return innovation;
	}

	/**
	 * Setzt die Innovationsnummer dieses Neurons um.
	 * 
	 * @param innovation neue Innovationsnummer
	 */
	public void setInnovation(int innovation)
	{
		this.innovation = innovation;
	}
	
	/**
	 * Gibt zurück, ob der Wert des Neurons innerhalb des letzten Resetzyklus 
	 * schon neu berechnet wurde.
	 * @return Wahrheitswert, ob Wert schon berechnet wurde
	 */
	public boolean isCalculated()
	{
		return this.calculated;
	}
	
	/**
	 * Berechnet alle eingehenden Neuronen und zuletzt dieses Neuron.
	 */
	public void calculateValue()
	{
		double sum = 0.;
		
		for (Gene g : incoming)
		{
			if (!g.getOrigin().isCalculated() &&  g.getEnabled())
			{
				log.debug("wait for Neuron " + g.getOrigin().getInnovation());
				g.getOrigin().calculateValue();
			}
			
			sum += g.getWeight() * g.getOrigin().getValue();
		}
		
		this.setValue(Neuron.sigmoid(sum));
	}
	
	/**
	 * Verrechnet die Summe aller eingehenden Werte mit ihren Gewichtungen auf 
	 * eine Sigmoid-Funktion.
	 * TODO: Diese Funktion könnte auch von außen einstellbar sein.
	 * @param value eingehender Wert
	 * @return mit sigmoid-Funktion verrechneter Wert
	 */
	public static double sigmoid(double value)
	{
		double sig = 1/(1 + Math.pow(5., -1 * value));
		
		return sig;
	}
	
	@Override 
	public String toString() 
	{
		String neuron = new String();
		
		neuron = "InnNummer: " + this.innovation + ", Type: " + this.type;
		return neuron;
	}
}
