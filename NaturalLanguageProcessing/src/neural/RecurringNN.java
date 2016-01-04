package neural;

import helpers.DelayHelper;

import java.util.ArrayList;

public class RecurringNN extends NeuralNetwork<double[][], double[]> {

	// fields that are actually important to the network's functioning
	Neuron[] hiddenLayer;
	public Neuron[] outputNeurons;
	private int numAbstractNeurons;
	private double[][] refinedInputs;

	public RecurringNN(int numInputs, int numOutputs, int numAbstractNeurons) {
		super(numInputs, numOutputs);
		this.numAbstractNeurons = numAbstractNeurons;
		hiddenLayer = new Neuron[numAbstractNeurons];
		for (int i = 0; i < hiddenLayer.length; i++) {
			hiddenLayer[i] = new Neuron(getLayerSize());
		}
		outputNeurons = new Neuron[getNumOutputs()];
		for (int i = 0; i < outputNeurons.length; i++) {
			outputNeurons[i] = new Neuron(getLayerSize());
		}
	}

	@Override
	public void calculateOutput(double[][] input) {
		refinedInputs = new double[input.length][getLayerSize()];
		for (int i = 0; i < input.length; i++) {
			System.arraycopy(input[i], 0, refinedInputs[i], 0, getNumInputs());
		}
		for (int i = 0; i < input.length - 1; i++) {
			for (int j = 0; j < numAbstractNeurons; j++) {
				hiddenLayer[j].gamma = refinedInputs[i + 1][j];
				double output = hiddenLayer[j].calculateOutput(refinedInputs[i]);
				if (i < input.length - 1) {
					refinedInputs[i + 1][getNumInputs() + j] = output;
				}
			}
		}
		for (int i = 0; i < getNumOutputs(); i++) {
			outputNeurons[i].calculateOutput(refinedInputs[input.length - 1]);
		}
	}

	public int getLayerSize() {
		return getNumInputs() + numAbstractNeurons;
	}

	@Override
	public void adjustForAnswer(double[][] input, double[] answers) {
		if (answers.length < getNumOutputs()) {
			System.err.println("Desired output length is shorter than real output length.");
		}
		calculateOutput(input);
		for (int i = 0; i < getNumOutputs(); i++) {
			outputNeurons[i].error = answers[i] - outputNeurons[i].gamma;
		}
		// backtrack from the output layer to the first of the input layers
		for (int i = 0; i < getNumOutputs(); i++) {
			outputNeurons[i].distributeError(hiddenLayer);
			outputNeurons[i].adjustWeights(refinedInputs[input.length - 1]);
		}
		double[] errors = new double[numAbstractNeurons];
		for (int i = 0; i < input.length - 1; i++) {
			for (int j = 0; j < numAbstractNeurons; j++) {
				for (int k = 0; k < numAbstractNeurons; k++) {
					errors[k] += NeuralNetwork.LEARNING_RATE * hiddenLayer[j].delta() * hiddenLayer[j].weights[k];
				}
				hiddenLayer[j].adjustWeights(refinedInputs[input.length - 2 - i]);
			}
		}
		if (getVerbosity() >= 4) {
			boolean anyWrong = false;
			for (int i = 0; i < answers.length; i++) {
				double gamma = outputNeurons[i].getAdjustedGamma(getGammaTruncation());
				if (answers[i] != gamma) {
					anyWrong = true;
					break;
				}
			}
			if (anyWrong) {
				for (int i = 0; i < answers.length; i++) {
					double gamma = outputNeurons[i].getAdjustedGamma(getGammaTruncation());
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

	@Override
	public int trainTillPerfection(double[][][] problems, double[][] solutions, boolean randomizeOrder) {
		boolean done = false;
		int iteration = 0;
		// XXX
		// FFNNDisplay visual = null;
		if (isDisplayingVisually()) {
			// visual = new FFNNDisplay(this, getDisplayWidth(),
			// getDisplayHeight(), getDisplayDelay());
		}
		while (!done) {
			if (isDisplayingVisually()) {
				long st = System.currentTimeMillis();
				// XXX
				// visual.repaint();
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
					System.out.println("Problem " + j + " : ");
					for (int q = 0; q < problems[j].length; q++) {
						for (int i = 0; i < problems[j][q].length; i++) {
							System.out.print((int) problems[j][q][i]);
						}
						System.out.println();
					}
				}
				adjustForAnswer(problems[j], solutions[j]);
				for (int i = 0; i < getNumOutputs(); i++) {
					if (Math.abs(solutions[j][i] - outputNeurons[i].gamma) > getGammaTruncation()) {
						done = false;
					}
				}
			}
			iteration++;
			// XXX
			// visual.setVisible(true);

			if (done) {
				if (getVerbosity() >= 1) {
					System.out.println("Learned input in " + iteration + " iterations of " + problems.length + " problems (" + (iteration * problems.length) + " total iterations).");
					System.out.println("Learning rate was " + LEARNING_RATE);
					System.out.println("Truncation used was " + this.getGammaTruncation() + " (" + (1 - getGammaTruncation()) + "+ for 1, " + getGammaTruncation() + "- for 0)");
					if (randomizeOrder) {
						System.out.println("Problems were randomly ordered.");
					}
				}
				// XXX
				// if (isDisplayingVisually()) {
				// visual.continueRepainting();
				// }
				break;
			}
		}
		return iteration;
	}
}
