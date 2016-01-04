package examples;

import neural.UndirectedNN;

public class OR {

	public static void main(String[] args) {
		UndirectedNN n = new UndirectedNN(2, 1, 1, 1);
		n.setGammaTruncation(0.5);
		n.setVerbosity(5);
		n.setDisplayVisually(true, 500, 500, 0);
		double[][] problems = { { 0, 0 }, { 0, 1 }, { 1, 0 }, { 1, 1 } };
		double[][] solutions = { { 0 }, { 1 }, { 1 }, { 1 } };
		n.trainTillPerfection(problems, solutions);
		// FFNN n = new FFNN(new int[] { 2, 1 });/*
		// n.setGammaTruncation(0.5);
		// n.setVerbosity(5);
		// n.setDisplayVisually(true, 500, 500, 0);
		// double[][] problems = { { 0, 0 }, { 0, 1 }, { 1, 0 }, { 1, 1 } };
		// double[][] solutions = { { 0 }, { 1 }, { 1 }, { 1 } };
		// n.trainTillPerfection(problems, solutions);*/
	}
}
