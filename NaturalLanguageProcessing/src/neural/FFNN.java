package neural;

import helpers.DelayHelper;
import helpers.StreamHelper;

import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;

import javax.swing.JFrame;

/**
 * A feed-forward neural network.
 * 
 * @author Dwight Naylor
 *
 */
public class FFNN extends NeuralNetwork<double[], double[]> {

	// fields that are actually important to the network's functioning
	Neuron[][] neurons;

	/**
	 * @param sizes
	 *            The sizes of each layer. The first element is the number of
	 *            input neurons, the last element is the number of output
	 *            neurons, each number in between is the size of a hidden layer.
	 *            {1,2,3,1} would make a network with 1 input, and two hidden
	 *            layers, the first of size 2 and the next of size 3, and one
	 *            output neuron.
	 */
	public FFNN(int[] sizes) {
		super(sizes[0], sizes[sizes.length - 1]);
		neurons = new Neuron[sizes.length - 1][];
		for (int i = 0; i < sizes.length - 1; i++) {
			neurons[i] = new Neuron[sizes[i + 1]];
			for (int j = 0; j < sizes[i + 1]; j++) {
				neurons[i][j] = new Neuron(sizes[i]);
			}
		}
	}

	@Override
	public int hashCode() {
		return getCompactedForm().hashCode();
	}

	public String getCompactedForm() {
		StringBuffer ret = new StringBuffer("");
		ret.append(NeuralUtils.compact(neurons.length + 1));
		ret.append(NeuralUtils.compact(getNumInputs()));
		for (int i = 0; i < neurons.length; i++) {
			ret.append(NeuralUtils.compact(neurons[i].length));
		}
		for (int i = 0; i < neurons.length; i++) {
			for (int j = 0; j < neurons[i].length; j++) {
				ret.append(NeuralUtils.compact(neurons[i][j].biasWeight));
				for (int k = 0; k < neurons[i][j].weights.length; k++) {
					ret.append(NeuralUtils.compact(neurons[i][j].weights[k]));
				}
			}
		}
		return ret.toString();
	}

	public static FFNN parse(String s) {
		return parse(new ByteArrayInputStream(s.getBytes()));
	}

	public static FFNN parse(InputStream s) {
		int numLayers = NeuralUtils.parseInt(StreamHelper.getString(s, 4));
		int[] layerSizes = new int[numLayers];
		for (int i = 0; i < numLayers; i++) {
			layerSizes[i] = NeuralUtils.parseInt(StreamHelper.getString(s, 4));
		}
		FFNN ret = new FFNN(layerSizes);
		for (int i = 1; i < numLayers; i++) {
			for (int j = 0; j < layerSizes[i]; j++) {
				Neuron currentNeuron = ret.neurons[i - 1][j];
				currentNeuron.biasWeight = NeuralUtils.parseDouble(StreamHelper.getString(s, 8));
				for (int k = 0; k < layerSizes[i - 1]; k++) {
					currentNeuron.weights[k] = NeuralUtils.parseDouble(StreamHelper.getString(s, 8));
				}
			}
		}
		return ret;
	}

	@Override
	public void calculateOutput(double[] input) {
		for (int i = 0; i < neurons.length; i++) {
			double[] nextInput = new double[neurons[i].length];
			for (int j = 0; j < neurons[i].length; j++) {
				nextInput[j] = neurons[i][j].calculateOutput(input);
			}
			input = nextInput;
		}
	}

