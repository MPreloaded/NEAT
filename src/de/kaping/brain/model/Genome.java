package de.kaping.brain.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Beschreibung eines Netzwerkes durch Zusammenfassung von Neuronen und Genes.
 * Ein Netzwerk wird durch seine Performance bewertet (<code>fitness</code>).
 * Zusätzlich werden die Mutierungsraten hier gespeichert und durch Aufrufe von
 * außen verändert.
 * <p>
 * Die <code>adjustedFitness</code> steht für eine veränderte, angepasste
 * Performance-Berwertung, die für die tatsächliche Auswahl überlebender
 * Netzwerke verwendet wird.
 * 
 * @author MPreloaded
 */
public class Genome implements Comparable<Genome> {

	// @SuppressWarnings("unused")
	private static final Logger log = LogManager.getLogger();
	private static int maxID;

	private ObservableList<Neuron> neurons;
	private ObservableList<Gene> genes;

	private DoubleProperty fitness;
	private DoubleProperty adjustedFitness;
	private IntegerProperty ID;

	/* Enthält alle Mutations-/ relevanten Raten für das Netzwerk */
	private DoubleProperty[] rates;

	/**
	 * Konstruktor
	 */
	public Genome() {
		this(FXCollections.observableArrayList(), false, null);
	}

	/**
	 * Konstruktor, welcher von matchGenome aufgerufen wird, damit die ID des
	 * alten Genomes in das neue Überführt wird (Wichtig für History)
	 * 
	 * @param ID
	 */
	public Genome(IntegerProperty ID) {
		this(FXCollections.observableArrayList(), false, ID);
	}

	/**
	 * Konstruktor mit Liste von Anfangsneuronen.
	 * 
	 * @param neurons
	 *           Anfangsneuronen
	 *        ID
	 *        	ID des Genomes (null, wenn dieses Genome nicht aus einem anderen hervorgeht)
	 */
	public Genome(ObservableList<Neuron> neurons, IntegerProperty ID) {
		this(neurons, false, ID);
	}

	/**
	 * Konstruktor mit Liste von Anfangsneuronen, der bei basic = true eine
	 * Mutation durchführt.
	 * 
	 * @param neurons
	 * @param basic
	 */
	public Genome(ObservableList<Neuron> neurons, boolean basic, IntegerProperty ID) {
		super();
		if(ID == null){
		this.ID = new SimpleIntegerProperty(maxID + 1);
		maxID++;
		}else{
			this.ID = ID;
		}

		/* TODO: Entfernen des Hardcoden */
		this.rates = new SimpleDoubleProperty[6];
		this.rates[0] = new SimpleDoubleProperty(
			.25); /* Ändern aller Gewichtungen */
		this.rates[1] = new SimpleDoubleProperty(
			2.0); /* Hinzufügen von Verbindungen */
		this.rates[2] = new SimpleDoubleProperty(
			0.5); /* Trennen einer Verbindung durch Einfügen Neuron */
		this.rates[3] = new SimpleDoubleProperty(
			0.4); /* Hinzufügen von BIAS-Verbindung */
		this.rates[4] = new SimpleDoubleProperty(
			0.4); /* Deaktivieren einer aktiven Verbindung */
		this.rates[5] = new SimpleDoubleProperty(
			0.2); /* Aktivieren einer inaktiven Verbindung */

		this.neurons = FXCollections.observableArrayList();
		this.neurons = neurons;
		this.genes = FXCollections.observableArrayList();
		this.fitness = new SimpleDoubleProperty(0.0);
		this.adjustedFitness = new SimpleDoubleProperty(0.0);

		if (basic)
			this.mutateGenome();
	}

	/**
	 * Gibt die eindeutige ID des Genoms zurück
	 * 
	 * @return
	 */
	public int getID() {
		return this.ID.get();
	}

	public IntegerProperty getIDProperty() {
		return this.ID;
	}

	/**
	 * Setzt die aktuelle Bewertung des Netzwerkes neu.
	 * 
	 * @param fitness
	 *           neue Bewertung
	 */
	public void setFitness(double fitness) {
		this.fitness.set(fitness);
	}

