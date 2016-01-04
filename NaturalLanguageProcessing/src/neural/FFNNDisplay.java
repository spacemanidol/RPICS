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
public class FFNNDisplay extends JFrame implements KeyListener, MouseMotionListener, MouseListener {

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
	private final FFNN network;
	Point3D[][] neuronLocations;

	public FFNNDisplay(final FFNN network, final int width, final int height, final int displayDelay) {
		this.w = new World3D();
		this.player = new Vector3D(-width, 0, -height, 45, 25);
		this.network = network;
		this.setSize(width + DIFF, height + DIFF);
		neuronLocations = calculateNeuronLocations(100);
		camera = new Camera(0, 0, 0, 0, 0);
		spin = 45;
		tilt = 115;
		JRootPane customPane = new JRootPane() {

			PriorityQueue<Double, Object[]> linesToDraw = new PriorityQueue<Double, Object[]>();

			public void paint(Graphics g) {
				long st = System.currentTimeMillis();
				BufferedImage buffer = (BufferedImage) createImage(getWidth(), getHeight());
				Graphics2D bg = (Graphics2D) buffer.getGraphics();
				prepareToDrawNeurons(network, bg, width, height);
				while (!linesToDraw.isEmpty()) {
					Object[] curLine = linesToDraw.poll();
					Point from = ((Point) curLine[1]);
					Point to = ((Point) curLine[2]);
					bg.setColor(new Color((int) curLine[0], 0, 0));
					NeuralUtils.drawLine(bg, (int) from.getX(), (int) from.getY(), (int) to.getX(), (int) to.getY(), (int) curLine[3]);
				}
				adjustCamera();
				w.paint(bg, camera);

				bg.setColor(Color.black);
				bg.drawString("WASD keys move forward,left,back, and right. Shift moves down and Spacebar moves up. Click and drag to look around.", 20, 10);

				g.drawImage(buffer, 0, 0, null);
				DelayHelper.delayFor(displayDelay - (System.currentTimeMillis() - st));
				checkForKeyPresses();
			}

			private void prepareToDrawNeurons(final FFNN network, Graphics2D bg, int width, int height) {
				bg.setColor(Color.black);
				double mw = findMaxWeight();
				for (int i = 0; i < network.neurons.length; i++) {
					for (int j = 0; j < network.neurons[i].length; j++) {
						Point3D biasLocation = neuronLocations[i][0];
						if (biasLocation.getZ() > 0) {
							bg.fillRect((int) biasLocation.getDrawPoint(camera).getX() - 4, (int) biasLocation.getDrawPoint(camera).getY() - 4, 9, 9);
						}
						Point3D endNeuronLocation = neuronLocations[i + 1][j + 1];
						if (endNeuronLocation.getZ() > 0) {
							bg.fillRect((int) endNeuronLocation.getDrawPoint(camera).getX() - 2, (int) endNeuronLocation.getDrawPoint(camera).getY() - 2, 5, 5);
						}
						drawLineFor(width, height, mw, biasLocation, endNeuronLocation, network.neurons[i][j].biasWeight);
						for (int k = 0; k < (i == 0 ? network.getNumInputs() : network.neurons[i - 1].length); k++) {
							Point3D startNeuronLocation = neuronLocations[i][k + 1];
							if (startNeuronLocation.getZ() > 0) {
								bg.fillRect((int) startNeuronLocation.getDrawPoint(camera).getX() - 2, (int) startNeuronLocation.getDrawPoint(camera).getY() - 2, 5, 5);
							}
							drawLineFor(width, height, mw, startNeuronLocation, endNeuronLocation, network.neurons[i][j].weights[k]);
						}
					}
				}
			}

			private void drawLineFor(int width, int height, double mw, Point3D n1, Point3D n2, double w) {
				Point3D n1c = n1.getCameraPoint(camera);
				Point3D n2c = n2.getCameraPoint(camera);
				if (n1c.getZ() > 0 || n2c.getZ() > 0) {
					if (n2c.getZ() <= 0) {
						n2 = Point3D.getPointBetween(n1, n2, (n1c.getZ() - 0.1) / (n1c.getZ() - n2c.getZ()));
					} else if (n1c.getZ() <= 0) {
						n1 = Point3D.getPointBetween(n2, n1, (n2c.getZ() - 0.1) / (n2c.getZ() - n1c.getZ()));
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
					linesToDraw.put(-new Line3D(n1, n2).getDistanceTo(camera), new Object[] { r, n1.getDrawPoint(camera), n2.getDrawPoint(camera), (int) (Math.abs(w) / mw * 10 + 0.5) });
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
		for (int i = 0; i < network.neurons.length; i++) {
			for (int j = 0; j < network.neurons[i].length; j++) {
				mw = Math.max(mw, Math.abs(network.neurons[i][j].biasWeight));
				for (int k = 0; k < (i == 0 ? network.getNumInputs() : network.neurons[i - 1].length); k++) {
					mw = Math.max(mw, Math.abs(network.neurons[i][j].weights[k]));
				}
			}
		}
		return mw;
	}

	private Point3D[][] calculateNeuronLocations(int b) {
		Point3D[][] neuronLocations = new Point3D[network.neurons.length + 1][];
		neuronLocations[0] = new Point3D[network.getNumInputs() + 1];
		neuronLocations[0][0] = new Point3D(getWidth() / 2, 0, 0);
		// Draw the bias weights
		for (int i = 0; i < network.neurons.length; i++) {
			neuronLocations[i + 1] = new Point3D[network.neurons[i].length + 1];
			int d = getWidth() / 2;
			neuronLocations[i + 1][0] = new Point3D(i % 2 == 1 ? d : 0, (int) ((double) (i + 1) / network.neurons.length * (getHeight() - DIFF)), i % 2 == 0 ? d : 0);
		}
		// Draw the input layer to first layer weights
		for (int i = 1; i <= network.getNumInputs(); i++) {
			double d = (double) (i - 1) / (network.getNumInputs() - 1);
			if (i == 1 && network.getNumInputs() == 1) {
				d = 0;
			}
			neuronLocations[0][i] = new Point3D((int) (b + d * (getWidth() - DIFF - b)) - b - getWidth() / 2, 0, 0);
		}
		// Draw all of the actual neuron weights
		for (int i = 1; i <= network.neurons.length; i++) {
			for (int j = 1; j <= network.neurons[i - 1].length; j++) {
				double widthRatio = (double) (j - 1) / (network.neurons[i - 1].length - 1);
				if (network.neurons[i - 1].length == 1) {
					widthRatio = 0;
				}
				int d = (int) (b + widthRatio * (getWidth() - DIFF - b)) - b - getWidth() / 2;
				neuronLocations[i][j] = new Point3D(i % 2 == 0 ? d : 0, (int) ((double) i / network.neurons.length * (getHeight() - DIFF)), i % 2 == 1 ? d : 0);
			}
		}
		return neuronLocations;
	}

	public void checkForKeyPresses() {
		int move = 5;
		if (this.keyPressBuffer[87]) {
			this.player.setLocation(Math3D.rotatePoint(this.player, move, this.player.getZenith(), 0));
		}
		if (this.keyPressBuffer[83]) {
			this.player.setLocation(Math3D.rotatePoint(this.player, move, this.player.getZenith(), 180));
		}
		if (this.keyPressBuffer[65]) {
			this.player.setLocation(Math3D.rotatePoint(this.player, move, this.player.getZenith() - 90.0D, 0.0D));
		}
		if (this.keyPressBuffer[68]) {
			this.player.setLocation(Math3D.rotatePoint(this.player, move, this.player.getZenith() + 90.0D, 0.0D));
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
