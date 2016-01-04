package graphics.basicShapes;

import graphics.genMath.Math3D;
import graphics.only3d.Camera;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

public class Point3D {
	private double x;
	private double y;
	private double z;

	public Point3D() {
	}

	public Point3D(double x, double y, double z) {
		this.setX(x);
		this.setY(y);
		this.setZ(z);
	}

	public Point3D(Point3D p) {
		this(p.x, p.y, p.z);
	}

	public double dotProduct(Point3D p) {
		return p.getX() * getX() + p.getY() * getY() + p.getZ() * getZ();
	}

	public Point3D subtractFrom(Point3D p) {
		return new Point3D(p.getX() - getX(), p.getY() - getY(), p.getZ() - getZ());
	}

	public Point3D addTo(Point3D p) {
		return new Point3D(p.getX() + getX(), p.getY() + getY(), p.getZ() + getZ());
	}

	public double getDotProduct(Point3D p) {
		return (p.getX() * getX() + p.getY() * getY() + p.getZ() * getZ());
	}

	public Point getDrawPoint(Camera c) {
		return getCameraPoint(c).getDirectDrawPoint(c);
	}

	public Point getDirectDrawPoint(Camera c) {
		return new Point((int) (c.getDrawXFor(this) + 0.5), (int) (c.getDrawYFor(this) + 0.5));
	}

	public static Point3D getPointBetween(Point3D p1, Point3D p2, double dist) {
		return new Point3D(p1.getX() + dist * (p2.getX() - p1.getX()), p1.getY() + dist * (p2.getY() - p1.getY()), p1.getZ() + dist * (p2.getZ() - p1.getZ()));
	}

	public static Point3D getRandomPoint(int range) {
		return new Point3D((int) (Math.random() * range * 2) - range, (int) (Math.random() * range * 2) - range, (int) (Math.random() * range * 2) - range);
	}

	public Point3D getCameraPoint(Vector3D c) {
		Point3D par = Math3D.getRotatedPoint(c, this, -c.getZenith(), 0);
		Point3D cl = c.getPerpendicularLine().getPointClosestTo(par);
		return c.subtractFrom(Math3D.getRotatedPoint(cl, par, 0, -c.getAzumith()));
	}

	public double getDistanceTo(Point3D p) {
		return Math3D.getDistance(this, p);
	}

	public void draw(Graphics g, Camera c, Color color) {
		g.setColor(color);
		Point p = getDrawPoint(c);
		g.drawLine(p.x, p.y, p.x, p.y);
	}

	public void setX(double x) {
		this.x = x;
	}

	public void translate(double xt, double yt, double zt) {
		setX(getX() + xt);
		setY(getY() + yt);
		setZ(getZ() + zt);
	}

	public void setLocation(double nx, double ny, double nz) {
		setX(nx);
		setY(ny);
		setZ(nz);
	}

	public void setLocation(Point3D p) {
		setLocation(p.getX(), p.getY(), p.getZ());
	}

	public Point3D clone() {
		return new Point3D(x, y, z);
	}

	public double getX() {
		return x;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getY() {
		return y;
	}

	public void setZ(double z) {
		this.z = z;
	}

	public double getZ() {
		return z;
	}

	public boolean equalsRounded(Point3D p) {
		return (Math.round(p.getX()) == Math.round(getX()) && Math.round(p.getY()) == Math.round(getY()) && Math.round(p.getZ()) == Math.round(getZ()));
	}

	public boolean equals(Object o) {
		if (!(o instanceof Point3D)) {
			return false;
		}
		Point3D p = (Point3D) o;
		return (p.getX() == getX() && p.getY() == getY() && p.getZ() == getZ());
	}

	public boolean almostEquals(Point3D p) {
		return (Math.abs(p.getX() - getX()) < 0.0001 && Math.abs(p.getY() - getY()) < 0.0001 && Math.abs(p.getZ() - getZ()) < 0.0001);
	}

	public String toString() {
		return "Point3D[" + getX() + "," + getY() + "," + getZ() + "]";
	}

	public int hashCode() {
		return (int) ((x % 1000) * 1000000 + (y % 1000) * 1000 + (z % 1000));
	}

	public Point3D multiply(double t) {
		return new Point3D(getX() * t, getY() * t, getZ() * t);
	}
	
	public void scale(double magnitude) {
		double adj = magnitude / getMagnitude();
		x = x * adj;
		y = y * adj;
		z = z * adj;
	}

	public double getMagnitude() {
		return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2));
	}
}
