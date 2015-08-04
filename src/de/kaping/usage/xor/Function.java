package de.kaping.usage.xor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.kaping.brain.model.AbstractFunction;
import de.kaping.brain.model.Genome;
import de.kaping.brain.model.Neuron;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Function extends AbstractFunction {

	private static Logger log = LogManager.getLogger();
	private static Function instance;

	/**
	 * args Stelle 0 sollte den Pointer auf neurons beinhalten.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public double evaluateNetwork(Genome genome, Object... args) {
		double fit = 0.;
		ObservableList<Neuron> neurons = FXCollections.observableArrayList();

		try {
			neurons = (ObservableList<Neuron>) args[0];
		} catch (Exception e) {
			log.error("Wrong Paramter in evluateNetwork: " + e);
		}

		fit += genomeTest(genome, neurons, 0, 0, 0);
		fit += genomeTest(genome, neurons, 0, 1, 1);
		fit += genomeTest(genome, neurons, 1, 0, 1);
		fit += genomeTest(genome, neurons, 1, 1, 0);

		return fit;
	}

	private double genomeTest(Genome g, ObservableList<Neuron> neurons, 
								double in1, double in2, double out1) {
		double result = 0.;
		double outG   = 0.;

		neurons.get(0).setValue(in1);
		neurons.get(1).setValue(in2);
		
		g.simulateGenome();
		
		outG = neurons.get(3).getValue();
		
		result = 1 / Math.abs(outG - out1);

		return result;
	}
	
	private Function()
	{
		super();
	}
	
	public static Function getInstance()
	{
		if (instance == null)
			instance = new Function();
		
		return instance;
	}

}
