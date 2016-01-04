package neural;

public class Neuron {

	double[] weights;
	double biasWeight;
	double gamma;
	double error;

	public Neuron(int inputs) {
		biasWeight =NeuralUtils.getRandomNeuronWeight();
		weights = new double[inputs];
		for (int i = 0; i < weights.length; i++) {
			weights[i] = NeuralUtils.getRandomNeuronWeight();
		}
	}

	double delta() {
		return gamma * (1 - gamma) * error;
	}

	public void distributeError(Neuron[] inputs) {
		for (int i = 0; i < inputs.length; i++) {
			inputs[i].error += NeuralNetwork.LEARNING_RATE * delta() * weights[i];
		}
	}

	public void adjustWeights(double[] inputValues) {
		for (int i = 0; i < weights.length; i++) {
			weights[i] += NeuralNetwork.LEARNING_RATE * inputValues[i] * delta();
		}
		error = 0;
		biasWeight -= NeuralNetwork.LEARNING_RATE * delta();
	}

	private static double sigmoid(double exponent) {
		return (1.0 / (1 + Math.pow(Math.E, (-1) * exponent)));
	}

	public double calculateOutput(double[] inputs) {
		double sum = -biasWeight;
		for (int i = 0; i < inputs.length; i++) {
			sum += inputs[i] * weights[i];
		}
		gamma = sigmoid(sum);
		return gamma;
	}

	public double getAdjustedGamma(double adjustmentRate) {
		if (gamma >= 1 - adjustmentRate) {
			return 1;
		}
		if (gamma < 0 + adjustmentRate) {
			return 0;
		}
		return gamma;
	}

	public double getTruncatedGamma() {
		if (gamma >= 0.5) {
			return 1;
		}
		return 0;
	}
}
