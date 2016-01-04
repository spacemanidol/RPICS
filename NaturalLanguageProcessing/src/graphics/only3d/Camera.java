package graphics.only3d;

import graphics.basicShapes.Point3D;
import graphics.basicShapes.Vector3D;

public class Camera extends Vector3D {
	private int fieldWidth;
	private int fieldHeight;

	public double zmult = 0.01;
	private double distmult = 5.0;

	public Camera() {
	}

	public Camera(double x, double y, double z) {
		super(x, y, z);
	}

	public Camera(double x, double y, double z, double zen, double azi) {
		super(x, y, z, zen, azi);
	}

	public double getDrawDistFor(Point3D p) {
		return p.getZ() * this.zmult;
	}

	public double getDrawXFor(Point3D p) {
		return (p.getX() / getDrawDistFor(p)) * this.distmult + getFieldWidth()
				/ 2;
	}

	public double getDrawYFor(Point3D p) {
		return (p.getY() / getDrawDistFor(p)) * this.distmult
				+ getFieldHeight() / 2;
	}

	public void setFieldHeight(int fieldHeight) {
		this.fieldHeight = fieldHeight;
	}

	public int getFieldHeight() {
		return this.fieldHeight;
	}

	public void setFieldWidth(int fieldWidth) {
		this.fieldWidth = fieldWidth;
	}

	public int getFieldWidth() {
		return this.fieldWidth;
	}
}