	/**
	 * Gibt die aktuelle Bewertung des Netzwerkes zurück.
	 * 
	 * @return aktuelle Bewertung
	 */
	public double getFitness() {
		return this.fitness.get();
	}

	/**
	 * Setzt die tatsächliche Bewertung des Netzwerkes neu.
	 * 
	 * @param fitness
	 *           neue Bewertung
	 */
	public void setAdjustedFitness(double fitness) {
		this.adjustedFitness.set(fitness);
	}

	/**
	 * Gibt die tatsächliche Bewertung des Netzwerkes zurück.
	 * 
	 * @return tatsächliche Bewertung
	 */
	public double getAdjustedFitness() {
		return this.adjustedFitness.get();
	}

	/**
	 * Setzt eine Liste von Neuronen des Netzwerkes. Praktisch, falls ein
	 * Netzwerk kopiert werden soll.
	 * 
	 * @param neurons
	 *           Liste von Neuronen
	 */
	public void setNeurons(ObservableList<Neuron> neurons) {
		this.neurons = neurons;
	}

	/**
	 * Gibt eine Liste aller Neuronen des Netzwerkes zurück.
	 * 
	 * @return Liste aller Neuronen
	 */
	public ObservableList<Neuron> getNeurons() {
		return this.neurons;
	}

	/**
	 * Setzt eine Liste von Verbindungen des Netzwerkes.
	 * 
	 * @param genes
	 *           Liste von Verbindungen
	 */
	public void setGenes(ObservableList<Gene> genes) {
		this.genes = genes;
	}

	/**
	 * Gibt eine Liste aller Verbindungen des Netzwerkes zurück.
	 * 
	 * @return Liste aller Verbindungen
	 */
	public ObservableList<Gene> getGenes() {
		return this.genes;
	}

	/**
	 * Setzt die Mutationsraten für dieses Netzwerk neu.
	 * 
	 * @param rates
	 *           neue Mutationsraten
	 */
	public void setRates(DoubleProperty[] rates) {
		if (rates.length == 6)
			this.rates = rates;
	}

	/**
	 * Gibt die aktuellen Mutationsraten zurück.
	 * 
	 * @return Mutationsraten
	 */
	public DoubleProperty[] getRates() {
		return rates;
	}

	/**
	 * Fügt eine einzelne Verbindung zum Netzwerk hinzu, wenn dadurch kein
	 * Zyklus entsteht.
	 * 
	 * @param gene
	 *           neue Verbindung
	 * @return Wahrheitswert, ob Verbindung hinzugefügt werden konnte
	 */
	public boolean addGene(Gene gene) {
		if (!genes.contains(gene))
			if (neurons.contains(gene.getInto())
				&& neurons.contains(gene.getOrigin()))
			if (!this.checkForLoop(gene))
				return genes.add(gene);

		return false;
	}

	/**
	 * Fügt ein neues Neuron zum Netzwerk hinzu.
	 * 
	 * @param neuron
	 *           neues Neuron
	 * @return Wahrheitswert, ob Neuron hinzugefügt werden konnte
	 */
	public boolean addNeuron(Neuron neuron) {
		if (!neurons.contains(neuron))
			return neurons.add(neuron);

		return false;
	}

	/**
	 * Kopiert dieses Netzwerk.
	 * 
	 * @return kopiertes Objekt
	 */
	public Genome copyGenome() {
		Genome copy = new Genome();

		ObservableList<Neuron> neurons = FXCollections.observableArrayList();
		ObservableList<Gene> genes = FXCollections.observableArrayList();
		DoubleProperty[] rates = new SimpleDoubleProperty[6];

		for (Neuron n : this.neurons)
			neurons.add(n);

		for (Gene g : this.genes)
			genes.add(g.copyGene());

		for (int i = 0; i < 6; i++)
			rates[i] = new SimpleDoubleProperty(this.rates[i].doubleValue());
		;

		copy.setNeurons(neurons);
		copy.setGenes(genes);
		copy.setRates(rates);

		return copy;
	}

