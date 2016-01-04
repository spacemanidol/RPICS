package neural;

public class PartialNN {

	PartialNeuron[][] neurons;

	public PartialNN(int[] sizes,int initialConnectivity) {
		neurons = new PartialNeuron[sizes.length - 1][];
		for (int i = 0; i < sizes.length - 1; i++) {
			neurons[i] = new PartialNeuron[sizes[i + 1]];
			for (int j = 0; j < sizes[i + 1]; j++) {
				neurons[i][j] = new PartialNeuron();
			}
		}
	}

	public void calculateAndStoreError(double[] answers) {
		// TODO Auto-generated method stub

	}

	public double[] calculateAndStoreOutput(double[] input) {
		// TODO Auto-generated method stub
		return null;
	}

	public void adjustForAnswer(double[] input, double[] answers) {
		calculateAndStoreOutput(input);
		calculateAndStoreError(answers);
		// TODO Auto-generated method stub

	}

	public int trainTillPerfection(double[][] problems, double[][] solutions, boolean randomizeOrder) {
		// TODO Auto-generated method stub
		return 0;
	}
}
