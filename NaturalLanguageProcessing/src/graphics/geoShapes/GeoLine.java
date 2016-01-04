package graphics.geoShapes;

import graphics.basicShapes.Point3D;

public class GeoLine {
	Point3D init;
	double xm;
	double ym;
	double zm;

	public GeoLine(Point3D a, Point3D b) {
		deriveLine(this, a, b);
	}

	public Point3D getContainedPoint(double l) {
		return new Point3D(init.getX() + l * xm, init.getY() + l * ym,
				init.getZ() + l * zm);
	}

	public boolean equals(Object o) {
		if (!(o instanceof GeoLine)) {
			return false;
		}
		GeoLine l = (GeoLine) o;
		return this.contains(l.init) && parallelTo(l);
	}

	public boolean parallelTo(GeoLine l) {
		boolean xmsame = false;
		if (xm == 0 && l.xm == 0) {
			xmsame = true;
		}
		if (xm == 0 || l.xm == 0 && xm != l.xm) {
			return false;
		}
		boolean ymsame = false;
		if (ym == 0 && l.ym == 0) {
			ymsame = true;
		}
		if (ym == 0 || l.ym == 0 && ym != l.ym) {
			return false;
		}
		boolean zmsame = false;
		if (zm == 0 && l.zm == 0) {
			zmsame = true;
		}
		if (zm == 0 || l.zm == 0 && zm != l.zm) {
			return false;
		}
		return (xmsame || ymsame || xm / l.xm == ym / l.ym)
				&& (zmsame || ymsame || ym / l.ym == zm / l.zm)
				&& (xmsame || zmsame || zm / l.zm == xm / l.xm);
	}

	public Point3D getPointClosestTo(Point3D p) {
		Point3D line = new Point3D(xm, ym, zm);
		Point3D op = init.subtractFrom(p);
		double a = op.getDotProduct(line);
		return this.getContainedPoint(a);
	}

	public double getMinimumDistanceTo(Point3D p) {
		Point3D helper = new Point3D(init.getX() + xm, init.getY() + ym,
				init.getZ() + zm);
		double d1;
		double d2 = init.getDistanceTo(helper);
		double d3;
		double td1 = helper.getDistanceTo(p);
		double td2 = init.getDistanceTo(p);
		if (td1 < td2) {
			d1 = td2;
			d3 = td1;
		} else {
			d1 = td1;
			d3 = td2;
		}
		double a2 = Math
				.toDegrees(Math.acos((Math.pow(d2, 2) + Math.pow(d3, 2) - Math
						.pow(d1, 2)) / (2 * d2 * d3)));
		double a5 = 180 - a2;
		return Math.sin(Math.toRadians(a5)) * d3;
	}

	public boolean contains(Point3D p) {
		return getMinimumDistanceTo(p) < 0.0000000000000001;
	}

	private void deriveLine(GeoLine line, Point3D a, Point3D b) {
		line.init = a;
		double d = a.getDistanceTo(b);
		line.xm = (b.getX() - a.getX()) / d;
		line.ym = (b.getY() - a.getY()) / d;
		line.zm = (b.getZ() - a.getZ()) / d;
	}

	public String toString() {
		return "GeoLine[x=" + init.getX() + "+" + xm + "t" + " , " + "y="
				+ init.getY() + "+" + ym + "t" + " , " + "z=" + init.getZ()
				+ "+" + zm + "t" + "]";
	}

}