	@Override
	public void adjustForAnswer(double[] input, double[] answers) {
		if (answers.length < getNumOutputs()) {
			System.err.println("Desired output length is shorter than real output length.");
		}
		calculateOutput(input);
		for (int i = 0; i < getNumOutputs(); i++) {
			neurons[neurons.length - 1][i].error = answers[i] - neurons[neurons.length - 1][i].gamma;
		}
		// backtrack through each layer, propagating error with each step.
		for (int i = neurons.length - 1; i > 0; i--) {
			double[] currentInputs = new double[neurons[i - 1].length];
			for (int j = 0; j < currentInputs.length; j++) {
				currentInputs[j] = neurons[i - 1][j].gamma;
			}
			for (int j = 0; j < neurons[i].length; j++) {
				neurons[i][j].distributeError(neurons[i - 1]);
				neurons[i][j].adjustWeights(currentInputs);
			}
		}
		for (int j = 0; j < neurons[0].length; j++) {
			neurons[0][j].adjustWeights(input);
		}
		if (getVerbosity() >= 4) {
			boolean anyWrong = false;
			for (int i = 0; i < answers.length; i++) {
				double gamma = neurons[neurons.length - 1][i].getAdjustedGamma(getGammaTruncation());
				if (answers[i] != gamma) {
					anyWrong = true;
					break;
				}
			}
			if (anyWrong) {
				for (int i = 0; i < answers.length; i++) {
					double gamma = neurons[neurons.length - 1][i].getAdjustedGamma(getGammaTruncation());
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

	// public void adjustForAnswer(double[] input, double[] answers) {
	// calculateAndStoreOutput(input);
	// for (int i = 0; i < getNumOutputs(); i++) {
	// neurons[neurons.length - 1][i].error = answers[i]
	// - neurons[neurons.length - 1][i].gamma;
	// }
	// for (int i = neurons.length - 1; i >= 0; i--) {
	// double[] currentInputs;
	// if (i > 0) {
	// currentInputs = new double[neurons[i - 1].length];
	// for (int j = 0; j < currentInputs.length; j++) {
	// currentInputs[j] = neurons[i - 1][j].gamma;
	// }
	// for (int j = 0; j < neurons[i].length; j++) {
	// neurons[i][j].adjustWeights(neurons[i - 1], currentInputs,
	// LEARNING_RATE);
	// }
	// } else {
	// currentInputs = input;
	// for (int j = 0; j < neurons[i].length; j++) {
	// neurons[i][j].adjustWeights(null, currentInputs,
	// LEARNING_RATE);
	// }
	// }
	// }
	// if (getVerbosity() >= 4) {
	// for (int i = 0; i < answers.length; i++) {
	// double gamma = neurons[neurons.length - 1][i]
	// .getAdjustedGamma(getGammaTruncation());
	// if (answers[i] != gamma) {
	// System.out.print("*****");
	// }
	// System.out.println(i + ":::" + answers[i] + "|" + gamma);
	// }
	// }
	// }

	public static void adjust(FFNN network, String inputString, double[] output) {
		double[] input = new double[inputString.length() * 8];
		for (int i = 0; i < inputString.length(); i++) {
			int num = inputString.charAt(i);
			for (int j = 0; j < 8; j++) {
				input[i * 8 + j] = num % 2;
				num /= 2;
			}
		}
		// double[] output = new double[outputString.length() * 8];
		// for (int i = 0; i < outputString.length(); i++) {
		// int num = (int) outputString.charAt(i);
		// for (int j = 0; j < 8; j++) {
		// output[i * 8 + j] = num % 2;
		// num /= 2;
		// }
		// }
		network.adjustForAnswer(input, output);
	}

	public static String stringParse(FFNN network) {
		char[] ret = new char[network.getNumOutputs() / 8];
		for (int i = 0; i < network.getNumOutputs(); i += 8) {
			int charSum = 0;
			for (int j = 7; j >= 0; j--) {
				charSum *= 2;
				if (network.neurons[network.neurons.length - 1][i + j].gamma >= 0.5) {
					charSum++;
				}
			}
			ret[i / 8] = (char) charSum;
		}
		return new String(ret);
	}

	@Override
	public int trainTillPerfection(double[][] problems, double[][] solutions, boolean randomizeOrder) {
		return trainTillPerfection(problems, solutions, randomizeOrder, null);
	}

	public int trainTillPerfection(double[][] problems, double[][] solutions, boolean randomizeOrder, FFNNHelper helper) {
		boolean done = false;
		int iteration = 0;
		FFNNDisplay visual = null;
		if (isDisplayingVisually()) {
			visual = new FFNNDisplay(this, getDisplayWidth(), getDisplayHeight(), getDisplayDelay());
		}
		JFrame graphFrame = new JFrame();
		graphFrame.setLocation(visual.getX() + visual.getWidth(), 0);
		graphFrame.setSize(500, 500);
		graphFrame.setContentPane(new Container() {
			private static final long serialVersionUID = 1L;

			@Override
			public void paint(Graphics g) {
				double rm = 0;
				for (int i = 0; i < RMSES.size(); i++) {
					rm = Math.max(rm, RMSES.get(i));
				}
				g.setColor(Color.red);
				for (int i = 0; i < RMSES.size(); i++) {
					int x = (int) ((double) i / RMSES.size() * getWidth());
					g.drawLine(x, getHeight() - 1, x, getHeight() - 1 - (int) (RMSES.get(i) / rm * getHeight()));
				}
			}
		});
		graphFrame.setVisible(true);
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
			double RMSE = 0;
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
					// neurons[neurons.length -
					// 1][i].getAdjustedGamma(getGammaTruncation()) !=
					// solutions[j][i]
					if (Math.abs(solutions[j][i] - neurons[neurons.length - 1][i].gamma) > getGammaTruncation()) {
						RMSE += Math.pow(solutions[j][i] - neurons[neurons.length - 1][i].gamma, 2);
						done = false;
					}
				}
			}
			RMSES.add(Math.pow(RMSE / (problems.length * getNumOutputs()), 0.5));
			if (helper != null) {
				helper.doPostIterationAction(iteration);
			}
			iteration++;
			graphFrame.repaint();
			if (isDisplayingVisually()) {
				visual.setVisible(true);
			}

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
				}
				if (isDisplayingVisually()) {
					visual.continueRepainting();
				}
				break;
			}
		}
		return iteration;
	}

	ArrayList<Double> RMSES = new ArrayList<Double>();

	public char getResultsAsChar() {
		char ret = 0;
		for (int i = 7; i >= 0; i--) {
			ret *= 2;
			ret += neurons[neurons.length - 1][i].getTruncatedGamma();
		}
		return ret;
	}

	public Neuron getNeuron(int layer, int index) {
		return neurons[layer][index];
	}

	public String getResultsAsString() {
		double[] endValues = new double[getNumOutputs()];
		for (int i = 0; i < getNumOutputs(); i++) {
			endValues[i] = neurons[neurons.length - 1][i].gamma;
		}
		return NeuralUtils.toString(endValues);
	}

	public ArrayList<Double> getResults() {
		ArrayList<Double> endValues = new ArrayList<Double>();
		for (int i = 0; i < getNumOutputs(); i++) {
			endValues.add(neurons[neurons.length - 1][i].gamma);
		}
		return endValues;
	}
}