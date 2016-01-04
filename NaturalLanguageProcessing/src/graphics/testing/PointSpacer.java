package graphics.testing;

import graphics.basicShapes.Point3D;
import graphics.basicShapes.Vector3D;
import graphics.basicmodels.Line;
import graphics.basicmodels.Model;
import graphics.basicmodels.PointModel;
import graphics.genMath.Math3D;
import graphics.only3d.Camera;
import graphics.only3d.World3D;
import helpers.DelayHelper;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JRootPane;

@SuppressWarnings("serial")
public class PointSpacer extends JFrame implements KeyListener, MouseMotionListener, MouseListener, MouseWheelListener {

	private static boolean started = false;

	public static void main(String[] args) {
		new PointSpacer(400, 400, 20).continueRepainting();
	}

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
	Point3D[] points;
	Point3D[] velocities;
	double[][] tied;
	double desiredDistance = 10;
	double velocityMultiplier = 100;
	private int move = 100;

	public PointSpacer(final int width, final int height, final int displayDelay) {
		this.w = new World3D();
		this.player = new Vector3D(0, 0, -2000, 0, 0);
		this.setSize(width + DIFF, height + DIFF);
		camera = new Camera(0, -100, 0, 0, 0);
		tilt = 90;
		makeThings();
		JRootPane customPane = new JRootPane() {
			public void paint(Graphics g) {
				// if (!started) {
				// try {
				// Thread.sleep(10000);
				// } catch (InterruptedException e) {
				// e.printStackTrace();
				// }
				// started = true;
				// }
				long st = System.currentTimeMillis();
				BufferedImage buffer = (BufferedImage) createImage(getWidth(), getHeight());
				Graphics2D bg = (Graphics2D) buffer.getGraphics();
				adjustCamera();
				w.paint(bg, camera);

				bg.setColor(Color.black);
				bg.drawString("WASD keys move forward,left,back, and right. Shift moves down and Spacebar moves up. Click and drag to look around.", 20, 10);
				bg.drawString("Move speed is currently " + move + ". Scroll to change move speed. (Up is faster, down is slower)", 20, 25);

				g.drawImage(buffer, 0, 0, null);
				if (started) {
					for (int i = 0; i < points.length; i++) {
						// velocities[i].scale(velocities[i].getMagnitude() *
						// 0.75);
						for (int j = 0; j < points.length; j++) {
							if (i != j) {
								Point3D subtract = points[i].subtractFrom(points[j]);
								double dist = points[i].getDistanceTo(points[j]);
								double magnitude = Math.min(10, 5000 * velocityMultiplier / Math.pow(dist, 2));
								subtract.scale(-magnitude);
								velocities[i].translate(subtract.getX(), subtract.getY(), subtract.getZ());
								if (tied[i][j] > 0) {
									subtract = points[i].subtractFrom(points[j]);
									subtract.scale(Math.min(100, (0.001) * velocityMultiplier * dist));
									if (dist < tied[i][j]) {
										subtract.scale(-1);
									}
									velocities[i].translate(subtract.getX(), subtract.getY(), subtract.getZ());
								}
							}
						}
					}
					for (int i = 0; i < points.length; i++) {
						points[i].setLocation(points[i].addTo(velocities[i]));
						velocities[i] = velocities[i].multiply(0.9);
						// velocities[i] = velocities[i].multiply(0.5 +
						// Math.sin(Math.toRadians(frame*3)) / 2);
					}
				}
				checkForKeyPresses();
				DelayHelper.delayFor(displayDelay - (System.currentTimeMillis() - st));
			}
		};
		setContentPane(customPane);
		getComponent(0).addMouseMotionListener(this);
		getComponent(0).addMouseListener(this);
		getComponent(0).addMouseWheelListener(this);
		addKeyListener(this);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}

	private int randLocValue() {
		return (int) (Math.random() * 1000) - 500;
	}

	private void makeThings() {
		// points = new Model[512];
		// for (int i = 0; i < points.length; i++) {
		// points[i] = new PointModel(w, (int) (Math.random() * 1000), (int)
		// (Math.random() * 1000), (int) (Math.random() * 1000));
		// }
		// tied = new double[points.length][points.length];

//		makeTwistExample();
		makeCubes(7, 1, false);
		// makeMultipleDodecahedra(20, 4);
		// tieLine(100);
		// tieRandomly(200, 0.0075);
		// tieRing(500);
		// makeSquareGrid(20);
		velocities = new Point3D[points.length];
		for (int i = 0; i < points.length; i++) {
			velocities[i] = new Point3D();
		}
	}