	/**
	 * Kombiniert dieses Netzwerk mit einem zweiten, um ein neues Netzwerk zu
	 * erzeugen. Die ID wird dabei übernommen
	 * 
	 * @param gen2
	 *           zweites Netzwerk zur Kombination
	 * @return neu generiertes Netzwerk
	 */
	public Genome matchGenomes(Genome gen2) {
		Genome child = new Genome(this.ID);
		/* Bestimmung besseres und schlechteres Netzwerk */
		Genome h = (this.fitness.get() > gen2.getFitness()) ? this : gen2;
		Genome l = (this.fitness.get() > gen2.getFitness()) ? gen2 : this;

		int size1 = h.genes.size();
		int size2 = l.getGenes().size();

		if (size1 == 0 && size2 == 0) {
			child = h.copyGenome();
			return child;
		}

		h.getGenes().sort(null);
		l.getGenes().sort(null);

		/* Berechne die maximale Innovationsnummer */
		int maxH = (size1 > 0)
			? h.getGenes().get(size1 - 1).getHistoricalMarking() : 0;
		int maxL = (size2 > 0)
			? l.getGenes().get(size2 - 1).getHistoricalMarking() : 0;
		int maxInn = Math.max(maxH, maxL) + 1;

		Gene[] inn = new Gene[maxInn];

		for (Gene g : l.getGenes())
			inn[g.getHistoricalMarking()] = g;

		/*
		 * Füge Verbindungen aus beiden Netzwerken dem neuen hinzu. Disjoint-
		 * und Excess-Verbindungen nur vom besseren
		 */
		for (Gene g : h.getGenes())
			if (inn[g.getHistoricalMarking()] != null && Math.random() < 0.5
				&& inn[g.getHistoricalMarking()].getEnabled()) {
			child.addGene(inn[g.getHistoricalMarking()].copyGene());
			} else
			child.addGene(g.copyGene());

		/* Fügt alle notwendigen Neuronen hinzu */
		for (Neuron n : Pool.getInstance().getEssentialNeurons())
			child.addNeuron(n);

		/* Fügt alle zusätzlichen Neuronen hinzu */
		for (Gene g : child.getGenes()) {
			child.addNeuron(g.getOrigin());
			child.addNeuron(g.getInto());
		}

		/* neues Netzwerk bekommt Mutationsraten vom besseren Netzwerk */
		DoubleProperty[] rates = new SimpleDoubleProperty[6];
		DoubleProperty[] hrates = h.getRates();
		for (int i = 0; i < 6; i++)
			rates[i] = new SimpleDoubleProperty(hrates[i].doubleValue());

		return child;
	}

	/**
	 * Führt einen Mutationszyklus aus. Das bedeutet:
	 * <ul>
	 * <li>Verändern der Mutationsraten (Zufällig erhöhen oder verringern)</li>
	 * <li>Durch Zufall auswählen, ob die jeweiligen Mutationen ausgeführt
	 * werden</li>
	 * <li>Linkmutation (Hinzufügen neuer Verbindungen)</li>
	 * <li>Neuronmutation (Trennen einer Verbindung in zwei)</li>
	 * <li>Biasmutation (Hinzufügen neuer Verbindung ausgehend vom Biasneuron)
	 * </li>
	 * <li>Disablemutation (Deaktivieren einer aktiven Verbindung)</li>
	 * <li>Enablemutation (Aktivieren einer inaktiven Verbindung)</li>
	 * </ul>
	 */
	public void mutateGenome() {
		/* erst Raten modifizieren... */
		this.alterRates();

		/* ... dann gegebenenfalls Mutierungen durchführen */
		if (Math.random() < rates[0].get())
			this.mutateConnections();

		for (int i = 1; i < 6; i++) {
			double rate = rates[i].get();

			while (rate > 0.) {
			if (Math.random() < rate) {
				switch (i) {
				case 1:
					mutateLink(false);
					break;
				case 2:
					if (this.genes.size() > 0)
						mutateNode();
					break;
				case 3:
					mutateLink(true);
					break;
				case 4:
					mutateEnable(true);
					break;
				case 5:
					mutateEnable(false);
					break;
				default:
					/* Das kann nicht passieren!!! */
				}
			}

			rate -= 1.;
			}
		}
	}

