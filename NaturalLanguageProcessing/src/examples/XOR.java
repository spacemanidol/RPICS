package examples;

import neural.FFNN;

public class XOR {
	public static void main(String[] args) {
		FFNN n = new FFNN(new int[] { 2, 3, 1 });
		n.setGammaTruncation(0.1);
		n.setVerbosity(2);
		n.setDisplayVisually(true, 500, 500, 0);
		double[][] problems = { { 0, 0 }, { 0, 1 }, { 1, 0 }, { 1, 1 } };
		double[][] solutions = { { 0 }, { 1 }, { 1 }, { 0 } };
		n.trainTillPerfection(problems, solutions);
	}
}
