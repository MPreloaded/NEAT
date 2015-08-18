package de.kaping.brain.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Speichert bestimmte Werte der Species über die Generationen, damit man den
 * Verlauf der Performance sehen kann
 * 
 * @author jkeyd
 *
 */
public class SpeciesHistory implements IHistory<Species> {

	public static final SpeciesHistory INSTANCE = new SpeciesHistory();
	private Map<Species, List<Double>> hMap = new HashMap<Species, List<Double>>();

	private SpeciesHistory() {
	};

	/**
	 * Gibt für jede vergangene Generation die aufgezeichneten Werte zuürpck,
	 * falls vorhanden, 0 sonst Es dürfen nicht mehr Werte gespeichert sein, als
	 * es Generationen gibt!
	 */
	@Override
	public double[] getHistory(Species o) {
		if (o == null)
			throw new IllegalArgumentException("Species Parameter is NULL!");

		double[] re = new double[Pool.getInstance().getGeneration()];
		List<Double> l = hMap.get(o);
		if(l==null){
			return null;
		}
		int c=Pool.getInstance().getGeneration()-l.size();
		for (double d : l) {
			re[c] = d;
			c++;
		}
		return re;
	}

	/**
	 * Speichert die AverageFitness der Species für die aktuelle Generation
	 * Diese Methode sollte nur einmal pro Spezies und Generation aufgerufen
	 * werden!
	 */
	@Override
	public void addEntry(Species o) {
		if (o == null)
			throw new IllegalArgumentException("Species Parameter is NULL!");
		List<Double> l = hMap.get(o);

		// Liste noch nicht vorhanden --> Anlegen
		if (l == null) {
			l = new ArrayList<Double>();
			hMap.put(o, l);
		}
		l.add(o.getAverageFitness());
	}

	@Override
	public int getSize() {
		return hMap.size();
	}

}
