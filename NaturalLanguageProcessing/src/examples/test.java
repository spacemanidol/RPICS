package examples;

import neural.*;

public class test {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void main(String[] args) {
		NeuralNetwork n = new UndirectedNN(2, 1, 3, 2);
		// NeuralNetwork n = new FFNN(new int[] { 1, 1, 1 });
		n.setGammaTruncation(0.1);
		n.setVerbosity(2);
		n.setDisplayVisually(true, 500, 500, 0);
		double[][] problems = NeuralUtils.getBinaryCombos(2);
		double[][] solutions = { { 0 }, { 1 }, { 1 }, { 0 } };
		n.trainTillPerfection(problems, solutions, false);
		// FFNN n = new FFNN(new int[] { 2, 1 });/*
		// n.setGammaTruncation(0.5);
		// n.setVerbosity(5);
		// n.setDisplayVisually(true, 500, 500, 0);
		// double[][] problems = { { 0, 0 }, { 0, 1 }, { 1, 0 }, { 1, 1 } };
		// double[][] solutions = { { 0 }, { 1 }, { 1 }, { 1 } };
		// n.trainTillPerfection(problems, solutions);*/
	}
}
