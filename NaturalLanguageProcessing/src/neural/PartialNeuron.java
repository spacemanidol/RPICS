package neural;

import helpers.MathHelper;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Set;

public class PartialNeuron {

	Hashtable<PartialNeuron, Double> weights;
	ArrayList<PartialNeuron> outputs;
	double biasWeight;
	double lastCalculatedOutput;
	double error;

	public PartialNeuron() {
		biasWeight = NeuralUtils.getRandomNeuronWeight();
		weights = new Hashtable<PartialNeuron, Double>();
	}

	public PartialNeuron(ArrayList<PartialNeuron> inputs) {
		this();
		for (PartialNeuron neuron : inputs) {
			addInput(neuron);
		}
	}

	public void addInput(PartialNeuron neuron) {
		weights.put(neuron, NeuralUtils.getRandomNeuronWeight());
	}

	private Set<PartialNeuron> getInputNeurons() {
		return weights.keySet();
	}

	public void adjustWeights(double learningRate) {
		double delta = lastCalculatedOutput * (1 - lastCalculatedOutput) * error;
		for (PartialNeuron input : getInputNeurons()) {
			input.error += delta * weights.get(input);
			weights.put(input, weights.get(input) + learningRate * input.lastCalculatedOutput * delta);
		}
		error = 0;
		biasWeight -= learningRate * delta;
	}

	public double calculateOutput() {
		double sum = -biasWeight;
		for (PartialNeuron input : getInputNeurons()) {
			sum += input.lastCalculatedOutput * weights.get(input);
		}
		lastCalculatedOutput = MathHelper.sigmoid(sum);
		return lastCalculatedOutput;
	}

	public double getAdjustedGamma(double adjustmentRate) {
		if (lastCalculatedOutput >= 1 - adjustmentRate) {
			return 1;
		}
		if (lastCalculatedOutput < 0 + adjustmentRate) {
			return 0;
		}
		return lastCalculatedOutput;
	}

	public double getTruncatedGamma() {
		if (lastCalculatedOutput >= 0.5) {
			return 1;
		}
		return 0;
	}
}
