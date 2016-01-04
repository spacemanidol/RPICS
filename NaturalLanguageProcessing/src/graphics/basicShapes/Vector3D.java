package graphics.basicShapes;

import graphics.genMath.Math3D;
import graphics.geoShapes.GeoLine;

import java.util.ArrayList;

public class Vector3D extends Point3D {

	private double zen;
	private double azi;

	public Vector3D() {
	}

	public Vector3D(double x, double y, double z) {
		super(x, y, z);
	}

	public Vector3D(double x, double y, double z, double zen, double azi) {
		this(x, y, z);
		this.setZenith(zen);
		this.setAzimuth(azi);
	}

	public String toString() {
		return "Vector3D[" + getX() + "," + getY() + "," + getZ() + ","
				+ this.getZenith() + "," + this.getAzumith() + "]";
	}

	public GeoLine getPerpendicularLine() {
		return new GeoLine(this, Math3D.rotatePoint(this, 100, 90, 0));
	}

	Polygon3D perpPlane;

	public Polygon3D getPerpendicularPlane() {
		if (perpPlane != null) {
			return perpPlane;
		}
		Point3D r1 = Math3D.rotatePoint(this, 1000, getZenith() + 90, 0);
		Point3D r2 = Math3D.rotatePoint(this, 1000, getZenith(),
				-90 - this.getAzumith());
		Point3D r3 = Math3D.rotatePoint(this, 1000, getZenith() - 90, 0);
		Point3D r4 = Math3D.rotatePoint(this, 1000, getZenith(),
				90 - this.getAzumith());
		ArrayList<Point3D> rt = new ArrayList<Point3D>();
		rt.add(r1);
		rt.add(r2);
		rt.add(r3);
		rt.add(r4);
		return new Polygon3D(rt);
	}

	public void turnToFace(Point3D p) {
		this.setZenith(Math3D.getZenithFrom(this, p));
		this.setAzimuth(Math3D.getAzimuthFrom(this, p));
	}

	public void putWithinBounds() {
		// removed because it wasn't doing anything.
		// double z = getZenith() % 360;
		// if (maxZenithRot != 999 && maxZenithRot > 360 && z > maxZenithRot %
		// 360) {
		// z = maxZenithRot % 360;
		// }
		// this.zen = (Math.max(Math.min(z, maxZenithRot), minZenithRot));
		// double a = getAzumith() % 360;
		// if (maxAzumithRot != 999 && maxAzumithRot > 360 && z > maxAzumithRot
		// % 360) {
		// z = maxAzumithRot % 360;
		// }
		// this.azi = (Math.max(Math.min(a, maxAzumithRot), minAzumithRot));
	}

	public void rotateZenith(double degrees) {
		this.setZenith(getZenith() + degrees);
	}

	public void rotateAzumith(double degrees) {
		this.setAzimuth(getAzumith() + degrees);
	}

	public void setZenith(double xr) {
		this.zen = xr;
		putWithinBounds();
	}

	public double getZenith() {
		return zen;
	}

	public void setAzimuth(double yr) {
		this.azi = yr;
		putWithinBounds();
	}

	public double getAzumith() {
		return azi;
	}
}
