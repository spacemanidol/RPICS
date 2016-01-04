package graphics.genMath;

import graphics.basicShapes.*;

public class Math3D {

	public static double getAzimuthFrom(Point3D orig, Point3D p) {
		double xd = orig.getX() - p.getX();
		double yd = orig.getY() - p.getY();
		double zd = orig.getZ() - p.getZ();
		if (isZero(xd)) {
			xd = 0;
		}
		if (isZero(yd)) {
			yd = 0;
		}
		if (isZero(zd)) {
			zd = 0;
		}
		double ad = Math.sqrt(Math.pow(xd, 2) + Math.pow(zd, 2));
		if (ad == 0) {
			return 90 * -(Math.abs(yd) / yd);
		}
		return -Math.toDegrees(Math.atan(yd / ad));
	}

	public static double getZenithFrom(Point3D orig, Point3D p) {
		double xd = orig.getX() - p.getX();
		double zd = orig.getZ() - p.getZ();
		if (isZero(xd)) {
			xd = 0;
		}
		if (isZero(zd)) {
			zd = 0;
		}
		if (zd == 0) {
			if (xd == 0) {
				return 0;
			}
			return 90 * -Math.abs(xd) / xd;
		}
		double ret = Math.toDegrees(Math.atan(Math.abs(xd / zd)));
		if (zd > 0 && xd < 0) {
			ret = 180 - ret;
		}
		if (zd > 0 && xd >= 0) {
			ret += 180;
		}
		if (xd > 0 && zd < 0) {
			ret = 360 - ret;
		}
		return ret;
	}

	private static boolean isZero(double xd) {
		return xd < 0.00000000001 && xd > -0.00000000001;
	}

	public static Point3D getRotatedPoint(Point3D orig, Point3D p, double zm,
			double am) {
		if (orig.almostEquals(p)) {
			return p;
		}
		double z = getZenithFrom(orig, p);
		double a = getAzimuthFrom(orig, p);
		if (z >= 180) {
			z -= 180;
			a = 180 - a;
		}
		Point3D ret = rotatePoint(orig, getDistance(orig, p), z + zm, -(a + am));
		return ret;
	}

	public static Point3D rotatePoint(Point3D orig, double length, double zen,
			double azi) {
		Point3D ret = new Point3D();
		ret.setX(length * Math.sin(Math.toRadians(zen))
				* Math.cos(Math.toRadians(azi)));
		ret.setY(-length * Math.sin(Math.toRadians(azi)));
		ret.setZ(length * Math.cos(Math.toRadians(zen))
				* Math.cos(Math.toRadians(azi)));
		ret = ret.addTo(orig);
		if (isZero(ret.getX())) {
			ret.setX(0);
		}
		if (isZero(ret.getY())) {
			ret.setY(0);
		}
		if (isZero(ret.getZ())) {
			ret.setZ(0);
		}
		return ret;
	}

	public static double round(double o, int places) {
		return o - (o % Math.pow(10, -places));
	}

	public static double getDistance(Point3D a, Point3D b) {
		return Math.sqrt(Math.pow(a.getX() - b.getX(), 2)
				+ Math.pow(a.getY() - b.getY(), 2)
				+ Math.pow(a.getZ() - b.getZ(), 2));
	}

	public static double getDistance(Point3D a, Line3D l) {
		double d1 = getDistance(a, l.getPointA());
		double d2 = getDistance(a, l.getPointB());
		double d3 = getDistance(l.getPointA(), l.getPointB());
		double s = (d1 + d2 + d3) / 2.0;
		return Math.sqrt(s * (s - d1) * (s - d2) * (s - d3)) / d3 * 2;
	}

	public static double getDistance(Point3D p, Polygon3D poly) {
		Point3D a = poly.getPoints().get(0);
		Point3D b = poly.getPoints().get(1);
		Point3D c = poly.getPoints().get(2);
		double d1 = getDistance(a, p);
		double d2 = getDistance(a, new Line3D(b, c));
		double d3 = getDistance(p, new Line3D(b, c));
		double s = (d1 + d2 + d3) / 2.0;
		return Math.sqrt(s * (s - d1) * (s - d2) * (s - d3)) / d3 * 2;
	}
}
