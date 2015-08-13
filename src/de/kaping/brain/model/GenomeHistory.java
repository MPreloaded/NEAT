package de.kaping.brain.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Speichert den Verlauf der Performance der Genome über die Generationen hinweg
 * 
 * @author Keydara
 *
 */
public class GenomeHistory {

	private Map<Genome, List<Double>> fitMap = new HashMap<Genome, List<Double>>();
	private Pool myPool = Pool.getInstance();
	public static final GenomeHistory INSTANCE = new GenomeHistory();

	private GenomeHistory() {
	}

	/**
	 * Speichert die Performance-Werte für die aktuelle Generation des Genoms
	 * (aktuell nur die Fitness)
	 * 
	 * @param g
	 */
	public void addGeneration(Genome g) {
		List<Double> histList = fitMap.get(g);
		// Wenn für diese Generation noch kein Eintrag vorhanden ist, füge ihn
		// hinzu
		if (histList != null && histList.size() < myPool.getGeneration()) {
			histList.add(g.getFitness());
		} else {
			// Liste anlegen und Mappen, wenn noch nicht geschehen
			histList = new ArrayList<Double>();
			histList.add(g.getFitness());
			fitMap.put(g, histList);
		}
	}

	/**
	 * Liefert die vergangenen FitnessWerte für ein bestimmtes Genom zurück oder
	 * null falls es keine History für das Genom gibt
	 * 
	 * @param g
	 *           Das Genom für welches die Fitness History zurückgegeben werden
	 *           soll
	 * @return double[] mit allen aufgezeichneten Fitness Werten von g
	 */
	public double[] getFitnessHistory(Genome g) {
		List<Double> histList = fitMap.get(g);
		if (histList == null) {
			return null;
		}

		// Daten in Array kopieren
		double[] re = new double[myPool.getGeneration()];
		int c = myPool.getGeneration()-histList.size();
		for (double d : histList) {
			re[c] = d;
			c++;
		}
		// Array zurückgeben
		return re;
	}
	
	public int getSize(){
		return fitMap.size();
	}

}