	/**
	 * Berechnet für alle Neuronen die Werte innerhalb dieses Netzwerkes.
	 * Verwendet hierbei die aktuell eingegebenen Werte innerhalb der Input
	 * Neuronen.
	 */
	public void simulateGenome() {
		log.debug("Genome: " + this.ID);
		/* Zurücksetzen und erstellen der IncomingList aller Neuronen */
		for (Neuron n : neurons)
			n.resetIncoming();

		for (Gene g : genes)
			if (g.getEnabled()) {
			Neuron into = g.getInto();
			into.addIncoming(g);
			}

		/* neue Werte für Neuronen berechnen */
		for (Neuron n : neurons)
			if (!n.isCalculated())
			n.calculateValue();

	}

	/**
	 * Berechnet einen Wert, in wieweit sich zwei Netzwerke in ihrer Struktur
	 * unterscheiden.
	 * 
	 * @param gen2
	 *           zweites Netzwerk
	 * @return Strukturunterscheidungsgrad
	 */
	public double deltaStructure(Genome gen2) {
		int structuralDifferences = 0;
		int size1 = this.genes.size();
		int size2 = gen2.getGenes().size();
		double n = Math.max(size1, size2);

		/* Berechne die maximale Innovationsnummer */
		int maxInn = Pool.getInstance().getHistoricalMarking();

		boolean[] inn1 = new boolean[maxInn + 1];
		boolean[] inn2 = new boolean[maxInn + 1];

		for (Gene g : this.genes)
			inn1[g.getHistoricalMarking()] = true;
		for (Gene g : gen2.getGenes())
			inn2[g.getHistoricalMarking()] = true;

		for (int i = 0; i < maxInn; i++)
			if (inn1[i] != inn2[i])
			structuralDifferences++;

		if (n < 0.5)
			n = 1.0;
		return structuralDifferences / n;
	}

	/**
	 * Berechnet einen Wert, in wieweit sich bei übereinstimmenden Verbindungen
	 * zwischen zwei Netzwerken die Gewichtungen im Durchschnitt unterscheiden.
	 * 
	 * @param gen2
	 *           zweites Netzwerk
	 * @return durchschnittlicher Gewichtungsunterschied
	 */
	public double deltaWeight(Genome gen2) {
		double deltaWeight = 0.;
		int n = 0;

		/* Berechne die maximale Innovationsnummer */
		int maxInn = Pool.getInstance().getHistoricalMarking();

		Gene[] inn = new Gene[maxInn + 1];

		for (Gene g : gen2.getGenes())
			inn[g.getHistoricalMarking()] = g;

		for (Gene g : this.genes)
			if (inn[g.getHistoricalMarking()] != null) {
			Gene g2 = inn[g.getHistoricalMarking()];
			deltaWeight += Math.abs(g.getWeight() - g2.getWeight());
			n++;
			}

		if (n == 0)
			n = 1;
		return deltaWeight / n;
	}

	/* Änderung aller Gewichtungen */
	private void mutateConnections() {
		/*
		 * TODO: Vielleicht Hard Coding entfernen und einen Parameter einführen
		 */
		double step = 0.1;
		double lowChange = 0.9;

		for (int i = 0; i < genes.size(); i++) {
			Gene gene = genes.get(i);
			if (Math.random() < lowChange)
			gene.setWeight(gene.getWeight() + Math.random() * 2 * step - step);
			else
			gene.setWeight(Math.random() * 4 - 2);
		}
	}

