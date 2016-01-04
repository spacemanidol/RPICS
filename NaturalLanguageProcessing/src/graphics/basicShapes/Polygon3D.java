package graphics.basicShapes;

import graphics.genMath.Math3D;
import graphics.only3d.Camera;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.util.ArrayList;

public class Polygon3D {

	private ArrayList<Point3D> points;

	public Polygon3D() {
		setPoints(new ArrayList<Point3D>());
	}

	@SuppressWarnings("unchecked")
	public Polygon3D(ArrayList<Point3D> p) {
		setPoints((ArrayList<Point3D>) p.clone());
	}

	public Polygon3D(Point3D[] p) {
		ArrayList<Point3D> copy = new ArrayList<Point3D>();
		for (int i = 0; i < p.length; i++) {
			copy.add(p[i]);
		}
		setPoints(copy);
	}

	public void addPoint(Point3D p) {
		getPoints().add(p);
	}

	public void translateX(double a) {
		for (int i = 0; i < this.points.size(); i++) {
			points.get(i).setX(points.get(i).getX() + a);
		}
	}

	public void translateY(double a) {
		for (int i = 0; i < this.points.size(); i++) {
			points.get(i).setY(points.get(i).getY() + a);
		}
	}

	public void translateZ(double a) {
		for (int i = 0; i < this.points.size(); i++) {
			points.get(i).setZ(points.get(i).getZ() + a);
		}
	}

	public Polygon getRemovedYPolygon() {
		int[] xp = new int[this.points.size()];
		int[] zp = new int[this.points.size()];
		for (int i = 0; i < points.size(); i++) {
			xp[i] = (int) points.get(i).getX();
			zp[i] = (int) points.get(i).getZ();
		}
		return new Polygon(xp, zp, points.size());
	}

	public Polygon3D getRotated(Vector3D orig, double zen, double azi) {
		Polygon3D ret = new Polygon3D(points);
		for (int a = 0; a < points.size(); a++) {
			Point3D r = new Point3D(ret.getPoints().get(a));
			r = Math3D.getRotatedPoint(orig, r, zen, 0);
			r = Math3D.getRotatedPoint(orig.getPerpendicularLine().getPointClosestTo(r), r, 0, azi);
			ret.points.set(a, r);
		}
		return ret;
	}

	public void draw(Graphics go, Camera camera, Color color) {
		Graphics2D g = (Graphics2D) go;
		g.setColor(color);

		Point3D[] cameraPoints = new Point3D[getPoints().size()];
		for (int i = 0; i < cameraPoints.length; i++) {
			cameraPoints[i] = getPoints().get(i).getCameraPoint(camera);
		}

		for (int i = 0; i < getPoints().size(); i++) {
			Point3D p1c = cameraPoints[i];
			Point3D p2c = cameraPoints[(i + 1) % getPoints().size()];
			if (p1c.getZ() > 0 || p2c.getZ() > 0) {
				if (p2c.getZ() <= 0) {
					p2c = Point3D.getPointBetween(p1c, p2c, (p1c.getZ() - 0.1) / (p1c.getZ() - p2c.getZ()));
				} else if (p1c.getZ() <= 0) {
					p1c = Point3D.getPointBetween(p2c, p1c, (p2c.getZ() - 0.1) / (p2c.getZ() - p1c.getZ()));
				}
				Point p1d = p1c.getDirectDrawPoint(camera);
				Point p2d = p2c.getDirectDrawPoint(camera);
				g.drawLine(p1d.x, p1d.y, p2d.x, p2d.y);
			}
		}
	}

	public void fill(Graphics go, Camera c, Color color) {
		if (shouldFill(c)) {
			Graphics2D g = (Graphics2D) go;
			g.setColor(color);
			int[] px = new int[getPoints().size()];
			int[] py = new int[getPoints().size()];
			for (int i = 0; i < px.length; i++) {
				Point dpf = getPoints().get(i).getDrawPoint(c);
				px[i] = dpf.x;
				py[i] = dpf.y;
			}
			g.fill(new Polygon(px, py, px.length));
		}
	}

	private boolean shouldFill(Camera c) {
		for (int i = 0; i < this.points.size(); i++) {
			if (c.getDrawDistFor(points.get(i).getCameraPoint(c)) < 0) {
				return false;
			}
		}
		return true;
	}

	public void setPoints(ArrayList<Point3D> points) {
		this.points = points;
	}

	public ArrayList<Point3D> getPoints() {
		return points;
	}
}
