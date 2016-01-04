package examples;

import neural.FFNN;

public class AND {

	public static void main(String[] args) {
		FFNN n = new FFNN(new int[] { 2, 1 });
		n.setGammaTruncation(0.5);
		n.setVerbosity(5);
		n.setDisplayVisually(true, 500, 500, 0);
		double[][] problems = { { 0, 0 }, { 0, 1 }, { 1, 0 }, { 1, 1 } };
		double[][] solutions = { { 0 }, { 0 }, { 0 }, { 1 } };
		n.trainTillPerfection(problems, solutions);
	}
}