	void makeSquareGrid(int sideLength) {
		makePoints(sideLength * sideLength);
		for (int i = 0; i < sideLength; i++) {
			for (int j = 0; j < sideLength; j++) {
				int cv = i * sideLength + j;
				if (i > 0) {
					tie(cv, (i - 1) * sideLength + j);
				}
				if (i < sideLength - 1) {
					tie(cv, (i + 1) * sideLength + j);
				}
				if (j > 0) {
					tie(cv, i * sideLength + j - 1);
				}
				if (j < sideLength - 1) {
					tie(cv, i * sideLength + j + 1);
				}
			}
		}
	}

	void makeTwistExample() {
		points = new Model[4];
		int dist = 50;
		points[0] = new PointModel(w, dist, dist, 0);
		points[1] = new PointModel(w, -dist, dist, 0);
		points[2] = new PointModel(w, -dist, -dist, 0);
		points[3] = new PointModel(w, dist, -dist, 0);
		tied = new double[points.length][points.length];
		tie(0, 1);
		tie(1, 3);
		tie(2, 3);
		tie(2, 0);
	}

	void makeMultipleDodecahedra(int num, int centerPoints) {
		makePoints(20 * num + centerPoints);
		for (int j = 0; j < num; j++) {
			for (int i = 0; i < centerPoints; i++) {
				tie(points.length - i - 1, j * 20);
			}
			for (int i = 0; i < 5; i++) {
				tie(j * 20 + i, j * 20 + (i + 1) % 5);
				tie(j * 20 + i, j * 20 + i + 5);
				tie(j * 20 + i + 5, j * 20 + i + 10);
				tie(j * 20 + (i + 1) % 5 + 5, j * 20 + i + 10);
				tie(j * 20 + i + 10, j * 20 + i + 15);
				tie(j * 20 + i + 15, j * 20 + (i + 1) % 5 + 15);
			}
		}
	}

	private void makePoints(int numPoints) {
		points = new Model[numPoints];
		for (int i = 0; i < points.length; i++) {
			points[i] = new PointModel(w, randLocValue(), randLocValue(), randLocValue());
		}
		tied = new double[points.length][points.length];
	}

	void makeDodecahedron() {
		makePoints(20);
		for (int i = 0; i < 5; i++) {
			tie(i, (i + 1) % 5);
			tie(i, i + 5);
			tie(i + 5, i + 10);
			tie((i + 1) % 5 + 5, i + 10);
			tie(i + 10, i + 15);
			tie(i + 15, (i + 1) % 5 + 15);
		}
	}

	void makeCubes(int edgeLength, int numCubes, boolean centerTether) {
		int cubeVolume = (int) Math.pow(edgeLength, 3);
		if (centerTether) {
			points = new Model[cubeVolume * numCubes + 1];
		} else {
			points = new Model[cubeVolume * numCubes];
		}
		for (int i = 0; i < points.length; i++) {
			points[i] = new PointModel(w, randLocValue(), randLocValue(), randLocValue());
		}
		tied = new double[points.length][points.length];
		for (int i = 0; i < numCubes; i++) {
			if (centerTether) {
				tie(i * cubeVolume, points.length - 1);
			}
			doCubeTying(edgeLength, i * cubeVolume);
		}
	}

	void doCubeTying(int edgeLength, int offset) {
		for (int a = 0; a < edgeLength; a++) {
			for (int b = 0; b < edgeLength; b++) {
				for (int c = 0; c < edgeLength; c++) {
					int cv = offset + a * edgeLength * edgeLength + b * edgeLength + c;
					if (cv < points.length) {
						int nv = offset + (a - 1) * edgeLength * edgeLength + b * edgeLength + c;
						if (a > 0 && nv >= 0 && nv < points.length) {
							tie(cv, nv);
						}
						nv = offset + (a + 1) * edgeLength * edgeLength + b * edgeLength + c;
						if (a < edgeLength - 1 && nv >= 0 && nv < points.length) {
							tie(cv, nv);
						}
						nv = offset + a * edgeLength * edgeLength + (b - 1) * edgeLength + c;
						if (b > 0 && nv >= 0 && nv < points.length) {
							tie(cv, nv);
						}
						nv = offset + a * edgeLength * edgeLength + (b + 1) * edgeLength + c;
						if (b < edgeLength - 1 && nv >= 0 && nv < points.length) {
							tie(cv, nv);
						}
						nv = offset + a * edgeLength * edgeLength + b * edgeLength + c - 1;
						if (c > 0 && nv >= 0 && nv < points.length) {
							tie(cv, nv);
						}
						nv = offset + a * edgeLength * edgeLength + b * edgeLength + c + 1;
						if (c < edgeLength - 1 && nv >= 0 && nv < points.length) {
							tie(cv, nv);
						}
					}
				}
			}
		}
	}

