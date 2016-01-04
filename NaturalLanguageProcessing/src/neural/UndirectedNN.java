package neural;

import helpers.DelayHelper;

import java.util.ArrayList;
import java.util.Hashtable;

import neural.general.GeneralNeuron;

public class UndirectedNN extends NeuralNetwork<double[], double[]> {

	// fields that are actually important to the network's functioning
	ArrayList<GeneralNeuron> neurons;
	private Hashtable<GeneralNeuron, Integer> neuronLookupTable;
	ArrayList<GeneralNeuron> outputNeurons;
	private int abstractLayers;

	public UndirectedNN(int numInputs, int numOutputs, int numNeurons, int abstractLayers) {
		super(numInputs, numOutputs);
		this.abstractLayers = abstractLayers;
		neurons = new ArrayList<GeneralNeuron>();
		neuronLookupTable = new Hashtable<GeneralNeuron, Integer>();
		for (int i = 0; i < numNeurons; i++) {
			neurons.add(new GeneralNeuron(numInputs + numNeurons - 1));
			neuronLookupTable.put(neurons.get(i), i);
		}
		outputNeurons = new ArrayList<GeneralNeuron>();
		for (int i = 0; i < numOutputs; i++) {
			GeneralNeuron outputNeuron = new GeneralNeuron(numNeurons);
			outputNeuron.inputNeurons = new ArrayList<GeneralNeuron>();
			outputNeurons.add(outputNeuron);
		}
		for (int i = 0; i < neurons.size(); i++) {
			for (int j = 0; j < neurons.size(); j++) {
				if (i != j) {
					neurons.get(i).inputNeurons.add(neurons.get(j));
				}
			}
			for (int j = 0; j < outputNeurons.size(); j++) {
				outputNeurons.get(j).inputNeurons.add(neurons.get(i));
			}
		}
	}

	@Override
	public void calculateOutput(double[] input) {
		double[] neuronSums = new double[neurons.size()];
		for (int i = 0; i < neurons.size(); i++) {
			neuronSums[i] = neurons.get(i).getOutputFrom(input);
		}
		for (int layer = 0; layer < abstractLayers; layer++) {
			for (int i = 0; i < neurons.size(); i++) {
				neurons.get(i).output = neuronSums[i];
			}
			for (int i = 0; i < neurons.size(); i++) {
				neuronSums[i] = neurons.get(i).getOutputWith(input);
			}
		}
		for (int i = 0; i < getNumOutputs(); i++) {
			outputNeurons.get(i).output = outputNeurons.get(i).getOutputFrom(neuronSums);
		}
	}

	public void adjustForAnswer(double[] input, double[] answers) {
		calculateOutput(input);
		for (int i = 0; i < outputNeurons.size(); i++) {
			outputNeurons.get(i).error = answers[i] - outputNeurons.get(i).output;
		}
		double[] neuronErrorSums = new double[neurons.size()];
		for (int i = 0; i < abstractLayers; i++) {
			for (int k = 0; k < getNumOutputs(); k++) {
				GeneralNeuron curOut = outputNeurons.get(k);
				double delta = curOut.output * (1 - curOut.output) * curOut.error;
				for (int j = 0; j < neurons.size(); j++) {
					neuronErrorSums[j] += curOut.inputWeights.get(j) * delta;
				}
			}
			for (int j = 0; j < neurons.size(); j++) {
				neurons.get(j).error = neuronErrorSums[j];
				neuronErrorSums[j] = 0;
			}
			for (int k = 0; k < neurons.size(); k++) {
				double delta = neurons.get(k).output * (1 - neurons.get(k).output) * neurons.get(k).error;
				for (int j = 0; j < getNumInputs(); j++) {
					neurons.get(k).inputWeights.set(j, neurons.get(k).inputWeights.get(j) + LEARNING_RATE * input[j] * delta);
				}
				neurons.get(k).biasWeight += LEARNING_RATE * delta;
			}
		}
		for (int i = 0; i < outputNeurons.size(); i++) {
			double delta = outputNeurons.get(i).output * (1 - outputNeurons.get(i).output) * outputNeurons.get(i).error;
			for (int j = 0; j < outputNeurons.get(i).getNumInputs(); j++) {
				outputNeurons.get(i).inputWeights.set(i, outputNeurons.get(i).inputWeights.get(i) + LEARNING_RATE * outputNeurons.get(i).inputNeurons.get(j).output * delta);
			}
			outputNeurons.get(i).biasWeight += LEARNING_RATE * delta;
		}
		if (getVerbosity() >= 4) {
			boolean anyWrong = false;
			for (int i = 0; i < answers.length; i++) {
				double gamma = outputNeurons.get(i).getTruncatedOutput(getGammaTruncation());
				if (answers[i] != gamma) {
					anyWrong = true;
					break;
				}
			}
			if (anyWrong) {
				for (int i = 0; i < answers.length; i++) {
					double gamma = outputNeurons.get(i).getTruncatedOutput(getGammaTruncation());
					if (answers[i] != gamma) {
						System.out.print("*****");
					}
					System.out.println(i + ":::" + answers[i] + "|" + gamma);
				}
			} else {
				System.out.println("Correct.");
			}
		}
	}

