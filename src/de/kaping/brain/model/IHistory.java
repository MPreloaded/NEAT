package de.kaping.brain.model;

/**
 * Interface für die History Klassen, welche Daten über Genomes/Species
 * speichern sollen
 * 
 * @author jkeydara
 *
 */
public interface IHistory<T> {
	/**
	 * Liefert ein double[] zurück, welches sämtliche gespeicherten Werte für o
	 * enthält
	 * 
	 * @param o
	 * @return
	 */
	public double[] getHistory(T o);

	/**
	 * Speichert für das angegebene Objekt, Werte des Objekts
	 * 
	 * @param o
	 */
	public void addEntry(T o);

	/**
	 * Liefert die Anzahl der im History Objekt gespeicherten Objekte zurück
	 * 
	 * @return
	 */
	public int getSize();
}
