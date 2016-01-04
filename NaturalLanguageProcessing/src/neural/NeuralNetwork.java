package neural;

public abstract class NeuralNetwork<I, O> {

	// fields that are actually important to the network's functioning
	public static final double LEARNING_RATE = 1;
	private final int numInputs;
	private final int numOutputs;

	// fields that control when a solution is counted as correct.
	private double gammaTruncation = 0.1;

	// fields for display/output reasons. NO effect on execution.
	private int verbosity = 0;
	private boolean displayVisually = false;
	private int displayWidth = 400;
	private int displayHeight = 400;
	private int displayDelay = 0;

	public NeuralNetwork(int numInputs, int numOutputs) {
		this.numInputs = numInputs;
		this.numOutputs = numOutputs;
	}

	public boolean isDisplayingVisually() {
		return displayVisually;
	}

	public int getDisplayWidth() {
		return displayWidth;
	}

	public int getDisplayHeight() {
		return displayHeight;
	}

	public int getDisplayDelay() {
		return displayDelay;
	}

	public void setDisplayVisually(boolean displayVisually) {
		this.displayVisually = displayVisually;
	}

	public void setDisplayVisually(boolean displayVisually, int displayWidth, int displayHeight, int displayDelay) {
		this.displayVisually = displayVisually;
		this.displayWidth = displayWidth;
		this.displayHeight = displayHeight;
		this.displayDelay = Math.max(20, displayDelay);
	}

	public static double sigmoid(double num) {
		return 1.0 / (1 + Math.pow(Math.E, -num));
	}

	public int getNumInputs() {
		return numInputs;
	}

	public int getNumOutputs() {
		return numOutputs;
	}

	public int getVerbosity() {
		return verbosity;
	}

	public void setVerbosity(int verbosity) {
		this.verbosity = verbosity;
	}

	public double getGammaTruncation() {
		return gammaTruncation;
	}

	public void setGammaTruncation(double gammaTruncation) {
		this.gammaTruncation = gammaTruncation;
	}

	/**
	 * Calculates the output of this neural network. The exact method of
	 * accessing the output is left to the network implementation.
	 */
	public abstract void calculateOutput(I input);

	public abstract void adjustForAnswer(I input, O answers);

	public int trainTillPerfection(I[] problems, O[] solutions) {
		return trainTillPerfection(problems, solutions, true);
	}

	public abstract int trainTillPerfection(I[] problems, O[] solutions, boolean randomizeOrder);
}