	public int getNeuronIndex(GeneralNeuron neuron) {
		return neuronLookupTable.get(neuron);
	}

	public int trainTillPerfection(double[][] problems, double[][] solutions) {
		return trainTillPerfection(problems, solutions, true);
	}

	public int trainTillPerfection(double[][] problems, double[][] solutions, boolean randomizeOrder) {
		return trainTillPerfection(problems, solutions, randomizeOrder, null);
	}

	public int trainTillPerfection(double[][] problems, double[][] solutions, boolean randomizeOrder, FFNNHelper helper) {
		boolean done = false;
		int iteration = 0;
		UndirectedNNDisplay visual = new UndirectedNNDisplay(this, getDisplayWidth(), getDisplayHeight(), getDisplayDelay());
		while (!done) {
			if (isDisplayingVisually()) {
				long st = System.currentTimeMillis();
				visual.repaint();
				DelayHelper.delayFor(getDisplayDelay() - (System.currentTimeMillis() - st));
			}
			done = true;
			if (getVerbosity() >= 2) {
				System.out.println("----------Iteration # " + iteration + "----------");
			}
			ArrayList<Integer> order = new ArrayList<Integer>();
			for (int i = 0; i < problems.length; i++) {
				order.add(i);
			}
			for (int k = 0; k < problems.length; k++) {
				int orderIndex = k;
				if (randomizeOrder) {
					orderIndex = (int) (NeuralUtils.random() * order.size());
				}
				int j = order.get(orderIndex);
				if (randomizeOrder) {
					order.remove(orderIndex);
				}
				if (getVerbosity() >= 3) {
					System.out.print("Problem " + j + " : ");
					for (int i = 0; i < problems[j].length; i++) {
						System.out.print((int) problems[j][i]);
					}
					System.out.println();
				}
				adjustForAnswer(problems[j], solutions[j]);
				for (int i = 0; i < getNumOutputs(); i++) {
					if (Math.abs(solutions[j][i] - outputNeurons.get(i).output) > getGammaTruncation()) {
						done = false;
					}
				}
			}
			if (helper != null) {
				helper.doPostIterationAction(iteration);
			}
			iteration++;

			if (done) {
				if (getVerbosity() >= 1) {
					System.out.println("Learned input in " + iteration + " iterations of " + problems.length + " problems (" + (iteration * problems.length) + " total iterations).");
					System.out.println("Learning rate was " + LEARNING_RATE);
					System.out.println("Truncation used was " + this.getGammaTruncation() + " (" + (1 - getGammaTruncation()) + "+ for 1, " + getGammaTruncation() + "- for 0)");
					if (randomizeOrder) {
						System.out.println("Problems were randomly ordered.");
					}
					if (helper != null) {
						helper.doCompletionAction();
					}
					visual.continueRepainting();
				}
				break;
			}
		}
		return iteration;
	}
}
