package neural;

import graphics.basicShapes.Line3D;
import graphics.basicShapes.Point3D;
import graphics.basicShapes.Vector3D;
import graphics.genMath.Math3D;
import graphics.only3d.Camera;
import graphics.only3d.World3D;
import helpers.DelayHelper;
import helpers.PriorityQueue;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JRootPane;

@SuppressWarnings("serial")
public class UndirectedNNDisplay extends JFrame implements KeyListener,
		MouseMotionListener, MouseListener {

	World3D w;
	private Vector3D player;
	private Camera camera;
	private double spin = 0;
	private double tilt = 0;
	private int minTilt = 1;
	private int maxTilt = 179;

	public boolean[] keyPressBuffer = new boolean[1024];
	private Point lastMousePress;
	private Point lastMouseDrag;

	public static final int DIFF = 40;
	private final UndirectedNN network;
	Point3D middleBiasLocation;
	Point3D outputBiasLocation;
	Point3D[] neuronLocations;
	Point3D[] outputNeuronLocations;

	public UndirectedNNDisplay(final UndirectedNN network, final int width,
			final int height, final int displayDelay) {
		this.w = new World3D();
		this.player = new Vector3D(-width, 0, -height, 45, 25);
		this.network = network;
		this.setSize(width + DIFF, height + DIFF);
		calculateNeuronLocations(1000, 1000, 1000);
		camera = new Camera(0, 0, 0, 0, 0);
		spin = 45;
		tilt = 115;
		JRootPane customPane = new JRootPane() {

			PriorityQueue<Double, Object[]> linesToDraw = new PriorityQueue<Double, Object[]>();

			public void paint(Graphics g) {
				long st = System.currentTimeMillis();
				BufferedImage buffer = (BufferedImage) createImage(getWidth(),
						getHeight());
				Graphics2D bg = (Graphics2D) buffer.getGraphics();
				prepareToDrawNeurons(network, bg, width, height);
				while (!linesToDraw.isEmpty()) {
					Object[] curLine = linesToDraw.poll();
					Point from = ((Point) curLine[1]);
					Point to = ((Point) curLine[2]);
					bg.setColor(new Color((int) curLine[0], 0, 0));
					NeuralUtils.drawLine(bg, (int) from.getX(),
							(int) from.getY(), (int) to.getX(),
							(int) to.getY(), (int) curLine[3]);
				}
				bg.setColor(Color.black);
				for (int i = 0; i < neuronLocations.length; i++) {
					Point p = neuronLocations[i].getDrawPoint(camera);
					bg.fillRect(p.x, p.y, 5, 5);
				}
				for (int i = 0; i < outputNeuronLocations.length; i++) {
					Point p = outputNeuronLocations[i].getDrawPoint(camera);
					bg.fillRect(p.x, p.y, 5, 5);
				}
				bg.setColor(Color.green);
				Point p1 = middleBiasLocation.getDrawPoint(camera);
				bg.fillRect(p1.x, p1.y, 10, 10);
				Point p2 = outputBiasLocation.getDrawPoint(camera);
				bg.fillRect(p2.x, p2.y, 10, 10);
				adjustCamera();
				w.paint(bg, camera);

				bg.setColor(Color.black);
				bg.drawString(
						"WASD keys move forward,left,back, and right. Shift moves down and Spacebar moves up. Click and drag to look around.",
						20, 10);

				g.drawImage(buffer, 0, 0, null);
				DelayHelper.delayFor(displayDelay
						- (System.currentTimeMillis() - st));
				checkForKeyPresses();
			}

			private void prepareToDrawNeurons(final UndirectedNN network,
					Graphics2D bg, int width, int height) {
				double mw = findMaxWeight();
				// Draw the weights to middle neurons
				for (int i = 0; i < network.neurons.size(); i++) {
					// Draw the biases to the middle neurons
					drawLineFor(width, height, mw, middleBiasLocation,
							neuronLocations[i + network.getNumInputs()],
							network.neurons.get(i).biasWeight);
					// Draw the weights from the input neurons
					for (int j = 0; j < network.getNumInputs(); j++) {
						drawLineFor(width, height, mw, neuronLocations[j],
								neuronLocations[i + network.getNumInputs()],
								network.neurons.get(i).inputWeights.get(j));
					}
					// Draw the weights from other middle neurons
					for (int j = 0; j < network.neurons.get(i).getNumInputs(); j++) {
						drawLineFor(
								width,
								height,
								mw,
								neuronLocations[i + network.getNumInputs()],
								neuronLocations[network
										.getNeuronIndex(network.neurons.get(i).inputNeurons
												.get(j))
										+ network.getNumInputs()],
								network.neurons.get(i).inputWeights.get(j
										+ network.getNumInputs()));
					}
				}
				// Draw the weights to output neurons
				for (int i = 0; i < network.getNumOutputs(); i++) {
					// Draw the bias for the output neurons
					drawLineFor(width, height, mw, outputBiasLocation,
							outputNeuronLocations[i],
							network.outputNeurons.get(i).biasWeight);
					// draw the weights from middle neurons
					for (int j = 0; j < network.neurons.size(); j++) {
						drawLineFor(width, height, mw, neuronLocations[j
								+ network.getNumInputs()],
								outputNeuronLocations[i],
								network.outputNeurons.get(i).inputWeights
										.get(j));
					}
				}
			}

			private void drawLineFor(int width, int height, double mw,
					Point3D n1, Point3D n2, double w) {
				Point3D n1c = n1.getCameraPoint(camera);
				Point3D n2c = n2.getCameraPoint(camera);
				if (n1c.getZ() > 0 || n2c.getZ() > 0) {
					if (n2c.getZ() <= 0) {
						n2 = Point3D.getPointBetween(n1, n2, (n1c.getZ() - 0.1)
								/ (n1c.getZ() - n2c.getZ()));
					} else if (n1c.getZ() <= 0) {
						n1 = Point3D.getPointBetween(n2, n1, (n2c.getZ() - 0.1)
								/ (n2c.getZ() - n1c.getZ()));
					}
					int r = (int) (128 + w / mw * 127);
					// Tossed in to fix various color-related
					// bugs...shouldn't
					// actually be necessary
					if (r < 0) {
						r = 0;
					}
					if (r > 255) {
						r = 255;
					}
					linesToDraw.put(
							-new Line3D(n1, n2).getDistanceTo(camera),
							new Object[] { r, n1.getDrawPoint(camera),
									n2.getDrawPoint(camera),
									(int) (Math.abs(w) / mw * 10 + 0.5) });
				}
			}
		};
		setContentPane(customPane);
		getComponent(0).addMouseMotionListener(this);
		getComponent(0).addMouseListener(this);
		addKeyListener(this);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}

	private void adjustCamera() {
		camera.setFieldWidth(getWidth());
		camera.setFieldHeight(getHeight());
		camera.setLocation(player);
		camera.setY(camera.getY() + 100);
		tilt = Math.max(minTilt, Math.min(maxTilt, tilt));
		player.setZenith(spin);
		player.setAzimuth(tilt);
		camera.setLocation(Math3D.getRotatedPoint(player, camera, spin, tilt));
		camera.turnToFace(player);
	}

	private double findMaxWeight() {
		double mw = 10;
		for (int i = 0; i < network.neurons.size(); i++) {
			mw = Math.max(mw, Math.abs(network.neurons.get(i).biasWeight));
			for (int j = 0; j < network.neurons.get(i).getNumInputs(); j++) {
				mw = Math.max(mw,
						Math.abs(network.neurons.get(i).inputWeights.get(j)));
			}
		}
		for (int i = 0; i < network.outputNeurons.size(); i++) {
			mw = Math
					.max(mw, Math.abs(network.outputNeurons.get(i).biasWeight));
			for (int j = 0; j < network.outputNeurons.get(i).getNumInputs(); j++) {
				mw = Math.max(mw,
						Math.abs(network.outputNeurons.get(i).inputWeights
								.get(j)));
			}
		}
		return mw;
	}

	private void calculateNeuronLocations(int width, int height, int depth) {
		int biaseDistance = 100;
		// biases
		middleBiasLocation = new Point3D(width / 2.0, 0, -biaseDistance);
		outputBiasLocation = new Point3D(width / 2.0, height, -biaseDistance);
		neuronLocations = new Point3D[network.getNumInputs()
				+ network.neurons.size()];
		// input neurons
		for (int i = 0; i < network.getNumInputs(); i++) {
			neuronLocations[i] = new Point3D(width / 2.0, -height / 10,
					((double) i / network.getNumInputs()) * depth);
		}
		// middle neurons
		for (int i = 0; i < network.neurons.size(); i++) {
			neuronLocations[i + network.getNumInputs()] = new Point3D(
					Math.random() * width, Math.random() * height,
					Math.random() * depth);
		}
		// output neurons
		outputNeuronLocations = new Point3D[network.getNumOutputs()];
		for (int i = 0; i < network.getNumOutputs(); i++) {
			outputNeuronLocations[i] = new Point3D(
					((double) i / network.getNumOutputs()) * width,
					height * 1.1, depth / 2);
		}
	}

	public void checkForKeyPresses() {
		int move = 5;
		if (this.keyPressBuffer[87]) {
			this.player.setLocation(Math3D.rotatePoint(this.player, move,
					this.player.getZenith(), 0));
		}
		if (this.keyPressBuffer[83]) {
			this.player.setLocation(Math3D.rotatePoint(this.player, move,
					this.player.getZenith(), 180));
		}
		if (this.keyPressBuffer[65]) {
			this.player.setLocation(Math3D.rotatePoint(this.player, move,
					this.player.getZenith() - 90.0D, 0.0D));
		}
		if (this.keyPressBuffer[68]) {
			this.player.setLocation(Math3D.rotatePoint(this.player, move,
					this.player.getZenith() + 90.0D, 0.0D));
		}
		if (this.keyPressBuffer[16]) {
			player.setY(player.getY() + move);
		}
		if (this.keyPressBuffer[32]) {
			player.setY(player.getY() - move);
		}
		double rot = 0.01;
		if (this.keyPressBuffer[37]) {
			this.spin += rot;
		}
		if (this.keyPressBuffer[39]) {
			this.spin -= rot;
		}
		if (this.keyPressBuffer[38]) {
			this.tilt -= rot;
		}
		if (this.keyPressBuffer[40]) {
			this.tilt += rot;
		}
		if (this.player != null) {
			adjustCamera();
		}
	}

	public void keyPressed(KeyEvent e) {
		this.keyPressBuffer[e.getKeyCode()] = true;
	}

	public void keyReleased(KeyEvent e) {
		this.keyPressBuffer[e.getKeyCode()] = false;
	}

	public void keyTyped(KeyEvent e) {
	}

	public void mouseClicked(MouseEvent arg0) {
	}

	public void mouseEntered(MouseEvent arg0) {
	}

	public void mouseExited(MouseEvent arg0) {
	}

	public void mousePressed(MouseEvent e) {
		this.lastMousePress = new Point(e.getX(), e.getY());
	}

	public void mouseReleased(MouseEvent arg0) {
		this.lastMouseDrag = null;
	}

	public void mouseDragged(MouseEvent e) {
		if (this.lastMouseDrag == null) {
			this.lastMouseDrag = this.lastMousePress;
		}
		if (this.lastMouseDrag != null) {
			double dx = e.getX() - this.lastMouseDrag.getX();
			double dy = e.getY() - this.lastMouseDrag.getY();
			this.spin += dx / 2;
			this.tilt += dy / 2;
		}
		this.lastMouseDrag = new Point(e.getX(), e.getY());
	}

	public void mouseMoved(MouseEvent arg0) {
	}

	public void continueRepainting() {
		new Thread() {
			public void run() {
				while (true) {
					repaint();
				}
			}
		}.start();
	}
}
