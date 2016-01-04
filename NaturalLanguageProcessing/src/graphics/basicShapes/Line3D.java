package graphics.basicShapes;

import graphics.only3d.Camera;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

public class Line3D {

	private Point3D pointA;
	private Point3D pointB;

	public Line3D() {
	}

	public Line3D(Point3D a, Point3D b) {
		this.setPointA(a);
		this.setPointB(b);
	}

	public double getDistanceTo(Point3D point) {
		double t = pointA.subtractFrom(point).dotProduct(
				pointB.subtractFrom(point))
				/ pointB.subtractFrom(pointA).getDistanceTo(
						new Point3D(0, 0, 0));
		if (t < 0) {
			return pointA.getDistanceTo(point);
		}
		if (t > 1) {
			return pointB.getDistanceTo(point);
		}
		return pointA.addTo(pointB.subtractFrom(pointA).multiply(t)).getDistanceTo(point);
	}

	public void draw(Graphics g, Camera c, Color color) {
		if (getPointA() == null || getPointB() == null) {
			return;
		}
		g.setColor(color);
		Point p1 = this.getPointA().getDrawPoint(c);
		Point p2 = this.getPointB().getDrawPoint(c);
		g.drawLine(p1.x, p1.y, p2.x, p2.y);
	}

	public void setPointA(Point3D pointA) {
		this.pointA = pointA;
	}

	public Point3D getPointA() {
		return pointA;
	}

	public void setPointB(Point3D pointB) {
		this.pointB = pointB;
	}

	public Point3D getPointB() {
		return pointB;
	}
}