	/* Hinzufügen einer neuen Verbindung */
	private void mutateLink(boolean bias) {
		Random rn = new Random();
		Neuron neuron1 = this.neurons.get(rn.nextInt(this.neurons.size()));
		Neuron neuron2 = this.neurons.get(rn.nextInt(this.neurons.size()));

		/* Keine Verbindung auf das gleiche Neuron */
		if (neuron1 == neuron2)
			return;

		Type t1 = neuron1.getType();
		Type t2 = neuron2.getType();

		Neuron origin, into;

		/*
		 * wenn beide zufälligen Neuronen Input oder Output sind, dann Abbruch
		 */
		if ((t1 == Type.INPUT || t1 == Type.BIAS)
			&& (t2 == Type.INPUT || t2 == Type.BIAS)
			|| (t1 == Type.OUTPUT && t2 == Type.OUTPUT))
			return;

		/*
		 * sollte ein Neuron Input sein, so soll es der Origin werden, ein
		 * Outputneuron sollte das Ziel werden
		 */
		if (neuron2.getType() == Type.INPUT || neuron1.getType() == Type.OUTPUT
			|| neuron2.getType() == Type.BIAS) {
			origin = neuron2;
			into = neuron1;
		} else {
			origin = neuron1;
			into = neuron2;
		}

		if (bias)
			origin = this.getBias();

		Gene newGene = new Gene(origin, into);

		/* Besteht diese Verbindung bereits, dann Abbruch */
		for (int i = 0; i < genes.size(); i++)
			if (newGene.isEqual(genes.get(i)))
			return;

		/* Alle Tests erfolgreich: neue verbindung einrichten */
		newGene.setWeight(rn.nextDouble() * 4 - 2);

		Pool.getInstance().defineHistoricalMarking(newGene);

		this.addGene(newGene);
	}

	/* Trennen einer Verbindung durch Einfügen eines Neurons */
	private void mutateNode() {
		Random rn = new Random();
		Gene gene = genes.get(rn.nextInt(genes.size()));
		Pool pool = Pool.getInstance();
		boolean neuronExists = false;

		/* bei inaktiver Verbindung wird die Mutierung abgebrochen */
		if (gene.getEnabled() == false) {
			return;
		}

		gene.setEnabled(false);

		Neuron newNeuron = null;
		for (Neuron n : pool.getNewNeurons())
			if (n.getInnovation() == gene.getHistoricalMarking()) {
			newNeuron = n;
			neuronExists = true;
			break;
			}

		if (!neuronExists) {
			newNeuron = new Neuron(Type.HIDDEN, gene.getHistoricalMarking());
			pool.addNewNeuron(newNeuron);
		}

		Gene newGene1 = new Gene(gene.getOrigin(), newNeuron, 1.0);
		Gene newGene2 = new Gene(newNeuron, gene.getInto(), gene.getWeight());

		pool.defineHistoricalMarking(newGene1);
		pool.defineHistoricalMarking(newGene2);

		this.addNeuron(newNeuron);
		this.addGene(newGene1);
		this.addGene(newGene2);
	}

	/* (De-)Aktivieren einer (in-)aktiven Verbindung */
	private void mutateEnable(boolean enable) {
		Random rn = new Random();
		List<Gene> candidates = new ArrayList<Gene>();

		for (int i = 0; i < genes.size(); i++)
			if (genes.get(i).getEnabled() == enable)
			candidates.add(genes.get(i));

		/* Wenn es keine (in-)aktive Verbindung gibt, Abbruch */
		if (candidates.size() == 0)
			return;

		Gene gene = candidates.get(rn.nextInt(candidates.size()));
		gene.setEnabled(!gene.getEnabled());
	}

	/* Ändern der Raten zur Entwicklung verschiedenster Netzwerke */
	private void alterRates() {
		/*
		 * 0.95 * 1.05263 ~= 1 TODO: Vielleicht Hardcoding entfernen und einen
		 * Parameter einführen
		 */
		for (int i = 0; i < 6; i++)
			if (Math.random() < 0.5)
			rates[i].set(rates[i].get() * 0.95);
			else
			rates[i].set(rates[i].get() * 1.05263);
	}

	/* Suchen des Biasneurons innerhalb des Netzwerkes */
	private Neuron getBias() {
		Neuron bias = null;

		/*
		 * Es sollte nur ein Biasneuron vorhanden sein. Sollten mehrere
		 * existieren, so wird das erste ausgewählt.
		 */
		for (int i = 0; i < neurons.size(); i++)
			if (neurons.get(i).getType() == Type.BIAS) {
			bias = neurons.get(i);
			break;
			}

		/* Eigentlich nicht nötig, zur Sicherheit */
		if (bias == null) {
			bias = new Neuron(Type.BIAS);
			this.addNeuron(bias);
		}

		return bias;
	}