	void tieRing(int numPoints) {
		makePoints(numPoints);
		for (int i = 0; i < points.length; i++) {
			tie(i, (i + 1) % points.length);
		}
	}

	void tieLine(int numPoints) {
		makePoints(numPoints);
		for (int i = 0; i < points.length - 1; i++) {
			tie(i, i + 1);
		}
	}

	void tieRandomly(int numPoints, double probability) {
		makePoints(numPoints);
		for (int i = 0; i < points.length; i++) {
			for (int j = 0; j < points.length; j++) {
				if (i != j) {
					if (Math.random() < probability) {
						tie(i, j);
					}
				}
			}
		}
	}

	private void tie(int i, int j) {
		tied[i][j] = desiredDistance;
		tied[j][i] = desiredDistance;
		new Line(w, points[i], points[j]);
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

	public void checkForKeyPresses() {
		if (this.keyPressBuffer[KeyEvent.VK_W]) {
			this.player.setLocation(Math3D.rotatePoint(this.player, move, this.player.getZenith(), 0));
		}
		if (this.keyPressBuffer[KeyEvent.VK_S]) {
			this.player.setLocation(Math3D.rotatePoint(this.player, move, this.player.getZenith(), 180));
		}
		if (this.keyPressBuffer[KeyEvent.VK_A]) {
			this.player.setLocation(Math3D.rotatePoint(this.player, move, this.player.getZenith() - 90.0D, 0.0D));
		}
		if (this.keyPressBuffer[KeyEvent.VK_D]) {
			this.player.setLocation(Math3D.rotatePoint(this.player, move, this.player.getZenith() + 90.0D, 0.0D));
		}
		if (this.keyPressBuffer[KeyEvent.VK_SHIFT]) {
			player.setY(player.getY() + move);
		}
		if (this.keyPressBuffer[KeyEvent.VK_SPACE]) {
			player.setY(player.getY() - move);
		}
		double rot = 1;
		if (this.keyPressBuffer[KeyEvent.VK_RIGHT]) {
			this.spin += rot;
		}
		if (this.keyPressBuffer[KeyEvent.VK_LEFT]) {
			this.spin -= rot;
		}
		if (this.keyPressBuffer[KeyEvent.VK_UP]) {
			this.tilt -= rot;
		}
		if (this.keyPressBuffer[KeyEvent.VK_DOWN]) {
			this.tilt += rot;
		}
		if (keyPressBuffer[KeyEvent.VK_ENTER]) {
			started = true;
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

	public void mouseClicked(MouseEvent e) {
		// double minDist = Double.MAX_VALUE;
		// int minIndex = -1;
		// for (int i = 0; i < points.length; i++) {
		// Point3D cameraPoint = points[i].getCameraPoint(camera);
		// if (cameraPoint.getZ() > 0) {
		// Point drawPoint = cameraPoint.getDirectDrawPoint(camera);
		// double dist = Math.sqrt(Math.pow(drawPoint.x - e.getX(), 2) +
		// Math.pow(drawPoint.y - e.getY(), 2));
		// if (dist < minDist) {
		// minIndex = i;
		// minDist = dist;
		// }
		// }
		// }
		//
		// for (int i = 0; i < points.length; i++) {
		// if (i != minIndex) {
		// Point3D subtract = points[i].subtractFrom(points[minIndex]);
		// double dist = points[i].getDistanceTo(points[minIndex]);
		// double magnitude = 1000000 * velocityMultiplier / Math.pow(dist, 2);
		// subtract.scale(-magnitude);
		// velocities[i].translate(subtract.getX(), subtract.getY(),
		// subtract.getZ());
		// }
		// }
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
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

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		move -= e.getUnitsToScroll();
		if (move < 0) {
			move = 0;
		}
	}
}
