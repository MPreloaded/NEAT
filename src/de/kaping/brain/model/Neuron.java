package de.kaping.brain.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/** 
 * Erzeugt Neuronen für das Netzwerk, die jeweils einen Knoten darstellen.
 * Die einzelnen Neuronen können dabei Input, Hidden, Bias oder Outputneuronen
 * sein.
 * <p>
 * Inputneuronen bekommen ihren <code>value</code> von außen und stellen somit
 * eine Art Sensor der Umgebung dar.
 * Hiddenneuronen stellen eine Kommunikations- und Datenverarbeitungsebene dar.
 * Das Biasneuron ist ein besonderes Inputneuron, das den fixen 
 * <code>value</code> 1.0 besitzt. Dadurch können Verschiebungen der Funktionen
 * dargestellt werden.
 * Die Outputneuronen sind die Kommunikation an die Außenwelt. Mit diesen werden
 * die Ergebnisse des Neuronalen Netzes in "sichtbare" Handlungen umgesetzt. Über
 * diese kann dann auch gewichtet werden, wie erfolgreich das Netz die gestellte
 * Aufgabe bewältigt hat.
 * 
 * @author MPreloaded
 */
public class Neuron {
	
	private static final Logger log = LogManager.getLogger();
	
	private Type       type;
	private List<Gene> incoming;
	private double     value;

	/**
	 * Konstruktor
	 */
	public Neuron()
	{
		this(Type.UNDEFINED, 0., null);
	}
	
	/**
	 * Konstruktor, der den Typ des Neurons bereits festlegt
	 * @param type Typ des Neurons
	 */
	public Neuron(Type type) 
	{
		this(type, 0., null);
	}

	/**
	 * Konstruktor, der sowohl Typ als auch Wert des Neurons festlegt
	 * @param type Typ des Neurons
	 * @param value Wert des Neurons
	 */
	public Neuron(Type type, double value)
	{
		this(type, value, null);
	}
	
	/**
	 * Konstruktor, der Typ, Wert und eingehende Verbindungen festlegt
	 * @param type Typ des Neurons
	 * @param value Wert des Neurons
	 * @param inc Eingehende Verbindungen des Neurons
	 */
	public Neuron(Type type, double value, List<Gene> inc)
	{
		super();
		
		log.trace("   Create new Neuron.");
		
		this.type     = type;
		this.value    = value;
		
		if(inc != null)
			this.incoming = inc;
		else
			this.incoming = new ArrayList<Gene>();
	}
	
	/**
	 * Fügt eine einzelne neue Verbindung hinzu.
	 * Enthält bis jetzt keine Exception für ungültige Verbindungen 
	 * (wie eine eingehende Verbindung in ein Inputneuron).
	 * @param inc Neue eingehende Verbindung
	 * @return Wahrheitswert, ob alles funktioniert hat
	 */
	public boolean addIncoming(Gene inc)
	{                                             
		if(!incoming.contains(inc))
			return incoming.add(inc);
		
		return false;
		
	}
	
	/**
	 * Versucht eine bestehende Verbindung zu löschen, falls sie existiert.
	 * @param inc Die zu löschende Verbindung
	 * @return Wahrheitswert, ob die Verbindung gefunden und gelöscht wurde
	 */
	public boolean deleteIncoming(Gene inc)
	{
		return incoming.remove(inc);
	}
	
	/**
	 * Ersetzt die Liste der eingehenden Verbindungen.
	 * VORSICHT! Alte Verbindungen werden nicht gespeichert.
	 * @param incoming Neue Liste von eingehenden Verbindungen.
	 */
	public void setIncoming(List<Gene> incoming)
	{
		this.incoming = incoming;
	}
	
	/**
	 * Gibt die aktuelle Liste aller eingehenden Verbindungen zurück
	 * @return Liste der eingehenden Verbindungen
	 */
	public List<Gene> getIncoming()
	{
		return this.incoming;
	}
	
	/**
	 * Setzt den Wert des Neurons. Keine Berechnung!
	 * @param value neuer Wert des Neurons
	 */
	public void setValue(double value)
	{
		this.value = value;
	}
	
	/**
	 * Gibt den aktuellen Wert des Neurons zurück. Wichtig für jegliche 
	 * Berechnungen oder Outputneuronen.
	 * @return Wert des Neurons
	 */
	public double getValue()
	{
		return this.value;
	}
	
	/**
	 * Gibt den Typen eines Neurons zurück (Input, Hidden, Bias, Output)
	 * @return Typ des Neurons
	 */
	public Type getType()
	{
		return this.type;
	}
	
	/**
	 * Setzt den Typ eines Neurons
	 * @param type neuer Typ des Neurons
	 */
	public void setType(Type type)
	{
		this.type = type;		
	}
}
