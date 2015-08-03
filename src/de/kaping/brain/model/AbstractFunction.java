package de.kaping.brain.model;

/**
 * Abstrakte Klasse, um die benötigten Funktionen für eine Fitnessbewertung
 * den Netzwerken mitzugeben.
 * 
 * Für die speziellen Testfälle sollte es dann idealer Weise nur noch nötig sein
 * spezielle Klassen zu erstellen, die von AbstractFunction erben.
 * 
 * @author MPreloaded
 */
public abstract class AbstractFunction {
	
	public abstract double evaluateNetwork(Genome genome, Object[] args);

}