	/*
	 * Kontrolliert, ob durch das Hinzufügen dieser Verbindung ein Zyklus
	 * entsteht
	 */
	private boolean checkForLoop(Gene g) {
		/*
		 * Wenn nicht zwei Hiddenneuronen verbunden werden, so ist kein Zyklus
		 * möglich
		 */
		log.trace("ENTER checkForLoop() - " + this.getID());
		if (g.getOrigin().getType() != Type.HIDDEN
			|| g.getInto().getType() != Type.HIDDEN) {
			log.trace("EXIT checkForLoop() - Not both Neurons are HIDDEN");
			return false;
		}

		List<Gene> possibleLoopGenes = new ArrayList<Gene>();
		List<Neuron> loopNeurons = new ArrayList<Neuron>();

		/* Alle möglichen Zyklusverbindungen in einer Liste zusammenfassen */
		for (Gene gene : genes)
			if (gene.getOrigin().getType() == Type.HIDDEN
				&& gene.getInto().getType() == Type.HIDDEN)
			possibleLoopGenes.add(gene);
		possibleLoopGenes.add(g);

		/* Erstellen einer Liste aller verbundenen Hiddenneurons */
		for (Gene gene : possibleLoopGenes) {
			if (!loopNeurons.contains(gene.getOrigin())) {
			loopNeurons.add(gene.getOrigin());
			gene.getOrigin().resetIncoming();
			}
			if (!loopNeurons.contains(gene.getInto())) {
			loopNeurons.add(gene.getInto());
			gene.getInto().resetIncoming();
			}
			gene.getInto().addIncoming(gene);
		}

		/* Zykluserkennung mittels Tiefensuche */
		for (Neuron neuron : loopNeurons) {
			boolean[] visited = new boolean[loopNeurons.size()];
			boolean[] finished = new boolean[loopNeurons.size()];

			boolean cycl = checkLoopNeuron(neuron, loopNeurons, visited,
				finished);

			/* Zyklus erkannt */
			if (cycl) {
			log.trace("EXIT checkForLoop() - Zyklus erkannt");
			return true;
			}
		}
		log.trace("EXIT checkForLoop() - End");
		return false;
	}

	/*
	 * Benötigte Untermethode für checkForLoop Wird benutzt, um ein Neuron zu
	 * kontrollieren
	 */
	private boolean checkLoopNeuron(Neuron n, List<Neuron> list,
			boolean[] visited, boolean[] finished) {
		if (finished[list.indexOf(n)])
			return false;

		/*
		 * Wenn schonmal besucht aber nicht beendet, dann kam ein Anruf von
		 * diesem Neuron: Zyklus!!
		 */
		if (visited[list.indexOf(n)])
			return true;

		visited[list.indexOf(n)] = true;
		// log.debug("# of incoming Connections: "+n.getIncoming().size());
		for (Gene gene : n.getIncoming()) {
			if (checkLoopNeuron(gene.getOrigin(), list, visited, finished))
			return true;
		}

		finished[list.indexOf(n)] = true;

		return false;
	}

	@Override
	/**
	 * Vergleicht die Bewertungen der beiden Netzwerke. Gibt -1 zurück, wenn die
	 * Bewertung dieses Netzwerkes besser ist als die von <code>o</code>, 1 wenn
	 * schlechter und 0 wenn identisch. Somit wird sicher gestellt, dass die
	 * höchste Fitness an erster Stelle steht.
	 */
	public int compareTo(Genome o) {
		/* TODO: fitness oder adjustedFitness ? */
		double deltaFitness = this.fitness.get() - o.getFitness();

		if (deltaFitness > 0)
			return -1;
		else if (deltaFitness < 0)
			return 1;
		else
			return 0;
	}

	@Override
	public String toString() {
		String genome = new String();
		String nline = "\n";

		genes.sort(null);

		genome = nline + "Netzwerk " + this.ID.intValue() + ", Fitness: "
			+ String.format("%.3f", this.fitness.doubleValue());

		for (Neuron n : neurons)
			genome = genome + nline + n;

		for (Gene g : genes)
			genome = genome + nline + g;

		genome = genome + nline;
		return genome;
	}
}
