package examples;

import neural.FFNN;
import neural.NeuralUtils;

public class TreeLearner {
	public static void main(String[] args) {
		FFNN n = new FFNN(new int[] { 9, 1 });
		n.setGammaTruncation(0.1);
		n.setVerbosity(5);
		n.setDisplayVisually(true, 500, 500, 0);
		double[][] problems = { { 1, 0, 0, 1, 0, 1, 0, 1, 0 }, { 1, 0, 0, 0, 0, 1, 1, 0, 1 }, { 1, 0, 0, 0, 0, 1, 1, 0, 1 }, { 0, 0, 1, 0, 1, 0, 1, 0, 0 }, { 0, 1, 0, 1, 0, 1, 0, 1, 0 }, { 0, 1, 1, 1, 1, 0, 0, 1, 1 } };
		double[][] solutions = { { 0 }, { 0 }, { 0 }, { 1 }, { 1 }, { 1 }};
		n.trainTillPerfection(problems, solutions);
		double[] problem = new double[] { 1, 0, 0, 0, 1, 0, 0, 0, 1 };
		// double[] problem = new double[] { 0, 0, 1, 0, 1, 0, 1, 0, 0 };
		NeuralUtils.print(problem);
	}
}
