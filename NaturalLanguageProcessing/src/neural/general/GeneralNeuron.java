package neural.general;

import java.util.ArrayList;

import neural.NeuralNetwork;
import neural.NeuralUtils;

public class GeneralNeuron {

	public ArrayList<GeneralNeuron> inputNeurons;
	public ArrayList<Double> inputWeights;
	public double biasWeight;
	public double output;
	public double error;

	public GeneralNeuron(int numInputs) {
		biasWeight = NeuralUtils.getRandomNeuronWeight();
		inputNeurons = new ArrayList<GeneralNeuron>();
		inputWeights = new ArrayList<Double>();
		for (int i = 0; i < numInputs; i++) {
			inputWeights.add(NeuralUtils.getRandomNeuronWeight());
		}
	}

	public int getNumInputs() {
		return inputNeurons.size();
	}

	public void adjustInputWeights() {
		double delta = output * (1 - output) * error;
		for (int i = 0; i < getNumInputs(); i++) {
			inputNeurons.get(i).error += delta * inputWeights.get(i);
			inputWeights.set(i, inputWeights.get(i)
					- NeuralNetwork.LEARNING_RATE * inputNeurons.get(i).output
					* delta);
		}
		error = 0;
		biasWeight += NeuralNetwork.LEARNING_RATE * delta;
	}

	public double getOutputFrom(double[] inputs) {
		double sum = biasWeight;
		for (int i = 0; i < inputs.length; i++) {
			sum += inputs[i] * inputWeights.get(i);
		}
		return (1.0 / (1 + Math.pow(Math.E, (-1) * sum)));
	}

	public double getOutputWith(double[] extraInputs) {
		double sum = biasWeight;
		for (int i = 0; i < extraInputs.length; i++) {
			sum += extraInputs[i] * inputWeights.get(i);
		}
		for (int i = 0; i < inputNeurons.size(); i++) {
			sum += inputNeurons.get(i).output
					* inputWeights.get(i + extraInputs.length);
		}
		return (1.0 / (1 + Math.pow(Math.E, (-1) * sum)));
	}

	public double getTruncatedOutput(double adjustmentRate) {
		if (output >= 1 - adjustmentRate) {
			return 1;
		}
		if (output < 0 + adjustmentRate) {
			return 0;
		}
		return output;
	}
}
